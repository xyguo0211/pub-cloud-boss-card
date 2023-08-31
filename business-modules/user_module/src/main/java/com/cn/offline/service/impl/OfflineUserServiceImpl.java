package com.cn.offline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Constant;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.entity.OfflineRoleMenuDo;
import com.cn.offline.entity.OfflineUserDo;
import com.cn.offline.entity.OnlineUserDo;
import com.cn.offline.mapper.OfflineUserMapper;
import com.cn.offline.service.IOfflineUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.common.OfflineConstants;
import com.pub.core.common.OnlineConstants;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.DateUtils;
import com.pub.core.utils.MD5Util;
import com.pub.core.utils.StringUtils;
import com.pub.redis.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 离线用户表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Slf4j
@Service
public class OfflineUserServiceImpl extends ServiceImpl<OfflineUserMapper, OfflineUserDo> implements IOfflineUserService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private OfflineRoleMenuServiceImpl offlineRoleMenuServiceImpl;
    @Autowired
    private OfflineRoleServiceImpl offlineRoleServiceImpl;
    @Autowired
    private OfflineMenuServiceImpl offlineMenuServiceImpl;

    @Value("${offline_short_token_redis_cache_time}")
    private  Long short_token_redis_cache_time ;

    @Resource
    private TokenProvider tokenProvider;

    public JSONObject login(OfflineUserDo req) throws  Exception{
        JSONObject js=new JSONObject();
        /**
         * 校验用户名和密码是否正确
         */
        String name = req.getName();
        QueryWrapper<OfflineUserDo> wq=new QueryWrapper<>();
        wq.eq("name",name);
        wq.last("limit 1");
        OfflineUserDo one_db_name = getOne(wq);
        if(one_db_name==null){
            throw new BusinessException("用戶不存在！");
        }

        Integer isBlack = one_db_name.getIsBlack();
        if(OnlineConstants.blockStatus.block==isBlack){
            throw new BusinessException("用户已被拉黑！");
        }
        Integer roleId = one_db_name.getRoleId();
        if(OfflineConstants.offlineRole.system!=roleId){
            //如果不是管理员
            Date startTime = one_db_name.getStartTime();
            Date endTime = one_db_name.getEndTime();
            if(startTime==null||endTime==null){
                throw new BusinessException("非管理员用户，请申请上线时间！");
            }
            Date date=new Date();
            if(!date.after(startTime)||!date.before(endTime)){
                throw new BusinessException("非管理员用户，请在管理制定时间内登陆账号！");
            }
        }

        String pwd = MD5Util.MD5Encode(req.getPwd(),"UTF-8");
        String err_count_name="offline_login_err_count_" + name;
        String cache_str = redisCache.getStringCache(err_count_name);
        if(StringUtils.isNotBlank(cache_str)){
            int  cache=Integer.valueOf(cache_str);
            if(5==cache){
                //如果是第五次输入密码错误
                throw new BusinessException("输入密码错误超过5次，请20分钟以后重试！");
            }
        }
        if(!one_db_name.getPwd().equals(pwd)){
            /**
             * 如果连续输入密码错误，账号必须锁定10分钟
             */
            Integer cache=1;
            if(StringUtils.isNotBlank(cache_str)){
                cache=Integer.valueOf(cache_str)+1;
            }
            redisCache.putCacheWithExpireTime(err_count_name,cache,60*20);
            throw new BusinessException("密码错误！");
        }else{
            redisCache.deleteCache(err_count_name);
        }


        /**
         * 生成token
         */
        User mUser=new User();
        Integer id = one_db_name.getId();
        mUser.setId(id);
        mUser.setLoginName(one_db_name.getName());
        mUser.setUserType(one_db_name.getRoleId());
        mUser.setNikeName(one_db_name.getNikeName());
        Date startTime = one_db_name.getStartTime();
        if(startTime!=null){
            mUser.setStartDate(startTime);
        }
        Date endTime = one_db_name.getEndTime();
        if(endTime!=null){
            mUser.setEndDate(endTime);
        }
        String jwt ="Bearer_offline" +  tokenProvider.createTokenNewONline(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        /**
         * 查詢用户权限
         * Set<String> permission = redisCache.getCache(Constant.REDIS_PERMISSION_CACHE_KEY+ jwt, Set.class);
         */
        List<Map> roleMeumList = offlineRoleMenuServiceImpl.getRoleMeumList(one_db_name.getRoleId());
        if(roleMeumList!=null&&roleMeumList.size()>0){
            Set<String> set=new HashSet();
            for (Map map : roleMeumList) {
                String menu_url = map.get("menu_url").toString();
                set.add(menu_url);
            }
            redisCache.putCacheWithExpireTime(Constant.REDIS_PERMISSION_CACHE_KEY+ jwt,JSONObject.toJSONString(set),short_token_redis_cache_time+5*60);
        }
        js.put("permissions",roleMeumList);
        one_db_name.setPwd(null);
        js.put("user",one_db_name);
        js.put("token",jwt);
        log.info(DateUtils.dateTimeNow()+"时间登录了系统{}",JSONObject.toJSONString(req));
        return js;
    }

    public JSONObject refreshToken(String old_jwt) throws Exception{
        JSONObject jsonObject=new JSONObject();
        User muser = redisCache.getCache(old_jwt,User.class);
        if(muser==null){
            //说明需要重新登录
            log.info("redis的token已过期，请重新登录  ===实际传参传参{}",old_jwt);
            return null;
        }
        //更换token
        QueryWrapper<OfflineUserDo> qw=new QueryWrapper<>();
        qw.eq("id", muser.getId());
        OfflineUserDo offlineUserDo = getOne(qw);
        if(offlineUserDo==null){
            //不存在,需要重新登录
            log.info("jwt的userid,查询不到用户id，请重新登录  ===实际传参传参{}",old_jwt);
            throw new BusinessException("jwt的userid,查询不到用户id，请重新登录！");
        }
        Integer isBlack = offlineUserDo.getIsBlack();
        if(isBlack!=null&&isBlack==OnlineConstants.blockStatus.block){
            //不存在,需要重新登录
            log.info("jwt的userid,查询到用户id，用户已被拉黑{}",old_jwt);
            throw new BusinessException("jwt的userid,查询到用户id，用户已被拉黑！");
        }
        Integer roleId = offlineUserDo.getRoleId();
        if(OfflineConstants.offlineRole.system!=roleId){
            //如果不是管理员
            Date startTime = offlineUserDo.getStartTime();
            Date endTime = offlineUserDo.getEndTime();
            if(startTime==null||endTime==null){
                throw new BusinessException("非管理员用户，请申请上线时间！");
            }
            Date date=new Date();
            if(!date.after(startTime)||!date.before(endTime)){
                throw new BusinessException("非管理员用户，请在管理制定时间内登陆账号！");
            }
        }
        /**
         * 生成token
         */
        User mUserNew=new User();
        Integer id = offlineUserDo.getId();
        mUserNew.setId(id);
        mUserNew.setLoginName(offlineUserDo.getName());
        mUserNew.setUserType(offlineUserDo.getRoleId());
        mUserNew.setNikeName(offlineUserDo.getNikeName());
        Date startTime = offlineUserDo.getStartTime();
        if(startTime!=null){
            mUserNew.setStartDate(startTime);
        }
        Date endTime = offlineUserDo.getEndTime();
        if(endTime!=null){
            mUserNew.setEndDate(endTime);
        }
        //生成新的token,然后将旧的数据放回
        String jwt = "Bearer_offline" + tokenProvider.createTokenNewONline(mUserNew);
        redisCache.putCacheWithExpireTime(jwt,mUserNew,short_token_redis_cache_time);
        //删掉过期token
        redisCache.deleteCache(old_jwt);
        /**
         * 查詢用户权限
         * Set<String> permission = redisCache.getCache(Constant.REDIS_PERMISSION_CACHE_KEY+ jwt, Set.class);
         */
        List<Map> roleMeumList = offlineRoleMenuServiceImpl.getRoleMeumList(offlineUserDo.getRoleId());
        if(roleMeumList!=null&&roleMeumList.size()>0){
            Set<String> set=new HashSet();
            for (Map map : roleMeumList) {
                String menu_url = map.get("menu_url").toString();
                set.add(menu_url);
            }
            redisCache.putCacheWithExpireTime(Constant.REDIS_PERMISSION_CACHE_KEY+ jwt,JSONObject.toJSONString(set),short_token_redis_cache_time+5*60);
            /**
             * 删掉过期权限配置
             */
            redisCache.deleteCache(Constant.REDIS_PERMISSION_CACHE_KEY+old_jwt);
        }
        jsonObject.put("permissions",roleMeumList);
        jsonObject.put("token",jwt);
        offlineUserDo.setPwd(null);
        jsonObject.put("user",offlineUserDo);
        //##缓存一段时间,避免高并发重复请求,因为会出现并发请求拿新的token来换token情况,缓存30秒
        String online_cache_jwt="offline_Cache_" + jwt;
        redisCache.putCacheWithExpireTime(online_cache_jwt,jsonObject.toJSONString(),30);
        return jsonObject;
    }


    public void changePassword(JSONObject req) throws  Exception{
        User currentUser = UserContext.getCurrentUser();
        Integer id = currentUser.getId();
        OfflineUserDo byId = getById(id);
        String oldPwd =  MD5Util.MD5Encode(req.getString("oldPwd"),"UTF-8");
        if(!byId.getPwd().equals(oldPwd)){
            throw new BusinessException("旧的密码验证错误！");
        }
        String newPwd = req.getString("newPwd");
        byId.setPwd(MD5Util.MD5Encode(newPwd,"UTF-8"));
        byId.setUpdateTime(new Date());
        updateById(byId);
        log.info(DateUtils.dateTimeNow()+"时间修改了自己密码{}==={}",JSONObject.toJSONString(req),JSONObject.toJSONString(currentUser));

    }

    public void addUser(OfflineUserDo req) throws Exception{
        String name = req.getName();
        if(StringUtils.isBlank(name)){
            throw new BusinessException("用户名不能为空！");
        }
        String pwd = req.getPwd();
        if(StringUtils.isBlank(pwd)){
            throw new BusinessException("密码不能为空！");
        }
        String nikeName = req.getNikeName();
        if(StringUtils.isBlank(nikeName)){
            throw new BusinessException("昵称不能为空！");
        }
        Integer roleId = req.getRoleId();
        if(roleId==null){
            throw new BusinessException("请选择新用户角色！");
        }
        req.setIsBlack(OfflineConstants.blockStatus.block_no);
        req.setCreateTime(new Date());
        req.setPwd(MD5Util.MD5Encode(pwd,"UTF-8"));
        save(req);
        User currentUser = UserContext.getCurrentUser();
        log.info(DateUtils.dateTimeNow()+"时间新增了一个用户{}==={}",JSONObject.toJSONString(req),JSONObject.toJSONString(currentUser));
    }

    public void isBlack(OfflineUserDo req) throws Exception{
        Integer id = req.getId();
        if(id==null){
            throw new BusinessException("请选择一条用户！");
        }

        Integer isBlack = req.getIsBlack();
        if(isBlack==null){
            throw new BusinessException("请选择拉黑类型！");
        }
        updateById(req);
        User currentUser = UserContext.getCurrentUser();
        log.info(DateUtils.dateTimeNow()+"时间操作了拉黑接口{}==={}",JSONObject.toJSONString(req),JSONObject.toJSONString(currentUser));
    }

    public List<OfflineUserDo> getPageList(OfflineUserDo req) {
        QueryWrapper<OfflineUserDo> wq=new QueryWrapper<>();
        String name = req.getName();
        if(StringUtils.isNotBlank(name)){
            wq.like("name", name);
        }

        String nikeName = req.getNikeName();

        if(StringUtils.isNotBlank(nikeName)){
            wq.like("nike_name", nikeName);
        }
        Integer isBlack = req.getIsBlack();

        if(isBlack!=null){
            wq.eq("is_black", isBlack);
        }
        Integer roleId = req.getRoleId();

        if(roleId!=null){
            wq.eq("role_id", roleId);
        }
        BaseController.startPage();
        wq.orderByDesc("update_time");
        List<OfflineUserDo> list = list(wq);
        for (OfflineUserDo offlineUserDo : list) {
            Integer roleId1 = offlineUserDo.getRoleId();
            if(roleId1!=null){
                OfflineRoleDo byId = offlineRoleServiceImpl.getById(roleId1);
                offlineUserDo.setRoleName(byId.getName());
                //0 不可以接单   1可以接单
                if(byId.getIsOrder()!=null&&byId.getIsOrder()==1){
                    offlineUserDo.setIsOrder("是");
                }else{
                    offlineUserDo.setIsOrder("否");
                }

            }
        }
        return list;
    }

    public void setWorkTime(JSONObject req) throws Exception{
        Integer id = req.getInteger("id");
        if(id==null){
            throw new BusinessException("请选择一条用户！");
        }
        String startTimeStr = req.getString("startTime");
        String endTimeStr = req.getString("endTime");
        if(StringUtils.isBlank(startTimeStr)||StringUtils.isBlank(endTimeStr)){
            throw new BusinessException("请设置开始和结束时间！");
        }
      Date startTime= DateUtils.parseDate(startTimeStr,DateUtils.YYYY_MM_DD_HH_MM_SS);
      Date endTime= DateUtils.parseDate(endTimeStr,DateUtils.YYYY_MM_DD_HH_MM_SS);
        if(startTime.after(endTime)){
            throw new BusinessException("开始时间必须大于结束时间！");
        }
        OfflineUserDo offlineUserDo=new OfflineUserDo();
        offlineUserDo.setId(id);
        offlineUserDo.setStartTime(startTime);
        offlineUserDo.setEndTime(endTime);
        updateById(offlineUserDo);
        User currentUser = UserContext.getCurrentUser();
        log.info(DateUtils.dateTimeNow()+"时间设置用户工作时间{}==={}",JSONObject.toJSONString(req),JSONObject.toJSONString(currentUser));
    }
}

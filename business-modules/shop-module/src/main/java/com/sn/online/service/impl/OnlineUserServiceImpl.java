package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Constant;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.exception.BusinessException;
import com.pub.core.utils.StringUtils;
import com.pub.redis.util.RedisCache;
import com.sn.online.common.OnlineConstants;
import com.sn.online.common.SendGmail;
import com.sn.online.common.SendGmailUtil;
import com.sn.online.entity.OnlineUserDo;
import com.sn.online.entity.dto.OnlineUserRegisterDto;
import com.sn.online.mapper.OnlineUserMapper;
import com.sn.online.service.IOnlineUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 在线用户表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Slf4j
@Service
public class OnlineUserServiceImpl extends ServiceImpl<OnlineUserMapper, OnlineUserDo> implements IOnlineUserService {
    
    @Resource
    private TokenProvider tokenProvider;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SendGmail sendGmail;

    @Value("${short_token_redis_cache_time}")
    private  Long short_token_redis_cache_time ;


    public void register(OnlineUserRegisterDto req) throws  Exception{
        String name = req.getName();
        /**
         * 校验用户是否存在
         */
        QueryWrapper<OnlineUserDo> wq=new QueryWrapper<>();
        wq.eq("name",name);
        wq.last("limit 1");
        OnlineUserDo one_db_name = getOne(wq);
        if(one_db_name!=null){
            throw new BusinessException("The user already exists！");
        }
        /**
         * 校验验证码是否存在
         */
        String randomCode = req.getRandomCode();
        QueryWrapper<OnlineUserDo> wq_random=new QueryWrapper<>();
        wq_random.eq("my_invitation_code",randomCode);
        wq_random.last("limit 1");
        OnlineUserDo one_db_random_code = getOne(wq_random);
        if(one_db_random_code==null){
            //说明不存在
            throw new BusinessException(" The invitation code does not exist ！");
        }
        String emailCode = req.getEmailCode();
        /*String stringCache = redisCache.getStringCache(name);
        if(StringUtils.isBlank(stringCache)||!emailCode.equals(stringCache)){
            //说明不存在
            throw new BusinessException(" Verification code error！");
        }*/
        Date createTime = new java.util.Date();
        OnlineUserDo onlineUserDo_save=new OnlineUserDo();
        BeanUtils.copyProperties(req,onlineUserDo_save);
        onlineUserDo_save.setCreateTime(createTime);
        onlineUserDo_save.setUpdateTime(createTime);
        onlineUserDo_save.setIsBlack(OnlineConstants.blockStatus.block_no);
        onlineUserDo_save.setBalance("0");
        onlineUserDo_save.setMyInvitationCode(UUID.randomUUID().toString().replaceAll("-",""));
        onlineUserDo_save.setRole(OnlineConstants.onlineRole.system_no);
        save(onlineUserDo_save);
    }

    public JSONObject login(OnlineUserDo req) throws  Exception {
        JSONObject js=new JSONObject();
        /**
         * 校验用户名和密码是否正确
         */
        String name = req.getName();
        QueryWrapper<OnlineUserDo> wq=new QueryWrapper<>();
        wq.eq("name",name);
        wq.last("limit 1");
        OnlineUserDo one_db_name = getOne(wq);
        if(one_db_name==null){
            throw new BusinessException("The user does not exists！");
        }
        String pwd = req.getPwd();
        if(!one_db_name.getPwd().equals(req.getPwd())){
            throw new BusinessException("Login password error！");
        }
        Integer isBlack = req.getIsBlack();
        if(OnlineConstants.blockStatus.block==isBlack){
            throw new BusinessException("User has been blacklisted！");
        }
        /**
         * 生成token
         */
        User mUser=new User();
        Integer id = one_db_name.getId();
        mUser.setId(Long.valueOf(id));
        mUser.setLoginName(one_db_name.getName());
        mUser.setPassword(one_db_name.getPwd());
        String jwt ="Bearer " +  tokenProvider.createTokenNewONline(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        js.put("user",one_db_name);
        js.put("token",jwt);
        return js;
    }

    public void loginOut() {
    }

    public void sendEmail(String emailAddress) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        SendGmailUtil.gmailSender(emailAddress);
       /* sendGmail.sendEmai(randomNumber,emailAddress);*/
        /**
         * 10分钟过期
         */
        redisCache.putCacheWithExpireTime(emailAddress,randomNumber,1000*60*10);
    }

    public void changePassword(JSONObject req) throws Exception{
        User currentUser = UserContext.getCurrentUser();
        Long id = currentUser.getId();
        OnlineUserDo byId = getById(id);
        String oldPwd = req.getString("oldPwd");
        if(!byId.getPwd().equals(oldPwd)){
            throw new BusinessException("Old password verification error！");
        }
        String newPwd = req.getString("newPwd");
        byId.setPwd(newPwd);
        byId.setUpdateTime(new Date());
        updateById(byId);
    }

    public JSONObject refreshToken(String old_jwt) {
        JSONObject jsonObject=new JSONObject();
        String js_redis_str = redisCache.getStringCache(old_jwt);
        if(StringUtils.isBlank(js_redis_str)){
            //说明需要重新登录
            log.info("redis的token已过期，请重新登录  ===实际传参传参{}",old_jwt);
            return null;
        }

        //更换token
        JSONObject jsonObject_redis = JSONObject.parseObject(js_redis_str);
        QueryWrapper<OnlineUserDo> qw=new QueryWrapper<>();
        Object user_id = jsonObject_redis.get("user_id");
        qw.eq("id", user_id);
        OnlineUserDo user = getOne(qw);
        if(user==null){
            //不存在,需要重新登录
            log.info("jwt的userid,查询不到用户id，请重新登录  ===实际传参传参{}",old_jwt);
            return null;
        }
        User mUser=new User();
        mUser.setId(Long.valueOf(user.getId()));
        mUser.setLoginName(user.getName());
        //生成新的token,然后将旧的数据放回
        String jwt = "Bearer " + tokenProvider.createTokenNewONline(mUser);
        //这个时间一定要大于token自己过期时间
        Integer flag = jsonObject_redis.getInteger("flag");
        jsonObject.put("flag",flag);
        if(Constant.Online.LoginTimeOut.long_time==flag){
            //获取过期时间
            Long expire = RedisCacheUtils.getExpire(old_jwt);
            RedisCacheUtils.putStringExpires(jwt,js_redis_str,expire,TimeUnit.SECONDS);
            long l = new Date().getTime() + expire-2000;
            jsonObject.put("expire_time",l);
        }else{
            RedisCacheUtils.putStringExpires(jwt,js_redis_str,short_token_redis_cache_time,TimeUnit.SECONDS);
        }
        //删掉过期token
        RedisCacheUtils.removeKey(old_jwt);
        jsonObject.put("token",jwt);
        jsonObject.put("user",user);
        //##缓存一段时间,避免高并发重复请求,因为会出现并发请求拿新的token来换token情况,缓存30秒
        String online_cache_jwt="online_Cache_" + jwt;
        RedisCacheUtils.putStringExpires(online_cache_jwt,jsonObject.toJSONString(),30,TimeUnit.SECONDS);
        return jsonObject;
    }
}

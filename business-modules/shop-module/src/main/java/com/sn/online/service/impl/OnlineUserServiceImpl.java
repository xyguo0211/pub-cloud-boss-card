package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Constant;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OnlineConstants;
import com.pub.core.exception.BusinessException;
import com.pub.core.utils.StringUtils;
import com.pub.redis.util.RedisCache;

import com.sn.online.entity.OnlineUserDo;
import com.sn.online.entity.dto.OnlineUserRegisterDto;
import com.sn.online.mapper.OnlineUserMapper;
import com.sn.online.service.IOnlineUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sn.online.utils.SendGmail;
import com.sn.online.utils.SendGmailUtil;
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
        String stringCache = redisCache.getStringCache(name);
        if(StringUtils.isBlank(stringCache)||!emailCode.equals(stringCache)){
            //说明不存在
            throw new BusinessException(" Verification code error！");
        }
        Date createTime = new Date();
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
        Integer isBlack = one_db_name.getIsBlack();
        if(OnlineConstants.blockStatus.block==isBlack){
            throw new BusinessException("User has been blacklisted！");
        }
        String pwd = req.getPwd();
        String err_count_name="login_err_count_" + name;
        String cache_str = redisCache.getStringCache(err_count_name);
        if(StringUtils.isNotBlank(cache_str)){
           int  cache=Integer.valueOf(cache_str);
            if(5==cache){
                //如果是第五次输入密码错误
                throw new BusinessException("Password input error for five consecutive times, account locked for 20 minutes！");
            }
        }
        if(!one_db_name.getPwd().equals(req.getPwd())){
            /**
             * 如果连续输入密码错误，账号必须锁定10分钟
             */
            Integer cache=1;
            if(StringUtils.isNotBlank(cache_str)){
                cache=Integer.valueOf(cache_str)+1;
            }
            redisCache.putCacheWithExpireTime(err_count_name,cache,60*20);
            throw new BusinessException("Login password error！");
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
        String jwt ="Bearer " +  tokenProvider.createTokenNewONline(mUser);
        redisCache.putCacheWithExpireTime(jwt,mUser,short_token_redis_cache_time);
        one_db_name.setPwd(null);
        js.put("user",one_db_name);
        js.put("token",jwt);
        return js;
    }

    public void loginOut() {
    }

    public void sendEmail(String emailAddress) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
      /*  SendGmailUtil.gmailSender(emailAddress);*/
       /* sendGmail.sendEmai(randomNumber,emailAddress);*/
        /**
         * 10分钟过期
         */
        redisCache.putCacheWithExpireTime(emailAddress,"1111",1000*60*10);
    }
    public void sendEmailBank(String emailAddress) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
      /*  SendGmailUtil.gmailSender(emailAddress);*/
       /* sendGmail.sendEmai(randomNumber,emailAddress);*/
        /**
         * 10分钟过期
         */
        redisCache.putCacheWithExpireTime(emailAddress+"_bank","1111",1000*60*10);
    }

    public void changePassword(JSONObject req) throws Exception{
        User currentUser = UserContext.getCurrentUser();
        Integer id = currentUser.getId();
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
        User muser = redisCache.getCache(old_jwt,User.class);
        if(muser==null){
            //说明需要重新登录
            log.info("redis的token已过期，请重新登录  ===实际传参传参{}",old_jwt);
            return null;
        }
        //更换token
        QueryWrapper<OnlineUserDo> qw=new QueryWrapper<>();
        qw.eq("id", muser.getId());
        OnlineUserDo user = getOne(qw);
        if(user==null){
            //不存在,需要重新登录
            log.info("jwt的userid,查询不到用户id，请重新登录  ===实际传参传参{}",old_jwt);
            return null;
        }
        Integer isBlack = user.getIsBlack();
        if(isBlack!=null&&isBlack==OnlineConstants.blockStatus.block){
            //不存在,需要重新登录
            log.info("jwt的userid,查询到用户id，用户已被拉黑{}",old_jwt);
            return null;
        }
        //生成新的token,然后将旧的数据放回
        String jwt = "Bearer " + tokenProvider.createTokenNewONline(muser);
        redisCache.putCacheWithExpireTime(jwt,muser,short_token_redis_cache_time);
        //删掉过期token
        redisCache.deleteCache(old_jwt);
        jsonObject.put("token",jwt);
        user.setPwd(null);
        jsonObject.put("user",user);
        //##缓存一段时间,避免高并发重复请求,因为会出现并发请求拿新的token来换token情况,缓存30秒
        String online_cache_jwt="online_Cache_" + jwt;
        redisCache.putCacheWithExpireTime(online_cache_jwt,jsonObject.toJSONString(),30);
        return jsonObject;
    }
}

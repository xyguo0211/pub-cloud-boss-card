package com.sn.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OnlineConstants;
import com.pub.core.exception.BusinessException;
import com.pub.redis.util.RedisCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rabb.shop.entity.OnlineUserBankAccountDo;
import rabb.shop.entity.OnlineUserDo;
import rabb.shop.mapper.OnlineUserBankAccountMapper;

import java.util.Date;

/**
 * <p>
 * 用户银行卡管理 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Service
public class OnlineUserBankAccountServiceImpl extends ServiceImpl<OnlineUserBankAccountMapper, OnlineUserBankAccountDo> implements IService<OnlineUserBankAccountDo> {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private OnlineUserServiceImpl onlineUserServiceImpl;


    public void addBankAccount(OnlineUserBankAccountDo req) throws Exception{
        /**
         * 校验验证码
         */
        String emailCode = req.getEmailCode();
        User currentUser = UserContext.getCurrentUser();
        Integer id = currentUser.getId();
        OnlineUserDo onlineUserDo = onlineUserServiceImpl.getById(id);
        String emailAddress = onlineUserDo.getName();
        String stringCache = redisCache.getStringCache(emailAddress + "_bank");
        if(stringCache==null||!stringCache.equals(emailCode)){
            throw new BusinessException("Email verification code error !");
        }
        String bankName = req.getBankName();
        String bankAccountNumber = req.getBankAccountNumber();
        QueryWrapper<OnlineUserBankAccountDo> wq=new QueryWrapper<>();
        wq.eq("bank_name",bankName);
        wq.eq("bank_account_number",bankAccountNumber);
        wq.eq("user_id",currentUser.getId());
        wq.last("limit 1");
        OnlineUserBankAccountDo one = getOne(wq);
        if(one!=null){
            throw new BusinessException(" The bank card has been submitted! ！");
        }
        req.setCreateTime(new Date());
        req.setDeleteStatus(OnlineConstants.deleteStats.delete_no);
        req.setUserId(currentUser.getId());
        req.setLoginName(currentUser.getLoginName());
        save(req);

    }
}

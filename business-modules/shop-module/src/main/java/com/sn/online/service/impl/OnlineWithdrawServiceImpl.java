package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OnlineConstants;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import com.sn.online.config.FilePathOnlineConfig;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rabb.shop.entity.OnlineUserBankAccountDo;
import rabb.shop.entity.OnlineUserDo;
import rabb.shop.entity.OnlineWithdrawDo;
import rabb.shop.entity.OnlineWithdrawImageDo;
import rabb.shop.mapper.OnlineWithdrawMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 提现管理 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Service
public class OnlineWithdrawServiceImpl extends ServiceImpl<OnlineWithdrawMapper, OnlineWithdrawDo> implements IService<OnlineWithdrawDo> {

    @Autowired
    private OnlineUserServiceImpl onlineUserService;
    @Autowired
    private OnlineUserBankAccountServiceImpl onlineUserBankAccountServiceImpl;


    @Autowired
    private OnlineWithdrawImageServiceImpl onlineWithdrawImageServiceImpl;

    @Autowired
    private FilePathOnlineConfig filePathOnlineConfig;

    public void addOnlineWithdraw(OnlineWithdrawDo req) throws Exception{
        /**
         * 只允许有一次正在提交的记录
         */
        User currentUser = UserContext.getCurrentUser();
        Integer userId = currentUser.getId();
        QueryWrapper<OnlineWithdrawDo> wq_check=new QueryWrapper<>();
        wq_check.eq("user_id",userId);
        wq_check.eq("status",OnlineConstants.DrawStats.initial);
        wq_check.last("limit 1");
        OnlineWithdrawDo one = getOne(wq_check);
        if(one!=null){
            throw new BusinessException("  There is already an incomplete withdrawal transaction  ！");
        }
        OnlineUserDo byId = onlineUserService.getById(userId);
        String balance = byId.getBalance();
        String drawalFee = req.getDrawalFee();
        if(Double.valueOf(balance)<Double.valueOf(drawalFee)){
            throw new BusinessException("   Withdrawal amount, please do not exceed the balance   ！");
        }
        Integer bankId = req.getBankId();
        OnlineUserBankAccountDo onlineUserBankAccountDo = onlineUserBankAccountServiceImpl.getById(bankId);
        if(onlineUserBankAccountDo!=null){
            req.setBankName(onlineUserBankAccountDo.getBankName());
            req.setBankAccountName(onlineUserBankAccountDo.getBankAccountName());
            req.setBankAccountNumber(onlineUserBankAccountDo.getBankAccountNumber());
        }
        req.setCreateTime(new Date());
        req.setStatus(OnlineConstants.DrawStats.initial);
        req.setUserId(userId);
        req.setUserName(byId.getNikeName());
        save(req);
    }

    public List<OnlineWithdrawDo> getPageList(JSONObject req) {
        String startTime = req.getString("startTime");
        String endTime = req.getString("endTime");
        User currentUser = UserContext.getCurrentUser();
        QueryWrapper<OnlineWithdrawDo> wq=new QueryWrapper<>();
        if(StringUtils.isNotBlank(startTime)){
            wq.ge("create_time",startTime);
        }
        if(StringUtils.isNotBlank(endTime)){
            wq.lt("create_time",endTime);
        }
        wq.eq("user_id",currentUser.getId());
        wq.orderByDesc("id");
        BaseController.startPage();
        List<OnlineWithdrawDo> list = list(wq);
        return list;
    }

    public OnlineWithdrawDo getDetail(Integer id) {
        OnlineWithdrawDo onlineWithdrawDo = getById(id);
        QueryWrapper<OnlineWithdrawImageDo> wq=new QueryWrapper<>();
        wq.eq("parent_id",id);
        List<OnlineWithdrawImageDo> list = onlineWithdrawImageServiceImpl.list(wq);
        if(list!=null){
            for (OnlineWithdrawImageDo onlineWithdrawImageDo : list) {
                onlineWithdrawImageDo.setImageUrl(filePathOnlineConfig.getBaseUrl()+"/"+onlineWithdrawImageDo.getImageUrl());
            }
        }
        onlineWithdrawDo.setListOnlineWithdrawImageDo(list);
        return onlineWithdrawDo;
    }
}

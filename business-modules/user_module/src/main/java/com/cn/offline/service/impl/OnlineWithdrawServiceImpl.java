package com.cn.offline.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.OnlineTransactionHistoryDo;
import com.cn.offline.entity.OnlineUserDo;
import com.cn.offline.entity.OnlineWithdrawDo;
import com.cn.offline.entity.OnlineWithdrawImageDo;
import com.cn.offline.mapper.OnlineWithdrawMapper;
import com.cn.offline.service.IOnlineWithdrawService;
import com.pub.core.common.OnlineConstants;
import com.pub.core.common.OrderStatusEnum;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.CalculateUtil;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class OnlineWithdrawServiceImpl extends ServiceImpl<OnlineWithdrawMapper, OnlineWithdrawDo> implements IOnlineWithdrawService {


    @Autowired
    private OnlineUserServiceImpl onlineUserServiceImpl;

    @Autowired
    private OnlineTransactionHistoryServiceImpl onlineTransactionHistoryServiceImpl;

    @Autowired
    private OnlineWithdrawImageServiceImpl onlineWithdrawImageServiceImpl;

    @Autowired
    private OfflineFilePathOnlineConfig filePathOnlineConfig;

    public List<OnlineWithdrawDo> getPageList(JSONObject req) {
        String startTime = req.getString("startTime");
        String endTime = req.getString("endTime");
        QueryWrapper<OnlineWithdrawDo> wq=new QueryWrapper<>();
        if(StringUtils.isNotBlank(startTime)){
            wq.ge("create_time",startTime);
        }
        if(StringUtils.isNotBlank(endTime)){
            wq.lt("create_time",endTime);
        }
        Integer status = req.getInteger("status");
        if(status!=null){
            wq.eq("status",status);
        }
        wq.orderByDesc("id");
        BaseController.startPage();
        List<OnlineWithdrawDo> list = list(wq);
        return list;
    }

    public synchronized void submitDraw(OnlineWithdrawDo req) throws Exception{
        User currentUser = UserContext.getCurrentUser();
        Date createTime = new Date();
        req.setCreateTime(createTime);
        Integer status = req.getStatus();
        OnlineWithdrawDo onlineWithdrawDo_db = getById(req.getId());
        if(onlineWithdrawDo_db.getStatus()==OrderStatusEnum.DrawalFee_STATUS_SUCESS.getCode()){
            throw new BusinessException("该提现已完成交易！");
        }
        if(status== OrderStatusEnum.DrawalFee_STATUS_SUCESS.getCode()){
            //如果是成功需要生成交易记录的
            OnlineUserDo onlineUserDo = onlineUserServiceImpl.getById(onlineWithdrawDo_db.getUserId());
            String balance = onlineUserDo.getBalance();
            onlineWithdrawDo_db.setBeforeDrawalFee(balance);
            BigDecimal cal = CalculateUtil.cal(new StringBuilder(balance).append("-").append(onlineWithdrawDo_db.getDrawalFee()).toString());
            onlineWithdrawDo_db.setAftherDrawalFee(cal.toString());
            onlineUserDo.setBalance(cal.toString());
            onlineUserServiceImpl.updateById(onlineUserDo);
            /**
             * 生成一笔交易记录
             */
            OnlineTransactionHistoryDo onlineTransactionHistoryDo=new OnlineTransactionHistoryDo();
            onlineTransactionHistoryDo.setType(OrderStatusEnum.TR_TYPE_RECOD.getCode());
            onlineTransactionHistoryDo.setUserId(onlineWithdrawDo_db.getUserId());
            onlineTransactionHistoryDo.setTotalAmonunt(onlineWithdrawDo_db.getDrawalFee());
            onlineTransactionHistoryDo.setCreateTime(createTime);
            onlineTransactionHistoryServiceImpl.save(onlineTransactionHistoryDo);


        }
        onlineWithdrawDo_db.setStatus(req.getStatus());
        onlineWithdrawDo_db.setOfflineUserId(currentUser.getId());
        onlineWithdrawDo_db.setOfflineUserName(currentUser.getLoginName());
        onlineWithdrawDo_db.setMsg(req.getMsg());
        updateById(onlineWithdrawDo_db);
        List<OnlineWithdrawImageDo> listOnlineWithdrawImageDo = req.getListOnlineWithdrawImageDo();
        if(listOnlineWithdrawImageDo!=null&&listOnlineWithdrawImageDo.size()>0){
            for (OnlineWithdrawImageDo onlineWithdrawImageDo : listOnlineWithdrawImageDo) {
                onlineWithdrawImageDo.setParentId(onlineWithdrawDo_db.getId());
                String imageUrl = onlineWithdrawImageDo.getImageUrl();
                String[] split = imageUrl.split(filePathOnlineConfig.getBaseUrl());
                if(split.length>1){
                    onlineWithdrawImageDo.setImageUrl(split[1]);
                }else{
                    onlineWithdrawImageDo.setImageUrl(split[0]);
                }
                onlineWithdrawImageDo.setCreateTime(createTime);
            }
            onlineWithdrawImageServiceImpl.saveBatch(listOnlineWithdrawImageDo);
        }
    }
}

package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.OnlineOrderInfoReplyMapper;
import com.cn.offline.service.IOnlineOrderInfoReplyService;
import com.pub.core.common.OrderStatusEnum;
import com.pub.core.exception.BusinessException;
import com.pub.core.utils.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单图片回复表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Service
public class OnlineOrderInfoReplyServiceImpl extends ServiceImpl<OnlineOrderInfoReplyMapper, OnlineOrderInfoReplyDo> implements IOnlineOrderInfoReplyService {


    @Autowired
    private OfflineFilePathOnlineConfig filePathOnlineConfig;
    @Autowired
    private OnlineOrderInfoServiceImpl onlineOrderInfoService;
    @Autowired
    private OnlineOrderInfoReplyImageServiceImpl onlineOrderInfoReplyImageServiceImpl;
    @Autowired
    private OnlineTransactionHistoryServiceImpl onlineTransactionHistoryServiceImpl;
    @Autowired
    private OnlineUserServiceImpl onlineUserServiceImpl;

    @Transactional
    public void addOrderInfoReply(OnlineOrderInfoReplyDo req) throws  Exception{
        Date createTime = new Date();
        User currentUser = UserContext.getCurrentUser();
        Integer orderId = req.getOrderId();
        OnlineOrderInfoDo onlineOrderInfoDo = onlineOrderInfoService.getById(orderId);
        Integer orderStatus = onlineOrderInfoDo.getOrderStatus();
        if(OrderStatusEnum.TRACKING_STATUS_WAITING.getCode()!=orderStatus){
            throw  new BusinessException("该订单已关闭!请刷新页面");
        }
        onlineOrderInfoDo.setCompleteUserId(currentUser.getId());
        onlineOrderInfoDo.setCompleteUserName(currentUser.getLoginName());
        onlineOrderInfoDo.setOrderStatus(req.getStatus());
        onlineOrderInfoDo.setCompleteTime(createTime);
        req.setCreateTime(createTime);
        req.setReplyUserId(currentUser.getId());
        req.setReplyUserName(currentUser.getLoginName());
        save(req);
        List<OnlineOrderInfoReplyImageDo> listOnlineOrderInfoReplyImageDo = req.getListOnlineOrderInfoReplyImageDo();
        if(listOnlineOrderInfoReplyImageDo!=null&&listOnlineOrderInfoReplyImageDo.size()>0){
            for (OnlineOrderInfoReplyImageDo onlineOrderInfoReplyImageDo : listOnlineOrderInfoReplyImageDo) {
                onlineOrderInfoReplyImageDo.setCreateTime(createTime);
                onlineOrderInfoReplyImageDo.setReply_id(req.getId());
                String imageUrl = onlineOrderInfoReplyImageDo.getImageUrl();
                String[] split = imageUrl.split(filePathOnlineConfig.getBaseUrl());
                if(split.length>1){
                    onlineOrderInfoReplyImageDo.setImageUrl(split[1]);
                }else{
                    onlineOrderInfoReplyImageDo.setImageUrl(split[0]);
                }
            }
            onlineOrderInfoReplyImageServiceImpl.saveBatch(listOnlineOrderInfoReplyImageDo);
        }
        onlineOrderInfoService.updateById(onlineOrderInfoDo);
        /**
         * 生成一笔交易记录
         */
        if(req.getStatus()==OrderStatusEnum.TRACKING_STATUS_EXCEPTION.getCode()){
            OnlineUserDo onlineUserDo = onlineUserServiceImpl.getById(onlineOrderInfoDo.getUserId());
            String randomCode = onlineUserDo.getRandomCode();
            QueryWrapper<OnlineUserDo> wq_randomCode=new QueryWrapper<>();
            wq_randomCode.eq("my_invitation_code",randomCode);
            OnlineUserDo onlineUserDo_other = onlineUserServiceImpl.getOne(wq_randomCode);

            /**
             * 生成两笔交易记录,一笔是返现，一笔是卖卡
             */
            /**
             * 卖卡
             */
            List<OnlineTransactionHistoryDo> list=new ArrayList<>();
            OnlineTransactionHistoryDo entity=new OnlineTransactionHistoryDo();
            entity.setCreateTime(createTime);
            entity.setOrderId(onlineOrderInfoDo.getId());
            entity.setThirdId(onlineOrderInfoDo.getThirdId());
            entity.setTotalAmonunt(onlineOrderInfoDo.getTotalAmonuntFee());
            entity.setUserId(onlineUserDo.getId());
            entity.setCashBackFee(onlineOrderInfoDo.getCashBackFee());
            entity.setType(OrderStatusEnum.TR_TYPE_ORDER.getCode());
            entity.setThirdUserId(onlineUserDo_other.getId());
            entity.setThirdUserName(onlineUserDo_other.getName());
            list.add(entity);


            /**
             * 返现
             */
            OnlineTransactionHistoryDo entity2=new OnlineTransactionHistoryDo();
            entity2.setCreateTime(createTime);
            entity2.setOrderId(onlineOrderInfoDo.getId());
            entity2.setThirdId(onlineOrderInfoDo.getThirdId());
            entity2.setTotalAmonunt(onlineOrderInfoDo.getTotalAmonuntFee());
            entity2.setUserId(onlineUserDo_other.getId());
            entity2.setType(OrderStatusEnum.TR_TYPE_PERSON.getCode());
            entity2.setCashBackFee(onlineOrderInfoDo.getCashBackFee());
            entity2.setThirdUserId(onlineUserDo.getId());
            entity2.setThirdUserName(onlineUserDo.getName());
            list.add(entity2);

            onlineTransactionHistoryServiceImpl.saveBatch(list);
            /**
             * 更新个人用户账号
             */
            StringBuilder sb=new StringBuilder(onlineUserDo.getBalance()).append("+").append(onlineOrderInfoDo.getTotalAmonuntFee());
            BigDecimal cal = CalculateUtil.cal(sb.toString());
            onlineUserDo.setBalance(cal.toString());
            onlineUserServiceImpl.updateById(onlineUserDo);
            /**
             * 返现用户用户账号余额
             */
            StringBuilder sb_other=new StringBuilder(onlineUserDo_other.getBalance()).append("+").append(onlineOrderInfoDo.getCashBackFee());
            BigDecimal cal_other = CalculateUtil.cal(sb_other.toString());
            onlineUserDo_other.setBalance(cal_other.toString());
            onlineUserServiceImpl.updateById(onlineUserDo_other);
        }
    }
}

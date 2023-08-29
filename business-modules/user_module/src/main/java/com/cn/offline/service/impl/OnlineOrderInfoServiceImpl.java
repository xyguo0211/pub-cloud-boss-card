package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.OnlineOrderInfoMapper;
import com.cn.offline.service.IOnlineOrderInfoService;

import com.pub.core.common.OrderStatusEnum;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.CalculateUtil;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Service
public class OnlineOrderInfoServiceImpl extends ServiceImpl<OnlineOrderInfoMapper, OnlineOrderInfoDo> implements IOnlineOrderInfoService {

    @Autowired
    private OfflineUserServiceImpl offlineUserService;
    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;
    @Autowired
    private GoodSecondCountryServiceImpl goodSecondCountryServiceImpl;
    @Autowired
    private OfflineFilePathOnlineConfig offlineFilePathOnlineConfig;
    @Autowired
    private OnlineOrderInfoImageServiceImpl onlineOrderInfoImageServiceImpl;
    @Autowired
    private OnlineOrderInfoReplyServiceImpl onlineOrderInfoReplyServiceImpl;
    @Autowired
    private OnlineOrderInfoReplyImageServiceImpl onlineOrderInfoReplyImageServiceImpl;

    @Autowired
    private OnlineTransactionHistoryServiceImpl onlineTransactionHistoryServiceImpl;
    @Autowired
    private OnlineUserServiceImpl onlineUserServiceImpl;

    @Value("${shop.fee.rata}")
    private String shopFeeRata;

    public List<OnlineOrderInfoDo> getPageList(OnlineOrderInfoDo req) {
        User currentUser = UserContext.getCurrentUser();
        Integer id = currentUser.getId();
        OfflineUserDo byId = offlineUserService.getById(id);
        Integer roleId = byId.getRoleId();
        QueryWrapper<OnlineOrderInfoDo> wq=new QueryWrapper<>();
        if(roleId!=1){
            //管理员可以查看全部订单
            wq.eq("offline_user_id", id);
        }
        //9成功  -1失败  0 取消  1 初始化
        Integer orderStatus = req.getOrderStatus();
        if(orderStatus!=null){
            wq.eq("order_status", orderStatus);
        }
        //不需要  -1  审核中  0    审核 完成 9
        Integer isInspect = req.getIsInspect();
        if(isInspect!=null){
            wq.eq("is_inspect", isInspect);
        }
        String userName = req.getUserName();
        if(StringUtils.isNotBlank(userName)){
            wq.like("user_name", userName);
        }
        wq.orderByDesc("id");
        BaseController.startPage();
        List<OnlineOrderInfoDo> list = list(wq);
        for (OnlineOrderInfoDo onlineOrderInfoDo : list) {
            Integer firstId = onlineOrderInfoDo.getFirstId();
            GoodFirstMeumDo goodFirstMeumDo = goodFirstMeumServiceImpl.getById(firstId);
            onlineOrderInfoDo.setCardName(goodFirstMeumDo.getCardName());
            onlineOrderInfoDo.setCardImage(offlineFilePathOnlineConfig.getBaseUrl()+"/"+goodFirstMeumDo.getCardImgeUrl());
            Integer secondId = onlineOrderInfoDo.getSecondId();
            GoodSecondCountryDo goodSecondCountryDo = goodSecondCountryServiceImpl.getById(secondId);
            onlineOrderInfoDo.setCountryName(goodSecondCountryDo.getCountryName());
            onlineOrderInfoDo.setCountryImage(offlineFilePathOnlineConfig.getBaseUrl()+"/"+ goodSecondCountryDo.getCountryImage());
        }
        return list;
    }

    public OnlineOrderInfoDo getDetail(Integer id) {
        OnlineOrderInfoDo onlineOrderInfoDo = getById(id);
        QueryWrapper<OnlineOrderInfoImageDo> wq=new QueryWrapper<>();
        wq.eq("order_id",onlineOrderInfoDo.getId());
        List<OnlineOrderInfoImageDo> listOnlineOrderInfoImageDo = onlineOrderInfoImageServiceImpl.list(wq);
        for (OnlineOrderInfoImageDo onlineOrderInfoImageDo : listOnlineOrderInfoImageDo) {
            onlineOrderInfoImageDo.setImageUrl(offlineFilePathOnlineConfig.getBaseUrl()+"/"+onlineOrderInfoImageDo.getImageUrl());
        }
        onlineOrderInfoDo.setListOrderInfoImage(listOnlineOrderInfoImageDo);
        QueryWrapper<OnlineOrderInfoReplyDo> wq_reply=new QueryWrapper<>();
        wq_reply.eq("order_id",onlineOrderInfoDo.getId());
        OnlineOrderInfoReplyDo onlineOrderInfoReplyDo = onlineOrderInfoReplyServiceImpl.getOne(wq_reply);
        if(onlineOrderInfoReplyDo!=null){
            Integer id_reply = onlineOrderInfoReplyDo.getId();
            QueryWrapper<OnlineOrderInfoReplyImageDo> wq_image=new QueryWrapper<>();
            wq_image.eq("reply_id",id_reply);
            List<OnlineOrderInfoReplyImageDo> listOnlineOrderInfoReplyImageDo = onlineOrderInfoReplyImageServiceImpl.list(wq_image);
            for (OnlineOrderInfoReplyImageDo onlineOrderInfoReplyImageDo : listOnlineOrderInfoReplyImageDo) {
                onlineOrderInfoReplyImageDo.setImageUrl(offlineFilePathOnlineConfig.getBaseUrl()+"/"+onlineOrderInfoReplyImageDo.getImageUrl());
            }
            onlineOrderInfoReplyDo.setListOnlineOrderInfoReplyImageDo(listOnlineOrderInfoReplyImageDo);
        }
        onlineOrderInfoDo.setOnlineOrderInfoReplyDo(onlineOrderInfoReplyDo);
        return onlineOrderInfoDo;
    }

    public void reviewOrder(OnlineOrderInfoDo req) {
        Date createTime = new Date();
        User currentUser = UserContext.getCurrentUser();
        OnlineOrderInfoDo onlineOrderInfoDo = getById(req.getId());
        onlineOrderInfoDo.setIsInspect(OrderStatusEnum.IS_INSPECT_SUCESS.getCode());
        onlineOrderInfoDo.setInspectCompleteTime(createTime);
        onlineOrderInfoDo.setInspectUserId(currentUser.getId());
        onlineOrderInfoDo.setInspectUserName(currentUser.getLoginName());
        onlineOrderInfoDo.setInspectFee(req.getInspectFee());
        String inspectFee = onlineOrderInfoDo.getInspectFee();
        BigDecimal cal = CalculateUtil.cal(new StringBuilder(inspectFee).append("*").append(shopFeeRata).toString());
        onlineOrderInfoDo.setCashBackFee(cal.toString());
        onlineOrderInfoDo.setTransactionAmount(inspectFee);
        /**
         * 生成两笔交易记录
         */
        onlineOrderInfoReplyServiceImpl.opertorSucess(onlineOrderInfoDo);
        updateById(onlineOrderInfoDo);
    }




}

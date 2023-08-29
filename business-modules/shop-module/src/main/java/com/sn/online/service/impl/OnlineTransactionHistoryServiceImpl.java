package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sn.online.config.dto.OnlineTransactionHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rabb.shop.entity.GoodFirstMeumDo;
import rabb.shop.entity.OnlineOrderInfoDo;
import rabb.shop.entity.OnlineTransactionHistoryDo;
import rabb.shop.entity.OnlineWithdrawDo;
import rabb.shop.enumschool.OnlineOrderStatusEnum;
import rabb.shop.mapper.OnlineTransactionHistoryMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 交易记录 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-12
 */
@Service
public class OnlineTransactionHistoryServiceImpl extends ServiceImpl<OnlineTransactionHistoryMapper, OnlineTransactionHistoryDo> implements IService<OnlineTransactionHistoryDo> {

    @Autowired
    private OnlineOrderInfoServiceImpl onlineOrderInfoServiceImpl;
    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;
    @Autowired
    private OnlineWithdrawServiceImpl onlineWithdrawServiceImpl;

    public List<OnlineTransactionHistoryDto> getPageList(JSONObject req) {
        User currentUser = UserContext.getCurrentUser();
        QueryWrapper<OnlineTransactionHistoryDo> wq=new QueryWrapper<>();
        String startTime = req.getString("startTime");
        String endTime = req.getString("endTime");
        wq.eq("user_id",currentUser.getId());
        if(StringUtils.isNotBlank(startTime)){
            wq.ge("create_time",startTime);
        }
        if(StringUtils.isNotBlank(endTime)){
            wq.lt("create_time",endTime);
        }
        wq.orderByDesc("id");
        BaseController.startPage();
        List<OnlineTransactionHistoryDo> list = list(wq);
        List<OnlineTransactionHistoryDto> rtn=new ArrayList<>();
        for (OnlineTransactionHistoryDo onlineTransactionHistoryDo : list) {
            OnlineTransactionHistoryDto onlineTransactionHistoryDto=new OnlineTransactionHistoryDto();
            onlineTransactionHistoryDto.setId(onlineTransactionHistoryDo.getId());
            Integer orderId = onlineTransactionHistoryDo.getOrderId();
            if(orderId!=null){
                OnlineOrderInfoDo onlineOrderInfoDo = onlineOrderInfoServiceImpl.getById(orderId);
                if(onlineOrderInfoDo!=null){
                    Integer firstId = onlineOrderInfoDo.getFirstId();
                    GoodFirstMeumDo goodFirstMeumDo = goodFirstMeumServiceImpl.getById(firstId);
                    onlineTransactionHistoryDto.setCardName(goodFirstMeumDo.getCardName());
                }

            }

            onlineTransactionHistoryDto.setThirdUserName(onlineTransactionHistoryDo.getThirdUserName());
            Integer type = onlineTransactionHistoryDo.getType();
            if(OnlineOrderStatusEnum.TR_TYPE_ORDER.getCode()==type){
                onlineTransactionHistoryDto.setTotalAmonunt(onlineTransactionHistoryDo.getTotalAmonunt());
            } else if(OnlineOrderStatusEnum.TR_TYPE_PERSON.getCode()==type){
                //只有返现的是金额(不是提现)
                onlineTransactionHistoryDto.setTotalAmonunt(onlineTransactionHistoryDo.getCashBackFee());
            }else{
                //提现
                 onlineTransactionHistoryDto.setTotalAmonunt(onlineTransactionHistoryDo.getTotalAmonunt());
                Integer withdrawId = onlineTransactionHistoryDo.getWithdrawId();
                OnlineWithdrawDo onlineWithdrawDo = onlineWithdrawServiceImpl.getById(withdrawId);
                onlineTransactionHistoryDto.setBankName(onlineWithdrawDo.getBankName());
            }
            onlineTransactionHistoryDto.setType(onlineTransactionHistoryDo.getType());
            onlineTransactionHistoryDto.setCreateTime(onlineTransactionHistoryDo.getCreateTime());
            rtn.add(onlineTransactionHistoryDto);
        }
        return rtn;
    }
}

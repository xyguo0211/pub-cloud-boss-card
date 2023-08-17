package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineFilePathOnlineConfig;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.OnlineOrderInfoMapper;
import com.cn.offline.service.IOnlineOrderInfoService;

import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String userName = req.getUserName();
        if(StringUtils.isNotBlank(userName)){
            wq.eq("user_name", userName);
        }
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
}

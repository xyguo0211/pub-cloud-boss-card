package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.entity.*;
import com.cn.offline.mapper.OnlineUserMapper;
import com.cn.offline.service.IOnlineUserService;

import com.pub.core.util.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


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

    @Autowired
    private OnlineWithdrawServiceImpl onlineWithdrawServiceImpl;
    @Autowired
    private OnlineOrderInfoServiceImpl onlineOrderInfoServiceImpl;

    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;
    @Autowired
    private GoodSecondCountryServiceImpl goodSecondCountryServiceImpl;

    public List<OnlineUserDo> getPageList(OnlineUserDo onlineUserDo) {
        BaseController.startPage();
        QueryWrapper<OnlineUserDo> wq=new QueryWrapper<>();
        String name = onlineUserDo.getName();
        if(StringUtils.isNotBlank(name)){
            wq.and(wrapper -> {
                wrapper.like("name",name).or().like("nike_name",name);
            });

        }
        Integer isBlack = onlineUserDo.getIsBlack();
        if(isBlack!=null){
            wq.eq("is_black",isBlack);
        }
        Integer role = onlineUserDo.getRole();
        if(role!=null){
            wq.eq("role",role);
        }
        wq.orderByDesc("update_time");
        //测试提交
        List<OnlineUserDo> list = list(wq);
        for (OnlineUserDo userDo : list) {
            String randomCode = userDo.getRandomCode();
            QueryWrapper<OnlineUserDo> wq_one=new QueryWrapper<>();
            wq_one.eq("my_invitation_code",randomCode);
            OnlineUserDo db = getOne(wq_one);
            if(db!=null){
                userDo.setRandomCodeUse(db.getNikeName());
            }

        }
        return list;
    }

    public void setBlack(OnlineUserDo onlineUserDo) {
        onlineUserDo.setUpdateTime(new Date());
        updateById(onlineUserDo);
    }

    public List<OnlineWithdrawDo> getDraw(Integer userId) {
        QueryWrapper<OnlineWithdrawDo> wq=new QueryWrapper<>();
        wq.eq("user_id",userId);
        wq.orderByDesc("create_time");
        BaseController.startPage();
        List<OnlineWithdrawDo> list = onlineWithdrawServiceImpl.list(wq);
        return list;
    }

    public List<OnlineOrderInfoDo> getOrder(Integer userId) {
        QueryWrapper<OnlineOrderInfoDo> wq=new QueryWrapper<>();
        wq.eq("user_id",userId);
        wq.orderByDesc("create_time");
        BaseController.startPage();
        List<OnlineOrderInfoDo> list = onlineOrderInfoServiceImpl.list(wq);
        for (OnlineOrderInfoDo onlineOrderInfoDo : list) {
            GoodFirstMeumDo goodFirstMeumDo = goodFirstMeumServiceImpl.getById(onlineOrderInfoDo.getFirstId());
            onlineOrderInfoDo.setCardName(goodFirstMeumDo.getCardName());
            GoodSecondCountryDo goodSecondCountryDo = goodSecondCountryServiceImpl.getById(onlineOrderInfoDo.getSecondId());
            onlineOrderInfoDo.setCountryName(goodSecondCountryDo.getCountryName());
        }
        return list;
    }
}

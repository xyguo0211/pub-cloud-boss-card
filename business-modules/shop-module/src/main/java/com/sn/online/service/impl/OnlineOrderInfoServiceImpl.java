package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.AES;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OnlineConstants;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.utils.StringUtils;
import com.pub.core.util.controller.BaseController;
import com.sn.online.config.FilePathOnlineConfig;
import com.sn.online.entity.*;
import com.sn.online.entity.dto.OnlineOrderSubmitDto;
import com.sn.online.mapper.OnlineOrderInfoMapper;
import com.sn.online.service.IOnlineOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private GoodSecondCountryServiceImpl  goodSecondCountryServiceImpl;
    @Autowired
    private GoodThirdRateServiceImpl  goodThirdRateServiceImpl;
    @Autowired
    private GoodThirdCardTypeServiceImpl  goodThirdCardTypeServiceImpl;
    @Autowired
    private GoodFirstMeumEquirementsServiceImpl  goodFirstMeumEquirementsServiceImpl;
    @Autowired
    private GoodFirstMeumServiceImpl  goodFirstMeumServiceImpl;
    @Autowired
    private OnlineOrderInfoImageServiceImpl  onlineOrderInfoImageServiceImpl;
    @Autowired
    private OnlineOrderInfoReplyServiceImpl  onlineOrderInfoReplyServiceImpl;
    @Autowired
    private OnlineOrderInfoReplyImageServiceImpl  onlineOrderInfoReplyImageServiceImpl;

    @Autowired
    private FilePathOnlineConfig filePathOnlineConfig;

    public List<OnlineOrderInfoDo> getPageList(JSONObject req) throws Exception{
        User currentUser = UserContext.getCurrentUser();
        if(currentUser==null){
            throw  new BusinessException("Please log in !");
        }
        QueryWrapper<OnlineOrderInfoDo> wq=new QueryWrapper<>();
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
        List<OnlineOrderInfoDo> list = list(wq);
        return list;
    }

    public OnlineOrderInfoDo getDetailInfo(Integer id) {
        OnlineOrderInfoDo onlineOrderInfoDo = getById(id);
        QueryWrapper<OnlineOrderInfoImageDo> wq=new QueryWrapper<>();
        wq.eq("order_id",id);
        List<OnlineOrderInfoImageDo> list = onlineOrderInfoImageServiceImpl.list(wq);
        if(list!=null&&list.size()>0){
            for (OnlineOrderInfoImageDo onlineOrderInfoImageDo : list) {
                String imageUrl = onlineOrderInfoImageDo.getImageUrl();
                onlineOrderInfoImageDo.setImageUrl(filePathOnlineConfig.getBaseUrl()+imageUrl);
            }
            onlineOrderInfoDo.setListOrderInfoImage(list);
        }
        QueryWrapper<OnlineOrderInfoReplyDo> wq_reply=new QueryWrapper<>();
        wq_reply.eq("order_id",id);
        wq_reply.last("limit 1");
        OnlineOrderInfoReplyDo onlineOrderInfoReplyDo = onlineOrderInfoReplyServiceImpl.getOne(wq_reply);
        if(onlineOrderInfoReplyDo!=null){
            QueryWrapper<OnlineOrderInfoReplyImageDo> wq_OrderInfoReplyImage=new QueryWrapper<>();
            wq_OrderInfoReplyImage.eq("reply_id",onlineOrderInfoReplyDo.getId());
            List<OnlineOrderInfoReplyImageDo> list_OnlineOrderInfoReplyImageDo = onlineOrderInfoReplyImageServiceImpl.list(wq_OrderInfoReplyImage);
            if(list_OnlineOrderInfoReplyImageDo!=null&&list_OnlineOrderInfoReplyImageDo.size()>0){
                for (OnlineOrderInfoReplyImageDo onlineOrderInfoReplyImageDo : list_OnlineOrderInfoReplyImageDo) {
                    String imageUrl = onlineOrderInfoReplyImageDo.getImageUrl();
                    onlineOrderInfoReplyImageDo.setImageUrl(filePathOnlineConfig.getBaseUrl()+imageUrl);
                }
                onlineOrderInfoReplyDo.setListOnlineOrderInfoReplyImageDo(list_OnlineOrderInfoReplyImageDo);
            }
            onlineOrderInfoDo.setOnlineOrderInfoReplyDo(onlineOrderInfoReplyDo);
        }
        return onlineOrderInfoDo;
    }

    public GoodThirdRateDo openOrder(Integer third_id) {
        GoodThirdRateDo goodThirdRateDo = goodThirdRateServiceImpl.getById(third_id);
        Integer secondId = goodThirdRateDo.getSecondId();
        GoodSecondCountryDo goodSecondCountryDo = goodSecondCountryServiceImpl.getById(secondId);
        goodThirdRateDo.setGoodSecondCountryDo(goodSecondCountryDo);
        Integer firstId = goodThirdRateDo.getFirstId();
        GoodFirstMeumDo goodFirstMeumDo = goodFirstMeumServiceImpl.getById(firstId);
        QueryWrapper<GoodFirstMeumEquirementsDo> wq_equirements=new QueryWrapper<>();
        wq_equirements.eq("first_id",goodFirstMeumDo.getId());
        List<GoodFirstMeumEquirementsDo> listequirements = goodFirstMeumEquirementsServiceImpl.list(wq_equirements);
        if(listequirements!=null&&listequirements.size()>0){
            goodFirstMeumDo.setListEquirements(listequirements);
        }
        goodThirdRateDo.setGoodFirstMeumDo(goodFirstMeumDo);
        QueryWrapper<GoodThirdCardTypeDo> wq_car_type=new QueryWrapper<>();
        wq_car_type.eq("third_id",goodThirdRateDo.getId());
        List<GoodThirdCardTypeDo> list_GoodThirdCardTypeDo = goodThirdCardTypeServiceImpl.list(wq_car_type);
        if(list_GoodThirdCardTypeDo!=null&&list_GoodThirdCardTypeDo.size()>0){
            goodThirdRateDo.setListCardType(list_GoodThirdCardTypeDo);
        }
        return goodThirdRateDo;
    }

    public void submitOrder(OnlineOrderSubmitDto onlineOrderSubmitDto) {
        User currentUser = UserContext.getCurrentUser();
        OnlineOrderInfoDo onlineOrderInfoDo=new OnlineOrderInfoDo();
        BeanUtils.copyProperties(onlineOrderSubmitDto,onlineOrderInfoDo);
        Date createTime = new Date();
        onlineOrderInfoDo.setCreateTime(createTime);
        onlineOrderInfoDo.setUserId(currentUser.getId());
        onlineOrderInfoDo.setOrderStatus(OnlineConstants.orderStats.initial);
        save(onlineOrderInfoDo);
        List<String> images = onlineOrderSubmitDto.getImages();
        List<OnlineOrderInfoImageDo> list_OnlineOrderInfoImageDo=new ArrayList<>();
        if(images!=null&&images.size()>0){
            for (String image : images) {
                OnlineOrderInfoImageDo onlineOrderInfoImageDo=new OnlineOrderInfoImageDo();
                onlineOrderInfoImageDo.setOrderId(onlineOrderInfoDo.getId());
                onlineOrderInfoImageDo.setCreateTime(createTime);
                onlineOrderInfoImageDo.setImageUrl(image);
                list_OnlineOrderInfoImageDo.add(onlineOrderInfoImageDo);
            }
            onlineOrderInfoImageServiceImpl.saveBatch(list_OnlineOrderInfoImageDo);
        }

    }

}

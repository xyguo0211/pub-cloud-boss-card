package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pub.core.utils.StringUtils;
import com.pub.core.web.controller.BaseController;
import com.sn.online.entity.OnlineOrderInfoDo;
import com.sn.online.mapper.OnlineOrderInfoMapper;
import com.sn.online.service.IOnlineOrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    public List<OnlineOrderInfoDo> getPageList(JSONObject req) {

        QueryWrapper<OnlineOrderInfoDo> wq=new QueryWrapper<>();
        String startTime = req.getString("startTime");
        String endTime = req.getString("endTime");
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
}

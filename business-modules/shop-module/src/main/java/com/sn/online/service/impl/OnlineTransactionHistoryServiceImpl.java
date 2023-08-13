package com.sn.online.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import com.sn.online.entity.GoodFirstMeumDo;
import com.sn.online.entity.OnlineTransactionHistoryDo;
import com.sn.online.mapper.OnlineTransactionHistoryMapper;
import com.sn.online.service.IOnlineTransactionHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class OnlineTransactionHistoryServiceImpl extends ServiceImpl<OnlineTransactionHistoryMapper, OnlineTransactionHistoryDo> implements IOnlineTransactionHistoryService {

    public List<OnlineTransactionHistoryDo> getPageList(JSONObject req) {
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
        BaseController.startPage();
        List<OnlineTransactionHistoryDo> list = list(wq);
        return list;
    }
}

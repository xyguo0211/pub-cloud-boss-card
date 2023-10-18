package com.cn.school.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.school.config.Constant;
import com.cn.school.entity.*;
import com.cn.school.mapper.IntegralManageMapper;
import com.cn.school.mapper.UserMapper;
import com.cn.school.service.IIntegralManageService;
import com.cn.school.service.IUserService;
import com.pub.core.common.OnlineConstants;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.CalculateUtil;
import com.pub.core.utils.StringUtils;
import com.pub.redis.util.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Log4j2
@Service
public class IntegralManageServiceImpl extends ServiceImpl<IntegralManageMapper, IntegralManageDo> implements IIntegralManageService {


    public List<IntegralManageDo> getPageList(IntegralManageDo req) {
        QueryWrapper<IntegralManageDo> wq=new QueryWrapper<>();
        Integer userId = req.getUserId();
        if(userId!=null){
            wq.eq("user_id", userId);
        }
        Integer status = req.getStatus();
        if(status!=null){
            wq.eq("status",status);
        }
        String phone = req.getPhone();
        if(StringUtils.isNotBlank(phone)){
            wq.like("phone",phone);
        }
        String identityName = req.getIdentityName();
        if(StringUtils.isNotBlank(identityName)){
            wq.like("identity_name",identityName);
        }
        BaseController.startPage();
        List<IntegralManageDo> list = list(wq);
        return list;
    }
}

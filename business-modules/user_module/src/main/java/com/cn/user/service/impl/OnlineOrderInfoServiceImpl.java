package com.cn.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.user.entity.*;
import com.cn.user.entity.dto.OnlineOrderSubmitDto;
import com.cn.user.mapper.OnlineOrderInfoMapper;
import com.cn.user.service.IOnlineOrderInfoService;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;

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


}

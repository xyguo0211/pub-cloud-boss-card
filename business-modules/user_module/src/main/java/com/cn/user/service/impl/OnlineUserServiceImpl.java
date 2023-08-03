package com.cn.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.user.entity.OnlineUserDo;
import com.cn.user.entity.dto.OnlineUserRegisterDto;
import com.cn.user.mapper.OnlineUserMapper;
import com.cn.user.service.IOnlineUserService;
import com.pub.core.exception.BusinessException;
import com.pub.core.utils.StringUtils;
import com.pub.redis.util.RedisCache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;
import java.util.UUID;


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

}

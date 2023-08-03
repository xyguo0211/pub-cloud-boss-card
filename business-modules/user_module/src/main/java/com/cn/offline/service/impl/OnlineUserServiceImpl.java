package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.offline.entity.OnlineUserDo;
import com.cn.offline.mapper.OnlineUserMapper;
import com.cn.offline.service.IOnlineUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


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

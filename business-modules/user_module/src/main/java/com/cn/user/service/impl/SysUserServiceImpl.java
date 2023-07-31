package com.cn.user.service.impl;

import com.cn.auth.entity.User;
import com.cn.user.mapper.SysUserMapper;
import com.cn.user.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 登录帐号表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements SysUserService {


}

package com.cn.offline.service.impl;

import com.cn.offline.entity.OfflineUserDo;
import com.cn.offline.mapper.OfflineUserMapper;
import com.cn.offline.service.IOfflineUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 离线用户表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Service
public class OfflineUserServiceImpl extends ServiceImpl<OfflineUserMapper, OfflineUserDo> implements IOfflineUserService {

}

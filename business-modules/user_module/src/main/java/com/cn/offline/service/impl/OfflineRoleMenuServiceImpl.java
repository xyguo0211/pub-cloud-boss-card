package com.cn.offline.service.impl;

import com.cn.offline.entity.OfflineRoleMenuDo;
import com.cn.offline.mapper.OfflineRoleMenuMapper;
import com.cn.offline.service.IOfflineRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Service
public class OfflineRoleMenuServiceImpl extends ServiceImpl<OfflineRoleMenuMapper, OfflineRoleMenuDo> implements IOfflineRoleMenuService {

    @Autowired
   private OfflineRoleMenuMapper offlineRoleMenuMapper;

    public List<Map> getRoleMeumList(Integer roleId) {
        List<Map> roleMeumList = offlineRoleMenuMapper.getRoleMeumList(roleId);
        return roleMeumList;
    }
}

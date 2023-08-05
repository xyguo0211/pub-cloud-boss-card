package com.cn.offline.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.offline.entity.OfflineMenuDo;
import com.cn.offline.entity.OfflineRoleMenuDo;
import com.cn.offline.mapper.OfflineMenuMapper;
import com.cn.offline.service.IOfflineMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.common.OfflineConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单表 (EIP) 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Service
public class OfflineMenuServiceImpl extends ServiceImpl<OfflineMenuMapper, OfflineMenuDo> implements IOfflineMenuService {

    @Autowired
    private OfflineRoleMenuServiceImpl offlineRoleMenuService;

    public List<OfflineMenuDo> getRoleMeum(Integer roleId) {
        List<OfflineMenuDo> list = list();
        QueryWrapper<OfflineRoleMenuDo> wq=new QueryWrapper<>();
        wq.eq("role_id",roleId);
        List<OfflineRoleMenuDo> list_check = offlineRoleMenuService.list(wq);
        Set<Integer> set_check=new HashSet<>();
        for (OfflineRoleMenuDo offlineRoleMenuDo : list_check) {
            set_check.add(offlineRoleMenuDo.getMenuId());
        }
        for (OfflineMenuDo offlineMenuDo : list) {
            Integer id = offlineMenuDo.getId();
            if(set_check.contains(id)){
                offlineMenuDo.setIsCheck(OfflineConstants.checkStatus.check);
            }else{
                offlineMenuDo.setIsCheck(OfflineConstants.checkStatus.check_no);
            }
        }
        return list;
    }
}

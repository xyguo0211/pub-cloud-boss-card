package com.sn.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.exception.BusinessException;
import com.pub.core.util.controller.BaseController;
import com.pub.core.utils.StringUtils;
import org.springframework.stereotype.Service;
import rabb.shop.entity.OfflineRoleDo;
import rabb.shop.mapper.OfflineRoleMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Service
public class OfflineRoleServiceImpl extends ServiceImpl<OfflineRoleMapper, OfflineRoleDo> implements IService<OfflineRoleDo> {

    public void addRole(OfflineRoleDo req) throws Exception{
        String name = req.getName();
        QueryWrapper<OfflineRoleDo> wq=new QueryWrapper<>();
        wq.eq("name",name);
        List<OfflineRoleDo> list = list(wq);
        if(list!=null&&list.size()>0){
            throw new BusinessException("角色已存在！");
        }
        req.setCreateTime(new Date());
        save(req);
    }

    public List<OfflineRoleDo> getPageList(OfflineRoleDo req) {
        QueryWrapper<OfflineRoleDo> wq=new QueryWrapper<>();
        String name = req.getName();
        if(StringUtils.isNotBlank(name)){
            wq.like("name", name);
        }
        BaseController.startPage();
        List<OfflineRoleDo> list = list(wq);
        return list;
    }
}

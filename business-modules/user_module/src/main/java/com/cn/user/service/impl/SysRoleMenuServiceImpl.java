package com.cn.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cn.user.entity.SysMenuDo;
import com.cn.user.entity.SysRoleMenuDo;
import com.cn.user.mapper.SysRoleMenuMapper;
import com.cn.user.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单关系表 服务实现类
 * </p>
 *
 * @author ganyongheng
 * @since 2022-08-18
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenuDo> implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    
    @Autowired
    private  SysMenuServiceImpl sysMenuServiceImpl;

    public JSONObject getMenuByUserId(Integer id) {

        JSONObject jObject_rtn=new JSONObject();
       return jObject_rtn;
    }


}

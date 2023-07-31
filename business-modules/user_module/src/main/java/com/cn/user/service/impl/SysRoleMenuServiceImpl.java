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

    public JSONObject getMenuByUserId(Long id) {

        List<Map> map= sysRoleMenuMapper.getMenuByUserId(id);
        JSONObject jObject_rtn=new JSONObject();
       if(map!=null&&map.size()>0){
           //获取所有的菜单
           List<JSONObject> list=new ArrayList<>();
           Map<Integer,List<Map>> tempMap=new HashMap<>();
           Map<String, String> permissions=new HashMap<>();
           for (Map map1 : map) {
               Integer parent_id = Integer.valueOf(map1.get("parent_id").toString()) ;
               List<Map> maps = tempMap.get(parent_id);
               if(maps==null){
                   maps=new ArrayList<>();
                   maps.add(map1);
                   tempMap.put(parent_id,maps);
               }else{
                   maps.add(map1);
               }
               Object fun_value = map1.get("fun_value");
               if(fun_value==null){
                   permissions.put(map1.get("menu_url").toString(), "");
               }else{
                   permissions.put(map1.get("menu_url").toString(), fun_value.toString());
               }


           }
           for (Integer id_p : tempMap.keySet()) {
               JSONObject jsonObject=new JSONObject();
               SysMenuDo byId = sysMenuServiceImpl.getById(id_p);
               List<Map> maps = tempMap.get(id_p);
               jsonObject.put("id",byId.getId());
               jsonObject.put("name",byId.getMenuName());
               jsonObject.put("child",maps);
               list.add(jsonObject);
           }
           jObject_rtn.put("menus",list);
           //获取所有的权限
           jObject_rtn.put("permissions",permissions);
       }
       return jObject_rtn;
    }


}

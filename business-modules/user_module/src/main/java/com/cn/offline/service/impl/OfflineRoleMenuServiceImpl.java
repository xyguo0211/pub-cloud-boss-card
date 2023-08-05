package com.cn.offline.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.offline.entity.OfflineRoleMenuDo;
import com.cn.offline.mapper.OfflineRoleMenuMapper;
import com.cn.offline.service.IOfflineRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pub.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public void addRoleMenu(JSONObject req) {
        Integer roleId = req.getInteger("roleId");
        String menuList = req.getString("menuList");
        Date createTime = new Date();
        List<OfflineRoleMenuDo> listSave=new ArrayList<>();
        if(StringUtils.isNotBlank(menuList)){
            String[] split = menuList.split("#");
            if(split.length>0){
                 for (String s : split) {
                    OfflineRoleMenuDo offlineRoleMenuDo=new OfflineRoleMenuDo();
                    offlineRoleMenuDo.setCreateTime(createTime);
                    offlineRoleMenuDo.setRoleId(roleId);
                    offlineRoleMenuDo.setMenuId(Integer.valueOf(s));
                    listSave.add(offlineRoleMenuDo);
                }
            }
        }
        /**
         * 先删除，后插入
         */
        deleteAndInsert( roleId,listSave);

    }

    private void deleteAndInsert(Integer roleId, List<OfflineRoleMenuDo> listSave) {
        QueryWrapper<OfflineRoleMenuDo> wq=new QueryWrapper<>();
        wq.eq("role_id",roleId);
        remove(wq);
        if(listSave.size()>0){
            saveBatch(listSave);
        }
    }
}

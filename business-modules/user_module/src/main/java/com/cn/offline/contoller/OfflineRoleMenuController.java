package com.cn.offline.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.service.impl.OfflineRoleMenuServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/offline/offlineRoleMenuDo")
public class OfflineRoleMenuController extends BaseController {

    @Autowired
    private OfflineRoleMenuServiceImpl offlineRoleMenuServiceImpl;
    /**
     * 添加角色菜单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_ROLE_CENTER)
    public AjaxResult addRoleMenu(@RequestBody JSONObject req){
        try{
            offlineRoleMenuServiceImpl.addRoleMenu(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}


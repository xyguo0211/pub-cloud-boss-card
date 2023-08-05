package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OfflineMenuDo;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.service.impl.OfflineMenuServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单表 (EIP) 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/offline/offlineMenuDo")
public class OfflineMenuController extends BaseController {

    @Autowired
    private OfflineMenuServiceImpl offlineMenuServiceImpl;

    /**
     * 获取角色菜单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getRoleMeum", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_ROLE_CENTER)
    public AjaxResult getRoleMeum(@RequestParam Integer roleId){
        try{
           List<OfflineMenuDo> list= offlineMenuServiceImpl.getRoleMeum(roleId);
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

}


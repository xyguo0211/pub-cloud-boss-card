package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.entity.OfflineUserDo;
import com.cn.offline.service.impl.OfflineRoleServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/offline/offlineRoleDo")
public class OfflineRoleController extends BaseController {

    @Autowired
    private OfflineRoleServiceImpl offlineRoleServiceImpl;

    /**
     * 新增角色
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_ROLE_CENTER)
    public AjaxResult addRole(@RequestBody OfflineRoleDo req){
        try{
            offlineRoleServiceImpl.addRole(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 角色分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_ROLE_CENTER)
    public AjaxResult getPageList(@RequestBody OfflineRoleDo req){
        try{
            List<OfflineRoleDo> pageList = offlineRoleServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


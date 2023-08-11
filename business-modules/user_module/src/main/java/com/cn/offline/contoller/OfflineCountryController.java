package com.cn.offline.contoller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OfflineCountryDo;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.service.impl.OfflineCountryServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单表 (EIP) 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-11
 */
@Controller
@RequestMapping("/offlineCountryDo")
public class OfflineCountryController extends BaseController {

    @Autowired
    private OfflineCountryServiceImpl offlineCountryServiceImpl;

    /**
     * 新增角色
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addCountryDo", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addCountryDo(@RequestBody OfflineCountryDo req){
        try{
            QueryWrapper<OfflineCountryDo> wq=new QueryWrapper<>();
            wq.eq("country_name",req.getCountryName());
            OfflineCountryDo one = offlineCountryServiceImpl.getOne(wq);
            if(one!=null){
                return AjaxResult.error("重复国家名称！");
            }
            req.setCreateTime(new Date());
            offlineCountryServiceImpl.save(req);
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
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult getPageList(@RequestBody OfflineCountryDo req){
        try{
            List<OfflineCountryDo> pageList = offlineCountryServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     *删除
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult delete(@RequestParam Integer id){
        try{
            offlineCountryServiceImpl.removeById(id);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


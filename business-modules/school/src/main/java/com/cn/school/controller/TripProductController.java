package com.cn.school.controller;


import com.cn.auth.config.TimingLog;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.service.impl.TripProductServiceImpl;
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
 * 班次表 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Controller
@RequestMapping("/school/tripProductDo")
public class TripProductController extends BaseController {

    @Autowired
    private TripProductServiceImpl tripProductService;

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripProductDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripProductDo(@RequestBody TripProductDo tripProductDo){
        try{
            tripProductService.addTripProductDo(tripProductDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getPageList(@RequestBody TripProductDo req){
        try{
            List<TripProductDo> pageList = tripProductService.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/editTripProductDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editTripAreaDo(@RequestBody TripProductDo tripProductDo){
        try{
            tripProductService.editTripAreaDo(tripProductDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}


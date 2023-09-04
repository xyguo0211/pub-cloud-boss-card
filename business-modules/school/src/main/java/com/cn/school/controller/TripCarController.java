package com.cn.school.controller;


import com.cn.auth.config.TimingLog;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.TripProductDo;
import com.cn.school.service.impl.TripCarServiceImpl;
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
 * 车次 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Controller
@RequestMapping("/school/tripCarDo")
public class TripCarController extends BaseController {

    @Autowired
    private TripCarServiceImpl tripCarService;

    /**
     * 核销车票前，选择车次
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getCarCheck", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getCarCheck(){
        try{
          List<TripCarDo> list=  tripCarService.getCarCheck();
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripCarDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripCarDo(@RequestBody TripCarDo tripCarDo){
        try{
            tripCarService.addTripCarDo(tripCarDo);
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
    public AjaxResult getPageList(@RequestBody TripCarDo req){
        try{
            List<TripCarDo> pageList = tripCarService.getPageList(req);
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
    @RequestMapping(value = "/editTripCarDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editTripCarDo(@RequestBody TripCarDo tripCarDo){
        try{
            tripCarService.editTripCarDo(tripCarDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    /*@TimingLog
    @RequestMapping(value = "/editTripCarDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editTripCarDo(@RequestBody TripCarDo tripCarDo){
        try{
            tripCarService.editTripCarDo(tripCarDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }*/
    /**
     * 车次配置车票下拉选框
     */
    @TimingLog
    @RequestMapping(value = "/tripCarDoProductCheckBox", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult tripCarDoProductCheckBox(){
        try{
            List<TripProductDo> tripProductDos = tripCarService.tripCarDoProductCheckBox();
            return AjaxResult.success(tripProductDos);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }



}


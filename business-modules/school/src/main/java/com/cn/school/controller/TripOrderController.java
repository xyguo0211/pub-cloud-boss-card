package com.cn.school.controller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Controller
@RequestMapping("/school/tripOrderDo")
public class TripOrderController extends BaseController {

    @Autowired
    private TripOrderServiceImpl tripOrderService;


    /**
     * 我的订单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/myTripOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult myTripOrder(Integer status){
        try{
            List<TripOrderDo> pageList = tripOrderService.myTripOrderDo(status);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 下单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripOrder(@RequestBody TripOrderDo tripOrderDo){
        try{
             tripOrderService.addTripOrder(tripOrderDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 下单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/testVx", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult testVx(@RequestParam Integer id){
        try{
            TripOrderDo tripOrderDo = tripOrderService.getById(id);
            PrepayWithRequestPaymentResponse prepayWithRequestPaymentResponse = tripOrderService.WeChartPay(tripOrderDo, "oH-Ut5XAibVwhZfLjMhpAcxpWJ0I");
            return AjaxResult.success(prepayWithRequestPaymentResponse);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }


}


package com.cn.school.controller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.entity.TripOrderDo;
import com.cn.school.entity.UserDo;
import com.cn.school.service.impl.TripOrderServiceImpl;
import com.cn.school.service.impl.UserServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.pub.core.utils.StringUtils;
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
    @Autowired
    private UserServiceImpl userServiceImpl;


    /**
     * 我的订单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/myTripOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult myTripOrder(@RequestBody TripOrderDo tripOrderDo){
        try{
            List<TripOrderDo> pageList = tripOrderService.myTripOrderDo(tripOrderDo);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 我的返现订单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/myInvitationTripOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult myInvitationTripOrder(@RequestBody TripOrderDo tripOrderDo){
        try{
            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            UserDo byIUserDod = userServiceImpl.getById(id);
            tripOrderDo.setInvitationOpenid(byIUserDod.getOpenid());
            List<TripOrderDo> pageList = tripOrderService.myInvitationTripOrder(tripOrderDo);
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
            /**
             * 创建订单号
             */
            tripOrderService.createTripOrder(tripOrderDo);
            /**
             * 先预支付
             */
            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            UserDo byIUserDod = userServiceImpl.getById(id);
            PrepayWithRequestPaymentResponse prepayWithRequestPaymentResponse = tripOrderService.WeChartPay(tripOrderDo, byIUserDod.getOpenid());
            /**
             * 再保存订单信息,因为要减票所以这样做
             */
            tripOrderService.addTripOrder(tripOrderDo);
            JSONObject json=new JSONObject();
            json.put("orderId",tripOrderDo.getOrderId());
            json.put("prepayWithRequestPaymentResponse",prepayWithRequestPaymentResponse);
            return AjaxResult.success(json);
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
    /**
     * 二维码验证上车
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/checkTripOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult checkTripOrder(@RequestParam Integer carId,@RequestParam Integer id){
        try{
            TripOrderDo tripOrderDo = tripOrderService.checkTripOrder(id, carId);
            return AjaxResult.success(tripOrderDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 预备退款接口，告诉用户退款金额和扣除的费用
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/refundsTripOrderBefore", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult refundsTripOrderBefore(@RequestParam String orderId){
        try{
            JSONObject jsonObject = tripOrderService.refundsTripOrderBefore(orderId);
            return AjaxResult.success(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 退款确认接口
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/refundsTripOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult refundsTripOrder(@RequestParam String orderId){
        try{
            tripOrderService.refundsTripOrder(orderId);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 查询订单是否支付成功接口
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/checkPaySucess", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult checkPaySucess(@RequestParam String orderId){
        try{
            Integer integer = tripOrderService.checkPaySucess(orderId);
            JSONObject js=new JSONObject();
            if(1==integer){
                js.put("code",1);
                js.put("msg","支付成功");
            }else{
                js.put("code",0);
                js.put("msg","支付失败");
            }
            return AjaxResult.success(js);
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
    public AjaxResult getPageList(@RequestBody TripOrderDo req){
        try{
            List<TripOrderDo> pageList = tripOrderService.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 导出订单
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult importExcel(@RequestBody TripOrderDo req){
        try{
            String url = tripOrderService.importExcel(req);
            return AjaxResult.success("成功",url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 积分下单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripOrderByIntegral", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripOrderByIntegral(@RequestBody TripOrderDo tripOrderDo){
        try{
            /**
             * 创建订单号
             */
            tripOrderService.createTripOrderByIntegral(tripOrderDo);
            tripOrderService.addTripOrderByIntegral(tripOrderDo);
            JSONObject json=new JSONObject();
            json.put("orderId",tripOrderDo.getOrderId());
            return AjaxResult.success(json);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }


}


package com.cn.school.controller;

import com.cn.auth.config.TimingLog;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.TripProductCarRelationDo;
import com.cn.school.service.impl.TripProductCarRelationServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/school/tripProductCarRelationDo")
public class TripProductCarRelationController extends BaseController {

    @Autowired
    private TripProductCarRelationServiceImpl tripProductCarRelationServiceImpl;

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripCarDoProduct", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripCarDoProduct(@RequestBody TripProductCarRelationDo tripProductCarRelationDo){
        try{
            tripProductCarRelationServiceImpl.addTripCarDoProduct(tripProductCarRelationDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}

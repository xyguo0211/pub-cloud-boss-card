package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.pub.core.web.controller.BaseController;
import com.pub.core.web.domain.AjaxResult;
import com.pub.core.web.page.TableDataInfo;
import com.sn.online.entity.GoodFirstMeumDo;
import com.sn.online.entity.OnlineOrderInfoDo;
import com.sn.online.service.impl.OnlineOrderInfoServiceImpl;
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
 * @since 2023-08-02
 */
@Controller
@RequestMapping("/online/onlineOrderInfoDo")
public class OnlineOrderInfoController extends BaseController {

    @Autowired
   private OnlineOrderInfoServiceImpl onlineOrderInfoServiceImpl;

    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getPageList(@RequestBody JSONObject req){
        try{
            List<OnlineOrderInfoDo> pageList = onlineOrderInfoServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;


import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.sn.online.entity.GoodThirdRateDo;
import com.sn.online.entity.OnlineOrderInfoDo;
import com.sn.online.entity.dto.OnlineOrderSubmitDto;
import com.sn.online.service.impl.OnlineOrderInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

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

    /**
     * 查看列表分页数据
     * @param req
     * @return
     */
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
    /**
     * 查看详情页面
     */

    @TimingLog
    @RequestMapping(value = "/getDetailInfo", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getDetailInfo(@RequestParam Integer id){
        try{
            OnlineOrderInfoDo onlineOrderInfoDo = onlineOrderInfoServiceImpl.getDetailInfo(id);
            return AjaxResult.success(onlineOrderInfoDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 打开下单接口，数据获取
     * @param third_id
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/openOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult openOrder(@RequestParam Integer third_id){
        try{
            GoodThirdRateDo goodThirdRateDo = onlineOrderInfoServiceImpl.openOrder(third_id);
            return AjaxResult.success(goodThirdRateDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 提交下单接口
     * @param onlineOrderSubmitDto
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult submitOrder(@RequestBody OnlineOrderSubmitDto onlineOrderSubmitDto){
        try{
            onlineOrderInfoServiceImpl.submitOrder(onlineOrderSubmitDto);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


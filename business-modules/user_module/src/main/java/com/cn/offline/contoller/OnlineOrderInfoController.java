package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.OnlineOrderInfoDo;
import com.cn.offline.entity.OnlineOrderInfoReplyDo;
import com.cn.offline.service.impl.OnlineOrderInfoServiceImpl;
import com.pub.core.util.controller.BaseController;

import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.apache.catalina.startup.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/offline/onlineOrderInfoDo")
public class OnlineOrderInfoController extends BaseController {

    @Autowired
    private OnlineOrderInfoServiceImpl onlineOrderInfoService;

    /**
     * 获取订单分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_ORDER)
    public AjaxResult getPageList(@RequestBody OnlineOrderInfoDo req){
        try{
            List<OnlineOrderInfoDo> pageList = onlineOrderInfoService.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 打开详情页
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_ORDER)
    public AjaxResult getDetail(@RequestParam Integer id){
        try{
            OnlineOrderInfoDo onlineOrderInfoDo = onlineOrderInfoService.getDetail(id);
            return AjaxResult.success(onlineOrderInfoDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 订单审核
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/reviewOrder", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_ORDER)
    public AjaxResult reviewOrder(@RequestBody OnlineOrderInfoDo req){
        try{
            onlineOrderInfoService.reviewOrder(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 轮询获取订单消息接口
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getOrderMsg", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_ORDER)
    public AjaxResult getOrderMsg(){
        try{
            User currentUser = UserContext.getCurrentUser();
            onlineOrderInfoService.getOrderMsg(currentUser);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


}


package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.OnlineOrderInfoDo;
import com.cn.offline.service.impl.OnlineOrderInfoServiceImpl;
import com.pub.core.util.controller.BaseController;

import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
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
     * 客服获取订单分页数据
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

}


package com.cn.offline.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OnlineOrderInfoDo;
import com.cn.offline.entity.OnlineWithdrawDo;
import com.cn.offline.service.impl.OnlineWithdrawServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 提现管理 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Controller
@RequestMapping("/offline/onlineWithdrawDo")
public class OnlineWithdrawController extends BaseController {

    @Autowired
    private OnlineWithdrawServiceImpl onlineWithdrawServiceImpl;


    /**
     * 查看列表分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_DRAW)
    public AjaxResult getPageList(@RequestBody JSONObject req){
        try{
            List<OnlineWithdrawDo> pageList = onlineWithdrawServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 回复提现记录
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/submitDraw", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_DRAW)
    public AjaxResult submitDraw(@RequestBody  OnlineWithdrawDo req){
        try{
            onlineWithdrawServiceImpl.submitDraw(req);
            return AjaxResult.success();
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
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_DRAW)
    public AjaxResult getDetail(@RequestParam Integer id){
        try{
            OnlineWithdrawDo onlineWithdrawDo = onlineWithdrawServiceImpl.getDetail(id);
            return AjaxResult.success(onlineWithdrawDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


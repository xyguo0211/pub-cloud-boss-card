package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.sn.online.config.dto.OnlineTransactionHistoryDto;
import com.sn.online.service.impl.OnlineTransactionHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 交易记录 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-12
 */
@Controller
@RequestMapping("/online/onlineTransactionHistoryDo")
public class OnlineTransactionHistoryController extends BaseController {

    @Autowired
    private OnlineTransactionHistoryServiceImpl onlineTransactionHistoryServiceImpl;
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
            List<OnlineTransactionHistoryDto> pageList = onlineTransactionHistoryServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


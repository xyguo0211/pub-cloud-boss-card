package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.sn.online.entity.OnlineOrderInfoDo;
import com.sn.online.entity.OnlineUserBankAccountDo;
import com.sn.online.entity.OnlineWithdrawDo;
import com.sn.online.service.impl.OnlineWithdrawServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/online/onlineWithdrawDo")
public class OnlineWithdrawController extends BaseController {

    @Autowired
    private OnlineWithdrawServiceImpl  onlineWithdrawServiceImpl;

    /**
     * 添加提现记录
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addOnlineWithdraw", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addOnlineWithdraw(@RequestBody OnlineWithdrawDo req){
        try{
            User currentUser = UserContext.getCurrentUser();
            if(currentUser==null){
                return AjaxResult.error("Please log in !");
            }
            onlineWithdrawServiceImpl.addOnlineWithdraw(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
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
            User currentUser = UserContext.getCurrentUser();
            if(currentUser==null){
                return AjaxResult.error("Please log in !");
            }
            List<OnlineWithdrawDo> pageList = onlineWithdrawServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
}


package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.sn.online.config.Decrypt;
import com.sn.online.config.Encrypt;
import com.sn.online.service.impl.OnlineWithdrawServiceImpl;
import com.sn.online.utils.AESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import rabb.shop.entity.OnlineWithdrawDo;

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
    @Decrypt
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
    /**
     *  请求参数解密
     * @param req
     * @return
     */
    @Decrypt
    /**
     *返回参数加密
     */
    @Encrypt
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
    /**
     * 打开详情页
     * @return
     */
    @Encrypt
    @TimingLog
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getDetail(@RequestParam Integer id){
        try{
            OnlineWithdrawDo onlineWithdrawDo = onlineWithdrawServiceImpl.getDetail(id);
            return AjaxResult.success(onlineWithdrawDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    @TimingLog
    @RequestMapping(value = "/testAES", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult testAES(@RequestBody JSONObject body){
        try{
            String encrypt = AESUtils.encrypt(JSONObject.toJSONString(body));
            return AjaxResult.success(encrypt,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


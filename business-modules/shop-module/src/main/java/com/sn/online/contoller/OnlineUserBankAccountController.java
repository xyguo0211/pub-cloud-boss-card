package com.sn.online.contoller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.pub.core.common.OnlineConstants;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.sn.online.config.Decrypt;
import com.sn.online.config.Encrypt;
import com.sn.online.entity.OnlineUserBankAccountDo;
import com.sn.online.entity.OnlineUserDo;
import com.sn.online.service.impl.OnlineUserBankAccountServiceImpl;
import com.sn.online.service.impl.OnlineUserServiceImpl;
import com.sn.online.service.impl.SysDataDictionaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户银行卡管理 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-06
 */
@Controller
@RequestMapping("/online/onlineUserBankAccountDo")
public class OnlineUserBankAccountController extends BaseController {

    @Autowired
    private OnlineUserBankAccountServiceImpl onlineUserBankAccountService;
    @Autowired
    private OnlineUserServiceImpl onlineUserServiceImpl;

    /**
     * 添加银行卡
     * @return
     */
    @Decrypt
    @TimingLog
    @RequestMapping(value = "/addBankAccount", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addBankAccount(@RequestBody OnlineUserBankAccountDo req){
        try{
            User currentUser = UserContext.getCurrentUser();
            if(currentUser==null){
                return AjaxResult.error("Please log in !");
            }
            onlineUserBankAccountService.addBankAccount(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/sendBankEmail", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult sendBankEmail(){
        try{
            User currentUser = UserContext.getCurrentUser();
            if(currentUser==null){
                return AjaxResult.error("Please log in !");
            }
            Integer id = currentUser.getId();
            OnlineUserDo onlineUserDo = onlineUserServiceImpl.getById(id);
            String emailAddress = onlineUserDo.getName();
            onlineUserServiceImpl.sendEmailBank(emailAddress);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 获取银行卡列表
     * @return
     */
    @Encrypt
    @TimingLog
    @RequestMapping(value = "/getBanklist", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getlist(){
        try{
            User currentUser = UserContext.getCurrentUser();
            if(currentUser==null){
                return AjaxResult.error("Please log in !");
            }
            QueryWrapper<OnlineUserBankAccountDo> wq=new QueryWrapper<>();
            wq.eq("user_id",currentUser.getId());
            wq.eq("delete_status", OnlineConstants.deleteStats.delete_no);
            List<OnlineUserBankAccountDo> list = onlineUserBankAccountService.list(wq);
            return AjaxResult.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


}


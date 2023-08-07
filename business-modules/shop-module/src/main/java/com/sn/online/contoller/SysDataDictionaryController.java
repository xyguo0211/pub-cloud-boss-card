package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.pub.core.util.domain.AjaxResult;
import com.sn.online.service.impl.SysDataDictionaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统数据字典数据库 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-01
 */
@Controller
@RequestMapping("/online/sysDataDictionaryDo")
public class SysDataDictionaryController {
    @Autowired
    private SysDataDictionaryServiceImpl sysDataDictionaryServiceImpl;

    @RequestMapping(value = "/refreshCache", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult refreshCache() {
        try{
            sysDataDictionaryServiceImpl.refreshCache();
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    @RequestMapping(value = "/getBankMsg", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getBankMsg() {
        try{
            List<String> listRtn=new ArrayList<>();
            Map<String, String> bankMsg = sysDataDictionaryServiceImpl.getSysBaseParam("BankMsg");
            for (String s : bankMsg.keySet()) {
                listRtn.add(bankMsg.get(s));
            }
            return AjaxResult.success(listRtn);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}


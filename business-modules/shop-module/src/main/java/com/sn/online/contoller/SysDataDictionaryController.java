package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.pub.core.web.domain.AjaxResult;
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
@RequestMapping("/sysDataDictionaryDo")
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
}


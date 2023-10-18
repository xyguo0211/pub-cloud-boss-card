package com.cn.school.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.config.TimingLog;
import com.cn.school.entity.SysDataDictionaryDo;
import com.cn.school.service.impl.SysDataDictionaryServiceImpl;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.utils.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("/school/sysDataDictionaryDo")
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

    /**
     * 获取退费规则的配置
     * @return
     */
    @RequestMapping(value = "/getRefundsMsg", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getRefundsMsg() {
        try{
            String sysBaseParam = sysDataDictionaryServiceImpl.getSysBaseParam("refunds_notice_msg", "refunds_notice_msg");
            return AjaxResult.success(sysBaseParam,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    /**
     * 添加退费规则的配置
     * @return
     */
    @RequestMapping(value = "/addRefundsConfig", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addRefundsConfig(@RequestBody JSONObject js) {
        try{
            QueryWrapper<SysDataDictionaryDo> wq=new QueryWrapper<>();
            wq.eq("param_key","refunds_notice_msg");
            wq.eq("param_name","refunds_notice_msg");
            SysDataDictionaryDo one = sysDataDictionaryServiceImpl.getOne(wq);
            if(one==null){
                one=new SysDataDictionaryDo();
                one.setParamKey("refunds_notice_msg");
                one.setParamName("refunds_notice_msg");
                one.setParamValue(js.toJSONString());
                one.setParamDesc("退费规则配置");
                one.setStatus(9);
                one.setCreateDate(new Date());
                sysDataDictionaryServiceImpl.save(one);
            }else{
                one.setCreateDate(new Date());
                one.setParamValue(js.toJSONString());
                sysDataDictionaryServiceImpl.updateById(one);
            }
            sysDataDictionaryServiceImpl.refreshCache();
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    /**
     * 添加退费规则的配置
     * @return
     */
    @RequestMapping(value = "/addTicketsNum", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult addTicketsNum(@RequestParam Integer num) {
        try{
            QueryWrapper<SysDataDictionaryDo> wq=new QueryWrapper<>();
            wq.eq("param_key","tickets_num");
            wq.eq("param_name","tickets_num");
            SysDataDictionaryDo one = sysDataDictionaryServiceImpl.getOne(wq);
            if(one==null){
                one=new SysDataDictionaryDo();
                one.setParamKey("tickets_num");
                one.setParamName("tickets_num");
                one.setParamValue(num+"");
                one.setParamDesc("剩余车票最小值告警");
                one.setStatus(9);
                one.setCreateDate(new Date());
                sysDataDictionaryServiceImpl.save(one);
            }else{
                one.setCreateDate(new Date());
                one.setParamValue(num+"");
                sysDataDictionaryServiceImpl.updateById(one);
            }
            sysDataDictionaryServiceImpl.refreshCache();
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }


    /**
     * 获取退费规则的配置
     * @return
     */
    @RequestMapping(value = "/getTicketsNum", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getTicketsNum() {
        try{
            String sysBaseParam = sysDataDictionaryServiceImpl.getSysBaseParam("tickets_num", "tickets_num");
            return AjaxResult.success("成功",sysBaseParam);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    /**
     * 获取学校下拉框
     * @return
     */
    @RequestMapping(value = "/getSchoolName", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getSchool() {
        try{
            String school_name = sysDataDictionaryServiceImpl.getSysBaseParam("school_name", "school_name");
            String[] split = school_name.split("##");
            return AjaxResult.success("成功",split);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    /**
     * 添加学校下拉框
     * @return
     */
    @RequestMapping(value = "/addSchoolName", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult addSchoolName(@RequestParam String schoolName) {
        try{
            QueryWrapper<SysDataDictionaryDo> wq=new QueryWrapper<>();
            wq.eq("param_key","school_name");
            wq.eq("param_name","school_name");
            SysDataDictionaryDo one = sysDataDictionaryServiceImpl.getOne(wq);
            String school_name = one.getParamValue();
           StringBuilder sb=new StringBuilder();
           if(StringUtils.isNotBlank(school_name)){
               sb.append(school_name).append("##").append(schoolName);
           }
            one.setParamValue(sb.toString());
            sysDataDictionaryServiceImpl.updateById(one);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 提现积分配置
     * @return
     */
    @RequestMapping(value = "/addIntegralManageNum", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult addIntegralManageNum(@RequestParam String num) {
        try{
            QueryWrapper<SysDataDictionaryDo> wq=new QueryWrapper<>();
            wq.eq("param_key","integral_manage_num");
            wq.eq("param_name","integral_manage_num");
            SysDataDictionaryDo one = sysDataDictionaryServiceImpl.getOne(wq);
            if(one==null){
                one=new SysDataDictionaryDo();
                one.setParamKey("integral_manage_num");
                one.setParamName("integral_manage_num");
                one.setParamValue(num);
                one.setParamDesc("最小提现积分配置！");
                one.setStatus(9);
                one.setCreateDate(new Date());
                sysDataDictionaryServiceImpl.save(one);
            }else{
                one.setCreateDate(new Date());
                one.setParamValue(num);
                sysDataDictionaryServiceImpl.updateById(one);
            }
            sysDataDictionaryServiceImpl.refreshCache();
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}


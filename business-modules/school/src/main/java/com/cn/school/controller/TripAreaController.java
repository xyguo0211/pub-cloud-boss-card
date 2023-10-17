package com.cn.school.controller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.school.entity.TripAreaDo;
import com.cn.school.service.impl.TripAreaServiceImpl;
import com.cn.school.service.impl.TripCarServiceImpl;
import com.cn.school.service.impl.TripProductServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 行程表 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-16
 */
@Controller
@RequestMapping("/school/tripAreaDo")
public class TripAreaController extends BaseController {

    @Autowired
    private TripAreaServiceImpl tripAreaService;

    @Autowired
    private TripProductServiceImpl tripProductService;

    @Autowired
    private TripCarServiceImpl tripCarServiceImpl;




    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addTripAreaDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addTripAreaDo(@RequestBody TripAreaDo tripAreaDo){
        try{
            tripAreaService.addTripAreaDo(tripAreaDo);
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
    @RequestMapping(value = "/startTrips", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult startTrips(){
        try{
            Map<String, Set<String>> stringListMap = tripAreaService.startTrips();
            return AjaxResult.success(stringListMap);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/endTrips", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult endTrips(String startTrips){
        try{
            Map<String, List<TripAreaDo>> stringListMap= tripAreaService.endTrips(startTrips);
            return AjaxResult.success(stringListMap);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 获取热门路线
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/gethotTrips", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult gethotTrips(){
        try{
            List<TripAreaDo> byIdEntity = tripAreaService.gethotTrips();
            return AjaxResult.success(byIdEntity);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 查询班次
     */
    @TimingLog
    @RequestMapping(value = "/findTrips", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult findTrips(@RequestParam Integer tripAreaId,String data){
        try{
            List<Map> trips = tripAreaService.findTrips(tripAreaId, data);
            return AjaxResult.success(trips);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getPageList(@RequestBody TripAreaDo req){
        try{
            List<TripAreaDo> pageList = tripAreaService.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/editTripAreaDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult editTripAreaDo(@RequestBody TripAreaDo tripAreaDo){
        try{
            tripAreaService.editTripAreaDo(tripAreaDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 车次下拉选框
     * @param
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getCheckBox", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getCheckBox(){
        try{
            List<TripAreaDo> pageList = tripAreaService.getCheckBox();
            return AjaxResult.success(pageList);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 上传文件
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImage(MultipartFile file){
        try{
            String url= tripAreaService.uploadImage(file);
            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 删除航线接口
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/deleteTripAreaDo", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteTripAreaDo(@RequestBody TripAreaDo tripAreaDo){
        try{
            tripAreaService.editTripAreaDo(tripAreaDo);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}


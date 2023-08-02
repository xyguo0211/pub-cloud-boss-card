package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.pub.core.web.domain.AjaxResult;
import com.sn.online.entity.GoodFirstMeumDo;
import com.sn.online.entity.GoodThirdRateDo;
import com.sn.online.entity.OnlineUserDo;
import com.sn.online.entity.dto.OnlineOrderSubmitDto;
import com.sn.online.service.impl.GoodFirstMeumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/online/goodFirstMeumDo")
public class GoodFirstMeumController {

    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;

    @TimingLog
    @RequestMapping(value = "/getFirstPage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getFirstPage(){
        try{
            List<GoodFirstMeumDo> firstPage = goodFirstMeumServiceImpl.getFirstPage();
            return AjaxResult.success(firstPage);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    @TimingLog
    @RequestMapping(value = "/openOrder", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult openOrder(@RequestParam Integer third_id){
        try{
            GoodThirdRateDo goodThirdRateDo = goodFirstMeumServiceImpl.openOrder(third_id);
            return AjaxResult.success(goodThirdRateDo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    @TimingLog
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult submitOrder(@RequestBody OnlineOrderSubmitDto onlineOrderSubmitDto){
        try{
             goodFirstMeumServiceImpl.submitOrder(onlineOrderSubmitDto);
            return AjaxResult.success();
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
            String url= goodFirstMeumServiceImpl.uploadImage(file);
            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


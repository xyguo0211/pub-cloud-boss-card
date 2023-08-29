package com.sn.online.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;

import com.pub.core.util.domain.AjaxResult;
import com.sn.online.service.impl.GoodFirstMeumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import rabb.shop.entity.GoodFirstMeumDo;

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
    @Value("${imageName}")
    private String imageName;

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


    /**
     * 上传文件
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult uploadImage(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            /**
             *
             */
            String[] split = imageName.split("#");
            boolean isImage=false;
            for (String s : split) {
                if(fileName.endsWith(s)){
                    isImage=true;
                    break;
                }
            }
            if(!isImage){
                return AjaxResult.error("Upload file format is not an image !");
            }
            String url= goodFirstMeumServiceImpl.uploadImage(file);
            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


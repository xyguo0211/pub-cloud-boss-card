package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.OfflineRoleDo;
import com.cn.offline.service.impl.GoodFirstMeumServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/offline/goodFirstMeumDo")
public class GoodFirstMeumController extends BaseController {

    @Autowired
    private GoodFirstMeumServiceImpl goodFirstMeumServiceImpl;

    @Value("${imageName}")
    private String imageName;

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
                return AjaxResult.error("上传文件非图片格式 !");
            }
            String url= goodFirstMeumServiceImpl.uploadImage(file);
            return AjaxResult.success(url);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addFirstCard", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addFirstCard(@RequestBody GoodFirstMeumDo req){
        try{
            goodFirstMeumServiceImpl.addFirstCard(req);
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
    @RequestMapping(value = "/updateFirstCard", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult updateFirstCard(@RequestBody GoodFirstMeumDo req){
        try{
            goodFirstMeumServiceImpl.updateFirstCard(req);
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
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult getById(@RequestParam Integer id){
        try{
            GoodFirstMeumDo byIdEntity = goodFirstMeumServiceImpl.getByIdEntity(id);
            return AjaxResult.success(byIdEntity);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult deleteById(@RequestParam Integer id){
        try{
             goodFirstMeumServiceImpl.deleteById(id);
            return AjaxResult.success();
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
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult getPageList(@RequestBody GoodFirstMeumDo req){
        try{
            List<GoodFirstMeumDo> pageList = goodFirstMeumServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


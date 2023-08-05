package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodSecondCountryDo;
import com.cn.offline.entity.GoodThirdRateDo;
import com.cn.offline.service.impl.GoodThirdRateServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-02
 */
@Controller
@RequestMapping("/offline/goodThirdRateDo")
public class GoodThirdRateController extends BaseController {

    @Autowired
    private GoodThirdRateServiceImpl goodThirdRateServiceImpl;

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addThirdRate", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addThirdRate(@RequestBody GoodThirdRateDo req){
        try{
            goodThirdRateServiceImpl.addThirdRate(req);
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
    @RequestMapping(value = "/updateThirdRate", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult updateThirdRate(@RequestBody GoodThirdRateDo req){
        try{
            goodThirdRateServiceImpl.updateThirdRate(req);
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
            GoodThirdRateDo byId = goodThirdRateServiceImpl.getEntityById(id);
            return AjaxResult.success(byId);
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
            goodThirdRateServiceImpl.deleteById(id);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}


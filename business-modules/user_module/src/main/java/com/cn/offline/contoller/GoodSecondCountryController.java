package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.GoodSecondCountryDo;
import com.cn.offline.service.impl.GoodSecondCountryServiceImpl;
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
@RequestMapping("/offline/goodSecondCountryDo")
public class GoodSecondCountryController extends BaseController {

    @Autowired
    private GoodSecondCountryServiceImpl goodSecondCountryServiceImpl;

    /**
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addSecondCountry", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addSecondCountry(@RequestBody GoodSecondCountryDo req){
        try{
            goodSecondCountryServiceImpl.addSecondCountry(req);
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
    @RequestMapping(value = "/updateSecondCountry", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult updateSecondCountry(@RequestBody GoodSecondCountryDo req){
        try{
            goodSecondCountryServiceImpl.updateSecondCountry(req);
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
            GoodSecondCountryDo byId = goodSecondCountryServiceImpl.getById(id);
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
            goodSecondCountryServiceImpl.deleteById(id);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

}


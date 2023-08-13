package com.cn.offline.contoller;


import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.GoodFirstMeumDo;
import com.cn.offline.entity.OnlineOrderInfoReplyDo;
import com.cn.offline.service.impl.OnlineOrderInfoReplyServiceImpl;
import com.pub.core.util.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 订单图片回复表 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/offline/onlineOrderInfoReplyDo")
public class OnlineOrderInfoReplyController {

    @Autowired
    private OnlineOrderInfoReplyServiceImpl onlineOrderInfoReplyServiceImpl;

    /**
     * 回复订单
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addOrderInfoReply", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.SELL_GIFT_CARD)
    public AjaxResult addOrderInfoReply(@RequestBody OnlineOrderInfoReplyDo req){
        try{
            onlineOrderInfoReplyServiceImpl.addOrderInfoReply(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
}


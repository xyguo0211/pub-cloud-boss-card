package com.sn.shop.contoller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.authority.AuthMenuKeyConstant;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.AuthorityType;
import com.pub.core.web.controller.BaseController;
import com.pub.core.web.domain.AjaxResult;
import com.pub.core.web.page.TableDataInfo;
import com.pub.redis.util.RedisCache;
import com.sn.shop.entity.ShopDo;
import com.sn.shop.service.impl.ShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/shopDo")
public class ShopController extends BaseController {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ShopServiceImpl shopServiceImpl;

    @Authentication(menu = AuthMenuKeyConstant.XHX_CATEGOR_CONFIGY, type = AuthorityType.QUERY)
    @RequestMapping(value = "getShop", method = RequestMethod.POST)
    @ResponseBody
    public TableDataInfo getShop(@RequestBody ShopDo req){
        redisCache.setnxWithExptime("getShop","getShop",2000);
        List<ShopDo> list = shopServiceImpl.listPage(req);
        return getDataTable(list);
    }
    @RequestMapping(value = "saveShop", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult saveShop(@RequestBody JSONObject req){
        ShopDo shop=new ShopDo();
        shop.setProductId(req.getInteger("product_id"));
        shop.setProductName(req.getString("product_name"));
        shop.setSource(req.getString("source"));
        boolean save = shopServiceImpl.save(shop);
        JSONObject resp=new JSONObject();
        if(save){
            resp.put("code",0);
            resp.put("msg","新增成功");
        }else{
            resp.put("code",-1);
            resp.put("msg","新增失败");
        }
        return success();
    }
    @RequestMapping(value = "deleteShop", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteShop(@RequestBody JSONObject req){
        boolean id = shopServiceImpl.removeById(req.getInteger("id"));
        JSONObject resp=new JSONObject();
        if(id){
            resp.put("code",0);
            resp.put("msg","删除成功");
        }else{
            resp.put("code",-1);
            resp.put("msg","删除失败");
        }
        return success();
    }
}

package api.shop.api;

import api.shop.entity.Shop;
import api.shop.hysitx.ShopControllerHysitxFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shop-boss" ,fallbackFactory = ShopControllerHysitxFactory.class)
public interface ShopControllerApi {
    @PostMapping(value = "/shop_boss/shop/getShop")
    public Shop getShop(@RequestBody JSONObject req);
    @PostMapping(value = "/shop_boss/shop/saveShop")
    public JSONObject saveShop(@RequestBody JSONObject req);
    @PostMapping(value = "/shop_boss/shop/deleteShop")
    public JSONObject deleteShop(@RequestBody JSONObject req);
}


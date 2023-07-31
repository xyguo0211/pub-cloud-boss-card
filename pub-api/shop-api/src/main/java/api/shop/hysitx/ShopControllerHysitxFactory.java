package api.shop.hysitx;
import api.shop.api.ShopControllerApi;
import api.shop.entity.Shop;
import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ShopControllerHysitxFactory implements  FallbackFactory<ShopControllerApi>{

    @Override
    public ShopControllerApi create(Throwable throwable) {
        return new ShopControllerApi(){

            @Override
            public Shop getShop(JSONObject req) {
                Shop shop = new Shop();
                shop.setId(0);
                shop.setProductName("熔断产品");
                return shop;
            }

            @Override
            public JSONObject saveShop(JSONObject req) {
                return null;
            }

            @Override
            public JSONObject deleteShop(JSONObject req) {
                return null;
            }
        };
    }
}

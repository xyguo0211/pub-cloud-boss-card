package api.user.hysitx;
import api.user.api.UserControllerApi;
import api.user.entity.User;
import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserControllerHysitxFactory implements  FallbackFactory<UserControllerApi>{


    @Override
    public UserControllerApi create(Throwable throwable) {
        return new UserControllerApi(){

            @Override
            public User getUser(JSONObject req) {
                User user=new User();
                user.setId(0);
                user.setUserName("熔断用户");
                return user;
            }
        };
    }
}

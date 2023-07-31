package api.user.api;

import api.user.entity.User;
import api.user.hysitx.UserControllerHysitxFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "user-boss" ,fallbackFactory = UserControllerHysitxFactory.class)
public interface UserControllerApi {
    @PostMapping(value = "/user_boss/user/getUser")
    public User getUser(@RequestBody JSONObject req);
}


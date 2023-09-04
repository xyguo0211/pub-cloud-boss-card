package com.cn.school.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.auth.authority.AuthMenuKeyConstant;
import com.cn.auth.config.Constant;
import com.cn.auth.config.TimingLog;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.auth.entity.User;
import com.cn.auth.util.UserContext;
import com.cn.school.entity.TripCarDo;
import com.cn.school.entity.UserDo;
import com.cn.school.service.impl.UserServiceImpl;
import com.cn.school.util.RpcBaseResponseResult;
import com.cn.school.util.SmsSendServiceUtil;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.pub.core.utils.RandomUtil;
import com.pub.redis.util.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-14
 */
@Controller
@RequestMapping("/school/userDo")
public class UserController extends BaseController {

    @Autowired
    private UserServiceImpl userService;


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SmsSendServiceUtil smsSendServiceUtil;

    /**
     * 0是测试  1是正式
     */
    @Value("${isTest}")
    private  Integer isTest ;
    /**
     * 刷新token
     * @return
     */
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult refreshToken(){
        HttpServletRequest request = getRequest();
        String bearerToken = request.getHeader(TokenProvider.AUTHORIZATION_HEADER_ONLINE);
        if (!bearerToken.startsWith("BearerSchool")) {
            return AjaxResult.error(" token的格式不正确 !");
        }
        //##缓存一段时间,避免高并发重复请求,因为会出现并发请求拿新的token来换token情况,缓存30秒
        String online_cache_jwt="school_cache_" + bearerToken;
        String redis_cache = redisCache.getStringCache(online_cache_jwt);
        if(StringUtils.isNotBlank(redis_cache)){
            JSONObject jsonObject1 = JSONObject.parseObject(redis_cache);
            return AjaxResult.success(jsonObject1);
        }
        JSONObject new_token = userService.refreshToken(bearerToken);
        if(new_token==null){
            return AjaxResult.error("请重新登录");
        }
        return AjaxResult.success(new_token);
    }

    /**
     * 第一次下单是否实名认证接口
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/checkIdentity", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult checkIdentity(){
        try{
            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            UserDo byIUserDod = userService.getById(id);
            String phone = byIUserDod.getPhone();
            if(com.pub.core.utils.StringUtils.isBlank(phone)){
                return AjaxResult.error("请实名认证！");
            }
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 第一次下单实名认证接口提交信息
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addIdentity", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addIdentity(@RequestBody UserDo userDo){
        try{
            User currentUser = UserContext.getCurrentUser();
            Integer id = currentUser.getId();
            String phoneCode = userDo.getPhoneCode();
            String phone = userDo.getPhone();
            String stringCache = redisCache.getStringCache(phone);
            if(!phoneCode.equals(stringCache)){
                return AjaxResult.error("手机验证码错误");
            }
            UserDo byIUserDod = userService.getById(id);

            byIUserDod.setPhone(phone);
            byIUserDod.setSchool(userDo.getSchool());
            byIUserDod.setIdentityName(userDo.getIdentityName());
            userService.updateById(byIUserDod);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 获取验证码,校验用户是否已被拉黑
     */
    @TimingLog
    @RequestMapping(value = "/getMsg/{phone}", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult getMsg(@PathVariable String phone){
        JSONObject rtn=new JSONObject();
        try {
            //第一步校验这个手机号是否被使用
            UserDo userDo = userService.checkPhoneExit(phone);
            if(userDo!=null){
                return AjaxResult.error("手机号已被注册过！");
            }

            if(isTest==0){
                //测试环境
                redisCache.putCacheWithExpireTime(phone,"1111",5*60);
                return AjaxResult.success();
            }else{
                int randomLenFours = RandomUtil.getRandomLenFours();
                RpcBaseResponseResult rpcBaseResponseResult = smsSendServiceUtil.sendMassage("您好，您的验证码是:" + randomLenFours, phone);
                int status = rpcBaseResponseResult.getStatus();
                if(0==status){
                    redisCache.putCacheWithExpireTime(phone,String.valueOf(randomLenFours),5*60);
                    return AjaxResult.success();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("失败，请联系管理员！");
    }

    /**
     * 分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult getPageList(@RequestBody UserDo req){
        try{
            List<UserDo> pageList = userService.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 拉黑接口
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addBlack", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addBlack(@RequestBody UserDo req){
        try{
            userService.addBlack(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 修改角色  1 系统管理员 2 扫描人员 3普通用户
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult addRole(@RequestBody UserDo req){
        try{
            userService.updateById(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

}


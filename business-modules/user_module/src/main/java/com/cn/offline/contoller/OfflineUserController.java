package com.cn.offline.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.authority.AuthMenuKeyConstant;
import com.cn.auth.config.Authentication;
import com.cn.auth.config.TimingLog;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.offline.config.OfflineAuthMenuKeyConstant;
import com.cn.offline.entity.OfflineUserDo;
import com.cn.offline.entity.OnlineOrderInfoDo;
import com.cn.offline.entity.OnlineUserDo;
import com.cn.offline.service.impl.OfflineUserServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.util.page.TableDataInfo;
import com.pub.redis.util.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 离线用户表 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-08-03
 */
@Controller
@RequestMapping("/offline/userDo")
public class OfflineUserController extends BaseController {

    @Autowired
    private OfflineUserServiceImpl offlineUserServiceImpl;

    @Autowired
    private RedisCache redisCache;




    @TimingLog
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult login(@RequestBody OfflineUserDo req){
        try{
            JSONObject login = offlineUserServiceImpl.login(req);
            return AjaxResult.success(login);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 刷新token
     * @return
     */
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult refreshToken(){
        try{
            HttpServletRequest request = getRequest();
            String bearerToken = request.getHeader(TokenProvider.AUTHORIZATION_HEADER);
            if (!bearerToken.startsWith("Bearer_offline")) {
                return AjaxResult.error(" 不规则的token !");
            }
            //##缓存一段时间,避免高并发重复请求,因为会出现并发请求拿新的token来换token情况,缓存30秒
            String online_cache_jwt="offline_Cache_" + bearerToken;
            String redis_cache = redisCache.getStringCache(online_cache_jwt);
            if(StringUtils.isNotBlank(redis_cache)){
                JSONObject jsonObject1 = JSONObject.parseObject(redis_cache);
                return AjaxResult.success(jsonObject1);
            }
            JSONObject new_token = offlineUserServiceImpl.refreshToken(bearerToken);
            if(new_token==null){
                return AjaxResult.error("请重新登录");
            }
            return AjaxResult.success(new_token);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult changePassword(@RequestBody JSONObject req){
        try{
            offlineUserServiceImpl.changePassword(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 新增用戶
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult addUser(@RequestBody OfflineUserDo req){
        try{
            offlineUserServiceImpl.addUser(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }
    /**
     * 拉黑用户
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/isBlack", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult isBlack(@RequestBody OfflineUserDo req){
        try{
            offlineUserServiceImpl.isBlack(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * 查看用户的分页数据
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult getPageList(@RequestBody OfflineUserDo req){
        try{
            List<OfflineUserDo> pageList = offlineUserServiceImpl.getPageList(req);
            TableDataInfo dataTable = getDataTable(pageList);
            return AjaxResult.success(dataTable);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }
    /**
     * 设置工作时间
     * @param req
     * @return
     */
    @TimingLog
    @RequestMapping(value = "/setWorkTime", method = RequestMethod.POST)
    @ResponseBody
    @Authentication(menu = OfflineAuthMenuKeyConstant.BASE_USER_CENTER)
    public AjaxResult setWorkTime(@RequestBody JSONObject req){
        try{
             offlineUserServiceImpl.setWorkTime(req);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }

    }







}


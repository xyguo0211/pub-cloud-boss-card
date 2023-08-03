package com.cn.user.contoller;


import com.alibaba.fastjson.JSONObject;
import com.cn.auth.config.TimingLog;
import com.cn.auth.config.jwt.TokenProvider;
import com.cn.user.entity.OnlineUserDo;
import com.cn.user.entity.dto.OnlineUserRegisterDto;
import com.cn.user.service.impl.OnlineUserServiceImpl;
import com.pub.core.util.controller.BaseController;
import com.pub.core.util.domain.AjaxResult;
import com.pub.core.utils.AESUtil;
import com.pub.redis.util.RedisCache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 在线用户表 前端控制器
 * </p>
 *
 * @author ganyongheng
 * @since 2023-07-31
 */
@Controller
@RequestMapping("/online/userDo")
public class OnlineUserController extends BaseController {



}


package com.sn.online.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.http.param.MediaType;
import com.pub.core.util.domain.AjaxResult;
import com.sn.online.utils.AESUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class EncryptResponse implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        //如果需要对所有api加解密，这里直接 return true;
        return returnType.hasMethodAnnotation(Encrypt.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, org.springframework.http.MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try{
//            if(body.getMsg()!=null){
//                body.setMsg(SecretUtils.encrypt(body.getMsg()));
//            }
            if(body!=null){ // 加密传输数据，注意先转JSON再转String格式
                return AESUtils.encrypt(JSONObject.toJSONString(body));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


}

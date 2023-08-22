package com.sn.online.config;

import com.sn.online.utils.AESUtils;
import org.springframework.aop.BeforeAdvice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class DecryptRequest extends RequestBodyAdviceAdapter {




    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        //如果需要对所有api加解密，这里直接 return true;
        boolean b1 = methodParameter.hasMethodAnnotation(Decrypt.class);
        boolean b2 = methodParameter.hasParameterAnnotation(Decrypt.class);
        boolean b = b1 || b2;
        return b;
    }

    @Override
    public HttpInputMessage beforeBodyRead( HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        byte[] body = new byte[inputMessage.getBody().available()];
        inputMessage.getBody().read(body);
        try {
            String str = new String(body);
            String newStr = AESUtils.desEncrypt(str); // 解密加密串
            // str->inputstream
            /** 注意编码格式一定要指定UTF-8 不然会出现前后端解密错误 **/
            InputStream newInputStream = new ByteArrayInputStream(newStr.getBytes(StandardCharsets.UTF_8));
            return new HttpInputMessage() {
                @Override
                public InputStream getBody() throws IOException {
                    return newInputStream; // 返回解密串
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);

    }


}

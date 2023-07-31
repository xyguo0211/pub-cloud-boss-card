package com.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gateway.config.properties.IgnoreWhiteProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * 全局的ip白名单限制
 * 
 * @author ruoyi
 */
@Component
public class BlackUrlListGlobalFilter implements GlobalFilter, Ordered {

    


    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String host = exchange.getRequest().getURI().getHost();
        // 跳过不需要验证的路径
        Set<String> whites = ignoreWhite.getWhites();
        boolean contains = whites.contains(host);
        if(contains){
            return  setUnauthorizedResponse(exchange, "ip白名单限制");
        }

        return chain.filter(exchange);
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("code",-1);
            jsonObject.put("msg",msg);
            return bufferFactory.wrap(JSON.toJSONBytes(jsonObject));
        }));
    }

    @Override
    public int getOrder()
    {
        return -200;
    }
}
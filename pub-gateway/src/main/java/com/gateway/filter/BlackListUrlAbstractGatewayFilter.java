package com.gateway.filter;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 黑名单过滤器
 * 
 * 非全局过滤器
 */
@Component
public class BlackListUrlAbstractGatewayFilter extends AbstractGatewayFilterFactory<BlackListUrlAbstractGatewayFilter.Config>
{
    @Override
    public GatewayFilter apply(Config config)
    {
        return (exchange, chain) -> {

            String host = exchange.getRequest().getURI().getHost();
            if (config.matchBlacklist(host))
            {
                ServerHttpResponse response = exchange.getResponse();
                Map map = new  HashMap<String, Object>();
                map.put("code",-1);
                map.put("msg","局部黑名单IP限制");
                return exchange.getResponse().writeWith(
                        Mono.just(response.bufferFactory().wrap(JSON.toJSONBytes(map))));
            }

            return chain.filter(exchange);
        };
    }

    public BlackListUrlAbstractGatewayFilter()
    {
        super(Config.class);
    }

    public static class Config
    {
        private Set<String> blacklistUrl;


        public boolean matchBlacklist(String url)
        {
            return blacklistUrl.contains(url);
        }

        public Set<String> getBlacklistUrl()
        {
            return blacklistUrl;
        }

        public void setBlacklistUrl(Set<String> blacklistUrl)
        {
            this.blacklistUrl = blacklistUrl;
        }
    }

}

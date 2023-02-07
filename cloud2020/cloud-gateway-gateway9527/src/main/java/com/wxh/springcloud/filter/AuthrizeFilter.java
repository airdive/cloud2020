package com.wxh.springcloud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Author: liuchuang
 * Date: 2023/2/7
 * Time: 12:23
 * Declaration: All Rights Reserved !!!
 */
@Order(-1)
//定义过滤器顺序,越小优先级越高.或是实现Ordered接口中的getOrder是一样的
@Component
//定义组件,注入到Spring中
public class AuthrizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. 获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        //2. 获取参数中的authorization参数
        String authoriztion = queryParams.getFirst("authoriztion");
        //3. 判断参数值是否
        if("admin".equals(authoriztion)){
            // 4.是,放行
            return chain.filter(exchange);
        }
        // 5. 否.拦截
        // 5.1 设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // 5.2 拦截请求
        return exchange.getResponse().setComplete();
    }
}
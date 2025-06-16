package com.lycmoons.filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(-102)   // order 越小，越优先执行
// Security 的 filter 的默认 order 值可以在 application.yml 中配置

public class CorsFilter extends HttpFilter {
    // 跨域配置
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.addCorsHeader(request, response);
        chain.doFilter(request, response);
    }


    private void addCorsHeader(HttpServletRequest request, HttpServletResponse response) {
        // 前端请求源的限制
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        // 请求方法限制
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");

        // 允许自定义请求头通过
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }
}

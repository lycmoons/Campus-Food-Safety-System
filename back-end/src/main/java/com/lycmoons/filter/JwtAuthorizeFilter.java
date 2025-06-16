package com.lycmoons.filter;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lycmoons.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component // 检查请求头中的 token 信息
public class JwtAuthorizeFilter extends OncePerRequestFilter {
    @Resource
    JwtUtil jwtUtil;  // 自动填装 JWT 工具类

    @Override
    // 检查 token 信息
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头中的 token
        String headerToken = request.getHeader("Authorization");
        if (headerToken != null && headerToken.startsWith("Bearer ")) {
            DecodedJWT jwt = jwtUtil.resolveJwt(headerToken.substring(7));  // 去掉前置的 "Bearer "
            if (jwt != null) {
                // 解析得到 User 类
                UserDetails user = jwtUtil.getUser(jwt);

                // 解析出来的用户认证信息
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 放入 Security 的安全上下文中，方便后面业务中取用
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 后面可以通过request获取用户id
                request.setAttribute("id", jwtUtil.getUserid(jwt));
            }
        }
        filterChain.doFilter(request, response);
    }
}

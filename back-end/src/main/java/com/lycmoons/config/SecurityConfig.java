package com.lycmoons.config;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.AccountDTO;
import com.lycmoons.entity.vo.response.AuthorizeVO;
import com.lycmoons.filter.JwtAuthorizeFilter;
import com.lycmoons.mapper.AccountMapper;
import com.lycmoons.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.io.PrintWriter;


@Configuration
public class SecurityConfig {
    @Resource
    JwtUtil jwtUtil;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountMapper accountMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 请求拦截
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**",
                                         "/api/log/**",
                                         "/api/complaint/**",
                                         "/api/comment/**",
                                         "/api/report/**",
                                         "/api/food/**",
                                         "/api/email/**",
                                         "/api/news/**",
                                         "/api/post/**",
                                         "/api/knowledge/**",
                                         "/uploads/**",
                                         "/api/account/**")

                        .permitAll()
                        .anyRequest().authenticated()
                )

                // 登录处理
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )

                // 退出登录处理
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )

                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnAuthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )

                // CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 状态管理
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    // 验证 jwt 失败
    private void onUnAuthorized(HttpServletRequest request,
                                HttpServletResponse response,
                                AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401, authException.getMessage()).toJSON());
    }

    // 没有权限访问
    private void onAccessDeny(HttpServletRequest request,
                              HttpServletResponse response,
                              AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(403, accessDeniedException.getMessage()).toJSON());
    }

    // 登录成功响应
    private void onAuthenticationSuccess(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User user = (User) authentication.getPrincipal();  // 获取应用程序上下文中的用户认证信息

        // 再次查询用户，获取用户信息
        AccountDTO account = accountMapper.findAccountByUsername(user.getUsername());
        if (account == null) account = accountMapper.findAccountByEmail(user.getUsername());

        // 生成一个 token
        String token = jwtUtil.createJwt(user, account.getId(), account.getUsername());

        AuthorizeVO vo = new AuthorizeVO();
        vo.setToken(token);    // 令牌
        vo.setExpire(jwtUtil.expireTime());   // 过期时间
        vo.setRole(account.getRole());    // 用户角色
        vo.setUsername(account.getUsername());   // 用户名

        response.getWriter().write(RestBean.success(vo).toJSON());  // 发送响应
    }

    // 登录失败响应
    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(RestBean.failure(401, exception.getMessage()).toJSON());
    }

    // 退出登录成功响应
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        // 将 token 放入黑名单，让其失效（使用 redis ）
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            DecodedJWT jwt = jwtUtil.resolveJwt(token.substring(7));
            if(jwt != null){
                jwtUtil.turnBlack(jwt.getId(), jwt.getExpiresAt());
                writer.write(RestBean.success(null).toJSON());
            }
            else{  // 该 token 已失效
                writer.write(RestBean.failure(400,"登录已失效").toJSON());
            }
        }
        else{
            writer.write(RestBean.failure(400,"请求头信息缺失").toJSON());
        }
    }
}

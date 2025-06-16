package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.request.RegisterVO;
import com.lycmoons.entity.vo.request.ResetPasswordVO;
import com.lycmoons.entity.vo.response.PersonalInfoVO;
import com.lycmoons.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Resource
    AccountService accountService;

    // 用户注册
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody RegisterVO vo){
        String msg = accountService.register(vo);

        if(msg == null) return RestBean.success(null);
        else return RestBean.failure(400, msg);
    }

    // 用户重置密码
    @PostMapping("/reset-password")
    public RestBean<Void> resetPassword(@RequestBody ResetPasswordVO vo){
        String msg = accountService.resetPassword(vo);

        if(msg == null) return RestBean.success(null);
        else return RestBean.failure(400, msg);
    }

    // 获取用户个人信息
    @GetMapping("/person-info")
    public RestBean<PersonalInfoVO> getPersonalInfo(HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        PersonalInfoVO personalInfo = accountService.getPersonalInfo(userId);

        if(personalInfo == null) return RestBean.failure(400, "个人信息获取失败");
        return RestBean.success(personalInfo);
    }
}

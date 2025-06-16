package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.request.AskForCodeVO;
import com.lycmoons.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Resource
    EmailService emailService;

    // 获取邮箱验证码
    @PostMapping("/ask-code")
    public RestBean<Void> askCode(@RequestBody AskForCodeVO vo){
        String msg = emailService.askForCode(vo);

        if(msg == null) return RestBean.success(null);
        else return RestBean.failure(400, msg);
    }
}

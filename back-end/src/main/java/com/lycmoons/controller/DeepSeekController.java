package com.lycmoons.controller;

import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.request.AskDeepSeekVO;
import com.lycmoons.entity.vo.response.DeepSeekResponseVO;
import com.lycmoons.util.DeepSeekService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {
    @Resource
    DeepSeekService deepSeekService;

    @PostMapping("/ask")
    public RestBean<DeepSeekResponseVO> askDeepSeek(@RequestBody AskDeepSeekVO vo){
        String answer = deepSeekService.askQuestionAsync(vo.getQuestion()).join();
        return RestBean.success(new DeepSeekResponseVO(vo.getQuestion(), answer));
    }
}

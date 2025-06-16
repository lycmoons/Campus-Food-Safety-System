package com.lycmoons.entity.vo.response;

import lombok.Data;

@Data
public class DeepSeekResponseVO {
    String question;
    String answer;

    public DeepSeekResponseVO(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}

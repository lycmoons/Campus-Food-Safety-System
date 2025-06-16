package com.lycmoons.entity.vo.request;
import lombok.Data;

@Data
public class ResetPasswordVO {
    String password;
    String email;
    String code;
}

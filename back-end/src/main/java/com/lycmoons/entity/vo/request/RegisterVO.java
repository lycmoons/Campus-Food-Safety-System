package com.lycmoons.entity.vo.request;
import lombok.Data;

@Data
public class RegisterVO {
    String username;
    String password;
    String email;
    String code;
}

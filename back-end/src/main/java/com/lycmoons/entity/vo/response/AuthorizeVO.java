package com.lycmoons.entity.vo.response;
import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String token;
    Date expire;
    String username;
    String role;
}


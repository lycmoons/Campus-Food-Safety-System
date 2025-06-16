package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    Integer id;
    String username;
    String email;
    String password;
    String role;
}

package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class MessageDTO {
    Integer id;
    Integer user_id;
    String content;
    Date date;
}

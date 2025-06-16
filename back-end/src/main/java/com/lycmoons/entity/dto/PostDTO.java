package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class PostDTO {
    Integer id;
    String title;
    String content;
    String photo_url;
    Integer user_id;
    Date date;
}
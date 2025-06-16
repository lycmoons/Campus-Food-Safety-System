package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class ComplaintDTO {
    Integer id;
    Integer user_id;
    String content;
    String photo_url;
    Date date;
    String accepted;
    Float score;
}

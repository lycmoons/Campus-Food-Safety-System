package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class CommentDTO {
    Integer id;
    Integer user_id;
    Integer post_id;
    String content;
    Date date;
}

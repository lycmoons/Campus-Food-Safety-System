package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class NewsDTO {
    Integer id;
    String title;
    String content;
    Date date;
    String photo_url;
}

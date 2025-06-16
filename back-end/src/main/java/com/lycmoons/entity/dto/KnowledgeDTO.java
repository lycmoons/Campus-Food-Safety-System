package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class KnowledgeDTO {
    Integer id;
    String category;
    String content;
    String photo_url;
    String title;
    Date date;
}

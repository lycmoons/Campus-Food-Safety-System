package com.lycmoons.entity.vo.response;

import com.lycmoons.entity.dto.KnowledgeDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;
import java.util.List;

@Data
public class KnowledgeVO {
    Integer id;
    String title;
    String content;
    String category;
    List<String> photo_url;
    String date;

    public KnowledgeVO(KnowledgeDTO dto){
        id = dto.getId();
        title = dto.getTitle();
        content = dto.getContent();
        category = dto.getCategory();
        photo_url = Convertor.convertPhotoUrl(dto.getPhoto_url());
        date = Convertor.convertDate(dto.getDate());
    }
}

package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.NewsDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;
import java.util.List;

@Data
public class NewsVO {
    Integer id;
    String title;
    String content;
    List<String> photo_url;
    String date;


    // 将 News 数据库实体数据转化为响应数据
    public NewsVO(NewsDTO dto){
        id = dto.getId();
        title = dto.getTitle();
        content = dto.getContent();
        photo_url = Convertor.convertPhotoUrl(dto.getPhoto_url());
        date = Convertor.convertDate(dto.getDate());
    }
}

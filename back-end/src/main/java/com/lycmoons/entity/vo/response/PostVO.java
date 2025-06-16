package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.PostDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;
import java.util.List;

@Data
public class PostVO {
    Integer id;
    String username;
    String title;
    String content;
    List<String> photo_url;
    String date;

    public PostVO(PostDTO dto, String username){
        id = dto.getId();
        this.username = username;
        title = dto.getTitle();
        content = dto.getContent();
        photo_url = Convertor.convertPhotoUrl(dto.getPhoto_url());
        date = Convertor.convertDate(dto.getDate());
    }
}

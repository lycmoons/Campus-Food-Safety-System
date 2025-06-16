package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.ComplaintDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;
import java.util.List;

@Data
public class ComplaintVO {
    Integer id;
    String content;
    List<String> photo_url;
    String date;
    String accepted;
    Float score;

    public ComplaintVO(ComplaintDTO dto){
        id = dto.getId();
        content = dto.getContent();
        photo_url = Convertor.convertPhotoUrl(dto.getPhoto_url());
        date = Convertor.convertDate(dto.getDate());
        accepted = dto.getAccepted();
        score = dto.getScore();
    }
}

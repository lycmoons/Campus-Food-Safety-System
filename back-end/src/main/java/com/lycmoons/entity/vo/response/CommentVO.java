package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.CommentDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;

@Data
public class CommentVO {
    Integer id;
    String username;
    String content;
    String date;

    public CommentVO(CommentDTO dto, String username){
        id = dto.getId();
        this.username = username;
        content = dto.getContent();
        date = Convertor.convertDate(dto.getDate());
    }
}

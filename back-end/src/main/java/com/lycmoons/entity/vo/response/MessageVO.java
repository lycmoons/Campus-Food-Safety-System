package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.MessageDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;

@Data
public class MessageVO {
    Integer id;
    Integer user_id;
    String content;
    String date;

    public MessageVO(MessageDTO dto){
        id = dto.getId();
        user_id = dto.getUser_id();
        content = dto.getContent();
        date = Convertor.convertDate(dto.getDate());
    }
}

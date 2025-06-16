package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;

@Data
public class LogVO {
    Integer id;
    String table_name;
    String message;
    String date;

    public LogVO(LogDTO dto){
        id = dto.getId();
        table_name = dto.getTable_name();
        message = dto.getMessage();
        date = Convertor.convertDate(dto.getDate());
    }
}

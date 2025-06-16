package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class LogDTO {
    Integer id;
    Integer admin_id;
    String table_name;
    String message;
    Date date;
}

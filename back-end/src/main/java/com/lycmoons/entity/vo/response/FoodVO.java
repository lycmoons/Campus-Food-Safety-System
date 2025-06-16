package com.lycmoons.entity.vo.response;
import com.lycmoons.entity.dto.FoodDTO;
import com.lycmoons.util.Convertor;
import lombok.Data;


@Data
public class FoodVO {
    Integer id;
    String name;
    String photo_url;

    public FoodVO(FoodDTO dto){
        id = dto.getId();
        name = dto.getName();
        photo_url = dto.getPhoto_url();
    }
}

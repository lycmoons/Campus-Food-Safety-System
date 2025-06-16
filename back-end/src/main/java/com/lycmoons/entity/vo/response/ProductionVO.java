package com.lycmoons.entity.vo.response;


import com.lycmoons.entity.dto.FoodDTO;
import lombok.Data;

import java.util.Date;

@Data
public class ProductionVO {
    String manufacturer;
    String batch_num;
    Date product_date;
    Integer shelf_life;

    public ProductionVO(FoodDTO dto) {
        manufacturer = dto.getManufacturer();
        batch_num = dto.getBatch_num();
        product_date = dto.getProduct_date();
        shelf_life = dto.getShelf_life();
    }
}

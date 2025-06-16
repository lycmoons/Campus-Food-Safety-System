package com.lycmoons.entity.vo.response;

import com.lycmoons.entity.dto.ConsumerDTO;
import lombok.Data;

@Data
public class ConsumerVO {
    String food_counter;
    String dish;

    public ConsumerVO(ConsumerDTO dto) {
        this.food_counter = dto.getFood_counter();
        this.dish = dto.getDish();
    }
}

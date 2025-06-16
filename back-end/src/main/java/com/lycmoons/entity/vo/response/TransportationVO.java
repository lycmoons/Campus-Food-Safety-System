package com.lycmoons.entity.vo.response;

import com.lycmoons.entity.dto.FoodDTO;
import lombok.Data;

import java.util.Date;

@Data
public class TransportationVO {
    String carrier;
    Date start_time;
    Date end_time;
    Float temperature;
    Float humidity;

    public TransportationVO(FoodDTO dto){
        this.carrier=dto.getCarrier();
        this.start_time=dto.getStart_time();
        this.end_time=dto.getEnd_time();
        this.temperature=dto.getTemperature();
        this.humidity=dto.getHumidity();
    }
}

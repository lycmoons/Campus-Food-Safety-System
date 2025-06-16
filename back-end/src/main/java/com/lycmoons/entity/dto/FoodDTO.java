package com.lycmoons.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class FoodDTO {
    Integer id;
    String name;
    String manufacturer;
    String batch_num;
    Date product_date;
    Integer shelf_life;
    String carrier;
    Date start_time;
    Date end_time;
    String photo_url;
    Float temperature;
    Float humidity;
}

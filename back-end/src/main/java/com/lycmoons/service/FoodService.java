package com.lycmoons.service;
import com.lycmoons.entity.dto.ConsumerDTO;
import com.lycmoons.entity.dto.FoodDTO;
import com.lycmoons.entity.vo.request.DeleteFoodVO;
import com.lycmoons.entity.vo.response.ConsumerVO;
import com.lycmoons.entity.vo.response.FoodVO;
import com.lycmoons.entity.vo.response.ProductionVO;
import com.lycmoons.entity.vo.response.TransportationVO;

import java.util.List;

public interface FoodService {
    // 获取食品id、name、photo_url
    List<FoodVO> getFoodList();

    // 获取食品生产端信息
    ProductionVO getProductionById(Integer id);

    // 获取食品运输信息
    TransportationVO getTransportationById(Integer id);

    // 获取食品消费端信息
    List<ConsumerVO> getConsumerById(Integer id);

    // 删除一系列食品信息
    String deleteFood(DeleteFoodVO vo, Integer adminId);

    // 插入食品信息
    String insertFood(FoodDTO dto, Integer adminId);

    // 为指定食品插入consumer信息
    String insertConsumer(ConsumerDTO dto, Integer adminId);

    // 删除指定的consumer信息（一条）
    String deleteConsumer(Integer id, String food_counter, String dish, Integer adminId);
}

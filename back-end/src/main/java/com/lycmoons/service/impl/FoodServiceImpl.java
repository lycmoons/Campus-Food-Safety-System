package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.ConsumerDTO;
import com.lycmoons.entity.dto.FoodDTO;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.dto.MessageDTO;
import com.lycmoons.entity.vo.request.DeleteFoodVO;
import com.lycmoons.entity.vo.response.ConsumerVO;
import com.lycmoons.entity.vo.response.FoodVO;
import com.lycmoons.entity.vo.response.ProductionVO;
import com.lycmoons.entity.vo.response.TransportationVO;
import com.lycmoons.mapper.ConsumerMapper;
import com.lycmoons.mapper.FoodMapper;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.mapper.MessageMapper;
import com.lycmoons.service.FoodService;
import com.lycmoons.util.Convertor;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    @Resource
    FoodMapper foodMapper;

    @Resource
    LogMapper logMapper;

    @Resource
    ConsumerMapper consumerMapper;

    @Override
    public List<FoodVO> getFoodList() {
        List<FoodDTO> allFood = foodMapper.getAllFood();
        List<FoodVO> foodVOList = new ArrayList<>();
        for(FoodDTO foodDTO:allFood){
            foodVOList.add(new FoodVO(foodDTO));
        }
        return foodVOList;
    }

    @Override
    public ProductionVO getProductionById(Integer id) {
        FoodDTO food = foodMapper.getFoodById(id);
        return new ProductionVO(food);
    }

    @Override
    public TransportationVO getTransportationById(Integer id) {
        FoodDTO food = foodMapper.getFoodById(id);
        return new TransportationVO(food);
    }

    @Override
    public List<ConsumerVO> getConsumerById(Integer id) {
        List<ConsumerDTO> consumer = consumerMapper.findById(id);
        List<ConsumerVO> consumerVOList = new ArrayList<>();
        for(ConsumerDTO consumerDTO:consumer){
            consumerVOList.add(new ConsumerVO(consumerDTO));
        }
        return consumerVOList;
    }

    @Override
    public String deleteFood(DeleteFoodVO vo, Integer adminId) {
        for (Integer foodId : vo.getFoodIds()) {
            foodMapper.deleteFoodById(foodId);
            consumerMapper.deleteById(foodId);
        }
        logMapper.addLog(new LogDTO(null, adminId, "Food", "食品信息删除", new Date()));
        logMapper.addLog(new LogDTO(null, adminId, "Consumer", "食品信息删除", new Date()));
        return null;
    }

    @Override
    public String insertFood(FoodDTO dto, Integer adminId) {
        int cnt = foodMapper.insertFood(dto);
        if(cnt == 0){
            return "食品信息插入失败";
        }
        logMapper.addLog(new LogDTO(null, adminId, "Food", "食品信息插入", new Date()));
        return null;
    }

    @Override
    public String insertConsumer(ConsumerDTO dto, Integer adminId) {
        int cnt = consumerMapper.insert(dto);
        if(cnt == 0){
            return "菜品信息插入失败";
        }
        logMapper.addLog(new LogDTO(null, adminId, "Consumer", "菜品信息插入", new Date()));
        return null;
    }

    @Override
    public String deleteConsumer(Integer id, String food_counter, String dish, Integer adminId) {
        int cnt = consumerMapper.delete(id, food_counter, dish);
        if(cnt == 0){
            return "菜品信息删除失败";
        }
        logMapper.addLog(new LogDTO(null, adminId, "Consumer", "菜品信息删除", new Date()));
        return null;
    }
}

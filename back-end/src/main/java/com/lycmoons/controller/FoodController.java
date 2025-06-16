package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.ConsumerDTO;
import com.lycmoons.entity.dto.FoodDTO;
import com.lycmoons.entity.vo.request.AskForFood;
import com.lycmoons.entity.vo.request.DeleteFoodVO;
import com.lycmoons.entity.vo.response.ConsumerVO;
import com.lycmoons.entity.vo.response.FoodVO;
import com.lycmoons.entity.vo.response.ProductionVO;
import com.lycmoons.entity.vo.response.TransportationVO;
import com.lycmoons.mapper.FoodMapper;
import com.lycmoons.service.FoodService;
import com.lycmoons.service.PhotoService;
import com.lycmoons.util.Convertor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Resource
    FoodService foodService;


    @Resource
    PhotoService photoService;

    @Resource
    FoodMapper foodMapper;

    // 获取所有食物名称、图片信息
    @GetMapping("/all-food")
    public RestBean<List<FoodVO>> getAllFood(HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        List<FoodVO> foodList = foodService.getFoodList();
        if (foodList.isEmpty()) {
            return RestBean.failure(400, "为找到食品信息");
        }
        return RestBean.success(foodList);
    }

    // 获取指定食品的生产端信息
    @PostMapping("/production-info")
    public RestBean<ProductionVO> getProduction(HttpServletRequest request,
                                                @RequestBody AskForFood vo) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        ProductionVO production = foodService.getProductionById(vo.getId());
        if (production == null) {
            return RestBean.failure(400, "未找到食品的生产信息");
        }
        return RestBean.success(production);
    }

    // 获取指定食品的运输端信息
    @PostMapping("/transportation-info")
    public RestBean<TransportationVO> getTransportation(HttpServletRequest request,
                                                        @RequestBody AskForFood vo) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        TransportationVO trans = foodService.getTransportationById(vo.getId());
        if (trans == null) {
            return RestBean.failure(400, "未找到食品的运输信息");
        }
        return RestBean.success(trans);
    }

    // 获取指定食品的消费端信息
    @PostMapping("/consumer-info")
    public RestBean<List<ConsumerVO>> getConsumers(HttpServletRequest request,
                                                   @RequestBody AskForFood vo) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        List<ConsumerVO> consumer = foodService.getConsumerById(vo.getId());
        if (consumer.isEmpty()) {
            return RestBean.failure(400, "未找到食品的消费信息");
        }
        return RestBean.success(consumer);
    }


    // 删除一系列食品信息
    @PostMapping("/delete-food")
    public RestBean<Void> deleteFood(HttpServletRequest request,
                                     @RequestBody DeleteFoodVO vo) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }

        // 删除图片
        for(Integer id : vo.getFoodIds()) {
            FoodDTO f = foodMapper.getFoodById(id);
            photoService.deletePhoto(f.getPhoto_url());
        }

        // 删除数据库存储
        String msg = foodService.deleteFood(vo, adminId);
        if (msg == null){
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }

    // 插入食品信息
    @PostMapping("/add-food")
    public RestBean<Void> addFood(HttpServletRequest request,
                                  @RequestParam("name") String name,
                                  @RequestParam("manufacturer") String manufacturer,
                                  @RequestParam("batch_num") String batch_num,
                                  @RequestParam("product_date") String product_date,
                                  @RequestParam("shelf_life") Integer shelf_life,
                                  @RequestParam("carrier") String carrier,
                                  @RequestParam("start_time") String start_time,
                                  @RequestParam("end_time") String end_time,
                                  @RequestParam("image") MultipartFile[] image,
                                  @RequestParam("temperature") Float temperature,
                                  @RequestParam("humidity") Float humidity) throws ParseException, IOException {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        Date productDate = Convertor.convertDateString(product_date);
        Date startDate = Convertor.convertDateString(start_time);
        Date endDate = Convertor.convertDateString(end_time);
        String photo_url = photoService.storePhoto(image);
        String msg = foodService.insertFood(new FoodDTO(null, name, manufacturer, batch_num, productDate, shelf_life, carrier, startDate, endDate, photo_url, temperature, humidity), adminId);
        if (msg == null){
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }


    // 插入一条consumer信息
    @PostMapping("/add-consumer")
    public RestBean<Void> addConsumer(HttpServletRequest request,
                                      @RequestBody ConsumerDTO dto) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        String msg = foodService.insertConsumer(dto, adminId);
        if (msg == null){
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }

    // 删除一条consumer信息
    @PostMapping("/delete-consumer")
    public RestBean<Void> deleteConsumer(HttpServletRequest request,
                                      @RequestBody ConsumerDTO dto) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }
        String msg = foodService.deleteConsumer(dto.getId(), dto.getFood_counter(), dto.getDish(), adminId);
        if (msg == null){
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }
}
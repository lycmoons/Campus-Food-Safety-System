package com.lycmoons.mapper;
import com.lycmoons.entity.dto.FoodDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface FoodMapper {
    // 获取整个 Food 表的内容
    @Select("select * from Food")
    List<FoodDTO> getAllFood();

    // 获取某一个 Food 的信息
    @Select("select * from Food where id = #{id}")
    FoodDTO getFoodById(@Param("id") Integer id);

    // 删除一条食品数据
    @Delete("delete from Food where id = #{id}")
    int deleteFoodById(@Param("id") Integer id);

    // 插入一条食品信息
    @Insert("insert into Food values (#{id}, #{name}, #{manufacturer}, #{batch_num}, #{product_date}, #{shelf_life}, #{carrier}, #{start_time}, #{end_time}, #{photo_url}, #{temperature}, #{humidity})")
    int insertFood(FoodDTO food);
}

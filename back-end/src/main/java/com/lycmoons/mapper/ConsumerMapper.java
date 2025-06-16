package com.lycmoons.mapper;

import com.lycmoons.entity.dto.ConsumerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConsumerMapper {
    // 获取指定食品对应的菜品
    @Select("select * from Consumer where id = #{id};")
    List<ConsumerDTO> findById(Integer id);

    // 删除指定id的菜品
    @Delete("delete from Consumer where id = #{id}")
    int deleteById(Integer id);

    // 删除指定菜品的consumer信息
    @Delete("delete from Consumer where id = #{id} and food_counter = #{food_counter} and dish = #{dish}")
    int delete(@Param("id") Integer id, @Param("food_counter") String food_counter, @Param("dish") String dish);

    // 插入一条consumer信息
    @Insert("insert into Consumer values (#{id}, #{food_counter}, #{dish})")
    int insert(ConsumerDTO consumerDTO);
}

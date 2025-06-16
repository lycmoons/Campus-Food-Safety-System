package com.lycmoons.mapper;
import com.lycmoons.entity.dto.ReportDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ReportMapper {
    // 获取食品安全报告的 url
    @Select("select * from Report")
    ReportDTO getReportPhoto();

    // 修改食品报告
    @Update("update Report set photo_url = #{photo_url}")
    int updatePhotoUrl(@Param("photo_url") String photoUrl);
}


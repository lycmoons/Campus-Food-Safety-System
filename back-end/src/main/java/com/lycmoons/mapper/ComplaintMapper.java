package com.lycmoons.mapper;
import com.lycmoons.entity.dto.ComplaintDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ComplaintMapper {
    // 查找指定用户的投诉信息
    @Select("select * from Complaint where user_id = #{id} order by id desc")
    List<ComplaintDTO> getComplaintByUserId(@Param("id") Integer id);

    // 插入一条投诉信息
    @Insert("insert into Complaint values (#{id}, #{user_id}, #{content}, #{photo_url}, #{date}, #{accepted}, #{score})")
    int addComplaint(ComplaintDTO dto);

    // 获取所有的投诉信息，根据评分升序排序
    @Select("select * from Complaint order by score")
    List<ComplaintDTO> getAllComplaint();

    // 根据投诉的 id 来查找指定的投诉信息
    @Select("select * from Complaint where id = #{id}")
    ComplaintDTO getComplaintById(@Param("id") Integer id);

    // 将投诉标记为已经受理
    @Update("update Complaint set accepted = 'true' where id = #{id}")
    int acceptComplaint(@Param("id") Integer id);
}

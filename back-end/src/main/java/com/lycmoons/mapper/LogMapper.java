package com.lycmoons.mapper;
import com.lycmoons.entity.dto.LogDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface LogMapper {
    // 插入一条管理员日志
    @Insert("insert into Log values (#{id}, #{admin_id}, #{table_name}, #{message}, #{date})")
    int addLog(LogDTO dto);

    // 查找某个管理员的所有日志信息
    @Select("select * from Log where admin_id = #{id} order by id desc")
    List<LogDTO> getLogByAdminId(@Param("id") Integer id);
}

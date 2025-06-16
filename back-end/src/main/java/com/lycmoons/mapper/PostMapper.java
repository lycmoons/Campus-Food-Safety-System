package com.lycmoons.mapper;
import com.lycmoons.entity.dto.PostDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PostMapper {
    // 获取热门帖子的推送
    @Select("select * from Post order by id desc limit 3")
    List<PostDTO> getTopPost();

    // 插入一条帖子信息
    @Insert("insert into Post values (#{id}, #{title}, #{content}, #{photo_url}, #{user_id}, #{date})")
    int addPost(PostDTO postDTO);

    // 获取所有的帖子信息
    @Select("select * from Post order by id desc")
    List<PostDTO> getAllPost();
}

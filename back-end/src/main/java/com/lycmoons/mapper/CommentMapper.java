package com.lycmoons.mapper;
import com.lycmoons.entity.dto.CommentDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 获取指定帖子的所有评论
    @Select("select * from Comment where post_id = #{id} order by id desc")
    List<CommentDTO> getCommentByPostId(Integer id);

    // 插入一条评论
    @Insert("insert into Comment values (#{id}, #{user_id}, #{post_id}, #{content}, #{date})")
    int addComment(CommentDTO commentDTO);
}

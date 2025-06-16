package com.lycmoons.mapper;
import com.lycmoons.entity.dto.NewsDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface NewsMapper {
    // 获取热门新闻的推送
    @Select("select * from News order by id desc limit 3")
    List<NewsDTO> getTopNews();

    // 获取一条新闻信息
    @Select("select * from News where id = #{id}")
    NewsDTO getNewsById(Integer id);

    // 获取所有的新闻信息
    @Select("select * from News order by id desc")
    List<NewsDTO> getAllNews();

    // 删除某条新闻内容
    @Delete("delete from News where id = #{id}")
    int deleteNews(Integer id);

    // 插入一条新闻信息
    @Insert("insert into News values (#{id}, #{title}, #{content}, #{date}, #{photo_url})")
    int insertNews(NewsDTO newsDTO);
}

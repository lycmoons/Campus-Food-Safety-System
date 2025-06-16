package com.lycmoons.mapper;
import com.lycmoons.entity.dto.KnowledgeDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface KnowledgeMapper {
    // 获取热门知识的推送
    @Select("select * from Knowledge order by id desc limit 3")
    List<KnowledgeDTO> getTopKnowledge();

    // 获取所有知识
    @Select("select * from Knowledge order by id")
    List<KnowledgeDTO> getAllKnowledge();

    // 获取一条知识
    @Select("select * from Knowledge where id = #{id}")
    KnowledgeDTO getKnowledgeById(Integer id);

    // 删除一条知识信息
    @Delete("delete from Knowledge where id = #{id}")
    int deleteKnowledge(Integer id);

    // 插入一条知识信息
    @Insert("insert into Knowledge values (#{id}, #{category}, #{content}, #{photo_url}, #{title}, #{date})")
    int insertKnowledge(KnowledgeDTO knowledgeDTO);
}

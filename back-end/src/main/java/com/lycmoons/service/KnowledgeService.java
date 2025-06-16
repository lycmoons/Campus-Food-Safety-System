package com.lycmoons.service;
import com.lycmoons.entity.vo.request.DeleteKnowledgeVO;
import com.lycmoons.entity.vo.response.KnowledgeVO;
import java.util.List;

public interface KnowledgeService {
    List<KnowledgeVO> getTopKnowledge();
    List<KnowledgeVO> getAllKnowledge();
    String deleteKnowledge(DeleteKnowledgeVO vo, Integer adminId);
    String addKnowledge(String title, String content, String category, String photo_url, Integer adminId);
}

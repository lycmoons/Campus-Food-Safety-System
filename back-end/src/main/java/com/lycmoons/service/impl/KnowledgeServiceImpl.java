package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.KnowledgeDTO;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.vo.request.DeleteKnowledgeVO;
import com.lycmoons.entity.vo.response.KnowledgeVO;
import com.lycmoons.mapper.KnowledgeMapper;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.service.KnowledgeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {
    @Resource
    KnowledgeMapper knowledgeMapper;

    @Resource
    LogMapper logMapper;

    @Override
    public List<KnowledgeVO> getTopKnowledge() {
        List<KnowledgeDTO> topKnowledge = knowledgeMapper.getTopKnowledge();
        if(topKnowledge == null || topKnowledge.isEmpty()){
            return null;
        }

        List<KnowledgeVO> knowledgeVO = new ArrayList<>();
        for (KnowledgeDTO knowledgeDTO : topKnowledge) {
            knowledgeVO.add(new KnowledgeVO(knowledgeDTO));
        }

        return knowledgeVO;
    }

    @Override
    public List<KnowledgeVO> getAllKnowledge() {
        List<KnowledgeDTO> allKnowledge = knowledgeMapper.getAllKnowledge();
        if(allKnowledge == null || allKnowledge.isEmpty()){
            return null;
        }

        List<KnowledgeVO> knowledgeVO = new ArrayList<>();
        for (KnowledgeDTO knowledgeDTO : allKnowledge) {
            knowledgeVO.add(new KnowledgeVO(knowledgeDTO));
        }

        return knowledgeVO;
    }

    @Override
    public String deleteKnowledge(DeleteKnowledgeVO vo, Integer adminId) {
        int cnt = 0;
        for(Integer id: vo.getKnowledgeIds()){
            cnt+= knowledgeMapper.deleteKnowledge(id);
        }
        if(cnt<=0){
            return "知识信息删除失败";
        }

        logMapper.addLog(new LogDTO(null,adminId,"Knowledge","知识信息删除",new Date()));
        return null;
    }

    @Override
    public String addKnowledge(String title, String content, String category, String photo_url, Integer adminId) {
        int cnt = knowledgeMapper.insertKnowledge(new KnowledgeDTO(null,category,content,photo_url,title,new Date()));
        if(cnt<=0){
            return "知识信息录入失败";
        }

        logMapper.addLog(new LogDTO(null,adminId,"Knowledge","知识信息录入",new Date()));
        return null;
    }
}

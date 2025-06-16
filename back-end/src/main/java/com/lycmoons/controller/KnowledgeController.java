package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.KnowledgeDTO;
import com.lycmoons.entity.vo.request.DeleteKnowledgeVO;
import com.lycmoons.entity.vo.response.KnowledgeVO;
import com.lycmoons.mapper.KnowledgeMapper;
import com.lycmoons.service.KnowledgeService;
import com.lycmoons.service.PhotoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    @Resource
    KnowledgeService knowledgeService;

    @Resource
    PhotoService photoService;

    @Resource
    KnowledgeMapper knowledgeMapper;

    // 知识推送
    @GetMapping("/top-knowledge")
    public RestBean<List<KnowledgeVO>> getTopKnowledge() {
        List<KnowledgeVO> topKnowledge = knowledgeService.getTopKnowledge();

        if (topKnowledge == null) {
            return RestBean.failure(400, "知识推送失败");
        }
        return RestBean.success(topKnowledge);
    }

    // 获取所有的知识
    @GetMapping("/all-knowledge")
    public RestBean<List<KnowledgeVO>> getAllKnowledge() {
        List<KnowledgeVO> allKnowledge = knowledgeService.getAllKnowledge();

        if (allKnowledge == null) {
            return RestBean.failure(400, "未找到任何知识信息");
        }
        return RestBean.success(allKnowledge);
    }

    // 删除一系列知识
    @PostMapping("/delete-knowledge")
    public RestBean<Void> deleteKnowledge(HttpServletRequest request,
                                          @RequestBody DeleteKnowledgeVO vo
    ) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null) {
            return RestBean.failure(400, "身份认证失败");
        }

        // 删除图片
        for(Integer id : vo.getKnowledgeIds()) {
            KnowledgeDTO k = knowledgeMapper.getKnowledgeById(id);
            photoService.deletePhoto(k.getPhoto_url());
        }

        // 删除数据库存储
        String msg = knowledgeService.deleteKnowledge(vo,adminId);
        if (msg == null) {
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }

    // 录入一条知识信息
    @PostMapping("/add-knowledge")
    public RestBean<Void> addKnowledge(HttpServletRequest request,
                                       @RequestParam("image") MultipartFile[] image,
                                       @RequestParam("title") String title,
                                       @RequestParam("content") String content,
                                       @RequestParam("category") String category
    ){
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null) {
            return RestBean.failure(400, "身份认证失败");
        }

        try {
            String photo_url = photoService.storePhoto(image);
            String msg = knowledgeService.addKnowledge(title,content,category,photo_url,adminId);
            if (msg == null) {
                return RestBean.success(null);
            }
            return RestBean.failure(400, msg);
        } catch (IOException e) {
            return RestBean.failure(400, e.getMessage());
        }
    }
}

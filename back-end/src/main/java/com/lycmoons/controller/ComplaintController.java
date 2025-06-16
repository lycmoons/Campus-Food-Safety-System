package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.ComplaintDTO;
import com.lycmoons.entity.vo.request.AcceptComplaintVO;
import com.lycmoons.entity.vo.response.ComplaintVO;
import com.lycmoons.service.ComplaintService;
import com.lycmoons.service.PhotoService;
import com.lycmoons.util.SentimentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    @Resource
    ComplaintService complaintService;

    @Resource
    PhotoService photoService;

    @Resource
    SentimentService sentimentService;

    // 获取指定用户的投诉信息
    @GetMapping("/all-complaint")
    public RestBean<List<ComplaintVO>> getUserComplaint(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");

        List<ComplaintVO> complaints = complaintService.getComplaintByUserId(userId);
        if (complaints == null) {
            return RestBean.failure(400, "未找到任何投诉信息");
        }

        return RestBean.success(complaints);
    }

    // 发送投诉
    @PostMapping("/send-complaint")
    public RestBean<Void> sendComplaint(
            HttpServletRequest request,
            @RequestPart("files") MultipartFile[] files, // 接收多个文件
            @RequestParam("content") String content     // 接收内容字段
    ) {
        try {
            String photo_url = photoService.storePhoto(files);
            Integer userId = (Integer) request.getAttribute("id");
            if(content!=null&&photo_url!=null&&userId!=null){
                sentimentService.getSentimentScoreAsync(content)
                        .thenAccept(score -> {  // 成功获取 score
                            String msg = complaintService.addComplaint(new ComplaintDTO(null, userId, content, photo_url, new Date(), "false", score));
                        })
                        .exceptionally(ex -> {  // 获取 score 失败
                            System.err.println("请求失败：" + ex.getMessage());
                            return null;
                        });
                    return RestBean.success(null);
            }
            return RestBean.failure(400, "投诉发送失败");
        } catch (IOException e) {
            return RestBean.failure(400, e.getMessage());
        }
    }

    // 管理员获取所有用户的投诉信息
    @GetMapping("/total-complaint")
    public RestBean<List<ComplaintVO>> getAllComplaint() {
        List<ComplaintVO> complaints = complaintService.getAllComplaint();
        if (complaints == null) {
            return RestBean.failure(400, "未找到任何投诉信息");
        }
        return RestBean.success(complaints);
    }

    // 管理员回复某个投诉
    @PostMapping("/accept-complaint")
    public RestBean<Void> acceptComplaint(HttpServletRequest request,
                                          @RequestBody AcceptComplaintVO vo){
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null) {
            return RestBean.failure(400, "身份认证失败");
        }

        String msg = complaintService.acceptComplaint(vo, adminId);
        if (msg == null) {
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }
}

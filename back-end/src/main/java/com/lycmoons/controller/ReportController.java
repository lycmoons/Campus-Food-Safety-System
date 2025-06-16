package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.ReportDTO;
import com.lycmoons.service.PhotoService;
import com.lycmoons.service.ReportService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Resource
    ReportService reportService;

    @Resource
    PhotoService photoService;

    // 获取食品安全报告
    @GetMapping("/get-report")
    public RestBean<ReportDTO> getReport() {
        ReportDTO photo = reportService.getReportPhoto();

        if (photo == null){
            return RestBean.failure(400, "食品安全报告获取失败");
        }
        return RestBean.success(photo);
    }

    // 更新食品安全报告
    @PostMapping("/update-report")
    public RestBean<Void> updateReport(HttpServletRequest request,
                                       @RequestParam("image") MultipartFile[] image) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null){
            return RestBean.failure(400, "身份认证失败");
        }

        // 上传图片
        try {
            String photo_url = photoService.storePhoto(image);

            // 删除原本图片
            ReportDTO r = reportService.getReportPhoto();
            photoService.deletePhoto(r.getPhoto_url());

            String msg = reportService.updateReportPhoto(photo_url, adminId);
            if (msg == null){
                return RestBean.success(null);
            }
            else {
                return RestBean.failure(400, msg);
            }
        } catch (IOException e) {
            return RestBean.failure(400, e.getMessage());
        }
    }
}
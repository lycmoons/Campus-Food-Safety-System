package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.dto.ReportDTO;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.mapper.ReportMapper;
import com.lycmoons.service.ReportService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    ReportMapper reportMapper;

    @Resource
    LogMapper logMapper;

    @Override
    public ReportDTO getReportPhoto() {
        return reportMapper.getReportPhoto();
    }

    @Override
    public String updateReportPhoto(String photo_url, Integer adminId) {
        int cnt = reportMapper.updatePhotoUrl(photo_url);
        if (cnt > 0) {
            // 管理员日志
            logMapper.addLog(new LogDTO(null, adminId, "Report", "食品安全报告更新", new Date()));
            return null;
        }
        else {
            return "食品报告更新失败";
        }
    }
}

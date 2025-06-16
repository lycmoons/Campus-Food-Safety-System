package com.lycmoons.service;
import com.lycmoons.entity.dto.ReportDTO;

public interface ReportService {
    ReportDTO getReportPhoto();
    String updateReportPhoto(String photo_url, Integer adminId);
}




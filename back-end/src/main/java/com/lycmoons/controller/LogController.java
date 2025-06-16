package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.response.LogVO;
import com.lycmoons.service.LogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Resource
    LogService logService;

    // 获取指定管理员的操作日志
    @GetMapping("/all-log")
    public RestBean<List<LogVO>> getAllLog(HttpServletRequest request) {
        Integer adminId = (Integer) request.getAttribute("id");
        List<LogVO> logs = logService.getLogListByAdminId(adminId);
        if (logs == null) {
            return RestBean.failure(400, "未找到任何日志信息");
        }
        return RestBean.success(logs);
    }
}

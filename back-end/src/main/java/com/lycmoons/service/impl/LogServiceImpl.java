package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.vo.response.LogVO;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.service.LogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Resource
    LogMapper logMapper;

    @Override
    public List<LogVO> getLogListByAdminId(Integer adminId) {
        List<LogDTO> logs = logMapper.getLogByAdminId(adminId);
        if(logs == null || logs.isEmpty()){
            return null;
        }

        List<LogVO> logVOS = new ArrayList<>();
        for (LogDTO dto : logs) {
            logVOS.add(new LogVO(dto));
        }
        return logVOS;
    }
}

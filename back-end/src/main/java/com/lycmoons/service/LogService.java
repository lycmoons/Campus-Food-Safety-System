package com.lycmoons.service;
import com.lycmoons.entity.vo.response.LogVO;
import java.util.List;

public interface LogService {
    List<LogVO> getLogListByAdminId(Integer adminId);
}

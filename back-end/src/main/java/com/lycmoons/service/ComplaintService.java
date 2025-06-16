package com.lycmoons.service;
import com.lycmoons.entity.dto.ComplaintDTO;
import com.lycmoons.entity.vo.request.AcceptComplaintVO;
import com.lycmoons.entity.vo.response.ComplaintVO;
import java.util.List;

public interface ComplaintService {
    List<ComplaintVO> getComplaintByUserId(Integer userId);
    String addComplaint(ComplaintDTO dto);
    List<ComplaintVO> getAllComplaint();
    String acceptComplaint(AcceptComplaintVO vo, Integer adminId);
}

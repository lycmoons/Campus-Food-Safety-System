package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.ComplaintDTO;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.dto.MessageDTO;
import com.lycmoons.entity.vo.request.AcceptComplaintVO;
import com.lycmoons.entity.vo.response.ComplaintVO;
import com.lycmoons.mapper.ComplaintMapper;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.mapper.MessageMapper;
import com.lycmoons.service.ComplaintService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Resource
    ComplaintMapper complaintMapper;

    @Resource
    MessageMapper messageMapper;

    @Resource
    LogMapper logMapper;

    @Override
    public List<ComplaintVO> getComplaintByUserId(Integer userId) {
        List<ComplaintDTO> complaints= complaintMapper.getComplaintByUserId(userId);
        if (complaints==null|| complaints.isEmpty()){
            return null;
        }

        List<ComplaintVO> complaintVOS=new ArrayList<>();
        for (ComplaintDTO complaintDTO:complaints){
            complaintVOS.add(new ComplaintVO(complaintDTO));
        }
        return complaintVOS;
    }

    @Override
    public String addComplaint(ComplaintDTO dto) {
        int cnt = complaintMapper.addComplaint(dto);
        if (cnt>0){
            return null;
        }
        else{
            return "投诉发送失败";
        }
    }

    @Override
    public List<ComplaintVO> getAllComplaint() {
        List<ComplaintDTO> complaints = complaintMapper.getAllComplaint();
        if (complaints==null|| complaints.isEmpty()){
            return null;
        }

        List<ComplaintVO> complaintVOS=new ArrayList<>();
        for (ComplaintDTO complaintDTO:complaints){
            complaintVOS.add(new ComplaintVO(complaintDTO));
        }
        return complaintVOS;
    }

    @Override
    public String acceptComplaint(AcceptComplaintVO vo, Integer adminId) {
        // 对应投诉的用户 id
        ComplaintDTO complaint = complaintMapper.getComplaintById(vo.getId());
        if (complaint==null){
            return "未找到该投诉信息";
        }

        // 向指定的用户插入一条消息
        int cnt = messageMapper.addMessage(new MessageDTO(null,complaint.getUser_id(),"投诉受理：" + vo.getContent(),new Date()));
        if(cnt <= 0) {
            return "投诉受理失败";
        }

        // 将投诉标记为已受理
        complaintMapper.acceptComplaint(complaint.getId());

        // 管理员日志
        logMapper.addLog(new LogDTO(null, adminId, "Complaint", "投诉信息受理", new Date()));
        return null;
    }
}

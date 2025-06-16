package com.lycmoons.service;
import com.lycmoons.entity.vo.request.AskForCodeVO;

public interface EmailService {
    String askForCode(AskForCodeVO vo);
    String checkCode(String email, String code);
}

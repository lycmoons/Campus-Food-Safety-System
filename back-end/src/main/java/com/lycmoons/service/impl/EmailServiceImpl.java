package com.lycmoons.service.impl;
import com.lycmoons.entity.vo.request.AskForCodeVO;
import com.lycmoons.service.EmailService;
import com.lycmoons.util.Const;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    AmqpTemplate amqpTemplate;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String askForCode(AskForCodeVO vo) {
        String email = vo.getEmail();
        String type = vo.getType();
        if(email != null && (type.equals("register") || type.equals("reset"))) {
            // 生成验证码
            Random random = new Random();
            String code = (random.nextInt(899999) + 100000) + "";

            // 创建一个邮件任务，添加到消息队列中
            Map<String, Object> task = Map.of(
                    "email", email,
                    "type", type,
                    "code", code
            );

            // 加入消息队列
            amqpTemplate.convertAndSend("mail", task);

            // 将验证码存入 redis 方便后续检验，有效时间 3 分钟
            stringRedisTemplate.opsForValue().set(Const.EMAIL_VERIFY_CODE+email, code, 3, TimeUnit.MINUTES);
            return null;
        }
        return "验证码请求失败";
    }

    @Override
    public String checkCode(String email, String code) {
        String originalCode = stringRedisTemplate.opsForValue().get(Const.EMAIL_VERIFY_CODE+email);
        // 数据库中没有这个邮箱对应的验证码
        if(originalCode == null) return "请重新获取验证码";

        // 验证码校验错误
        else if(!code.equals(originalCode)) return "验证码错误";

        // 成功后删除这个验证码，一个验证码只能用一次
        else {
            stringRedisTemplate.delete(Const.EMAIL_VERIFY_CODE+email);
            return null;
        }
    }
}

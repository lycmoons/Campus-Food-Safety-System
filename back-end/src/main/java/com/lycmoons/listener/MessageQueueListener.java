package com.lycmoons.listener;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RabbitListener(queues = "mail")   // 指定消息队列名称
public class MessageQueueListener {
    @Resource
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String from;

    // 包装邮件数据的生成
    private SimpleMailMessage createMessage(String title, String content, String targetEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);     // 邮件标题
        message.setText(content);      // 邮件内容
        message.setTo(targetEmail);    // 邮件接收方
        message.setFrom(from);     // 邮件发送方
        return message;
    }

    // 封装不同类型的邮件等内容
    private String getContent(String type, String code){
        return switch(type){
            case "register" -> "您的邮件注册验证码为：" + code +
                    "，有效时间为3分钟，为了保障您的隐私和安全，请勿向他人泄露验证码信息。";
            case "reset" -> "您真在进行重置密码操作，验证码：" + code +
                    "，有效时间为3分钟，如非本人操作，请无视。";
            default -> null;
        };
    }

    // 处理消息队列中一个一个需要发送的验证码任务
    @RabbitHandler  // 声明为 Rabbit 中 mail 消息队列的处理器
    public void sendMailMessage(Map<String,Object> data) {
        String email = (String) data.get("email");
        String code = (String) data.get("code");
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case "register" ->
                    createMessage("注册", getContent(type, code), email);
            case "reset" ->
                    createMessage("重置密码", getContent(type, code), email);
            default -> null;
        };

        // 发送
        if(message == null) return;
        javaMailSender.send(message);
    }
}

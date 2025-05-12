package com.example.be.auth.service;

import com.example.be.auth.dto.MailEnum;
import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.NotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendMail(String url, MailEnum status) throws MessagingException {
        if(!redisTemplate.hasKey(url)) throw new NotFoundException(ErrorCode.URL_NOT_FOUND);

        String email =(String)redisTemplate.opsForHash().get(url, "user");

        MimeMessage message = bodyTemplate(email, status);

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            throw new ConflictException(ErrorCode.FAIL_SEND_EMAIL);
        }

        redisTemplate.delete(url);
    }

    public MimeMessage bodyTemplate(String email, MailEnum status) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);

        switch(status){
            case SUCCESS:
                helper.setSubject("영상 변환 완료");
                helper.setText("요성하신 영상의 랜드마크 추출이 완료되었습니다.");
                break;
            case FAIL:
                helper.setSubject("영상 변환 실패");
                helper.setText("요성하신 영상의 랜드마크 추출이 실패하였습니다.");
                break;
        }

        return message;
    }
}
package com.example.be.auth.service;

import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email) throws MessagingException {
        MimeMessage message = bodyTemplate(email);

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            throw new ConflictException(ErrorCode.FAIL_SEND_EMAIL);
        }
    }

    public MimeMessage bodyTemplate(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);
        helper.setSubject("영상 변환 완료");
        helper.setText("요성하신 영상의 랜드마크 추출이 완료되었습니다.");

        return message;
    }
}
package com.example.be.auth.service;

import com.example.be.auth.dto.MailEnum;
import com.example.be.common.exception.ErrorCode;
import com.example.be.common.exception.ErrorException;
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
//        if(!redisTemplate.hasKey(url)) throw new ErrorException(ErrorCode.URL_NOT_FOUND);
//
//        String email =(String)redisTemplate.opsForHash().get(url, "user");

        MimeMessage message = bodyTemplate(url, status);

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, url);

        try {
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            throw new ErrorException(ErrorCode.FAIL_SEND_EMAIL);
        }

        redisTemplate.delete(url);
    }

    public MimeMessage bodyTemplate(String email, MailEnum status) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setTo(email);

        switch(status){
            case SUCCESS:
                helper.setSubject("[Signory] 영상 변환 완료 알림");
                helper.setText(buildExtractionSuccess(), true);
                break;
            case FAIL:
                helper.setSubject("[Signory] 영상 변환 실패 알림");
                helper.setText(buildExtractionFail(), true);
                break;
        }

        return message;
    }

    public String buildExtractionSuccess() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("</head>");
        sb.append("<body style=\"margin: 0; padding: 0; width: 100%; text-align: center; background-color: #f9f9f9; font-family: Arial, sans-serif;\">");
        sb.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f9f9f9;\">");
        sb.append("<tr>");
        sb.append("<td align=\"center\">");
        sb.append("<table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #ffffff; border-radius: 8px;\">");
        sb.append("<tbody>");

        // 제목
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"background-color: #0076BF; padding: 20px; color: white; font-size: 24px; font-weight: bold;\">영상 변환 완료</td>");
        sb.append("</tr>");

        // 본문 내용
        sb.append("<tr>");
        sb.append("<td style=\"padding: 30px; text-align: left; font-size: 16px; color: #333333;\">");
        sb.append("<p style=\"margin: 0 0 10px 0;\">안녕하세요,</p>");
        sb.append("<p style=\"margin: 0 0 10px 0;\">요청하신 영상의 랜드마크 추출이 완료되었습니다.</p>");
        sb.append("</td>");
        sb.append("</tr>");

        // 푸터
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"font-size: 12px; color: #888888; padding: 20px;\">이 이메일은 자동으로 발송되었습니다. 문의가 필요하시면 ").append(senderEmail).append(" 으로 연락해주세요.</td>");
        sb.append("</tr>");

        sb.append("</table>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    public String buildExtractionFail() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("</head>");
        sb.append("<body style=\"margin: 0; padding: 0; width: 100%; text-align: center; background-color: #f9f9f9; font-family: Arial, sans-serif;\">");
        sb.append("<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #f9f9f9;\">");
        sb.append("<tr>");
        sb.append("<td align=\"center\">");
        sb.append("<table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background-color: #ffffff; border-radius: 8px;\">");


        // 제목
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"background-color: #0076BF; padding: 20px; color: white; font-size: 24px; font-weight: bold;\">영상 변환 실패</td>");
        sb.append("</tr>");

        // 본문 내용
        sb.append("<tr>");
        sb.append("<td style=\"padding: 30px; text-align: left; font-size: 16px; color: #333333;\">");
        sb.append("<p style=\"margin: 0 0 10px 0;\">안녕하세요,</p>");
        sb.append("<p style=\"margin: 0 0 10px 0;\">요청하신 영상의 랜드마크 추출이 실패하였습니다. 다시 시도해주세요.</p>");
        sb.append("</td>");
        sb.append("</tr>");

        // 푸터
        sb.append("<tr>");
        sb.append("<td align=\"center\" style=\"font-size: 12px; color: #888888; padding: 20px;\">이 이메일은 자동으로 발송되었습니다. 문의가 필요하시면 ").append(senderEmail).append(" 으로 연락해주세요.</td>");
        sb.append("</tr>");

        sb.append("</table>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}
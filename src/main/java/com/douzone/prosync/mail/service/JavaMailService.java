package com.douzone.prosync.mail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class JavaMailService implements AuthenticateService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;
    private static int number;

    public JavaMailService(JavaMailSender javaMailSender, @Value("${mail.sender}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    private static void createNumber() {
        number = (int) (Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    private MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "<html lang=\"ko\">"
                    + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; color: #333;\">"
                    + "<div style=\"background: #ffffff; padding: 30px; border-radius: 5px; max-width: 500px; margin: 0 auto; border: 5px solid #ff9d00; box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);\">"
                    + "<h1 style=\"font-size: 24px; color: #ff6c00; text-align: center; border-bottom: 2px solid #ff9d00;\">Prosync 인증 코드</h1>"
                    + "<p style=\"font-size: 16px; line-height: 1.5; color: #555; text-align: center; font-weight: bold\">아래 인증 코드를 입력하여 이메일 주소를 인증해 주세요.</p>"
                    + "<p style=\"font-size: 16px; line-height: 1.5; color: #555; text-align: center; font-weight: bold\">인증번호의 유효기간은 10분입니다. 참고바랍니다.</p>"
                    + "<div style=\"background: #ff6c00; padding: 15px; border-radius: 5px; text-align: center; font-size: 32px; font-weight: bold; color: #ffffff; margin: 10px 0;\">" + number + "</div>"
                    + "<p style=\"font-size: 15px; color: #999; text-align: center; margin-top: 10px \">감사합니다.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }


    @Override
    public String sendForAuthenticate(String toUser) {
        MimeMessage message = CreateMail(toUser);
        javaMailSender.send(message);

        return Integer.toString(number);
    }
}
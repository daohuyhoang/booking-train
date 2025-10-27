package com.group2.case_study.services.impl;

import com.group2.case_study.models.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    public void sendMail(String to, String subject, Booking booking) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("booking", booking);
            String htmlContent = thymeleafTemplateEngine.process("/mail/ticket.html", context);

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            System.out.println("Email sent successfully to: " + to);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to: " + to);
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while sending email to: " + to);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void sendOtpEmail(String to, String otpCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject("Mã OTP xác thực đăng ký - Booking Train");

            Context context = new Context();
            context.setVariable("otpCode", otpCode);
            String htmlContent = thymeleafTemplateEngine.process("/mail/otp-email.html", context);

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            System.out.println("OTP email sent successfully to: " + to);
        } catch (MessagingException e) {
            System.err.println("Failed to send OTP email to: " + to);
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email OTP", e);
        } catch (Exception e) {
            System.err.println("Unexpected error while sending OTP email to: " + to);
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException("Không thể gửi email OTP", e);
        }
    }
}

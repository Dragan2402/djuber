package com.djuber.djuberbackend.Infastructure.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendClientVerificationEmail(String email, String token){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setFrom("djuber.drives@gmail.com");
            helper.setSubject("Djuber Account Verification");
            String verificationToken = "http://localhost:4200/authentication/verify/"+token;
            String emailBody = "<p>Hello,</p>"
                    + "<p>Thank you for signing up for our service. In order to complete your registration, please use the following token to verify your account:</p>"
                    + "<p><b>Verification Token: " + verificationToken + "</b></p>"
                    + "<p>If you did not sign up for our service, you can safely ignore this email.</p>"
                    + "<p>Thank you,</p>"
                    + "<p>The Djuber Support Team</p>";
            helper.setText(emailBody, true);
            mailSender.send(message);

        } catch (MessagingException ignored) {

        }
    }

    @Async
    public void sendPasswordResetEmail(String email, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom("djuber.drives@gmail.com");
            helper.setSubject("Djuber account password reset");
            String resetToken = "http://localhost:4200/authentication/passwordReset/"+token;
            String emailBody = "<p>Hello,</p>"
                    + "<p>We received a request to reset the password for your account. If you made this request, please use the following token to reset your password:</p>"
                    + "<p><b>Reset Token: " + resetToken + "</b></p>"
                    + "<p>If you did not make this request, you can safely ignore this email.</p>"
                    + "<p>Thank you,</p>"
                    + "<p>The Djuber Support Team</p>";
            helper.setText(emailBody,true);
            mailSender.send(message);
        } catch (MessagingException ignored) {
        }
    }
}

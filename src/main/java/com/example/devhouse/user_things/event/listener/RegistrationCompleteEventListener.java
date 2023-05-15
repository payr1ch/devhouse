package com.example.devhouse.user_things.event.listener;

import com.example.devhouse.user_things.event.RegistrationCompleteEvent;
import com.example.devhouse.user_things.user.User;
import com.example.devhouse.user_things.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;

    private User theUser;
    private  final JavaMailSender mailSender;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl() + "/api/register/verifyEmail?token=" + verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("click the link to verify your email: {}" + url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "DevHouse";
        String mailContent = "<html>"
                + "<head>"
                + "<style>"
                + "body {"
                + "background-color: #F0E8F2;"
                + "color: #3C1656;"
                + "font-family: Arial, sans-serif;"
                + "font-size: 16px;"
                + "}"
                + "h1 {"
                + "color: #7A51A1;"
                + "font-size: 24px;"
                + "text-align: center;"
                + "}"
                + "p {"
                + "margin: 20px 0;"
                + "}"
                + "a {"
                + "color: #C76BA4;"
                + "text-decoration: none;"
                + "}"
                + "a:hover {"
                + "text-decoration: underline;"
                + "}"
                + "hr {"
                + "border-top: 1px solid #7A51A1;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h1>Email Verification</h1>"
                + "<p>Hi " + theUser.getUsername() + ",</p>"
                + "<p>Thank you for registering with us! Please follow the link below to complete your registration:</p>"
                + "<p><a href=\"" + url + "\">Verify your email to activate your account</a></p>"
                + "<hr>"
                + "<p>Thank you,</p>"
                + "<p>DevHouse Team</p>"
                + "</body>"
                + "</html>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("devhousenreply@outlook.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}

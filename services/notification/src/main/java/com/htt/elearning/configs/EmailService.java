package com.htt.elearning.configs;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendSimpleMail(String to, String username, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Information about your instructor's account");
            helper.setText(
                    "<h3>Dear sweetie,</h3><br><br>" +
                            "<p>We are thrilled to welcome you to HR&YJ Academy. Your instructor account has been successfully created. Below are your account details:</p><br><br>" +
                            "<strong>Username:</strong> " + username + "<br>" +
                            "<strong>Password:</strong> " + password + "<br><br>" +
                            "<p>We look forward to your contributions and wish you a great experience with us.</p><br><br>" +
                            "Best regards,<br>" +
                            "HR&YJ Academy", true
            );
            mailSender.send(message);
            System.out.println("Email đã được gửi thành công!");}
        catch (MessagingException e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }
}

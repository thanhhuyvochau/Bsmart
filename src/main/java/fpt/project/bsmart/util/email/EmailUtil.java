package fpt.project.bsmart.util.email;

import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.Verification;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.UUID;

@Component
@Transactional
public class EmailUtil {
    @Autowired
    @Qualifier("activateTemplate")
    private String activateTemplate;
    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;

    public EmailUtil(JavaMailSender mailSender, VerificationRepository verificationRepository) {
        this.mailSender = mailSender;
        this.verificationRepository = verificationRepository;
    }

    public boolean sendVerifyEmailTo(User user) {
        String subject = "Verify BSmart Account";
        String from = "noreply@bsmart.gmail";
        String verifyCode = String.valueOf(UUID.randomUUID());
        String activeLink = String.format("localhost:8080/verify?code=%s", verifyCode);
        Verification.Builder builder = Verification.Builder.getBuilder();
        Verification verification = builder.withCode(verifyCode).withUser(user).build().getObject();
        sendHtmlEmail(String.format(activateTemplate, activeLink), user.getEmail(), from, subject);
        verificationRepository.save(verification);
        return true;
    }

    public boolean sendMockVerifyEmailTo(User user) {
        String subject = "Verify BSmart Account";
        String from = "noreply@bsmart.gmail";
        String verifyCode = String.valueOf(UUID.randomUUID());
        String activeLink = String.format("http://localhost:8080/verify?code=%s", verifyCode);
        Verification.Builder builder = Verification.Builder.getBuilder();
        sendHtmlEmail(String.format(activateTemplate, activeLink), user.getEmail(), from, subject);
        return true;
    }

    public boolean sendTextPlainEmail(String content, String to, String from, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Không thể gửi email, đã xảy ra lỗi!");
        }
    }

    public boolean sendHtmlEmail(String content, String to, String from, String subject) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(to);
            boolean html = true;
            helper.setText(content, html);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Không thể gửi email, đã xảy ra lỗi!");
        }
    }
}

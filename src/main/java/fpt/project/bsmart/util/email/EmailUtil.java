package fpt.project.bsmart.util.email;

import fpt.project.bsmart.entity.Order;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.Verification;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Qualifier("verifyAccountTemplate")
    private String verifyAccountTemplate;
    @Autowired
    @Qualifier("orderTemplate")
    private String orderTemplate;
    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;

    @Value("${host.url}")
    private String hostURL;

    public EmailUtil(JavaMailSender mailSender, VerificationRepository verificationRepository) {
        this.mailSender = mailSender;
        this.verificationRepository = verificationRepository;
    }

    public void sendVerifyEmailTo(User user) {
        try {
            String subject = "Verify BSmart Account";
            String from = "noreply@bsmart.gmail";
            String verifyCode = String.valueOf(UUID.randomUUID());
            String activeLink = hostURL + verifyCode;
            Verification.Builder builder = Verification.Builder.getBuilder();
            Verification verification = builder.withCode(verifyCode).withUser(user).build().getObject();
            sendHtmlEmail(String.format(verifyAccountTemplate, activeLink), user.getEmail(), from, subject);
            verificationRepository.save(verification);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Gửi mail xác thực thất bại!");
        }
    }

    public void sendOrderEmailTo(User user, Order or) {
        try {
            String subject = "Verify BSmart Account";
            String from = "noreply@bsmart.gmail";
            String verifyCode = String.valueOf(UUID.randomUUID());
            String activeLink = hostURL + verifyCode;
            Verification.Builder builder = Verification.Builder.getBuilder();
            Verification verification = builder.withCode(verifyCode).withUser(user).build().getObject();
            sendHtmlEmail(String.format(verifyAccountTemplate, activeLink), user.getEmail(), from, subject);
            verificationRepository.save(verification);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Gửi mail xác thực thất bại!");
        }
    }

    public boolean sendMockVerifyEmailTo(User user) {
        String subject = "Verify BSmart Account For Test";
        String from = "noreply@bsmart.gmail";
        String verifyCode = String.valueOf(UUID.randomUUID());
        String activeLink = String.format(hostURL + "verify?code=%s", verifyCode);
        Verification.Builder builder = Verification.Builder.getBuilder();
        sendHtmlEmail(String.format(verifyAccountTemplate, activeLink), user.getEmail(), from, subject);
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

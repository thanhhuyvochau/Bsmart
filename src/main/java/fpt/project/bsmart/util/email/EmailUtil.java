package fpt.project.bsmart.util.email;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.ManagerApprovalAccountRequest;
import fpt.project.bsmart.entity.request.ManagerApprovalCourseRequest;
import fpt.project.bsmart.repository.PasswordResetTokenRepository;
import fpt.project.bsmart.repository.VerificationRepository;
import fpt.project.bsmart.util.MessageUtil;
import fpt.project.bsmart.util.TextUtil;
import fpt.project.bsmart.util.TimeUtil;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static fpt.project.bsmart.util.Constants.ErrorMessage.SEND_MAIL_EXCEPTION_MESSAGE;

@Component
@Transactional
public class EmailUtil {
    @Autowired
    @Qualifier("verifyAccountTemplate")
    private String verifyAccountTemplate;
    @Autowired
    @Qualifier("resetPasswordTemplate")
    private String resetPasswordTemplate;
    @Autowired
    @Qualifier("orderTemplate")
    private String orderTemplate;

    @Autowired
    @Qualifier("courseApprovalTemplate")
    private String courseApprovalTemplate;
    @Autowired
    @Qualifier("mentorProfileApprovalTemplate")
    private String mentorProfileApprovalTemplate;

    @Autowired
    @Qualifier("payStudentFeeForTemplate")
    private String payStudentFeeForTemplate;
    @Autowired
    @Qualifier("refundTemplate")
    private String refundTemplate;
    @Autowired
    @Qualifier("unsatisfyClassTemplate")
    private String unsatisfyClassTemplate;
    @Autowired
    @Qualifier("startingClassTemplate")
    private String startingClassTemplate;
    private final JavaMailSender mailSender;
    private final VerificationRepository verificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final String SEND_FROM = "noreply@bsmart.gmail";
    @Value("${host.url}")
    private String hostURL;
    @Value("${forgot-password.url}")
    private String returnUrl;
    private final MessageUtil messageUtil;

    public EmailUtil(JavaMailSender mailSender, VerificationRepository verificationRepository, PasswordResetTokenRepository passwordResetTokenRepository, MessageUtil messageUtil) {
        this.mailSender = mailSender;
        this.verificationRepository = verificationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.messageUtil = messageUtil;
    }

    public void sendVerifyEmailTo(User user) {
        try {
            String subject = "Verify BSmart Account";
            String verifyCode = String.valueOf(UUID.randomUUID());
            String activeLink = hostURL + verifyCode;
            Verification.Builder builder = Verification.Builder.getBuilder();
            Verification verification = builder.withCode(verifyCode).withUser(user).build().getObject();
            sendHtmlEmail(String.format(verifyAccountTemplate, activeLink), user.getEmail(), SEND_FROM, subject);
            verificationRepository.save(verification);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Gửi mail xác thực thất bại!");
        }
    }

    public void sendResetPasswordEmail(User user) {
        try {
            String subject = "Bsmart: reset password";
            Instant expirationDate = Instant.now().plus(10, ChronoUnit.MINUTES);
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user);
            passwordResetToken.setExpirationDate(expirationDate);
            passwordResetToken.setToken(token);
            passwordResetTokenRepository.save(passwordResetToken);
            String resetLink = returnUrl.concat(token);
            String content = String.format(resetPasswordTemplate, user.getFullName(), resetLink, resetLink);
            sendHtmlEmail(content, user.getEmail(), SEND_FROM, subject);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage("Gửi mail thay đổi mật khẩu thất bại!");
        }
    }

    public void sendOrderEmailTo(User user, Order order) {
        OrderDetail orderDetail = order.getOrderDetails().get(0);
        Class clazz = orderDetail.getClazz();
        ReferralCode referralCode = orderDetail.getReferralCode();
        Course course = clazz.getCourse();
        String subject = "Thanh toán thành công khóa học";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("customerName", user.getFullName());
        parameters.put("courseName", course.getName());
        parameters.put("classCode", clazz.getCode());
        parameters.put("orderDate", TimeUtil.formatInstant(order.getCreated()));
        parameters.put("totalAmount", order.getTotalPrice().toString() + " VND");
        parameters.put("referralCode", referralCode.getCode());
        parameters.put("discountPercentage", referralCode.getDiscountPercent().toString() + "%");
        parameters.put("yourCompany", "MiSmart");
        String formattedTemplate = TextUtil.format(orderTemplate, parameters);
        sendHtmlEmail(formattedTemplate, user.getEmail(), SEND_FROM, subject);
    }

    public void sendApprovalCourseEmail(Course course, ManagerApprovalCourseRequest managerApprovalCourseRequest) {
        User user = course.getCreator();
        String subject = "Cập nhật trạng thái khóa học";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mentorName", user.getFullName());
        parameters.put("courseStatus", course.getStatus().getVietnameseLabel());
        parameters.put("courseName", course.getName());
        parameters.put("replyContent", managerApprovalCourseRequest.getMessage().isEmpty() ? "" : managerApprovalCourseRequest.getMessage());
        String formattedTemplate = TextUtil.format(courseApprovalTemplate, parameters);
        sendHtmlEmail(formattedTemplate, user.getEmail(), SEND_FROM, subject);
    }

    public void sendApprovalClassEmail(Course course, ManagerApprovalCourseRequest managerApprovalCourseRequest, Class clazz) {
        User user = course.getCreator();
        String subject = "Cập nhật trạng thái lớp học";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mentorName", user.getFullName());
        parameters.put("classStatus", clazz.getStatus().getVietnameseLabel());
        parameters.put("classCode", clazz.getCode());
        parameters.put("courseName", course.getName());
        parameters.put("replyContent", managerApprovalCourseRequest.getMessage().isEmpty() ? "" : managerApprovalCourseRequest.getMessage());
        String formattedTemplate = TextUtil.format(courseApprovalTemplate, parameters);
        sendHtmlEmail(formattedTemplate, user.getEmail(), SEND_FROM, subject);
    }

    public void sendApprovalMentorProfile(MentorProfile mentorProfile, ManagerApprovalAccountRequest managerApprovalAccountRequest) {
        User user = mentorProfile.getUser();
        String subject = "Thông Báo Về Trạng Thái Hồ Sơ";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mentorName", user.getFullName());
        parameters.put("mentorProfileStatus", mentorProfile.getStatus().getVietnameseLabel());
        parameters.put("replyContent", managerApprovalAccountRequest.getMessage());
        String formattedTemplate = TextUtil.format(mentorProfileApprovalTemplate, parameters);
        sendHtmlEmail(formattedTemplate, user.getEmail(), SEND_FROM, subject);
    }

    public void sendIncomeEmailForMentor(Class clazz) {
        Course course = clazz.getCourse();
        User creator = course.getCreator();
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        BigDecimal price = clazz.getPrice();
        BigDecimal totalIncome = price.multiply(BigDecimal.valueOf(studentClasses.size()));
        BigDecimal mismartIncome = totalIncome.multiply(BigDecimal.valueOf(0.3));
        BigDecimal mentorIncome = totalIncome.multiply(BigDecimal.valueOf(0.7));
        String subject = "Thông Báo Thanh Toán Cho Giảng Viên";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mentorName", creator.getFullName());
        parameters.put("classCode", clazz.getCode());
        parameters.put("courseName", course.getName());
        parameters.put("studentClassNumber", String.valueOf(studentClasses.size()));
        parameters.put("totalIncome", totalIncome.toString());
        parameters.put("mismartIncome", mismartIncome.toString());
        parameters.put("mentorIncome", mentorIncome.toString());
        String formattedTemplate = TextUtil.format(payStudentFeeForTemplate, parameters);
        sendHtmlEmail(formattedTemplate, creator.getEmail(), SEND_FROM, subject);
    }

    public void sendRefundEmailToStudent(OrderDetail orderDetail) {
        Order order = orderDetail.getOrder();
        User user = order.getUser();
        Class clazz = orderDetail.getClazz();
        Course course = clazz.getCourse();
        String subject = "Thông Báo Thanh Toán Cho Giảng Viên";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("classCode", clazz.getCode());
        parameters.put("courseName", course.getName());
        parameters.put("startDate", TimeUtil.formatInstant(clazz.getStartDate()));
        parameters.put("endDate", TimeUtil.formatInstant(clazz.getEndDate()));
        String formattedTemplate = TextUtil.format(refundTemplate, parameters);
        sendHtmlEmail(formattedTemplate, user.getEmail(), SEND_FROM, subject);
    }

    public void sendStartClass(Class clazz) {
        Course course = clazz.getCourse();
        User creator = course.getCreator();
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        String subject = "Thông Báo Bắt Đầu Lớp Học";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("classCode", clazz.getCode());
        parameters.put("studentNumber", String.valueOf(studentClasses.size()));
        parameters.put("startDate", TimeUtil.formatInstant(clazz.getStartDate()));
        parameters.put("endDate", TimeUtil.formatInstant(clazz.getEndDate()));
        String formattedTemplate = TextUtil.format(startingClassTemplate, parameters);
        sendHtmlEmail(formattedTemplate, creator.getEmail(), SEND_FROM, subject);

        for (StudentClass studentClass : studentClasses) {
            User student = studentClass.getStudent();
            sendHtmlEmail(formattedTemplate, student.getEmail(), SEND_FROM, subject);
        }
    }

    public void sendEndClassToMentor(Class clazz) {
        Course course = clazz.getCourse();
        User creator = course.getCreator();
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        String subject = "Thông Báo Bắt Đầu Lớp Học";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("classCode", clazz.getCode());
        parameters.put("studentNumber", String.valueOf(studentClasses.size()));
        parameters.put("startDate", TimeUtil.formatInstant(clazz.getStartDate()));
        parameters.put("endDate", TimeUtil.formatInstant(clazz.getEndDate()));
        String formattedTemplate = TextUtil.format(unsatisfyClassTemplate, parameters);
        sendHtmlEmail(formattedTemplate, creator.getEmail(), SEND_FROM, subject);
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
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(SEND_MAIL_EXCEPTION_MESSAGE));
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
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(messageUtil.getLocalMessage(SEND_MAIL_EXCEPTION_MESSAGE));
        }
    }
}

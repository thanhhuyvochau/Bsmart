package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.AccountDetail;
import fpt.project.bsmart.entity.dto.EmailDto;
import fpt.project.bsmart.service.ISendMailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;

@Service
@Transactional
public class SendMailServiceImplServiceImpl implements ISendMailService {

    JavaMailSender mailSender  ;

    public SendMailServiceImplServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Boolean sendMail(EmailDto emailDto ,String subject , String content,  String footer  ) {

            String to = emailDto.getMail();
            String host = "smtp.gmail.com";

            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("vuondaueducation@gmail.com", "ifduohgdpqxbpger");
                }

            });
            //session.setDebug(true);
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject(subject);

                Multipart multipart = new MimeMultipart();


                MimeBodyPart textPart = new MimeBodyPart();


                textPart.setText(content + footer);
                multipart.addBodyPart(textPart);

                message.setContent(multipart);

                System.out.println("sending...");

                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }



        return true;
    }

    @Override
    public Boolean sendMailToRegisterDoTeacher(List<EmailDto> emailDto, AccountDetail accountDetail , String password) {
        for (EmailDto emailInfo : emailDto) {
            emailInfo.setMail("vuondaueducation@gmail.com");
            String to = emailInfo.getMail();
            String host = "smtp.gmail.com";

            // Get system properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.debug", "false");

            Session  session = Session.getInstance(properties,
                    new Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(to, password);
                        }
                    });
            session.setDebug(true);


            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("ĐĂNG KÍ LÀM GIA SƯ VƯỜN ĐẬU");

                Multipart multipart = new MimeMultipart();


                MimeBodyPart textPart = new MimeBodyPart();


                textPart.setText("DANG KI LAM GIA SU");
                multipart.addBodyPart(textPart);

                message.setContent(multipart);

                System.out.println("sending...");

                Transport.send(message);
                System.out.println("Sent message successfully....");
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
        return true;
    }



}
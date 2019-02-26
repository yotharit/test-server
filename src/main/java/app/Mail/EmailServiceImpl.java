package app.Mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;


public class EmailServiceImpl implements EmailService {


    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to, String subject, SimpleMailMessage template, String... templateArgs) {

    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String fileName,byte[] file) {
        try {
            AppMailSender util = new AppMailSender();
            JavaMailSender emailSender = util.getJavaMailSender();

            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            DataSource attach =  new ByteArrayDataSource(file, "application/pdf");
            helper.addAttachment(fileName, attach);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

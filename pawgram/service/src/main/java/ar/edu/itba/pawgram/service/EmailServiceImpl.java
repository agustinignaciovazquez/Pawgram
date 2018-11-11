package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.interfaces.service.EmailService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService userService;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) throws SendMailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
        }catch (MailException e){
            throw new SendMailException(e.getMessage());
        }
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String text) throws SendMailException {
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, multipart, "utf-8");
            message.setContent(text, "text/html");
            helper.setTo(to);
            helper.setSubject(subject);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailException(e.getMessage());
        }
    }

    @Override
    public void sendWelcomeEmail(User user, String message, String subject) throws SendMailException {
        StringBuilder content = new StringBuilder("<h3>");
        content.append(message);
        content.append("</h3>");
        content.append("<img src='https://i.imgur.com/URtGAj1.png'>");
        sendHtmlMessage(user.getMail(),subject,content.toString());
    }

    @Override
    public void sendRecoverEmail(User user, String message, String subject) throws SendMailException {
        StringBuilder content = new StringBuilder("<h3>");
        content.append(message);
        content.append("</h3>");
        content.append("<h1>");
        content.append(userService.getResetToken(user));
        content.append("</h1>");
        content.append("<img src='https://i.imgur.com/URtGAj1.png'>");
        sendHtmlMessage(user.getMail(),subject,content.toString());
    }

}

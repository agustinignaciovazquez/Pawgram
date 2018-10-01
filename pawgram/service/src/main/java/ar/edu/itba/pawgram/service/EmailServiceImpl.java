package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.interfaces.service.EmailService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

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
    public void sendWelcomeEmail(User user) throws SendMailException{
        sendSimpleMessage(user.getMail(), "Bienvenido a Gran-DT", "Te damos la bienvenida a Pawgram "+user.getName());
    }

    @Override
    public void sendRecoverEmail(User user) throws SendMailException{
        sendSimpleMessage(user.getMail(), "Token de recuperacion", "Usa el siguiente token para restablecer"
                + " tu contrasena "+userService.getResetToken(user));
    }
}

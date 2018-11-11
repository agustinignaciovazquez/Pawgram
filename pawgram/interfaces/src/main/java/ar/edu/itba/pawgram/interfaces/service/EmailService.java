package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.User;

public interface EmailService {

    /**
     * Creates and sends a message.
     * @param to - Message receiver
     * @param subject - Subject of the e-mail
     * @param text - Content of the e-mail
     * @return void
     */
    public void sendSimpleMessage(String to, String subject, String text) throws SendMailException;

    /**
     * Creates and sends a html message.
     * @param to - Message receiver
     * @param subject - Subject of the e-mail
     * @param text - Content of the e-mail
     * @return void
     */
    public void sendHtmlMessage(String to, String subject, String text) throws SendMailException;

    /**
     * Sends a welcome mail to the newly user
     * @param user - Message receiver
     * @return void
     */
    public void sendWelcomeEmail(User user, String message, String subject) throws SendMailException;

    /**
     * Sends reset token to the user
     * @param user - Message receiver
     * @return void
     */
    public void sendRecoverEmail(User user, String message, String subject) throws SendMailException;
}

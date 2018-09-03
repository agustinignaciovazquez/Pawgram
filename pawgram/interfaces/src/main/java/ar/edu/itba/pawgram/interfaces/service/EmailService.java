package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.model.Comment;

public interface EmailService {

    /**
     * Creates and sends a message.
     * @param to - Message receiver
     * @param subject - Subject of the e-mail
     * @param text - Content of the e-mail
     * @return void
     */
    public void sendSimpleMessage(String to, String subject, String text);

}

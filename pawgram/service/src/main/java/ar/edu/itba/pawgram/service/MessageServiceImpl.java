package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.persistence.MessageDao;
import ar.edu.itba.pawgram.interfaces.service.MessageService;
import ar.edu.itba.pawgram.model.Chat;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    @Override
    public Chat getMessages(User origin, User destination) {
        return new Chat(origin,destination,messageDao.getMessages(origin,destination));
    }

    @Override
    public Message sendMessage(User origin, User destination, String content) {
        return messageDao.sendMessage(origin,destination,content, LocalDateTime.now());
    }

    @Override
    public List<User> getMessageUsers(User origin) {
        return messageDao.getMessageUsers(origin);
    }
}

package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.persistence.MessageDao;
import ar.edu.itba.pawgram.interfaces.service.MessageService;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    @Override
    public List<Message> getMessages(User origin, User destination, int page, int pageSize) {
        return messageDao.getMessages(origin,destination,pageSize,(page - 1) * pageSize);
    }

    @Override
    @Transactional
    public Message sendMessage(User origin, User destination, String content) {
        return messageDao.sendMessage(origin,destination,content, new Date());
    }

    @Override
    public List<User> getMessageUsers(User origin) {
        return messageDao.getMessageUsers(origin);
    }

    @Override
    public long getTotalMessages(User origin, User destination) {
        return messageDao.getTotalMessages(origin,destination);
    }

    @Override
    public long getMaxPage(User origin, User destination, int pageSize) {
        long total = getTotalMessages(origin,destination);
        return (long) Math.ceil((float) total / pageSize);
    }

}

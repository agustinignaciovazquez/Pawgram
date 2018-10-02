package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.MessageDao;
import ar.edu.itba.pawgram.model.Chat;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.persistence.rowmapper.MessageRowMapper;
import ar.edu.itba.pawgram.persistence.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageJdbcDao implements MessageDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    @Autowired
    private UserRowMapper userRowMapper;

    @Autowired
    private MessageRowMapper messageRowMapper;

    @Autowired
    public MessageJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("messages")
                .usingGeneratedKeyColumns("messageid");
    }

    @Override
    public List<Message> getMessages(User origin, User destination) {
        final List<Message> messages = jdbcTemplate.query("SELECT * FROM messages" +
                        " WHERE (origId = ? AND destId = ?) OR (destId = ? AND origId = ?) ORDER BY messageDate DESC",
                messageRowMapper, origin.getId(),destination.getId(),origin.getId(),destination.getId());
        return messages;
    }

    @Override
    public Message sendMessage(User origin, User destination, String content, LocalDateTime date) {
        final Map<String, Object> args = new HashMap<>();
        args.put("origId",origin.getId());
        args.put("destId",destination.getId());
        args.put("message",content);
        args.put("messageDate", Timestamp.valueOf(date));


        final Number messageId = jdbcInsert.executeAndReturnKey(args);

        return new Message(messageId.longValue(), destination.getId(), origin.getId(), content, date);
    }

    @Override
    public List<User> getMessageUsers(User origin) {
        final List<User> users = jdbcTemplate.query("SELECT DISTINCT users.* FROM users,messages " +
                " WHERE (users.id = messages.origId OR users.id = messages.destId) AND (origId = ? OR destId = ?)",
                userRowMapper,origin.getId(),origin.getId());
        return users;
    }
}

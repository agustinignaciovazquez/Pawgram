package ar.edu.itba.pawgram.persistence.rowmapper;

import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Message;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MessageRowMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Message(rs.getLong("messageId"),rs.getLong("destId"),
                rs.getLong("origId"),rs.getString("message"),
                rs.getTimestamp("messageDate").toLocalDateTime());
    }
}

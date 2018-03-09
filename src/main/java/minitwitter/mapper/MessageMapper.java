package minitwitter.mapper;

import minitwitter.model.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setUserId(rs.getString("person_id"));
        message.setMessage(rs.getString("content"));
        return message;
    }
}

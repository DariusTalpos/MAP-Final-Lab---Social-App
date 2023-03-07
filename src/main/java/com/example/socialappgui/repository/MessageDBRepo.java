package com.example.socialappgui.repository;

import com.example.socialappgui.domain.Message;
import com.example.socialappgui.validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class MessageDBRepo {

    String url;
    String username;
    String password;

    /**
     * the class constructor
     * @param url       - the url that leads to the database
     * @param username  - the username of the account used to access the database
     * @param password  - the password of the account used to access the database
     */
    public MessageDBRepo(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Message save(Message entity)
    {
        String sql = "insert into messages(id_from,id_to,sent_at,content) values (?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setLong(1, entity.getIdUserFrom());
            ps.setLong(2, entity.getIdUserTo());
            ps.setTimestamp(3,Timestamp.valueOf(entity.getSentAt()));
            ps.setString(4, entity.getContent());
            ps.executeUpdate();
            return null;
        }
        catch (SQLException e)
        {
            //  e.printStackTrace();
            return entity;
        }
    }

    public SortedSet<Message> findExchange(Long u1, Long u2)
    {
        SortedSet<Message> messages= new TreeSet<Message>();
        String sql="select * from messages where (id_from = ? and id_to = ?) or (id_from = ? and id_to = ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setLong(1,u1);
            ps.setLong(2,u2);
            ps.setLong(3,u2);
            ps.setLong(4,u1);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next())
            {
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("id_from");
                Long to = resultSet.getLong("id_to");
                LocalDateTime sentAt = resultSet.getTimestamp("sent_at").toLocalDateTime();
                String content = resultSet.getString("content");

                Message message = new Message(from,to,sentAt,content);
                message.setID(id);
                messages.add(message);
            }
        } catch (SQLException e) {
            //  e.printStackTrace();
        }

        return messages;
    }
}

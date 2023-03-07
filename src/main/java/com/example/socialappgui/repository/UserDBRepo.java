package com.example.socialappgui.repository;

import com.example.socialappgui.domain.User;
import com.example.socialappgui.validator.Validator;

import java.sql.*;
import java.util.HashSet;

/**
 * repository used for applying methods on the 'users' table
 */
public class UserDBRepo extends DataBaseRepository<Long, User>
{

    /**
     * the class constructor
     * @param validator - object of 'Validator' class that will be used to validate data of type 'E'
     * @param url - the url that leads to the database
     * @param username - the username of the account used to access the database
     * @param password  - the password of the account used to access the database
     */
    public UserDBRepo(Validator<User> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    /**
     * overridden method that will attempt to save the given entity
     * @param entity - the entity that the method will attempt to save
     * @return - throws IllegalArgumentException if the entity is null
     *         - null, if successful
     *         - the entity, otherwise
     */
    @Override
    public User save(User entity)
    {
        super.save(entity);
        String sql = "insert into users(username) values (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
            return null;
        }
        catch (SQLException e)
        {
            // e.printStackTrace();
            return entity;
        }
    }

    /**
     * overriden method that will attempt to update the entity
     * @param entity - the entity that the method will attempt to replace the original with
     * @return - throws IllegalArgumentException if the entity is null
     *         - null, if successful
     *         - the entity, otherwise
     */
    @Override
    public User update(User entity)
    {
        super.update(entity);
        User user = findOne(entity.getID());
        if(user!=null)
        {
            String sql = "update users set username = ? where id=?";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)
            ) {
                ps.setString(1, entity.getName());
                ps.setLong(2, entity.getID());
                ps.executeUpdate();
                return null;
            } catch (SQLException e) {
                // e.printStackTrace();
                return entity;
            }
        }
        return entity;
    }

    /**
     * overriden method that tries to delete the user with the given id
     * @param id - the id of the user
     * @return - throws IllegalArgumentException if the id is null
     *         - null, if successful
     *         - the entity, otherwise
     */
    @Override
    public User delete(Long id)
    {
        super.delete(id);
        User user = findOne(id);
        if(user!=null)
        {
            String sql = "delete from users where id=?";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setLong(1,id);
                ps.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return user;
    }

    /**
     * overridden method that attempts to find the user with the specified id
     * @param id - the id of the desired user
     * @return - throws IllegalArgumentException if the id is null
     *         - the user, if they exist
     *         - null, otherwise
     */
    @Override
    public User findOne(Long id)
    {
        super.findOne(id);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("select * from users where id = ?");
        )
        {
            ps.setLong(1,id);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return extractData(resultSet);
        }
        catch (SQLException e)
        {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * method that attempts to return every user in the database
     * @return - the users stored in the database
     */
    @Override
    public Iterable<User> findAll()
    {
        HashSet<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("select * from users");
             ResultSet resultSet = ps.executeQuery()
        )
        {
            while (resultSet.next()) {
                users.add(extractData(resultSet));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    protected User extractData(ResultSet resultSet)
    {
        try
        {
            Long userId = resultSet.getLong("id");
            String userName = resultSet.getString("username");
            User user = new User(userName);
            user.setID(userId);
            return user;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return  null;
        }
    }
}

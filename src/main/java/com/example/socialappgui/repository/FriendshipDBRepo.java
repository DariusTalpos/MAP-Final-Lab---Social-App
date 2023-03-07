package com.example.socialappgui.repository;

import com.example.socialappgui.domain.Friendship;
import com.example.socialappgui.domain.Request;
import com.example.socialappgui.domain.RequestType;
import com.example.socialappgui.validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
     * repository used for applying methods on the 'friendships' table
     */
public class FriendshipDBRepo extends DataBaseRepository<Long,Friendship>
{

    /**
         * the class constructor
         * @param validator - object of 'Validator' class that will be used to validate data of type 'E'
         * @param url - the url that leads to the database
         * @param username - the username of the account used to access the database
         * @param password  - the password of the account used to access the database
         */
    public FriendshipDBRepo(Validator<Friendship> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    /**
         * overridden method that will attempt to save the given friendship
         * @param entity - the friendship that the method will attempt to save
         * @return - throws IllegalArgumentException if the entity is null
         *         - null, if successful
         *         - the entity, otherwise
         */
    @Override
    public Friendship save(Friendship entity) {
            super.save(entity);

            String sql = "insert into friendships(id_user1, id_user2, friends_since, status, request) values (?, ?, ?, ?,?)";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)
            )
            {
                ps.setLong(1, entity.getIdUser1());
                ps.setLong(2, entity.getIdUser2());
                ps.setTimestamp(3, Timestamp.valueOf(entity.getFriendsSince()));
                ps.setString(4,entity.getDescription());
                ps.setString(5, entity.getRequest().toString());
                ps.executeUpdate();
                return null;
            }
            catch (SQLException e)
            {
                //  e.printStackTrace();
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
    public Friendship update(Friendship entity)
    {
            super.update(entity);
            Friendship friendship = findOne(entity.getID());
            if(friendship!=null)
            {
                String sql = "update friendships set status = ?, request = ? where friendships.id = ?";

                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement ps = connection.prepareStatement(sql)
                ) {
                    ps.setString(1, entity.getDescription());
                    ps.setString(2,entity.getRequest().toString());
                    ps.setLong(3, entity.getID());
                    ps.executeUpdate();
                    return null;
                } catch (SQLException e) {
                    //  e.printStackTrace();
                    return entity;
                }
            }
            return entity;
        }

    /**
         * overriden method that tries to delete the friendship with the given id
         * @param id - the id of the friendship
         * @return - throws IllegalArgumentException if the id is null
         *         - null, if successful
         *         - the entity, otherwise
         */
    @Override
    public Friendship delete(Long id)
    {
            super.delete(id);
            Friendship friendship = findOne(id);
            if(friendship!=null)
            {
                String sql = "delete from friendships where id=?";

                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ps.setLong(1,id);
                    ps.executeUpdate();
                }
                catch (SQLException e)
                {
                    //  e.printStackTrace();
                }
            }
            return friendship;
        }

    /**
         * overridden method that attempts to find the friendship with the specified id
         * @param id - the id of the desired friendship
         * @return - throws IllegalArgumentException if the id is null
         *         - the friendship, if it exists
         *         - null, otherwise
         */
    @Override
    public Friendship findOne(Long id)
    {
            super.findOne(id);
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement("select * from friendships where id = ?")
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
         * method that attempts to return every friendship in the database
         * @return - the friendships stored in the database
         */
    @Override
    public Iterable<Friendship> findAll()
    {
            Set<Friendship> friendships = new HashSet<>();

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement("select * from friendships");
                 ResultSet resultSet = ps.executeQuery()
            ) {
                while (resultSet.next()) {
                    friendships.add(extractData(resultSet));
                }
            } catch (SQLException e) {
                //  e.printStackTrace();
            }

            return friendships;
        }

    public Iterable<Friendship> findFriends(Long userId)
    {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("select * from friendships where (id_user1 = ? or id_user2 = ?) and request = ?")
        ) {
            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setString(3, "ACCEPTED");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                friendships.add(extractData(resultSet));

            }
        }catch (SQLException e)
        {
            //fac asta doar ca sa nu fie highlight;
        }
        return friendships;
    }

    public Iterable<Friendship> findSentRequests(Long userId)
    {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("select * from friendships where id_user1 = ? and request = ?")
        ) {
            ps.setLong(1, userId);
            ps.setString(2, "PENDING");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                friendships.add(extractData(resultSet));
            }
        } catch (SQLException e)
        {
            //fac asta doar ca sa nu fie highlight;
        }

        return friendships;
    }

    public Iterable<Friendship> findReceivedRequests(Long userId)
    {
        Set<Friendship> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement("select * from friendships where id_user2 = ? and request = ?")
        ) {
            ps.setLong(1, userId);
            ps.setString(2, "PENDING");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                friendships.add(extractData(resultSet));
            }
        } catch (SQLException e)
        {
            //fac asta doar ca sa nu fie highlight;
        }

        return friendships;
    }


    @Override
    protected Friendship extractData(ResultSet resultSet)
    {
            try
            {
                Long friendshipId = resultSet.getLong("id");
                Long idUser1 = resultSet.getLong("id_user1");
                Long idUser2 = resultSet.getLong("id_user2");
                LocalDateTime friendsSince = resultSet.getTimestamp("friends_since").toLocalDateTime();
                String status = resultSet.getString("status");
                RequestType requestType = RequestType.valueOf(resultSet.getString("request"));

                Friendship friendship = new Friendship(idUser1, idUser2, friendsSince, status, requestType);
                friendship.setID(friendshipId);
                return friendship;
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }
}

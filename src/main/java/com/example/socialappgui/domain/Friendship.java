package com.example.socialappgui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Friendship class that extends the Entity class, with the ID being 'Long' type
 */
public class Friendship extends Entity<Long>{
    private Long idUser1;
    private Long idUser2;
    private LocalDateTime friendsSince;
    private String description;

    private RequestType requestTypeState;

    /**
     * constructor for the class
     * @param idUser1 - the id of the first user
     * @param idUser2 - the id of the second user
     */
    public Friendship(Long idUser1, Long idUser2, LocalDateTime friendsSince, String description, RequestType requestType)
    {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.friendsSince = friendsSince;
        this.description = description;
        this.requestTypeState = requestType;
    }

    /**
     * getter for the first user's id
     * @return - the id of the first user
     */
    public Long getIdUser1() {
        return idUser1;
    }

    /**
     * getter for the second user's id
     * @return - the id of the second user
     */
    public Long getIdUser2() {
        return idUser2;
    }

    /**
     * getter for the date and time the users became friends
     * @return - the LocalDateTime object representing when the two became friends
     */
    public LocalDateTime getFriendsSince() {return friendsSince;}

    /**
     * getter for the status of the friendship
     * @return - the status of the friendship
     */
    public String getDescription() {return description;}

    public RequestType getRequest() {return requestTypeState;}

    /**
     * turns the friendship into a string
     * @return - the details of the friendship written in the form of a stirng
     */
    @Override
    public String toString()
    {
        return "Friendship ID:"+getID()+"; IDs of the two friends: "+idUser1+","+idUser2+"; They are friends since: "
                +friendsSince.toLocalDate()+", "+friendsSince.toLocalTime()+"; Friendship Status: "+ getDescription();
    }

    /**
     * verifies if the friendship and the 'o' Object are the same
     * @param o - the object that will be compared to the friendship
     * @return - true, if 'o' and the friendship are the same
     *         - false, otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship friendship = (Friendship) o;
        return (Objects.equals(idUser1, friendship.idUser1) &&
                Objects.equals(idUser2, friendship.idUser2)) ||
                (Objects.equals(idUser1, friendship.idUser2) &&
                        Objects.equals(idUser2, friendship.idUser1));
    }

    /**
     * determines the hashcode of the user
     * @return - the hascode determined by the ids of the two friends
     */
    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2);
    }
}

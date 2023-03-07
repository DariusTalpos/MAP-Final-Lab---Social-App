package com.example.socialappgui.domain;

import java.time.LocalDateTime;

/**
 * class used to display data in the table
 */
public class Request extends Entity<Long>{

    private Long friendID;

    private Long friendshipID;
    private String friendName;
    private LocalDateTime friendsSince;
    private String description;

    public Request(Long friendID, Long friendshipID, String friendName, LocalDateTime friendsSince, String description)
    {
        this.friendID = friendID;
        this.friendshipID = friendshipID;
        this.friendName = friendName;
        this.friendsSince = friendsSince;
        this.description = description;
    }

    public Long getFriendID() {return friendID;}

    public Long getFriendshipID() {return friendshipID;}

    public String getFriendName() {return friendName;}

    public void setFriendName(String name){ this.friendName = name;}

    public LocalDateTime getFriendsSince()
    {
        return friendsSince;
    }

    public String getDescription()
    {
        return description;
    }

}

package com.example.socialappgui.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> implements Comparable<Message>{
    private Long idUserFrom;
    private Long idUserTo;
    private LocalDateTime sentAt;
    private String content;

    public Message(Long idUserFrom, Long idUserTo, LocalDateTime sentAt, String content)
    {
        this.idUserFrom = idUserFrom;
        this.idUserTo = idUserTo;
        this.sentAt = sentAt;
        this.content = content;
    }

    public Long getIdUserFrom() {
        return idUserFrom;
    }

    public Long getIdUserTo() {
        return idUserTo;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(Message message) {
        return (int) (this.getID()-message.getID());
    }
}

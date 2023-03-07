package com.example.socialappgui.service;

import com.example.socialappgui.domain.Message;
import com.example.socialappgui.observer.Observable;
import com.example.socialappgui.observer.Observer;
import com.example.socialappgui.repository.MessageDBRepo;

import java.time.LocalDateTime;
import java.util.*;

public class MessageService implements Observable {
    private MessageDBRepo repo;

    private List<Observer> observers = new ArrayList<Observer>();

    public MessageService(MessageDBRepo repo) {
        this.repo = repo;
    }

    public void addMessage(Long from,Long to, String message)
    {
        Message msg = new Message(from,to, LocalDateTime.now(),message);
        repo.save(msg);
        notifyObservers();
    }

    public List<String> findExchange(Long user1, Long user2)
    {
        List<String> messages = new ArrayList<>();
        SortedSet<Message> msg = repo.findExchange(user1,user2);
        for (Message m: msg) {
            messages.add(m.getContent());
        }
        return messages;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o: observers)
            o.update();
    }
}

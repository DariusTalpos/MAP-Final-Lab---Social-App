package com.example.socialappgui.observer;

import com.example.socialappgui.events.Event;

public interface Observable
{
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
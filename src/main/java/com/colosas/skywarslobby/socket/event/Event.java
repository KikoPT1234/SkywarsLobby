package com.colosas.skywarslobby.socket.event;

public abstract class Event {

    protected final String eventName;

    protected Event(String eventName) {
        this.eventName = eventName;
    }

    public abstract String[] run(int serverPort, String[] args);

    public String getEventName() {
        return eventName;
    }
}


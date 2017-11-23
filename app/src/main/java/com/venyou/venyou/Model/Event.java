package com.venyou.venyou.Model;

import java.io.Serializable;

/**
 * Created by kuberkohli on 11/21/17.
 */

public class Event implements Serializable {

    String Name, Venue, City, State, id;

    public Event(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = Name;
    }

    public String getVenue() {
        return Venue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVenue(String venue) {
        this.Venue = venue;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = City;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = State;
    }
}

package com.venyou.venyou.Model;

import java.io.Serializable;

/**
 * Created by kuberkohli on 11/21/17.
 */

public class Event implements Serializable {

    public Event(){

    }

    String name, venue, city, state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

package com.poviolab.poviolabtest.model;


public class City {
    private long _id;
    private String name;
    //quick hack
    private double lastKnownTemperature=-100001;

    public double getLastKnownTemperature() {
        return lastKnownTemperature;
    }

    public void setLastKnownTemperature(double lastKnownTemperature) {
        this.lastKnownTemperature = lastKnownTemperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
}

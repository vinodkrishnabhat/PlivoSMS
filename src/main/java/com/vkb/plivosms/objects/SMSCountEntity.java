package com.vkb.plivosms.objects;

import java.sql.Timestamp;

public class SMSCountEntity {
    public Integer id;
    private String fromNumber;
    private Timestamp startTime;
    private Integer counter;

    public SMSCountEntity(Integer id, String fromNumber, Timestamp startTime, Integer counter) {
        this.id = id;
        this.fromNumber = fromNumber;
        this.startTime = startTime;
        this.counter = counter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}

package com.application.entity.kitchen.club;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "food_club_events")
@EnableAutoConfiguration
public class FoodClubEvent {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingDate;
    private String meetingTime;
    private String participants;
    private String chefs;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] foodPicture;

    public FoodClubEvent(String meetingDate, String meetingTime, String participants, String chefs) {
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.participants = participants;
        this.chefs = chefs;
    }

    public FoodClubEvent(Long id, String meetingDate, String meetingTime, String participants, String chefs, byte[] foodPicture) {
        this.id = id;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.participants = participants;
        this.chefs = chefs;
        this.foodPicture = foodPicture;
    }

    public FoodClubEvent(Long id, String meetingDate, String meetingTime, String participants, String chefs) {
        this.id = id;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.participants = participants;
        this.chefs = chefs;
    }

    public FoodClubEvent() {
    }

    public byte[] getFoodPicture() {
        return foodPicture;
    }

    public void setFoodPicture(byte[] foodPicture) {
        this.foodPicture = foodPicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getChefs() {
        return chefs;
    }

    public void setChefs(String chefs) {
        this.chefs = chefs;
    }
}

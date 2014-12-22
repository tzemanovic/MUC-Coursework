package com.tzemanovic.muccoursework.db;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TableTournament implements Serializable {
    private int id;
    private int startDate;
    private int endDate;
    private String name;
    private String pricePool;
    private int registeredTeams;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPricePool() {
        return pricePool;
    }

    public void setPricePool(String pricePool) {
        this.pricePool = pricePool;
    }

    public int getRegisteredTeams() {
        return registeredTeams;
    }

    public void setRegisteredTeams(int registeredTeams) {
        this.registeredTeams = registeredTeams;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

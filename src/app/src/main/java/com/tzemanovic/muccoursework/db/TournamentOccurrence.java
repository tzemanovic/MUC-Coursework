package com.tzemanovic.muccoursework.db;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public enum TournamentOccurrence {
    Past("past"),
    Current("current"),
    Upcoming("upcoming");

    private final String name;

    private TournamentOccurrence(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

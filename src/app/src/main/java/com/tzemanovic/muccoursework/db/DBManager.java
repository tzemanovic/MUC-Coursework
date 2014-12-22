package com.tzemanovic.muccoursework.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class DBManager {

    private static DBManager instance;
    private SQLiteDatabase db;

    // singleton
    public static DBManager getInstance(Context context) {
        return instance == null ? instance = new DBManager(context) : instance;
    }

    private DBManager(Context context) {
        SQLiteDBFromAsset sqLiteDBFromAsset = new SQLiteDBFromAsset(context, "dota2db.s3db");
        db = sqLiteDBFromAsset.getReadableDatabase();
    }

    public List<TableTeam> readTeams() {
        Cursor cursor = db.rawQuery("SELECT * FROM TeamsByPoints", null);
        List<TableTeam> teams = new ArrayList<TableTeam>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableTeam team = new TableTeam();
            team.setId(cursor.getInt(0));
            team.setName(cursor.getString(1));
            team.setPoints(cursor.getInt(2));
            team.setRegionId(cursor.getInt(3));
            teams.add(team);
            cursor.moveToNext();
        }
        cursor.close();
        return teams;
    }

    public List<TableRegion> readRegions() {
        Cursor cursor = db.rawQuery("SELECT _Id, Name FROM Region", null);
        List<TableRegion> regions = new ArrayList<TableRegion>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableRegion region = new TableRegion();
            region.setId(cursor.getInt(0));
            region.setName(cursor.getString(1));
            regions.add(region);
            cursor.moveToNext();
        }
        cursor.close();
        return regions;
    }

    public String readRegionById(int regionId) {
        Cursor cursor = db.rawQuery("SELECT Name FROM Region WHERE _Id = " + String.valueOf(regionId), null);
        cursor.moveToFirst();
        String region = "";
        if (!cursor.isAfterLast()) {
            region = cursor.getString(0);
        }
        cursor.close();
        return region;
    }

    public List<TableTeamResult> readResultsByTeamId(int teamId) {
        Cursor cursor = db.rawQuery("SELECT _Id, Win, Loss, Year, Month FROM TeamResult WHERE TeamId = " + String.valueOf(teamId) + " ORDER BY Year, Month ASC", null);
        List<TableTeamResult> results = new ArrayList<TableTeamResult>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableTeamResult result = new TableTeamResult();
            result.setId(cursor.getInt(0));
            result.setWin(cursor.getInt(1));
            result.setLoss(cursor.getInt(2));
            result.setYear(cursor.getInt(3));
            result.setMonth(cursor.getInt(4));
            results.add(result);
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }

    public List<TableTournament> readTournamentsByOccurrence(TournamentOccurrence occurrence) {
        String condition = "";
        String now = String.valueOf(new Date().getTime() / 1000);
        switch (occurrence) {
            case Past:
                // 0 Date means it's to be determined
                condition = " WHERE EndDate <> 0 AND EndDate < " + now;
                break;
            case Current:
                condition = " WHERE (EndDate = 0 OR EndDate > " + now + ") AND StartDate < " + now;
                break;
            case Upcoming:
                condition = " WHERE StartDate = 0 OR StartDate > " + now;
                break;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM TournamentsByStartDate" + condition, null);
        List<TableTournament> tournaments = new ArrayList<TableTournament>(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableTournament tournament = new TableTournament();
            tournament.setId(cursor.getInt(0));
            tournament.setStartDate(cursor.getInt(1));
            tournament.setEndDate(cursor.getInt(2));
            tournament.setName(cursor.getString(3));
            tournament.setPricePool(cursor.getString(4));
            tournament.setRegisteredTeams(cursor.getInt(5));
            tournament.setLocation(cursor.getString(6));
            tournaments.add(tournament);
            cursor.moveToNext();
        }
        cursor.close();
        return tournaments;
    }
}

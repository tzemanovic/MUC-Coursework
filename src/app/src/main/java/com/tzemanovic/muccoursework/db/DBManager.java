package com.tzemanovic.muccoursework.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class DBManager {

    private static DBManager instance;
    private SQLiteDatabase db;

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

    public String getRegionById(int regionId) {
        Cursor cursor = db.rawQuery("SELECT Name FROM Region WHERE _Id = " + String.valueOf(regionId), null);
        cursor.moveToFirst();
        String region = "";
        if (!cursor.isAfterLast()) {
            region = cursor.getString(0);
        }
        return region;
    }
}

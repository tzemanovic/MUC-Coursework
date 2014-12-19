package com.tzemanovic.muccoursework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class SQLiteDBFromAsset extends SQLiteOpenHelper {

    private Context context;
    private String dbName;

    public SQLiteDBFromAsset(Context context, String dbName) {
        super(context, dbName, null, 1);
        this.context = context;
        this.dbName = dbName;
        copyDBFromAssetFile();
    }

    private void copyDBFromAssetFile() {
        String dbPath = context.getDatabasePath(dbName).getAbsolutePath();
        try {
            // comment out this condition to reload db file
            if (!new File(dbPath).exists()) {
                this.getReadableDatabase();

                InputStream inputStream = context.getAssets().open(dbName);
                OutputStream outputStream = new FileOutputStream(dbPath);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                //Close the streams
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}

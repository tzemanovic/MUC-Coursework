package com.tzemanovic.muccoursework;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.DBManager;
import com.tzemanovic.muccoursework.db.TableRegion;
import com.tzemanovic.muccoursework.helper.FontLoader;
import com.tzemanovic.muccoursework.helper.Preferences;

import java.util.List;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class PreferencesActivity extends BaseActivity {

    SharedPreferences sharedPreferences;
    Spinner region;
    Spinner units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCurrentActionId(R.id.action_preferences);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DBManager dbManager = DBManager.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ((TextView) findViewById(R.id.preferencesHeading)).setTypeface(FontLoader.constantia(this));

        region = (Spinner) findViewById(R.id.region);
        // read regions from database
        List<TableRegion> regions = dbManager.readRegions();
        String[] regionsAry = new String[regions.size()];
        int i = 0;
        // populate regions array with data from db
        for (TableRegion region : regions) {
            regionsAry[i++] = region.getName();
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, regionsAry);
        // Specify the layout to use when the list of choices appears
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        region.setAdapter(regionAdapter);
        // try to load region from saved preferences
        region.setSelection(Preferences.getInt(sharedPreferences, Preferences.region));

        units = (Spinner) findViewById(R.id.units);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this, R.array.units_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        units.setAdapter(unitsAdapter);
        // try to load units from saved preferences
        units.setSelection(Preferences.getInt(sharedPreferences, Preferences.units));

        Button savePreferences = (Button) findViewById(R.id.savePreferences);
        savePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
                startPreviousActivity();
            }
        });

        Button cancelPreferences = (Button) findViewById(R.id.cancelPreferences);
        cancelPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPreviousActivity();
            }
        });

    }

    private void startPreviousActivity() {
        finish();
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Preferences.putInt(editor, Preferences.region, region.getSelectedItemPosition());
        Preferences.putInt(editor, Preferences.units, units.getSelectedItemPosition());
    }

}

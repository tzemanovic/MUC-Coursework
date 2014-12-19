package com.tzemanovic.muccoursework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class BaseActivity extends ActionBarActivity {

    private int currentActionId;

    public void setCurrentActionId(int currentActionId) {
        this.currentActionId = currentActionId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        int size = menu.size();
        for (int i = 0; i < size; ++i) {
            MenuItem mi = menu.getItem(i);
            if (mi.getItemId() == currentActionId) {
                mi.setVisible(false);
            } else {
                mi.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_news: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_teams: {
                Intent intent = new Intent(this, TeamsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_preferences: {
                Intent intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_quit: {
                App.quit();
                System.exit(0);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        App.addActivity(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        App.removeActivity(this);
    }

}

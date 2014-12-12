package com.tzemanovic.muccoursework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.DBManager;
import com.tzemanovic.muccoursework.db.TableTeam;
import com.tzemanovic.muccoursework.helper.FontLoader;
import com.tzemanovic.muccoursework.helper.Preferences;

import java.util.List;

/**
 * Created by Tomas Zemanovic on 11/12/2014.
 */
public class TeamsActivity extends BaseActivity {

    private TableLayout teamsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCurrentActionId(R.id.action_teams);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareContent();
    }

    private void prepareContent() {
        setContentView(R.layout.activity_teams);
        DBManager dbManager = DBManager.getInstance(getApplicationContext());

        teamsTable = (TableLayout) findViewById(R.id.teamsTable);

        ((TextView) findViewById(R.id.teamsHeading)).setTypeface(FontLoader.constantia(this));

        int region = Preferences.getInt(getApplicationContext(), Preferences.region);
        Button teamsRegion = (Button) findViewById(R.id.teamsRegion);
        teamsRegion.setText(dbManager.getRegionById(region));
        teamsRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamsActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });

        TextView rank = (TextView) findViewById(R.id.teamsTableHeadRank);
        rank.setTypeface(FontLoader.constantia(this));

        TextView name = (TextView) findViewById(R.id.teamsTableHeadName);
        name.setTypeface(FontLoader.constantia(this));

        TextView points = (TextView) findViewById(R.id.teamsTableHeadPoints);
        points.setTypeface(FontLoader.constantia(this));

        showTeams(dbManager.readTeams());
    }

    private void showTeams(List<TableTeam> teams) {
        int region = Preferences.getInt(getApplicationContext(), Preferences.region);
        int rankCount = 1;
        for (final TableTeam team : teams) {
            if (region == 0 || team.getRegionId() == region) {
                View teamsTableRow = View.inflate(getApplicationContext(), R.layout.teams_table_row, null);

                TextView rank = (TextView) teamsTableRow.findViewById(R.id.teamsTableRowRank);
                rank.setText(String.valueOf(rankCount++));
                //rank.setTypeface(FontLoader.constantia(this));

                TextView name = (TextView) teamsTableRow.findViewById(R.id.teamsTableRowName);
                name.setText(team.getName());
                //name.setTypeface(FontLoader.constantia(this));

                TextView points = (TextView) teamsTableRow.findViewById(R.id.teamsTableRowPoints);
                points.setText(String.valueOf(team.getPoints()));
                //points.setTypeface(FontLoader.constantia(this));

                teamsTable.addView(teamsTableRow);
            }
        }
    }

}

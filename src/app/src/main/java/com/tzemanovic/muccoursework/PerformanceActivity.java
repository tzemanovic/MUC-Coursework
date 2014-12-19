package com.tzemanovic.muccoursework;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.DBManager;
import com.tzemanovic.muccoursework.db.TableTeamResult;
import com.tzemanovic.muccoursework.helper.FontLoader;

import java.util.List;

/**
 * Created by Tomas Zemanovic on 13/12/2014.
 */
public class PerformanceActivity extends BaseActivity {

    private LinearLayout layout;
    private SurfaceView performanceGraph;
    List<TableTeamResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int teamId = getIntent().getIntExtra("teamId", 0);
        String teamName = getIntent().getStringExtra("teamName");

        TextView heading = (TextView) findViewById(R.id.performanceHeading);
        heading.setTypeface(FontLoader.constantia(this));
        heading.setText(teamName.toUpperCase() + " WIN/LOSS RATIO");

        results = DBManager.getInstance(this).getResultsByTeamId(teamId);
        layout = (LinearLayout) findViewById(R.id.performanceLayout);
        openPerformanceGraph();
    }

    private void openPerformanceGraph() {
        if (performanceGraph == null) {
            performanceGraph = new PerformanceSurfaceView(this, results);
            performanceGraph.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
            layout.addView(performanceGraph);
        }
    }

}

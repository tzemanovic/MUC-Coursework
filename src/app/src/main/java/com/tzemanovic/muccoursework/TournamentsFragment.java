package com.tzemanovic.muccoursework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.DBManager;
import com.tzemanovic.muccoursework.db.TableTournament;
import com.tzemanovic.muccoursework.db.TournamentOccurrence;
import com.tzemanovic.muccoursework.helper.FontLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TournamentsFragment extends Fragment {

    protected TournamentOccurrence occurrence;

    private TableLayout tournamentsTable;
    private LinearLayout tournamentsLayout;
    private TextView noTournaments;
    private DateFormat df = new SimpleDateFormat("d MMM\nyyyy");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournaments, container, false);
        prepareContent(view);
        return view;
    }

    protected void prepareContent(View view) {
        DBManager dbManager = DBManager.getInstance(getActivity());

        ((TextView) view.findViewById(R.id.tournamentsHeading)).setTypeface(FontLoader.constantia(getActivity()));
        tournamentsTable = (TableLayout) view.findViewById(R.id.tournamentsTable);
        tournamentsLayout = (LinearLayout) view.findViewById(R.id.tournamentsLayout);
        noTournaments = (TextView) view.findViewById(R.id.noTournaments);

        TextView start = (TextView) view.findViewById(R.id.tournamentsTableHeadStart);
        start.setTypeface(FontLoader.constantia(getActivity()));

        TextView end = (TextView) view.findViewById(R.id.tournamentsTableHeadEnd);
        end.setTypeface(FontLoader.constantia(getActivity()));

        TextView name = (TextView) view.findViewById(R.id.tournamentsTableHeadName);
        name.setTypeface(FontLoader.constantia(getActivity()));

        showTournaments(dbManager.readTournamentsByOccurrence(occurrence));
    }

    private void showTournaments(List<TableTournament> tournaments) {
        if (tournaments.size() == 0) {
            tournamentsLayout.removeView(tournamentsTable);
            noTournaments.setText("No " + occurrence.toString() + " tournaments.");
        }
        else {
            for (final TableTournament tournament : tournaments) {
                View tournamentsTableRow = View.inflate(getActivity(), R.layout.tournaments_table_row, null);

                TableRow row = (TableRow) tournamentsTableRow.findViewById(R.id.tournamentsTableRow);
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openTournamentsDetails(tournament);
                    }
                });

                TextView start = (TextView) tournamentsTableRow.findViewById(R.id.tournamentsTableRowStart);
                int startDate = tournament.getStartDate();
                if (startDate == 0) {
                    start.setText("TBD\n ");
                } else {
                    start.setText(df.format(new Date((long) startDate * 1000)));
                }

                TextView end = (TextView) tournamentsTableRow.findViewById(R.id.tournamentsTableRowEnd);
                int endDate = tournament.getEndDate();
                if (endDate == 0) {
                    end.setText("TBD\n ");
                } else {
                    end.setText(df.format(new Date((long) endDate * 1000)));
                }

                TextView name = (TextView) tournamentsTableRow.findViewById(R.id.tournamentsTableRowName);
                name.setText(tournament.getName() + "\n ");

                tournamentsTable.addView(tournamentsTableRow);
            }
        }
    }

    private void openTournamentsDetails(TableTournament tournament) {
        Intent intent = new Intent(getActivity(), TournamentActivity.class);
        intent.putExtra("tournament", tournament);
        startActivity(intent);
    }
}

package com.tzemanovic.muccoursework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.DBManager;
import com.tzemanovic.muccoursework.db.TableTournament;
import com.tzemanovic.muccoursework.db.TournamentOccurrence;
import com.tzemanovic.muccoursework.helper.FontLoader;

import java.util.List;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TournamentsFragmentPast extends TournamentsFragment {

    public TournamentsFragmentPast() {
        super();
        occurrence = TournamentOccurrence.Past;
    }

}
package com.tzemanovic.muccoursework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tzemanovic.muccoursework.db.TournamentOccurrence;
import com.tzemanovic.muccoursework.helper.FontLoader;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TournamentsFragmentUpcoming extends TournamentsFragment {

    public TournamentsFragmentUpcoming() {
        super();
        occurrence = TournamentOccurrence.Upcoming;
    }

}
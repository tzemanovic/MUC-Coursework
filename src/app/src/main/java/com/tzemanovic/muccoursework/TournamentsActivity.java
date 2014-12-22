package com.tzemanovic.muccoursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tzemanovic.muccoursework.helper.FontLoader;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TournamentsActivity extends BaseActivity {

    final static private String[] TAB_NAMES = { "PAST", "CURRENT", "UPCOMING" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setCurrentActionId(R.id.action_tournaments);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ViewPager pager = (ViewPager)findViewById(R.id.tournamentsPages);
        pager.setAdapter(new TournamentsPagerAdapter(getSupportFragmentManager()));
        // switch to the Current tournaments page
        pager.setCurrentItem(1);
        // change tab strips font
        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tournamentsPager);
        for (int i = 0; i < tabStrip.getChildCount(); ++i) {
            View child = tabStrip.getChildAt(i);
            ((TextView)child).setTypeface(FontLoader.constantia(this));
        }
    }

    private class TournamentsPagerAdapter extends FragmentPagerAdapter {

        public TournamentsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0: return new TournamentsFragmentPast();
                case 1: return new TournamentsFragmentCurrent();
                case 2: return new TournamentsFragmentUpcoming();
            }
            return null;
        }

        @Override
        public int getCount() {
            return TAB_NAMES.length;
        }

        @Override
        public CharSequence getPageTitle(int index) {
            return TAB_NAMES[index];
        }
    }

}

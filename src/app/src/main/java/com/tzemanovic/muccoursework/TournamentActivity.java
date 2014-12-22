package com.tzemanovic.muccoursework;

import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tzemanovic.muccoursework.db.TableTournament;
import com.tzemanovic.muccoursework.helper.FontLoader;
import com.tzemanovic.muccoursework.helper.Preferences;
import com.tzemanovic.muccoursework.helper.ReverseGeocoder;

/**
 * Created by Tomas Zemanovic on 20/12/2014.
 */
public class TournamentActivity extends BaseActivity {

    private TableTournament tournament;
    private TextView location;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tournament = (TableTournament) getIntent().getSerializableExtra("tournament");

        TextView heading = (TextView) findViewById(R.id.tournamentHeading);
        heading.setText(tournament.getName());
        heading.setTypeface(FontLoader.constantia(this));

        ((TextView) findViewById(R.id.tournamentPricePool)).setText(tournament.getPricePool());
        ((TextView) findViewById(R.id.tournamentRegisteredTeams)).setText(String.valueOf(tournament.getRegisteredTeams()));
        location = (TextView) findViewById(R.id.tournamentLocation);
        location.setText(tournament.getLocation());
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.tournamentMap)).getMap();
        map.setMyLocationEnabled(true);


        // Geocoder doesn't work on Android emulator therefore using AsyncLocationReader instead in here
        new AsyncLocationReader().execute(tournament.getLocation());
        /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if (geocoder.isPresent()) {
            try {
                List<Address> addressMatches = geocoder.getFromLocationName(tournament.getLocation(), 1);
                if (addressMatches.size() > 0) {
                    final Address address = addressMatches.get(0);
                    LatLng tournamentLocation = new LatLng(address.getLatitude(), address.getLongitude());
                    showTournamentLocation(tournamentLocation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void displayLocationDistance(float distance) {
        int units = Preferences.getInt(this, Preferences.units);
        String distanceStr = "";
        switch (units) {
            case 0:
                distanceStr = " (" + String.valueOf(Math.round(distance / 1000)) + "km)";
                break;
            case 1:
                distanceStr = " (" + String.valueOf(Math.round(distance / 1609.344f)) + "miles)";
                break;
        }
        location.setText(tournament.getLocation() + distanceStr);
    }

    private class AsyncLocationReader extends AsyncTask<String, Void, LatLng> {
        @Override
        protected LatLng doInBackground(String... strings) {
            return ReverseGeocoder.getFromLocationName(strings[0]);
        }

        @Override
        protected void onPostExecute(final LatLng tournamentLocation) {
            // mark tournament location on mat
            map.addMarker(new MarkerOptions().position(tournamentLocation).title(tournament.getLocation()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(tournamentLocation, 2));
            showTournamentLocation(tournamentLocation);
        }
    }

    private void showTournamentLocation(LatLng tournamentLocation) {
        // try to get user location
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location userLocation = locationManager.getLastKnownLocation(bestProvider);
        if (userLocation != null) {
            // calculate distance between user and tournament location
            float[] result = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), tournamentLocation.latitude, tournamentLocation.longitude, result);
            float distance = result[0];
            displayLocationDistance(distance);
            // move and zoom camera to show both user and tournament location at the same time
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            boundsBuilder.include(tournamentLocation);
            boundsBuilder.include(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
            LatLngBounds bounds = boundsBuilder.build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }

}

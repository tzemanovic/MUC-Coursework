package com.tzemanovic.muccoursework.helper;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tomas Zemanovic on 20/12/2014.

Geocoder on emulated Android bug workaround
https://code.google.com/p/android/issues/detail?id=8816
*/

public class ReverseGeocoder {

    public static LatLng getFromLocationName(String name) {
        // prepare HTTP request
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" + Uri.encode(name));
        StringBuilder builder = new StringBuilder();
        try {
            // send HTTP request
            HttpResponse response = client.execute(httpGet);
            // read response
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // try to convert to JSON object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        double lon = 0;
        double lat = 0;
        // extract longitude and latitude from JSON data
        try {
            JSONObject location = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            lon = location.getDouble("lng");
            lat = location.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LatLng(lat, lon);
    }

}

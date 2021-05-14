//Xuancheng Tu's Skillion coding task

package com.xuanchengtu.codingtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
    This class defines the content of the detail page of each user. After a list entry is clicked
    on the main page, the detail page of the corresponding user will be displayed. The page contains
    user's name, user's company name, email, address, and the location displayed on Google map.
 */
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LatLng latlng; //latitude and longitude data of the user
    private MapView mapView; //the google map that shows the location of user based on data stored
                             //in class field latlng

    /*
       Implement OnMapReadyCallback interface. Add a marker on Google map based on user's
       latitude and longitude
     */
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(latlng));
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }

    /*
       Seven methods below (from onStart() to onLowMemory()) are necessary in order to show the map
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onLowMemory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail");

        //get the detail information about a specific user passed in by intent
        Intent i = getIntent();
        String[] attrs = i.getStringArrayExtra("attrs");

        //update text fields on the page
        ((TextView) findViewById(R.id.username)).setText("Name: " + attrs[0]);
        ((TextView) findViewById(R.id.companyname)).setText("Company name: " + attrs[1]);
        ((TextView) findViewById(R.id.emailaddr)).setText("Email: " + attrs[2]);
        ((TextView) findViewById(R.id.addrcontent)).setText(attrs[3]);

        //display user's location on Google map
        latlng = new LatLng(Double.valueOf(attrs[4]), Double.valueOf(attrs[5]));
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
    }
}
package com.cs541.abel.localization.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import com.cs541.abel.localization.Models.CheckedInLocation;
import com.cs541.abel.localization.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<CheckedInLocation> checkedInLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        Intent intent = getIntent();
        Bundle args = getIntent().getExtras();

        this.checkedInLocations = (ArrayList<CheckedInLocation>) args.getSerializable("ARRAYLIST");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mMap.setMyLocationEnabled(true);

        LatLng myLocation = new LatLng(intent.getDoubleExtra("latitude", -34), intent.getDoubleExtra("longitude", 151));
        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, Float.parseFloat("12.0f")));

        for(int i = 0; i < checkedInLocations.size(); i++) {

            LatLng tempLocation = new LatLng(Double.parseDouble(checkedInLocations.get(i).getLatitude()), Double.parseDouble(checkedInLocations.get(i).getLongitude()));
            String locationName = checkedInLocations.get(i).getLocationName();


            mMap.addMarker(new MarkerOptions().position(tempLocation).title(locationName));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng latLng) {

                   String longitude = Double.toString(latLng.longitude);
                   String latitude = Double.toString(latLng.latitude);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                    builder.setTitle("Location Information");

                    final EditText locationNameEditText = new EditText(MapsActivity.this);
                    locationNameEditText.setHint("Location Name");
                    locationNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(locationNameEditText);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //1. add marker
                            mMap.addMarker(new MarkerOptions().position(latLng).title(locationNameEditText.getText().toString()))
                                    .setDraggable(true);

                            //2. add location to db

                        }
                    });

                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                       }
                   });

                   builder.show();

                }
            });

        }



    }

}

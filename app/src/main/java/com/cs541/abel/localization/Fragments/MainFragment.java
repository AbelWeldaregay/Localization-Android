package com.cs541.abel.localization.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cs541.abel.localization.Adapters.LocationAdapter;
import com.cs541.abel.localization.R;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MainFragment extends Fragment  {


    private TextView longitudeTextView;
    private TextView latitudeTextView;
    private TextView addressTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    Geocoder geocoder;
    List<Address> addresses;
    ArrayList<com.cs541.abel.localization.Models.Location> locations;
    ListView locationsListView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);

            }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.longitudeTextView = view.findViewById(R.id.longitudeTextView);
        this.latitudeTextView = view.findViewById(R.id.latitudeTextView);
        this.addressTextView = view.findViewById(R.id.addressTextView);
        this.locationsListView = view.findViewById(R.id.locationsListView);

        this.locations = new ArrayList<>();
        LocationAdapter locationAdapter = new LocationAdapter(getContext(), R.layout.locations_row, locations);
        locationsListView.setAdapter(locationAdapter);


        this.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location: ", location.toString());
                latitudeTextView.setText("Latitude: " + Double.toString(Double.parseDouble(String.format("%.2f",location.getLatitude()))));
                longitudeTextView.setText("Longitude: " + Double.toString(Double.parseDouble(String.format("%.2f",location.getLongitude()))));
                geocoder = new Geocoder(getContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String area = addresses.get(0).getLocality();
                    String city = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();

                    String fullAddress = address + ", " + area + ", "+city +", "+country+", "+postalcode;

                    addressTextView.setText(fullAddress);
                } catch (IOException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            //we have permission
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.locationListener);
        }

        return view;
    }



}
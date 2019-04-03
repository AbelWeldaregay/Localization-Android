package com.cs541.abel.localization.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs541.abel.localization.Adapters.CheckedInLocationAdapter;
import com.cs541.abel.localization.Models.CheckedInLocation;
import com.cs541.abel.localization.Models.DatabaseClient;
import com.cs541.abel.localization.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    static MainActivity instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView addressTextView;
    EditText locationNameEditText;
    Location lastKnownLocation;
    ListView locationsListView;
    ArrayList<CheckedInLocation> checkedInLocations = new ArrayList<>();


    public static MainActivity getInstance() {
        return instance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        locationNameEditText = findViewById(R.id.checkInNameEditText);
        locationsListView = findViewById(R.id.locationsListView);
        loadCheckedInLocations();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        updateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "the app is called Localization dummy!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();


    }

    private void updateLocation() {
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

    }

    public void checkInHandler(View view) {
        String locationName = (this.locationNameEditText.getText().toString()).trim();



        if(locationName.isEmpty()) {
            Toast.makeText(this, "Check-In name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {

            Double latitude = this.lastKnownLocation.getLatitude();
            Double longitude = this.lastKnownLocation.getLongitude();
            String locationAddress = getCompleteAddressString(latitude, longitude);
            String time = Double.toString(this.lastKnownLocation.getTime());

            CheckedInLocation checkedInLocation = new CheckedInLocation(Double.toString(longitude), Double.toString(latitude), time, locationAddress, locationName);

            saveCheckedInLocation(checkedInLocation);

        }
    }

    public void viewOnGoogleMapsHandler(View view) {

        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);

    }


    public void saveCheckedInLocation(final CheckedInLocation checkedInLocation) {


        class saveCheckedInLocation extends AsyncTask<Void, Void, Void> {


            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(MainActivity.this.getApplicationContext()).getAppDatabase()
                        .checkedInLocationDao()
                        .insert(checkedInLocation);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                checkedInLocations.add(checkedInLocation);

                CheckedInLocationAdapter checkedInLocationAdapter = new CheckedInLocationAdapter(MainActivity.this,
                        R.layout.locations_row, checkedInLocations);

                locationsListView.setAdapter(checkedInLocationAdapter);
                Toast.makeText(MainActivity.this, "Location Saved", Toast.LENGTH_SHORT).show();
            }
        }

        new saveCheckedInLocation().execute();

    }




    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    public void loadCheckedInLocations() {


        class loadCheckedInLocations extends AsyncTask<Void, Void, ArrayList<CheckedInLocation>> {

            @Override
            protected ArrayList<CheckedInLocation> doInBackground(Void... voids) {

                checkedInLocations = (ArrayList<CheckedInLocation>) DatabaseClient.getInstance((MainActivity.this).getApplicationContext()).getAppDatabase()
                        .checkedInLocationDao()
                        .getAll();

                return checkedInLocations;
            }

            @Override
            protected void onPostExecute(ArrayList<CheckedInLocation> checkedInLocations) {
                super.onPostExecute(checkedInLocations);

                final CheckedInLocationAdapter checkedInLocationAdapter = new CheckedInLocationAdapter(MainActivity.this, R.layout.locations_row, checkedInLocations);
                locationsListView.setAdapter(checkedInLocationAdapter);
            }
        }

        new loadCheckedInLocations().execute();




    }


    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void updateTextView(final Location location) {

        this.lastKnownLocation = location;

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String longitude = Double.toString(location.getLongitude());
                String latitude = Double.toString(location.getLatitude());
                String address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                longitudeTextView.setText(longitude);
                latitudeTextView.setText(latitude);
                addressTextView.setText(address);


            }
        });



    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }


    @SuppressLint("LongLogTag")
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                strAdd = "Cannot calculate Address";
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


}

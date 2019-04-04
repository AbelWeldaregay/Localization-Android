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
import com.cs541.abel.localization.Models.SavedLocation;
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

import java.io.Serializable;
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
    Location lastCheckedInLocation;
    ListView locationsListView;
    ArrayList<CheckedInLocation> checkedInLocations = new ArrayList<>();
    ArrayList<SavedLocation> savedLocations = new ArrayList<>();

    /**
     * Get the instance of MainActivity
     * @return
     */
    public static MainActivity getInstance() {
        return instance;
    }


    /**
     * OnCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedLocations();
        instance = this;

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        locationNameEditText = findViewById(R.id.checkInNameEditText);
        locationsListView = findViewById(R.id.locationsListView);
        loadCheckedInLocations();

        if(checkedInLocations.size() != 0) {

            double longitude = Double.parseDouble(checkedInLocations.get(checkedInLocations.size() - 1).getLongitude());
            double latitude = Double.parseDouble(checkedInLocations.get(checkedInLocations.size() - 1).getLatitude());

            lastCheckedInLocation.setLongitude(longitude);
            lastCheckedInLocation.setLatitude(latitude);

        }


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


    /**
     * Send a request for a location update
     */
    private void updateLocation() {
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());

    }


    /**
     * Invoked when check-in button is clicked
     * @param view
     */
    public void checkInHandler(View view) {


        Location checkInLocation = this.lastKnownLocation;

        String checkInLong = Double.toString(checkInLocation.getLongitude());
        String checkInLat = Double.toString(checkInLocation.getLatitude());
        String checkInName = locationNameEditText.getText().toString();


        String checkInTime = (new java.util.Date()).toString();
        String checkInAddress = getCompleteAddressString(checkInLocation.getLatitude(), checkInLocation.getLongitude());

        CheckedInLocation checkedInLocation = new CheckedInLocation(checkInLong, checkInLat, checkInTime, checkInAddress, checkInName);
        SavedLocation savedLocation = new SavedLocation(checkInName, checkInLat, checkInLong, checkInAddress);


        /**
         * 1. find the closest saved location to the check-in location
         */
        if(savedLocations.size() != 0) {

            Location tempLocation = new Location("TEMP_LOCATION");
            tempLocation.setLongitude(Double.parseDouble(savedLocations.get(0).getLongitude()));
            tempLocation.setLatitude(Double.parseDouble(savedLocations.get(0).getLatitude()));

            double shortestDistance = checkInLocation.distanceTo(tempLocation);
            int closestLocationIndex = 0;

            for(int i = 1; i < savedLocations.size(); i++) {

                tempLocation.setLatitude(Double.parseDouble(savedLocations.get(i).getLatitude()));
                tempLocation.setLongitude(Double.parseDouble(savedLocations.get(i).getLongitude()));

                if(checkInLocation.distanceTo(tempLocation) < shortestDistance) {

                    closestLocationIndex = i;
                    shortestDistance = checkInLocation.distanceTo(tempLocation);

                }

            }

            tempLocation.setLongitude(Double.parseDouble(savedLocations.get(closestLocationIndex).getLongitude()));
            tempLocation.setLatitude(Double.parseDouble(savedLocations.get(closestLocationIndex).getLatitude()));

            if(checkInLocation.distanceTo(tempLocation) <= 30) {

                String longitude = savedLocations.get(closestLocationIndex).getLongitude();
                String latitude = savedLocations.get(closestLocationIndex).getLatitude();
                String address = savedLocations.get(closestLocationIndex).getAddress();
                String locationName = savedLocations.get(closestLocationIndex).getLocationName();
                String time = (new java.util.Date()).toString();

                CheckedInLocation newCheckIn = new CheckedInLocation(longitude, latitude, time, address, locationName);
                saveCheckedInLocation(newCheckIn);


            } else {

                saveCheckedInLocation(checkedInLocation);
                saveLocation(savedLocation);
            }


        } else {

            saveCheckedInLocation(checkedInLocation);
            saveLocation(savedLocation);
        }



    }


    /**
     * Starts new intent to open google maps activity
     * @param view
     */
    public void viewOnGoogleMapsHandler(View view) {

        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra("longitude", this.lastKnownLocation.getLongitude());
        intent.putExtra("latitude", this.lastKnownLocation.getLatitude());
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST", (Serializable) this.checkedInLocations);
        intent.putExtras(args);


        startActivity(intent);

    }


    public void loadSavedLocations() {


        class LoadSavedLocations extends AsyncTask<Void, Void, ArrayList<SavedLocation>> {

            @Override
            protected ArrayList<SavedLocation> doInBackground(Void... voids) {

                savedLocations = (ArrayList<SavedLocation>) DatabaseClient.getInstance((MainActivity.this).getApplicationContext()).getAppDatabase()
                        .savedLocationDao()
                        .getAll();

                return savedLocations;
            }

        }

        new LoadSavedLocations().execute();
    }


    /**
     * Save the location given
     * @param savedLocation
     */
    public void saveLocation(final SavedLocation savedLocation) {

        class saveLocation extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(MainActivity.this.getApplicationContext()).getAppDatabase()

                        .savedLocationDao()

                        .insert(savedLocation);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                savedLocations.add(savedLocation);
                Toast.makeText(MainActivity.this, "New Location Saved", Toast.LENGTH_SHORT).show();


            }
        }

        new saveLocation().execute();


    }

    /**
     * Writes the checked in location to mysqli database
     * @param checkedInLocation
     */
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


    /**
     * Loads all the checked-in locations to checkedInLocations ArrayList
     */
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


    /**
     * Returns a string address of the given lat and long
     * @param LATITUDE
     * @param LONGITUDE
     * @return
     */
    @SuppressLint("LongLogTag")
    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
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

package com.cs541.abel.localization.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class MyLocationService extends BroadcastReceiver {


    public static final String ACTION_PROCESS_UPDATE = "com.cs541.abel.localization.Activities.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent != null) {

            final String action = intent.getAction();

            if(ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);

                if(result != null) {

                    Location location = result.getLastLocation();
                    try {
                        MainActivity.getInstance().updateTextView(location);

                    } catch (Exception ex) {
                        Toast.makeText(context, location.toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            }

        }

    }
}

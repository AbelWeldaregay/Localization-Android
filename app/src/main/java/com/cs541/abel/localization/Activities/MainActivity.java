package com.cs541.abel.localization.Activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.cs541.abel.localization.Models.CheckedInLocation;
import com.cs541.abel.localization.Models.CheckedInLocationsDB;
import com.cs541.abel.localization.R;


public class MainActivity extends AppCompatActivity {

    private  CheckedInLocationsDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = Room.databaseBuilder(getApplicationContext(),
                CheckedInLocationsDB.class, "checked_in_locations").build();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                CheckedInLocation checkedInLocation = new CheckedInLocation();
//
//                db.checkedInLocationDao() . insertAll(checkedInLocation);
//
//            }
//        }) .start();

    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        return super.onCreateView(parent, name, context, attrs);
    }
}

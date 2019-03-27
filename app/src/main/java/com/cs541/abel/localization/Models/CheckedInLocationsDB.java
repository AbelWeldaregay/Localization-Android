package com.cs541.abel.localization.Models;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {CheckedInLocation.class}, version = 1)
public abstract class CheckedInLocationsDB extends RoomDatabase {

    public abstract CheckedInLocationDao checkedInLocationDao();

}

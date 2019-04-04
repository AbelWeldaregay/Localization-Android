package com.cs541.abel.localization.Models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SavedLocationDao {

    @Query("SELECT * FROM savedlocation")
    List<SavedLocation> getAll();


    @Insert
    void insertAll(ArrayList<SavedLocation> savedLocations);

    @Insert
    void insert(SavedLocation savedLocation);

    @Update
    void updateSavedLocations(SavedLocation... savedLocations);

    @Delete
    void delete(SavedLocation... savedLocations);

}

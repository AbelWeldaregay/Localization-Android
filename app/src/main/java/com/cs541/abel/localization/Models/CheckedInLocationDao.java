package com.cs541.abel.localization.Models;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CheckedInLocationDao {

    @Query("SELECT * FROM checkedinlocation")
    List<CheckedInLocation> getAll();

    @Insert
    void insertAll(ArrayList<CheckedInLocation> checkedInLocations);

    @Insert
    void insert(CheckedInLocation checkedInLocation);

    @Update
    void updateCheckedInLocations(CheckedInLocation... checkedInLocations);

    @Delete
    void delete(CheckedInLocation... checkedInLocations);

}

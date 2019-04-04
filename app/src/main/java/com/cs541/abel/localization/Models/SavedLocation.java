package com.cs541.abel.localization.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

@Entity
public class SavedLocation {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "location")
    private Location location;

    @ColumnInfo(name = "address")
    private String address;

    public SavedLocation(int uid, Location location, String address) {
        this.uid = uid;
        this.location = location;
        this.address = address;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUid() {
        return uid;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }
}

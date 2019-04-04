package com.cs541.abel.localization.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CheckedInLocation implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "latitude")
    private String latitude;

    @ColumnInfo(name = "longitude")
    private String longitude;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "area")
    private String area;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "postalCode")
    private String postalCode;

    @ColumnInfo(name = "locationName")
    private String locationName;

    public CheckedInLocation(String longitude, String latitude, String time, String address, String locationName) {

        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.address = address;
        this.locationName = locationName;


    }

    public int getUid() {
        return uid;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getTime() {return time; }

    public void setTime(String time) {this.time = time; }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}

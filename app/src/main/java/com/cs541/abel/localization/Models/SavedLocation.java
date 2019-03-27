package com.cs541.abel.localization.Models;

public class SavedLocation {

    private String longitude;
    private String latitude;
    private String time;
    private String address;
    private String nickName;

    public SavedLocation(String longitude, String latitude, String time, String address, String nickName) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
        this.address = address;
        this.nickName = nickName;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public String getNickName() {
        return nickName;
    }
}
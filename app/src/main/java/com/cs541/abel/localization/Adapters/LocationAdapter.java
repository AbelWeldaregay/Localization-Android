package com.cs541.abel.localization.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs541.abel.localization.Models.SavedLocation;
import com.cs541.abel.localization.R;

import java.util.ArrayList;

public class LocationAdapter extends ArrayAdapter<SavedLocation> {

    public static final String TAG = "locationAdapter";
    private Context mContext;
    private int mResource;


    static class ViewHolder {

        TextView longtitudeTextView;
        TextView latitudeTextView;
        TextView timeTextView;
        TextView nicknameTextView;
        TextView addressTextView;
    }

    public LocationAdapter(Context context, int resource, ArrayList<SavedLocation> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mResource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String longtitude = getItem(position).getLongitude();
        String latitude = getItem(position).getLatitude();
        String address = getItem(position).getAddress();
        String time = getItem(position).getTime();
        String nickName = getItem(position).getNickName();

        SavedLocation newLocation = new SavedLocation(longtitude, latitude, time, address, nickName);

        ViewHolder holder;

        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            convertView = inflater.inflate(this.mResource, parent, false);

            holder = new ViewHolder();

            holder.latitudeTextView = convertView.findViewById(R.id.latTextView);
            holder.longtitudeTextView = convertView.findViewById(R.id.longTextView);
            holder.addressTextView = convertView.findViewById(R.id.addressTextView);
            holder.nicknameTextView = convertView.findViewById(R.id.customNameTextView);
            holder.timeTextView = convertView.findViewById(R.id.timeTextView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }


        holder.timeTextView.setText(time);
        holder.nicknameTextView.setText(nickName);
        holder.addressTextView.setText(address);
        holder.longtitudeTextView.setText(longtitude);
        holder.latitudeTextView.setText(latitude);



        return convertView;

    }



}

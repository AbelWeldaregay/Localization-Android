package com.cs541.abel.localization.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.cs541.abel.localization.Models.CheckedInLocation;
import com.cs541.abel.localization.R;
import java.util.ArrayList;

public class CheckedInLocationAdapter extends ArrayAdapter<CheckedInLocation> {

    public static final String TAG = "CheckedInLocationAdapter";
    private Context mContext;
    private int mResource;

    /**
     * Holds a view
     * @author Abel Weldaregay
     */
    static class ViewHolder {

        TextView longitudeTextView;
        TextView latitudeTextView;
        TextView timeTextView;
        TextView addressTextView;
        TextView locationNameTextview;

    }

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public CheckedInLocationAdapter(Context context, int resource, ArrayList<CheckedInLocation> objects) {

        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;

    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String longitude = getItem(position).getLongitude();
        String latitude = getItem(position).getLatitude();
        String time = getItem(position).getTime();
        String address = getItem(position).getAddress();
        String locationName = getItem(position).getLocationName();

        CheckedInLocation checkedInLocation = new CheckedInLocation(longitude, latitude, time, address, locationName);

        ViewHolder holder;

        if(convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.addressTextView = (TextView) convertView.findViewById(R.id.addressRowTextView);
            holder.latitudeTextView = (TextView) convertView.findViewById(R.id.latTextView);
            holder.longitudeTextView = (TextView) convertView.findViewById(R.id.longTextView);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.locationNameTextview = (TextView) convertView.findViewById(R.id.locationNameTextView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.addressTextView.setText("Address: " + address);
        holder.locationNameTextview.setText(locationName);
        holder.timeTextView.setText("Time Stamp: " + time);
        holder.latitudeTextView.setText("Latitude  : " + latitude);
        holder.longitudeTextView.setText("Longitude: " + longitude);


        return convertView;

    }




}

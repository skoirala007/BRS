package com.example.pravasagar.brs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shruti on 4/26/2016.
 */
class ScheduleListAdapter extends ArrayAdapter<ScheduleView> {

    public ScheduleListAdapter(Context context, int resource, List<ScheduleView> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.scheduleitem, parent, false);
        }
        // Lookup view for data population
        TextView Day = (TextView) convertView.findViewById(R.id.day);
        TextView Time = (TextView) convertView.findViewById(R.id.time);
        TextView Driver = (TextView) convertView.findViewById(R.id.drid);
        
        // Populate the data into the template view using the data object
        ScheduleView scheduleView = getItem(position);

        Day.setText(scheduleView.getDay());
        Time.setText(scheduleView.getTime());
        Driver.setText(scheduleView.getDriverId());

        // Return the completed view to render on screen
        return convertView;
    }
}

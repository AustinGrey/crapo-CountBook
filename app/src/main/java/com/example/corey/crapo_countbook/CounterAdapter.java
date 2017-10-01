/*
 * CounterAdapter
 *
 * V 1.0
 *
 * September 27 2017
 *
 * Copyright (c) 2017. Austin Crapo. Permission is granted to the University of Alberta to
 * compile, run, inspect or edit the code as required for grading purposes.
 */
package com.example.corey.crapo_countbook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * CounterAdapter
 *
 * Converts a Counter array in a list of views for an activity to be able to present
 *
 * Some adaptations from
 * https://stackoverflow.com/questions/27849550/how-do-i-update-a-listview-from-an-arrayadapter-class
 * Accessed Sept 26 2017
 */

public class CounterAdapter extends ArrayAdapter<Counter> {
    public static final String EDIT_INDEX = "com.example.myfirstapp.EDIT_INDEX";
    public static final String COUNTER_STORE_FILENAME = "com.example.myfirstapp.COUNTER_STORE_FILENAME";
    private ArrayList<Counter> datalist;
    private Context context;
    private CounterStore counterStore;

    /**
     * Contructs a CounterAdapter
     * @param context app context using this CounterAdapter
     * @param resource XML resource used to interpret Counter objects
     * @param objects ArrayList of counters the adapter will be interpreting
     * @param counterStore CounterStore object of app context creating this adaptor for persistance
     */
    public CounterAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<Counter> objects, CounterStore counterStore) {
        super(context, resource, objects);
        this.datalist = (ArrayList<Counter>) objects;
        this.context = context;
        this.counterStore = counterStore;
    }

    // Adapted from https://github.com/codepath/android-custom-array-adapter-demo/blob/master/app/src/main/java/com/codepath/example/customadapterdemo/CustomUsersAdapter.java
    // Sept 26 2017
    @Override

    /**
     * Creates a ViewGroup using a list item and Counter to represent the Counter
     * @param position index into the array the counter is
     * @param convertView converted XML view
     * @param parent parent view to attach new view to
     * @return
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Counter user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Button addButton = (Button) convertView.findViewById(R.id.liAdd);
        Button subButton = (Button) convertView.findViewById(R.id.liSub);
        Button editButton = (Button) convertView.findViewById(R.id.liEdit);

        final Counter dataitem = datalist.get(position);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataitem.changeCount(1);
                counterStore.commit(datalist);
                notifyDataSetChanged();
            }
        });
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataitem.changeCount(-1)){
                    counterStore.commit(datalist);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context.getApplicationContext(),
                            "Negative counts are not supported", Toast.LENGTH_SHORT).show();
                }

            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adapted from https://developer.android.com/training/basics/firstapp/starting-activity.html
                // Sept 26 2016

                Intent intent = new Intent(context, EditCounterActivity.class);
                // Give the new editCounterActivity enough information to access the shared file
                intent.putExtra(EDIT_INDEX, position);
                intent.putExtra(COUNTER_STORE_FILENAME, counterStore.getFilename());
                context.startActivity(intent);
            }
        });


        // Lookup view for data population
        TextView liCount = (TextView) convertView.findViewById(R.id.liCount);
        TextView liName = (TextView) convertView.findViewById(R.id.liName);
        TextView liDate = (TextView) convertView.findViewById(R.id.liDate);
        // Populate the data into the template view using the data object
        liCount.setText(String.valueOf(user.getCount()));
        liName.setText(user.getName());
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yy\nHH:mm:ss");
        liDate.setText(dateFormat.format(user.getModified_date()));

        // Return the completed view to render on screen
        return convertView;
    }
}

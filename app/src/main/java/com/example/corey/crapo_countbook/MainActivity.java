/*
 * MainActivity
 *
 * V 1.0
 *
 * September 27 2017
 *
 * Copyright (c) 2017. Austin Crapo. Permission is granted to the University of Alberta to
 * compile, run, inspect or edit the code as required for grading purposes.
 */

package com.example.corey.crapo_countbook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * MainActivity
 *
 * Presents the list of counters and a small interface for adding new counters.
 */
public class MainActivity extends Activity {

    static final String FILENAME = "file.sav"; // File data will be stored in for this app

    // Views to be used to assist in presenting counters and adding new counters
    private EditText nameText;
    private EditText initCountText;
    private EditText commentText;
    private ListView counterView;
    private CounterStore counterStore;

    // Data handling variables
    private ArrayList<Counter> counterList;
    private CounterAdapter adapter;
    private TextView summary;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Create a storage object for passing data between activities
        counterStore = new CounterStore(FILENAME, this);

        // Gather Views for editing
        nameText = (EditText) findViewById(R.id.mName);
        initCountText = (EditText) findViewById(R.id.mInitialValue);
        commentText = (EditText) findViewById(R.id.mComment);
        Button saveButton = (Button) findViewById(R.id.save);
        counterView = (ListView) findViewById(R.id.oldTweetsList);
        summary = (TextView) findViewById(R.id.mSummary);

        // Save button to create a new Counter
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                String name = nameText.getText().toString();
                String initCountString = initCountText.getText().toString();
                // Dont let invalid counters get created
                if(name.equals("")){
                    if (initCountString.equals("")){
                        Toast.makeText(getApplicationContext(),
                                "Name and Initial Count are required", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Name is required", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else {
                    if (initCountString.equals("")){
                        Toast.makeText(getApplicationContext(),
                                "Initial Count is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                int initialCount = Integer.valueOf(initCountString);
                String comment = commentText.getText().toString();

                // Reset fields for next counter
                nameText.setText("");
                initCountText.setText("");
                commentText.setText("");

                Counter newCounter = new Counter(initialCount, name, comment);
                counterList.add(newCounter);

                adapter.notifyDataSetChanged();
                updateSummary(counterList.size());

                // Save changes to file
                save();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // We must load the array from file first because this is where changes will be committed
        load();
        adapter = new CounterAdapter(this, R.layout.list_item, counterList, counterStore);
        counterView.setAdapter(adapter);
        // Also make sure the summary has a proper updated count being displayed
        updateSummary(counterView.getCount());
    }

    /**
     * Loads counterList with the representation currently on file
     */
    private void load(){
        counterList = counterStore.load();
    }

    /**
     * Replaces with counterList on file with the one currently in memory
     */
    private void save(){
        counterStore.commit(counterList);
    }

    /**
     * Updates the summary textview to contain the specified value
     * @param value value for summary to display
     */
    private void updateSummary(int value){
        summary.setText("Summary: " + String.valueOf(value) + " counter(s)");
    }
}



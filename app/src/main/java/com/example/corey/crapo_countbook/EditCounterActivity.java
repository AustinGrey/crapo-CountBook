/*
 * EditCounterActivity
 *
 * V 1.0
 *
 * September 27 2017
 *
 * Copyright (c) 2017. Austin Crapo. Permission is granted to the University of Alberta to
 * compile, run, inspect or edit the code as required for grading purposes.
 */
package com.example.corey.crapo_countbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * EditCounterActivity
 *
 * Activity that loads data from a single Counter, and presents it for editing by a user.
 */
public class EditCounterActivity extends AppCompatActivity {
    private ArrayList<Counter> datalist;
    private CounterStore counterStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_counter);
        // Get the Intent that started this activity and extract the information about the file and
        // position where the counter we want to edit is stored
        Intent intent = getIntent();
        final int index = intent.getIntExtra(CounterAdapter.EDIT_INDEX, -1);
        final String FILENAME = intent.getStringExtra(CounterAdapter.COUNTER_STORE_FILENAME);
        if (index == -1) {
            // Nothing can be done that wouldn't be unexpected for the user, we have not been passed
            // a proper index to the array.
            finish();
        }
        // Create a new file handler and load up the array
        counterStore = new CounterStore(FILENAME,this);
        load();
        // Then grab the specific counter we want to edit
        final Counter counter = datalist.get(index);
        super.onCreate(savedInstanceState);


        // Get the button views
        Button saveButton = (Button) this.findViewById(R.id.ecSave);
        Button resButton = (Button) this.findViewById(R.id.ecReset);
        Button delButton = (Button) this.findViewById(R.id.ecDelete);

        // Capture the layout's TextViews and set the values of the counter as their text
        final TextView name = (TextView) findViewById(R.id.ecName);
        final TextView initCount = (TextView) findViewById(R.id.ecInitial);
        final TextView comment = (TextView) findViewById(R.id.ecComment);
        final TextView count = (TextView) findViewById(R.id.ecCount);
        name.setText(counter.getName());
        initCount.setText(String.valueOf(counter.getStart_val()));
        comment.setText(counter.getComment());
        count.setText(String.valueOf(counter.getCount()));

        // The save button should update the new counter values to file
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                String nameString = name.getText().toString();
                String initCountString = initCount.getText().toString();
                String countString = count.getText().toString();

                // Avoid invalid counter values
                String toastString = "Required: ";
                if(nameString.equals("")){
                    toastString += "name, ";
                }
                if(initCountString.equals("")){
                    toastString += "initial count, ";
                }
                if(countString.equals("")){
                    toastString += "count";
                }
                if(!toastString.equals("Required: ")){
                    if(toastString.endsWith(", ")){
                        toastString = toastString.substring(0, toastString.length() - 2);
                    }
                    Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set the values and write to file
                counter.setName(nameString);
                counter.setStart_val(Integer.decode(initCountString));
                counter.setComment(comment.getText().toString());
                counter.setCount(Integer.decode(countString));
                save();
                finish();
            }
        });

        resButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                // We could have used counter.resetCount(), but the user would intuitively
                // expect the current text in the initCount to be the the target value, even
                // though the initCount value has not been saved.
                count.setText(initCount.getText());
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                datalist.remove(index);
                save();
                finish();
            }
        });
    }

    // load to the datalist from file
    private void load(){
        datalist = counterStore.load();
    }

    // save the datalist to file
    private void save(){
        counterStore.commit(datalist);
    }
}

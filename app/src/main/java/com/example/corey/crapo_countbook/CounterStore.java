/*
 * CounterStore
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * CounterStore
 *
 * Manages a file which is meant to store an array of counters in JSON format.
 */
public class CounterStore {
    private String FILENAME;
    private Context context;
    private ArrayList<Counter> counterList;

    /**
     * Contructs a CounterStore
     * @param FILENAME file for writing to and reading from
     * @param context app context managing this CounterStore
     */
    public CounterStore(String FILENAME, Context context) {
        this.FILENAME = FILENAME;
        this.context = context;
    }

    /**
     * Takes a counterList and writes it to file
     * @param counterList list to write to file
     */
    public void commit(ArrayList<Counter> counterList){
        this.counterList = counterList;
        saveInFile();
    }

    /**
     * Loads an ArrayList of counters from file and returns it
     * @return ArrayList of counters read from file
     */
    public ArrayList<Counter> load(){
        loadFromFile();
        return this.counterList;
    }

    /**
     * Returns the file name in use by this CounterStore. Useful for passing to another activity
     * so it can have its own CounterStore reading from this file.
     * @return filename in use by this counter store
     */
    public String getFilename(){
        return this.FILENAME;
    }

    /**
     * Loads data from the file and converts it into an ArrayList of counters and then stores it
     * in it's memory.
     */
    private void loadFromFile() {
        try {
            FileInputStream fis = this.context.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-09-19
            Type listType = new TypeToken<ArrayList<Counter>>(){}.getType();
            counterList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            counterList = new ArrayList<Counter>();
        }
    }

    /**
     * Writes the current in memory ArrayList of counters to file, overwriting contents.
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = this.context.openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(counterList, out);
            out.flush(); /*forces the buffer to be written*/

            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }

    }
}

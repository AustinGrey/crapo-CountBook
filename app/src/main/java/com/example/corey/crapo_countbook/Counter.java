/*
 * Counter
 *
 * V 1.0
 *
 * September 27 2017
 *
 * Copyright (c) 2017. Austin Crapo. Permission is granted to the University of Alberta to
 * compile, run, inspect or edit the code as required for grading purposes.
 */
package com.example.corey.crapo_countbook;

import java.util.Date;

/**
 * Counter
 *
 * Manages a count of some named value
 */
public class Counter{
    private int count;
    private int start_val;
    private String name;
    private String comment;
    private Date modified_date;

    /**
     * Contructs a Counter
     * @param start_val initial value for the counter to be at
     * @param name name for the counter
     */
    public Counter(int start_val, String name) {
        this.start_val = start_val;
        this.count = start_val;
        this.name = name;
        this.comment = "";
        this.modified_date = new Date();
    }

    /**
     * Contructs a counter
     * @param start_val initial value for the counter to be at
     * @param name name for te counter
     * @param comment comment for the counter
     */
    public Counter(int start_val, String name, String comment) {
        this.start_val = start_val;
        this.count = start_val;
        this.name = name;
        this.comment = comment;
        this.modified_date = new Date();
    }

    /**
     * Get the current count value
     * @return current count value
     */
    public int getCount() {
        return count;
    }

    /**
     * Set the current count to a specific value. Also updates the modified date if successful
     * @param count specific value to set count to, must be >= 0
     * @return true if the count was set, false if it was not (eg. if value was negative)
     */
    public boolean setCount(int count) {
        // Set the count to a specific value, do not let it go below 0
        if (count >= 0){
            this.count = count;
            // Only update the date if the counter was updated.
            updateModified_date();
            return true;
        }
        return false;
    }

    /**
     * Change the count by a specific difference. Also updates the modified date if successful.
     * @param delta amount to change the counter by
     * @return true if the counter was changed, false if not
     */
    public boolean changeCount(int delta) {
        // Update the count by adding the delta to it,
        int newCount = this.getCount() + delta;
        return this.setCount(newCount);
    }

    /**
     * Resets the current count to match the initial count. Also updates the modified date
     * if successful
     */
    public void resetCount() {
        // Update the counter to its initial value
        this.count = this.getStart_val();
        updateModified_date();
    }

    /**
     * Get the initial count of the counter
     * @return initial count of the counter
     */
    public int getStart_val() {
        return start_val;
    }

    /**
     * Set the initial count of the counter. Also updates the modified date if successful
     * @param start_val initial count of the counter, must be >= 0
     */
    public void setStart_val(int start_val) {
        if (start_val >= 0){
            this.start_val = start_val;
            updateModified_date();
        }

    }

    /**
     * Get the name of the counter
     * @return name of the counter
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the counter. Also updates the modified date.
     * @param name name of the counter
     */
    public void setName(String name) {
        this.name = name;
        updateModified_date();
    }

    /**
     * Get the comment for this counter
     * @return comment for this counter
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment for this counter. Also updates the modified date.
     * @param comment comment for this counter
     */
    public void setComment(String comment) {
        this.comment = comment;
        updateModified_date();
    }

    /**
     * Get the last modified date of the counter. It is the creation date if not yet modified.
     * @return last modified date of the counter.
     */
    public Date getModified_date() {
        return modified_date;
    }

    /**
     * Set the last modified date of the counter, this should not happen outside of this class in
     * order to protect the validity of the data.
     * @param modified_date modified date of the counter
     */
    private void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    /**
     * Update the modified date to the current time
     */
    private void updateModified_date(){
        this.modified_date = new Date();
    }

    @Override
    public String toString(){
        return this.getName() + this.getCount();
    }
}

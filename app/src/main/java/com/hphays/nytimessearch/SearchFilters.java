package com.hphays.nytimessearch;

import android.os.Parcelable;

import org.parceler.Parcel;


/**
 * Created by hhays on 11/15/16.
 */
@Parcel
public class SearchFilters {

    String beginDate;
    boolean arts;
    boolean fashionAndStyle;
    boolean sports;
    boolean newest;


    //empty constructor needed by parceler library
    public SearchFilters() {
    }

    public SearchFilters(String date, boolean arts, boolean fashionAndStyle, boolean sports, boolean newest) {
        this.beginDate = date;
        this.arts = arts;
        this.fashionAndStyle = fashionAndStyle;
        this.sports = sports;
        this.newest = newest;
    }
}
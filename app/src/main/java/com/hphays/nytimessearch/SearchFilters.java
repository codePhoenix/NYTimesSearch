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

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setArts(boolean arts) {
        this.arts = arts;
    }

    public void setFashionAndStyle(boolean fashionAndStyle) {
        this.fashionAndStyle = fashionAndStyle;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public void setNewest(boolean newest) {
        this.newest = newest;
    }

    boolean newest;

    public void setOldest(boolean oldest) {
        this.oldest = oldest;
    }

    boolean oldest;


    //empty constructor needed by parceler library
    public SearchFilters() {
    }

    public SearchFilters(String date, boolean arts, boolean fashionAndStyle, boolean sports, boolean newest, boolean oldest) {
        this.beginDate = date;
        this.arts = arts;
        this.fashionAndStyle = fashionAndStyle;
        this.sports = sports;
        this.newest = newest;
        this.oldest = oldest;
    }
}
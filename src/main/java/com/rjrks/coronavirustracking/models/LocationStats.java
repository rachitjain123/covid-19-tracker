package com.rjrks.coronavirustracking.models;

import java.text.NumberFormat;

public class LocationStats {

    private String country;
    private int latestTotalCases;

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    private int diffFromPrevDay;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public String getNumFormattedLatestTotalCases() {
        return NumberFormat.getIntegerInstance().format(latestTotalCases);
    }

    public String getNumFormattedDiffFromPrevDay() {
        return NumberFormat.getIntegerInstance().format(diffFromPrevDay);
    }


}

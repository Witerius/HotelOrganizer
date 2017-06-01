package com.wizardmb.witerius.hotelorganizer.model;

/**
 * Created by Witerius on 24.09.2016.
 */

import java.util.Date;

public final class ModelAllData {

    private int apartmentID;

    private long timeStamp;

    private long date_start;
    private long date_end;

    private int date_startInt;
    private int date_endInt;

    private int dolg;
    private int oplacheno;
    private String notice;
    private String name;
    private String mobil;
    private int isApplicants;

    public final int getIsApplicants() {
        return isApplicants;
    }

    public final void setApplicants(int applicants) {
        isApplicants = applicants;
    }

    public final int getOplacheno() {
        return oplacheno;
    }

    public final void setOplacheno(int oplacheno) {
        this.oplacheno = oplacheno;
    }

    public final int getDolg() {
        return dolg;
    }

    public final void setDolg(int dolg) {
        this.dolg = dolg;
    }

    public final long getDate_end() {
        return date_end;
    }

    public final void setDate_end(long date_end) {
        this.date_end = date_end;
    }

    public final long getDate_start() {
        return date_start;
    }

    public final void setDate_start(long date_start) {
        this.date_start = date_start;
    }

    public final long getTimeStamp() {
        return timeStamp;
    }

    public final int getApartmentID() {
        return apartmentID;
    }

    public final int getDate_startInt() {
        return date_startInt;
    }

    public final void setDate_startInt(int date_startInt) {
        this.date_startInt = date_startInt;
    }

    public final int getDate_endInt() {
        return date_endInt;
    }

    public final  void setDate_endInt(int date_endInt) {
        this.date_endInt = date_endInt;
    }

    public final void setApartmentID(int apartmentID) {
        this.apartmentID = apartmentID;
    }

    public final String getNotice() {
        return notice;
    }
    public final void setNotice(String notice) {
        this.notice = notice;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getMobil() {
        return mobil;
    }

    public final void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public ModelAllData() {
        this.timeStamp = new Date().getTime();
    }

    public ModelAllData(long timeStamp, int apartmentID, long date_start,
                        long date_end, int dolg, int oplacheno, String notice, int date_startInt, int date_endInt, String name, String mobil, int isApplicants) {

        this.apartmentID = apartmentID;

        this.timeStamp = timeStamp;

        this.date_start = date_start;
        this.date_end = date_end;
        this.date_startInt = date_startInt;
        this.date_endInt = date_endInt;

        this.dolg = dolg;
        this.oplacheno = oplacheno;
        this.notice = notice;
        this.mobil = mobil;
        this.name = name;
        this.isApplicants = isApplicants;

    }


}

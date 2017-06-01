package com.wizardmb.witerius.hotelorganizer.database;

/**
 * Created by User on 24.03.2016.
 */

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;

public final class DBUpdateManager_AllData {

    private SQLiteDatabase database;

    DBUpdateManager_AllData(SQLiteDatabase database) {
        this.database = database;

    }


    private void apartmentId(long timeStamp, int apartmentId) {

        update(DBHelperA.DATA_APARTMENT_ID_COLUMN, timeStamp, apartmentId);
    }

    private void dolg(long timeStamp, int dolg) {

        update(DBHelperA.DATA_DOLG_COLUMN, timeStamp, dolg);
    }

    private void oplacheno(long timeStamp, int oplacheno) {

        update(DBHelperA.DATA_OPLACHENO_COLUMN, timeStamp, oplacheno);
    }

    private void startDay(long timeStamp, long startDay)
    {

        update(DBHelperA.DATA_START_COLUMN, timeStamp, startDay);
    }

    private void endDay(long timeStamp, long endDay)
    {

        update(DBHelperA.DATA_END_COLUMN, timeStamp, endDay);
    }
    private void startDayInt(long timeStamp, int startDay)
    {

        update(DBHelperA.DATA_START_INT_COLUMN, timeStamp, startDay);
    }

    private void endDayInt(long timeStamp, int endDay)
    {

        update(DBHelperA.DATA_END_INT_COLUMN, timeStamp, endDay);
    }
    private void notice(long timeStamp, String notice)
    {

        update(DBHelperA.DATA_NOTICE_COLUMN, timeStamp, notice);
    }
    private void name(long timeStamp, String name)
    {

        update(DBHelperA.DATA_NAME_CLIENT_COLUMN, timeStamp, name);
    }
    private void mobil(long timeStamp, String mobil)
    {

        update(DBHelperA.DATA_MOBIL_CLIENT_COLUMN, timeStamp, mobil);
    }

    private void applicants(long timeStamp, int applicants)
    {

        update(DBHelperA.DATA_IS_APPLICANTS_COLUMN, timeStamp, applicants);
    }



    public final void itemMethod(ModelAllData modelAllData) {

        apartmentId(modelAllData.getTimeStamp(), modelAllData.getApartmentID());
        dolg(modelAllData.getTimeStamp(), modelAllData.getDolg());
        oplacheno(modelAllData.getTimeStamp(), modelAllData.getOplacheno());

        startDay(modelAllData.getTimeStamp(), modelAllData.getDate_start());
        endDay(modelAllData.getTimeStamp(), modelAllData.getDate_end());

        startDayInt(modelAllData.getTimeStamp(), modelAllData.getDate_startInt());
        endDayInt(modelAllData.getTimeStamp(), modelAllData.getDate_endInt());
        notice(modelAllData.getTimeStamp(), modelAllData.getNotice());
        name(modelAllData.getTimeStamp(), modelAllData.getName());
        mobil(modelAllData.getTimeStamp(), modelAllData.getMobil());
        applicants(modelAllData.getTimeStamp(), modelAllData.getIsApplicants());
    }


    private void update(String column, long key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_ALL_DATA, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

    private void update (String column, long key, long value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_ALL_DATA, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

    private void update (String column, long key, int value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_ALL_DATA, cv, DBHelperA.DATA_TIME_STAMP_COLUMN + " = " + key, null);
    }

}
package com.wizardmb.witerius.hotelorganizer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

/**
 * Created by User on 29.04.2016.
 */
public final class DBHelperA extends SQLiteOpenHelper {
    private static final int DBHelperA = 1;
    private static final String DATABASE_NAME = "hotel_organizer_database";

    static final String TABLE_BASE_ALL_DATA = "base_all_data_table";

    static final String TABLE_BASE_APARTMENT = "base_apartment_table";

    public DBHelperA(Context context) {
        super(context, DATABASE_NAME, null, DBHelperA);

        queryManager_all_data = new DBQueryManager_AllData(getReadableDatabase());
        updateManager_all_data = new DBUpdateManager_AllData(getWritableDatabase());

        queryManager_apartment = new DBQueryManager_apartment(getReadableDatabase());
        updateManager_apartment = new DBUpdateManager_apartment(getWritableDatabase());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BASE_ALL_DATA_TABLE_CREATE_SCRIPT);
        db.execSQL(BASE_APARTMENT_TABLE_CREATE_SCRIPT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_BASE_ALL_DATA);
        db.execSQL("DROP TABLE " + TABLE_BASE_APARTMENT);

        onCreate(db);

    }

    ////////////////////////////////////////////////
    static final String DATA_TIME_STAMP_COLUMN = "data_time_stamp";

    static final String DATA_MOBIL_CLIENT_COLUMN = "data_client_mobil";
    static final String DATA_NAME_CLIENT_COLUMN = "data_client_name";
    static final String DATA_APARTMENT_ID_COLUMN = "data_apartment_id";

    static final String DATA_START_COLUMN = "data_date_start";
    static final String DATA_END_COLUMN = "data_date_end";

    static final String DATA_START_INT_COLUMN = "data_date_start_int";
    static final String DATA_END_INT_COLUMN = "data_date_end_int";

    static final String DATA_OPLACHENO_COLUMN = "data_oplacheno";
    static final String DATA_DOLG_COLUMN = "data_dolg";
    static final String DATA_NOTICE_COLUMN = "data_notice";
    static final String DATA_IS_APPLICANTS_COLUMN = "data_applicants";

    static final String BASE_ALL_DATA_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_BASE_ALL_DATA + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DATA_APARTMENT_ID_COLUMN + " INTEGER, " + DATA_END_COLUMN + " LONG, "
            + DATA_END_INT_COLUMN + " INTEGER, "+ DATA_START_INT_COLUMN + " INTEGER, "
            + DATA_NOTICE_COLUMN + " STRING, "+ DATA_NAME_CLIENT_COLUMN + " STRING, "
            + DATA_MOBIL_CLIENT_COLUMN + " STRING, " + DATA_IS_APPLICANTS_COLUMN + " INTEGER, "
            + DATA_START_COLUMN + " LONG, " + DATA_OPLACHENO_COLUMN + " INTEGER, "
            + DATA_DOLG_COLUMN + " INTEGER, " + DATA_TIME_STAMP_COLUMN + " LONG);";

    private static final String SELECTION_TIME_STAMP_ALL_DATA = DATA_TIME_STAMP_COLUMN + " = ?";


    private DBQueryManager_AllData queryManager_all_data;

    private DBUpdateManager_AllData updateManager_all_data;

    public final  void saveAllDataInBase(ModelAllData task) {
        ContentValues newValues = new ContentValues();

        newValues.put(DATA_TIME_STAMP_COLUMN, task.getTimeStamp());

        newValues.put(DATA_MOBIL_CLIENT_COLUMN, task.getMobil());
        newValues.put(DATA_NAME_CLIENT_COLUMN, task.getName());
        newValues.put(DATA_APARTMENT_ID_COLUMN, task.getApartmentID());

        newValues.put(DATA_START_COLUMN, task.getDate_start());
        newValues.put(DATA_END_COLUMN, task.getDate_end());

        newValues.put(DATA_START_INT_COLUMN, task.getDate_startInt());
        newValues.put(DATA_END_INT_COLUMN, task.getDate_endInt());
        newValues.put(DATA_NOTICE_COLUMN, task.getNotice());

        newValues.put(DATA_OPLACHENO_COLUMN, task.getOplacheno());
        newValues.put(DATA_DOLG_COLUMN, task.getDolg());

        newValues.put(DATA_IS_APPLICANTS_COLUMN, task.getIsApplicants());

        getWritableDatabase().insert(TABLE_BASE_ALL_DATA, null, newValues);

    }

    public final DBQueryManager_AllData queryAllData() {

        return queryManager_all_data;

    }

    public final DBUpdateManager_AllData updateAllData() {

        return updateManager_all_data;
    }

    public final void removeDataInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_ALL_DATA, SELECTION_TIME_STAMP_ALL_DATA, new String[]{Long.toString(timeStamp)});

    }


    ////////////////////////////////////////////////

    static final String APARTMENT_ID_COLUMN = "apartment_id";
    static final String APARTMENT_NUM_COLUMN = "apartment_num";
    static final String APARTMENT_PAYMENT_COLUMN = "apartment_payment";

    static final String APARTMENT_ADDRESS_COLUMN = "apartment_address";
    static final String APARTMENT_SHORT_CUT_COLUMN = "apartment_short_cut";


    static final String BASE_APARTMENT_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TABLE_BASE_APARTMENT + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  APARTMENT_ID_COLUMN + " INTEGER, "
            +  APARTMENT_NUM_COLUMN + " INTEGER, "
            + APARTMENT_PAYMENT_COLUMN + " INTEGER, " + APARTMENT_ADDRESS_COLUMN+ " STRING, "
            + APARTMENT_SHORT_CUT_COLUMN  + " STRING);";

    private static final String SELECTION_ID_APARTMENT = APARTMENT_ID_COLUMN  + " = ?";

    private DBQueryManager_apartment queryManager_apartment;

    private DBUpdateManager_apartment updateManager_apartment;

    public final  void saveApartmentInBase(ModelApartment task) {
        ContentValues newValues = new ContentValues();

        newValues.put(APARTMENT_ID_COLUMN, task.getApartmentId());
        newValues.put(APARTMENT_NUM_COLUMN, task.getApartmentNum());

        newValues.put(APARTMENT_ADDRESS_COLUMN, task.getAddress());
        newValues.put(APARTMENT_PAYMENT_COLUMN, task.getPayment());

        newValues.put(APARTMENT_SHORT_CUT_COLUMN, task.getShortCut());

        getWritableDatabase().insert(TABLE_BASE_APARTMENT, null, newValues);

    }

    public final DBQueryManager_apartment queryApartment() {

        return queryManager_apartment;

    }

    public final DBUpdateManager_apartment updateApartment() {

        return updateManager_apartment;
    }

    public final void removeApartmentInBase(long timeStamp) {

        getWritableDatabase().delete(TABLE_BASE_APARTMENT, SELECTION_ID_APARTMENT, new String[]{Long.toString(timeStamp)});

    }
}

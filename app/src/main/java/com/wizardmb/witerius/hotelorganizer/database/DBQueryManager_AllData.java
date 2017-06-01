package com.wizardmb.witerius.hotelorganizer.database;

/**
 * Created by User on 24.03.2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;

import java.util.ArrayList;
import java.util.List;

public final class DBQueryManager_AllData {

    private SQLiteDatabase database;


    DBQueryManager_AllData(SQLiteDatabase database) {
        this.database = database;
        Cursor c;
        c = database.query(DBHelperA.TABLE_BASE_ALL_DATA, null, null, null, null, null, null);
        logCursor(c);
        c.close();

    }

    // вывод в лог данных из курсора
    private void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }

                } while (c.moveToNext());
            }
        }
    }

    public final List<ModelAllData> getAllDataFiltred(int startColumn, int endColumn, int applicantsA) {
        List<ModelAllData> listModelItems = new ArrayList<>();
        String startC = String.valueOf(startColumn);
        String endC = String.valueOf(endColumn);
        String applicantsC = String.valueOf(applicantsA);

        String sqlQuery = "SELECT * FROM base_all_data_table WHERE data_applicants = ? AND data_date_start_int BETWEEN ? AND ? OR data_date_end_int >= ? AND data_date_end_int <= ? AND data_applicants = ?"; // OR data_date_end_int

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {applicantsC, startC, endC,  startC, endC, applicantsC});
        if (cursor.moveToFirst()) {
            do {
                int apartmentId = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_APARTMENT_ID_COLUMN));

                int dolg = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_DOLG_COLUMN));
                int oplacheno = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_OPLACHENO_COLUMN));

                long dateEnd = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_END_COLUMN));
                long dateStart = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_START_COLUMN));
                int dateEndInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_END_INT_COLUMN));

                int dateStartInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_START_INT_COLUMN));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String notice = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NOTICE_COLUMN));
                String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_CLIENT_COLUMN));
                String mobil = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_MOBIL_CLIENT_COLUMN));
                int applicants = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_IS_APPLICANTS_COLUMN));

                ModelAllData modelAllData = new ModelAllData( timeStamp, apartmentId,  dateStart,
                        dateEnd, dolg, oplacheno, notice, dateStartInt, dateEndInt, name, mobil, applicants);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }
    public final List<ModelAllData> getAllDataFiltredOccupied(int startColumn) {
        List<ModelAllData> listModelItems = new ArrayList<>();
        String startC = String.valueOf(startColumn);

        String sqlQuery = "SELECT * FROM base_all_data_table WHERE data_date_start_int <= ? AND data_date_end_int >= ?";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {startC, startC});
        if (cursor.moveToFirst()) {
            do {
                int apartmentId = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_APARTMENT_ID_COLUMN));

                int dolg = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_DOLG_COLUMN));
                int oplacheno = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_OPLACHENO_COLUMN));

                long dateEnd = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_END_COLUMN));
                long dateStart = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_START_COLUMN));
                int dateEndInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_END_INT_COLUMN));

                int dateStartInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_START_INT_COLUMN));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String notice = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NOTICE_COLUMN));
                String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_CLIENT_COLUMN));
                String mobil = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_MOBIL_CLIENT_COLUMN));
                int applicants = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_IS_APPLICANTS_COLUMN));
                ModelAllData modelAllData = new ModelAllData( timeStamp, apartmentId,  dateStart,
                        dateEnd, dolg, oplacheno, notice, dateStartInt, dateEndInt, name, mobil, applicants);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }

    public final List<ModelAllData> getAllDataFiltredEnd(int startColumn, int endColumn) {
        List<ModelAllData> listModelItems = new ArrayList<>();
        String startC = String.valueOf(startColumn);
        String endC = String.valueOf(endColumn);
        String sqlQuery = "SELECT * FROM base_all_data_table WHERE data_date_end_int BETWEEN ? AND ?";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {startC, endC});
        if (cursor.moveToFirst()) {
            do {
                int apartmentId = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_APARTMENT_ID_COLUMN));

                int dolg = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_DOLG_COLUMN));
                int oplacheno = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_OPLACHENO_COLUMN));

                long dateEnd = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_END_COLUMN));
                long dateStart = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_START_COLUMN));
                int dateEndInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_END_INT_COLUMN));
                int dateStartInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_START_INT_COLUMN));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String notice = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NOTICE_COLUMN));
                String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_CLIENT_COLUMN));
                String mobil = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_MOBIL_CLIENT_COLUMN));
                int applicants = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_IS_APPLICANTS_COLUMN));
                ModelAllData modelAllData = new ModelAllData(timeStamp, apartmentId,  dateStart,
                        dateEnd, dolg, oplacheno, notice, dateStartInt, dateEndInt, name, mobil, applicants);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }

    public final List<ModelAllData> getAllDataAssociated(int numApartmentId) {
        List<ModelAllData> listModelItems = new ArrayList<>();
        String apartmentIdIs = String.valueOf(numApartmentId);
        String sqlQuery = "SELECT * FROM base_all_data_table WHERE data_apartment_id LIKE ? ";

        Cursor cursor = database.rawQuery(sqlQuery, new String[] {apartmentIdIs});
        if (cursor.moveToFirst()) {
            do {
                int apartmentId = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_APARTMENT_ID_COLUMN));

                int dolg = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_DOLG_COLUMN));
                int oplacheno = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_OPLACHENO_COLUMN));

                long dateEnd = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_END_COLUMN));
                long dateStart = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_START_COLUMN));
                int dateEndInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_END_INT_COLUMN));
                int dateStartInt = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_START_INT_COLUMN));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelperA.DATA_TIME_STAMP_COLUMN));
                String notice = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NOTICE_COLUMN));
                String name = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_NAME_CLIENT_COLUMN));
                String mobil = cursor.getString(cursor.getColumnIndex(DBHelperA.DATA_MOBIL_CLIENT_COLUMN));
                int applicants = cursor.getInt(cursor.getColumnIndex(DBHelperA.DATA_IS_APPLICANTS_COLUMN));
                ModelAllData modelAllData = new ModelAllData( timeStamp, apartmentId,  dateStart,
                        dateEnd, dolg, oplacheno, notice, dateStartInt, dateEndInt, name, mobil, applicants);

                listModelItems.add(modelAllData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelItems;
    }



}
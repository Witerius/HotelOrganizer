package com.wizardmb.witerius.hotelorganizer.database;

/**
 * Created by User on 24.03.2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.util.ArrayList;
import java.util.List;

public final class DBQueryManager_apartment {

    private SQLiteDatabase database;


    DBQueryManager_apartment(SQLiteDatabase database) {
        this.database = database;

        Cursor c;
        c = database.query(DBHelperA.TABLE_BASE_APARTMENT, null, null, null, null, null, null);
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
    public final void deleteAllApartment()
    {

        database.execSQL("DROP TABLE " + DBHelperA.TABLE_BASE_APARTMENT);
        database.execSQL(DBHelperA.BASE_APARTMENT_TABLE_CREATE_SCRIPT);
    }


    public final List<ModelApartment> getAllApartment(String selection, String[] selectionArgs, String orderBy) {
        List<ModelApartment> listModelApartment = new ArrayList<>();

        Cursor cursor = database.query(DBHelperA.TABLE_BASE_APARTMENT, null, selection, selectionArgs, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                int apartmentId = cursor.getInt(cursor.getColumnIndex(DBHelperA.APARTMENT_ID_COLUMN));
                int payment = cursor.getInt(cursor.getColumnIndex(DBHelperA.APARTMENT_PAYMENT_COLUMN));
                String address = cursor.getString(cursor.getColumnIndex(DBHelperA.APARTMENT_ADDRESS_COLUMN));
                String shortcut = cursor.getString(cursor.getColumnIndex(DBHelperA.APARTMENT_SHORT_CUT_COLUMN));
                int num = cursor.getInt(cursor.getColumnIndex(DBHelperA.APARTMENT_NUM_COLUMN));
                ModelApartment modelApartment = new ModelApartment(apartmentId, payment, address, shortcut, num);
                listModelApartment.add(modelApartment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listModelApartment;
    }


}
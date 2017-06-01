package com.wizardmb.witerius.hotelorganizer.database;

/**
 * Created by User on 24.03.2016.
 */

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

public final class DBUpdateManager_apartment {

    private SQLiteDatabase database;

    DBUpdateManager_apartment(SQLiteDatabase database) {
        this.database = database;

    }

    private void payment(int idApartment, int payment) {

        update(DBHelperA.APARTMENT_PAYMENT_COLUMN, idApartment, payment);
    }
    private void num(int idApartment, int num) {

        update(DBHelperA.APARTMENT_NUM_COLUMN, idApartment, num);
    }

    private void shortcut(int idApartment, String shortcut) {

        update(DBHelperA.APARTMENT_SHORT_CUT_COLUMN, idApartment, shortcut);
    }

    private void address(int idApartment, String address)
    {

        update(DBHelperA.APARTMENT_ADDRESS_COLUMN, idApartment, address);
    }


    public final void apartmentMethod(ModelApartment modelApartment) {

        payment(modelApartment.getApartmentId(), modelApartment.getPayment());
        num(modelApartment.getApartmentId(), modelApartment.getApartmentNum());
        shortcut(modelApartment.getApartmentId(), modelApartment.getShortCut());

        address(modelApartment.getApartmentId(), modelApartment.getAddress());

    }


    private void update(String column, int key, String value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_APARTMENT, cv, DBHelperA.APARTMENT_ID_COLUMN+ " = " + key, null);
    }

    private void update (String column, int key, int value) {

        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelperA.TABLE_BASE_APARTMENT, cv, DBHelperA.APARTMENT_ID_COLUMN+ " = " + key, null);
    }

}
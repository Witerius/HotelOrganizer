package com.wizardmb.witerius.hotelorganizer.model;

import com.wizardmb.witerius.hotelorganizer.MainActivity;

/**
 * Created by Witerius on 02.10.2016.
 */

public final class ModelApartment
{
    private int apartmentId;
    private int apartmentNum;
    private int payment;
    private String address;
    private String shortCut;

    public ModelApartment()
    {
        MainActivity.saveIndexApartment_s++;

        this.apartmentId = MainActivity.saveIndexApartment_s;

    }
    public ModelApartment(int apartmentId, int payment, String address, String shortCut, int apartmentNum)
    {
        this.apartmentId = apartmentId;
        this.apartmentNum = apartmentNum;
        this.payment = payment;
        this.address = address;
        this.shortCut = shortCut;

    }

    public final int getApartmentNum() {
        return apartmentNum;
    }

    public final  void setApartmentNum(int apartmentNum) {
        this.apartmentNum = apartmentNum;
    }

    public final String getShortCut() {
        return shortCut;
    }

    public final void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public final String getAddress() {
        return address;
    }

    public final void setAddress(String address) {
        this.address = address;
    }

    public final int getPayment() {
        return payment;
    }

    public final void setPayment(int payment) {
        this.payment = payment;
    }
   /* public final void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }*/

    public final int getApartmentId() {
        return apartmentId;
    }


}

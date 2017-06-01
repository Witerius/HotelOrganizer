package com.wizardmb.witerius.hotelorganizer.model;

/**
 * Created by Witerius on 10.10.2016.
 */

public final class ModelContact  {

    private String _name = "";
    private String _phone = "";
    private int _my_id;
    public ModelContact() {}

    public final String get_name() {
        return _name;
    }

    public final String get_phone() {
        return _phone;
    }

    public final void setName(String name) {
        _name = name;
    }

    public final  void setPhone(String phone) {
        _phone = phone;
    }

    public final void setMyId(int my_id) {
        _my_id = my_id;
    }

}
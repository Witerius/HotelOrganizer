package com.wizardmb.witerius.hotelorganizer.adapter;

/**
 * Created by User on 20.03.2016.
 */

import com.wizardmb.witerius.hotelorganizer.fragment.MainClientFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.util.List;


public final class ClientAdapter
{

    public static List<ModelContact> client_s;

    private MainClientFragment mainClientFragment;

    public ClientAdapter(MainClientFragment mainClientFragment) {

        this.mainClientFragment = mainClientFragment;

    }

    public final ModelContact getItem(int position) {

        return client_s.get(position);
    }

    public final void addItem(ModelContact item) {

        try {
            client_s.add(item);

        } catch (Exception e) {
         e.printStackTrace();
        }

    }

    public final void addItem(int location, ModelContact item) {

        client_s.add(location, item);


    }

    public final int getItemCount() {

        return client_s.size();
    }


}
package com.wizardmb.witerius.hotelorganizer.adapter;

/**
 * Created by User on 20.03.2016.
 */

import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;

import java.util.HashMap;
import java.util.Map;

public final class CellAdapter {

    private Map<Integer, ModelAllData> hmCVIdMoAD;

    public CellAdapter() {

        hmCVIdMoAD = new HashMap<>();

    }

    public final ModelAllData getItemHM(int position) {
        return hmCVIdMoAD.get(position);
    }

    public final void addItemHM(int key, ModelAllData item) {

        hmCVIdMoAD.put(key, item);

    }

    public final void removeItem(int key) {

        hmCVIdMoAD.remove(key);

    }

}
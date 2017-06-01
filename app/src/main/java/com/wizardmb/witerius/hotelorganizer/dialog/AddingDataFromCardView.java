package com.wizardmb.witerius.hotelorganizer.dialog;

import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

/**
 * Created by Witerius on 27.10.2016.
 */

public interface AddingDataFromCardView
{
    void startDialog(int numberOfDay, int numberOfRow);
    void startDialog(ModelContact modelContact);
}

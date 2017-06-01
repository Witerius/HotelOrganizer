package com.wizardmb.witerius.hotelorganizer;

import java.text.SimpleDateFormat;

/**
 * Created by User on 20.03.2016.
 */
public final class Utils {

    public static String getWeekDate(long date) {
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE dd.MM.yy");
        return fullDateFormat.format(date);
    }
}

package com.wizardmb.witerius.hotelorganizer.dialog;

/**
 * Created by User on 15.06.2016.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.mSettings;

public final class ADialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if(MainActivity.saveNumHelp_s == 0)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help1)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();

                        }
                    });
        }
        else if(MainActivity.saveNumHelp_s == 1)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help2)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
        }
        else if(MainActivity.saveNumHelp_s == 2)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help3)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });
        }

        MainActivity.saveNumHelp_s++;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(MainActivity.APP_PREFERENCES_SAVE_NUM_GUIDE, MainActivity.saveNumHelp_s);
        editor.apply();
        return builder.create();
    }


}
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

public final class BDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        if(MainActivity.isFirstStart_s)
        {   MainActivity.isFirstStart_s = false;
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(MainActivity.APP_PREFERENCES_SAVE_FIRST_START1, MainActivity.isFirstStart_s);
            editor.apply();
        }

        changeText(builder);

        return builder.create();
    }

    private void backNext(boolean backOrNext)
    {
        if(backOrNext)
        {
            MainActivity.saveNumHelp_s--;
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(MainActivity.APP_PREFERENCES_SAVE_NUM_GUIDE, MainActivity.saveNumHelp_s);
            editor.apply();
        }
        else
        {
            MainActivity.saveNumHelp_s++;
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(MainActivity.APP_PREFERENCES_SAVE_NUM_GUIDE, MainActivity.saveNumHelp_s);
            editor.apply();
        }
    }
    private void changeText(final android.app.AlertDialog.Builder builder)
    {

        if(MainActivity.saveNumHelp_s == 0)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help1)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
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
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
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
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        else if(MainActivity.saveNumHelp_s == 3)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help4)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        if(MainActivity.saveNumHelp_s == 4)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help5)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        else if(MainActivity.saveNumHelp_s == 5)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help6)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        else if(MainActivity.saveNumHelp_s == 6)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help7)
                    .setIcon(R.drawable.ic_action_help)
                    .setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            backNext(false);

                            MainActivity.startBDialog();
                        }
                    }).setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    backNext(true);

                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }
        else if(MainActivity.saveNumHelp_s == 7)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help8)
                    .setIcon(R.drawable.ic_action_help)
                    ./*setPositiveButton(R.string.dialog_next, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            backNext(false);
                            MainActivity.startBDialog();
                        }
                    }).*/setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    backNext(true);
                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        } /* else if(MainActivity.saveNumHelp_s == 8)
        {
            builder.setTitle(R.string.guide)
                    .setMessage(R.string.help9)
                    .setIcon(R.drawable.ic_action_help)
                    .setNegativeButton(R.string.dialog_back, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                     backNext(true);
                    MainActivity.startBDialog();
                }
            }).setNeutralButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();
                }
            });
        }*/
    }

}
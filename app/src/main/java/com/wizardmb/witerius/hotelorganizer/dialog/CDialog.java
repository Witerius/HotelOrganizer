package com.wizardmb.witerius.hotelorganizer.dialog;

/**
 * Created by User on 15.06.2016.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.wizardmb.witerius.hotelorganizer.R;

public final class CDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.rate_app)
                    .setMessage(R.string.to_rate_it)
                    .setIcon(R.drawable.ic_action_grade)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=com.wizardmb.witerius.hotelorganizer"));
                            startActivity(intent);
                            dialog.cancel();
                        }
                    }).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                        }
                    });

        return builder.create();
    }


}
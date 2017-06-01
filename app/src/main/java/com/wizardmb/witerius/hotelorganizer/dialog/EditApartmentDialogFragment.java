package com.wizardmb.witerius.hotelorganizer.dialog;

/**
 * Created by User on 29.03.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.util.Calendar;

public final class EditApartmentDialogFragment extends DialogFragment {

    public static EditApartmentDialogFragment newInstance(ModelApartment modelApartment) {
        EditApartmentDialogFragment editApartmentDialogFragment = new EditApartmentDialogFragment();

        Bundle args = new Bundle();
        args.putString("shortcut", modelApartment.getShortCut());
        args.putString("address", modelApartment.getAddress());

        args.putInt("id_apartment", modelApartment.getApartmentId());
        args.putInt("num_apartment", modelApartment.getApartmentNum());
        args.putInt("payment", modelApartment.getPayment());

        editApartmentDialogFragment.setArguments(args);
        return editApartmentDialogFragment;
    }

    private EditingApartmentListener editingApartmentLstener;
    /* Checks if external storage is available for read and write */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editingApartmentLstener = (EditingApartmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingApartmentListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        int payment = args.getInt("payment");
        String shortcut = args.getString("shortcut");
        String address = args.getString("address");
        int apartment_id = args.getInt("id_apartment");
        int apartment_num = args.getInt("num_apartment");

        final ModelApartment modelApartment = new ModelApartment(apartment_id, payment, address, shortcut, apartment_num);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_editing_apartment_edit);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_apartment, null);


        final TextInputLayout tilApartmentShortCut = (TextInputLayout) container.findViewById(R.id.tilDialogApartmentShortcut);
        final EditText etApartmentShortcut = tilApartmentShortCut.getEditText();

        final TextInputLayout tilApartmentAddress = (TextInputLayout) container.findViewById(R.id.tilDialogApartmentAddress);
        final EditText etAddress = tilApartmentAddress.getEditText();

        final TextInputLayout tilApartmentPayment = (TextInputLayout) container.findViewById(R.id.tilDialogApartmentPayment);
        final EditText etPayment = tilApartmentPayment.getEditText();
        tilApartmentShortCut.setHint(getResources().getString(R.string.shortcut));
        tilApartmentAddress.setHint(getResources().getString(R.string.address));
        tilApartmentPayment.setHint(getResources().getString(R.string.payment));
        builder.setView(container);

        try {
            etApartmentShortcut.setText(modelApartment.getShortCut());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            etAddress.setText(modelApartment.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            etPayment.setText(String.valueOf(modelApartment.getPayment()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    modelApartment.setShortCut(etApartmentShortcut.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelApartment.setAddress(etAddress.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelApartment.setPayment(Integer.parseInt(etPayment.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelApartment.setAddress(etAddress.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editingApartmentLstener.onApartmentEdited(modelApartment);

                dialog.dismiss();
            }
        });


        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etApartmentShortcut.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilApartmentShortCut.setError(getResources().getString(R.string.dialog_error_empty_shortcut));
                }

                etApartmentShortcut.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilApartmentShortCut.setError(getResources().getString(R.string.dialog_error_empty_shortcut));
                        } else {
                            positiveButton.setEnabled(true);
                            tilApartmentShortCut.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (etAddress.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilApartmentAddress.setError(getResources().getString(R.string.dialog_error_empty_address));
                }

                etAddress.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilApartmentAddress.setError(getResources().getString(R.string.dialog_error_empty_address));
                        } else {
                            positiveButton.setEnabled(true);
                            tilApartmentAddress.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        });

        return alertDialog;
    }

}
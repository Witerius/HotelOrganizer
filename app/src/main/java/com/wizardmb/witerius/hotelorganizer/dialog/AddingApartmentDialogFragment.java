package com.wizardmb.witerius.hotelorganizer.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.util.Calendar;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewApartmentFromDialod;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.mSettings;

public final class AddingApartmentDialogFragment extends DialogFragment {

    private AddingApartmentListener addingApartmentListener;

    public interface AddingApartmentListener {
        void onApartmentAdded(ModelApartment newTask);

        void onApartmentAddingCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addingApartmentListener = (AddingApartmentListener) activity;
            activity = getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingApartmentListener");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();

        if(isNewApartmentFromDialod) {
            isNewApartmentFromDialod = false;

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_apartment_add);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_apartment, null);


//эдиты для редактирования

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

        final ModelApartment modelApartment = new ModelApartment();

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
                    modelApartment.setPayment(Integer.valueOf(etPayment.getText().toString()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelApartment.setApartmentNum( MainActivity.apartmentAllRow + 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(isNewApartmentFromDialod)
                {
                    if( MainActivity.isDialogAddingOrEditing2)
                    {
                        AddingDateDialogFragment.addApartmentInDialodAdding(modelApartment);
                    }
                    else
                    {
                        EditDateDialogFragment.addApartmentInDialodEdit(modelApartment);
                    }
                }
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(MainActivity.APP_PREFERENCES_SAVE_INDEX_APARTMENT, MainActivity.saveIndexApartment_s);
                editor.apply();
                addingApartmentListener.onApartmentAdded(modelApartment);
                dialog.dismiss();
            }
        });


        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingApartmentListener.onApartmentAddingCancel();
                MainActivity.saveIndexApartment_s--;

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
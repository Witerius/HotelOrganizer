package com.wizardmb.witerius.hotelorganizer.dialog;

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

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewClientFromDialod;

public final class AddingClientDialogFragment extends DialogFragment
{

    private AddingClientListener addingClientListener;

    MainActivity activity1;

    public interface AddingClientListener {
        void onClientAdded(ModelContact newTask);

        void onClientAddingCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addingClientListener = (AddingClientListener) activity;
            activity = getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingClientListener");
        }
        try {
            activity1 = (MainActivity) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(isNewClientFromDialod) {
            isNewClientFromDialod = false;

        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_client_add);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_client, null);

//эдиты для редактирования

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.tilDialogClientName);
        final EditText etName = tilName.getEditText();

        final TextInputLayout tilMobil = (TextInputLayout) container.findViewById(R.id.tilDialogClientMobil);
        final EditText etMobil = tilMobil.getEditText();

        tilName.setHint(getResources().getString(R.string.first_name));
        tilMobil.setHint(getResources().getString(R.string.mobil));

        builder.setView(container);

        final ModelContact modelClient = new ModelContact();

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    modelClient.setName(etName.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelClient.setPhone(etMobil.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(isNewClientFromDialod)
                {
                    if( MainActivity.isDialogAddingOrEditing)
                    {
                        AddingDateDialogFragment.addContactInDialodAdding(modelClient);
                    }
                    else
                    {
                        EditDateDialogFragment.addContactInDialodEdit(modelClient);
                    }

                }
                addingClientListener.onClientAdded(modelClient);
                dialog.dismiss();
            }
        });


        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addingClientListener.onClientAddingCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                if (etMobil.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilMobil.setError(getResources().getString(R.string.dialog_error_empty_mobil));
                }

                etMobil.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilMobil.setError(getResources().getString(R.string.dialog_error_empty_mobil));
                        } else {
                            positiveButton.setEnabled(true);
                            tilMobil.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                if (etName.length() == 0) {
                    positiveButton.setEnabled(false);
                    tilName.setError(getResources().getString(R.string.dialog_error_empty_name));
                }

                etName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 0) {
                            positiveButton.setEnabled(false);
                            tilName.setError(getResources().getString(R.string.dialog_error_empty_name));
                        } else {
                            positiveButton.setEnabled(true);
                            tilName.setErrorEnabled(false);
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
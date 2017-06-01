package com.wizardmb.witerius.hotelorganizer.dialog;

/**
 * Created by User on 29.03.2016.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.Utils;
import com.wizardmb.witerius.hotelorganizer.adapter.ApartmentAdapter;
import com.wizardmb.witerius.hotelorganizer.fragment.CellSFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;


public final class InfoFragment extends DialogFragment {
    public final int  PERMISSION_REQUEST_CONTACT = 377;
    private String mobilForCall;
    private RadioGroup  radioGroup;
    public static InfoFragment newInstance(ModelAllData modelAllData) {
        InfoFragment editDialogFragment = new InfoFragment();

        Bundle args = new Bundle();
        args.putLong("timeStamp", modelAllData.getTimeStamp());
        args.putLong("date_start", modelAllData.getDate_start());
        args.putInt("date_startInt", modelAllData.getDate_startInt());

        args.putInt("apartmentID", modelAllData.getApartmentID());
        args.putLong("date_end", modelAllData.getDate_end());
        args.putInt("date_endInt", modelAllData.getDate_endInt());

        args.putString("notice", modelAllData.getNotice());
        args.putString("name", modelAllData.getName());
        args.putString("mobil", modelAllData.getMobil());
        args.putInt("oplacheno", modelAllData.getOplacheno());
        args.putInt("applicants", modelAllData.getIsApplicants());

        args.putInt("dolg", modelAllData.getDolg());

        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        long timeStamp = args.getLong("timeStamp");
        long startD = args.getLong("date_start");
        int startDint = args.getInt("date_startInt");

        int apartmentId = args.getInt("apartmentID");
        long endD = args.getLong("date_end");
        int endDint = args.getInt("date_endInt");
        String notice = args.getString("notice");
        String name = args.getString("name");
        final String mobil = args.getString("mobil");
        int oplacheno  = args.getInt("oplacheno");
        int dolg = args.getInt("dolg");
        int applicants = args.getInt("applicants");

        final ModelAllData modelAllData = new ModelAllData( timeStamp, apartmentId, startD,
        endD, dolg, oplacheno, notice, startDint, endDint, name, mobil, applicants);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.date_info);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.info_data, null);

        TextView tvClient = (TextView) container.findViewById(R.id.tvClient);

        radioGroup = (RadioGroup) container.findViewById(R.id.tvClientMobil);

        TextView tvApartmentShortCut = (TextView) container.findViewById(R.id.tvApartmentShortCut);

        TextView tvDataNotice = (TextView) container.findViewById(R.id.tvNotice);
        TextView tvDataNotice1 = (TextView) container.findViewById(R.id.tvNotice1);

        TextView tvDateStart = (TextView) container.findViewById(R.id.tvDateStart);
        TextView tvDateEnd = (TextView) container.findViewById(R.id.tvDateEnd);

        TextView tvDataOplacheno = (TextView) container.findViewById(R.id.tvDataOplacheno);
        TextView tvDataOplacheno1 = (TextView) container.findViewById(R.id.tvDataOplacheno1);
        TextView tvDateDolg = (TextView) container.findViewById(R.id.tvDateDolg);
        TextView tvDateDolg1 = (TextView) container.findViewById(R.id.tvDateDolg1);

        ImageView call = (ImageView) container.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mobilForCall ));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvClient.setHint(getResources().getString(R.string.client));
        tvDateStart.setHint(getResources().getString(R.string.start_date));
        tvDateEnd.setHint(getResources().getString(R.string.end_date));

        tvApartmentShortCut.setHint(getResources().getString(R.string.shortcut));
        try {
            if(notice.equals(null))
            {
                tvDataNotice.setVisibility(View.INVISIBLE);
                tvDataNotice1.setVisibility(View.INVISIBLE);
            }
            else
            {
                tvDataNotice.setVisibility(View.VISIBLE);
                tvDataNotice1.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvDataNotice.setVisibility(View.INVISIBLE);
            tvDataNotice1.setVisibility(View.INVISIBLE);
        }
        if(oplacheno  == 0)
        {
            tvDataOplacheno.setVisibility(View.INVISIBLE);
            tvDataOplacheno1.setVisibility(View.INVISIBLE);
        }
        else
        {
            tvDataOplacheno.setVisibility(View.VISIBLE);
            tvDataOplacheno1.setVisibility(View.VISIBLE);
        }
        if(dolg == 0)
        {
            tvDateDolg.setVisibility(View.INVISIBLE);
            tvDateDolg1.setVisibility(View.INVISIBLE);
        }
        else
        {
            tvDateDolg.setVisibility(View.VISIBLE);
            tvDateDolg1.setVisibility(View.VISIBLE);
        }

        tvDateStart.setText(Utils.getWeekDate(modelAllData.getDate_start()));
        tvDateEnd.setText(Utils.getWeekDate(modelAllData.getDate_end()));

        try {
            final String[] tempMobil = mobil.split(",");

            for(int i =0; i<tempMobil.length; i++)
            {
                 final RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setText(tempMobil[i]);
                radioButton.setId(i);
                if(i==0)
                {
                    radioButton.setChecked(true);

                    mobilForCall = tempMobil[0];

                }
               radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        radioGroup.getCheckedRadioButtonId();
                        mobilForCall = (String) radioButton.getText();

                        radioGroup.clearCheck();
                        radioButton.setChecked(true);

                    }
                });
                radioGroup.addView(radioButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvClient.setText(modelAllData.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvDateStart.setText(Utils.getWeekDate(modelAllData.getDate_start()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvDateEnd.setText(Utils.getWeekDate(modelAllData.getDate_end()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            tvApartmentShortCut.setText(ApartmentAdapter.itemApartmentModel.get(CellSFragment.rowForEdit).getShortCut());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvDateDolg.setText(String.valueOf(modelAllData.getDolg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tvDataOplacheno.setText(String.valueOf(modelAllData.getOplacheno()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tvDataNotice.setText(modelAllData.getNotice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.setView(container);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                radioGroup = null;
                dialog.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermission(inflater.getContext());
        }

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

    public final void askPermission(Context mContext){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                MainActivity.needReload = true;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext,
                        Manifest.permission.CALL_PHONE)) {

                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);

                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CONTACT);

                        }
                    });
                    builder.show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions((Activity)mContext,
                            new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

        }
    }

}
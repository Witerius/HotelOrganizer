package com.wizardmb.witerius.hotelorganizer.dialog;

/**
 * Created by User on 29.03.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.Utils;
import com.wizardmb.witerius.hotelorganizer.adapter.ApartmentAdapter;
import com.wizardmb.witerius.hotelorganizer.adapter.ClientAdapter;
import com.wizardmb.witerius.hotelorganizer.fragment.CellSFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.util.Calendar;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewApartmentFromDialod;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewClientFromDialod;
import static com.wizardmb.witerius.hotelorganizer.fragment.CellSFragment.isJustEdit;

public final class EditDateDialogFragment extends DialogFragment {
    private ClientFind clientFind;
    static Calendar calendar;
    static EditText etEndDay, etStartDay;
    static int dayIs;
    static long startDayDate, endDayDate;
    static  int startDayNum, endDayNum;

    private LinearLayout linLayoutContacts, linLayoutApartments;
    public static Button btApartments, btContacts;
    boolean isApartmentVisible = false;
    boolean isContactsVisible = false;

    private int[] colors = new int[2];
    private int nomerCveta = 0;

    public static int apartmentsId;
    public static String mobilNum;
    public static String clientName;

    MainActivity activity1;

    public static EditDateDialogFragment newInstance(ModelAllData modelAllData) {
        EditDateDialogFragment editDialogFragment = new EditDateDialogFragment();
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

        args.putInt("dolg", modelAllData.getDolg());
        args.putInt("applicants", modelAllData.getIsApplicants());

        editDialogFragment.setArguments(args);
        return editDialogFragment;
    }

    private EditingDateListener editingDateListener;
    /* Checks if external storage is available for read and write */


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            editingDateListener = (EditingDateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditingDateListener");
        }
        try {
            clientFind = (ClientFind) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingDateListener");
        }
        activity1 = (MainActivity) activity;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        MainActivity.isDialogAddingOrEditing = false;
        long timeStamp = args.getLong("timeStamp");
        long startD = args.getLong("date_start");
        int startDint = args.getInt("date_startInt");

        apartmentsId = args.getInt("apartmentID");
        long endD = args.getLong("date_end");
        int endDint = args.getInt("date_endInt");
        String notice = args.getString("notice");
        clientName = args.getString("name");
        mobilNum = args.getString("mobil");
        int oplacheno  = args.getInt("oplacheno");
        int dolg = args.getInt("dolg");
        final int isApplicants = args.getInt("applicants");

        dayIs = 0;
        final ModelAllData modelAllData = new ModelAllData( timeStamp, apartmentsId, startD,
        endD, dolg, oplacheno, notice, startDint, endDint, clientName, mobilNum, isApplicants);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_editing_data_edit);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_data, null);

        final TextInputLayout tilStartDay = (TextInputLayout) container.findViewById(R.id.tilDialogDateStart);
        etStartDay = tilStartDay.getEditText();

        final TextInputLayout tilEndDay = (TextInputLayout) container.findViewById(R.id.tilDialogDateEnd);
        etEndDay = tilEndDay.getEditText();

        final TextInputLayout tilNotice = (TextInputLayout) container.findViewById(R.id.tilDialogDataNotice);
        final EditText etNotice  = tilNotice.getEditText();

        final TextInputLayout tilOplacheno = (TextInputLayout) container.findViewById(R.id.tilDialogDataOplacheno);
        final EditText etOplacheno =  tilOplacheno.getEditText();

        final TextInputLayout tilDolg = (TextInputLayout) container.findViewById(R.id.tilDialogDateDolg);
        final EditText etDolgi = tilDolg.getEditText();

        Spinner spApplicatns = (Spinner) container.findViewById(R.id.applicants);
        if(isJustEdit)
        {
            spApplicatns.setVisibility(View.INVISIBLE);
        }
       else
        {
            spApplicatns.setVisibility(View.VISIBLE);
        }
        String a = getString(R.string.confirm);
        String b = getString(R.string.applicants);
        final String [] applicantsSP = {a, b};

        tilStartDay.setHint(getResources().getString(R.string.start_date));
        tilEndDay.setHint(getResources().getString(R.string.end_date));

        tilOplacheno.setHint(getResources().getString(R.string.paid));
        tilNotice.setHint(getResources().getString(R.string.notice));
        tilDolg.setHint(getResources().getString(R.string.debt));

        etStartDay.setText(Utils.getWeekDate(modelAllData.getDate_start()));
        etEndDay.setText(Utils.getWeekDate(modelAllData.getDate_end()));

        try {
            if(modelAllData.getDolg()!=0)
            {
                etDolgi.setText(String.valueOf(modelAllData.getDolg()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(modelAllData.getOplacheno()!=0)
            {
                etOplacheno.setText(String.valueOf(modelAllData.getOplacheno()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            etNotice.setText(modelAllData.getNotice());
        } catch (Exception e) {
            e.printStackTrace();
        }


        ImageButton btnClient = (ImageButton) container.findViewById(R.id.btnClient);
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewClientFromDialod = true;
                DialogFragment addingClientDialogFragment = new AddingClientDialogFragment();
                addingClientDialogFragment.show(getFragmentManager(), "AddingClientDialogFragment");

            }
        });

        ImageButton btnApartment = (ImageButton) container.findViewById(R.id.btnApartment);
        btnApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewApartmentFromDialod = true;
                DialogFragment addingApartmentDialogFragment = new AddingApartmentDialogFragment();
                addingApartmentDialogFragment.show(getFragmentManager(), "AddingApartmentDialogFragment");
            }
        });
        builder.setView(container);

        ArrayAdapter<String> selectApplicantsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, applicantsSP);

        spApplicatns.setAdapter(selectApplicantsAdapter);
        if(isApplicants ==1)
        {
            spApplicatns.setSelection(1);

        }
        else if(isApplicants == 0)
        {
            spApplicatns.setSelection(0);
        }

        spApplicatns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1)
                {
                    modelAllData.setApplicants(1);

                }
                else
                {
                    modelAllData.setApplicants(0);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        linLayoutApartments = (LinearLayout) container.findViewById(R.id.linLayoutApartments);
        linLayoutApartments.setVisibility(View.GONE);

        for(int i = 0; i< ApartmentAdapter.itemApartmentModel.size(); i++)
        {
            napolnenieSpiskaApartment(ApartmentAdapter.itemApartmentModel.get(i), inflater);
        }

        linLayoutContacts = (LinearLayout) container.findViewById(R.id.linLayoutContacts);
        linLayoutContacts.setVisibility(View.GONE);

       /* for(int i = 0; i< ClientAdapter.client_s.size(); i++)
        {
            napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
        }*/


        btApartments = (Button) container.findViewById(R.id.btApartments);
        for(int i=0; i<ApartmentAdapter.itemApartmentModel.size(); i++ )
        {
            if(ApartmentAdapter.itemApartmentModel.get(i).getApartmentId() == modelAllData.getApartmentID())
            {
                btApartments.setText(ApartmentAdapter.itemApartmentModel.get(i).getShortCut());
            }
        }

        btApartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isApartmentVisible)
                {
                    isApartmentVisible = false;
                    linLayoutApartments.setVisibility(View.GONE);
                }
                else {

                    isApartmentVisible = true;
                    linLayoutApartments.setVisibility(View.VISIBLE);
                }
            }
        });
        btContacts = (Button) container.findViewById(R.id.btContacts);

        try {
            btContacts.setText(modelAllData.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        btContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isContactsVisible)
                {
                    isContactsVisible = false;
                    linLayoutContacts.setVisibility(View.GONE);
                }
                else {
                    linLayoutContacts.removeAllViews();
                    clientFind.findClient("");
                    for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                    {
                        napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                    }
                    isContactsVisible = true;
                    linLayoutContacts.setVisibility(View.VISIBLE);
                }
            }
        });


        SearchView searchView1 = (SearchView) container.findViewById(R.id.searchView1);
        searchView1.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                linLayoutContacts.removeAllViews();
                clientFind.findClient("");
                for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                {
                    napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                }

                return false;
            }
        });

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!isContactsVisible)
                {
                    isContactsVisible = true;
                    linLayoutContacts.setVisibility(View.VISIBLE);
                }

                if(newText.equals(""))
                {
                    linLayoutContacts.removeAllViews();
                    clientFind.findClient("");
                    for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                    {
                        napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                    }

                }
                else
                {
                    linLayoutContacts.removeAllViews();
                    clientFind.findClient(newText);

                    try {
                        for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                        {
                            napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                return false;
            }
        });

        calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        etStartDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayIs = 1;
                if (etStartDay.length() == 0) {
                    etStartDay.setText(" ");
                }

                DatePickerFragment datePickerFragment = new DatePickerFragment();

                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerFragment");
            }
        });
        etEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayIs = 2;
                if (etEndDay.length() == 0) {
                    etEndDay.setText(" ");
                }

                DatePickerFragment datePickerFragment = new DatePickerFragment();

                datePickerFragment.show(getActivity().getSupportFragmentManager(), "DatePickerFragment");
            }
        });

        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    modelAllData.setName(clientName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelAllData.setMobil(mobilNum);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelAllData.setApartmentID(apartmentsId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(endDayNum !=0) {
                        modelAllData.setDate_end(endDayDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(startDayNum !=0) {
                        modelAllData.setDate_start(startDayDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelAllData.setOplacheno(Integer.parseInt(etOplacheno.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modelAllData.setNotice(etNotice.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelAllData.setDolg(Integer.parseInt(etDolgi.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(startDayNum !=0)
                    {
                        modelAllData.setDate_startInt(startDayNum);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(endDayNum !=0)
                    {
                        modelAllData.setDate_endInt(endDayNum);
                    }


                } catch (Exception e){
                    e.printStackTrace();
                }
                if(!isJustEdit)
                {

                    activity1.linLayout.removeViewAt(CellSFragment.positionOfConfirm);
                    isJustEdit = true;
                }


               editingDateListener.onDataEdited(modelAllData);

                dialog.dismiss();
            }
        });


        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isJustEdit = true;
                linLayoutContacts.removeAllViews();
                clientFind.findClient("");
                for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                {
                    napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                }

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        setNull();
    }

    private static void setDateInStartEnd(int year, int monthOfYear, int dayOfMonth)
    {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(dayIs == 1)
        {
            etStartDay.setText(Utils.getWeekDate(calendar.getTimeInMillis()));
            startDayDate = calendar.getTimeInMillis();

            int yearTemp = calendar.get(Calendar.YEAR);
            int monthTemp = calendar.get(Calendar.MONTH);
            int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);
            String monthStringTemp = "";
            String dayStringTemp = "";
            if(monthTemp<10)
            {
                monthStringTemp = "0"+monthTemp;
            }
            else
            {
                monthStringTemp = ""+monthTemp;
            }
            if(dayTemp<10)
            {
                dayStringTemp = "0"+dayTemp;
            }
            else
            {
                dayStringTemp = ""+dayTemp;
            }
            String dayStartTemp = ""+ yearTemp + monthStringTemp +  dayStringTemp;
            startDayNum = Integer.parseInt(dayStartTemp);

        }
        else if (dayIs == 2)
        {
            etEndDay.setText(Utils.getWeekDate(calendar.getTimeInMillis()));
            endDayDate = calendar.getTimeInMillis();

            int yearTemp = calendar.get(Calendar.YEAR);
            int monthTemp = calendar.get(Calendar.MONTH);
            int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);
            String monthStringTemp = "";
            String dayStringTemp = "";
            if(monthTemp<10)
            {
                monthStringTemp = "0"+monthTemp;
            }
            else
            {
                monthStringTemp = ""+monthTemp;
            }
            if(dayTemp<10)
            {
                dayStringTemp = "0"+dayTemp;
            }
            else
            {
                dayStringTemp = ""+dayTemp;
            }
            String dayEndTemp = ""+ yearTemp + monthStringTemp +  dayStringTemp;
            endDayNum = Integer.parseInt(dayEndTemp);
        }
        dayIs = 0;
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDateInStartEnd(year,  monthOfYear,  dayOfMonth);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            if(dayIs == 1)
            {
                etStartDay.setText(null);
            }
            else if (dayIs == 2)
            {
                etEndDay.setText(null);
            }
            dayIs = 0;
        }


    }
    private void napolnenieSpiskaApartment(ModelApartment modelApartment, LayoutInflater inflater) {

        View item = inflater.inflate(R.layout.model_client, linLayoutApartments, false);
        TextView tv_name_OfStartView = (TextView) item.findViewById(R.id.tvFamilyName);
        TextView tv_payment_OfStartView = (TextView) item.findViewById(R.id.tvMobil);

        item.setOnClickListener(new ApartmentClickListener(modelApartment));
        tv_name_OfStartView.setText(modelApartment.getShortCut());
        tv_payment_OfStartView.setText(String.valueOf(modelApartment.getPayment()));

        colors[0] = getActivity().getResources().getColor(R.color.gray_50);
        colors[1] = getActivity().getResources().getColor(R.color.gray_60);

        item.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

        linLayoutApartments.addView(item);

    }
    private void napolnenieSpiskaContacts(ModelContact modelContacts, LayoutInflater inflater) {

        View item = inflater.inflate(R.layout.model_client, linLayoutContacts, false);
        TextView tv_name_OfStartView = (TextView) item.findViewById(R.id.tvFamilyName);
        TextView tv_payment_OfStartView = (TextView) item.findViewById(R.id.tvMobil);

        item.setOnClickListener(new ContactClickListener(modelContacts));

        tv_name_OfStartView.setText(modelContacts.get_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv_payment_OfStartView.setText(String.valueOf(modelContacts.get_phone()));
        }

        colors[0] = getActivity().getResources().getColor(R.color.gray_50);
        colors[1] = getActivity().getResources().getColor(R.color.gray_60);

        item.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

        linLayoutContacts.addView(item);

    }

    private class ContactClickListener implements View.OnClickListener {

        ModelContact tempModelC;
        public ContactClickListener(ModelContact modelContact)
        {
            tempModelC = modelContact;
        }
        @Override
        public void onClick(View view) {
            isContactsVisible = false;
            linLayoutContacts.setVisibility(View.GONE);

            clientName = tempModelC.get_name();
            mobilNum = tempModelC.get_phone();
            btContacts.setText(tempModelC.get_name());
        }
    }
    private class ApartmentClickListener implements View.OnClickListener {

        ModelApartment tempModelC;
        public ApartmentClickListener(ModelApartment modelApartment)
        {
            tempModelC = modelApartment;
        }
        @Override
        public void onClick(View view) {
            isApartmentVisible = false;
            linLayoutApartments.setVisibility(View.GONE);

            apartmentsId = tempModelC.getApartmentId();
            btApartments.setText(tempModelC.getShortCut());

        }
    }
    public static void addContactInDialodEdit(ModelContact modelContact) {
        btContacts.setText(modelContact.get_name());
        mobilNum = modelContact.get_phone();
        clientName = modelContact.get_name();
    }

    public static void addApartmentInDialodEdit(ModelApartment modelApartment) {
        btApartments.setText(modelApartment.getShortCut());
        apartmentsId = modelApartment.getApartmentId();
    }
    private void setNull()
    {
        btContacts = null;
        mobilNum = null;
        clientName = null;
        btApartments = null;
        apartmentsId = 0;
        etEndDay = null;
        etStartDay = null;
    }
}
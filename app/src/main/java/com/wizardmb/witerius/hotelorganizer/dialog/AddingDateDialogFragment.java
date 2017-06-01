package com.wizardmb.witerius.hotelorganizer.dialog;

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
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.util.Calendar;
import java.util.Date;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewApartmentFromDialod;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.isNewClientFromDialod;


public final class AddingDateDialogFragment extends DialogFragment{

    private AddingDateListener addingDateListener;
    private ClientFind clientFind;
    static Calendar calendar;
    private static EditText etEndDay, etStartDay;
    static int dayIs;
    static long startDayDate, endDayDate;
    static  int startDayNum, endDayNum,numberOfRow;
    private boolean directAdd = false;
    private LinearLayout linLayoutContacts, linLayoutApartments;
    public static Button btApartments, btContacts;
    boolean isApartmentVisible = false;
    boolean isContactsVisible = false;

    private int[] colors = new int[2];
    private int nomerCveta = 0;

    public static int apartmentsId;
    public static String mobilNum;
    public static String clientName;

    public interface AddingDateListener {
        void onDataAdded( ModelAllData newTask);

        void onDataAddingCancel();

    }

    @Override
    public void onAttach(Activity activity ) {
        super.onAttach(activity);
        try {
            addingDateListener = (AddingDateListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingDateListener");
        }

        try {
            clientFind = (ClientFind) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement AddingDateListener");
        }

    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        setNull();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        MainActivity.isDialogAddingOrEditing = true;
        Bundle arg = getArguments();
        try {
            directAdd = arg.getBoolean("isDirect");
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {

            startDayNum = arg.getInt("integerOfDay");
        } catch (Exception e) {
            e.printStackTrace();

        } try {

            numberOfRow = arg.getInt("numberOfRow");
        } catch (Exception e) {
            e.printStackTrace();

        }

        builder.setTitle(R.string.dialog_data_add);

        final LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_task_add_data, null);

            dayIs = 0;
//эдиты для редактирования

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

        String a = getString(R.string.confirm);
        String b = getString(R.string.applicants);
        final String [] applicantsSP = {a, b};

        tilStartDay.setHint(getResources().getString(R.string.start_date));
        tilEndDay.setHint(getResources().getString(R.string.end_date));

        tilOplacheno.setHint(getResources().getString(R.string.paid));
        tilNotice.setHint(getResources().getString(R.string.notice));
        tilDolg.setHint(getResources().getString(R.string.debt));


        ImageButton btnClient = (ImageButton) container.findViewById(R.id.btnClient);

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNewClientFromDialod = true;

                DialogFragment addingClientDialogFragment = new AddingClientDialogFragment();
                addingClientDialogFragment.show(MainActivity.fragmentManager, "AddingClientDialogFragment");

            }
        });

        ImageButton btnApartment = (ImageButton) container.findViewById(R.id.btnApartment);

        btnApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNewApartmentFromDialod = true;
                DialogFragment addingApartmentDialogFragment = new AddingApartmentDialogFragment();
                addingApartmentDialogFragment.show(MainActivity.fragmentManager, "AddingApartmentDialogFragment");
            }
        });
        builder.setView(container);

        final ModelAllData modelAllData = new ModelAllData();
       ArrayAdapter<String> selectApplicantsAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, applicantsSP);

        spApplicatns.setAdapter(selectApplicantsAdapter);

            spApplicatns.setSelection(0);


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
        try {

            mobilNum = ClientAdapter.client_s.get(0).get_phone();
            clientName = ClientAdapter.client_s.get(0).get_name();

        } catch (Exception e) {
            e.printStackTrace();
        }


        linLayoutApartments = (LinearLayout) container.findViewById(R.id.linLayoutApartments);
        linLayoutApartments.setVisibility(View.GONE);

        try {
            for(int i=0; i< ApartmentAdapter.itemApartmentModel.size(); i++)
            {
                napolnenieSpiskaApartment(ApartmentAdapter.itemApartmentModel.get(i), inflater);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        linLayoutContacts = (LinearLayout) container.findViewById(R.id.linLayoutContacts);
        linLayoutContacts.setVisibility(View.GONE);

        /*for(int i = 0; i< ClientAdapter.client_s.size(); i++)
        {
            napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
        }*/


        btApartments = (Button) container.findViewById(R.id.btApartments);
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
                    try {
                        clientFind.findClient("");
                        for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                        {
                            napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isContactsVisible = true;
                    linLayoutContacts.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            if(!directAdd)
            {
                apartmentsId = ApartmentAdapter.itemApartmentModel.get(0).getApartmentId();
            }
            else
            {
                apartmentsId = numberOfRow;
                for(int i=0; i<ApartmentAdapter.itemApartmentModel.size(); i++)
                {
                    if(ApartmentAdapter.itemApartmentModel.get(i).getApartmentId() == numberOfRow )
                    {
                        btApartments.setText(ApartmentAdapter.itemApartmentModel.get(i).getShortCut());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SearchView searchView1 = (SearchView) container.findViewById(R.id.searchView1);
        searchView1.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                linLayoutContacts.removeAllViews();
                try {
                    clientFind.findClient("");
                    for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                    {
                        napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                    try {
                        clientFind.findClient("");
                        for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                        {
                            napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

        calendar.setTime(new Date());

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
        if(directAdd)
        {
            String stOfDay = String.valueOf(startDayNum);
            String [] tempDate = stOfDay.split("(?<=\\G.{4})");
            String [] tempDayMonth = tempDate[1].split("(?<=\\G.{2})");

            calendar.set(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDayMonth[0]),
                    Integer.parseInt(tempDayMonth[1]));

            startDayDate = calendar.getTimeInMillis();

            etStartDay.setText(Utils.getWeekDate(calendar.getTimeInMillis()));

        }

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
                    modelAllData.setDate_end(endDayDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelAllData.setDate_start(startDayDate);
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
                    modelAllData.setDate_startInt(startDayNum);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modelAllData.setDate_endInt(endDayNum);

                } catch (Exception e){
                    e.printStackTrace();
                }
                if (etStartDay.length() == 0) {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    modelAllData.setDate_start(calendar.getTimeInMillis());
                    dayIs = 1;
                    setDateInStartEnd(year,  month,  day);
                    modelAllData.setDate_startInt(startDayNum);
                }

                if (etEndDay.length() == 0) {

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    modelAllData.setDate_end(calendar.getTimeInMillis());

                    dayIs =2;
                    setDateInStartEnd(year,  month,  day);
                    modelAllData.setDate_endInt(endDayNum);
                }


                addingDateListener.onDataAdded(modelAllData);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                linLayoutContacts.removeAllViews();
                try {
                    clientFind.findClient("");
                    for(int i = 0; i< ClientAdapter.client_s.size(); i++)
                    {
                        napolnenieSpiskaContacts(ClientAdapter.client_s.get(i), inflater);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addingDateListener.onDataAddingCancel();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        return alertDialog;
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

    public static void addContactInDialodAdding(ModelContact modelContact) {
        btContacts.setText(modelContact.get_name());
        mobilNum = modelContact.get_phone();
        clientName = modelContact.get_name();
    }

    public static void addApartmentInDialodAdding(ModelApartment modelApartment) {
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
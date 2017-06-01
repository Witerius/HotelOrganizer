package com.wizardmb.witerius.hotelorganizer.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.adapter.ApartmentAdapter;
import com.wizardmb.witerius.hotelorganizer.dialog.ADialog;
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.dbHelperLLs;

/**
 * A simple {@link Fragment} subclass.
 */
//созданна во втором. фрагмент из таба
public final class ApartmentFragment extends MainApartmentFragment  {
    public ApartmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 1) {
            ADialog aDialog = new ADialog();
            aDialog.show(MainActivity.fragmentManager, "Help1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_apartment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvApartment);

        int wrapContent = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams lParams1 = new RelativeLayout.LayoutParams(
                wrapContent, wrapContent);

        lParams1.setMargins(0, 0, 0, 50);
        recyclerView.setLayoutParams(lParams1);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void addApartmentFromDBForMainActivity() {

        adapter = new ApartmentAdapter(this);
        List<ModelApartment> apartment = new ArrayList<>();
        apartment.addAll(dbHelperLLs.queryApartment().getAllApartment(null, null, "apartment_address, apartment_payment, apartment_short_cut"));

        int sizeC = apartment.size();

        for (int i = 0; i < sizeC; i++) {

            addApartment(apartment.get(i), false);

        }
        adapter.getApartmentAllRowSize();

    }
    @Override
    public void addApartment(ModelApartment newApartment, boolean saveToDB) {
        int position = -1;
        int sizeA = adapter.getItemCount();
        for (int i = 0; i < sizeA; i++) {

            ModelApartment task =  adapter.getItem(i);

            Integer str2 = task.getApartmentNum();
            Integer str1 = newApartment.getApartmentNum();
            int res=str1.compareTo(str2);
            if (res < 0) {
                position = i;
                break;
            }

        }
        if (position != -1) {
            adapter.addItem(position, newApartment);

        } else {
            adapter.addItem(newApartment);

        }

        if (saveToDB) {
            dbHelperLLs.saveApartmentInBase(newApartment);
        }


    }

    @Override
    public void removeAllAssociationData(int idApartment)
    {
        List<ModelAllData> apartmentAssociated = new ArrayList<>();
        apartmentAssociated.addAll(dbHelperLLs.queryAllData().getAllDataAssociated(idApartment));
        int sizeTemp = apartmentAssociated.size();
        for(int i =0; i<sizeTemp; i++)
        {
            dbHelperLLs.removeDataInBase(apartmentAssociated.get(i).getTimeStamp());

        }
        activity.startOccudiedATForApartmentWhenRemove();

    }


}
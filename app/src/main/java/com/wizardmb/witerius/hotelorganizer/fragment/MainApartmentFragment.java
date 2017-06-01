package com.wizardmb.witerius.hotelorganizer.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.adapter.ApartmentAdapter;
import com.wizardmb.witerius.hotelorganizer.dialog.EditApartmentDialogFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

/**
 * Created by User on 20.03.2016.
 */
public abstract class MainApartmentFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected ApartmentAdapter adapter;
    MainActivity activity;
    private int tempRemovingIndex = -1;
    private int tempRemovingApartmentId = -1;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();

        }

    }

    public abstract void addApartment(ModelApartment newTask, boolean saveToDB);
    public abstract void addApartmentFromDBForMainActivity();

    public final void updateApartment(ModelApartment task) {
        adapter.updateApartment(task);
    }

    public final void removeApartmentDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_apartment);

        ModelApartment removingList = adapter.getItem(location); // удаление Списка
        tempRemovingIndex = removingList.getApartmentNum(); //индекс строки удаляемого апартамента
        tempRemovingApartmentId = removingList.getApartmentId();


        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                adapter.removeItem(location);


                Toast.makeText(getActivity(), R.string.please_waite, Toast.LENGTH_LONG).show();
                MainActivity.dbHelperLLs.removeApartmentInBase(tempRemovingApartmentId);  // удаление

                int tempSize = adapter.getItemCount();
                for(int i =0; i<tempSize; i++) // снижение индекса строки у тех, кто позже удаляемого
                {
                    if( adapter.getItem(i).getApartmentNum()> tempRemovingIndex)
                    {
                        int apartmentNum = adapter.getItem(i).getApartmentNum() - 1;
                        adapter.getItem(i).setApartmentNum(apartmentNum);

                        MainActivity.dbHelperLLs.updateApartment().apartmentMethod(adapter.getItem(i));

                    }

                }

                removeAllAssociationData(tempRemovingApartmentId);

                tempRemovingApartmentId = -1;
                tempRemovingIndex = -1;

                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        dialogBuilder.show();
    }

    public abstract void removeAllAssociationData(int idApartment);


    public final void removeAllApartmentFrommDB()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_apartment_all);

        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Toast.makeText(getActivity(), R.string.please_waite, Toast.LENGTH_LONG).show();
                MainActivity.dbHelperLLs.queryApartment().deleteAllApartment();
                adapter.removeAllItems();

                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        dialogBuilder.show();
    }

    public final void showApartmentEditDialog(ModelApartment task) {  // показывает диалог редактир
        DialogFragment editingApartmentDialog = EditApartmentDialogFragment.newInstance(task);
        editingApartmentDialog.show(getActivity().getSupportFragmentManager(), "EditApartmentDialogFragment");
    }

}


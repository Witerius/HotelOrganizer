package com.wizardmb.witerius.hotelorganizer.adapter;

/**
 * Created by User on 20.03.2016.
 */

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.fragment.MainApartmentFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.util.ArrayList;
import java.util.List;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.apartmentAllRow;


public final class ApartmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MenuItem myActionEditItemApartment;
    private MenuItem myActionDeleteItemApartment;
    private MenuItem myActionDeleteAllApartment;

    public static List<ModelApartment> itemApartmentModel;

    private MainApartmentFragment mainApartmentFragment;

    private int[] colors = new int[2];
    private int nomerCveta = 0;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_apartment, viewGroup, false);
        TextView shortCut = (TextView) v.findViewById(R.id.tvShortCut);
        TextView payment = (TextView) v.findViewById(R.id.tvPayment);
        TextView address = (TextView) v.findViewById(R.id.tvAddress);

        return new ApartmentViewHolder(v, shortCut, payment, address);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ModelApartment itemApartmentModel = ApartmentAdapter.itemApartmentModel.get(position);
        final Resources resources = viewHolder.itemView.getResources();

        setDataFromBindViewHolder(itemApartmentModel, viewHolder, resources);

    }

    private void setDataFromBindViewHolder(ModelApartment modelApartment,
                                           RecyclerView.ViewHolder viewHolder, final Resources resources) {

        viewHolder.itemView.setEnabled(true);

        final ApartmentViewHolder apartmentViewHolder = (ApartmentViewHolder) viewHolder;

        final View itemView = apartmentViewHolder.itemView;

        apartmentViewHolder.shortCut.setText(modelApartment.getShortCut());
        apartmentViewHolder.address.setText(modelApartment.getAddress());
        apartmentViewHolder.payment.setText(String.valueOf(modelApartment.getPayment()));

        itemView.setVisibility(View.VISIBLE);

        colors[0] = resources.getColor(R.color.gray_50);
        colors[1] = resources.getColor(R.color.gray_60);

        itemView.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

    }


    public ApartmentAdapter(MainApartmentFragment mainApartmentFragment) {

        this.mainApartmentFragment = mainApartmentFragment;

        itemApartmentModel = new ArrayList<>();


    }

    public final ModelApartment getItem(int position) {
        return itemApartmentModel.get(position);
    }

    public final void addItem(ModelApartment item) {

        itemApartmentModel.add(item);

        apartmentAllRow++;
        notifyItemInserted(getItemCount() - 1);
    }

    public final void addItem(int location, ModelApartment item) {

        itemApartmentModel.add(location, item);

        apartmentAllRow++;
        notifyItemInserted(location);
    }

    public final void updateApartment(ModelApartment newModelApartment) {
        for (int i = 0; i < getItemCount(); i++) {

            ModelApartment task =  getItem(i);

            if (newModelApartment.getApartmentId()== task.getApartmentId()) {

                removeItem(i);

                getMainApartmentFragment().addApartment(newModelApartment, false);

            }

        }
    }


    public final void removeItem(int location) {

        if (location >= 0 && location <= getItemCount() -1) {

            itemApartmentModel.remove(location);

            apartmentAllRow = itemApartmentModel.size();
            notifyItemRemoved(location);
        }

    }

    public final void removeAllItems() {

        if (getItemCount() != 0) {

            itemApartmentModel = new ArrayList<>();

            apartmentAllRow = 0;
            notifyDataSetChanged();
        }
    }

    public final void getApartmentAllRowSize()
    {
        apartmentAllRow = getItemCount();
    }
    @Override
    public int getItemCount() {

        return itemApartmentModel.size();
    }

    private class ApartmentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        TextView shortCut;
        TextView payment;
        TextView address;

        ApartmentViewHolder(View itemView, TextView shortCut, TextView payment, TextView address) {
            super(itemView);

            this.shortCut = shortCut;
            this.payment = payment;
            this.address = address;

            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(R.string.select_action);
            myActionEditItemApartment = menu.add(R.string.dialog_editing_apartment);
            myActionDeleteItemApartment = menu.add(R.string.dialog_deleting_apartment);
            myActionDeleteAllApartment = menu.add(R.string.delete_all_apartment);
            myActionEditItemApartment.setOnMenuItemClickListener(this);
            myActionDeleteItemApartment.setOnMenuItemClickListener(this);
            myActionDeleteAllApartment.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem itemClient) {

            if (itemClient == myActionEditItemApartment) {
                ModelApartment modelApartmentValue = getItem(getLayoutPosition());
                getMainApartmentFragment().showApartmentEditDialog(modelApartmentValue);

            } else if (itemClient == myActionDeleteItemApartment) {

                getMainApartmentFragment().removeApartmentDialog(getLayoutPosition());

            }else if (itemClient == myActionDeleteAllApartment) {

                getMainApartmentFragment().removeAllApartmentFrommDB();


            }
            return true;
        }

    }

    private MainApartmentFragment getMainApartmentFragment() {
        return mainApartmentFragment;
    }

}
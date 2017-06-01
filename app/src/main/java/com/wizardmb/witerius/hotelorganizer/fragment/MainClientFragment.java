package com.wizardmb.witerius.hotelorganizer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;

import com.wizardmb.witerius.hotelorganizer.adapter.ClientAdapter;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.util.ArrayList;

/**
 * Created by User on 20.03.2016.
 */
public abstract class MainClientFragment extends Fragment// implements ClientFind
{

    protected ClientAdapter adapter;

    public static final String CONTACT_ID = ContactsContract.Contacts._ID;
    public static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    public static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    public static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    public static final String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public abstract void addClient(ModelContact newTask, boolean saveToDB, Context tempContext);
    public abstract ArrayList<ModelContact> getAllContacts(Context context, String[] selectionArgs);

    public abstract void addClientFromDBForMainActivity();

    public abstract void createListOfContacts(Context tempContext);
    public abstract void findClientA(String client, Context context);


}


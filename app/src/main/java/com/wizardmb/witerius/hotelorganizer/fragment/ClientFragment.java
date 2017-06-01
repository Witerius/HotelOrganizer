package com.wizardmb.witerius.hotelorganizer.fragment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.adapter.ClientAdapter;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.util.ArrayList;
import java.util.HashMap;

import static com.wizardmb.witerius.hotelorganizer.adapter.ClientAdapter.client_s;

/**
 * A simple {@link Fragment} subclass.
 */
//созданна во втором. фрагмент из таба
public final class ClientFragment extends MainClientFragment  {

    public ClientFragment() {
        // Required empty public constructor
    }
    public final int  PERMISSION_REQUEST_CONTACT = 345;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_client, container, false);

        adapter = new ClientAdapter(this);

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void addClientFromDBForMainActivity() {

        adapter = new ClientAdapter(this);

    }
    @Override
    public  void createListOfContacts(Context tempContext)
    {
        client_s = new ArrayList<>();
       // clientString = new ArrayList<>();
        String temp[] = {""};

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermission(tempContext, temp, false);
            }
                else
                {
                    client_s = getAllContacts(tempContext, temp);
                  /*  int sizeA = client_s.size();
                    for (int i = 0; i < sizeA; i++) {

                        clientString.add(client_s.get(i).get_name());

                    }*/
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addClient(ModelContact newClient, boolean saveToDB, Context tempContext) {
        int position = -1;
        int sizeA;
        try {
            sizeA = adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
            sizeA = 0;
        }
        for (int i = 0; i < sizeA; i++) {

            ModelContact task =  adapter.getItem(i);

            String str2 = task.get_name();
            String str1 = newClient.get_name();
            int res = str1.compareTo(str2);
            if (res < 0) {
                position = i;
                break;
            }

        }
        if (position != -1) {
            adapter.addItem(position, newClient);
        } else {
            adapter.addItem(newClient);
        }

        if (saveToDB) {

            addNewContacts(newClient, tempContext);
        }


    }

    private void addNewContacts(ModelContact modelContact, Context tempContext) {

        String accountNameWeWant = "SpecialAccount";
        String phone = modelContact.get_phone();
        String name = modelContact.get_name();
        String accountname = null;
        String accounttype = null;
        if (ActivityCompat.checkSelfPermission(tempContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Account[] accounts = AccountManager.get( tempContext).getAccounts();
// Находим нужную нам учетную запись. Если она не находится,
// используем 'null' — значение, задаваемое по умолчанию.
        for(Account account : accounts) {
            if(account.equals(accountNameWeWant)) {
                accountname = account.name;
                accounttype = account.type;
                break;
            }
        }
        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert
                (ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountname)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accounttype)
                .build());
// создаем новый контакт
        ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference
                                (ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name)
                        .build());
// если нам известен телефонный номер, добавляем его
        if(phone != null
                && phone.trim().length() > 0) {
            ops.add(ContentProviderOperation.newInsert
                    (ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference
                            (ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                            phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }
        try {

            tempContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public final ArrayList<ModelContact> getAllContacts(Context context, String[] selectionArgs) {

        ContentResolver cr = context.getContentResolver();

        Cursor pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{PHONE_NUMBER, PHONE_CONTACT_ID},
              null, null,
                null
        );
        if(pCur != null){
            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();

                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));

                    ArrayList<String> curPhones = new ArrayList<>();

                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);

                    }
                    curPhones.add(pCur.getString(0));

                    phones.put(contactId, curPhones);

                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0",
                        null,
                        DISPLAY_NAME + " ASC");
                if (cur != null) {
                    if (cur.getCount() > 0) {
                        ArrayList<ModelContact> modelContacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));

                            if(phones.containsKey(id)) {
                                ModelContact con = new ModelContact();
                                con.setMyId(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));

                                con.setPhone(TextUtils.join(", ", phones.get(id).toArray()));

                                modelContacts.add(con);
                            }
                        }
                        return modelContacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }

    public final ArrayList<ModelContact> getAllContactsFind(Context context, String[] selectionArgs) {

        ContentResolver cr = context.getContentResolver();

        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + selectionArgs[0] +"%'";
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
               /* ContactsContract.CommonDataKinds.Phone.NUMBER,*/ PHONE_NUMBER, PHONE_CONTACT_ID};
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);

        if(pCur != null){

            if(pCur.getCount() > 0) {
                HashMap<Integer, ArrayList<String>> phones = new HashMap<>();

                while (pCur.moveToNext()) {
                    Integer contactId = pCur.getInt(pCur.getColumnIndex(PHONE_CONTACT_ID));

                    ArrayList<String> curPhones = new ArrayList<>();

                    if (phones.containsKey(contactId)) {
                        curPhones = phones.get(contactId);

                    }
                    curPhones.add(pCur.getString(1));

                    phones.put(contactId, curPhones);

                }
                Cursor cur = cr.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[]{CONTACT_ID, DISPLAY_NAME, HAS_PHONE_NUMBER},
                        HAS_PHONE_NUMBER + " > 0", null,

                        DISPLAY_NAME + " ASC");

                if (cur != null) {
                    if (cur.getCount() > 0) {
                        ArrayList<ModelContact> modelContacts = new ArrayList<>();
                        while (cur.moveToNext()) {
                            int id = cur.getInt(cur.getColumnIndex(CONTACT_ID));

                            if(phones.containsKey(id)) {
                                ModelContact con = new ModelContact();
                                con.setMyId(id);
                                con.setName(cur.getString(cur.getColumnIndex(DISPLAY_NAME)));

                                con.setPhone(TextUtils.join(", ", phones.get(id).toArray()));

                                modelContacts.add(con);
                            }
                        }
                        return modelContacts;
                    }
                    cur.close();
                }
            }
            pCur.close();
        }
        return null;
    }

    public final void askPermission(Context mContext, String[] temp, boolean isSearch){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                MainActivity.needReload = true;
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext,
                        Manifest.permission.READ_CONTACTS)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);

                        }
                    });
                    builder.show();
                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions((Activity)mContext,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                if(!isSearch)
                {
                    client_s =  getAllContacts(mContext, temp);
                }
                else {
                    client_s =  getAllContactsFind(mContext, temp);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted

                } else {
                    //permission denied
                }
                return;
            }
        }
    }

   @Override
    public final void findClientA(String client, Context tempContext)
    {
        client_s = new ArrayList<>();

        String temp[] = {client};
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermission(tempContext, temp, true);
            }
            else
            {
                client_s = getAllContactsFind(tempContext, temp);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
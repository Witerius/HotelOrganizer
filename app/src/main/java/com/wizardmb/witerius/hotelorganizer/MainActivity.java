package com.wizardmb.witerius.hotelorganizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.wizardmb.witerius.hotelorganizer.database.DBHelperA;
import com.wizardmb.witerius.hotelorganizer.dialog.ADialog;
import com.wizardmb.witerius.hotelorganizer.dialog.AddingApartmentDialogFragment;
import com.wizardmb.witerius.hotelorganizer.dialog.AddingClientDialogFragment;
import com.wizardmb.witerius.hotelorganizer.dialog.AddingDataFromCardView;
import com.wizardmb.witerius.hotelorganizer.dialog.AddingDateDialogFragment;
import com.wizardmb.witerius.hotelorganizer.dialog.BDialog;
import com.wizardmb.witerius.hotelorganizer.dialog.CDialog;
import com.wizardmb.witerius.hotelorganizer.dialog.ClientFind;
import com.wizardmb.witerius.hotelorganizer.dialog.EditingApartmentListener;
import com.wizardmb.witerius.hotelorganizer.dialog.EditingDateListener;
import com.wizardmb.witerius.hotelorganizer.fragment.ApartmentFragment;
import com.wizardmb.witerius.hotelorganizer.fragment.CellSFragment;
import com.wizardmb.witerius.hotelorganizer.fragment.ClientFragment;
import com.wizardmb.witerius.hotelorganizer.fragment.MainApartmentFragment;
import com.wizardmb.witerius.hotelorganizer.fragment.MainClientFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;
import com.wizardmb.witerius.hotelorganizer.model.ModelContact;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;


public final class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddingClientDialogFragment.AddingClientListener,
         AddingDateDialogFragment.AddingDateListener, AddingApartmentDialogFragment.AddingApartmentListener,
        EditingApartmentListener, EditingDateListener, AddingDataFromCardView, ClientFind
      {
    public static DBHelperA dbHelperLLs;

    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences  mSettings;

    //number help
    public static final String APP_PREFERENCES_SAVE_NUM_GUIDE = "saveNumOfHelpI";
    private int saveNumHelp = 0;
    public static int saveNumHelp_s;

    //индекс апартаментов
    public static final String APP_PREFERENCES_SAVE_INDEX_APARTMENT = "saveIndexApartment"; //индекс прибавлен на 1
    private int saveIndexApartment = 0;
    public static int saveIndexApartment_s;

    //первый старт
    public static final String APP_PREFERENCES_SAVE_FIRST_START1 = "saveFirstStart1";
    private boolean isFirstStart = true;
    public static boolean isFirstStart_s;

    //тип рекламы
 /*   public static final String APP_PREFERENCES_ADD_MOB = "addMob";
    private boolean isAddMob = true;
    public static boolean isAddMob_s;*/

    public static final String APP_PREFERENCES_SAVE_ID_EMPTY_MOAD = "saveIdEmptyMoad";
    private long idEmptyMoad = 10;
    public  static long idEmptyMoad_s;

    private CellSFragment startFragment;
    public static FragmentManager fragmentManager;
    private MainClientFragment clientFragment;
    private MainApartmentFragment apartmentFragment;

    private FloatingActionButton fab;
    private int nomerTaba = 0;

    public static List<String> dateString;
    public static List<Integer> dateInteger;

    public static int todayDate_s;
    public static int twoDayLast, endOfDay;

    public static int apartmentAllRow = 0;

    public static AddingDataFromCardView addingDataFromCardView;

    private boolean isRestoreAfterChangeMonitore = false;
    private boolean isAddedNewRow = false;

    public static EditingDateListener confirmMoveEdited;
    public LinearLayout linLayout, linFree, linOccupied;

    private boolean linLayoutVisibilyty = false;
    private boolean linOccuredVisibilyty = false;
    private boolean linFreeVisibilyty = false;
    public static boolean needReload = false;

    public static boolean  isNewApartmentFromDialod  = false;
    public static boolean isNewClientFromDialod = false;
    public static boolean isDialogAddingOrEditing = true;
    public static boolean isDialogAddingOrEditing2 = true;
    private int numOfCore;

          @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        if(!isRestoreAfterChangeMonitore)
        {
            reloadActivity();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_back:
                startFragment.backClick();
                return true;
            case R.id.action_next:
                startFragment.nextClick();
                return true;
            case R.id.action_all_data:
                nomerTaba = 0;
                if(needReload)
                {
                    needReload = false;
                    reloadActivity();
                }
                else {
                    FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();

                    ft0.replace(R.id.container, startFragment);
                    ft0.commit();
                }

                return true;

            case R.id.action_apartment:
                nomerTaba = 2;

                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                    ft1.replace(R.id.container, apartmentFragment);
                    ft1.commit();


                return true;

            case R.id.action_rating:
                CDialog cDialog = new CDialog();
                cDialog.show(MainActivity.fragmentManager, "Help2");
                return true;
            case R.id.action_help:
                startBDialog();
                return true;
            case R.id.action_video:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtu.be/z112e3ipSvI"));
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void startBDialog()
    {
        BDialog bDialog = new BDialog();
        bDialog.show(MainActivity.fragmentManager, "Help1");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(nomerTaba == 1 || nomerTaba == 2 )
            {
                nomerTaba = 0;
                fab.setVisibility(View.VISIBLE);

                if(needReload)
                {
                    needReload = false;
                    reloadActivity();
                }
                else {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.container, startFragment);
                    ft.commit();
                }


            }

            else
            {
                super.onBackPressed();

            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_SAVE_FIRST_START1)) {
            isFirstStart= mSettings.getBoolean(APP_PREFERENCES_SAVE_FIRST_START1, true);
            isFirstStart_s = isFirstStart;
        }

      /*  if (mSettings.contains(APP_PREFERENCES_ADD_MOB)) {
            isAddMob= mSettings.getBoolean(APP_PREFERENCES_ADD_MOB, true);
            isAddMob_s = isAddMob;
        }*/

        try {
            if (mSettings.contains(APP_PREFERENCES_SAVE_NUM_GUIDE)) {

                saveNumHelp = mSettings.getInt(APP_PREFERENCES_SAVE_NUM_GUIDE, 0);
                saveNumHelp_s = saveNumHelp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSettings.contains(APP_PREFERENCES_SAVE_INDEX_APARTMENT)) {
            saveIndexApartment = mSettings.getInt(APP_PREFERENCES_SAVE_INDEX_APARTMENT, 0);
            saveIndexApartment_s = saveIndexApartment;

        }
        if(mSettings.contains(APP_PREFERENCES_SAVE_ID_EMPTY_MOAD))
        {
            idEmptyMoad = mSettings.getLong(APP_PREFERENCES_SAVE_ID_EMPTY_MOAD, 10);
            idEmptyMoad_s = idEmptyMoad;
        }

        MyApplication.activityResumed();
    }


    @Override
    protected void onPause() {
        super.onPause();

        MyApplication.activityPaused();
    }

    public final int getNomerTaba() {
        return nomerTaba;
    }

    @Override
    public void findClient(String text) {
        clientFragment.findClientA(text, this);
    }


    final class ClientFragmentAT extends AsyncTask<Void, Void, MainClientFragment>
    {
        @Override
        protected MainClientFragment doInBackground(Void... params) {

            clientFragment = new ClientFragment();
            clientFragment.addClientFromDBForMainActivity();
            clientFragment.createListOfContacts(MainActivity.this);

            return clientFragment;
        }

        @Override
        protected void onPostExecute(MainClientFragment result) {
            super.onPostExecute(result);

        }
    }
    final class ApartmentFragmentAT extends AsyncTask<Void, Void, MainApartmentFragment>
    {
        @Override
        protected MainApartmentFragment doInBackground(Void... params) {

            dateString = new ArrayList<>();
            dateInteger = new ArrayList<>();

            apartmentFragment = new ApartmentFragment();
            apartmentFragment.addApartmentFromDBForMainActivity();
            return apartmentFragment;
        }

        @Override
        protected void onPostExecute(MainApartmentFragment result) {
            super.onPostExecute(result);

        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isRestoreAfterChangeMonitore = true;
        outState.putBoolean("isRestoreAfterChangeMonitore", isRestoreAfterChangeMonitore);

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isRestoreAfterChangeMonitore = savedInstanceState.getBoolean("isRestoreAfterChangeMonitore");

    }

    @Override
    public void startDialog(int numberOfDay, int numberOfRow) {
        DialogFragment addingDateDialogFragment = new AddingDateDialogFragment();

        Bundle isDirect = new Bundle();
        isDirect.putBoolean("isDirect", true);
        isDirect.putInt("integerOfDay", dateInteger.get(numberOfDay-1));
        isDirect.putInt("numberOfRow", numberOfRow);

        addingDateDialogFragment.setArguments(isDirect);

        addingDateDialogFragment.show(fragmentManager, "AddingDateDialogFragment");
    }

    @Override
    public void startDialog(ModelContact modelContact) {
        DialogFragment addingDateDialogFragment = new AddingDateDialogFragment();

        Bundle isDirect = new Bundle();

        isDirect.putString("modelContactName", modelContact.get_name());
        isDirect.putString("modelContactPhone", modelContact.get_phone());

        addingDateDialogFragment.setArguments(isDirect);

        addingDateDialogFragment.show(fragmentManager, "AddingDateDialogFragment");
    }

    @Override
    public void onClientAdded(ModelContact newTask) {

        clientFragment.addClient(newTask, true, this);
    }

    @Override
    public void onClientAddingCancel() {
        Toast.makeText(this, R.string.client_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onApartmentAdded(ModelApartment newTask) {
        apartmentFragment.addApartment(newTask, true);
        if(nomerTaba ==0)
        {
            isAddedNewRow= true;
            startFragment.addApartmnetInHMap();

            fab.setVisibility(View.VISIBLE);
            startFragment.startOccupiedAT();
        }

            if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 2) {
                ADialog aDialog = new ADialog();
                aDialog.show(MainActivity.fragmentManager, "Help1");
            }


    }

    @Override
    public void onApartmentAddingCancel() {
        Toast.makeText(this, R.string.apartment_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onApartmentEdited(ModelApartment updatedTask) {
        apartmentFragment.updateApartment(updatedTask);
        dbHelperLLs.updateApartment().apartmentMethod(updatedTask);

        startFragment.addApartmnetInHMap();
        startFragment.startOccupiedAT();
    }

    @Override
    public void onDataAdded(ModelAllData newTask) {
        if(isAddedNewRow)
        {
            for(int i1=0;i1<15; i1++ )
            {
                startFragment.addNewRowInGridView(apartmentAllRow, i1);
            }
            isAddedNewRow = false;
        }

        if(newTask.getIsApplicants() == 1)
        {
            startFragment.addNewData(newTask, true, true);
        }
        else if (newTask.getIsApplicants() == 0)
        {
            startFragment.addNewData(newTask, true, false);
        }

        startFragment.startOccupiedAT();

    }

    public final void removeAllInApplicantsLinLayout()
    {
        linLayout.removeAllViews();
    }

    @Override
    public void onDataAddingCancel() {
        Toast.makeText(this, R.string.data_adding_cancel, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataEdited(ModelAllData updatedTask) {
        if(updatedTask.getIsApplicants() == 1)
        {
            startFragment.addNewData(updatedTask, false, true);

        }
        else if(updatedTask.getIsApplicants() == 0)
        {
            startFragment.addNewData(updatedTask, false, false);

        }
        dbHelperLLs.updateAllData().itemMethod(updatedTask);
        startFragment.startApplicantsAT();

        startFragment.startOccupiedAT();
    }

    public final void reloadActivity()
    {
        numOfCore = getNumCores();

        try {
            setContentView(R.layout.activity_main0);
            dbHelperLLs = new DBHelperA(getApplicationContext());
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            if (mSettings.contains(APP_PREFERENCES_SAVE_INDEX_APARTMENT)) {
                saveIndexApartment = mSettings.getInt(APP_PREFERENCES_SAVE_INDEX_APARTMENT, 0);
                saveIndexApartment_s = saveIndexApartment;
            }
            else
            {
                saveIndexApartment_s = 0;
            }

            if(numOfCore>2)
            {
                startCreate2Fragment();
            }
            else {
                ClientFragmentAT clientFAT = new ClientFragmentAT();
                clientFAT.execute();

                dateString = new ArrayList<>();
                dateInteger = new ArrayList<>();

                apartmentFragment = new ApartmentFragment();
                apartmentFragment.addApartmentFromDBForMainActivity();

            }

            if (mSettings.contains(APP_PREFERENCES_SAVE_FIRST_START1)) {
                isFirstStart= mSettings.getBoolean(APP_PREFERENCES_SAVE_FIRST_START1, true);
                isFirstStart_s = isFirstStart;
            }
            else
            {
                isFirstStart_s = true;
            }
         /*   if (mSettings.contains(APP_PREFERENCES_ADD_MOB)) {
                isAddMob= mSettings.getBoolean(APP_PREFERENCES_ADD_MOB, true);
                isAddMob_s = isAddMob;
            }
            else
            {
                isAddMob_s = true;
            }*/
            try {
                if (mSettings.contains(APP_PREFERENCES_SAVE_NUM_GUIDE)) {
                    saveNumHelp = mSettings.getInt(APP_PREFERENCES_SAVE_NUM_GUIDE, 0);
                    saveNumHelp_s = saveNumHelp;
                }
                else {
                    saveNumHelp_s = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                saveNumHelp_s = 0;
            }

            if(mSettings.contains(APP_PREFERENCES_SAVE_ID_EMPTY_MOAD))
            {
                idEmptyMoad = mSettings.getLong(APP_PREFERENCES_SAVE_ID_EMPTY_MOAD, 10);
                idEmptyMoad_s = idEmptyMoad;
            }
            else
            {
                idEmptyMoad_s = 10;
            }

            try {
                addingDataFromCardView = MainActivity.this;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                setSupportActionBar(toolbar);

            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setBackgroundColor(getResources().getColor(R.color.gray_20));
            fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setImageResource(R.drawable.ic_add_white_24dp);
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a = getNomerTaba();

                    try {
                        if (a == 0) {

                           DialogFragment addingDateDialogFragment = new AddingDateDialogFragment();

                            addingDateDialogFragment.show(fragmentManager, "AddingDateDialogFragment");
                        }else  if (a == 1) {
                            DialogFragment addingClientDialogFragment = new AddingClientDialogFragment();
                            addingClientDialogFragment.show(fragmentManager, "AddingcClientDialogFragment");
                        } else if (a == 2 ) {
                            DialogFragment addingApartmentDialogFragment = new AddingApartmentDialogFragment();
                            addingApartmentDialogFragment.show(fragmentManager, "AddingApartmentDialogFragment");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

            final LinearLayout.LayoutParams lpVisible = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            final int NOTVISIBLE = 0;
            final LinearLayout.LayoutParams lpInvisible = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    NOTVISIBLE);

            linLayout = (LinearLayout) findViewById(R.id.linLayout);
            linLayout.setLayoutParams(lpInvisible);

            linFree = (LinearLayout) findViewById(R.id.linLayoutFree);
            linFree.setLayoutParams(lpInvisible);

            linOccupied = (LinearLayout) findViewById(R.id.linLayoutOccupied);
            linOccupied.setLayoutParams(lpInvisible);

            Button btnFree = (Button) findViewById(R.id.btFree);
            btnFree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linFreeVisibilyty)
                    {
                        linFreeVisibilyty = false;
                        linFree.setVisibility(View.INVISIBLE);
                        linFree.setLayoutParams(lpInvisible);
                    }
                    else
                    {
                        linFreeVisibilyty = true;
                        linFree.setVisibility(View.VISIBLE);
                        linFree.setLayoutParams(lpVisible);
                    }
                }
            });
            Button btnOccupied = (Button) findViewById(R.id.btOccupied);
            btnOccupied.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linOccuredVisibilyty)
                    {
                        linOccuredVisibilyty = false;
                        linOccupied.setVisibility(View.INVISIBLE);
                        linOccupied.setLayoutParams(lpInvisible);
                    }
                    else
                    {
                        linOccuredVisibilyty = true;
                        linOccupied.setVisibility(View.VISIBLE);
                        linOccupied.setLayoutParams(lpVisible);
                    }
                }
            });
            Button btnLayout = (Button) findViewById(R.id.btLayout);
            btnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(linLayoutVisibilyty)
                    {
                        linLayoutVisibilyty = false;
                        linLayout.setVisibility(View.INVISIBLE);
                        linLayout.setLayoutParams(lpInvisible);
                    }
                    else
                    {
                        linLayoutVisibilyty = true;
                        linLayout.setVisibility(View.VISIBLE);
                        linLayout.setLayoutParams(lpVisible);
                    }
                }
            });

            fragmentManager = getSupportFragmentManager();

            startFragment = new CellSFragment();
            try {
                confirmMoveEdited = this;
            } catch (Exception e) {
                e.printStackTrace();
            }


            // при первом запуске программы
            FragmentTransaction ft = fragmentManager
                    .beginTransaction();
            // добавляем в контейнер при помощи метода add()

            ft.add(R.id.container, startFragment);
            ft.commit();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public final void startCreate2Fragment()
    {
        ApartmentFragmentAT apartmentFAT = new ApartmentFragmentAT();
        apartmentFAT.execute();
        ClientFragmentAT clientFAT = new ClientFragmentAT();
        clientFAT.execute();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if(nomerTaba == 0) {
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();

                ft0.replace(R.id.container, startFragment);
                ft0.commit();

            }
            if(nomerTaba == 1) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.container, clientFragment);
                ft.commit();

            }
            if(nomerTaba == 2) {
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();

                ft1.replace(R.id.container, apartmentFragment);
                ft1.commit();

            }

        } catch (Exception e) {
            e.printStackTrace();
            reloadActivity();
        }
    }

    public final void startOccudiedATForApartmentWhenRemove()
    {
        startFragment.startOccupiedAT();
    }
    public final void clearViewOccupiedAndFree()
    {
        linOccupied.removeAllViews();
        linFree.removeAllViews();
    }

          private int getNumCores() {
              //Private Class to display only CPU devices in the directory listing
              class CpuFilter implements FileFilter {
                  @Override
                  public boolean accept(File pathname) {
                      //Check if filename is "cpu", followed by a single digit number
                      if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                          return true;
                      }
                      return false;
                  }
              }

              try {
                  //Get directory containing CPU info
                  File dir = new File("/sys/devices/system/cpu/");
                  //Filter to only list the devices we care about
                  File[] files = dir.listFiles(new CpuFilter());
                  //Return the number of cores (virtual CPU devices)
                  return files.length;
              } catch(Exception e) {
                  //Default to return 1 core
                  return 1;
              }
          }

}
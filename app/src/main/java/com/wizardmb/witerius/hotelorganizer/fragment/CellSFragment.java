package com.wizardmb.witerius.hotelorganizer.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.wizardmb.witerius.hotelorganizer.MainActivity;
import com.wizardmb.witerius.hotelorganizer.R;
import com.wizardmb.witerius.hotelorganizer.Utils;
import com.wizardmb.witerius.hotelorganizer.adapter.CellAdapter;
import com.wizardmb.witerius.hotelorganizer.dialog.ADialog;
import com.wizardmb.witerius.hotelorganizer.dialog.BDialog;
import com.wizardmb.witerius.hotelorganizer.dialog.ConfirmDialog;
import com.wizardmb.witerius.hotelorganizer.dialog.ConfirmInterface;
import com.wizardmb.witerius.hotelorganizer.dialog.EditDateDialogFragment;
import com.wizardmb.witerius.hotelorganizer.dialog.InfoFragment;
import com.wizardmb.witerius.hotelorganizer.model.ModelAllData;
import com.wizardmb.witerius.hotelorganizer.model.ModelApartment;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wizardmb.witerius.hotelorganizer.MainActivity.apartmentAllRow;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.dateInteger;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.dateString;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.dbHelperLLs;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.endOfDay;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.fragmentManager;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.mSettings;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.todayDate_s;
import static com.wizardmb.witerius.hotelorganizer.MainActivity.twoDayLast;
import static com.wizardmb.witerius.hotelorganizer.adapter.ApartmentAdapter.itemApartmentModel;

/**
 * Created by Witerius on 30.10.2016.
 */

public final class CellSFragment extends Fragment implements ConfirmInterface {

    protected ScrollView ll_context;
    protected CellAdapter adapter;
    public  MainActivity activity;

    private GridLayout gridLayout;

    private int rowIndex;
    private int columnIndex;
    private int indexOfGridL; //id of all item on page and Index

    final int LEFT = 8;
    final int TOP = 8;
    final int RIGHT = 0;
    final int BOTTOM = 0;

    private int indexOfStartDrag = -1;

    private boolean clickForMove = false;
    private static LayoutInflater inflater;

    private int monthTwoLast;
    public static Map<Integer, Integer> apartmentHashMap;

    private MenuItem myActionEditItem;
    private MenuItem myActionDeleteItem;
    private MenuItem myActionMoveItem;

    private Snackbar snackbar;
    private ArrayList<ModelAllData> tempAllData;
    public static int rowForEdit = 0;
    private int backOrNextDayStart, backOrNextDayEnd;

    private int[] colors = new int[2];
    private int nomerCveta = 0;

    private List<ModelAllData> applicantsAL;
    private List<ModelAllData> occupiedAL;
    private List<ModelApartment> apartmentFreeAL;

    public static ConfirmInterface confirmInterface;
    public static int positionOfConfirm =0;
    public  static boolean isJustEdit = true;

    private boolean isError = false;
    private boolean isMoveFromApplicants = false;

    // Класс для работы потоком ввода в файл
    private FileOutputStream outputStream;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 3) {
            BDialog bDialog = new BDialog();
            bDialog.show(MainActivity.fragmentManager, "Help1");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();

        }

        try {
            confirmInterface  = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        addCellFromBD(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        ll_context = (ScrollView) rootView.findViewById(R.id.ll_context);
        this.inflater = inflater;


        return rootView;
    }
    public CellSFragment()
    {
        this.setRetainInstance(true);
    }

    public final void addNewData(ModelAllData newTask, boolean saveInBase, boolean isItAppplicants) {

        if(saveInBase)
        { MainActivity.dbHelperLLs.saveAllDataInBase(newTask);}

        try {
            if(!isItAppplicants) {
                int apartmId = newTask.getApartmentID();

                int apartmentNum = apartmentHashMap.get(apartmId);

                int dateSize = dateInteger.size();
                int tempI = 0;
                for (int i = 0; i < dateSize; i++) {
                    int dateIntTemp = dateInteger.get(i);
                    if (dateIntTemp == newTask.getDate_startInt()) {
                        tempI = i;
                        break;
                    }
                }
                int indexOfGridLayoutGet = 14 * apartmentNum + tempI + apartmentNum + 1;

                if (indexOfStartDrag != indexOfGridLayoutGet && !saveInBase && !isMoveFromApplicants) {
                    setNullTextInCellForEdit(indexOfStartDrag);

                }
                adapter.addItemHM(indexOfGridLayoutGet, newTask);

                View tempView = gridLayout.getChildAt(indexOfGridLayoutGet);
                TextView tv_name_OfTempView = (TextView) tempView.findViewById(R.id.name_c);
                TextView tv_date_OfTempView = (TextView) tempView.findViewById(R.id.date_c);

                String tempDateForCardView = "" + Utils.getWeekDate(newTask.getDate_start())
                        + " - " + Utils.getWeekDate(newTask.getDate_end());
                String tempName = newTask.getName();

                tv_name_OfTempView.setText(tempName);
                tv_date_OfTempView.setText(tempDateForCardView);
            }
            if(saveInBase && isItAppplicants) {
                int position = -1;
                int sizeA = applicantsAL.size();
                for (int i = 0; i < sizeA; i++) {

                    ModelAllData task =  applicantsAL.get(i);

                    String str2 = task.getName();
                    String str1 = newTask.getName();
                    int res=str1.compareTo(str2);
                    if (res < 0) {
                        position = i;
                        break;
                    }

                }
                if (position != -1) {

                    napolnenieSpiska(newTask, position, true);
                    applicantsAL.add(position, newTask);
                } else {

                    napolnenieSpiska(newTask, 0, false);
                    applicantsAL.add(newTask);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final void startApplicantsAT()
    {
        ApplicantsFullAT applicantsFullAT = new ApplicantsFullAT();
        applicantsFullAT.execute();
    }

    private void addCellFromBD(boolean isStart) {
        if(isStart)
        {
            createDayList();
            RemoveOldDataAT removeOldDataAT = new RemoveOldDataAT();
            removeOldDataAT.execute(monthTwoLast);

        }
        if(MainActivity.isFirstStart_s && MainActivity.saveNumHelp_s == 0)
        {
            ADialog aDialog = new ADialog();
            aDialog.show(MainActivity.fragmentManager, "Help1");

        }
        adapter = new CellAdapter();
        rowIndex = 0;
        columnIndex = 0;
        indexOfGridL = 0;

        gridLayout = new GridLayout(activity);
        gridLayout.setOrientation(GridLayout.HORIZONTAL);
        gridLayout.setColumnCount(15);
        gridLayout.setRowCount(apartmentAllRow + 2);
        ll_context.removeAllViews();
        ll_context.addView(gridLayout);

        if(isStart)
        {
            tempAllData = new ArrayList<>();
            tempAllData.addAll(MainActivity.dbHelperLLs.queryAllData().getAllDataFiltred(twoDayLast, endOfDay, 0));

            startApplicantsAT();
            startOccupiedAT();
        }
        for (int i = 0; i <= apartmentAllRow; i++) {

            for (int i1 = 0; i1 < 15; i1++) {

                if (i == 0 ) {
                    CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card_date, null);

                    TextView date_head = (TextView) cardView_f.findViewById(R.id.date_head);

                    try {

                        if (i1 != 0) {
                            date_head.setText(dateString.get(i1 - 1));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        cardView_f.setId(indexOfGridL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Кнопка будет создана в этой строке...
                    GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
                    //... и в этой колонке
                    GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

                    // Создадим параметр, в который передадим 2 строчки выше.
                    GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

                    gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
                //    gridLayoutParam.height =  100;
                //    gridLayoutParam.width = 420;
                    try {
                        gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
                    } catch (Exception e) {
                        e.printStackTrace();

                        isError = true;

                    }

                    indexOfGridL++;
                    columnIndex++;
                } else if (i > 0 && i1 > 0)

                {
                    CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);

                    cardView_f.setOnCreateContextMenuListener(new LongClickListennerForCardView());
                    cardView_f.setOnClickListener(new CardViewClickListener());
                    try {

                        cardView_f.setId(indexOfGridL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Кнопка будет создана в этой строке...
                    GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
                    //... и в этой колонке
                    GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

                    // Создадим параметр, в который передадим 2 строчки выше.
                    GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

                    gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
                //    gridLayoutParam.height =  android.support.v7.widget.GridLayout.LayoutParams.WRAP_CONTENT;//100;
                //    gridLayoutParam.width = android.support.v7.widget.GridLayout.LayoutParams.WRAP_CONTENT; //420

                    try {
                        gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
                    } catch (Exception e) {
                        e.printStackTrace();

                        isError = true;

                    }

                    indexOfGridL++;
                    columnIndex++;
                } else if (i > 0 && i1 == 0) {
                    CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);

                    TextView name_c = (TextView) cardView_f.findViewById(R.id.name_c);

                    for (int i2 = 0; i2 < apartmentAllRow; i2++) {
                        try {
                            int rowTemp = itemApartmentModel.get(i2).getApartmentNum();
                            if (rowTemp == i) {

                                name_c.setText(itemApartmentModel.get(i2).getShortCut());

                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }


                    try {

                        cardView_f.setId(indexOfGridL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Кнопка будет создана в этой строке...
                    GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
                    //... и в этой колонке
                    GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

                    // Создадим параметр, в который передадим 2 строчки выше.
                    GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

                    gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
                 //   gridLayoutParam.height =  android.support.v7.widget.GridLayout.LayoutParams.WRAP_CONTENT;//100;
                 //   gridLayoutParam.width = android.support.v7.widget.GridLayout.LayoutParams.WRAP_CONTENT; //420

                    try {
                        gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
                    } catch (Exception e) {
                        e.printStackTrace();
                        isError = true;

                    }

                    indexOfGridL++;
                    columnIndex++;
                }
            }
            columnIndex = 0;
            rowIndex++;

        }
        LinearLayout admob = (LinearLayout) inflater.inflate(R.layout.admob_ll, null);

        GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
        GridLayout.Spec column = GridLayout.spec(columnIndex, 1);
        GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

        gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
      /* gridLayoutParam.height = 140;
        gridLayoutParam.width = 420;*/

      /*  if(!MainActivity.isAddMob_s )
        {
            MainActivity.isAddMob_s = true;*/
            NativeExpressAdView adView = (NativeExpressAdView) admob.findViewById(R.id.adView2);

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("ca-app-pub-3867788656747346~4499468514")
                    .build();
            adView.loadAd(request);

            try {
                gridLayout.addView(admob, indexOfGridL, gridLayoutParam);

            } catch (Exception e) {
                e.printStackTrace();

                isError = true;

            }
      /*  }
        else
        {
            MainActivity.isAddMob_s = false;

        }


        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(MainActivity.APP_PREFERENCES_ADD_MOB, MainActivity.isAddMob_s);
        editor.apply();*/

        if( isError)
        {
            activity.reloadActivity();
        }

        indexOfGridL++;

        int lmiSize = tempAllData.size();
        int dateSize = dateInteger.size();

        addApartmnetInHMap();

        addDataInGridLayout(dateSize, lmiSize);


    }
   /* private void changeAddIfEmpty(GridLayout.LayoutParams gridLayoutParam)
    {
        LinearLayout admobA = (LinearLayout) inflater.inflate(R.layout.admob_ll, null);
            NativeExpressAdView adView = (NativeExpressAdView) admobA.findViewById(R.id.adView2);

            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("ca-app-pub-3867788656747346~4499468514")
                    .build();
            adView.loadAd(request);

            try {
                gridLayout.addView(admobA, indexOfGridL, gridLayoutParam);

            } catch (Exception e) {
                e.printStackTrace();

                isError = true;

            }

    }*/

    public final void backClick()
    {
        getBackNextWeek(true);
        addCellFromBD(false);

    }

    public final void nextClick()
    {
        getBackNextWeek(false);
        addCellFromBD(false);

    }


    private void addDataInGridLayout(int dateSize, int lmiSize)
    {
        for (int i = 0; i < dateSize; i++) {
            int dateIntTemp = dateInteger.get(i);
            for (int i1 = 0; i1 < lmiSize; i1++) {
                int tempDateFor = tempAllData.get(i1).getDate_startInt();

                if (tempDateFor == dateIntTemp) {

                    int apartmId = tempAllData.get(i1).getApartmentID();

                    int indexOfGridLayoutGet = 0;
                    View tempView = null;
                    try {
                        int apartmentNum = apartmentHashMap.get(apartmId);

                        indexOfGridLayoutGet = 14 * apartmentNum + i + apartmentNum + 1;
                        tempView = gridLayout.getChildAt(indexOfGridLayoutGet);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    adapter.addItemHM(indexOfGridLayoutGet, tempAllData.get(i1));

                    TextView tv_name_OfTempView = null;
                    try {
                        tv_name_OfTempView = (TextView) tempView.findViewById(R.id.name_c);
                        String tempName = tempAllData.get(i1).getName();
                        tv_name_OfTempView.setText(tempName);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    TextView tv_date_OfTempView = null;
                    try {
                        tv_date_OfTempView = (TextView) tempView.findViewById(R.id.date_c);
                        String tempDateForCardView = "" + Utils.getWeekDate(tempAllData.get(i1).getDate_start())
                                + " - " + Utils.getWeekDate(tempAllData.get(i1).getDate_end());
                        tv_date_OfTempView.setText(tempDateForCardView);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }

            }
        }
    }

    public final void addNewRowInGridView(int i, int i1)
    {
        gridLayout.setRowCount(apartmentAllRow + 1);
        if (i > 0 && i1 > 0)

        {
            CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);

            cardView_f.setOnCreateContextMenuListener(new LongClickListennerForCardView());
            cardView_f.setOnClickListener(new CardViewClickListener());

            try {

                cardView_f.setId(indexOfGridL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Кнопка будет создана в этой строке...
            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            //... и в этой колонке
            GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

            // Создадим параметр, в который передадим 2 строчки выше.
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

            gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
          /*  gridLayoutParam.height = 100;
            gridLayoutParam.width = 420;*/

            try {
                gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
            } catch (Exception e) {
                e.printStackTrace();

                activity.reloadActivity();

            }

            indexOfGridL++;
            columnIndex++;
        } else if (i > 0 && i1 == 0) {

            CardView cardView_f = (CardView) inflater.inflate(R.layout.item_card, null);

            TextView name_c = (TextView) cardView_f.findViewById(R.id.name_c);

            for (int i2 = 0; i2 < apartmentAllRow; i2++) {
                try {
                    int rowTemp = itemApartmentModel.get(i2).getApartmentNum();
                    if (rowTemp == i) {

                        name_c.setText(itemApartmentModel.get(i2).getShortCut());

                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }


            try {

                cardView_f.setId(indexOfGridL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Кнопка будет создана в этой строке...
            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            //... и в этой колонке
            GridLayout.Spec column = GridLayout.spec(columnIndex, 1);

            // Создадим параметр, в который передадим 2 строчки выше.
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, column);

            gridLayoutParam.setMargins(LEFT, TOP, RIGHT, BOTTOM);
         /*   gridLayoutParam.height = 100;
            gridLayoutParam.width = 420;*/

            try {
                gridLayout.addView(cardView_f, indexOfGridL, gridLayoutParam);
            } catch (Exception e) {
                e.printStackTrace();

                activity.reloadActivity();

            }

            indexOfGridL++;
            columnIndex++;
        }
        if(i1==14)
        {
            columnIndex = 0;
            rowIndex++;
        }


    }

    public final void addApartmnetInHMap() {
        newApartmentInHM();

        for (int ii = 0; ii < apartmentAllRow; ii++) {
            try {
                apartmentHashMap.put(itemApartmentModel.get(ii).getApartmentId(), itemApartmentModel.get(ii).getApartmentNum());

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    public final void newApartmentInHM() {
        apartmentHashMap = new HashMap<>();

    }
    public final void getBackNextWeek(boolean isBack)
    {
        dateInteger = new ArrayList<>();
        dateString = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        String stOfDay;
        if(isBack)
        {
            stOfDay= String.valueOf(backOrNextDayStart);// Устанавливаем точку отсчета
        }
        else
        {
            stOfDay= String.valueOf(backOrNextDayEnd);// Устанавливаем точку отсчета

        }

        String[] tempDate = stOfDay.split("(?<=\\G.{4})");
        String[] tempDayMonth = tempDate[1].split("(?<=\\G.{2})");

        calendar.set(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDayMonth[0]),
                Integer.parseInt(tempDayMonth[1]));


        if(isBack)
        {
            calendar.add(Calendar.DAY_OF_MONTH, -14); //вычисляем день

        }
        else
        {
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        for (int i = 0; i < 14; i++) {
            String dataIs = Utils.getWeekDate(calendar.getTimeInMillis());

            int yearTemp = calendar.get(Calendar.YEAR);
            int monthTemp = calendar.get(Calendar.MONTH);
            int dayTemp1 = calendar.get(Calendar.DAY_OF_MONTH);

            String monthStringTempD = "";
            String dayStringTempD = "";
            if (monthTemp < 10) {
                monthStringTempD = "0" + monthTemp;
            } else {
                monthStringTempD = "" + monthTemp;
            }
            if (dayTemp1 < 10) {
                dayStringTempD = "0" + dayTemp1;
            } else {
                dayStringTempD = "" + dayTemp1;
            }


            String dayStartTemp = "" + yearTemp + monthStringTempD + dayStringTempD;
            int dayForList = Integer.parseInt(dayStartTemp);

            dateString.add(dataIs);
            dateInteger.add(dayForList);

            calendar.add(Calendar.DAY_OF_MONTH, 1); //Прибавляем сутки

            if(i==0)
            {
                backOrNextDayStart = dayForList;
            }
            else if (i == 13) {

                backOrNextDayEnd = dayForList;
                tempAllData = new ArrayList<>();

                tempAllData.addAll(MainActivity.dbHelperLLs.queryAllData().getAllDataFiltred(backOrNextDayStart,
                        backOrNextDayEnd, 0));
                startApplicantsAT(); //был здесь

                startOccupiedAT();

            }
        }

    }
    public final void startOccupiedAT()
    {
        activity.clearViewOccupiedAndFree();
        OccupiedFullAT occupiedFullAT = new OccupiedFullAT();
        occupiedFullAT.execute();
    }

    public final void createDayList() {

        dateInteger = new ArrayList<>();
        dateString = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Устанавливаем текущее время

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String monthSTemp = "";
        String daySTemp = "";
        if (month < 10) {
            monthSTemp = "0" + month;
        } else {
            monthSTemp = "" + month;
        }
        if (day < 10) {
            daySTemp = "0" + day;
        } else {
            daySTemp = "" + day;
        }

        String tempToday = "" + year + ""+ monthSTemp + daySTemp;
        todayDate_s = Integer.parseInt(tempToday);

        int dayTemp = 0;

        if (day < 3) {
            dayTemp = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            day = dayTemp - (2 - day);
            month = month - 1;

        } else {
            day = day - 2;

        }
        calendar.set(year, month, day);
        int yearDayTwoLast = calendar.get(Calendar.YEAR);
        int monthrDayTwoLast = calendar.get(Calendar.MONTH);
        int dayrDayTwoLast = calendar.get(Calendar.DAY_OF_MONTH);

        String monthStringTemp = "";
        String dayStringTemp = "";
        if (monthrDayTwoLast < 10) {
            monthStringTemp = "0" + monthrDayTwoLast;
        } else {
            monthStringTemp = "" + monthrDayTwoLast;
        }
        if (dayrDayTwoLast < 10) {
            dayStringTemp = "0" + dayrDayTwoLast;
        } else {
            dayStringTemp = "" + dayrDayTwoLast;
        }
        String dayStartDayTwoLast = "" + yearDayTwoLast + monthStringTemp + dayStringTemp;

        twoDayLast = Integer.parseInt(dayStartDayTwoLast);
        backOrNextDayStart = twoDayLast;

        if (monthrDayTwoLast < 1) { //было 2
            yearDayTwoLast--;
         /*   if (monthrDayTwoLast == 1) {
                monthrDayTwoLast = 11;
            } else*/ if (monthrDayTwoLast == 0) {
                monthrDayTwoLast = 10;
            }

        }
        if (monthrDayTwoLast >= 1) { //было 2
            monthrDayTwoLast = monthrDayTwoLast - 1; //было 2

        }

        if (dayrDayTwoLast > 28) {
            dayrDayTwoLast = 28;
        }
        String monthTwoString = "";
        String dayTwoString = "";


        if (monthrDayTwoLast < 10) {
            monthTwoString = "0" + monthrDayTwoLast;
        } else {
            monthTwoString = "" + monthrDayTwoLast;
        }
        if (dayrDayTwoLast < 10) {
            dayTwoString = "0" + dayrDayTwoLast;
        } else {
            dayTwoString = "" + dayrDayTwoLast;
        }

        String monthTwoLastStr = "" + yearDayTwoLast + monthTwoString + dayTwoString;
        monthTwoLast = Integer.parseInt(monthTwoLastStr);

        for (int i = 0; i < 14; i++) {
            String dataIs = Utils.getWeekDate(calendar.getTimeInMillis());

            int yearTemp = calendar.get(Calendar.YEAR);
            int monthTemp = calendar.get(Calendar.MONTH);
            int dayTemp1 = calendar.get(Calendar.DAY_OF_MONTH);

            String monthStringTempD = "";
            String dayStringTempD = "";
            if (monthTemp < 10) {
                monthStringTempD = "0" + monthTemp;
            } else {
                monthStringTempD = "" + monthTemp;
            }
            if (dayTemp1 < 10) {
                dayStringTempD = "0" + dayTemp1;
            } else {
                dayStringTempD = "" + dayTemp1;
            }


            String dayStartTemp = "" + yearTemp + monthStringTempD + dayStringTempD;
            int dayForList = Integer.parseInt(dayStartTemp);

            dateString.add(dataIs);
            dateInteger.add(dayForList);

            calendar.add(Calendar.DAY_OF_MONTH, 1); //Прибавляем сутки

            if (i == 13) {
                endOfDay = dayForList;
                backOrNextDayEnd = dayForList;

            }
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void setTextInCardView(int key, boolean isEmpty, ModelAllData task) {
        CardView startView = (CardView) gridLayout.getChildAt(key);

        TextView tv_name_OfStartView = (TextView) startView.findViewById(R.id.name_c);
        TextView tv_date_OfStartView = (TextView) startView.findViewById(R.id.date_c);
        if (!isEmpty) {
            String tempDateStart = "" + Utils.getWeekDate(task.getDate_start())
                    + " - " + Utils.getWeekDate(task.getDate_end());
            tv_name_OfStartView.setText(task.getName());
            tv_date_OfStartView.setText(tempDateStart);
        } else {
            tv_name_OfStartView.setText("");
            tv_date_OfStartView.setText("");
        }
    }
    private Calendar calculateDate(int column)
    {
        Calendar newCalend = Calendar.getInstance();
        newCalend.setTime(new Date());

        String stOfDay = String.valueOf(dateInteger.get(column - 1));

        String[] tempDate = stOfDay.split("(?<=\\G.{4})");
        String[] tempDayMonth = tempDate[1].split("(?<=\\G.{2})");

        newCalend.set(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDayMonth[0]),
                Integer.parseInt(tempDayMonth[1]));
        return newCalend;
    }

    private int getApartmentId(int row)
    {
        int rowId=0;
        for (Map.Entry entry : apartmentHashMap.entrySet()) {
            int value = (int) entry.getValue();
            if (value == row) {
                rowId = (int) entry.getKey();
            }

        }
        return rowId;
    }

    private ModelAllData getModelFromHM(boolean isEmpty, int indexOfCardView)
    {
        ModelAllData modelAllData;
        if (isEmpty) {
            ////вычисляем строку и колонну,
            int row = 1;
            int index = 15;
            int apInd = apartmentAllRow + 1;
            int column = 0;
            for (int i = 0; i < apInd; i++) {
                if (index > indexOfCardView) {
                    column = indexOfCardView - (index - 15);

                    break;
                } else {
                    row++;
                    index = index + 15;
                }

            }
            row--;
            //по строке вытаскиваем idApartment
            int rowId = getApartmentId(row);
            //// вычисляем день по колонне
            Calendar newCalend = calculateDate(column);

            modelAllData = new ModelAllData(calculateIdEmptyMOADid(true), rowId, newCalend.getTimeInMillis(),
                    newCalend.getTimeInMillis(), 0, 0, null, dateInteger.get(column - 1), dateInteger.get(column - 1), null, null, 0);

        } else {
            modelAllData = adapter.getItemHM(indexOfCardView);
        }
        return modelAllData;
    }
    private long calculateIdEmptyMOADid(boolean save)
    {
        MainActivity.idEmptyMoad_s++;
        if(save)
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putLong(MainActivity.APP_PREFERENCES_SAVE_ID_EMPTY_MOAD, MainActivity.idEmptyMoad_s);
            editor.apply();
        }

        return  MainActivity.idEmptyMoad_s;
    }

    ////////////////////////////
    private void selectSecondCardView(View v) {
        boolean startEmpty = false;
        boolean endEmpty = false;

        int indexOfEndDrag = v.getId();

        clickForMove = false;
        CardView tempCardView = (CardView) gridLayout.getChildAt(indexOfStartDrag);
        tempCardView.setBackgroundColor(Color.WHITE);

        ModelAllData startMOAD;
        ModelAllData endMOAD;

        if (adapter.getItemHM(indexOfStartDrag) == null)
        {
            startEmpty = true;
        }
        if (adapter.getItemHM(indexOfEndDrag) == null)
        {
            endEmpty = true;
        }
        //получил model из HM
        startMOAD = getModelFromHM(startEmpty, indexOfStartDrag);
        endMOAD = getModelFromHM(endEmpty, indexOfEndDrag);

        int apartIdStart = startMOAD.getApartmentID();
        long dateStart = startMOAD.getDate_start();
        int dateIntStart = startMOAD.getDate_startInt();

        int apartIdEnd = endMOAD.getApartmentID();
        long dateEnd = endMOAD.getDate_start();
        int dateIntEnd = endMOAD.getDate_startInt();

        startMOAD.setApartmentID(apartIdEnd);
        startMOAD.setDate_start(dateEnd);
        startMOAD.setDate_startInt(dateIntEnd);

        endMOAD.setApartmentID(apartIdStart);
        endMOAD.setDate_start(dateStart);
        endMOAD.setDate_startInt(dateIntStart);

        if(!startEmpty)
        {  updateCellForDrag(indexOfEndDrag, startMOAD);}
        setTextInCardView(indexOfEndDrag, startEmpty, startMOAD);

        if(!endEmpty)
        { updateCellForDrag(indexOfStartDrag, endMOAD);}
        setTextInCardView(indexOfStartDrag, endEmpty, endMOAD);

        snackbar.dismiss();

    }

    private void createDataOnClickIfEmpty(View v) {

        final CardView cardView = (CardView) v;
        final int idCardView = cardView.getId();

        TextView tv_name_OfStartView = (TextView) cardView.findViewById(R.id.name_c);
        TextView tv_date_OfStartView = (TextView) cardView.findViewById(R.id.date_c);

        if (tv_name_OfStartView.length() == 0 && tv_date_OfStartView.length() == 0) {

            int row = 1;
            int index = 15;
            int apInd = apartmentAllRow + 1;
            int column = 0;
            for (int i = 0; i < apInd; i++) {
                if (index > idCardView) {
                    column = idCardView - (index - 15);

                    break;
                } else {
                    row++;
                    index = index + 15;
                }

            }

            row--;
            MainActivity.addingDataFromCardView.startDialog(column, row);

        } else {

            ModelAllData modelAllData = adapter.getItemHM(idCardView);
            showInfoDate(modelAllData, idCardView);
        }
    }

    private void checkFirstItem() {
        snackbar = Snackbar.make(getActivity().findViewById(R.id.activity_main),
                R.string.select_2nd_item, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickForMove = false;
                CardView tempCardView = (CardView) gridLayout.getChildAt(indexOfStartDrag);
                tempCardView.setBackgroundColor(Color.WHITE);
            }
        });
        snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                clickForMove = true;
                CardView tempCardView = (CardView) gridLayout.getChildAt(indexOfStartDrag);
                tempCardView.setBackgroundColor(getResources().getColor(R.color.color_select));
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        snackbar.show();

    }
    final class RemoveOldDataAT extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            List<ModelAllData> removeOldData = new ArrayList<>();
            removeOldData.addAll(dbHelperLLs.queryAllData().getAllDataFiltredEnd(0, params[0]));
            int sizeTemp = removeOldData.size();
            for (int i = 0; i < sizeTemp; i++) {
                dbHelperLLs.removeDataInBase(removeOldData.get(i).getTimeStamp());

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }


    final class CardViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!clickForMove) {
                createDataOnClickIfEmpty(v);
            } else {
                selectSecondCardView(v);
            }

        }
    }
    private boolean isCardViewEmpty(View v)
    {
        final CardView cardView = (CardView) v;
        boolean isEmpty = false;

        TextView tv_name_OfStartView = (TextView) cardView.findViewById(R.id.name_c);
        TextView tv_date_OfStartView = (TextView) cardView.findViewById(R.id.date_c);

        if (tv_name_OfStartView.length() == 0 && tv_date_OfStartView.length() == 0) {
            isEmpty = true;
        }
        return isEmpty;
    }
    final class LongClickListennerForCardView implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (item == myActionEditItem) {

                ModelAllData modelDataValue = adapter.getItemHM(indexOfStartDrag);
                showCellEditDialog(modelDataValue);

            } else if (item == myActionDeleteItem) {

                removeCellDialog(indexOfStartDrag, false);

            } else if (item == myActionMoveItem) {

                checkFirstItem();

            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (!clickForMove) {

                boolean isEmpty = isCardViewEmpty(v);
                indexOfStartDrag = v.getId();

                menu.setHeaderTitle(R.string.select_action);

                myActionMoveItem = menu.add(R.string.move_data);

                myActionMoveItem.setOnMenuItemClickListener(this);

                if(!isEmpty)
                {
                    myActionEditItem = menu.add(R.string.dialog_editing_data);
                    myActionDeleteItem = menu.add(R.string.dialog_deleting_data);

                    myActionEditItem.setOnMenuItemClickListener(this);
                    myActionDeleteItem.setOnMenuItemClickListener(this);

                }
            }

        }
    }

    public final void setNullTextInCellForEdit(int idCardView) {
        CardView cardView = (CardView) gridLayout.getChildAt(idCardView);
        TextView tv_name_OfStartView = (TextView) cardView.findViewById(R.id.name_c);
        TextView tv_date_OfStartView = (TextView) cardView.findViewById(R.id.date_c);
        tv_date_OfStartView.setText("");
        tv_name_OfStartView.setText("");

    }

    public final void updateCellForDrag(int key, ModelAllData task) {

        adapter.addItemHM(key, task);
        dbHelperLLs.updateAllData().itemMethod(task);
        startOccupiedAT();
    }

    public final void removeCellDialog(final int location, final boolean isItApplicants) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message_data);

        ModelAllData removingList;

        if(!isItApplicants)
        {
            removingList = adapter.getItemHM(location);// удаление Списка
        }
        else
        {
            removingList = applicantsAL.get(location);
        }
        final long timeStamp = removingList.getTimeStamp();

        dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!isItApplicants)
                {
                    adapter.removeItem(location);
                    setNullTextInCellForEdit(location);
                    MainActivity.dbHelperLLs.removeDataInBase(timeStamp);  // удаление

                    startOccupiedAT();
                    dialog.dismiss();
                }
                else
                {
                    applicantsAL.remove(location);
                    activity.linLayout.removeViewAt(location);

                    MainActivity.dbHelperLLs.removeDataInBase(timeStamp);
                    startOccupiedAT();
                    dialog.dismiss();
                }

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

    public final void showCellEditDialog(ModelAllData task) {  // показывает диалог редактир
        DialogFragment editingDateDialog = EditDateDialogFragment.newInstance(task);

        int numberOfRow = 0;
        int rotationRow = 14;

        while (rotationRow < indexOfStartDrag) {
            rotationRow = rotationRow + 15;
            numberOfRow = numberOfRow + 1;
        }
        rowForEdit = numberOfRow;
        editingDateDialog.show(getActivity().getSupportFragmentManager(), "EditDateDialogFragment");
    }
//Применяется только для кард вью, т.к. нужно вычисление по грид лайоуту
    public final void showInfoDate(ModelAllData task, int idOfView) {
        DialogFragment infoDialog = InfoFragment.newInstance(task);

        int numberOfRow = 0;
        int rotationRow = 14;

        while (rotationRow < idOfView) {
            rotationRow = rotationRow + 15;
            numberOfRow = numberOfRow + 1;
        }

        rowForEdit = numberOfRow;
        infoDialog.show(getActivity().getSupportFragmentManager(), "InfoFragment");
    }

    final class SelectListener implements View.OnClickListener
    {
        ModelAllData mbomLL;

        SelectListener(ModelAllData mbomLL1)
        {
            mbomLL = mbomLL1;

        }
        @Override
        public void onClick(View view) {
            try {
                for(int i =0; i<itemApartmentModel.size();i++) {

                    if (mbomLL.getApartmentID() == itemApartmentModel.get(i).getApartmentId()) {

                        rowForEdit = i+1;

                    }
                }


                DialogFragment infoDialog = InfoFragment.newInstance(mbomLL);
                infoDialog.show(getActivity().getSupportFragmentManager(), "InfoFragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    final class LongClickListennerForApplicants implements View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {
        ModelAllData mbomLL;

        LongClickListennerForApplicants(ModelAllData mbomLL1)
        {
            mbomLL = mbomLL1;
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int sizeB = applicantsAL.size();
            for(int i1 = 0; i1<sizeB; i1++)
            {
                ModelAllData model = applicantsAL.get(i1);
                if (mbomLL.getTimeStamp()== model.getTimeStamp())
                {
                    positionOfConfirm = i1;

                }
            }
            if (item == myActionEditItem) {
                isJustEdit = false;

                for(int i =0; i<itemApartmentModel.size();i++) {

                    if (mbomLL.getApartmentID() == itemApartmentModel.get(i).getApartmentId()) {

                        rowForEdit = i+1;

                    }
                }
                DialogFragment editingDateDialog = EditDateDialogFragment.newInstance(applicantsAL.get(positionOfConfirm));
                editingDateDialog.show(getActivity().getSupportFragmentManager(), "EditDateDialogFragment");

            } else if (item == myActionDeleteItem) {

                removeCellDialog(positionOfConfirm, true);

            } else if (item == myActionMoveItem) {

                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.show(fragmentManager, "ConfirmDialog");

            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (!clickForMove) {

                menu.setHeaderTitle(R.string.select_action);

                myActionMoveItem = menu.add(R.string.confirm);
                myActionEditItem = menu.add(R.string.dialog_editing_data);
                myActionDeleteItem = menu.add(R.string.dialog_deleting_data);

                myActionMoveItem.setOnMenuItemClickListener(this);
                myActionEditItem.setOnMenuItemClickListener(this);
                myActionDeleteItem.setOnMenuItemClickListener(this);

            }
        }

    }
    @Override
    public void confirmMove() {
        isMoveFromApplicants = true;
        try {
            ModelAllData tempMAD = applicantsAL.get(positionOfConfirm);

            tempMAD.setApplicants(0);
            MainActivity.confirmMoveEdited.onDataEdited(tempMAD);
            isMoveFromApplicants = false;

        } catch (Exception e) {
            e.printStackTrace();
            isMoveFromApplicants = false;
            activity.reloadActivity();
        }

    }

    private void napolnenieSpiska(final ModelAllData task, int position, boolean havePosition) {
        View item = inflater.inflate(R.layout.model_occupied, activity.linLayout, false);
        TextView tv_name_OfStartView = (TextView) item.findViewById(R.id.tvNameOccupied );
        TextView tv_apartment_OfStartView = (TextView)item.findViewById(R.id.tvApartmentOccupied);
        TextView tv_date_OfStartView = (TextView)item.findViewById(R.id.tvDateOccupied);

        String tempDateStart = "" + Utils.getWeekDate(task.getDate_start())
                + " - " + Utils.getWeekDate(task.getDate_end());

        tv_date_OfStartView.setText(tempDateStart);

        item.setOnClickListener(new SelectListener(task));

        colors[0] = activity.getResources().getColor(R.color.gray_50);
        colors[1] = activity.getResources().getColor(R.color.gray_60);

        item.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

        tv_name_OfStartView.setText(task.getName());
        item.setOnCreateContextMenuListener(new LongClickListennerForApplicants(task));

        String shortCut="";
        try {
            for(int i=0 ; i< itemApartmentModel.size(); i++)
            {
                if(task.getApartmentID() == itemApartmentModel.get(i).getApartmentId())
                {
                    shortCut = itemApartmentModel.get(i).getShortCut();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_apartment_OfStartView.setText(shortCut);
        if(!havePosition)
        {
            activity.linLayout.addView(item);
        }
        else {

            activity.linLayout.addView(item, position);
        }


    }
    private void napolnenieSpiskaOccupied(final ModelAllData task) {

        View item = inflater.inflate(R.layout.model_occupied, activity.linOccupied, false);
        TextView tv_name_OfStartView = (TextView) item.findViewById(R.id.tvNameOccupied );
        TextView tv_apartment_OfStartView = (TextView)item.findViewById(R.id.tvApartmentOccupied);
        TextView tv_date_OfStartView = (TextView)item.findViewById(R.id.tvDateOccupied);

        String tempDateStart = "" + Utils.getWeekDate(task.getDate_start())
                + " - " + Utils.getWeekDate(task.getDate_end());

        item.setOnClickListener(new SelectListener(task));

        colors[0] = activity.getResources().getColor(R.color.gray_50);
        colors[1] = activity.getResources().getColor(R.color.gray_60);

        item.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

        String shortCut="";
        try {
            for(int i=0 ; i< itemApartmentModel.size(); i++)
            {
                if(task.getApartmentID() == itemApartmentModel.get(i).getApartmentId())
                {
                    shortCut = itemApartmentModel.get(i).getShortCut();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_apartment_OfStartView.setText(shortCut);
        tv_name_OfStartView.setText(task.getName());
        tv_date_OfStartView.setText(tempDateStart);

        activity.linOccupied.addView(item);


    }
    private void napolnenieSpiskaApartment(ModelApartment modelApartment) {

        View item = inflater.inflate(R.layout.model_client, activity.linFree, false);
        TextView tv_name_OfStartView = (TextView) item.findViewById(R.id.tvFamilyName);
        TextView tv_payment_OfStartView = (TextView) item.findViewById(R.id.tvMobil);

        tv_name_OfStartView.setText(modelApartment.getShortCut());
        tv_payment_OfStartView.setText(String.valueOf(modelApartment.getPayment()));

        colors[0] = activity.getResources().getColor(R.color.gray_50);
        colors[1] = activity.getResources().getColor(R.color.gray_60);

        item.setBackgroundColor(colors[nomerCveta]);
        if(nomerCveta==0)
        {
            nomerCveta=1;
        }
        else
        {
            nomerCveta=0;
        }

        activity.linFree.addView(item);

    }
    final class ApplicantsFullAT extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.removeAllInApplicantsLinLayout();

        }

        @Override
        protected Void doInBackground(Void... params) {
            applicantsAL = new ArrayList<>();
            applicantsAL.addAll(MainActivity.dbHelperLLs.queryAllData().getAllDataFiltred(twoDayLast, endOfDay, 1));

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            int sizeA = applicantsAL.size();

            for(int i=0; i<sizeA; i++)
            {
                napolnenieSpiska(applicantsAL.get(i), 0, false);
            }
        }
    }

    final class OccupiedFullAT extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {

            apartmentFreeAL = new ArrayList<>();
            occupiedAL = new ArrayList<>();

            apartmentFreeAL.addAll(dbHelperLLs.queryApartment().getAllApartment(null, null, "apartment_address, apartment_payment, apartment_short_cut"));
            occupiedAL.addAll(MainActivity.dbHelperLLs.queryAllData().getAllDataFiltredOccupied(todayDate_s));


            try {
                for(int i=0; i<apartmentFreeAL.size(); i++)
                {
                    for(int i1 = 0; i1 <occupiedAL.size(); i1++)
                    {
                        if(apartmentFreeAL.get(i).getApartmentId() == occupiedAL.get(i1).getApartmentID())
                        {
                            apartmentFreeAL.remove(i);
                            i--;

                            break;
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            int sizeA = apartmentFreeAL.size();
            int sizeB = occupiedAL.size();

            for(int i=0; i<sizeA; i++)
            {
                napolnenieSpiskaApartment(apartmentFreeAL.get(i));
            }
            for(int i=0; i<sizeB; i++)
            {
                napolnenieSpiskaOccupied(occupiedAL.get(i));
            }
        }
    }


}

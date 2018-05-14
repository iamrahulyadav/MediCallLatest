package themedicall.com;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import themedicall.com.Adapter.MedAlarmAdapter;
import themedicall.com.BroadCasts.AlarmReceiver;
import themedicall.com.GetterSetter.MedAlarmGetterSetter;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.DatabaseHelperForMedAlarm;

import static android.content.Context.ALARM_SERVICE;


public class MedicineReminderDateFragment extends Fragment {

    ImageView im_plus_morning, im_minus_morning;
    ImageView im_plus_afternoon, im_minus_afternoon;
    ImageView im_plus_evening, im_minus_evening;
    ImageView im_plus_night, im_minus_night;
    RelativeLayout rl_expand_for_morning, rl_expand_for_afternoon, rl_expand_for_evening, rl_expand_for_night;
    RelativeLayout rl_plus_minus_morning, rl_plus_minus_afternoon, rl_plus_minus_evening, rl_plus_minus_night;
    TextView addMedicineForMorning , addMedicineForAfternoon , addMedicineForEvening , addMedicineForNight ;
    TextView NoOfTakeMedicineForMorning , NoOfTakeMedicineForAfternoon , NoOfTakeMedicineForEvening , NoOfTakeMedicineForNight ;
    TextView NoOfSkipMedicineForMorning , NoOfSkipMedicineForAfternoon , NoOfSkipMedicineForEvening , NoOfSkipMedicineForNight ;
    static int currentItem;
    static int dynamicCurrentItem;
    TextView medicineTime ;
    String medName;
    String time;
    String spinnerText;
    String alarmTime = "";
    String dateHourMinSecDBParameter ;
    String dateDBParameter ;
    AlarmManager alarmManager;
    private PendingIntent pendingIntentForMorning;
    private PendingIntent pendingIntentForAfternoon;
    private PendingIntent pendingIntentForEvening;
    private PendingIntent pendingIntentForNight;
    ArrayList<MedAlarmGetterSetter> medicineListForMorning ;
    ArrayList<MedAlarmGetterSetter> medicineListForAfternoon ;
    ArrayList<MedAlarmGetterSetter> medicineListForEvening ;
    ArrayList<MedAlarmGetterSetter> medicineListForNight ;
    static RecyclerView recycler_view_medicine_list_for_morning;
    static RecyclerView recycler_view_medicine_list_for_afternoon;
    static RecyclerView recycler_view_medicine_list_for_evening;
    static RecyclerView recycler_view_medicine_list_for_night;
    static LinearLayoutManager linearLayoutManagerForMorning ;
    static LinearLayoutManager linearLayoutManagerForAfternoon ;
    static LinearLayoutManager linearLayoutManagerForEvening ;
    static LinearLayoutManager linearLayoutManagerForNight ;


    public MedicineReminderDateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_medicine_reminder_date, container, false);


        initiate(view);
        getMedicineFromDB(currentItem);
        getMedicineTakeFromDB(currentItem);
        getMedicineSkipFromDB(currentItem);
        Log.e("tag" , "current view pager of position in on create view : " + currentItem);
        plusClickListenerForMorning();
        plusClickListenerForAftermoon();
        plusClickListenerForEvining();
        plusClickListenerForNight();
        setAlarmForMorning();
        setAlarmForAfternoon();
        setAlarmForEvening();
        setAlarmForNight();

        return view;

    }



    public void initiate(View view)
    {
        im_plus_morning = (ImageView) view.findViewById(R.id.im_plus_morning);
        im_minus_morning = (ImageView) view.findViewById(R.id.im_minus_morning);
        im_plus_afternoon = (ImageView) view.findViewById(R.id.im_plus_afternoon);
        im_minus_afternoon = (ImageView) view.findViewById(R.id.im_minus_afternoon);
        im_plus_evening = (ImageView) view.findViewById(R.id.im_plus_evning);
        im_minus_evening = (ImageView) view.findViewById(R.id.im_minus_evning);
        im_plus_night = (ImageView) view.findViewById(R.id.im_plus_night);
        im_minus_night = (ImageView) view.findViewById(R.id.im_minus_night);


        rl_expand_for_morning = (RelativeLayout) view.findViewById(R.id.rl_expand_for_morning);
        rl_expand_for_afternoon = (RelativeLayout) view.findViewById(R.id.rl_expand_for_afternoon);
        rl_expand_for_evening = (RelativeLayout) view.findViewById(R.id.rl_expand_for_evening);
        rl_expand_for_night = (RelativeLayout) view.findViewById(R.id.rl_expand_for_night);

        rl_plus_minus_morning = (RelativeLayout) view.findViewById(R.id.rl_plus_minus_morning);
        rl_plus_minus_afternoon = (RelativeLayout) view.findViewById(R.id.rl_plus_minus_afternoon);
        rl_plus_minus_evening = (RelativeLayout) view.findViewById(R.id.rl_plus_minus_evening);
        rl_plus_minus_night = (RelativeLayout) view.findViewById(R.id.rl_plus_minus_night);

        addMedicineForMorning = (TextView) view.findViewById(R.id.addMedicineForMorning);
        addMedicineForAfternoon = (TextView) view.findViewById(R.id.addMedicineForAfternoon);
        addMedicineForEvening = (TextView) view.findViewById(R.id.addMedicineForEvening);
        addMedicineForNight = (TextView) view.findViewById(R.id.addMedicineForNight);

        NoOfTakeMedicineForMorning = (TextView) view.findViewById(R.id.NoOfTakeMedicineForMorning);
        NoOfTakeMedicineForAfternoon = (TextView) view.findViewById(R.id.NoOfTakeMedicineForAfternoon);
        NoOfTakeMedicineForEvening = (TextView) view.findViewById(R.id.NoOfTakeMedicineForEvening);
        NoOfTakeMedicineForNight = (TextView) view.findViewById(R.id.NoOfTakeMedicineForNight);

        NoOfSkipMedicineForMorning = (TextView) view.findViewById(R.id.NoOfSkipMedicineForMorning);
        NoOfSkipMedicineForAfternoon = (TextView) view.findViewById(R.id.NoOfSkipMedicineForAfternoon);
        NoOfSkipMedicineForEvening = (TextView) view.findViewById(R.id.NoOfSkipMedicineForEvening);
        NoOfSkipMedicineForNight = (TextView) view.findViewById(R.id.NoOfSkipMedicineForNight);



        recycler_view_medicine_list_for_morning = (RecyclerView) view.findViewById(R.id.recycler_view_medicine_list_for_morning);
        recycler_view_medicine_list_for_morning.setHasFixedSize(true);
        linearLayoutManagerForMorning = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL, false);
        recycler_view_medicine_list_for_morning.setLayoutManager(linearLayoutManagerForMorning);


        recycler_view_medicine_list_for_afternoon = (RecyclerView) view.findViewById(R.id.recycler_view_medicine_list_for_afternoon);
        recycler_view_medicine_list_for_afternoon.setHasFixedSize(true);
        linearLayoutManagerForAfternoon = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL, false);
        recycler_view_medicine_list_for_afternoon.setLayoutManager(linearLayoutManagerForAfternoon);


        recycler_view_medicine_list_for_evening = (RecyclerView) view.findViewById(R.id.recycler_view_medicine_list_for_evening);
        recycler_view_medicine_list_for_evening.setHasFixedSize(true);
        linearLayoutManagerForEvening = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL, false);
        recycler_view_medicine_list_for_evening.setLayoutManager(linearLayoutManagerForEvening);


        recycler_view_medicine_list_for_night = (RecyclerView) view.findViewById(R.id.recycler_view_medicine_list_for_night);
        recycler_view_medicine_list_for_night.setHasFixedSize(true);
        linearLayoutManagerForNight = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL, false);
        recycler_view_medicine_list_for_night.setLayoutManager(linearLayoutManagerForNight);



        medicineListForMorning = new ArrayList<MedAlarmGetterSetter>();
        medicineListForAfternoon = new ArrayList<MedAlarmGetterSetter>();
        medicineListForEvening = new ArrayList<MedAlarmGetterSetter>();
        medicineListForNight = new ArrayList<MedAlarmGetterSetter>();

        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent morningIntent = new Intent(getActivity(), AlarmReceiver.class);
        morningIntent.putExtra("alarmId", 0);
        pendingIntentForMorning = PendingIntent.getBroadcast(getActivity(), 0, morningIntent, 0);
        pendingIntentForAfternoon = PendingIntent.getBroadcast(getActivity(), 1, morningIntent, 0);
        pendingIntentForEvening = PendingIntent.getBroadcast(getActivity(), 2, morningIntent, 0);
        pendingIntentForNight = PendingIntent.getBroadcast(getActivity(), 3, morningIntent, 0);



    }

    public void getMedicineTakeFromDB(int pos)
    {
        DatabaseHelperForMedAlarm databaseHelperForMedAlarm = new DatabaseHelperForMedAlarm(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, pos);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        String medTakeForMorning = databaseHelperForMedAlarm.NoOfMedTake("Morning" , date);
        String medTakeForAfternoon = databaseHelperForMedAlarm.NoOfMedTake("Afternoon" , date);
        String medTakeForEvening = databaseHelperForMedAlarm.NoOfMedTake("Evening" , date);
        String medTakeForNight = databaseHelperForMedAlarm.NoOfMedTake("Night" , date);

        Log.e("tag" , "get data form db for medicine take morning : "+medTakeForMorning);
        Log.e("tag" , "get data form db for medicine take afternoon : "+medTakeForAfternoon);
        Log.e("tag" , "get data form db for medicine take evening : "+medTakeForEvening);
        Log.e("tag" , "get data form db for medicine take night : "+medTakeForNight);


        if(medTakeForMorning == null)
        {
            NoOfTakeMedicineForMorning.setText("0");
        }
        else
        {
            NoOfTakeMedicineForMorning.setText(medTakeForMorning);
        }

        if(medTakeForAfternoon == null)
        {
            NoOfTakeMedicineForAfternoon.setText("0");
        }
        else
        {
            NoOfTakeMedicineForAfternoon.setText(medTakeForAfternoon);
        }


        if(medTakeForEvening == null)
        {
            NoOfTakeMedicineForEvening.setText("0");
        }
        else
        {
            NoOfTakeMedicineForEvening.setText(medTakeForEvening);
        }


        if(medTakeForNight == null)
        {
            NoOfTakeMedicineForNight.setText("0");
        }
        else
        {
            NoOfTakeMedicineForNight.setText(medTakeForNight);
        }



    }


    public void getMedicineSkipFromDB(int pos)
    {

        DatabaseHelperForMedAlarm databaseHelperForMedAlarm = new DatabaseHelperForMedAlarm(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, pos);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        String medSkipForMorning = databaseHelperForMedAlarm.NoOfMedSkip("Morning" , date);
        String medSkipForAfternoon = databaseHelperForMedAlarm.NoOfMedSkip("Afternoon" , date);
        String medSkipForEvening = databaseHelperForMedAlarm.NoOfMedSkip("Evening" , date);
        String medSkipForNight = databaseHelperForMedAlarm.NoOfMedSkip("Night" , date);


        Log.e("tag" , "get data form db for medicine skip morning : "+medSkipForMorning);
        Log.e("tag" , "get data form db for medicine skip afternoon : "+medSkipForAfternoon);
        Log.e("tag" , "get data form db for medicine skip evening : "+medSkipForEvening);
        Log.e("tag" , "get data form db for medicine skip night : "+medSkipForNight);


        if(medSkipForMorning == null)
        {
            NoOfSkipMedicineForMorning.setText("0");
        }
        else
        {
            NoOfSkipMedicineForMorning.setText(medSkipForMorning);
        }

        if(medSkipForAfternoon == null)
        {
            NoOfSkipMedicineForAfternoon.setText("0");
        }
        else
        {
            NoOfSkipMedicineForAfternoon.setText(medSkipForAfternoon);
        }

        if(medSkipForEvening == null)
        {
            NoOfSkipMedicineForEvening.setText("0");
        }
        else
        {
            NoOfSkipMedicineForEvening.setText(medSkipForEvening);
        }

        if(medSkipForNight == null)
        {
            NoOfSkipMedicineForNight.setText("0");
        }
        else
        {
            NoOfSkipMedicineForNight.setText(medSkipForNight);
        }




    }


    public void getMedicineFromDB(int pos)
    {

        DatabaseHelperForMedAlarm databaseHelperForMedAlarm = new DatabaseHelperForMedAlarm(getActivity());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, pos);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());

        Log.e("tag" , "get data form db currentItem : "+currentItem);
        Log.e("tag" , "get data form db date : "+date);
        List medListForMorning = databaseHelperForMedAlarm.getMedicine("Morning" , date);
        Log.e("tag" , "get data form db for morning : "+medListForMorning.size());
        List medListForAfternoon = databaseHelperForMedAlarm.getMedicine("Afternoon" , date);
        Log.e("tag" , "get data form db for afternoon : "+medListForAfternoon.size());
        List medListForEvening = databaseHelperForMedAlarm.getMedicine("Evening" , date);
        Log.e("tag" , "get data form db for evening : "+medListForEvening.size());
        List medListForNight = databaseHelperForMedAlarm.getMedicine("Night" , date);
        Log.e("tag" , "get data form db for night : "+medListForNight.size());


        if(medListForMorning.size() == 0)
        {
            recycler_view_medicine_list_for_morning.setVisibility(View.GONE);
        }
        else
        {
            recycler_view_medicine_list_for_morning.setVisibility(View.VISIBLE);
            MedAlarmAdapter medAlarmAdapterMorning = new MedAlarmAdapter(getActivity() , medListForMorning);
            recycler_view_medicine_list_for_morning.setAdapter(medAlarmAdapterMorning);
        }


        if(medListForAfternoon.size() == 0)
        {
            recycler_view_medicine_list_for_afternoon.setVisibility(View.GONE);
        }
        else
        {
            recycler_view_medicine_list_for_afternoon.setVisibility(View.VISIBLE);
            MedAlarmAdapter medAlarmAdapterAfternoon = new MedAlarmAdapter(getActivity() , medListForAfternoon);
            recycler_view_medicine_list_for_afternoon.setAdapter(medAlarmAdapterAfternoon);
        }


        if(medListForEvening.size() == 0)
        {
            recycler_view_medicine_list_for_evening.setVisibility(View.GONE);
        }
        else
        {
            recycler_view_medicine_list_for_evening.setVisibility(View.VISIBLE);
            MedAlarmAdapter medAlarmAdapterEvening = new MedAlarmAdapter(getActivity() , medListForEvening);
            recycler_view_medicine_list_for_evening.setAdapter(medAlarmAdapterEvening);
        }



        if(medListForNight.size() == 0)
        {
            recycler_view_medicine_list_for_night.setVisibility(View.GONE);
        }
        else
        {
            recycler_view_medicine_list_for_night.setVisibility(View.VISIBLE);
            MedAlarmAdapter medAlarmAdapterNight = new MedAlarmAdapter(getActivity() , medListForNight);
            recycler_view_medicine_list_for_night.setAdapter(medAlarmAdapterNight);
        }


    }


    public void plusClickListenerForMorning(){

        rl_plus_minus_morning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rl_expand_for_afternoon.getVisibility()==View.VISIBLE){
                    rl_expand_for_afternoon.setVisibility(View.GONE);

                    im_plus_afternoon.setVisibility(View.VISIBLE);
                    im_minus_afternoon.setVisibility(View.GONE);
                }
                if (rl_expand_for_evening.getVisibility()==View.VISIBLE){
                    rl_expand_for_evening.setVisibility(View.GONE);
                    im_plus_evening.setVisibility(View.VISIBLE);
                    im_minus_evening.setVisibility(View.GONE);
                }

                if (rl_expand_for_night.getVisibility()==View.VISIBLE){
                    rl_expand_for_night.setVisibility(View.GONE);
                    im_plus_night.setVisibility(View.VISIBLE);
                    im_minus_night.setVisibility(View.GONE);
                }


                if (rl_expand_for_morning.getVisibility()==View.VISIBLE){

                    im_plus_morning.setVisibility(View.VISIBLE);
                    im_minus_morning.setVisibility(View.GONE);
                    rl_expand_for_morning.setVisibility(View.GONE);
                }
                else if (rl_expand_for_morning.getVisibility()==View.GONE){

                    im_plus_morning.setVisibility(View.GONE);
                    im_minus_morning.setVisibility(View.VISIBLE);
                    rl_expand_for_morning.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //

    public void plusClickListenerForAftermoon(){

        rl_plus_minus_afternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rl_expand_for_morning.getVisibility()==View.VISIBLE){
                    rl_expand_for_morning.setVisibility(View.GONE);
                    im_plus_morning.setVisibility(View.VISIBLE);
                    im_minus_morning.setVisibility(View.GONE);
                }

                if (rl_expand_for_evening.getVisibility()==View.VISIBLE){
                    rl_expand_for_evening.setVisibility(View.GONE);
                    im_plus_evening.setVisibility(View.VISIBLE);
                    im_minus_evening.setVisibility(View.GONE);
                }

                if (rl_expand_for_night.getVisibility()==View.VISIBLE){
                    rl_expand_for_night.setVisibility(View.GONE);
                    im_plus_night.setVisibility(View.VISIBLE);
                    im_minus_night.setVisibility(View.GONE);
                }


                if (rl_expand_for_afternoon.getVisibility()==View.VISIBLE){

                    im_plus_afternoon.setVisibility(View.VISIBLE);
                    im_minus_afternoon.setVisibility(View.GONE);
                    rl_expand_for_afternoon.setVisibility(View.GONE);
                }
                else if (rl_expand_for_afternoon.getVisibility()==View.GONE){

                    im_plus_afternoon.setVisibility(View.GONE);
                    im_minus_afternoon.setVisibility(View.VISIBLE);
                    rl_expand_for_afternoon.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //

    public void plusClickListenerForEvining(){

        rl_plus_minus_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rl_expand_for_morning.getVisibility()==View.VISIBLE){
                    rl_expand_for_morning.setVisibility(View.GONE);
                    im_plus_morning.setVisibility(View.VISIBLE);
                    im_minus_morning.setVisibility(View.GONE);
                }
                if (rl_expand_for_afternoon.getVisibility()==View.VISIBLE){
                    rl_expand_for_afternoon.setVisibility(View.GONE);
                    im_plus_afternoon.setVisibility(View.VISIBLE);
                    im_minus_afternoon.setVisibility(View.GONE);
                }

                if (rl_expand_for_night.getVisibility()==View.VISIBLE){
                    rl_expand_for_night.setVisibility(View.GONE);
                    im_plus_night.setVisibility(View.VISIBLE);
                    im_minus_night.setVisibility(View.GONE);
                }

                if (rl_expand_for_evening.getVisibility()==View.VISIBLE){

                    im_plus_evening.setVisibility(View.VISIBLE);
                    im_minus_evening.setVisibility(View.GONE);
                    rl_expand_for_evening.setVisibility(View.GONE);
                }
                else if (rl_expand_for_evening.getVisibility()==View.GONE){

                    im_plus_evening.setVisibility(View.GONE);
                    im_minus_evening.setVisibility(View.VISIBLE);
                    rl_expand_for_evening.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //
    public void plusClickListenerForNight(){

        rl_plus_minus_night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rl_expand_for_morning.getVisibility()==View.VISIBLE){
                    rl_expand_for_morning.setVisibility(View.GONE);
                    im_plus_morning.setVisibility(View.VISIBLE);
                    im_minus_morning.setVisibility(View.GONE);
                }
                if (rl_expand_for_afternoon.getVisibility()==View.VISIBLE){
                    rl_expand_for_afternoon.setVisibility(View.GONE);
                    im_plus_afternoon.setVisibility(View.VISIBLE);
                    im_minus_afternoon.setVisibility(View.GONE);
                }


                if (rl_expand_for_evening.getVisibility()==View.VISIBLE){
                    rl_expand_for_evening.setVisibility(View.GONE);
                    im_plus_evening.setVisibility(View.VISIBLE);
                    im_minus_evening.setVisibility(View.GONE);
                }


                if (rl_expand_for_night.getVisibility()==View.VISIBLE){

                    im_plus_night.setVisibility(View.VISIBLE);
                    im_minus_night.setVisibility(View.GONE);
                    rl_expand_for_night.setVisibility(View.GONE);
                }
                else if (rl_expand_for_night.getVisibility()==View.GONE){

                    im_plus_night.setVisibility(View.GONE);
                    im_minus_night.setVisibility(View.VISIBLE);
                    rl_expand_for_night.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //




    public void setAlarmForMorning(){

        rl_expand_for_morning.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                setMedicineReminderDialog();


            }
        });
    }

    //

    public void setAlarmForAfternoon(){

        rl_expand_for_afternoon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                setMedicineReminderDialog();

            }
        });
    }
    //

    public void setAlarmForEvening(){

        rl_expand_for_evening.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                setMedicineReminderDialog();

            }
        });
    }

    //

    public void setAlarmForNight(){

        rl_expand_for_night.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {


                setMedicineReminderDialog();



            }
        });
    }

    //


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarmForMorning(int timeHour, int timeMinute, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Log.e("TAG", "Year: " + year);
        Log.e("TAG", "month: " + month);
        Log.e("TAG", "DAY: " + day);
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        Log.e("TAG", "TimeHousr: " + timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyyhh:mm:ss");
        String dateWithHourMinSecond = sdf.format(calendar.getTime());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        dateDBParameter = date ;


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForMorning);

            Log.e("tag" , "calender time is get time in millis in morning: "+calendar.getTime());

         dateHourMinSecDBParameter = dateWithHourMinSecond;

    }

    //

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarmForAftermoon(int timeHour, int timeMinute, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Log.e("TAG", "Year: " + year);
        Log.e("TAG", "month: " + month);
        Log.e("TAG", "DAY: " + day);
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        Log.e("TAG", "TimeHousr: " + timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyyhh:mm:ss");
        String dateWithHourMinSecond = sdf.format(calendar.getTime());
        dateHourMinSecDBParameter = dateWithHourMinSecond;


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        dateDBParameter = date ;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForAfternoon);



        Log.e("tag" , "calender time is get time in millis in afternoon: "+calendar.getTimeInMillis());
        Log.e("tag" , "calender time is get time in afternoon: "+dateWithHourMinSecond);


    }
    //

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarmForEvining(int timeHour, int timeMinute, int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Log.e("TAG", "Year: " + year);
        Log.e("TAG", "month: " + month);
        Log.e("TAG", "DAY: " + day);
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        Log.e("TAG", "TimeHousr: " + timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);


        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyyhh:mm:ss");
        String dateWithHourMinSecond = sdf.format(calendar.getTime());
        dateHourMinSecDBParameter = dateWithHourMinSecond;


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        dateDBParameter = date ;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForEvening);

        Log.e("tag" , "calender time is get time in millis in evening: "+calendar.getTimeInMillis());

    }
    //

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarmForNight(int timeHour, int timeMinute, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Log.e("TAG", "Year: " + year);
        Log.e("TAG", "month: " + month);
        Log.e("TAG", "DAY: " + day);
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        Log.e("TAG", "TimeHousr: " + timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyyhh:mm:ss");
        String dateWithHourMinSecond = sdf.format(calendar.getTime());
        dateHourMinSecDBParameter = dateWithHourMinSecond;


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
        String date = dateFormat.format(calendar.getTime());
        dateDBParameter = date ;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForNight);


        Log.e("tag" , "calender time is get time in millis in night: "+calendar.getTimeInMillis());

    }


    public static MedicineReminderDateFragment instance(int pos , Context context)
    {
        currentItem = pos;
        //getMedicineFromDB(context , pos);

        Log.e("tag" , "View pager Current Pos Is : " +currentItem);
        MedicineReminderDateFragment medicineReminderDateFragment = new MedicineReminderDateFragment();
        return medicineReminderDateFragment;

    }


    public void setMedicineReminderDialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.medicine_reminder_custom_layout);
        final EditText medicineName = (EditText) dialog.findViewById(R.id.medicineName);
        LinearLayout medicineTimeLayout = (LinearLayout) dialog.findViewById(R.id.medicineTimeLayout);
        medicineTime = (TextView) dialog.findViewById(R.id.medicineTime);
        final Spinner medicineQuantity = (Spinner) dialog.findViewById(R.id.medicineQuantity);
        Button medicineReminderSubmit = (Button) dialog.findViewById(R.id.medicineReminderSubmit);
        Button medicineReminderCancel = (Button) dialog.findViewById(R.id.medicineReminderCancle);
        dialog.show();


        ArrayAdapter<String> medicineQuantityArrayAdapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.medicine_quantity));
        medicineQuantity.setAdapter(medicineQuantityArrayAdapter);


        medicineTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setMedicineReminderTime();

            }
        });

        medicineReminderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        medicineReminderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(medicineName.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Please enter medicine name", Toast.LENGTH_SHORT).show();
                }
                else if(medicineTime.getText().toString().equals("Time"))
                {
                    Toast.makeText(getActivity(), "please select time for take medicine", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialog.dismiss();
                    medName = medicineName.getText().toString();
                    time = medicineTime.getText().toString();
                    spinnerText = medicineQuantity.getSelectedItem().toString();
                    Log.e("tag" , "alarm time is : "+alarmTime);
                    Log.e("tag" , "alarm data and time : "+dateHourMinSecDBParameter);
                    Log.e("tag" , "date without seconds in else : "+dateDBParameter);

                    String MedicineTake = "0";
                    String MedicineSkip = "0";

                    DatabaseHelperForMedAlarm databaseHelperForMedAlarm = new DatabaseHelperForMedAlarm(getActivity());
                    long isDataInserted = databaseHelperForMedAlarm.insertDataInTable(medName , spinnerText , alarmTime , dateHourMinSecDBParameter , MedicineTake , MedicineSkip , dateDBParameter);
                    if (isDataInserted > -1) {
                        Log.e("tag", "data inserted to table");
                    }

                    int dbDataCount = databaseHelperForMedAlarm.getCount();
                    Log.e("tag" , "db count is : "+dbDataCount);

                }
            }
        });



    }


    public void setMedicineReminderTime()
    {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
        SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
        SimpleDateFormat dayformate = new SimpleDateFormat("dd");

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, currentItem);
        String year = yearFormate.format(calendar.getTime());
        Log.e("TAG", "Year: " + year);
        String month = monthFormate.format(calendar.getTime());
        Log.e("TAG", "Mont: " + month);
        String currentday = dayformate.format(calendar.getTime());
        Log.e("TAG", "Day: " + currentday);


        final int claYear = Integer.parseInt(year);
        final int clamonth = Integer.parseInt(month);
        final int claday = Integer.parseInt(currentday);

        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";


                Log.e("TAG", "Selected time is : " +selectedHour + ":" + selectedMinute);

                medicineTime.setText(strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm);

                if(selectedHour>5 && selectedHour<12) { //setting range of time for morning
                    startAlarmForMorning(selectedHour, selectedMinute, claYear, clamonth, claday);
                    Log.e("tag" , "Time Between 5 AM to 12 PM");

                    alarmTime = "Morning";
                }
                else if(selectedHour>=12 && selectedHour<18) { //setting range of time for aftermoon

                    startAlarmForAftermoon(selectedHour, selectedMinute, claYear, clamonth, claday);
                    Log.e("tag" , "Time Between 12 PM to 5 PM");

                    alarmTime = "Afternoon";

                }

                else if(selectedHour>=18 && selectedHour<21) { //setting range of time for evining
                    Log.e("tag" , "Time Between 5 PM to 8 PM");
                    startAlarmForEvining(selectedHour, selectedMinute, claYear, clamonth, claday);

                    alarmTime = "Evening";
                }

                else if(selectedHour>=21) { //setting range of time for night

                    if (selectedHour>=21 && selectedHour<24){
                        startAlarmForNight(selectedHour, selectedMinute, claYear, clamonth, claday);
                        alarmTime = "Night";

                    }
                    Log.e("tag" , "Time Between 8 PM to 6 AM");

                }
                else if (selectedHour>=0 && selectedHour<6){
                    startAlarmForNight(selectedHour, selectedMinute, claYear, clamonth, claday);
                    alarmTime = "Night";

                }

                else {
                    Toast.makeText(getActivity(), "Out Of Range...", Toast.LENGTH_SHORT).show();
                }
            }
        }, hour, minute, false);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        getMedicineFromDB(currentItem);
        getMedicineTakeFromDB(currentItem);
        getMedicineSkipFromDB(currentItem);
        Log.e("tag" , "current view pager of position in on create view : " + currentItem);
    }

    @Override
    public void onStop() {
        super.onStop();
        getMedicineFromDB(currentItem);
        getMedicineTakeFromDB(currentItem);
        getMedicineSkipFromDB(currentItem);
        Log.e("tag" , "current view pager of position in on create view : " + currentItem);
    }

}

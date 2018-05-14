package themedicall.com.MedicineAlarm;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import themedicall.com.BroadCasts.AlarmReceiver;
import themedicall.com.R;

import static android.content.Context.ALARM_SERVICE;

public class MedAlarmDayOne extends Fragment {
    ImageView im_plus_morning, im_minus_morning;
    ImageView im_plus_afternoon, im_minus_afternoon;
    ImageView im_plus_evening, im_minus_evening;
    ImageView im_plus_night, im_minus_night;
    RelativeLayout rl_expand_for_morning, rl_expand_for_afternoon, rl_expand_for_evening, rl_expand_for_night;
    RelativeLayout rl_plus_minus_morning, rl_plus_minus_afternoon, rl_plus_minus_evening, rl_plus_minus_night;
    int currentItem;

    AlarmManager alarmManager;
    private PendingIntent pendingIntentForMorning;
    private PendingIntent pendingIntentForAfternoon;
    private PendingIntent pendingIntentForEvening;
    private PendingIntent pendingIntentForNight;


    public MedAlarmDayOne() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_med_alarm_day_one, container, false);


        initiate(view);
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


        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent morningIntent = new Intent(getActivity(), AlarmReceiver.class);
        morningIntent.putExtra("alarmId", 0);
        pendingIntentForMorning = PendingIntent.getBroadcast(getActivity(), 0, morningIntent, 0);
        pendingIntentForAfternoon = PendingIntent.getBroadcast(getActivity(), 1, morningIntent, 0);
        pendingIntentForEvening = PendingIntent.getBroadcast(getActivity(), 2, morningIntent, 0);
        pendingIntentForNight = PendingIntent.getBroadcast(getActivity(), 3, morningIntent, 0);



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

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
                SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
                SimpleDateFormat dayformate = new SimpleDateFormat("dd");

                Calendar calendar = new GregorianCalendar();
                //calendar.add(Calendar.DATE, 1);
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
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e("TAG", selectedHour + ":" + selectedMinute);
                        // eReminderTime.setText( selectedHour + ":" + selectedMinute);


                   /*     if((selectedHour <= (mcurrentTime.get(Calendar.HOUR_OF_DAY)))&&
                                (selectedMinute <= (mcurrentTime.get(Calendar.MINUTE)))){
                            Toast.makeText(getActivity(), "You have selected past time",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else*/ if(selectedHour>5 && selectedHour<12) { //setting range of time for morning
                            startAlarmForMorning(selectedHour, selectedMinute, claYear, clamonth, claday);
                        }

                        else {
                            Toast.makeText(getActivity(), "Out Of Range...", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }
        });
    }

    //

    public void setAlarmForAfternoon(){

        rl_expand_for_afternoon.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
                SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
                SimpleDateFormat dayformate = new SimpleDateFormat("dd");

                Calendar calendar = new GregorianCalendar();
                //calendar.add(Calendar.DATE, 1);
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
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e("TAG", selectedHour + ":" + selectedMinute);
                        // eReminderTime.setText( selectedHour + ":" + selectedMinute);

                      /*  if((selectedHour <= (mcurrentTime.get(Calendar.HOUR_OF_DAY)))&&
                                (selectedMinute <= (mcurrentTime.get(Calendar.MINUTE)))){
                            Toast.makeText(getActivity(), "You have selected past time",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else*/ if(selectedHour>=12 && selectedHour<18) { //setting range of time for aftermoon

                            startAlarmForAftermoon(selectedHour, selectedMinute, claYear, clamonth, claday);
                            Log.e("Tag" , "set alarm for after noon");
                        }

                        else {
                            Toast.makeText(getActivity(), "Out Of Range...", Toast.LENGTH_SHORT).show();
                        }




                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }
        });
    }
    //

    public void setAlarmForEvening(){

        rl_expand_for_evening.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
                SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
                SimpleDateFormat dayformate = new SimpleDateFormat("dd");

                Calendar calendar = new GregorianCalendar();
                //calendar.add(Calendar.DATE, 1);
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
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e("TAG", selectedHour + ":" + selectedMinute);
                        // eReminderTime.setText( selectedHour + ":" + selectedMinute);

                       /* if((selectedHour <= (mcurrentTime.get(Calendar.HOUR_OF_DAY)))&&
                                (selectedMinute <= (mcurrentTime.get(Calendar.MINUTE)))){
                            Toast.makeText(getActivity(), "You have selected past time",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else*/ if(selectedHour>=18 && selectedHour<21) { //setting range of time for evining

                            startAlarmForEvining(selectedHour, selectedMinute, claYear, clamonth, claday);
                        }

                        else {
                            Toast.makeText(getActivity(), "Out Of Range...", Toast.LENGTH_SHORT).show();
                        }




                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



            }
        });
    }

    //

    public void setAlarmForNight(){

        rl_expand_for_night.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy");
                SimpleDateFormat yearFormate = new SimpleDateFormat("yyyy");
                SimpleDateFormat monthFormate = new SimpleDateFormat("MM");
                SimpleDateFormat dayformate = new SimpleDateFormat("dd");

                Calendar calendar = new GregorianCalendar();
                //calendar.add(Calendar.DATE, 1);
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
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e("TAG", selectedHour + ":" + selectedMinute);
                        // eReminderTime.setText( selectedHour + ":" + selectedMinute);

                       /* if((selectedHour <= (mcurrentTime.get(Calendar.HOUR_OF_DAY)))&&
                                (selectedMinute <= (mcurrentTime.get(Calendar.MINUTE)))){
                            Toast.makeText(getActivity(), "You have selected past time",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else*/
                        if(selectedHour>=21) { //setting range of time for night

                            if (selectedHour>=21 && selectedHour<24){
                                startAlarmForNight(selectedHour, selectedMinute, claYear, clamonth, claday);
                            }
                        }
                        else if (selectedHour>=0 && selectedHour<6){
                            startAlarmForNight(selectedHour, selectedMinute, claYear, clamonth, claday);
                        }

                        else {
                            Toast.makeText(getActivity(), "Out Of Range...", Toast.LENGTH_SHORT).show();
                        }




                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();



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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForMorning);

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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForAfternoon);

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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForEvening);

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

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntentForNight);

    }






}

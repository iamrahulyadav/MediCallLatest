package themedicall.com;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PregnancyCalculator extends AppCompatActivity {

    Spinner monthSpinner , yearSpinner , daysSpinner ;
    String [] monthArray = {"January" , "February" , "March" , "April" , "May" , "June" , "July" , "August" , "September" , "October" , "November" , "December"};
    ArrayList<Integer> years = new ArrayList<>();
    ArrayList<Integer> days = new ArrayList<>();
    String monthName ;
    ArrayAdapter<String> monthAdapter ;
    ArrayAdapter<Integer> yearAdapter , daysAdapter ;
    int year , months , date;
    int checkMonth  = 0 , checkYear = 0;
    Button calculatePregnancyBtn ;
    Calendar calendar;
    public static final int WEEKS_IN_PREGNANCY = 40;
    public static final int DAYS_IN_PREGNANCY = WEEKS_IN_PREGNANCY * 7;
    String currentDate ;
    Date menstrualDate = null, expectedPregnancyDate = null ;
    Date currentDateFormat = null, spinnerDateSelectedFormat = null ;
    Date subtractedDate = null;

    SimpleDateFormat dateFormat;
    String selectedDateFromSpinner;
    String estimatedPregnancyDate;
    String spinnerDayText;
    String spinnerMonthText ;
    String spinnerYearText ;
    TextView pregnancyDisclaimer , deliveryDate , pregnancyWeek;
    LinearLayout deliveryDateLayout ;
    ProgressBar pregnancyProgressBar ;
    private int pStatus = 0;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnancy_calculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getCurrentDate();
        initiate();
        calculatePregnancyBtnClickListener();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initiate()
    {
        pregnancyDisclaimer = (TextView) findViewById(R.id.pregnancyDisclaimer);
        pregnancyWeek = (TextView) findViewById(R.id.pregnancyWeek);
        deliveryDate = (TextView) findViewById(R.id.deliveryDate);
        deliveryDateLayout = (LinearLayout) findViewById(R.id.deliveryDateLayout);
        pregnancyProgressBar = (ProgressBar) findViewById(R.id.pregnancyProgressBar);
        deliveryDateLayout.setVisibility(View.GONE);

        pregnancyDisclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pregnancyDisclaimerDialog();
            }
        });
    }

    public void getCurrentDate() {


        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        daysSpinner = (Spinner) findViewById(R.id.dateSpinner);
        calculatePregnancyBtn = (Button) findViewById(R.id.calculatePregnancyBtn);




        monthAdapter = new ArrayAdapter<String>(PregnancyCalculator.this , R.layout.spinner_list , R.id.spinnerList , monthArray);
        monthSpinner.setAdapter(monthAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(PregnancyCalculator.this, "selected month is : "+monthSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                int k = 0 ;

                if(++checkMonth > 1) {

                    days.clear();
                    int daysInMonth = calculateMonthDays(Integer.parseInt(yearSpinner.getSelectedItem().toString())  , monthSpinner.getSelectedItemPosition());

                    Log.e("tag" , "total days in month are : "+daysInMonth);
                   // Log.e("tag" , "total days in days spinner : "+Integer.parseInt(daysSpinner.getSelectedItem().toString()));

                    for (int j = 1; j <= daysInMonth; j++) {

                        days.add(j);

                        k = j;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        months = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DATE);
        monthName = monthArray[calendar.get(Calendar.MONTH)];

        currentDate = date+ " / " +monthName + " / " +year ;



        years.add(year);
        years.add(year-1);


        yearAdapter = new ArrayAdapter<Integer>(PregnancyCalculator.this , R.layout.spinner_list , R.id.spinnerList , years);
        yearSpinner.setAdapter(yearAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(++checkYear > 1) {

                    Log.e("tag", "selected year is on first time : " + yearSpinner.getSelectedItemPosition());

                    days.clear();
                    int daysInMonth = calculateMonthDays(Integer.parseInt(yearSpinner.getSelectedItem().toString())  , monthSpinner.getSelectedItemPosition());

                    Log.e("tag" , "total days in month are : "+daysInMonth);

                    for (int j = 1; j <= daysInMonth; j++) {
                        days.add(j);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(year, months , 1);
        // Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);


        for(int i = 1 ; i <= daysInMonth ; i++)
        {
            days.add(i);
        }


        daysAdapter = new ArrayAdapter<Integer>(PregnancyCalculator.this , R.layout.spinner_list , R.id.spinnerList , days);
        daysSpinner.setAdapter(daysAdapter);


        Log.e("tag", "total days in month are : " + daysInMonth);
        Log.e("tag", "get the current year : " + year);
        Log.e("tag", "get the current month : " + months);
        Log.e("tag", "get the current date : " + date);



        for(int i = 0 ;  i < monthArray.length ; i ++)
        {
            if(monthArray[i].equals(monthName))
            {
                Log.e("tag","data found at position : "+i);
                monthSpinner.setSelection(i);

                break;
            }
            else
            {
                Log.e("tag" , "month not found");
            }
        }


        for(int i = 0 ;  i < years.size() ; i ++)
        {
            if(years.get(i) == year)
            {
                Log.e("tag","data found at position : "+i);
                yearSpinner.setSelection(i);

                break;
            }
            else
            {
                Log.e("tag" , "year not found");
            }
        }



        for(int i = 0 ;  i < days.size() ; i ++)
        {
            if(days.get(i) == date)
            {
                Log.e("tag","data found at position : "+i);
                daysSpinner.setSelection(i);

                break;
            }
            else
            {
                Log.e("tag" , "date not found");
            }
        }
    }


    public int calculateMonthDays(int  year, int month)
    {
        Log.e("tag" , " in calculate Month Days year is : "+year);
        Log.e("tag" , " in calculate Month Days month is : "+month);

        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(year, month , 1);
        // Get the number of days in that month
        return mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }


    public void calculatePregnancyBtnClickListener()
    {
        dateFormat = new SimpleDateFormat("dd / MMMM / yyyy");


        calculatePregnancyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                spinnerDayText = daysSpinner.getSelectedItem().toString();
                spinnerMonthText = monthSpinner.getSelectedItem().toString();
                spinnerYearText = yearSpinner.getSelectedItem().toString();
                selectedDateFromSpinner = spinnerDayText+ " / " +spinnerMonthText+ " / " +spinnerYearText ;

                int compareValue = checkCurrentDateGraterThenSelectedDate(currentDate , selectedDateFromSpinner);

                Log.e("tag" , "compare value is : " +compareValue );

                Calendar subtractCalender = Calendar.getInstance();
                subtractCalender.add(Calendar.DATE , -DAYS_IN_PREGNANCY);
                Date subtractDaysFromCurrentDate = new Date(subtractCalender.getTimeInMillis());
                String subtractedDate = dateFormat.format(subtractDaysFromCurrentDate);
                Log.e("tag" , "subtracted date is : " +subtractedDate);

                int value = checkUserCannotSelectDateNineMonthBeforeDate(selectedDateFromSpinner , subtractedDate);

                Log.e("tag" , "check User Cannot Select Date Nine Month Before Date : "+value);



                if(compareValue == 1 && value == 1)
                {
                    pStatus = 0;
                    calendar = Calendar.getInstance();

                    try {
                        calendar.setTime(dateFormat.parse(selectedDateFromSpinner));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    calendar.add(Calendar.DATE, DAYS_IN_PREGNANCY);//insert the number of days you want to be added to the current date
                    Date resultDate = new Date(calendar.getTimeInMillis());
                    estimatedPregnancyDate = dateFormat.format(resultDate);

                    deliveryDateLayout.setVisibility(View.VISIBLE);
                    deliveryDate.setText(estimatedPregnancyDate);

                    long differenceDates = calculateDayFromTwoDates(selectedDateFromSpinner , estimatedPregnancyDate);
                    long remainingDaysTODelivery = calculateDayFromTwoDates(estimatedPregnancyDate , currentDate);
                    Log.e("tag" , "remaining day to delivery is  : " + Long.toString(remainingDaysTODelivery));

                    int totalDayOfPregnancy = 280 - (int)remainingDaysTODelivery;
                    //Convert long to String
                    String dayDifference = Long.toString(differenceDates);

                    final int totalWeek = totalDayOfPregnancy / 7;

                    Log.e("tag" , "week value is : " + totalWeek);


                    final int addOneInTotalWeek = totalWeek+1;


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (pStatus <= 100) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        pregnancyProgressBar.setProgress(pStatus);
                                        if(pStatus == 99)
                                        {
                                            pregnancyWeek.setText("Your are at " + addOneInTotalWeek + " week");
                                        }
                                    }
                                });
                                try {
                                    Thread.sleep(25);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                pStatus++;
                            }
                        }
                    }).start();


                    Log.e("tag" , "difference between two Dates is selected date to pregnancy date: " + dayDifference);
                    Log.e("tag" , "pregnancy date is : " + estimatedPregnancyDate);
                    Log.e("tag" , "total day of pregnancy is : " + totalDayOfPregnancy);
                    Log.e("tag" , "remaining days is pregnancy : " + remainingDaysTODelivery);
                    Log.e("tag" , "pregnancy total day is : " + dayDifference);
                    Log.e("tag" , "current date is : " + currentDate);
                    Log.e("tag" , "selected date from spinner is : "+selectedDateFromSpinner);
                }
                else
                {
                    selectPregnancyDateDialog();
                }



            }
        });
    }

    public long calculateDayFromTwoDates(String selectedDateFromSpinner, String estimatedPregnancyDate)
    {
        try {
            menstrualDate = dateFormat.parse(selectedDateFromSpinner);
            expectedPregnancyDate = dateFormat.parse(estimatedPregnancyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long findDifference = Math.abs(menstrualDate.getTime() - expectedPregnancyDate.getTime());
        long convertDifferenceToDays = findDifference / (24 * 60 * 60 * 1000);

        return convertDifferenceToDays;
    }



    public int checkCurrentDateGraterThenSelectedDate(String currentDate, String spinnerSelectedDate)
    {
        try {
            currentDateFormat = dateFormat.parse(currentDate);
            spinnerDateSelectedFormat = dateFormat.parse(spinnerSelectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date subtractDaysFromCurrentDate = new Date(currentDateFormat.getTime() - 7 * 24 * 3600 * 1000l);
        String subtractDateFormatConvertInString = dateFormat.format(subtractDaysFromCurrentDate);

        try {
            subtractedDate = dateFormat.parse(subtractDateFormatConvertInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e("tag" , "subtract seven days from current date is : "+subtractedDate);


        if (subtractedDate.before(spinnerDateSelectedFormat))
        {
            Log.e("tag" , "subtract date less then spinner date");

            return 0;
        }
        else
        {
            Log.e("tag" , "subtract date grater then spinner date");

            return 1;
        }
    }



    public int checkUserCannotSelectDateNineMonthBeforeDate(String spinnerSelectedDate, String subtractedDate)
    {
        Date spinnerDate = null , subtractDate = null;

        try {
             spinnerDate = dateFormat.parse(spinnerSelectedDate);
             subtractDate = dateFormat.parse(subtractedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if (spinnerDate.before(subtractDate))
        {
            Log.e("tag" , "selected date before nine month");

            return 0;
        }
        else
        {
            Log.e("tag" , "selected date within nine month");

            return 1;
        }
    }



    public void selectPregnancyDateDialog()
    {
        final Dialog dialog = new Dialog(PregnancyCalculator.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pregnancy_select_date_dialog);
        dialog.show();

        TextView cancelDialog = dialog.findViewById(R.id.cancel);
        TextView okDialog = dialog.findViewById(R.id.ok);


        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }


    public void pregnancyDisclaimerDialog()
    {
        final Dialog dialog = new Dialog(PregnancyCalculator.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pregnancy_disclaimer_dialog);
        dialog.show();

        TextView cancelDialog = dialog.findViewById(R.id.cancel);
        TextView okDialog = dialog.findViewById(R.id.ok);


        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        okDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


}

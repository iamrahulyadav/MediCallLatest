package themedicall.com;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import themedicall.com.BroadCasts.AlarmReceiver;
import themedicall.com.MedicineAlarm.MedAlarmDayFive;
import themedicall.com.MedicineAlarm.MedAlarmDayFour;
import themedicall.com.MedicineAlarm.MedAlarmDayOne;
import themedicall.com.MedicineAlarm.MedAlarmDaySeven;
import themedicall.com.MedicineAlarm.MedAlarmDaySix;
import themedicall.com.MedicineAlarm.MedAlarmDayThree;
import themedicall.com.MedicineAlarm.MedAlarmDayTwo;

public class MedAlarm extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    static String CurrentDay;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        floatingActionButton();
        initiate();



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


    public void floatingActionButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void initiate()
    {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(7);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);




        if(viewPager.getCurrentItem() == 0)
        {
            MedicineReminderDateFragment.instance(0 , MedAlarm.this );
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem() == 0)
                {
                    MedicineReminderDateFragment.instance(0 , MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 1)
                {
                    MedicineReminderDateFragment.instance(1 , MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 2)
                {
                    MedicineReminderDateFragment.instance(2 , MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 3)
                {
                    MedicineReminderDateFragment.instance(3 , MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 4)
                {
                    MedicineReminderDateFragment.instance(4, MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 5)
                {
                    MedicineReminderDateFragment.instance(5, MedAlarm.this);
                }
                if(viewPager.getCurrentItem() == 6)
                {
                    MedicineReminderDateFragment.instance(6, MedAlarm.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        DateFormat dateFormat = new SimpleDateFormat("EEEE dd-MMM-yyyy");

        for(int j = 0; j < 7; j++) {

            Date date = addDays(new Date(), +j);
            CurrentDay = dateFormat.format(date.getTime());

            adapter.addFragment(new MedicineReminderDateFragment(), CurrentDay);


        }


        viewPager.setAdapter(adapter);

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static Date addDays(Date d, int days)
    {
        d.setTime(d.getTime() + days * 1000L * 60L * 60L * 24L);
        return d;
    }
}
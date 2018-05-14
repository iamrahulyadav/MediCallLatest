package themedicall.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedTools extends AppCompatActivity {


    ImageView bmiCalculator , pregnancyCalculator , stepCounter , medAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_tools);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();
        clickListener();

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
        bmiCalculator = (ImageView) findViewById(R.id.bmiCalculator);
        pregnancyCalculator = (ImageView) findViewById(R.id.pregnancyCalculator);
        stepCounter = (ImageView) findViewById(R.id.stepCounter);
        medAlarm = (ImageView) findViewById(R.id.medAlarm);
    }

    public void clickListener()
    {
        bmiCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedTools.this , BMICalculator.class);
                startActivity(intent);
            }
        });


        pregnancyCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedTools.this , PregnancyCalculator.class);
                startActivity(intent);
            }
        });


        stepCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedTools.this , StepCounter.class);
                startActivity(intent);
            }
        });

        medAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedTools.this , MedAlarm.class);
                startActivity(intent);
            }
        });

    }




}

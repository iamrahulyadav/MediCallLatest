package themedicall.com;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import themedicall.com.Fragments.AlliedSignupFragment;
import themedicall.com.Fragments.DocterSignupFragment;
import themedicall.com.Fragments.MedicalStudentSignupFragment;
import themedicall.com.Globel.Utility;

public class MedicSignup extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    private RadioButton rd_doctor, rd_medical_studen, radio_alied;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_signup);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

    }

    private void init(){

        rd_doctor = (RadioButton) findViewById(R.id.rd_doctor);
        rd_medical_studen = (RadioButton) findViewById(R.id.rd_medical_studen);
        radio_alied = (RadioButton) findViewById(R.id.radio_alied);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        rd_doctor.setChecked(true);

        rd_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_doctor.setChecked(true);
                rd_medical_studen.setChecked(false);
                radio_alied.setChecked(false);
                viewPager.setCurrentItem(0);
            }
        });

        rd_medical_studen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_doctor.setChecked(false);
                rd_medical_studen.setChecked(true);
                radio_alied.setChecked(false);
                viewPager.setCurrentItem(1);
            }
        });

        radio_alied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_doctor.setChecked(false);
                rd_medical_studen.setChecked(false);
                radio_alied.setChecked(true);
                viewPager.setCurrentItem(2);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Log.e("TAG", "the positions of item is: " + position);
                if (position == 0){

                    rd_doctor.setChecked(true);
                    radio_alied.setChecked(false);
                    rd_medical_studen.setChecked(false);
                }
                if (position == 1){

                    rd_doctor.setChecked(false);
                    radio_alied.setChecked(false);
                    rd_medical_studen.setChecked(true);
                }
                if (position == 2){

                    rd_doctor.setChecked(false);
                    radio_alied.setChecked(true);
                    rd_medical_studen.setChecked(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        // tabLayout = (TabLayout) findViewById(R.id.tabs);
        /*tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(this).inflate(R.layout.tab_layout, tabLayout, false);

            RadioButton tabTextView = (RadioButton) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
            //tab.select();
        }

        TabLayout.Tab tab = tabLayout.getTabAt(0); // Count Starts From 0
        tab.select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int postion = tab.getPosition();
                Log.e("TAG", "the tap postion is: " + postion);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DocterSignupFragment(), "Doctor");
        adapter.addFragment(new MedicalStudentSignupFragment(), "Medical Student");
        adapter.addFragment(new AlliedSignupFragment(), "Allied");
        viewPager.setAdapter(adapter);


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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



}



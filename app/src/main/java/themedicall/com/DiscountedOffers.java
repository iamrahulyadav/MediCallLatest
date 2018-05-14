package themedicall.com;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiscountedOffers extends NavigationDrawer implements SearchView.OnQueryTextListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // Custom Action bar
    View customActionBarView ;
    Button locationFilter;
    ImageView userIcon , doctorFilterImage , searchViewImg ;
    RelativeLayout.LayoutParams params;
    SharedPreferences sharedPreferencesCity ;
    public static SharedPreferences.Editor cityEditor ;
    public static String  city;
    ListView cityListView;
    SearchView search_view;
    CustomCityNewAdapter customCityNewAdapter;
    SharedPreferences sharedPreferencesCityAndLatLng;
    public static SharedPreferences.Editor cityAndLatLngEditor;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_discounted_offers);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_discounted_offers, null, false);
        drawer.addView(view, 0);

        initiate();
        setDiscountedDoctorHospitalLabPharmacy();
        setImageInActionbar();
        SelectCity();
    }

    public void initiate()
    {
        networkDialog = new Dialog(DiscountedOffers.this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        searchViewImg.setVisibility(View.GONE);
        doctorFilterImage.setVisibility(View.GONE);
        params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        sharedPreferencesCity = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        city = sharedPreferencesCity.getString("city" , "0");
        locationFilter.setText(city);



    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DiscountedDoctorsForDiscountedOffer() , "Doctors");
        adapter.addFragment(new DiscoutedHospitalForDiscoutedOffer() , "Hospitals");
        adapter.addFragment(new DiscoutedLabsForDiscoutedOffer() , "Labs");
        adapter.addFragment(new DiscoutedPharmaciesForDiscoutedOffer() , "Pharmacies");
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

    public void setDiscountedDoctorHospitalLabPharmacy()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getCurrentItem() == 1)
                {

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void setImageInActionbar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);
                final String userTable = sharedPreferences.getString("usertable", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL+profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);


                Picasso.with(this).load(PROFILE_IMAGE_URL).placeholder(R.drawable.loginuser).transform(new CircleTransformPicasso()).into(userIcon);

                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (userTable.equals(getResources().getString(R.string.doctors))) {

                            Intent intent = new Intent(DiscountedOffers.this , UpdateDoctorProfile.class);
                            startActivity(intent);

                            //starting service for getting all data from server
                        }

                        if (userTable.equals(getResources().getString(R.string.patients))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }

                        if (userTable.equals(getResources().getString(R.string.labs))) {


                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }


                        if (userTable.equals(getResources().getString(R.string.hospitals))) {

                           /* Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.pharmacies))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.blood_donors))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.ambulances))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.health_professionals))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                    }
                });

            }
            else {
                imageView.setImageResource(R.drawable.loginuser);
                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DiscountedOffers.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }


    public void SelectCity()
    {

        locationFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DiscountedOffers.this);
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Select City");
                cityListView = (ListView) dialog.findViewById(R.id.cityList);
                search_view = (SearchView) dialog.findViewById(R.id.search_view);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                dialog.show();



                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                List cityList = databaseHelper.getAllPots();
                Log.e("TAG", "the city list count: " + databaseHelper.getCount());
                Log.e("TAG", "the city list from db: " + cityList.size());

                customCityNewAdapter = new CustomCityNewAdapter(getApplicationContext(), cityList);
                cityListView.setAdapter(customCityNewAdapter);
                search_view.setOnQueryTextListener(DiscountedOffers.this);


                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        //Toast.makeText(Home.this, "id "+City_id, Toast.LENGTH_SHORT).show();
                        sharedPreferencesCity = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
                        cityEditor = sharedPreferencesCity.edit();

                        cityEditor.putString("city" , City);
                        cityEditor.commit();
                        locationFilter.setText(City);
                        dialog.dismiss();

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                    }
                });

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        customCityNewAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        myReceiver = new MyReceiverForNetworkDialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onStart();
    }


    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onResume();
    }

    public void networkChange(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new CheckConnectivity(), intentFilter);
    }


    public class MyReceiverForNetworkDialog extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub

            int datapassed = intent.getIntExtra("DATAPASSED", 0);


            if (datapassed == 1234){


                networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                networkDialog.setContentView(R.layout.internet_connection_dialog);
                networkDialog.setCancelable(false);
                TextView enable = (TextView) networkDialog.findViewById(R.id.enable);
                TextView exit = (TextView) networkDialog.findViewById(R.id.exit);
                networkDialog.show();

                enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(i);
                    }
                });


                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }

            if(datapassed == 12345)
            {
                Log.e("tag", "data passed form check connectivity : "+datapassed);
                networkDialog.dismiss();
            }

        }
    }



}

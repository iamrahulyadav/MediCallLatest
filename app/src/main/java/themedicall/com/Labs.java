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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.ChugtaiLabFilterAdapter;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.Adapter.LabsListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.ChugtaiLabGetterSetter;
import themedicall.com.GetterSetter.LabsGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;

import java.util.ArrayList;
import java.util.List;

public class Labs extends NavigationDrawer implements SearchView.OnQueryTextListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // Custom Action bar
    View customActionBarView ;
    Button locationFilter;
    ImageView userIcon  , doctorFilterImage , searchViewImg;
    RelativeLayout.LayoutParams params;


    StringRequest strReq;
    String cancel_req_tag = "Hospitals";
    private static final String TAG = "Hospitals";
    String lab_name , lab_address , lab_img , lab_id , lab_contact , lab_offer_discount;
    Double lab_lat , lab_lng;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    public static SharedPreferences.Editor cityAndLatLngEditor;
    Double currentLat , currentLang;
    LatLng currentLatLang ;

    LabsListRecycleView labsListRecycleView;

    List<LabsGetterSetter> nearByLabsList;
    List<LabsGetterSetter> nextNearByLabsList;
    List<LabsGetterSetter> allLabsList;
    List<LabsGetterSetter> nextAllLabsList;

    public boolean allLabsServiceHasRun = false;
    public boolean nearbyLabsServiceHasRun = false;


    private int totalLab;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    SharedPreferences sharedPreferences ;
    public static String city;

    ListView cityListView;
    SearchView search_view;
    CustomCityNewAdapter customCityNewAdapter;
    public static int totalAmount = 0;
    public static int totalAmountAfterRemove = 0;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_labs);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_labs, null, false);
        drawer.addView(view, 0);

        initiate();
        SelectCity();
        customActionBarChecksAndAlsoCallNearbyAndAllLabsWebservice();
        setImageInActionbar();
        networkChange();
        //removePreviousView();
    }

    public void initiate()
    {

        networkDialog = new Dialog(Labs.this);

        nearByLabsList = new ArrayList<>();
        allLabsList = new ArrayList<>();

        dialog=new CustomProgressDialog(Labs.this, 1);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        //params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();


        sharedPreferencesCityAndLatLng = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);
        cityAndLatLngEditor = sharedPreferencesCityAndLatLng.edit();

        //labTestTotalAmount.setText(String.valueOf(totalAmount));

        sharedPreferences = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        city = sharedPreferences.getString("city" , "0");
        locationFilter.setText(city);


        if(viewPager.getCurrentItem() == 0)
        {
            params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            locationFilter.setVisibility(View.GONE);
            doctorFilterImage.setVisibility(View.GONE);
            searchViewImg.setVisibility(View.GONE);
        }

    }

    public void customActionBarChecksAndAlsoCallNearbyAndAllLabsWebservice()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem() == 0)
                {
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    locationFilter.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    searchViewImg.setVisibility(View.GONE);
                }
                else if (viewPager.getCurrentItem() == 1)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , 0);

                }
                else if (viewPager.getCurrentItem() == 2)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , 0);

                }
                else if (viewPager.getCurrentItem() == 3)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT , 0);

                }



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderLabsFragment(), "Book Lab Test");
        adapter.addFragment(new NearByLabsFragment(), "Nearby");
        adapter.addFragment(new AllLabsFragment(), "All Labs");
        adapter.addFragment(new DiscountedLab(), "Discounts");
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


    public void onAddFieldLabButton(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.ad_book_lab_test_custom_row, null);

        // Add the new row before the add field button.
        int totalViewCount = OrderLabsFragment.parentLayoutForLabTest.getChildCount();
        Log.e("tag" , "total view count in on add fields button "+totalViewCount);
        OrderLabsFragment.parentLayoutForLabTest.addView(rowView, OrderLabsFragment.parentLayoutForLabTest.getChildCount() - totalViewCount);
        Log.d("tag" , "Press Delete Btn " + OrderLabsFragment.parentLayoutForLabTest.getChildCount());


        if (OrderLabsFragment.parentLayoutForLabTest.getChildCount()>1){

            for (int i = 0; i<totalViewCount ; i++) {



                View view =  OrderLabsFragment.parentLayoutForLabTest.getChildAt(i);
                final AutoCompleteTextView bookLabTestAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.bookLabTestAutoComplete);
                final TextView lab_id_tv = (TextView) view.findViewById(R.id.lab_id_tv);
                final TextView id_tv = (TextView) view.findViewById(R.id.id_tv);
                final TextView lab_test_fee_tv = (TextView) view.findViewById(R.id.lab_test_fee_tv);
                final LinearLayout priceLayout = (LinearLayout) view.findViewById(R.id.priceLayout);
                final ChugtaiLabFilterAdapter chugtaiLabFilterAdapter = new ChugtaiLabFilterAdapter(Labs.this , OrderLabsFragment.cugtaiLabTestList);
                bookLabTestAutoComplete.setAdapter(chugtaiLabFilterAdapter);
                Log.e("TAg","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

               // hospitalNameTextChangeListener(editText, hostId);

                bookLabTestAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        ChugtaiLabGetterSetter chugtaiLabGetterSetter = (ChugtaiLabGetterSetter) chugtaiLabFilterAdapter.getItem(i);
                        String lab_id = chugtaiLabGetterSetter.getLab_test_id();
                        String id = chugtaiLabGetterSetter.getId();
                        String price = chugtaiLabGetterSetter.getLab_test_rate();
                        String name = chugtaiLabGetterSetter.getLab_test_name();
                        bookLabTestAutoComplete.setText(name);
                        lab_test_fee_tv.setText(price);
                        priceLayout.setVisibility(View.VISIBLE);

                        Log.e("TAG", "my selected chugtai lab id : " + lab_id);
                        Log.e("TAG", "my selected chugtai id : " + id);
                        Log.e("TAG", "my selected chugtai price : " + price);


                        totalAmount = totalAmount += Integer.parseInt(price) ;
                        Log.e("TAG", "my selected chugtai total amount : " + totalAmount);
                        OrderLabsFragment.labTestTotalAmount.setText(String.valueOf(totalAmount));
                    }

                });
            }
        }
    }

    public void onDeleteLabCross(View v) {

        int totalViewCount = OrderLabsFragment.parentLayoutForLabTest.getChildCount();


        if(totalViewCount == 1)
        {
            Toast.makeText(this, "No More Item to Remove", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.e("tag" , "total Amount in on Delete : "+totalViewCount);

            OrderLabsFragment.parentLayoutForLabTest.removeView((View) (v).getParent());
            Log.d("tag" , "Press Delete Btn");

            for (int i = 0; i<totalViewCount-1 ; i++) {

                View view = OrderLabsFragment.parentLayoutForLabTest.getChildAt(i);
                final TextView lab_test_fee_tv = (TextView) view.findViewById(R.id.lab_test_fee_tv);

                String feeText = lab_test_fee_tv.getText().toString();
                Log.e("tag" , "fee at each index : "+feeText);

                totalAmountAfterRemove = totalAmountAfterRemove += Integer.parseInt(feeText) ;
                Log.e("TAG", "my selected chugtai total amount in onDelete : " + totalAmountAfterRemove);

                OrderLabsFragment.labTestTotalAmount.setText(String.valueOf(totalAmountAfterRemove));

            }

            totalAmount = totalAmountAfterRemove ;
            totalAmountAfterRemove = 0 ;
            Log.e("tag" , "total amount after minus "+totalAmount);



        }

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



    public void SelectCity()
    {

        locationFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Labs.this);
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
                search_view.setOnQueryTextListener(Labs.this);


                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        //Toast.makeText(Home.this, "id "+City_id, Toast.LENGTH_SHORT).show();

                        cityAndLatLngEditor.putString("city" , City);
                        cityAndLatLngEditor.commit();
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

                            Intent intent = new Intent(Labs.this , UpdateDoctorProfile.class);
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
                        Intent intent = new Intent(Labs.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {

        totalAmount = 0 ;
        totalAmountAfterRemove = 0 ;
        super.onBackPressed();
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






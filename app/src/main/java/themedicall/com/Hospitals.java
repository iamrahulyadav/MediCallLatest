package themedicall.com;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.Adapter.HospitalFilterAdapter;
import themedicall.com.Adapter.HospitalsListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.GetterSetter.HospitalSearchFilterGetterSetter;
import themedicall.com.GetterSetter.HospitalsGetterSetter;
import themedicall.com.GetterSetter.SuggestionGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class Hospitals extends NavigationDrawer implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    RelativeLayout.LayoutParams params;
    ProgressBar searchViewProgress ;
    AutoCompleteTextView autoCompleteSearch ;
    FrameLayout autoCompleteSearchLayout ;
    Button ClearSearchBtn ;
    SearchView search_view ;
    CustomCityNewAdapter customCityNewAdapter;
    SharedPreferences sharedPreferencesCity ;
    public static SharedPreferences.Editor cityEditor ;
    ListView cityListView ;
    ArrayList<HospitalSearchFilterGetterSetter> HospitalNameFilter ;
    ArrayList<String> errorNotFound ;
    private Timer timer;
    JSONObject object;
    JSONArray hospitalArray;
    boolean nearByHospitalsServiceHasRun = false;
    boolean discountedHospitalsServiceHasRun = false;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;


    List<HospitalsGetterSetter> nexPageList;
    List<HospitalsGetterSetter> hospitalsList;
    List<HospitalsGetterSetter> discountedHospitalsList;
    ArrayList<HospitalMultipleDocGetterSetter> hospitalDocList;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    android.support.v7.widget.SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    ImageView searchViewImg  ,  doctorFilterImage ;
    View customActionBarView ;
    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject docObject;
    JSONArray basicInfoArray;
    private static final String TAG = "Hospitals";
    String  hospital_id , hospital_name  , hospital_addr , hospital_address ,  hospital_img  ,   hospital_landline_number , doctor_id , doctor_full_name , doctor_image , hospital_views , hospital_share_url , hospital_offer_any_discount;
    Double hospital_lat , hospital_lng;
    String hospitalId , hospitalName ;
    HospitalsGetterSetter dm ;
    public static String  city;
    StringRequest strReq;
    String cancel_req_tag = "Hospitals";
    public static ArrayList<SuggestionGetterSetter> HospitalNameList = new ArrayList<SuggestionGetterSetter>();



    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    private int totalDataOnServer;
    HospitalsListRecycleView hospitalsListRecycleView ;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    private int timerHandler = 2;
    Handler mHandler;
    private String textToSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hospitals);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);




        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_hospitals, null, false);
        drawer.addView(view, 0);

        initiate();
        getNearByAndDiscountHospitals();
        handleSearchView();
        SelectCity();
        setImageInActionbar();

    }

    public void initiate()
    {

        dialog=new CustomProgressDialog(Hospitals.this, 1);
        networkDialog = new Dialog(Hospitals.this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        HospitalNameFilter = new ArrayList<HospitalSearchFilterGetterSetter>();
        errorNotFound = new ArrayList<>();

        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        doctorFilterImage.setVisibility(View.GONE);
        params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        autoCompleteSearch = (AutoCompleteTextView) customActionBarView.findViewById(R.id.autoCompleteSearch);
        //autoCompleteSearch.setThreshold(4);
        hospitalsList = new ArrayList<>();
        discountedHospitalsList = new ArrayList<>();
        autoCompleteSearchLayout = (FrameLayout) findViewById(R.id.autoCompleteSearchLayout);
        ClearSearchBtn = (Button) findViewById(R.id.ClearSearchBtn);
        searchViewProgress = (ProgressBar) customActionBarView.findViewById(R.id.searchViewProgress);

        sharedPreferencesCity = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        city = sharedPreferencesCity.getString("city" , "0");
       // Toast.makeText(this, "city "+city, Toast.LENGTH_SHORT).show();
        locationFilter.setText(city);


        sharedPreferencesCityAndLatLng = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllHospitals(), "All Hospitals");
        adapter.addFragment(new NearbyHospitals(), "Nearby Hospitals");
        adapter.addFragment(new DiscountedHospitals(), "Discounts");
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


    public void getNearByAndDiscountHospitals()
    {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem() == 0)
                {
                    //handle in all Hospital fragment
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void  handleSearchView()
    {
        searchViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationFilter.setVisibility(View.GONE);
                userIcon.setVisibility(View.GONE);
                searchViewImg.setVisibility(View.GONE);
                autoCompleteSearchLayout.setVisibility(View.VISIBLE);

                autoCompleteSearch.setFocusableInTouchMode(true);
                autoCompleteSearch.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(autoCompleteSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ClearSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(autoCompleteSearch.getText().toString().equals(""))
                {
                    locationFilter.setVisibility(View.VISIBLE);
                    userIcon.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    autoCompleteSearchLayout.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteSearch.getWindowToken(), 0);
                }
                else
                {
                    autoCompleteSearch.setText("");
                }
            }
        });


        autoCompleteSearch.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(final Editable editable) {



                if(editable.toString().length() >= 3)
                {

                    Log.e("tag" , "text "+editable.toString());
                    Log.e("tag" , "web service call "+editable.toString());
                    HospitalNameFilter.clear();
                    useHandler(editable.toString());
                    //getHospitalsNameFilter(editable.toString());

                }


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


        });

        autoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    public void SelectCity()
    {

        locationFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Hospitals.this);
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
                search_view.setOnQueryTextListener(Hospitals.this);

                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        //Toast.makeText(Hospitals.this, "id "+City_id, Toast.LENGTH_SHORT).show();

                        sharedPreferencesCity = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
                        cityEditor = sharedPreferencesCity.edit();

                        cityEditor.putString("city" , City);
                        cityEditor.commit();
                        locationFilter.setText(City);
                        dialog.dismiss();

                        int pos = viewPager.getCurrentItem();
                        Log.e("tag" , "view pager location in select city : "+pos);


                        Intent intent = getIntent();
                        finish();
                        Log.e("tag" , "view pager location in select city after activity finish : "+pos);

                        startActivity(intent);
                        viewPager.setCurrentItem(pos);
                        Log.e("tag" , "view pager location in select city after activity start : "+pos);


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


    public void getHospitalsNameFilter(final String filterName)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospital Name Filter";

        searchViewProgress.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.GET_HOS_NAME_FILTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Hospital Filter Response: " + response.toString());
                searchViewProgress.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        object = new JSONObject(response);


                        hospitalArray = jObj.getJSONArray("hospital_names");

                        for (int i = 0; i < hospitalArray.length(); i++) {

                            JSONObject practiceObject = hospitalArray.getJSONObject(i);

                            hospital_id = practiceObject.getString("hospital_id");
                            hospital_name = practiceObject.getString("hospital_name");
                            hospital_address = practiceObject.getString("hospital_addr");

                            HospitalNameFilter.add(new HospitalSearchFilterGetterSetter(hospital_id , hospital_name , hospital_address));
                        }

                        if(HospitalNameFilter.size() != 0)

                        {

                            HospitalFilterAdapter hospitalFilterAdapter = new HospitalFilterAdapter(getApplicationContext(), HospitalNameFilter);
                            autoCompleteSearch.setAdapter(hospitalFilterAdapter);
                            hospitalFilterAdapter.notifyDataSetChanged();
                            autoCompleteSearch.showDropDown();


                        }
                        else
                        {
//                            String errorMsg = jObj.getString("error_message");
//                            errorNotFound.add(errorMsg);

                            Toast.makeText(Hospitals.this, "Record not found", Toast.LENGTH_SHORT).show();
                        }

                        Log.e("tag" , "hospital filter size size "+HospitalNameFilter.size());
                        // Log.e("tag" , "hospital Name and Id "+hashMap.toString());

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");

                        //Toast.makeText(getApplicationContext() , "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Hospital Filter Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                searchViewProgress.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("city", city);
                params.put("name", filterName);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()) .addToRequestQueue(strReq, cancel_req_tag);
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

                            Intent intent = new Intent(Hospitals.this , UpdateDoctorProfile.class);
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
                        Intent intent = new Intent(Hospitals.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", " Calls");
            timerHandler--;
           /* mHandler = new Handler();
            mHandler.postDelayed(this, 1000);*/

            Log.e("TAG", "runnin timmer is: " + timerHandler);

            if (timerHandler == 0){


                Log.e("TAG", "Seached Text Is: " + timerHandler);
                mHandler.removeCallbacks(mRunnableStartMainActivity);
                timerHandler = 2;
                if (textToSearch.length()>=3) {
                    getHospitalsNameFilter(textToSearch);
                }



            }
        }
    };


    //handler for the starign activity

    public void useHandler(String text) {

        textToSearch = text;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnableStartMainActivity, 1000);

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

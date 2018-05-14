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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.Adapter.DoctorsFilterAdapter;
import themedicall.com.Adapter.FindDoctorListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.DoctorSearchFilterGetterSetter;
import themedicall.com.GetterSetter.FindDoctorGetterSetter;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.PaginationScrollListener;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FindDoctor extends NavigationDrawer implements SearchView.OnQueryTextListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String speciality_id;
    public static String speciality_name;
    Button locationFilter;
    ImageView userIcon , doctorFilterImage  , searchViewImg;
    RelativeLayout.LayoutParams params;
    ProgressBar searchViewProgress ;

    View customActionBarView;
    public static String city;
    SharedPreferences sharedPreferencesCity;
    public static SharedPreferences.Editor cityEditor;
    ListView cityListView;
    SearchView search_view;
    CustomCityNewAdapter customCityNewAdapter;
    JSONObject object;
    JSONArray hospitalArray;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject innerQualificationObject;
    JSONObject hospitalListObject;
    JSONArray basicInfoArray;
    private final String TAG = "NearbyDocList";
    public static String  doctor_id ,  doctor_offer_any_discount , doctor_full_name  , doctor_experience , doctor_mobile , doctor_min_fee , docor_max_fee , doctor_img , doctor_status , doctor_qualification , hospital_id , hospital_name , hospital_image ,  doctor_verified_status , doctor_profile_percent , doctor_views , doctor_url ,  hospital_landline_id ,  hospital_landline_number , speciality_designation;
    Double  hospital_lat ,hospital_lng ;
    FindDoctorGetterSetter findDoctorGetterSetter ;
    List<FindDoctorGetterSetter> nexPageList;
    public List<FindDoctorGetterSetter> nearDocList;
    List<FindDoctorGetterSetter> discountedDocList;
    ArrayList<HospitalMultipleDocGetterSetter> docHospitalList;
    ArrayList qualificationList;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    ArrayList<HospitalLandLineListGetterSetter> tempLandLineList;
    String tempList , tempName;
    FindDoctorListRecycleView findDoctorListRecycleView ;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    private int totalDataOnServer;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    Double rating ;
    boolean nearByDoctorServiceHasRun = false;
    boolean discountedDoctorServiceHasRun = false;
    Double NearbyDocDistanceINKM ;
    double roundAboutKm ;
    FrameLayout autoCompleteSearchLayout ;
    Button ClearSearchBtn ;
    private int timerHandler = 2;
    Handler mHandler;
    private String textToSearch;

    String[] fee_range = {"Select Fee Range" , "100-500" , "500-1000" , "1000-2000" , "2000-3000" , "3000-5000"};
    String[] experience_range = {"Select Experience Range" , "0-5" , "5-10" , "10-15" , "15-20" , "20-25" , "25-30" , "30-35" , "35-40"};

    //doc filter dialog

    String getTextOfRadio ;
    RadioGroup GenderRadioGroup ;
    RadioButton GenderMaleOrFemale , GenderMale , GenderFeMale ;
    RatingBar doctorFilterRating ;
    Spinner doctorFilterFeeRange , doctorFilterExperienceRange ;
    SeekBar doctorRadiosSeekBar ;
    Button doctorFilterSubmit ;
    ArrayList<DoctorSearchFilterGetterSetter> doctorNameFilter ;
    ArrayList specialityDesignationList;
    AutoCompleteTextView autoCompleteSearch ;


    //end doc filter dialog




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_doctor);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView = getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_find_doctor, null, false);
        drawer.addView(view, 0);


        initiate();
        handleSearchView();
        docFilterDialog();
        getNearByAndDiscountDoctor();
        SelectCity();
        getSpecialityId();
        setImageInActionbar();
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
                    doctorNameFilter.clear();
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






    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllDoctorFragment(), "All Drs.");
        adapter.addFragment(new NearByDoctorFragment(), "Nearby Drs.");
        adapter.addFragment(new DiscountdDoctorFragment(), "Discounts");
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

    public void initiate()
    {

        networkDialog = new Dialog(FindDoctor.this);
        dialog=new CustomProgressDialog(FindDoctor.this, 1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();
       // NearByDoctorFragment.recyclerView_nearBy_Doc = (RecyclerView) findViewById(R.id.recycler_view_nearby_doc);
       // NearByDoctorFragment.recyclerView_nearBy_Doc.setHasFixedSize(true);
//        linearLayoutManager = new LinearLayoutManager(FindDoctor.this , LinearLayoutManager.VERTICAL, false);
//        NearByDoctorFragment.recyclerView_nearBy_Doc.setLayoutManager(linearLayoutManager);
       // NearByDoctorFragment.recyclerView_nearBy_Doc.setAdapter(findDoctorListRecycleView);
        nearDocList = new ArrayList<>();
        discountedDocList = new ArrayList<>();

        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        doctorFilterImage.setVisibility(View.GONE);
        params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        autoCompleteSearchLayout = (FrameLayout) findViewById(R.id.autoCompleteSearchLayout);
        ClearSearchBtn = (Button) findViewById(R.id.ClearSearchBtn);
        searchViewProgress = (ProgressBar) customActionBarView.findViewById(R.id.searchViewProgress);


        //searchView = (SearchView) customActionBarView.findViewById(R.id.searchView);
        sharedPreferencesCity = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        city = sharedPreferencesCity.getString("city" , "0");
        locationFilter.setText(city);

        sharedPreferencesCityAndLatLng = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);
        doctorNameFilter = new ArrayList<DoctorSearchFilterGetterSetter>();
        autoCompleteSearch = (AutoCompleteTextView) customActionBarView.findViewById(R.id.autoCompleteSearch);

    }

    public void docFilterDialog()
    {
        doctorFilterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(FindDoctor.this);
                dialog.setContentView(R.layout.doctor_filter_layout);


                GenderRadioGroup = (RadioGroup) dialog.findViewById(R.id.GenderRadioGroup);
                GenderMale = (RadioButton) dialog.findViewById(R.id.GenderMale);
                GenderFeMale = (RadioButton) dialog.findViewById(R.id.GenderFeMale);
                doctorFilterRating = (RatingBar) dialog.findViewById(R.id.doctorFilterRating);
                doctorFilterFeeRange = (Spinner) dialog.findViewById(R.id.doctorFilterFeeRange);
                doctorFilterExperienceRange = (Spinner) dialog.findViewById(R.id.doctorFilterExperienceRange);
                doctorRadiosSeekBar = (SeekBar) dialog.findViewById(R.id.doctorRadiosSeekBar);
                doctorFilterSubmit = (Button) dialog.findViewById(R.id.doctorFilterSubmit);


                doctorFilterRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                    }
                });


                ArrayAdapter<String> feeAdapter = new ArrayAdapter<String>(FindDoctor.this, R.layout.spinner_list , R.id.spinnerList , fee_range);
                doctorFilterFeeRange.setAdapter(feeAdapter);


                ArrayAdapter<String> experienceAdapter = new ArrayAdapter<String>(FindDoctor.this, R.layout.spinner_list , R.id.spinnerList , experience_range);
                doctorFilterExperienceRange.setAdapter(experienceAdapter);

                doctorRadiosSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                        Toast.makeText(getApplicationContext(),"onProgressChanged : "+i * 5 + " KM", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {


                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                    }
                });


                doctorFilterSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int selectedRadioButtonID = GenderRadioGroup.getCheckedRadioButtonId();

                        Log.e("tag" , "radio group id " + selectedRadioButtonID);
                        // If nothing is selected from Radio Group, then it return -1
                        if (selectedRadioButtonID != -1) {

                            RadioButton selectedRadioButton = (RadioButton) dialog.findViewById(selectedRadioButtonID);
                            String selectedRadioButtonText = selectedRadioButton.getText().toString();

                            Log.e("tag" , "filter dialog gender : "+selectedRadioButtonText);
                        }
                        else{
                            Log.e("tag" , "filter dialog gender null ");
                        }

                        Log.e("tag" , "filter dialog rating : " + doctorFilterRating.getRating());
                        Log.e("tag" , "filter dialog fee range : " + doctorFilterFeeRange.getSelectedItem().toString());
                        Log.e("tag" , "filter dialog experience range : " + doctorFilterExperienceRange.getSelectedItem().toString());
                        Log.e("tag" , "filter dialog radios seek bar : " + doctorRadiosSeekBar.getProgress());
                    }
                });





                dialog.show();

            }
        });
    }


    public void getSpecialityId()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {

            speciality_id=(String)bd.get("Speciality_id");
            speciality_name=(String)bd.get("Speciality_name");
            //Toast.makeText(this, "Speciality_name "+speciality_name, Toast.LENGTH_SHORT).show();
        }
    }
    public void getNearByAndDiscountDoctor()
    {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(viewPager.getCurrentItem() == 0)
                {
                    //handle in all doctor fragment
                }
//                else if(viewPager.getCurrentItem() == 1)
//                {
//                    if(nearByDoctorServiceHasRun)
//                    {
//                        Log.e("tag" , "nearby doctor service all ready Run");
//                    }
//                    else
//                    {
//                       getNearbyDocList();
//                    }
//                    nearbyDocPaginationScrollListener();
//
//                }

//                else if(viewPager.getCurrentItem() == 2)
//                {
//
//                    if(discountedDoctorServiceHasRun)
//                    {
//                        Log.e("tag" , "discounted doctor service all ready Run");
//                    }
//                    else
//                    {
//                        getDiscountedDocList();
//                    }
//                    discountDocPaginationScrollListener();
//
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void SelectCity()
    {

        locationFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FindDoctor.this);
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
                search_view.setOnQueryTextListener(FindDoctor.this);

                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        //Toast.makeText(FindDoctor.this, "id "+City_id, Toast.LENGTH_SHORT).show();

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


    //start nearby listing code

    public void getNearbyDocList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocList";

        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loadingImage.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Near by Doctor Response: " + response.toString());
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            nexPageList = new ArrayList<>();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");


                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


                            JSONArray ratingArray = basicInfoObject.getJSONArray("ratings");
                            Log.e("tag" , "rating array size "+ratingArray.length());
                            if(ratingArray.length()>0) {

                                JSONObject ratingObject = ratingArray.getJSONObject(0);
                                rating = ratingObject.getDouble("rating");

                                Log.e("tag", "rating rating  : " + rating);

                            }
                            else
                            {
                                rating = 0.0;
                            }
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);

                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");


                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }


                                    Log.e("tag", "hospital number size nearby : " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size nearby abc : " + tempLandLineList.size());

                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));

                                }


                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, NearbyDocLocation);


                                    NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000;

                                    roundAboutKm = round(NearbyDocDistanceINKM, 1);

                                }



                            }
                            Log.e("tag" , "Nearby hospital in distance "+roundAboutKm);
                            Log.e("tag" , "Nearby hospital in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);
                            nearDocList.add(findDoctorGetterSetter);

                        }


                        if(nearDocList.size() == 0)
                        {
                            NearByDoctorFragment.emptyViewNearByDoc.setVisibility(View.VISIBLE);
                            NearByDoctorFragment.recyclerView_nearBy_Doc.setVisibility(View.GONE);
                        }
                        else
                        {
                            findDoctorListRecycleView = new FindDoctorListRecycleView(FindDoctor.this , nearDocList);
                            NearByDoctorFragment.recyclerView_nearBy_Doc.setAdapter(findDoctorListRecycleView);
                            findDoctorListRecycleView.notifyDataSetChanged();
                        }


                        nearByDoctorServiceHasRun = true ;

                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(FindDoctor.this, "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                Toast.makeText(FindDoctor.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();

                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(FindDoctor.this) .addToRequestQueue(strReq, cancel_req_tag);
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    public void getNearbyDocListNext(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocList";

        //progressDialog.setMessage("Adding you ...");

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Doctor Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");



                        if (nexPageList.size()>1){
                            nexPageList.clear();
                        }

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();



                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");

                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


                            JSONArray ratingArray = basicInfoObject.getJSONArray("ratings");
                            Log.e("tag" , "rating array size "+ratingArray.length());
                            if(ratingArray.length()>0) {

                                JSONObject ratingObject = ratingArray.getJSONObject(0);
                                rating = ratingObject.getDouble("rating");

                                Log.e("tag", "rating rating  : " + rating);

                            }
                            else
                            {
                                rating = 0.0;
                            }
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);


                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");


                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }


                                    Log.e("tag", "hospital number size nearby: " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size nearby abc : " + tempLandLineList.size());

                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));

                                }


                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    //Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(Home.userCurrentLocation, NearbyDocLocation);

                                    Double distance = CalculationByDistance(currentLatLang, NearbyDocLocation);


                                    // Double NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000 ;

                                    roundAboutKm = round(distance, 1);

                                }




                            }

                            Log.e("tag" , "Nearby hospital in distance "+roundAboutKm);
                            //Log.e("tag" , "Nearby hospital in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);

                            //topDocList.add(findDoctorGetterSetter);

                            nexPageList.add(findDoctorGetterSetter);

                        }

                        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(nexPageList.size(), nexPageList);
                        findDoctorListRecycleView.addAll(loadNextData);
                        findDoctorListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                       // callBack.onSuccess(nexPageList);

                        if (NearByDoctorFragment.bar.getVisibility() == View.VISIBLE){
                            NearByDoctorFragment.bar.setVisibility(View.GONE);
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(FindDoctor.this , "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                Toast.makeText(FindDoctor.this , error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("offset", offset);
                params.put("lat", String.valueOf(Home.lat));
                params.put("lng", String.valueOf(Home.lang));
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(FindDoctor.this) .addToRequestQueue(strReq, cancel_req_tag);

        //return topDocList;
    }
    //

    // onPagination scroll listener
    public void nearbyDocPaginationScrollListener(){

        NearByDoctorFragment.recyclerView_nearBy_Doc.addOnScrollListener(new PaginationScrollListener(NearByDoctorFragment.linearLayoutManager, totalDataOnServer) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextNearbyPage(offset);

                //loadNextPage();
                //getBloodGroupListingService()
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.e("TAG", "I AM CALLING 123: ");


                if (isLoadingFinished){


                }




            }

            @Override
            public int getTotalPageCount() {

                Log.e("TAG", "Total Data count: " +totalDataOnServer );
                Log.e("TAG", "Total Data count layout: " +NearByDoctorFragment.linearLayoutManager.getItemCount() );
                if (indicator == 1){

                    return TOTAL_PAGES;
                }
                else {
                    return 0;
                }

                //return TOTAL_PAGES;
                // return (isLoadingFinished) ? TOTAL_PAGES : 0;
                //return 0;
            }

            @Override
            public boolean isLastPage() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;




            }

            @Override
            public boolean isLoading() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;




            }
        });
    }

    //


    //
    private void loadNextNearbyPage(String offset) {
        Log.d(TAG, "loadNextPage: " + currentPage);




        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        getNearbyDocListNext(offset);



/*
        // List<BloodReqiredGetterSetter> myCurrentArrayList = gettingNextPageForData();


      //  Log.e("TAG", "MY Current 123: " + myCurrentArrayList.size());


        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(myCurrentArrayList.size(), myCurrentArrayList);

        Log.e("TAG", "MY Current: " + loadNextData.size());
        Log.e("TAG", "MY Current list list: " + myCurrentArrayList.size());


        loadNextData.subList(loadNextData.size() - myCurrentArrayList.size(), loadNextData.size()).clear();

        //List<BloodReqiredGetterSetter> currntList = loadNextData.subList(loadNextData.size()-20, loadNextData.size()).clear();
        Log.e(TAG, "MY Current Total Item Count abc : " + myCurrentArrayList.size());
*/


     /*   findDoctorListRecycleView.removeLoadingFooter();

            isLoading = false;


        findDoctorListRecycleView.addAll(loadNextData);


            if (currentPage != TOTAL_PAGES) {
                findDoctorListRecycleView.addLoadingFooter();


            } else {
                isLastPage = true;

            }*/

        if (NearByDoctorFragment.bar.getVisibility()== View.GONE){
            NearByDoctorFragment.bar.setVisibility(View.VISIBLE);
        }


    }

    // end of nearby doc code







    // start discount doc code



    public void getDiscountedDocList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Discounted Doc List";

        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loadingImage.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DISCOUNTED_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Discounted doc Response: " + response.toString());
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            nexPageList = new ArrayList<>();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();


                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");

                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


                            JSONArray ratingArray = basicInfoObject.getJSONArray("ratings");
                            Log.e("tag" , "rating array size "+ratingArray.length());
                            if(ratingArray.length()>0) {

                                JSONObject ratingObject = ratingArray.getJSONObject(0);
                                rating = ratingObject.getDouble("rating");

                                Log.e("tag", "rating rating  : " + rating);

                            }
                            else
                            {
                                rating = 0.0;
                            }
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);



                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");

                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }

                                    Log.e("tag", "hospital number size : " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size abc: " + tempLandLineList.size());


                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));

                                }

                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, NearbyDocLocation);


                                    NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000;

                                    roundAboutKm = round(NearbyDocDistanceINKM, 1);


                                }



                            }

                            Log.e("tag" , "discounted hospital in distance "+roundAboutKm);
                            Log.e("tag" , "discounted hospital in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);
                            discountedDocList.add(findDoctorGetterSetter);


                        }

                        if(discountedDocList.size() == 0)
                        {
                            DiscountdDoctorFragment.emptyViewDiscountedDr.setVisibility(View.VISIBLE);
                            DiscountdDoctorFragment.recyclerView_discounted_Doc.setVisibility(View.GONE);
                        }
                        else
                        {
                            findDoctorListRecycleView = new FindDoctorListRecycleView(FindDoctor.this , discountedDocList);
                            DiscountdDoctorFragment.recyclerView_discounted_Doc.setAdapter(findDoctorListRecycleView);
                            findDoctorListRecycleView.notifyDataSetChanged();
                        }



                        discountedDoctorServiceHasRun = true;

                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(FindDoctor.this, "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "discounted doctor Error: " + error.getMessage());
                Toast.makeText(FindDoctor.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();

                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(FindDoctor.this) .addToRequestQueue(strReq, cancel_req_tag);
    }


    public void getDiscountedDocListNext(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocList";

        //progressDialog.setMessage("Adding you ...");

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DISCOUNTED_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "discounted doctor Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");



                        if (nexPageList.size()>1){
                            nexPageList.clear();
                        }

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();



                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");

                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


                            JSONArray ratingArray = basicInfoObject.getJSONArray("ratings");
                            Log.e("tag" , "rating array size "+ratingArray.length());
                            if(ratingArray.length()>0) {

                                JSONObject ratingObject = ratingArray.getJSONObject(0);
                                rating = ratingObject.getDouble("rating");

                                Log.e("tag", "rating rating  : " + rating);

                            }
                            else
                            {
                                rating = 0.0;
                            }
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);



                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");

                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }

                                    Log.e("tag", "hospital number size : " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size abc: " + tempLandLineList.size());

                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));
                                }

                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    //Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(Home.userCurrentLocation, NearbyDocLocation);

                                    Double distance = CalculationByDistance(currentLatLang, NearbyDocLocation);

                                    // Double NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000 ;

                                    roundAboutKm = round(distance, 1);


                                }



                            }

                            Log.e("tag" , "discounted doc in distance "+roundAboutKm);
                            //Log.e("tag" , "discounted doc in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);

                            //topDocList.add(findDoctorGetterSetter);

                            nexPageList.add(findDoctorGetterSetter);

                        }

                        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(nexPageList.size(), nexPageList);
                        findDoctorListRecycleView.addAll(loadNextData);
                        findDoctorListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                       // callBack.onSuccess(nexPageList);

                        if (DiscountdDoctorFragment.bar.getVisibility() == View.VISIBLE){
                            DiscountdDoctorFragment.bar.setVisibility(View.GONE);
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(FindDoctor.this, "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "discounted doctor Error: " + error.getMessage());
                Toast.makeText(FindDoctor.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //loadingImage.setVisibility(View.INVISIBLE);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("offset", offset);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(FindDoctor.this) .addToRequestQueue(strReq, cancel_req_tag);

        //return topDocList;
    }
    //

    // onPagination scroll listener
    public void discountDocPaginationScrollListener(){

        DiscountdDoctorFragment.recyclerView_discounted_Doc.addOnScrollListener(new PaginationScrollListener(DiscountdDoctorFragment.linearLayoutManager, totalDataOnServer) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                discountedDoctorLoadNextPage(offset);

                //loadNextPage();
                //getBloodGroupListingService()
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.e("TAG", "I AM CALLING 123: ");


                if (isLoadingFinished){


                }




            }

            @Override
            public int getTotalPageCount() {

                Log.e("TAG", "Total Data count: " +totalDataOnServer );
                Log.e("TAG", "Total Data count layout: " +DiscountdDoctorFragment.linearLayoutManager.getItemCount() );
                if (indicator == 1){

                    return TOTAL_PAGES;
                }
                else {
                    return 0;
                }

                //return TOTAL_PAGES;
                // return (isLoadingFinished) ? TOTAL_PAGES : 0;
                //return 0;
            }

            @Override
            public boolean isLastPage() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;




            }
        });
    }

    //


    //
    private void discountedDoctorLoadNextPage(String offset) {
        Log.d(TAG, "loadNextPage: " + currentPage);




        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        getDiscountedDocListNext(offset);




/*
        // List<BloodReqiredGetterSetter> myCurrentArrayList = gettingNextPageForData();


      //  Log.e("TAG", "MY Current 123: " + myCurrentArrayList.size());


        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(myCurrentArrayList.size(), myCurrentArrayList);

        Log.e("TAG", "MY Current: " + loadNextData.size());
        Log.e("TAG", "MY Current list list: " + myCurrentArrayList.size());


        loadNextData.subList(loadNextData.size() - myCurrentArrayList.size(), loadNextData.size()).clear();

        //List<BloodReqiredGetterSetter> currntList = loadNextData.subList(loadNextData.size()-20, loadNextData.size()).clear();
        Log.e(TAG, "MY Current Total Item Count abc : " + myCurrentArrayList.size());
*/


     /*   findDoctorListRecycleView.removeLoadingFooter();

            isLoading = false;


        findDoctorListRecycleView.addAll(loadNextData);


            if (currentPage != TOTAL_PAGES) {
                findDoctorListRecycleView.addLoadingFooter();


            } else {
                isLastPage = true;

            }*/

        if (DiscountdDoctorFragment.bar.getVisibility()== View.GONE){
            DiscountdDoctorFragment.bar.setVisibility(View.VISIBLE);
        }


    }

    // end of discount list code




    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
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

                            Intent intent = new Intent(FindDoctor.this , UpdateDoctorProfile.class);
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
                        Intent intent = new Intent(FindDoctor.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void getDoctorsNameFilter(final String filterName)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Doctors Name Filter";

        searchViewProgress.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.GET_DOCTOR_NAME_FILTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Doctors Filter Response: " + response.toString());
                searchViewProgress.setVisibility(View.GONE);

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        object = new JSONObject(response);


                        hospitalArray = jObj.getJSONArray("doctors");

                        for (int i = 0; i < hospitalArray.length(); i++) {

                            specialityDesignationList = new ArrayList();

                            JSONObject practiceObject = hospitalArray.getJSONObject(i);

                            doctor_id = practiceObject.getString("doctor_id");
                            doctor_full_name = practiceObject.getString("doctor_full_name");

                            Log.e("tag" ,"doctor id is : "+doctor_id);
                            Log.e("tag" ,"doctor full name is : "+doctor_full_name);

                            JSONArray specialityArray = practiceObject.getJSONArray("speciality");
                            for (int k = 0; k < specialityArray.length(); k++) {
                                JSONObject specialityObject = specialityArray.getJSONObject(k);
                                JSONObject innerSpecialityObject = specialityObject.getJSONObject("speciality");
                                speciality_designation = innerSpecialityObject.getString("speciality_designation");
                                specialityDesignationList.add(speciality_designation);

                            }
                            Log.e("tag" , "specialty list in filter : "+specialityDesignationList.toString());
//
//
                            StringBuilder specialistBuilder = new StringBuilder();
                            StringBuilder QualificationBuilder = new StringBuilder();
                            String specialistPrefix = "";
                            String qualificationPrefix = "";

                            for (Object str : specialityDesignationList) {
                                specialistBuilder.append(specialistPrefix);
                                specialistPrefix = " , ";
                                specialistBuilder.append(str);
                            }

                            Log.e("tag" , "specialty list in filter with comma separated : "+specialistBuilder.toString());


                            doctorNameFilter.add(new DoctorSearchFilterGetterSetter(doctor_id , doctor_full_name , specialistBuilder.toString()));
                        }

                        if(doctorNameFilter.size() != 0)

                        {
                            DoctorsFilterAdapter doctorsFilterAdapter = new DoctorsFilterAdapter(getApplicationContext(), doctorNameFilter);
                            autoCompleteSearch.setAdapter(doctorsFilterAdapter);
                            doctorsFilterAdapter.notifyDataSetChanged();
                            autoCompleteSearch.showDropDown();

                        }


                        else
                        {

                            Toast.makeText(FindDoctor.this, "Record not found", Toast.LENGTH_SHORT).show();
                        }

                        Log.e("tag" , "Doctors filter size size "+doctorNameFilter.size());

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        searchViewProgress.setVisibility(View.GONE);
                        //Toast.makeText(get    ApplicationContext() , "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctors Filter Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //        searchViewProgress.setVisibility(View.GONE);
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
                    getDoctorsNameFilter(textToSearch);
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


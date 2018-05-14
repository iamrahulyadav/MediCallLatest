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
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import themedicall.com.Adapter.PharmacyListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.PharmacyGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.PaginationScrollListener;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pharmacy extends NavigationDrawer implements SearchView.OnQueryTextListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // Custom Action bar
    View customActionBarView ;
    ImageView  doctorFilterImage , searchViewImg;
    Button locationFilter;
    ImageView userIcon;
    RelativeLayout.LayoutParams params;

    ProgressDialog progressDialog;
    StringRequest strReq;
    private static final String TAG = "Pharmacy";
    String pharmacies_name , pharmacies_address , pharmacies_img , pharmacies_id , pharmacies_contact, pharmacy_offer_discount;
    double  pharmacies_lat , pharmacies_lng ;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    public static SharedPreferences.Editor cityAndLatLngEditor;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    PharmacyListRecycleView pharmacyListRecycleView;


    List<PharmacyGetterSetter> allPharmacyList;
    List<PharmacyGetterSetter> nextAllPharmacyList;
    List<PharmacyGetterSetter> nearByPharmacyList;
    List<PharmacyGetterSetter> nextNearByPharmacyList;

    public boolean allPharmacyServiceHasRun = false;
    public boolean nearbyPharmacyServiceHasRun = false;


    private int totalPharmacy;
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
    double roundAboutKm ;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pharmacy);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);



        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_pharmacy, null, false);
        drawer.addView(view, 0);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        initiate();
        SelectCity();
        customActionBarChecksAndAlsoCallNearbyAndAllPharmacyWebservice();
        setImageInActionbar();
    }


    public void initiate()
    {
        networkDialog = new Dialog(Pharmacy.this);

        dialog=new CustomProgressDialog(Pharmacy.this, 1);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);

        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();

        sharedPreferencesCityAndLatLng = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);
        cityAndLatLngEditor = sharedPreferencesCityAndLatLng.edit();

        allPharmacyList = new ArrayList<>();
        nearByPharmacyList = new ArrayList<>();

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

    public void customActionBarChecksAndAlsoCallNearbyAndAllPharmacyWebservice()
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
                else if(viewPager.getCurrentItem() == 1)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    if(nearbyPharmacyServiceHasRun)
                    {
                        Log.e("tag" , "nearby pharmacy service all ready Run");
                    }
                    else
                    {
                        getNearByPharmacy();

                    }
                    nearbyPharmacyPaginationScrollListener();


                }
                else if(viewPager.getCurrentItem() == 2)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    if(allPharmacyServiceHasRun)
                    {
                        Log.e("tag" , "all pharmacy service all ready Run");
                    }
                    else
                    {
                        getAllPharmacy() ;
                    }
                    allPharmacyPaginationScrollListener();

                }

                else if(viewPager.getCurrentItem() == 3)
                {
                    searchViewImg.setVisibility(View.GONE);
                    doctorFilterImage.setVisibility(View.GONE);
                    locationFilter.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
                    params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderPharmacyFragment(), "Order Medicine");
        adapter.addFragment(new NearByPharmacyFragment(), "Nearby");
        adapter.addFragment(new AllPharmacyFragment(), "All Pharmacies");
        adapter.addFragment(new DiscountedPharmacy(), "Discounts");
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


    public void onAddBtn(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.add_medicine_custom_row, null);
        // Add the new row before the add field button.
        OrderPharmacyFragment.parentLinearLayout.addView(rowView, OrderPharmacyFragment.parentLinearLayout.getChildCount() - 1);
    }

    public void onDeleteBtn(View v) {
        OrderPharmacyFragment.parentLinearLayout.removeView((View) (v).getParent());
        Log.d("tag" , "Press Delete Btn");

    }


    public void getNearByPharmacy()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Nearby Pharmacy";

        progressDialog.setMessage("Adding you ...");
        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.NEAR_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Nearby Pharmacy Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalPharmacy = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        for (int i = 0; i < labArray.length(); i++) {

                            nextNearByPharmacyList = new ArrayList<>();


                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng nearbyPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            Double nearbyPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, nearbyPharmacyLocation);


                            Double nearbyPharmacyDistanceINKM = nearbyPharmacyDistanceInMeter / 1000 ;

                            double roundAboutKm = round(nearbyPharmacyDistanceINKM, 1);

                            Log.e("tag" , "Nearby Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Nearby Pharmacy in distance in Km "+nearbyPharmacyDistanceINKM );

                            nearByPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName))
                        }

                        if(nearByPharmacyList.size() == 0)
                        {
                            NearByPharmacyFragment.emptyViewNearByList.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            pharmacyListRecycleView = new PharmacyListRecycleView(Pharmacy.this , nearByPharmacyList);
                            NearByPharmacyFragment.recyclerView_nearBy_pharmacy.setAdapter(pharmacyListRecycleView);
                            pharmacyListRecycleView.notifyDataSetChanged();

                        }


                        nearbyPharmacyServiceHasRun = true ;



                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Nearby Pharmacy Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                  dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void nextGetNearByPharmacy(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Nearby Pharmacy";

        strReq = new StringRequest(Request.Method.POST, Glob.NEAR_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Next Nearby Pharmacy Response: " + response.toString());
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalPharmacy = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        if (nextNearByPharmacyList.size()>1){
                            nextNearByPharmacyList.clear();
                        }


                        for (int i = 0; i < labArray.length(); i++) {



                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng nearbyPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            Double nearbyPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, nearbyPharmacyLocation);


                            Double nearbyPharmacyDistanceINKM = nearbyPharmacyDistanceInMeter / 1000 ;

                            double roundAboutKm = round(nearbyPharmacyDistanceINKM, 1);

                            Log.e("tag" , "Nearby Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Nearby Pharmacy in distance in Km "+nearbyPharmacyDistanceINKM );

                            nearByPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName))
                            nextNearByPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));
                        }


                        List<PharmacyGetterSetter> loadNextData = PharmacyGetterSetter.creatData(nextNearByPharmacyList.size(), nextNearByPharmacyList);
                        pharmacyListRecycleView.addAll(loadNextData);
                        pharmacyListRecycleView.notifyDataSetChanged();

                       // pharmacyListRecycleView.notifyDataSetChanged();


                       // callBack.onSuccess(nextNearByPharmacyList);

                        if (NearByPharmacyFragment.bar.getVisibility() == View.VISIBLE){
                            NearByPharmacyFragment.bar.setVisibility(View.GONE);
                        }


//                        PharmacyListRecycleView pharmacyListRecycleView = new PharmacyListRecycleView(Pharmacy.this , nearByPharmacyList);
//                        NearByPharmacyFragment.recyclerView_nearBy_pharmacy.setAdapter(pharmacyListRecycleView);
//
//                        nearbyPharmacyServiceHasRun = true ;



                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Nearby Pharmacy Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    // onPagination scroll listener
    public void nearbyPharmacyPaginationScrollListener(){

        NearByPharmacyFragment.recyclerView_nearBy_pharmacy.addOnScrollListener(new PaginationScrollListener(NearByPharmacyFragment.linearLayoutManager, totalPharmacy) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalPharmacy );
                Log.e("TAG", "Total Data count layout: " +NearByPharmacyFragment.linearLayoutManager.getItemCount() );
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

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    //


    //
    private void loadNextPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        nextGetNearByPharmacy(offset);





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

        if (NearByPharmacyFragment.bar.getVisibility()== View.GONE){
            NearByPharmacyFragment.bar.setVisibility(View.VISIBLE);
        }


    }













    public void getAllPharmacy()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "All Pharmacy";

        progressDialog.setMessage("Adding you ...");
        dialog.show();

        strReq = new StringRequest(Request.Method.POST, Glob.ALL_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "All Pharmacy Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    String total = jObj.getString("total");
                    Log.e("TAG", " The total available data is: " + total);
                    totalPharmacy = Integer.parseInt(total);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        for (int i = 0; i < labArray.length(); i++) {

                            nextAllPharmacyList = new ArrayList<>();

                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng allPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            if(currentLang == 0.0)
                            {
                                roundAboutKm = 0.0;
                            }
                            else
                            {
                                Double allPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang , allPharmacyLocation);
                                Double allPharmacyDistanceINKM = allPharmacyDistanceInMeter / 1000 ;
                                roundAboutKm = round(allPharmacyDistanceINKM, 1);
                            }




                            Log.e("tag" , "All Pharmacy in distance "+roundAboutKm);
                            //Log.e("tag" , "All Pharmacy in distance in Km "+allPharmacyDistanceINKM );

                            allPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);



                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName))
                        }


                        if(allPharmacyList.size() == 0)
                        {
                            AllPharmacyFragment.emptyViewAllPharmacyList.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            pharmacyListRecycleView = new PharmacyListRecycleView(Pharmacy.this , allPharmacyList);
                            AllPharmacyFragment.recyclerView_all_pharmacy.setAdapter(pharmacyListRecycleView);
                            pharmacyListRecycleView.notifyDataSetChanged();
                        }



                        allPharmacyServiceHasRun = true ;



                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "All Pharmacy Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", city);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    public void NextgetAllPharmacy(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "All Pharmacy";


        strReq = new StringRequest(Request.Method.POST, Glob.ALL_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "All Pharmacy Response: " + response.toString());
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalPharmacy = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        if (nextAllPharmacyList.size()>1){
                            nextAllPharmacyList.clear();
                        }

                        for (int i = 0; i < labArray.length(); i++) {


                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng allPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            if(currentLang == 0.0)
                            {
                                roundAboutKm = 0.0;
                            }
                            else
                            {
                                Double allPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang , allPharmacyLocation);
                                Double allPharmacyDistanceINKM = allPharmacyDistanceInMeter / 1000 ;
                                roundAboutKm = round(allPharmacyDistanceINKM, 1);
                            }





                            Log.e("tag" , "All Pharmacy in distance "+roundAboutKm);
                            //Log.e("tag" , "All Pharmacy in distance in Km "+allPharmacyDistanceINKM );

                            allPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            nextAllPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));
                        }


                        List<PharmacyGetterSetter> loadNextData = PharmacyGetterSetter.creatData(nextAllPharmacyList.size(), nextAllPharmacyList);
                        pharmacyListRecycleView.addAll(loadNextData);
                        pharmacyListRecycleView.notifyDataSetChanged();

//                        PharmacyListRecycleView pharmacyListRecycleView = new PharmacyListRecycleView(Pharmacy.this , allPharmacyList);
//                        AllPharmacyFragment.recyclerView_all_pharmacy.setAdapter(pharmacyListRecycleView);

                        //callBack.onSuccess(nextNearByPharmacyList);

                        if (AllPharmacyFragment.bar.getVisibility() == View.VISIBLE){
                            AllPharmacyFragment.bar.setVisibility(View.GONE);
                        }


                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "All Pharmacy Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", city);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }




    // onPagination scroll listener
    public void allPharmacyPaginationScrollListener(){

        AllPharmacyFragment.recyclerView_all_pharmacy.addOnScrollListener(new PaginationScrollListener(AllPharmacyFragment.linearLayoutManager, totalPharmacy) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextAllPharmacyPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalPharmacy );
                Log.e("TAG", "Total Data count layout: " +AllPharmacyFragment.linearLayoutManager.getItemCount() );
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

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    //


    //
    private void loadNextAllPharmacyPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        NextgetAllPharmacy(offset);





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

        if (AllPharmacyFragment.bar.getVisibility()== View.GONE){
            AllPharmacyFragment.bar.setVisibility(View.VISIBLE);
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
                final Dialog dialog = new Dialog(Pharmacy.this);
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
                customCityNewAdapter = new CustomCityNewAdapter(getApplicationContext(),cityList);
                cityListView.setAdapter(customCityNewAdapter);
                search_view.setOnQueryTextListener(Pharmacy.this);


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

                            Intent intent = new Intent(Pharmacy.this , UpdateDoctorProfile.class);
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
                        Intent intent = new Intent(Pharmacy.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
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

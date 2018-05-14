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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.BloodRequiresRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.GetterSetter.BloodReqiredGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
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

public class BloodGroupListing extends NavigationDrawer {

    RecyclerView recyclerView_blood_required;
    List<BloodReqiredGetterSetter> requiredBloodList;
    List<BloodReqiredGetterSetter> nextRequiredBloodList;

    private static final String TAG = "RegisterActivity";
    ProgressDialog progressDialog;
    JSONObject jsonObjectBasic;
    JSONObject jsonObjectCity;
    JSONArray jsonArray;
    String bloodGroupId , city;
    SharedPreferences sharedPreferences ;
    String blood_donor_name , blood_group_id  , city_title , blood_group_title , blood_donor_instructions , blood_donor_phone;
    ImageView searchViewImg ;
    Button locationFilter;
    ImageView userIcon  , doctorFilterImage;
    RelativeLayout.LayoutParams params;
    StringRequest strReq ;
    String cancel_req_tag = "register";
    CustomProgressDialog dialog;
    BloodRequiresRecycleView bloodRequiresRecycleView;

    TextView emptyViewBloodList;
    private ProgressBar bar ;
    private int totalBloodDonner;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    LinearLayoutManager linearLayoutManager ;
    SharedPreferences sharedPreferencesForBloodCurrentCity ;
    String cityForBlood;
    MyReceiverForNetworkDialog myReceiver;
    Dialog networkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_blood_group_listing);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_blood_group_listing, null, false);
        drawer.addView(view, 0);


        initiate();
        getBloodGroupIdAndCity();

        sharedPreferencesForBloodCurrentCity = getSharedPreferences("CityForBlood" , MODE_PRIVATE);
        cityForBlood = sharedPreferencesForBloodCurrentCity.getString("bloodCity" , "0");
        locationFilter.setText(cityForBlood);

        Log.e("tag" , "city for blood : "+cityForBlood);


        getBloodGroupListingService();
        bloodPaginationScrollListener();
        setImageInActionbar();
    }

    public void initiate()
    {
        networkDialog = new Dialog(BloodGroupListing.this);
        dialog=new CustomProgressDialog(BloodGroupListing.this, 1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        bar = (ProgressBar) findViewById(R.id.loadmore_blood_progress);
        emptyViewBloodList = (TextView) findViewById(R.id.emptyViewBloodList);
        recyclerView_blood_required = (RecyclerView) findViewById(R.id.recycler_view_blood_required);
        recyclerView_blood_required.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(BloodGroupListing.this , LinearLayoutManager.VERTICAL, false);
        recyclerView_blood_required.setLayoutManager(linearLayoutManager);
        recyclerView_blood_required.setAdapter(bloodRequiresRecycleView);
        requiredBloodList = new ArrayList<>();
        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        doctorFilterImage.setVisibility(View.GONE);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        params = (RelativeLayout.LayoutParams)locationFilter.getLayoutParams();
        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        searchViewImg.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        city = sharedPreferences.getString("city" , "0");
        locationFilter.setText(city);
    }

    public void getBloodGroupIdAndCity()
    {
        Intent mIntent = getIntent();
        bloodGroupId = mIntent.getStringExtra("blood_group_id");
    }

    public void getBloodGroupListingService()
    {

        // Tag used to cancel the request

        progressDialog.setMessage("Please Waite ...");
        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.BLOOD_GROUP_LISTING_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "blood Response: " + response.toString());
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObjectBasic = new JSONObject(response);
                        String total = jsonObjectBasic.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalBloodDonner = Integer.parseInt(total);
                        jsonArray = jsonObjectBasic.getJSONArray("blood_donors");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            nextRequiredBloodList = new ArrayList<>();

                            JSONObject finalobject = jsonArray.getJSONObject(i);


                            blood_group_id = finalobject.getString("blood_group_id");
                            blood_donor_instructions = finalobject.getString("blood_donor_instructions");
                            blood_donor_name = finalobject.getString("blood_donor_name");
                            blood_donor_phone = finalobject.getString("blood_donor_phone");

                            jsonObjectCity = finalobject.getJSONObject("city");
                            city_title = jsonObjectCity.getString("city_title");


                            jsonObjectCity = finalobject.getJSONObject("blood_group");
                            blood_group_title = jsonObjectCity.getString("blood_group_title");



                            requiredBloodList.add(new BloodReqiredGetterSetter(blood_group_id , blood_group_title , blood_donor_name , city_title , blood_donor_phone , blood_donor_instructions));


                        }
                        Log.d("tag " , "length " +jsonArray.length());

                        Log.d("tag " , "size" + requiredBloodList.size());

                        bloodRequiresRecycleView = new BloodRequiresRecycleView(BloodGroupListing.this , requiredBloodList);
                        recyclerView_blood_required.setAdapter(bloodRequiresRecycleView);
                        bloodRequiresRecycleView.notifyDataSetChanged();

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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("blood_group_id", String.valueOf(bloodGroupId));
                params.put("city", cityForBlood);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
       // AppSingleton.getInstance(this).addToRequestQueue(strReq, cancel_req_tag);
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void nextGetBloodGroupListingService(final String offset)
    {

        // Tag used to cancel the request

        strReq = new StringRequest(Request.Method.POST, Glob.BLOOD_GROUP_LISTING_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {





                        jsonObjectBasic = new JSONObject(response);
                        String total = jsonObjectBasic.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalBloodDonner = Integer.parseInt(total);
                        jsonArray = jsonObjectBasic.getJSONArray("blood_donors");


                        if (nextRequiredBloodList.size()>1){
                            nextRequiredBloodList.clear();
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject finalobject = jsonArray.getJSONObject(i);


                            blood_group_id = finalobject.getString("blood_group_id");
                            blood_donor_instructions = finalobject.getString("blood_donor_instructions");
                            blood_donor_name = finalobject.getString("blood_donor_name");
                            blood_donor_phone = finalobject.getString("blood_donor_phone");

                            jsonObjectCity = finalobject.getJSONObject("city");
                            city_title = jsonObjectCity.getString("city_title");


                            jsonObjectCity = finalobject.getJSONObject("blood_group");
                            blood_group_title = jsonObjectCity.getString("blood_group_title");



                            requiredBloodList.add(new BloodReqiredGetterSetter(blood_group_id , blood_group_title , blood_donor_name , city_title , blood_donor_phone , blood_donor_instructions));


                            nextRequiredBloodList.add(new BloodReqiredGetterSetter(blood_group_id , blood_group_title , blood_donor_name , city_title , blood_donor_phone , blood_donor_instructions));
                        }
                        Log.d("tag " , "length " +jsonArray.length());

                        Log.d("tag " , "size" + requiredBloodList.size());

                        List<BloodReqiredGetterSetter> loadNextData = BloodReqiredGetterSetter.creatData(nextRequiredBloodList.size(), nextRequiredBloodList);
                        bloodRequiresRecycleView.addAll(loadNextData);
                        bloodRequiresRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
                        }

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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("blood_group_id", String.valueOf(bloodGroupId));
                params.put("city", "Lahore");
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // AppSingleton.getInstance(this).addToRequestQueue(strReq, cancel_req_tag);
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // onPagination scroll listener
    public void bloodPaginationScrollListener(){

        recyclerView_blood_required.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalBloodDonner) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextBloodRequiredPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalBloodDonner );
                Log.e("TAG", "Total Data count layout: " +linearLayoutManager.getItemCount() );
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

                return (totalBloodDonner-1 == bloodRequiresRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalBloodDonner-1 == bloodRequiresRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    //


    //
    private void loadNextBloodRequiredPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);


        nextGetBloodGroupListingService(offset);
//                List<HospitalDoctorGetterSetter> loadNextData = HospitalDoctorGetterSetter.creatData(nextDocList.size(), nextDocList);
//
//                Log.e(TAG, "MY Current Total Item Count abc : " + nextDocList.size());
//                hosDoctorListRecycleView.addAll(loadNextData);



//getNextHospitalDocList(new CallBack() {
//            @Override
//            public void onSuccess(List<HospitalDoctorGetterSetter> topDocList) {
//
//
//                List<HospitalDoctorGetterSetter> loadNextData = HospitalDoctorGetterSetter.creatData(topDocList.size(), topDocList);
//
//                Log.e("TAG", "MY Current " + nextDocList.size());
//                Log.e("TAG", "MY Current top doctor: " + topDocList.size());
//                Log.e("TAG", "MY Current list list: " + nextDocList.size());
//
//                //loadNextData.subList(loadNextData.size() - myCurrentArrayList.size(), loadNextData.size()).clear();
//
//                //List<BloodReqiredGetterSetter> currntList = loadNextData.subList(loadNextData.size()-20, loadNextData.size()).clear();
//                Log.e(TAG, "MY Current Total Item Count abc : " + nextDocList.size());
//
//                hosDoctorListRecycleView.addAll(loadNextData);
//            }
//
//        } , offset);
//

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

        if (bar.getVisibility()== View.GONE){
            bar.setVisibility(View.VISIBLE);
        }


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

                            Intent intent = new Intent(BloodGroupListing.this , UpdateDoctorProfile.class);
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
                        Intent intent = new Intent(BloodGroupListing.this , SignIn.class);
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

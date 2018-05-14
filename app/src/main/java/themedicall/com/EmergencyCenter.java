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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import themedicall.com.Adapter.CitiesListCustomAdapter;
import themedicall.com.Adapter.HospitalsListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.GetterSetter.HospitalsGetterSetter;
import themedicall.com.GetterSetter.SuggestionGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
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
import java.util.Timer;

public class EmergencyCenter extends AppCompatActivity {

    public RecyclerView recycler_view_emergency_centers ;
    LinearLayoutManager linearLayoutManager;

    List<HospitalsGetterSetter> hospitalsList;
    ArrayList<HospitalMultipleDocGetterSetter> hospitalDocList;
    List<HospitalsGetterSetter> nexPageList;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    View customActionBarView ;
    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject docObject;
    JSONArray basicInfoArray;
    private static final String TAG = "Hospitals";
    String  hospital_id , hospital_name  , hospital_addr , hospital_img  ,   hospital_landline_number , doctor_id , doctor_full_name , doctor_image , hospital_views , hospital_share_url , hospital_offer_any_discount;
    Double hospital_lat , hospital_lng;
    String hospitalId , hospitalName ;
    HospitalsGetterSetter dm ;
    String  city;
    StringRequest strReq;
    String cancel_req_tag = "Hospitals";
    public static ArrayList<SuggestionGetterSetter> HospitalNameList = new ArrayList<SuggestionGetterSetter>();
    CitiesListCustomAdapter citiesListCustomAdapter ;
    private Timer timer;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    private int totalDataOnServer;
    private ProgressBar bar;
    HospitalsListRecycleView hospitalsListRecycleView ;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    TextView emptyViewEmergencyCenter ;
    Dialog networkDialog;
    double roundAboutKm;
    MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initiate();
        getAllHospitalsDetail();
        allHospitalPaginationScrollListener();

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
        networkDialog = new Dialog(EmergencyCenter.this);
        dialog=new CustomProgressDialog(EmergencyCenter.this, 1);
        emptyViewEmergencyCenter = (TextView) findViewById(R.id.emptyViewEmergencyCenter);
        recycler_view_emergency_centers = (RecyclerView) findViewById(R.id.recycler_view_emergency_centers);
        recycler_view_emergency_centers.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(EmergencyCenter.this , LinearLayoutManager.VERTICAL, false);
        recycler_view_emergency_centers.setLayoutManager(linearLayoutManager);

        bar = (ProgressBar) findViewById(R.id.loadmore_progress_emergency_centers);
        progressDialog = new ProgressDialog(EmergencyCenter.this);
        progressDialog.setCancelable(false);
        hospitalsList = new ArrayList<>();
        sharedPreferencesCityAndLatLng = getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);

    }

    public void getAllHospitalsDetail()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospitals";

       // progressDialog.setMessage("Adding you ...");
        //showDialog();
        //AllHospitals.loading_all.setVisibility(View.VISIBLE);
        dialog.show();

        strReq = new StringRequest(Request.Method.POST, Glob.EMERGENCY_CENTERS_HOSPITALS_LISTING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Hospital Response: " + response.toString());
                //hideDialog();
                //AllHospitals.loading_all.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);

                        basicInfoArray = basicInfoObject.getJSONArray("hospitals");


                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            dm = new HospitalsGetterSetter();
                            hospitalDocList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            landLineList = new ArrayList<HospitalLandLineListGetterSetter>();
                            nexPageList = new ArrayList<>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            hospital_id = basicInfoObject.getString("hospital_id");
                            hospital_name = basicInfoObject.getString("hospital_name");
                            hospital_addr = basicInfoObject.getString("hospital_addr");
                            hospital_img = basicInfoObject.getString("hospital_img");
                            hospital_lat = basicInfoObject.getDouble("hospital_lat");
                            hospital_lng = basicInfoObject.getDouble("hospital_lng");
                            hospital_views = basicInfoObject.getString("hospital_views");
                            hospital_share_url = basicInfoObject.getString("hospital_share_url");
                            hospital_offer_any_discount = basicInfoObject.getString("hospital_offer_any_discount");

                            Log.d("tag " , "hos name "+hospital_name);


                            LatLng HospitalLocation = new LatLng(hospital_lat, hospital_lng);



                            if(currentLang == 0.0)
                            {
                                roundAboutKm = 0.0;
                            }
                            else
                            {
                                Double distance = SphericalUtil.computeDistanceBetween(currentLatLang, HospitalLocation);
                                Double pharmacyDistanceINKM = distance / 1000 ;
                                roundAboutKm = round(pharmacyDistanceINKM, 1);
                            }

//
//                            //Double HospitalDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang , HospitalLocation);
//
//                            Double distance = CalculationByDistance(currentLatLang , HospitalLocation);
//
//                            // Double pharmacyDistanceINKM = HospitalDistanceInMeter / 1000 ;
//
//                            roundAboutKm = round(distance, 1);

                            Log.e("tag" , "Pharmacy in distance "+roundAboutKm);
                            // Log.e("tag" , "Pharmacy in distance in Km "+pharmacyDistanceINKM );





                            dm.setHospitalId(hospital_id);
                            dm.setHospitalProfileName(hospital_name);
                            dm.setHospitalProfileAddress(hospital_addr);
                            dm.setHospitalProfileImg(hospital_img);
                            dm.setHospitalProfileNoOfDoc("0");
                            dm.setHospitalKm(roundAboutKm);
                            dm.setHospitalProfileNoOfViews(hospital_views);
                            dm.setHospitalProfileShareLink(hospital_share_url);
                            dm.setHospitalProfileDiscount(hospital_offer_any_discount);

                            JSONArray landLineArray = basicInfoObject.getJSONArray("landline");
                            for (int j = 0; j < landLineArray.length(); j++) {

                                JSONObject landLineObject = landLineArray.getJSONObject(j);
                                hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                landLineList.add(new HospitalLandLineListGetterSetter(hospital_name , hospital_landline_number));

                                //landLineList.add(hospital_landline_number);
                            }
//
                            JSONArray jsonArrayDoctor = basicInfoObject.getJSONArray("doctors");
                            for (int k = 0; k < jsonArrayDoctor.length(); k++) {
                                JSONObject hosDocObject = jsonArrayDoctor.getJSONObject(k);
                                docObject = hosDocObject.getJSONObject("doctors");


                                doctor_id = docObject.getString("doctor_id");
                                doctor_full_name = docObject.getString("doctor_full_name");
                                doctor_image = docObject.getString("doctor_img");

                                Log.d("tag" , "doc id "+doctor_id);
                                Log.d("tag" , "doc full_name "+doctor_full_name);
                                Log.d("tag" , "doc image "+doctor_image);

                                if(jsonArrayDoctor.length() > 0)
                                {
                                    dm.setHospitalProfileNoOfDoc(String.valueOf(jsonArrayDoctor.length()));
                                }
                                hospitalDocList.add(new HospitalMultipleDocGetterSetter(doctor_id, doctor_image , doctor_full_name, 1.1 , 1.3));

//                                if(k >= 2)
//                                {
//                                    break;
//                                }

                            }

                            dm.setLandLineList(landLineList);
                            dm.setAllDocList(hospitalDocList);

                            hospitalsList.add(dm);


                        }

                        if(hospitalsList.size() == 0)
                        {
                            emptyViewEmergencyCenter.setVisibility(View.VISIBLE);
                            recycler_view_emergency_centers.setVisibility(View.GONE);
                        }
                        else
                        {
                            hospitalsListRecycleView = new HospitalsListRecycleView(EmergencyCenter.this , hospitalsList);
                            recycler_view_emergency_centers.setAdapter(hospitalsListRecycleView);
                            hospitalsListRecycleView.notifyDataSetChanged();
                        }


                        //getHospitalsNames(s.toString());
                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(EmergencyCenter.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "All Hospital Error: " + error.getMessage());
               // Toast.makeText(EmergencyCenter.this, error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //AllHospitals.loading_all.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
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
        AppSingleton.getInstance(EmergencyCenter.this).addToRequestQueue(strReq, cancel_req_tag);
    }




    public void getAllHospitalsNext(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospitals";

        Log.e("tag" , "lat in sp next"+currentLat);
        Log.e("tag" , "lang in sp next"+currentLang);

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.EMERGENCY_CENTERS_HOSPITALS_LISTING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("hospitals");

                        if (nexPageList.size()>1){
                            nexPageList.clear();
                        }

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            dm = new HospitalsGetterSetter();
                            hospitalDocList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            hospital_id = basicInfoObject.getString("hospital_id");
                            hospital_name = basicInfoObject.getString("hospital_name");
                            hospital_addr = basicInfoObject.getString("hospital_addr");
                            hospital_img = basicInfoObject.getString("hospital_img");
                            hospital_lat = basicInfoObject.getDouble("hospital_lat");
                            hospital_lng = basicInfoObject.getDouble("hospital_lng");
                            hospital_views = basicInfoObject.getString("hospital_views");
                            hospital_share_url = basicInfoObject.getString("hospital_share_url");
                            hospital_offer_any_discount = basicInfoObject.getString("hospital_offer_any_discount");

                            Log.d("tag " , "hos name "+hospital_name);


                            LatLng HospitalLocation = new LatLng(hospital_lat, hospital_lng);



                            if(currentLang == 0.0)
                            {
                                roundAboutKm = 0.0;
                            }
                            else
                            {
                                Double distance = SphericalUtil.computeDistanceBetween(currentLatLang, HospitalLocation);
                                Double pharmacyDistanceINKM = distance / 1000 ;
                                roundAboutKm = round(pharmacyDistanceINKM, 1);
                            }

//                            //Double HospitalDistanceInMeter = SphericalUtil.computeDistanceBetween(Home.userCurrentLocation, HospitalLocation);
//
//
//                            Double distance = CalculationByDistance(currentLatLang , HospitalLocation);
//
//
//                            //Double pharmacyDistanceINKM = HospitalDistanceInMeter / 1000 ;
//
//                            double roundAboutKm = round(distance, 1);

                            Log.e("tag" , "All hospital in distance "+roundAboutKm);
                            //Log.e("tag" , "All hospital in distance in Km "+pharmacyDistanceINKM );



                            dm.setHospitalId(hospital_id);
                            dm.setHospitalProfileName(hospital_name);
                            dm.setHospitalProfileAddress(hospital_addr);
                            dm.setHospitalProfileImg(hospital_img);
                            dm.setHospitalProfileNoOfDoc("0");
                            dm.setHospitalKm(roundAboutKm);
                            dm.setHospitalProfileNoOfViews(hospital_views);
                            dm.setHospitalProfileShareLink(hospital_share_url);
                            dm.setHospitalProfileDiscount(hospital_offer_any_discount);

                            JSONArray landLineArray = basicInfoObject.getJSONArray("landline");
                            for (int j = 0; j < landLineArray.length(); j++) {

                                JSONObject landLineObject = landLineArray.getJSONObject(j);
                                hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                landLineList.add(new HospitalLandLineListGetterSetter(hospital_name , hospital_landline_number));

//                                landLineList.add(hospital_landline_number);
                            }
//
                            JSONArray jsonArrayDoctor = basicInfoObject.getJSONArray("doctors");
                            for (int k = 0; k < jsonArrayDoctor.length(); k++) {
                                JSONObject hosDocObject = jsonArrayDoctor.getJSONObject(k);
                                docObject = hosDocObject.getJSONObject("doctors");


                                doctor_id = docObject.getString("doctor_id");
                                doctor_full_name = docObject.getString("doctor_full_name");
                                doctor_image = docObject.getString("doctor_img");

                                Log.d("tag" , "doc id "+doctor_id);
                                Log.d("tag" , "doc full_name "+doctor_full_name);
                                Log.d("tag" , "doc image "+doctor_image);

                                if(jsonArrayDoctor.length() > 0)
                                {
                                    dm.setHospitalProfileNoOfDoc(String.valueOf(jsonArrayDoctor.length()));
                                }
                                hospitalDocList.add(new HospitalMultipleDocGetterSetter(doctor_id, doctor_image , doctor_full_name , 1.1 , 1.3));

//                                if(k >= 2)
//                                {
//                                    break;
//                                }

                            }

                            dm.setLandLineList(landLineList);
                            dm.setAllDocList(hospitalDocList);

                            nexPageList.add(dm);


                        }

                        List<HospitalsGetterSetter> loadNextData = HospitalsGetterSetter.creatData(nexPageList.size(), nexPageList);
                        hospitalsListRecycleView.addAll(loadNextData);
                        hospitalsListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getActivity() , " You are successfully Added!", Toast.LENGTH_SHORT).show();
                        //callBack.onSuccess(nexPageList);


                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
                        }


                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(EmergencyCenter.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
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
        //AppSingleton.getInstance(this).addToRequestQueue(strReq, cancel_req_tag);
        // Adding request to request queue
        AppSingleton.getInstance(EmergencyCenter.this).addToRequestQueue(strReq, cancel_req_tag);


        //return hospitalsList;
    }

    //



    //
    // onPagination scroll listener
    public void allHospitalPaginationScrollListener(){

        recycler_view_emergency_centers.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalDataOnServer) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);




                loadAllHospitalNextPage(offset);

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

                return (totalDataOnServer-1 == hospitalsListRecycleView.getItemCount()) ? true : false;




            }

            @Override
            public boolean isLoading() {

                return (totalDataOnServer-1 == hospitalsListRecycleView.getItemCount()) ? true : false;




            }
        });
    }

    //


    //
    private void loadAllHospitalNextPage(String offset) {
        Log.d(TAG, "loadNextPage: " + currentPage);


        getAllHospitalsNext(offset);




        /*List<HospitalsGetterSetter> myCurrentArrayList = getHospitalsNext();

        // List<BloodReqiredGetterSetter> myCurrentArrayList = gettingNextPageForData();

        Log.e("TAG", "MY Current 123: " + myCurrentArrayList.size());

        List<HospitalsGetterSetter> loadNextData = HospitalsGetterSetter.creatData(myCurrentArrayList.size(), myCurrentArrayList);

        loadNextData.subList(loadNextData.size() - myCurrentArrayList.size(), loadNextData.size()).clear();

        //List<BloodReqiredGetterSetter> currntList = loadNextData.subList(loadNextData.size()-20, loadNextData.size()).clear();
        Log.e(TAG, "MY Current Total Item Count abc : " + myCurrentArrayList.size());
*/
/*
           bloodRequiresRecycleView.removeLoadingFooter();

            isLoading = false;


                bloodRequiresRecycleView.addAll(loadNextData);


            if (currentPage != TOTAL_PAGES) {
                bloodRequiresRecycleView.addLoadingFooter();


            } else {
                isLastPage = true;

            }*/

        if (bar.getVisibility()== View.GONE){
            bar.setVisibility(View.VISIBLE);
        }


    }

//    public interface CallBack {
//        void onSuccess(List<HospitalsGetterSetter> topDocList);
//
//        void onFail(String msg);
//    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


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

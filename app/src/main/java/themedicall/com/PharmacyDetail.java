package themedicall.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import themedicall.com.Adapter.HospitalTimingListRecyclerView;
import themedicall.com.Adapter.HospitalsServiceListRecycleView;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.GetterSetter.HospitalTimingGetterSetter;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PharmacyDetail extends AppCompatActivity {

    public String pharmacyId , pharmacyName , pharmacyAddress , pharmacyPhone , pharmacyKm , pharmacy_timing_status , pharmacy_open_timing , pharmacy_close_timing  ,
            pharmacy_about_us , service_pharmacy_title , week_day_id , pharmacy_working_day_opening_time , pharmacy_working_day_close_timing  , pharmacy_id , week_day_title;
    Double  pharmacy_lat , pharmacy_lng ;
    Bitmap pharmacyImgBitmap ;
    public ImageView pharmacyRowProfileImg ;
    public TextView pharmacyRowId , pharmacyRowDistance , pharmacyRowName , pharmacyRowAddress , pharmacyRowPhoneNumber  , pharmacyDetailAboutHeading , pharmacyDetailAbout , pharmacyDetailTimingHeading , pharmacyDetailStartTiming
            , pharmacyDetailEndTiming , pharmacyDetailLocationHeading , pharmacyDetailServicesHeading , pharmacyAlwaysTiming ;
    RecyclerView pharmacyDetailServicesRecyclerView , pharmacy_timing_other_status_recycler_view;
    LinearLayout pharmacyDetailTimingLayout ;
    ImageView pharmacyDetailAboutLine , pharmacyDetailTimingLayoutLine , pharmacyDetailLocationLine ;
    ProgressDialog progressDialog;
    private static final String TAG = "Lab Detail";
    JSONObject basicInfoObject;
    ArrayList<HospitalServiceGetterSetter> servicesList;
    DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
    DateFormat f2 = new SimpleDateFormat("h:mma"); // 12 hours
    MapView mMapView;
    private GoogleMap googleMap;
    String pharmacy_timing ;
    ArrayList<HospitalTimingGetterSetter> timingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiate(savedInstanceState);
        getPharmacyInfoFromAdapter();
        setPharmacyInfo();
        getPharmacyDetail();

    }


    public void initiate(Bundle savedInstanceState)
    {

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);


        pharmacyRowProfileImg = (ImageView) findViewById(R.id.pharmacyRowProfileImg);
        pharmacyRowId = (TextView) findViewById(R.id.pharmacyRowId);
        pharmacyRowDistance = (TextView) findViewById(R.id.pharmacyRowDistance);
        pharmacyRowName = (TextView) findViewById(R.id.pharmacyRowName);
        pharmacyRowAddress = (TextView) findViewById(R.id.pharmacyRowAddress);
        pharmacyRowPhoneNumber = (TextView) findViewById(R.id.pharmacyRowPhoneNumber);


        pharmacyDetailAboutHeading = (TextView) findViewById(R.id.labDetailAboutHeading);
        pharmacyDetailAbout = (TextView) findViewById(R.id.labDetailAbout);
        pharmacyDetailTimingHeading = (TextView) findViewById(R.id.labDetailTimingHeading);
        pharmacyDetailStartTiming = (TextView) findViewById(R.id.labDetailStartTiming);
        pharmacyDetailEndTiming = (TextView) findViewById(R.id.labDetailEndTiming);
        pharmacyDetailLocationHeading = (TextView) findViewById(R.id.labDetailLocationHeading);
        pharmacyDetailServicesHeading = (TextView) findViewById(R.id.LabDetailServicesHeading);
        pharmacyAlwaysTiming = (TextView) findViewById(R.id.labAlwaysTiming);


        pharmacyDetailServicesRecyclerView = (RecyclerView) findViewById(R.id.pharmacyDetailServicesRecyclerView);
        pharmacyDetailServicesRecyclerView.setHasFixedSize(true);
        pharmacyDetailServicesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false));



        pharmacy_timing_other_status_recycler_view = (RecyclerView) findViewById(R.id.pharmacy_timing_other_status_recycler_view);
        pharmacy_timing_other_status_recycler_view.setHasFixedSize(true);
        pharmacy_timing_other_status_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false));



        pharmacyDetailTimingLayout = (LinearLayout) findViewById(R.id.labDetailTimingLayout);
        pharmacyDetailAboutLine = (ImageView) findViewById(R.id.labDetailAboutLine);
        pharmacyDetailTimingLayoutLine = (ImageView) findViewById(R.id.labDetailTimingLayoutLine);
        pharmacyDetailLocationLine = (ImageView) findViewById(R.id.labDetailLocationLine);




        servicesList = new ArrayList<HospitalServiceGetterSetter>();

        mMapView = (MapView) findViewById(R.id.mapViewPharmacy);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getPharmacyInfoFromAdapter()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            pharmacyImgBitmap = (Bitmap) intent.getParcelableExtra("pharmacyImgBitmap");
            pharmacyId= (String) bd.get("pharmacyId");
            pharmacyName=(String)bd.get("pharmacyName");
            pharmacyAddress=(String)bd.get("pharmacyAddress");
            pharmacyPhone=(String)bd.get("pharmacyPhone");
            pharmacyKm=(String)bd.get("pharmacyKm");
        }
    }


    public void setPharmacyInfo()
    {
        pharmacyRowProfileImg.setImageBitmap(pharmacyImgBitmap);
        pharmacyRowName.setText(pharmacyName);
        pharmacyRowAddress.setText(pharmacyAddress);
        pharmacyRowPhoneNumber.setText(pharmacyPhone);
        pharmacyRowDistance.setText(pharmacyKm);
    }



    public void getPharmacyDetail()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Lab detail";

        progressDialog.setMessage("Adding you ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.PHARMACY_DETAIL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Lab Detail Response: " + response.toString());
                //hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        basicInfoObject = new JSONObject(response);
                        JSONObject data = basicInfoObject.getJSONObject("pharmacy");

                        pharmacy_timing_status = data.getString("pharmacy_timing_status");
                        pharmacy_open_timing = data.getString("pharmacy_open_timing");
                        pharmacy_close_timing = data.getString("pharmacy_close_timing");
                        pharmacy_lat = data.getDouble("pharmacy_lat");
                        pharmacy_lng = data.getDouble("pharmacy_lng");
                        pharmacy_about_us = data.getString("pharmacy_about_us");



                        JSONArray labTimingArray = data.getJSONArray("working_days");
                        for (int i = 0; i < labTimingArray.length(); i++) {
                            JSONObject practiceObject = labTimingArray.getJSONObject(i);
                            pharmacy_working_day_opening_time = practiceObject.getString("pharmacy_working_day_opening_time");
                            pharmacy_working_day_close_timing = practiceObject.getString("pharmacy_working_day_close_timing");
                            pharmacy_id = practiceObject.getString("pharmacy_id");

                            JSONObject weekDaysObject = practiceObject.getJSONObject("week_days");
                            week_day_id = weekDaysObject.getString("week_day_id");
                            week_day_title = weekDaysObject.getString("week_day_title");

                            pharmacy_timing = pharmacy_working_day_opening_time + " - " + pharmacy_working_day_close_timing ;

                            timingList.add(new HospitalTimingGetterSetter(week_day_title , pharmacy_timing));

                        }



                        JSONArray servicesArray = data.getJSONArray("services");
                        for (int k = 0; k < servicesArray.length(); k++) {
                            JSONObject servicesObject = servicesArray.getJSONObject(k);
                            JSONObject innerServicesObject = servicesObject.getJSONObject("services");
                            service_pharmacy_title = innerServicesObject.getString("service_pharmacy_title");
                            servicesList.add(new HospitalServiceGetterSetter(service_pharmacy_title));
                        }







                        //String s = "12:18:00";
                        Date openTime = f1.parse(pharmacy_open_timing);
                        Date closeTime = f1.parse(pharmacy_close_timing);



                        if(pharmacy_about_us.equals(""))
                        {
                            pharmacyDetailAboutHeading.setVisibility(View.GONE);
                            pharmacyDetailAbout.setVisibility(View.GONE);
                            pharmacyDetailAboutLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            pharmacyDetailAbout.setText(pharmacy_about_us);
                        }

                        if(pharmacy_timing_status.equals("not"))
                        {
                            pharmacyDetailTimingHeading.setVisibility(View.GONE);
                            pharmacyDetailTimingLayout.setVisibility(View.GONE);
                            pharmacyDetailTimingLayoutLine.setVisibility(View.GONE);
                        }

                        else if(pharmacy_timing_status.equals("always"))
                        {
                            pharmacyAlwaysTiming.setVisibility(View.VISIBLE);
                            pharmacyDetailTimingLayout.setVisibility(View.GONE);
                        }

                        else if(pharmacy_timing_status.equals("open"))
                        {
                            pharmacyDetailStartTiming.setText(f2.format(openTime));
                            pharmacyDetailEndTiming.setText(f2.format(closeTime));
                        }

                        else if(pharmacy_timing_status.equals("other"))
                        {
                            pharmacyDetailTimingLayout.setVisibility(View.GONE);


                            HospitalTimingListRecyclerView HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(PharmacyDetail.this , timingList , "");
                            pharmacy_timing_other_status_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                            HospitalTimingListRecyclerView.notifyDataSetChanged();
                        }
//
//
                        if(servicesList.size() == 0)
                        {
                            pharmacyDetailServicesHeading.setVisibility(View.GONE);
                            pharmacyDetailServicesRecyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesList);
                            pharmacyDetailServicesRecyclerView.setAdapter(hospitalsServiceListRecycleView);
                            hospitalsServiceListRecycleView.notifyDataSetChanged();
                        }



                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                googleMap = mMap;

                                // For showing a move to my location button
                                if (ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                //googleMap.setMyLocationEnabled(true);

                                // For dropping a marker at a point on the Map
                                LatLng sydney = new LatLng(pharmacy_lat, pharmacy_lng);
                                googleMap.addMarker(new MarkerOptions().position(sydney).title(pharmacyName));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        });


                        //  Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Lab Detail Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("city", "Lahore");
                params.put("pharmacy_id", pharmacyId);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }






}

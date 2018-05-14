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


public class LabDetail extends AppCompatActivity {
    public String labId , labName , labAddress , labPhone , labKm , lab_timing_status , lab_open_timing , lab_close_timing  ,
            lab_about_us , service_lab_title , week_day_id , lab_working_day_opening_time , lab_working_day_close_timing  , lab_id , week_day_title;
    Double  lab_lat , lab_lng ;
    Bitmap labImgBitmap ;
    public ImageView labRowProfileImg ;
    public TextView labRowId , labRowDistance , labRowName , labRowAddress , labRowPhoneNumber , labDetailAboutHeading , labDetailAbout , labDetailTimingHeading , labDetailStartTiming
            , labDetailEndTiming , labDetailLocationHeading , LabDetailServicesHeading , labAlwaysTiming ;
    RecyclerView labDetailServicesRecyclerView , lab_timing_other_status_recycler_view;
    LinearLayout labDetailTimingLayout ;
    ImageView labDetailAboutLine , labDetailTimingLayoutLine , labDetailLocationLine ;
    ProgressDialog progressDialog;
    private static final String TAG = "Lab Detail";
    JSONObject basicInfoObject;
    ArrayList<HospitalServiceGetterSetter> servicesList;
    DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
    DateFormat f2 = new SimpleDateFormat("h:mma"); // 12 hours
    MapView mMapView;
    private GoogleMap googleMap;
    String lab_timing ;
    ArrayList<HospitalTimingGetterSetter> timingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        initiate(savedInstanceState);
        getLabInfoFromAdapter();
        setLabInfo();
        getLabDetail();

    }

    public void initiate(Bundle savedInstanceState)
    {

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);


        labRowProfileImg = (ImageView) findViewById(R.id.labRowProfileImg);
        labRowId = (TextView) findViewById(R.id.labRowId);
        labRowDistance = (TextView) findViewById(R.id.labRowDistance);
        labRowName = (TextView) findViewById(R.id.labRowName);
        labRowAddress = (TextView) findViewById(R.id.labRowAddress);
        labRowPhoneNumber = (TextView) findViewById(R.id.labRowPhoneNumber);


        labDetailAboutHeading = (TextView) findViewById(R.id.labDetailAboutHeading);
        labDetailAbout = (TextView) findViewById(R.id.labDetailAbout);
        labDetailTimingHeading = (TextView) findViewById(R.id.labDetailTimingHeading);
        labDetailStartTiming = (TextView) findViewById(R.id.labDetailStartTiming);
        labDetailEndTiming = (TextView) findViewById(R.id.labDetailEndTiming);
        labDetailLocationHeading = (TextView) findViewById(R.id.labDetailLocationHeading);
        LabDetailServicesHeading = (TextView) findViewById(R.id.LabDetailServicesHeading);
        labAlwaysTiming = (TextView) findViewById(R.id.labAlwaysTiming);


        labDetailServicesRecyclerView = (RecyclerView) findViewById(R.id.labDetailServicesRecyclerView);
        labDetailServicesRecyclerView.setHasFixedSize(true);
        labDetailServicesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false));



        lab_timing_other_status_recycler_view = (RecyclerView) findViewById(R.id.lab_timing_other_status_recycler_view);
        lab_timing_other_status_recycler_view.setHasFixedSize(true);
        lab_timing_other_status_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.VERTICAL , false));



        labDetailTimingLayout = (LinearLayout) findViewById(R.id.labDetailTimingLayout);
        labDetailAboutLine = (ImageView) findViewById(R.id.labDetailAboutLine);
        labDetailTimingLayoutLine = (ImageView) findViewById(R.id.labDetailTimingLayoutLine);
        labDetailLocationLine = (ImageView) findViewById(R.id.labDetailLocationLine);




        servicesList = new ArrayList<HospitalServiceGetterSetter>();

        mMapView = (MapView) findViewById(R.id.mapViewLab);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getLabInfoFromAdapter()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            labImgBitmap = (Bitmap) intent.getParcelableExtra("labImgBitmap");
            labId= (String) bd.get("labId");
            labName=(String)bd.get("labName");
            labAddress=(String)bd.get("labAddress");
            labPhone=(String)bd.get("labPhone");
            labKm=(String)bd.get("labKm");
        }
    }


    public void setLabInfo()
    {
        labRowProfileImg.setImageBitmap(labImgBitmap);
        labRowName.setText(labName);
        labRowAddress.setText(labAddress);
        labRowPhoneNumber.setText(labPhone);
        labRowDistance.setText(labKm);
    }



    public void getLabDetail()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Lab detail";

        progressDialog.setMessage("Adding you ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.LAB_DETAIL_URL, new Response.Listener<String>() {

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

                        lab_timing_status = data.getString("lab_timing_status");
                        lab_open_timing = data.getString("lab_open_timing");
                        lab_close_timing = data.getString("lab_close_timing");
                        lab_lat = data.getDouble("lab_lat");
                        lab_lng = data.getDouble("lab_lng");
                        lab_about_us = data.getString("lab_about_us");



                        JSONArray labTimingArray = data.getJSONArray("working_days");
                        for (int i = 0; i < labTimingArray.length(); i++) {
                            JSONObject practiceObject = labTimingArray.getJSONObject(i);
                            lab_working_day_opening_time = practiceObject.getString("lab_working_day_opening_time");
                            lab_working_day_close_timing = practiceObject.getString("lab_working_day_close_timing");
                            lab_id = practiceObject.getString("lab_id");

                            JSONObject weekDaysObject = practiceObject.getJSONObject("week_days");
                            week_day_id = weekDaysObject.getString("week_day_id");
                            week_day_title = weekDaysObject.getString("week_day_title");

                            lab_timing = lab_working_day_opening_time + " - " + lab_working_day_close_timing ;

                            timingList.add(new HospitalTimingGetterSetter(week_day_title , lab_timing));

                        }



                        JSONArray servicesArray = data.getJSONArray("services");
                        for (int k = 0; k < servicesArray.length(); k++) {
                            JSONObject servicesObject = servicesArray.getJSONObject(k);
                            JSONObject innerServicesObject = servicesObject.getJSONObject("services");
                            service_lab_title = innerServicesObject.getString("service_pharmacy_title");
                            servicesList.add(new HospitalServiceGetterSetter(service_lab_title));
                        }







                        //String s = "12:18:00";
                        Date openTime = f1.parse(lab_open_timing);
                        Date closeTime = f1.parse(lab_close_timing);



                        if(lab_about_us.equals(""))
                        {
                            labDetailAboutHeading.setVisibility(View.GONE);
                            labDetailAbout.setVisibility(View.GONE);
                            labDetailAboutLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            labDetailAbout.setText(lab_about_us);
                        }

                        if(lab_timing_status.equals("not"))
                        {
                            labDetailTimingHeading.setVisibility(View.GONE);
                            labDetailTimingLayout.setVisibility(View.GONE);
                            labDetailTimingLayoutLine.setVisibility(View.GONE);
                        }

                        else if(lab_timing_status.equals("always"))
                        {
                            labAlwaysTiming.setVisibility(View.VISIBLE);
                            labDetailTimingLayout.setVisibility(View.GONE);
                        }

                        else if(lab_timing_status.equals("open"))
                        {
                            labDetailStartTiming.setText(f2.format(openTime));
                            labDetailEndTiming.setText(f2.format(closeTime));
                        }

                        else if(lab_timing_status.equals("other"))
                        {
                            labDetailTimingLayout.setVisibility(View.GONE);


                            HospitalTimingListRecyclerView HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(LabDetail.this , timingList , "");
                            lab_timing_other_status_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                            HospitalTimingListRecyclerView.notifyDataSetChanged();
                        }
//
//
                        if(servicesList.size() == 0)
                        {
                            LabDetailServicesHeading.setVisibility(View.GONE);
                            labDetailServicesRecyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesList);
                            labDetailServicesRecyclerView.setAdapter(hospitalsServiceListRecycleView);
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
                                LatLng sydney = new LatLng(lab_lat, lab_lng);
                                googleMap.addMarker(new MarkerOptions().position(sydney).title(labName));

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
                params.put("lab_id", labId);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


}

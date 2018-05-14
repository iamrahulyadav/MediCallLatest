package themedicall.com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.HospitalsServiceListRecycleView;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.GetterSetter.HospitalsGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.CustomTextView;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class HospitalsInformation extends Fragment{
    MapView mMapView;
    private GoogleMap googleMap;
    RecyclerView hospitalDetailServices_recycler_view;
    MapFragment mapFragment;
    RecyclerView recyclerView_hospitals, recycler_view_hospitals_doc_list;
    List<HospitalsGetterSetter> hospitalsList;
    ArrayList<HospitalMultipleDocGetterSetter> hospitalDocList;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    View customActionBarView;
    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject hosDocObject;
    JSONObject docObject;
    JSONArray basicInfoArray;
    JSONArray hosDocArray;
    private static final String TAG = "Hospitals info";
    HospitalsGetterSetter dm;
    String city;
    SharedPreferences sharedPreferences;
    private static final int MAX_LINES = 2;
    DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
    DateFormat f2 = new SimpleDateFormat("h:mma"); // 12 hours
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;

    TextView hospitalDetailAbout, hospitalDetAffiliations, hospitalDetailBeds , recyclerViewMore , recyclerViewLess;
    TextView hospitalDetailAboutHeading, hospitalDetAffiliationsHeading, hospitalDetailTimingHeading , hospitalDetailBedsHeading , hospitalDetailLocationHeading ,
            hospitalDetailServicesHeading , hospitalAlwaysTiming , hospitalStartTiming , hospitalEndTiming ;

    ImageView hospitalDetailAboutLine , hospitalDetailAffiliationsLine , hospitalTimingLayoutLine , hospitalDetailBedsLine , mapViewLine ;
    String hospital_id, hospital_name, hospital_email, hospital_contact, hospital_total_beds, hospital_timing_status, hospital_open_timing, hospital_close_timing, hospital_addr,
            hospital_address, hospital_img, hospital_cover_img, hospital_about_us, hospital_area, hospital_city, hospital_affiliation, hospital_landline, hospital_services , hospital_views ;
    public static  String hospital_share_url ;
    Double hospital_lat, hospital_lng;
    ArrayList affiliationList;
    public static ArrayList<HospitalLandLineListGetterSetter> landLineList;
    ArrayList<HospitalServiceGetterSetter> servicesList;
    ArrayList<HospitalServiceGetterSetter> servicesListMoreButton;
    LinearLayout hospitalTimingLayout ;
    CustomProgressDialog dialog;
    LinearLayout hospitalDetailMainLayout , hospitalAboutLayout ,hospitalAffiliationLayout , hospitalDetailTimingLayout , hospitalSpaceLayout , hospitalLocationLayout , hospitalServiceLayout ;
    String hospitalId , hosKM , hosAdapterKM;
    public static double roundAboutHosKm ;
    String uniqueId;
    Date openTime , closeTime ;
    public HospitalsInformation() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        if (getArguments() != null) {
//            mParam1 = savedInstanceState.getString("params");
//        }



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hospitals_information, container, false);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        hospitalDetailServices_recycler_view = (RecyclerView) view.findViewById(R.id.hospitalDetailServices_recycler_view);
        hospitalDetailServices_recycler_view.setHasFixedSize(true);
        hospitalDetailServices_recycler_view.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        uniqueId();
        initiate(view);
        getHospitalInfo(hospitalId);
        viewMoreServices();
        viewLessServices();

//        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map_hos);
//        mapFragment.getMapAsync(this);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }




        return view;
    }


    public void uniqueId() {

        uniqueId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("tag" , "imei number: "+uniqueId);
    }
    public void initiate(View view)
    {

        Intent intent = getActivity().getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            hospitalId = (String) bd.get("hospital_id");
            hosKM = (String) bd.get("hosKM");
            hosAdapterKM = (String) bd.get("hosAdapterKM");

            Log.e("tag" , "hos km in hospital info "+hosKM);
            Log.e("tag" , "hos km in hospital info from adapter "+hosAdapterKM);

        }

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);


        dialog=new CustomProgressDialog(getActivity() , 1);


        hospitalAboutLayout = (LinearLayout) view.findViewById(R.id.hospitalAboutLayout);
        hospitalAffiliationLayout = (LinearLayout) view.findViewById(R.id.hospitalAffiliationLayout);
        hospitalDetailTimingLayout = (LinearLayout) view.findViewById(R.id.hospitalDetailTimingLayout);
        hospitalSpaceLayout = (LinearLayout) view.findViewById(R.id.hospitalSpaceLayout);
        hospitalLocationLayout = (LinearLayout) view.findViewById(R.id.hospitalLocationLayout);
        hospitalServiceLayout = (LinearLayout) view.findViewById(R.id.hospitalServiceLayout);
        hospitalDetailMainLayout = (LinearLayout) view.findViewById(R.id.hospitalDetailMainLayout);

        hospitalDetailAbout = (TextView) view.findViewById(R.id.hospitalDetailAbout) ;
        hospitalDetAffiliations = (TextView) view.findViewById(R.id.hospitalDetailAffiliations) ;
        hospitalDetailBeds = (TextView) view.findViewById(R.id.hospitalDetailBeds) ;
        recyclerViewMore = (TextView) view.findViewById(R.id.recyclerViewMore);
        recyclerViewLess = (TextView) view.findViewById(R.id.recyclerViewLess);
        hospitalDetailAboutHeading = (TextView) view.findViewById(R.id.hospitalDetailAboutHeading) ;
        hospitalDetAffiliationsHeading = (TextView) view.findViewById(R.id.hospitalDetailAffiliationsHeading) ;
        hospitalDetailTimingHeading = (TextView) view.findViewById(R.id.hospitalTimingHeading) ;
        hospitalDetailBedsHeading = (TextView) view.findViewById(R.id.hospitalDetailBedHeading) ;
        hospitalDetailLocationHeading = (TextView) view.findViewById(R.id.hospitalLocationHeading) ;
        hospitalDetailServicesHeading = (TextView) view.findViewById(R.id.hospitalDetailServices_recycler_view_heading) ;
        hospitalAlwaysTiming = (TextView) view.findViewById(R.id.hospitalAlwaysTiming) ;
        hospitalStartTiming = (TextView) view.findViewById(R.id.hospitalStartTiming) ;
        hospitalEndTiming = (TextView) view.findViewById(R.id.hospitalEndTiming) ;
        hospitalTimingLayout = (LinearLayout) view.findViewById(R.id.hospitalTimingLayout);

        hospitalDetailAboutLine = (ImageView) view.findViewById(R.id.hospitalDetailAboutLine);
        hospitalDetailAffiliationsLine = (ImageView) view.findViewById(R.id.hospitalDetailAffiliationsLine);
        hospitalTimingLayoutLine = (ImageView) view.findViewById(R.id.hospitalTimingLayoutLine);
        hospitalDetailBedsLine = (ImageView) view.findViewById(R.id.hospitalDetailBedsLine);
        mapViewLine = (ImageView) view.findViewById(R.id.mapViewLine);


        affiliationList = new ArrayList();
        landLineList = new ArrayList<HospitalLandLineListGetterSetter>();
        servicesList = new ArrayList<HospitalServiceGetterSetter>();
        servicesListMoreButton = new ArrayList<HospitalServiceGetterSetter>();
    }


    public void getHospitalInfo(final String hospitalId)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospitals info";

        progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loadingImage.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.HOSPITALS_INFO_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Hospitals info Response: " + response.toString());
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        basicInfoObject = new JSONObject(response);
                        String totalDoctor = basicInfoObject.getString("total_doctors");

                        JSONObject data = basicInfoObject.getJSONObject("hospital");

                            hospital_id = data.getString("hospital_id");
                            hospital_name = data.getString("hospital_name");
                            hospital_email = data.getString("hospital_email");
                            hospital_contact = data.getString("hospital_contact");
                            hospital_total_beds = data.getString("hospital_total_beds");
                            hospital_timing_status = data.getString("hospital_timing_status");
                            hospital_open_timing = data.getString("hospital_open_timing");
                            hospital_close_timing = data.getString("hospital_close_timing");
                            hospital_addr = data.getString("hospital_addr");
                            hospital_address = data.getString("hospital_address");
                            hospital_lat = data.getDouble("hospital_lat");
                            hospital_lng = data.getDouble("hospital_lng");
                            hospital_img = data.getString("hospital_img");
                            hospital_cover_img = data.getString("hospital_cover_img");
                            hospital_about_us = data.getString("hospital_about_us");
                            hospital_share_url = data.getString("hospital_share_url");
                            hospital_views = data.getString("hospital_views");



                        if(currentLang == 0.0)
                        {
                            roundAboutHosKm = 0.0;
                        }
                        else
                        {
                            LatLng HospitalLocation = new LatLng(hospital_lat, hospital_lng);

                            Double distance = CalculationByDistance(currentLatLang , HospitalLocation);

                            roundAboutHosKm = round(distance, 1);


                            Log.e("tag" , "Hospital distance "+roundAboutHosKm);
                        }




                            HospitalDetail.hospitalKm.setText(String.valueOf(roundAboutHosKm) + " KM");
                            Picasso.with(getActivity()).load(Glob.FETCH_IMAGE_URL + "hospitals/" + hospital_img).transform(new CircleTransformPicasso()).into(HospitalDetail.hospitalProfileImg);
                            HospitalDetail.hospitalProfileName.setText(hospital_name);
                            HospitalDetail.hospitalProfileAddress.setText(hospital_address);
                            HospitalDetail.hospitalProfileNoOfDoc.setText(totalDoctor);
                            HospitalDetail.hospitalRowNoOfViews.setText(hospital_views);
                            HospitalDetail.hospitalRowProfileLink.setText(hospital_share_url);

//                            if(hosKM == null)
//                            {
//                                HospitalDetail.hospitalKm.setText(String.valueOf(hosAdapterKM));
//                            }
//                            else
//                            {
//                                HospitalDetail.hospitalKm.setText(hosKM + " KM");
//                            }



//                            JSONObject areaObject = data.getJSONObject("area");
//                            if(areaObject == null)
//                            {
//                             Log.d("tag" , "area is null");
//                            }
//                            else
//                            {
//                                hospital_area  = areaObject.getString("area_title");
//                            }

//                            JSONObject cityObject = data.getJSONObject("city");
//                            if(cityObject == null)
//                            {
//                                Log.d("tag" , "city is null");
//                            }
//                            else
//                            {
//                                hospital_city  = cityObject.getString("city_title");
//                            }


                        JSONArray affiliationsArray = data.getJSONArray("affiliations");
                        for (int k = 0; k < affiliationsArray.length(); k++) {
                            JSONObject affiliationsObject = affiliationsArray.getJSONObject(k);
                            JSONObject innerAffiliationObject = affiliationsObject.getJSONObject("affiliations");
                            hospital_affiliation = innerAffiliationObject.getString("affiliation_title");
                            affiliationList.add(hospital_affiliation);

                        }


                        Log.e("tag " , "log after affiliation");


                        JSONArray landLineArray = data.getJSONArray("landline");
                        for (int k = 0; k < landLineArray.length(); k++) {
                            JSONObject landLineObject = landLineArray.getJSONObject(k);
                            hospital_landline = landLineObject.getString("hospital_landline_number");
                            landLineList.add(new HospitalLandLineListGetterSetter(hospital_name , hospital_landline));
                        }


                        Log.e("tag " , "log after landline");


                        JSONArray servicesArray = data.getJSONArray("services");
                        for (int k = 0; k < servicesArray.length(); k++) {
                            JSONObject servicesObject = servicesArray.getJSONObject(k);
                            JSONObject innerServicesObject = servicesObject.getJSONObject("services");
                            hospital_services = innerServicesObject.getString("services_hospital_title");

                            if(k <= 3)
                            {
                                servicesListMoreButton.add(new HospitalServiceGetterSetter(hospital_services));
                            }

                            servicesList.add(new HospitalServiceGetterSetter(hospital_services));
                        }


                        Log.e("tag " , "log after service");


                        Log.e("tag" , "servicesListMoreButton "+servicesListMoreButton.size());

                            Log.e("tag " , "affiliation list" + affiliationList.toString());
                            Log.e("tag " , "landdLine list" + landLineList.toString());
                            Log.e("tag " , "services list" + servicesList.toString());
                            Log.e("tag hos" , "lat hos"+hospital_lat);
                            Log.e("tag hos" , "land hos"+hospital_lng);
                            Log.e("tag" , "hos area "+hospital_area);
                            Log.e("tag" , "hos city "+hospital_city);
                            Log.e("tag" , "hospital_timing_status "+hospital_timing_status);




//                        Picasso.with(getContext()).load(Glob.FETCH_IMAGE_URL+"hospitals/"+hospital_img).into(HospitalDetail.hospitalDetailActionBarImg);
//                        HospitalDetail.hospitalDetailActionBarName.setText(hospital_name);
//                        HospitalDetail.hospitalDetailActionBarAddress.setText(hospital_addr);


                        //String s = "12:18:00";
                        if (!hospital_open_timing.equals("null") && !hospital_close_timing.equals("null")) {
                            openTime = f1.parse(hospital_open_timing);
                            closeTime = f1.parse(hospital_close_timing);
                        }

                        Log.e("tag" , "test test test test");

                        if(hospital_about_us.equals(""))
                        {
                            hospitalDetailAboutHeading.setVisibility(View.GONE);
                            hospitalDetailAbout.setVisibility(View.GONE);
                            hospitalDetailAboutLine.setVisibility(View.GONE);
                            hospitalAboutLayout.setVisibility(View.GONE);

                            Log.e("tag" , "in about section");

                        }
                        else
                        {
                            hospitalDetailAbout.setText(hospital_about_us);
                            CustomTextView customTextView = new CustomTextView();
                            customTextView.makeTextViewResizable(hospitalDetailAbout , 2, "View More", true);

                            Log.e("tag" , "in about in else section");


                        }


                        Log.e("tag " , "log after about us");



                        if(affiliationList.size() == 0)
                        {
                            hospitalDetAffiliationsHeading.setVisibility(View.GONE);
                            hospitalDetAffiliations.setVisibility(View.GONE);
                            hospitalDetailAffiliationsLine.setVisibility(View.GONE);
                            hospitalAffiliationLayout.setVisibility(View.GONE);
                        }
                        else
                        {
                            StringBuilder builder = new StringBuilder();

                            String prefix = "";
                            for (Object str : affiliationList) {
                                builder.append(prefix);
                                prefix = " \n ";
                                builder.append(str);
                            }

                            hospitalDetAffiliations.setText(builder.toString());
                        }


                        Log.e("tag " , "log after affilation set");



                        if(hospital_timing_status.equals("not"))
                        {
                            hospitalDetailTimingHeading.setVisibility(View.GONE);
                            hospitalTimingLayout.setVisibility(View.GONE);
                            hospitalTimingLayoutLine.setVisibility(View.GONE);
                            hospitalDetailTimingLayout.setVisibility(View.GONE);
                        }
                        else if(hospital_timing_status.equals("always"))
                        {
                            hospitalAlwaysTiming.setVisibility(View.VISIBLE);
                            hospitalTimingLayout.setVisibility(View.GONE);
                        }
                        else if(hospital_timing_status.equals("open"))
                        {
                            hospitalStartTiming.setText(f2.format(openTime));
                            hospitalEndTiming.setText(f2.format(closeTime));
                        }


                        Log.e("tag " , "log after time set");



                        if(hospital_total_beds.equals("0"))
                        {
                            hospitalDetailBedsHeading.setVisibility(View.GONE);
                            hospitalDetailBeds.setVisibility(View.GONE);
                            hospitalDetailBedsLine.setVisibility(View.GONE);
                            hospitalSpaceLayout.setVisibility(View.GONE);

                        }
                        else
                        {
                            hospitalDetailBeds.setText(hospital_total_beds + " Beds");
                        }


                        Log.e("tag " , "log after set beds");




                        if(servicesListMoreButton.size() == 0)
                        {
                            hospitalDetailServicesHeading.setVisibility(View.GONE);
                            hospitalDetailServices_recycler_view.setVisibility(View.GONE);
                            recyclerViewMore.setVisibility(View.GONE);
                            hospitalServiceLayout.setVisibility(View.GONE);

                        }
                        else
                        {
                            HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesListMoreButton);
                            hospitalDetailServices_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                            hospitalsServiceListRecycleView.notifyDataSetChanged();
                            if(servicesList.size() <= 3)
                            {
                                recyclerViewMore.setVisibility(View.VISIBLE);
                            }

                        }


                        Log.e("tag " , "log after service more button");




//                        ResizableCustomView.doResizeTextView(hospitalDetailAbout, MAX_LINES, "View More", true);







                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap mMap) {
                                googleMap = mMap;

                                // For showing a move to my location button
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                LatLng sydney = new LatLng(hospital_lat, hospital_lng);
                                googleMap.addMarker(new MarkerOptions().position(sydney).title(hospital_name));

                                // For zooming automatically to the location of the marker
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {

                                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", hospital_lat , hospital_lng , "");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        intent.setPackage("com.google.android.apps.maps");
                                        try
                                        {
                                            startActivity(intent);
                                        }
                                        catch(ActivityNotFoundException ex)
                                        {
                                            try
                                            {
                                                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                startActivity(unrestrictedIntent);
                                            }
                                            catch(ActivityNotFoundException innerEx)
                                            {
                                                Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                });



                            }
                        });


                        Log.e("tag" , "end of hospital info");
                        HospitalDetail.hospitalDetailLayout.setVisibility(View.VISIBLE);
                      //  Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Hospital info Error: " + error.getMessage());
                Toast.makeText(getContext(),
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
                params.put("key", Glob.Key);
                params.put("hospital_id", hospitalId);
                params.put("uniqueId", uniqueId);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void viewMoreServices()
    {
        recyclerViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesList);
                hospitalDetailServices_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                recyclerViewLess.setVisibility(View.VISIBLE);
                recyclerViewMore.setVisibility(View.GONE);
            }
        });
    }

    public void viewLessServices()
    {
        recyclerViewLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesListMoreButton);
                hospitalDetailServices_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                recyclerViewMore.setVisibility(View.VISIBLE);
                recyclerViewLess.setVisibility(View.GONE);
            }
        });
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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



}

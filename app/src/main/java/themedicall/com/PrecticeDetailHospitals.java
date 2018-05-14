package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import themedicall.com.Adapter.HospitalTimingListRecyclerView;
import themedicall.com.Adapter.HospitalsLandLineListRecycleView;
import themedicall.com.Adapter.HospitalsServiceListRecycleView;
import themedicall.com.GetterSetter.DocPrecticeHospitalGetterSetter;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.GetterSetter.HospitalTimingGetterSetter;
import themedicall.com.Interfaces.LandLineInferface;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class PrecticeDetailHospitals extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    ProgressDialog progressDialog;
    JSONArray basicInfoArray;
    private static final String TAG = "Practice";
    public static String  doc_practice_hospital_id , doc_practice_doctor_id  , doctor_timing_start_time , doctor_timing_end_time , week_day_id , doc_practice_hospital_name , doc_practice_hospital_img , hospital_addr,
            hospital_lat , hospital_lng , week_day_title , services_hospital_title;
    public static Double hosLat , hosLang ;
    TextView practiceDetailTimingHeading , practiceDetailServiceHeading , hosPitalNum ;
    ImageView practiceDetailTimingLine ,  practiceDetailMapLine;
    String practice_doctor_id, hospital_timing;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    ArrayList<HospitalTimingGetterSetter> timingList;
    ArrayList<HospitalTimingGetterSetter> tempTimingListForMore;
    LinearLayout practiceTimingLayout , practiceLocationLayout , practiceServicesLayout ;
    public TextView TimingViewMore , TimingViewLess , ServicesViewMore , ServicesViewLess;
    Button testBtn ;
    JSONObject object;
    int fragmentPos ;
    String hosAddress ;
    String hosName ;
    String hosLatitude ;
    String hosLongitude ;
    String hosCountText;
    int hospitalDetailCount ;
    ArrayList<String> address ;
    ArrayList<String> latList ;
    ArrayList<String> langList ;
    public static TextView docPracticeAddress ;
    ArrayList<DocPrecticeHospitalGetterSetter> docPracticeList;
    HospitalTimingListRecyclerView HospitalTimingListRecyclerView ;
    HospitalsServiceListRecycleView hospitalsServiceListRecycleView;
    public static RecyclerView prectice_detail_hosp_timing_recycler_view;
    String currentDay;
    RelativeLayout openMap ;

    ArrayList<HospitalServiceGetterSetter> precticeDetailServiceList;
    ArrayList<HospitalServiceGetterSetter> tempPrecticeDetailServiceList;
    public static RecyclerView prectice_detail_service_recycler_view;
    HospitalsLandLineListRecycleView hospitalsLandLineListRecycleView;
    private int REQUEST_PHONE_CALL = 23;

    static ArrayList<String> temp;

    static LandLineInferface mLandLineInferface;

    public PrecticeDetailHospitals() {
        // Required empty public constructor
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_prectice_detail_hospitals, container, false);

        TimingViewMore = (TextView) view.findViewById(R.id.recyclerTimingViewMore);
        TimingViewLess = (TextView) view.findViewById(R.id.recyclerTimingViewLess);
        ServicesViewMore = (TextView) view.findViewById(R.id.recyclerServicesViewMore);
        ServicesViewLess = (TextView) view.findViewById(R.id.recyclerServicesViewLess);
        //openMap = (RelativeLayout) view.findViewById(R.id.openMap);

        temp = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        practiceTimingLayout = (LinearLayout) view.findViewById(R.id.practiceTimingLayout);
        practiceLocationLayout = (LinearLayout) view.findViewById(R.id.practiceLocationLayout);
        practiceServicesLayout = (LinearLayout) view.findViewById(R.id.practiceServicesLayout);

        hosPitalNum = (TextView) view.findViewById(R.id.hosPitalNum);
        docPracticeAddress = (TextView) view.findViewById(R.id.docPracticeAddress);
        practiceDetailTimingHeading = (TextView) view.findViewById(R.id.practiceDetailTimingHeading);
        practiceDetailServiceHeading = (TextView) view.findViewById(R.id.practiceDetailServiceHeading);
        practiceDetailTimingLine = (ImageView) view.findViewById(R.id.practiceDetailTimingLine);
        practiceDetailMapLine = (ImageView) view.findViewById(R.id.practiceDetailMapLine);
        prectice_detail_hosp_timing_recycler_view = (RecyclerView) view.findViewById(R.id.prectice_detail_hosp_timing_recycler_view);
        prectice_detail_hosp_timing_recycler_view.setHasFixedSize(true);
        prectice_detail_hosp_timing_recycler_view.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));


        prectice_detail_service_recycler_view = (RecyclerView) view.findViewById(R.id.prectice_detail_hosp_service_recycler_view);
        prectice_detail_service_recycler_view.setHasFixedSize(true);
        prectice_detail_service_recycler_view.setLayoutManager(new GridLayoutManager(getContext() , 2 ,GridLayoutManager.VERTICAL, false ));




        docPracticeList = new ArrayList<>();
        precticeDetailServiceList = new ArrayList<>();
        tempPrecticeDetailServiceList = new ArrayList<>();
        address = new ArrayList<>();
        latList = new ArrayList<>();
        langList = new ArrayList<>();
        timingList = new ArrayList<>();
        landLineList = new ArrayList<HospitalLandLineListGetterSetter>();





        mMapView = (MapView) view.findViewById(R.id.practiceMapView);
        mMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "map fragment", Toast.LENGTH_SHORT).show();
            }
        });
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }




        getCurrentDay();
        readBundle(getArguments());
        DoctorDetail doctorDetail = new DoctorDetail();
//        doctorDetail.onClickLandlin(landLineList , view);
        hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(getActivity(), landLineList);
        //onClickForCall(hospitalsLandLineListRecycleView);




       /* practiceDetailTimingHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag" , "landline list in prectice detail hospital in button " + landLineList.size());

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Phone Numbers");
                ListView cityList = (ListView) dialog.findViewById(R.id.cityList);
                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
                search_view.setVisibility(View.GONE);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                dialog.show();

                hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(getActivity(), landLineList);
                cityList.setAdapter(hospitalsLandLineListRecycleView);
                cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView textView = (TextView) view.findViewById(R.id.landLineList);
                        String phoneNumber = textView.getText().toString();
                        //Toast.makeText(mContext, "phone number "+phoneNumber, Toast.LENGTH_SHORT).show();

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                            return;
                        }

                        getActivity().startActivity(callIntent);
                        dialog.dismiss();

                    }
                });

            }
        });
*/

        viewMoreTiming(view);
        viewLessTiming(view);
        viewMoreServices(view);
        viewLessServices(view);


        Log.e("tag" , "hospitalDetailCount "+hospitalDetailCount);

        Log.e("tag" , "viewPage count " +DocPrecticeDetailFragment.viewPager.getCurrentItem());




            if (timingList.size() == 0) {
                practiceTimingLayout.setVisibility(View.GONE);
                practiceDetailTimingLine.setVisibility(View.GONE);
                TimingViewMore.setVisibility(View.GONE);
                prectice_detail_hosp_timing_recycler_view.setVisibility(View.GONE);

            } else {

                if (timingList.size() == 1) {

                    Log.e("tag", "sub list size: in 1 " + timingList.size());

                    HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(getActivity() , timingList , currentDay);
                    prectice_detail_hosp_timing_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                    HospitalTimingListRecyclerView.notifyDataSetChanged();
                    TimingViewMore.setVisibility(View.GONE);


                } else if (timingList.size() == 2) {

                    Log.e("tag", "sub list size: in 2 " + timingList.size());


                    HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(getActivity() , timingList , currentDay);
                    prectice_detail_hosp_timing_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                    HospitalTimingListRecyclerView.notifyDataSetChanged();
                    TimingViewMore.setVisibility(View.GONE);


                } else {
                    tempTimingListForMore = new ArrayList<>(timingList.subList(0 , 2));
                    HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(getActivity() , tempTimingListForMore , currentDay);
                    prectice_detail_hosp_timing_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                    HospitalTimingListRecyclerView.notifyDataSetChanged();
                    TimingViewMore.setVisibility(View.VISIBLE);

                }

            }

        docPracticeAddress.setText(hosAddress);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if(precticeDetailServiceList.size() == 0)
        {
            practiceServicesLayout.setVisibility(View.GONE);
            practiceDetailMapLine.setVisibility(View.GONE);
            ServicesViewMore.setVisibility(View.GONE);
            prectice_detail_service_recycler_view.setVisibility(View.GONE);

        }
        else
        {

            if (precticeDetailServiceList.size() == 1) {

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(precticeDetailServiceList);
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.GONE);


            } else if (precticeDetailServiceList.size() == 2) {

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(precticeDetailServiceList);
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.GONE);


            }
            else if (precticeDetailServiceList.size() == 3) {

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(precticeDetailServiceList);
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.GONE);


            }
            else if (precticeDetailServiceList.size() == 4) {

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(precticeDetailServiceList);
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.GONE);

            }else {

                tempPrecticeDetailServiceList = new ArrayList<>(precticeDetailServiceList.subList(0 , 4));
                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(tempPrecticeDetailServiceList);
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.VISIBLE);

            }
        }



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
                LatLng sydney = new LatLng(Double.parseDouble(hosLatitude) , Double.parseDouble(hosLongitude));
                googleMap.addMarker(new MarkerOptions().position(sydney).title(hosName)).showInfoWindow();
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Double.parseDouble(hosLatitude), Double.parseDouble(hosLongitude), "");
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



//        openMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
////                        Uri.parse("http://maps.google.com/maps?saddr=31.471100,74.241920&daddr=31.5536,74.3304"));
////                startActivity(intent);
//
//                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 31.5536, 74.3304, "Where the party is at");
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                intent.setPackage("com.google.android.apps.maps");
//                startActivity(intent);
//            }
//        });



        //Toast.makeText(getContext(), "hos name  "+DocPrecticeDetailFragment.hospital_name, Toast.LENGTH_SHORT).show();
//        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.practice_hos);
//        mapFragment.getMapAsync(this);

        //getDocPrectice();

        return view;
    }




    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void getCurrentDay()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        currentDay = simpleDateFormat.format(date);
        Log.e("tag" , "current day :  "+currentDay);
    }

    public static PrecticeDetailHospitals newInstance(String address, String name , String lat  , String lang , int hospitalDetailCount , ArrayList<HospitalLandLineListGetterSetter> landLineList , ArrayList<HospitalTimingGetterSetter> timings, ArrayList<HospitalServiceGetterSetter> serviceList) {
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        bundle.putString("name", name);
        bundle.putString("lat", lat);
        bundle.putString("lang", lang);
        bundle.putInt("hospitalDetailCount", hospitalDetailCount);
        bundle.putInt("landLineList", hospitalDetailCount);
        bundle.putParcelableArrayList("landLineList", landLineList);
        bundle.putParcelableArrayList("hospitaltimings", timings);
        bundle.putParcelableArrayList("service", serviceList);

        PrecticeDetailHospitals fragment = new PrecticeDetailHospitals();

        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {

            hosAddress = bundle.getString("address");
            hosName = bundle.getString("name");
            hosLatitude = bundle.getString("lat");
            hosLongitude = bundle.getString("lang");
            hospitalDetailCount = bundle.getInt("hospitalDetailCount");
            landLineList = bundle.getParcelableArrayList("landLineList");
            timingList = bundle.getParcelableArrayList("hospitaltimings");
            precticeDetailServiceList  = bundle.getParcelableArrayList("service");

            Log.e("TAG", "timing lis sizing: " + timingList.size());

            Log.e("tag" , "landline list in prectice detail hospital "+landLineList.size());

        }
    }

    public void viewMoreTiming(final View v)
    {
        TimingViewMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("tag" , "view more click ");
                HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(getActivity() ,timingList , currentDay);
                prectice_detail_hosp_timing_recycler_view = (RecyclerView) v.findViewById(R.id.prectice_detail_hosp_timing_recycler_view);
                prectice_detail_hosp_timing_recycler_view.setHasFixedSize(true);
                prectice_detail_hosp_timing_recycler_view.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                prectice_detail_hosp_timing_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                HospitalTimingListRecyclerView.notifyDataSetChanged();
                TimingViewLess.setVisibility(View.VISIBLE);
                TimingViewMore.setVisibility(View.GONE);


            }
        });
    }

    public void viewLessTiming(final View v)
    {
        TimingViewLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag" , "view less click ");
                HospitalTimingListRecyclerView = new HospitalTimingListRecyclerView(getActivity() , tempTimingListForMore , currentDay);
                prectice_detail_hosp_timing_recycler_view = (RecyclerView) v.findViewById(R.id.prectice_detail_hosp_timing_recycler_view);
                prectice_detail_hosp_timing_recycler_view.setHasFixedSize(true);
                prectice_detail_hosp_timing_recycler_view.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));
                prectice_detail_hosp_timing_recycler_view.setAdapter(HospitalTimingListRecyclerView);
                HospitalTimingListRecyclerView.notifyDataSetChanged();
                TimingViewMore.setVisibility(View.VISIBLE);
                TimingViewLess.setVisibility(View.GONE);

            }
        });
    }

    public void viewMoreServices(final View v)
    {
        ServicesViewMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("tag" , "view more click ");

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(precticeDetailServiceList);
                prectice_detail_service_recycler_view = (RecyclerView) v.findViewById(R.id.prectice_detail_hosp_service_recycler_view);
                prectice_detail_service_recycler_view.setHasFixedSize(true);
                prectice_detail_service_recycler_view.setLayoutManager(new GridLayoutManager(getContext() , 2 ,GridLayoutManager.VERTICAL, false ));
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewLess.setVisibility(View.VISIBLE);
                ServicesViewMore.setVisibility(View.GONE);


            }
        });
    }

    public void viewLessServices(final View v)
    {
        ServicesViewLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("tag" , "view less click ");

                hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(tempPrecticeDetailServiceList);
                prectice_detail_service_recycler_view = (RecyclerView) v.findViewById(R.id.prectice_detail_hosp_service_recycler_view);
                prectice_detail_service_recycler_view.setHasFixedSize(true);
                prectice_detail_service_recycler_view.setLayoutManager(new GridLayoutManager(getContext() , 2 ,GridLayoutManager.VERTICAL, false ));
                prectice_detail_service_recycler_view.setAdapter(hospitalsServiceListRecycleView);
                hospitalsServiceListRecycleView.notifyDataSetChanged();
                ServicesViewMore.setVisibility(View.VISIBLE);
                ServicesViewLess.setVisibility(View.GONE);

            }
        });
    }

        @Override
    public void onResume() {

        super.onResume();
//            DoctorDetail doctorDetail = new DoctorDetail();
//            doctorDetail.onClickLandlin(landLineList);

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

    public void onClickForCall(final HospitalsLandLineListRecycleView hospitalsLandLineList)
    {
        DoctorDetail.doctorRowPhoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Phone Numbers");
                ListView cityList = (ListView) dialog.findViewById(R.id.cityList);
                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
                search_view.setVisibility(View.GONE);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                dialog.show();

                //hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(getActivity(), lList);
                cityList.setAdapter(hospitalsLandLineList);
                cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        TextView textView = (TextView) view.findViewById(R.id.landLineList);
                        String phoneNumber = textView.getText().toString();
                        //Toast.makeText(mContext, "phone number "+phoneNumber, Toast.LENGTH_SHORT).show();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phoneNumber));
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                            return;
                        }
                        getActivity().startActivity(callIntent);
                        dialog.dismiss();

                    }
                });
            }
        });
    }
}

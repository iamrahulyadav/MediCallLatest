package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.HospitalsLandLineListRecycleView;
import themedicall.com.GetterSetter.DocPrecticeHospitalGetterSetter;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.GetterSetter.HospitalTimingGetterSetter;
import themedicall.com.GetterSetter.SelectSpecialityGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
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
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class DocPrecticeDetailFragment extends Fragment
{
    public boolean docPracticeDetailServiceHasRun = false;
    ProgressDialog progressDialog;
    JSONObject hospitalObject;
    JSONObject innerQualificationObject;
    JSONObject innerSpecialityObject;
    JSONObject weekObject;
    JSONObject object;
    JSONArray hospitalArray;
    private static final String TAG = "Hospital";
    public static String  hospital_id , doctor_id  , hospital_name , hospital_img ;
    public static String hospitalName ;
    ArrayList<SelectSpecialityGetterSetter> hospitalNameArray  = new ArrayList<>();
    HashMap<String , String> hashMap = new HashMap<String, String>();
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    public static ArrayList<DocPrecticeHospitalGetterSetter> docPracticeList;
    ArrayList<DocPrecticeHospitalGetterSetter> docPracticeListSecond;
    DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
    DateFormat f2 = new SimpleDateFormat("h:mma"); // 12 hours
    Date openTime , closeTime ;
    public int myTAG;
    CustomProgressDialog dialog;
    int hospitalDetailCount ;
    String uniqueId;

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    JSONObject practiceObject;
    JSONObject docPracticeHospitalObject;
    JSONObject docPracticeWeekObject;
    JSONArray basicInfoArray;
    JSONArray baisArryHospitals;

    public static String  doc_practice_hospital_id , doc_practice_doctor_id  , doctor_timing_start_time , doctor_timing_end_time , week_day_id , doc_practice_hospital_name , doc_practice_hospital_img , hospital_addr,
            hospital_lat , hospital_lng , week_day_title , service_title , hospital_landline_id ,  hospital_landline_number;
    String doc_id , doctor_full_name , doctor_img , doctor_min_fee , docor_max_fee , doctor_experience , doctor_offer_any_discount , doctor_verified_status , doctor_qualification , speciality_designation , doctor_profile_percent , doctor_views ;
    Double rating ;
    public static String doctor_url;
    TextView noPracticeDetail;

    ArrayList<String> docQualificationList;
    ArrayList<String> docSpecialityList;
    HospitalsLandLineListRecycleView hospitalsLandLineListRecycleView;
    private int REQUEST_PHONE_CALL = 23;

    // ArrayList<HospitalServiceGetterSetter> timingListList;
    ArrayList<HospitalServiceGetterSetter> precticeDetailServiceList;
    ArrayList<HospitalTimingGetterSetter> timingList;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;

    ViewPagerAdapter adapter ;
    String docId , docKM  , docAdapterKM;

    String practice_doctor_id, hospital_timing;
    int hosNameCounter ;

    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    double lat , lang ;
    double roundAboutKm;
    public DocPrecticeDetailFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_doc_prectice_detail, container, false);

        dialog=new CustomProgressDialog(getActivity(), 1);
        uniqueId();

        Intent intent = getActivity().getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            docId = (String) bd.get("docId");
            Log.e("tag" , "doctor id is : "+docId);
            docKM = (String) bd.get("docKM");
            docAdapterKM = (String) bd.get("docAdapterKM");
            Log.e("tag" , "doc km : "+docKM);
            Log.e("tag" , "doc km from adapter : "+docAdapterKM);
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        ///
        docPracticeList = new ArrayList<>();
        //timingListList = new ArrayList<>();

        docQualificationList = new ArrayList<>();
        docSpecialityList = new ArrayList<>();
        docPracticeListSecond =  new ArrayList<>();
        precticeDetailServiceList = new ArrayList<>();

//        if(docPracticeHasRun)
//        {
//            Log.d("tag" , "Doc Practice Detail All ready Run");
//        }
//        else
//        {
//            getDocHospitals();
//            docPracticeHasRun = true ;
//        }

        noPracticeDetail = (TextView) view.findViewById(R.id.noPracticeDetail);
        noPracticeDetail.setVisibility(View.GONE);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(2);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);

        onClickLandlin();

        return view;
    }

    public void uniqueId() {

        uniqueId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("tag" , "imei number: "+uniqueId);
    }




    private void setupTabIcons(int hosNameCounter) {
        for(int i = 0 ; i < hosNameCounter ; i++)
        {

            final LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_layout, null);
            final TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabContent);
            final ImageView img = (ImageView) tabLinearLayout.findViewById(R.id.tabImage);

            //tabLayout.getTabAt(i).setIcon(tabIcons[i]);
            final int finalI = i;

//            Glide
//                    .with(getActivity())
//                    .load(Glob.FETCH_IMAGE_URL +  "hospitals/" + hospitalNameArray.get(i).getSpeciality_image())
//                    .asBitmap()
//                    .into(new SimpleTarget<Bitmap>(100,100) {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
//                            Log.e("TAG", "The bitmap: " + resource);
//                            Drawable drawable = new BitmapDrawable(getResources() , resource);
//                            tabContent.setText(hospitalNameArray.get(finalI).getSpeciality_name());
//                            img.setImageBitmap(resource);
//                            //tabContent.setCompoundDrawablesWithIntrinsicBounds(drawable , 0 , 0 , 0);
////                            drawable.setBounds( 0, 0, 50, 50 );
////                            tabContent.setCompoundDrawables(drawable , null , null , null);
////                            tabContent.setCompoundDrawablePadding(10);
//                            tabLayout.getTabAt(finalI).setCustomView(tabLinearLayout);
//                        }
//                    });




            Picasso.with(getActivity()).load(Glob.FETCH_IMAGE_URL +  "hospitals/" + docPracticeList.get(finalI).getDoc_practice_hospital_img()).transform(new CircleTransformPicasso()).into(img);
            if(docPracticeList.get(finalI).getDoc_practice_hospital_name().length() > 26)
            {
                String smallString = docPracticeList.get(finalI).getDoc_practice_hospital_name().substring(0 ,25)+" ...";
                tabContent.setText(smallString);
            }
            else
            {
                tabContent.setText(docPracticeList.get(finalI).getDoc_practice_hospital_name());
            }
            tabLayout.getTabAt(finalI).setCustomView(tabLinearLayout);

        }
    }

//    private View createTabItemView(String imgUri) {
//        ImageView imageView = new ImageView(getContext(
//        TabLayout.LayoutParams params = new TabLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(params);
//        Picasso.with(getContext()).load(Glob.FETCH_IMAGE_URL + "hospitals/" + hospitalsList.get(position).getHospitalProfileImg()).into(imageView);
//
//        return imageView;
//    }

    private void setupViewPager(final ViewPager viewPager) {

        if(docPracticeDetailServiceHasRun)
        {
            Log.e("tag" , "doctor practice detail service all ready Run");
        }
        else {

            getDocPrectice(new CallBack() {
                @Override
                public void onSuccess(ArrayList<DocPrecticeHospitalGetterSetter> docterListOfPracters) {

                    Log.e("TAG", "I am Shoaib " + docPracticeList.size());

                    adapter = new ViewPagerAdapter(getChildFragmentManager(), docPracticeList);


                    for (hosNameCounter = 0; hosNameCounter < docPracticeList.size(); hosNameCounter++) {

                        //hospitalName = (String) hashMap.get("hos_name") ;

                        Log.e("tag", "get hospital practice name: " + docPracticeList.get(hosNameCounter).getDoc_practice_hospital_name());
                        Log.e("tag", "get hospital detail id num : " + docPracticeList.get(hosNameCounter).getHospitalDetailCount());

                        //Log.d("tag " , "hospital name "+hospitalName);

                        // Picasso.with(getActivity()).load(Glob.FETCH_IMAGE_URL + "hospitals/" + hospitalNameArray.get(i).getSpeciality_image());

                        //getDocPrectice(i);
                        adapter.addFragment(new PrecticeDetailHospitals(), docPracticeList.get(hosNameCounter).getDoc_practice_hospital_name());
                        viewPager.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        Log.e("tag", "view pager count in main fragment: " + viewPager.getCurrentItem());

                        PrecticeDetailHospitals.newInstance(docPracticeList.get(hosNameCounter).getHospital_addr(), docPracticeList.get(hosNameCounter).getDoc_practice_hospital_name(), docPracticeList.get(hosNameCounter).getHospital_lat(), docPracticeList.get(hosNameCounter).getHospital_lng(), docPracticeList.get(hosNameCounter).getHospitalDetailCount(), docPracticeList.get(hosNameCounter).getLandLineList(), docPracticeList.get(hosNameCounter).getTimingList(), docPracticeList.get(hosNameCounter).getPrecticeDetailServiceList());

//                    PrecticeDetailHospitals precticeDetailHospitals = new PrecticeDetailHospitals();
//                    precticeDetailHospitals.viewMoreTiming();
//                    precticeDetailHospitals.viewLessTiming();


                    }
                    setupTabIcons(hosNameCounter);

                }

                @Override
                public void onFail(String msg) {

                }
            });
        }
    }

//    private void setupTabIcons(ArrayList<SelectSpecialityGetterSetter> hospitalNameArray) {
//        tabLayout.getTabAt(i).setIcon(tabIcons[i]);
//    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private List<DocPrecticeHospitalGetterSetter> list;


        int mNumOfTabs;
        Fragment fragment = null;

        public ViewPagerAdapter(FragmentManager manager, List<DocPrecticeHospitalGetterSetter> list) {
            super(manager);
            this.list = list;

        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);


        }

        @Override
        public Fragment getItem(int position) {

            DocPrecticeHospitalGetterSetter item = list.get(position);

            Log.e("TAG", "THE MESSAGE IN GETITEM: " + item.getPrecticeDetailServiceList().size());

            return PrecticeDetailHospitals.newInstance(item.getHospital_addr()  , item.getDoc_practice_hospital_name() , item.getHospital_lat()  , item.getHospital_lng() , item.getHospitalDetailCount() , item.getLandLineList() , item.getTimingList(), item.getPrecticeDetailServiceList());

            //return mFragmentList.get(position);
        }
        
        

        @Override
        public int getItemPosition(Object object) {
            Log.e("TAG", "The current Object is: " + object);
            return super.getItemPosition(object);


        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void getDocHospitals()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospital Name";

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DOCTOR_HOSPITAL_DETAIL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Hospital Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        object = new JSONObject(response);

                        JSONObject hospital = object.getJSONObject("doctors");

                        hospitalArray = hospital.getJSONArray("hospitals");

                        for (int i = 0; i < hospitalArray.length(); i++) {

                            JSONObject practiceObject = hospitalArray.getJSONObject(i);

                            hospital_id = practiceObject.getString("hospital_id");
                            doctor_id = practiceObject.getString("doctor_id");

                            //Log.d("tag id" , "hos id "+hospital_id);
                            // Log.d("tag id" , "doc id "+doctor_id);

                            hospitalObject = practiceObject.getJSONObject("hospitals");
                            hospital_name = hospitalObject.getString("hospital_name");
                            hospital_img = hospitalObject.getString("hospital_img");

                            Log.e("TAG", "The Titlt for Hospital Name in first service: " + hospital_name);


                            hospitalNameArray.add(new SelectSpecialityGetterSetter(hospital_id , hospital_name , hospital_img));

                        }





                        Log.e("tag" , "hospital size size "+hospitalNameArray.size());
                        // Log.e("tag" , "hospital Name and Id "+hashMap.toString());

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getContext(), "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("doctor_id", docId);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getContext()) .addToRequestQueue(strReq, cancel_req_tag);
    }




    public void getDocPrectice( final CallBack callBack)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "Practice";

        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DOCTOR_PRACTICE_DETAIL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Practice Response: " + response.toString());
                dialog.dismiss();

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        practiceObject = new JSONObject(response);
                        JSONObject practice = practiceObject.getJSONObject("doctors");
                        doc_id = practice.getString("doctor_id");
                        doctor_full_name = practice.getString("doctor_full_name");
                        doctor_img = practice.getString("doctor_img");
                        doctor_min_fee = practice.getString("doctor_min_fee");
                        docor_max_fee = practice.getString("docor_max_fee");
                        doctor_experience = practice.getString("doctor_experience");
                        doctor_offer_any_discount = practice.getString("doctor_offer_any_discount");
                        doctor_verified_status = practice.getString("doctor_verified_status");
                        doctor_views = practice.getString("doctor_views");
                        doctor_url = practice.getString("doctor_url");
                        Log.e("tag" , "doc name " +doctor_full_name);
                        Log.e("tag" , "doctor_views " +doctor_views);



                        DoctorDetail.doctorRowId.setText(doc_id);

                        Picasso.with(getActivity()).load(Glob.FETCH_IMAGE_URL + "doctors/" + doctor_img).transform(new CircleTransformPicasso()).into(DoctorDetail.doctorRowProfileImg);


//                        if(doctor_views.equals(null) || doctor_views.equals(""))
//                        {
//                            DoctorDetail.doctorRowNoOfViews.setText("0");
//                        }
//                        else
//                        {
                            DoctorDetail.doctorRowNoOfViews.setText(doctor_views);
                       // }



                        if (doctor_experience.contains("00")) {
                            DoctorDetail.doctorRowExperience.setVisibility(View.GONE);
                        } else {
                            DoctorDetail.doctorRowExperience.setText(doctor_experience);

                        }


//                        if(docKM == null)
//                        {
//                            DoctorDetail.doctorRowDistance.setText(docAdapterKM);
//                        }
//                        else if(docAdapterKM == null)
//                        {
//                            DoctorDetail.doctorRowDistance.setText(docKM +" KM");
//                        }



                        if (doctor_full_name.contains("Dr.")) {
                            DoctorDetail.doctorRowName.setText(doctor_full_name);

                        } else {
                            DoctorDetail.doctorRowName.setText("Dr. " + doctor_full_name);
                        }


                        if(doctor_offer_any_discount.contains("Yes"))
                        {
                            DoctorDetail.doctorRowDiscountedImg.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            DoctorDetail.doctorRowDiscountedImg.setVisibility(View.GONE);
                        }

                        if(doctor_verified_status.contains("1"))
                        {
                            DoctorDetail.verifiedDoc.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            DoctorDetail.verifiedDoc.setVisibility(View.GONE);
                        }

                        if (doctor_min_fee.equals(docor_max_fee)) {
                            DoctorDetail.doctorRowFees.setText("Fee : " + doctor_min_fee);
                        } else {
                            DoctorDetail.doctorRowFees.setText("Fee : " + doctor_min_fee + " - " + docor_max_fee);
                        }



                        JSONArray ratingArray = practice.getJSONArray("ratings");
                        for (int m = 0; m < ratingArray.length(); m++) {
                            JSONObject ratingObject = ratingArray.getJSONObject(m);
                            rating = ratingObject.getDouble("rating");

                            Log.e("tag" , "rating rating rating : "+rating);
                        }

                        if(rating == null)
                        {
                            DoctorDetail.doctorRowRating.setRating(0);
                        }
                        else
                        {
                            double roundAboutKm = round(rating, 1);
                            DoctorDetail.doctorRowRating.setRating(Float.parseFloat(String.valueOf(roundAboutKm)));
                        }



                        JSONArray doctorSpecialityArray = practice.getJSONArray("speciality");
                        for (int l = 0; l < doctorSpecialityArray.length(); l++) {
                            JSONObject specialityObject = doctorSpecialityArray.getJSONObject(l);
                            innerSpecialityObject = specialityObject.getJSONObject("speciality");
                            speciality_designation = innerSpecialityObject.getString("speciality_designation");
                            docSpecialityList.add(speciality_designation);

                        }

                        Log.e("tag" , "doc speciality "+docSpecialityList.size());


                        StringBuilder specialistBuilder = new StringBuilder();
                        StringBuilder QualificationBuilder = new StringBuilder();
                        String specialistPrefix = "";
                        String qualificationPrefix = "";

                        for (Object str : docSpecialityList) {
                            specialistBuilder.append(specialistPrefix);
                            specialistPrefix = " , ";
                            specialistBuilder.append(str);
                        }



                        JSONArray doctorQualificationArray = practice.getJSONArray("qualifications");
                        for (int k = 0; k < doctorQualificationArray.length(); k++) {
                            JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                            innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                            doctor_qualification = innerQualificationObject.getString("qualification_title");
                            docQualificationList.add(doctor_qualification);

                        }
                        Log.e("tag" , "doc qualification "+docQualificationList.size());

                        for (Object str : docQualificationList) {
                            QualificationBuilder.append(qualificationPrefix);
                            qualificationPrefix = " , ";
                            QualificationBuilder.append(str);
                        }





                        if(specialistBuilder.toString().equals(""))
                        {
                            DoctorDetail.doctorRowSpeciality.setVisibility(View.GONE);
                        }
                        else
                        {
                            DoctorDetail.doctorRowSpeciality.setText(specialistBuilder.toString());
                        }




                        if(QualificationBuilder.toString().equals(""))
                        {
                            DoctorDetail.doctorRowDegree.setVisibility(View.GONE);
                        }
                        else
                        {
                            DoctorDetail.doctorRowDegree.setText(QualificationBuilder.toString());
                        }





                        JSONArray servicesArray = practice.getJSONArray("services");
                        for (int k = 0; k < servicesArray.length(); k++) {
                            JSONObject servicesObject = servicesArray.getJSONObject(k);
                            JSONObject innerservicesObjectObject = servicesObject.getJSONObject("services");
                            service_title = innerservicesObjectObject.getString("service_title");

                            Log.e("TAG", "Hopstial service: " + service_title);
                            precticeDetailServiceList.add(new HospitalServiceGetterSetter(service_title));
                        }




                        baisArryHospitals = practice.getJSONArray("hospitals");

                        if(baisArryHospitals.length() == 0)
                        {
                            Log.e("tag" , "doc hospital practice size is : "+baisArryHospitals.length());
                            noPracticeDetail.setVisibility(View.VISIBLE);

                            DoctorDetail.doctorRowDistance.setText(String.valueOf(0.0) + " KM");
                        }
                        else {

                            for (hospitalDetailCount = 0; hospitalDetailCount < baisArryHospitals.length(); hospitalDetailCount++) {


                                timingList = new ArrayList<>();
                                landLineList = new ArrayList<HospitalLandLineListGetterSetter>();


                                JSONObject practiceObject = baisArryHospitals.getJSONObject(hospitalDetailCount);
                                docPracticeHospitalObject = practiceObject.getJSONObject("hospitals");
                                String hospitalId = docPracticeHospitalObject.getString("hospital_id");
                                doc_practice_hospital_name = docPracticeHospitalObject.getString("hospital_name");
                                doc_practice_hospital_img = docPracticeHospitalObject.getString("hospital_img");
                                hospital_addr = docPracticeHospitalObject.getString("hospital_addr");
                                hospital_lat = docPracticeHospitalObject.getString("hospital_lat");
                                hospital_lng = docPracticeHospitalObject.getString("hospital_lng");


                                Log.e("tag", "hospital lat in practice : " + hospital_lat);
                                Log.e("tag", "hospital lag in practice : " + hospital_lng);

                                Log.e("TAG", "The Titlt for Hospital Name: " + doc_practice_hospital_name);


                                JSONArray landLineArray = docPracticeHospitalObject.getJSONArray("landline");
                                for (int k = 0; k < landLineArray.length(); k++) {

                                    JSONObject landLineObject = landLineArray.getJSONObject(k);
                                    hospital_landline_id = landLineObject.getString("hospital_id");
                                    hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                    landLineList.add(new HospitalLandLineListGetterSetter(doc_practice_hospital_name, hospital_landline_number));

                                }


                                JSONArray practicesArray = docPracticeHospitalObject.getJSONArray("practices");
                                for (int j = 0; j < practicesArray.length(); j++) {

                                    JSONObject practiceDetailObject = practicesArray.getJSONObject(j);
                                    doc_practice_hospital_id = practiceDetailObject.getString("hospital_id");
                                    doc_practice_doctor_id = practiceDetailObject.getString("doctor_id");
                                    doctor_timing_start_time = practiceDetailObject.getString("doctor_timing_start_time");
                                    doctor_timing_end_time = practiceDetailObject.getString("doctor_timing_end_time");

                                    JSONObject weekDaytitle = practiceDetailObject.getJSONObject("week_days");
                                    week_day_id = weekDaytitle.getString("week_day_id");
                                    week_day_title = weekDaytitle.getString("week_day_title");


                                    Log.e("TAG", "The Titlt for Week Day is: " + week_day_title);

                                    try {

                                        openTime = f1.parse(doctor_timing_start_time);
                                        closeTime = f1.parse(doctor_timing_end_time);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (doctor_timing_start_time.equals(doctor_timing_end_time)) {
                                        hospital_timing = "N/A";
                                        Log.e("tag", "hospital timing in if " + hospital_timing);

                                    } else {
                                        hospital_timing = f2.format(openTime) + " - " + f2.format(closeTime);
                                        Log.e("tag", "hospital timing in else " + hospital_timing);
                                    }


                                    timingList.add(new HospitalTimingGetterSetter(week_day_title, hospital_timing));

                                }

                                docPracticeList.add(new DocPrecticeHospitalGetterSetter(doc_practice_hospital_id, doc_practice_doctor_id, doc_practice_hospital_name, doc_practice_hospital_img
                                        , hospital_addr, hospital_lat, hospital_lng, hospitalDetailCount, landLineList, precticeDetailServiceList, timingList));


                                Log.e("tag", "practice array size  " + docPracticeList.size());
                                Log.e("tag", "practice array size of service  " + precticeDetailServiceList.size());
                                Log.e("tag", "practice array size of timing   " + timingList.size());
                                Log.e("tag", "practice array size of landline   " + landLineList.size());

                            }

                            lat = Double.valueOf(docPracticeList.get(0).getHospital_lat()) ;
                            lang = Double.valueOf(docPracticeList.get(0).getHospital_lng()) ;
                            LatLng AllDocLocation = new LatLng(lat ,lang );


                            if(currentLang == 0.0)
                            {
                                roundAboutKm = 0.0;
                            }
                            else
                            {
                                Double distance = CalculationByDistance(currentLatLang , AllDocLocation);
                                Log.e("tag" , "All hospital distance in practice"+distance);
                                roundAboutKm = round(distance, 1);

                            }


                            DoctorDetail.doctorRowDistance.setText(String.valueOf(roundAboutKm) + " KM");


                            cliaimOrReportIconClickHandler();


                            Log.e("TAG", "The size of service List after loop timing : " + docPracticeList.get(0).getTimingList().size());
                            Log.e("TAG", "The size of service List after loop practice: " + docPracticeList.get(0).getPrecticeDetailServiceList().size());
                            Log.e("tag" , "practice array size "+docPracticeList.size());
                            Log.e("tag" , "basic array hospital size "+baisArryHospitals.length());


                            callBack.onSuccess(docPracticeList);

                        }

                        Log.e("tag" , "hi friends iam out of condition");

                        docPracticeDetailServiceHasRun = true ;

                        DoctorDetail.doctorDetailLayout.setVisibility(View.VISIBLE);





                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getContext(), "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("doctor_id", docId);
                params.put("uniqueId", uniqueId);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getContext()) .addToRequestQueue(strReq, cancel_req_tag);


    }

    public interface CallBack {
        void onSuccess(ArrayList<DocPrecticeHospitalGetterSetter> docterListOfPracters);
        void onFail(String msg);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void onClickLandlin()
    {

        DoctorDetail.doctorRowPhoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(DoctorDetail.this, "button click " +temArray.size(), Toast.LENGTH_SHORT).show();
                int pos = viewPager.getCurrentItem();
                Log.e("TAG", "the Fragment position is: "+ pos );
                Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, pos);
                ArrayList<HospitalLandLineListGetterSetter> test =  fragment.getArguments().getParcelableArrayList("landLineList");

                Log.e("TAG", "the Fragment "+ test.size() );




                if(test.size() == 0)
                {
                    Toast.makeText(getActivity(), "Contact detail not found", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custom_citylist_search);
                    dialog.setTitle("Phone Numbers");
                    ListView cityList = (ListView) dialog.findViewById(R.id.cityList);
                    SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
                    search_view.setVisibility(View.GONE);
                    Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                    bt_dilaog_done.setVisibility(View.GONE);
                    dialog.show();

                    hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(getActivity(), test);
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

                            startActivity(callIntent);
                            dialog.dismiss();

                        }
                    });
                }


            }
        });


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

    public void cliaimOrReportIconClickHandler(){


        //click listner for pencial icon


        DoctorDetail.im_pencila_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docId = DoctorDetail.doctorRowId.getText().toString();
                Log.e("TAG", "on pencial click doctor id is: " + docId);

                final Dialog claimDialog = new Dialog(getActivity());
                claimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                claimDialog.setContentView(R.layout.custome_claim_profile_from_doctor_listing);
                TextView tv_dialog_title = (TextView) claimDialog.findViewById(R.id.tv_dialog_title);
                final RadioGroup radioGroup = (RadioGroup) claimDialog.findViewById(R.id.rg_claim_profile);
                Button bt_dialog_ok = (Button) claimDialog.findViewById(R.id.bt_dialog_ok);

                tv_dialog_title.setText("Report or Claim " +  DoctorDetail.doctorRowName.getText().toString());
                final String claimeeId = DoctorDetail.doctorRowId.getText().toString();

                //setting on click for ok button
                bt_dialog_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(checkedRadioButtonId);
                        int idx = radioGroup.indexOfChild(radioButton);
                        Log.e("TAG", "here the id for claimmer  rrrrr: " + idx);
                        if(idx == -1){
                            Toast.makeText(getActivity(), "Please Select Radio first", Toast.LENGTH_SHORT).show();
                        }
                        else if (idx == 0){

                            //callling claim functionalities

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                            if (sharedPreferences!=null){
                                String userTable = sharedPreferences.getString("usertable", null);
                                if (userTable!=null && userTable.equals("doctors")) {

                                    final String doctorName = sharedPreferences.getString("userfullname", null);
                                    final String userId = sharedPreferences.getString("myid", null);
                                    Log.e("TAG", "here the id for claimmer: " + userId);
                                    Log.e("TAG", "here the id for claimmee: " + claimeeId);
                                    if (!userId.equals(claimeeId)){

                                        //calling claim api
                                        cliamProfileSendingDataService(userId, claimeeId, doctorName, DoctorDetail.doctorRowName.getText().toString());
                                        claimDialog.dismiss();

                                    }else {
                                        Toast.makeText(getActivity(), "Can not Claim Your Own Profile", Toast.LENGTH_SHORT).show();
                                        claimDialog.dismiss();
                                    }


                                }else if (userTable!=null && !userTable.equals("doctors")){

                                    Toast.makeText(getActivity(), "Only doctor Can Claim Doctor Profiles", Toast.LENGTH_SHORT).show();
                                    claimDialog.dismiss();
                                }
                                else {

                                    Intent intent = new Intent(getActivity() , SignIn.class);
                                    //intent.putExtra("item_position" , 0);
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("claimee_id", claimeeId);
                                    intent.putExtra("claimee_name", DoctorDetail.doctorRowName.getText().toString());
                                    intent.putExtra("from", "claim");
                                    startActivity(intent);
                                    claimDialog.dismiss();

                                }

                            }
                        }

                        else if (idx == 1){
                            //calling report funcatilaities

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                            if (sharedPreferences!=null){
                                final String reporterUserID = sharedPreferences.getString("userid", null);
                                if (reporterUserID!=null) {

                                    final String reporterName = sharedPreferences.getString("userfullname", null);
                                    claimDialog.dismiss();

                                    final Dialog reportDialog = new Dialog(getActivity());
                                    reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    reportDialog.setContentView(R.layout.custome_dialog_doctor_report);
                                    TextView tv_dialog_title = (TextView) reportDialog.findViewById(R.id.tv_dialog_title);
                                    final EditText et_dialog_report = (EditText) reportDialog.findViewById(R.id.et_dialog_report);
                                    Button bt_dialog_submit_report = (Button) reportDialog.findViewById(R.id.bt_dialog_submit_report);


                                    //submit button click listener
                                    bt_dialog_submit_report.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            String reportText = et_dialog_report.getText().toString();
                                            if (reportText.length()==0){
                                                et_dialog_report.setError("Should not be Empty");

                                            }else
                                            {
                                                reportDialog.dismiss();
                                                Log.e("TAG","The report Text is: " + reportText);
                                                //calling report api
                                                reportProfileSendingDataService(reporterUserID, DoctorDetail.doctorRowId.getText().toString(), reportText, reporterName,  DoctorDetail.doctorRowName.getText().toString());

                                            }

                                        }
                                    });


                                    reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                    reportDialog.show();


                                }
                                else {

                                    Toast.makeText(getActivity(), "Please Login or Register first", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity() , SignIn.class);
                                    //intent.putExtra("item_position" , 0);
                                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("claimee_id", claimeeId);
                                    intent.putExtra("claimee_name", DoctorDetail.doctorRowName.getText().toString());
                                    intent.putExtra("from", "report");
                                    startActivity(intent);
                                    claimDialog.dismiss();
                                }
                            }
                        }



                    }
                });

                claimDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                claimDialog.show();



            }
        });

    }

    //claim profile API service
    private void cliamProfileSendingDataService(final String myid, final String climId, final String doctorName, final String calimeeName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog = new CustomProgressDialog(getActivity(), 1);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.CLAI_PROFILE_MURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Calim Profile URL: " + response.toString());
                dialog.hide();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("error_message");
                        if (message.equals("Claimed Successfully")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Profiel Claim in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + doctorName + " Your Claim to "+ calimeeName + " Submitted Succesfully, We Will Notify You Soon");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            alert.show();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server Connection Failed", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", myid);
                params.put("claimed_id", climId);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    //claim report user api API service
    private void reportProfileSendingDataService(final String reporterID, final String toReportID, final String reportText, final String reporterName, final String toReportName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog = new CustomProgressDialog(getActivity(), 1);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.REPORT_DOCTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Calim Profile URL: " + response.toString());
                dialog.hide();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("error_message");
                        if (message.equals("Reported Successfully")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Your Report in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + reporterName + " Your Report Against "+ toReportName + " Submitted Succesfully, We Will Notify You Soon");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            alert.show();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server Connection Failed", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", toReportID);
                params.put("user_id", reporterID);
                params.put("report", reportText);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


}
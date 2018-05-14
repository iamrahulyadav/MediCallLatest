package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.HospitalDocListRecyclerView;
import themedicall.com.Adapter.HospitalsLandLineListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.GetterSetter.HospitalDoctorGetterSetter;
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

public class HospitalDetail extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
//    View customActionBarView ;
//    public static ImageView hospitalDetailActionBarImg ;
//    public static TextView hospitalDetailActionBarName , hospitalDetailActionBarAddress ;
    String hosName , hosAddress , hosNoOfDoc , hosKm;
    Bitmap hosImgBitmap ;
    ArrayList<String> landLineArray ;
    ArrayAdapter<String> cityAdapter;
    public static ImageView hospitalProfileImg , doctorCallBtn;
    public static TextView hospitalId , hospitalProfileName , hospitalProfileAddress , hospitalProfileNoOfDoc , hospitalKm , hospitalRowNoOfViews , hospitalRowProfileLink;
    LinearLayout hospitalShareLayout ;
    private int REQUEST_PHONE_CALL = 23;
    public static LinearLayout hospitalDetailLayout;
    HospitalsLandLineListRecycleView hospitalsLandLineListRecycleView;
    CustomProgressDialog dialog;


    List<HospitalDoctorGetterSetter> hospitalDetailDocList;
    List<HospitalDoctorGetterSetter> nextDocList;

    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject innerQualificationObject;
    JSONObject innerSpeciality;
    JSONArray basicInfoArray;

    private static final String TAG = "DocHosList";
    public String  doctor_id , doctor_full_name  , doctor_experience , doctor_mobile , doctor_min_fee , docor_max_fee , doctor_img , doctor_status , doctor_qualification , doctor_speciality ,  hospital_id , hospital_name , hospital_image , doctor_offer_any_discount , doctor_verified_status , doctor_views , doctor_url;
    HospitalDoctorGetterSetter hosDoctorGetterSetter ;
    ArrayList qualificationList;
    ArrayList specialityList;
    HospitalDocListRecyclerView hosDoctorListRecycleView ;
    String hospitalIdForService;
    Double rating ;

    private int totalDoctorInHospital;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    boolean hospitalDocListServiceHasRun = false;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar_for_hospitals_detail);
//        customActionBarView =getSupportActionBar().getCustomView();
//        getSupportActionBar().setElevation(0);
//        getSupportActionBar().setTitle("");

        initiate();
        getHospitalDocListOnPageSelected();

        //getHosInfo();
        //setHosInfo();


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


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            hospitalIdForService = (String) bd.get("hospital_id");
            Log.e("tag", "get id using intent: " + hospital_id);

        }
        networkDialog = new Dialog(HospitalDetail.this);


        dialog=new CustomProgressDialog(HospitalDetail.this, 1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        hospitalDetailActionBarImg = (ImageView) customActionBarView.findViewById(R.id.hospitalDetailActionBarImg) ;
//        hospitalDetailActionBarName = (TextView) customActionBarView.findViewById(R.id.hospitalDetailActionBarName) ;
//        hospitalDetailActionBarAddress = (TextView) customActionBarView.findViewById(R.id.hospitalDetailActionBarAddress) ;

        doctorCallBtn = (ImageView) findViewById(R.id.doctorCallBtn);
        hospitalProfileImg = (ImageView) findViewById(R.id.hospitalRowProfileImg);
        hospitalId = (TextView) findViewById(R.id.hospitalId);
        hospitalProfileName = (TextView) findViewById(R.id.hospitalRowName);
        hospitalProfileAddress = (TextView) findViewById(R.id.hospitalRowAddress);
        hospitalProfileNoOfDoc = (TextView) findViewById(R.id.hospitalRowNoOfDoc);
        hospitalRowNoOfViews = (TextView) findViewById(R.id.hospitalRowNoOfViews);
        hospitalRowProfileLink = (TextView) findViewById(R.id.hospitalRowProfileLink);
        hospitalKm = (TextView) findViewById(R.id.hospitalKm);
        hospitalDetailDocList = new ArrayList<>();
        hospitalShareLayout = (LinearLayout) findViewById(R.id.hospitalShareLayout);
        hospitalDetailLayout = (LinearLayout) findViewById(R.id.hospitalDetailLayout);
        hospitalDetailLayout.setVisibility(View.INVISIBLE);

        hospitalShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(HospitalDetail.this, "share link: "+HospitalsInformation.hospital_share_url, Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "View Hospital Profile click on this link \n" ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + HospitalsInformation.hospital_share_url);
                view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });


        doctorCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HospitalsInformation.landLineList.size() == 0)
                {
                    Toast.makeText(HospitalDetail.this, "Contact detail not found", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LandLineListDialog(HospitalsInformation.landLineList);
                }
            }
        });

    }


    public void getHosInfo()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            hospital_id= (String) bd.get("hospital_id");
            Log.e("tag", "check id in getHosInfo id: "+hospital_id );
            hosImgBitmap = (Bitmap) intent.getParcelableExtra("hosImgBitmap");
            hosName=(String)bd.get("hosName");
            hosAddress=(String)bd.get("hosAddress");
            hosNoOfDoc=(String)bd.get("hosNoOfDoc");
            hosKm=(String)bd.get("hosKm");
            landLineArray =(ArrayList) bd.getParcelableArrayList("landLineArray");

        }
    }



    public void setHosInfo()
    {
        hospitalProfileImg.setImageBitmap(hosImgBitmap);
        hospitalProfileName.setText(hosName);
        hospitalProfileAddress.setText(hosAddress);
        hospitalProfileNoOfDoc.setText(hosNoOfDoc);
        hospitalKm.setText(hosKm);

        doctorCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (landLineArray.size() == 0)
                {
                    Toast.makeText(HospitalDetail.this, "No Contact detail", Toast.LENGTH_SHORT).show();
                }
                else
                {
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HospitalsInformation(), "Hospital Info");
        adapter.addFragment(new HospitalDocList(), "Panel Doctors");
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


    public void LandLineListDialog(ArrayList landLine)
    {

        final Dialog dialog = new Dialog(HospitalDetail.this);
        dialog.setContentView(R.layout.custom_citylist_search);
        dialog.setTitle("Phone Numbers");
        ListView cityList = (ListView) dialog.findViewById(R.id.cityList);
        SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setVisibility(View.GONE);
        Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
        bt_dilaog_done.setVisibility(View.GONE);
        dialog.show();


        hospitalsLandLineListRecycleView = new HospitalsLandLineListRecycleView(HospitalDetail.this , landLine);
        cityList.setAdapter(hospitalsLandLineListRecycleView);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView) view.findViewById(R.id.landLineList);
                String phoneNumber = textView.getText().toString();
                //Toast.makeText(mContext, "phone number "+phoneNumber, Toast.LENGTH_SHORT).show();

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                if (ContextCompat.checkSelfPermission(HospitalDetail.this , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) HospitalDetail.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);

                    return;

                }

                startActivity(callIntent);

                dialog.dismiss();

            }
        });


    }


    public void getHospitalDocListOnPageSelected()
    {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(viewPager.getCurrentItem() == 1)
                {
                    if(hospitalDocListServiceHasRun)
                    {
                        Log.e("tag" , "hos doc list service all ready Run");
                    }
                    else
                    {
                        getHospitalDocList();
                        Log.e("tag" , "abc test ");
                    }

                    hosDocPaginationScrollListener();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void getHospitalDocList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocHosList";

        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loadingImage.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.HOSPITALS_DOCTOR_LIST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Hospital Doc Response: " + response.toString());
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
                        totalDoctorInHospital = Integer.parseInt(total);

                        float totalListing = totalDoctorInHospital/10 ;
                        Log.e("tag" , "total scroll able listing : "+totalListing);

                        basicInfoArray = basicInfoObject.getJSONArray("doctors");

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            hosDoctorGetterSetter = new HospitalDoctorGetterSetter();
                            qualificationList = new ArrayList();
                            specialityList = new ArrayList();
                            nextDocList = new ArrayList<HospitalDoctorGetterSetter>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");


                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");


                            hosDoctorGetterSetter.setDoctorRowId(doctor_id);
                            hosDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            hosDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            hosDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            hosDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            hosDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            hosDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            hosDoctorGetterSetter.setDoctorRowOfferDiscount(doctor_offer_any_discount);
                            hosDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            hosDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);
                            hosDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            hosDoctorGetterSetter.setDoctorRowDistance((String.valueOf(HospitalsInformation.roundAboutHosKm)));
                            hosDoctorGetterSetter.setDoctorRowNoOfThumbsUp("20");


                            JSONArray doctorSpecialityArray = basicInfoObject.getJSONArray("speciality");
                            for (int k = 0; k < doctorSpecialityArray.length(); k++) {
                                JSONObject qualificationObject = doctorSpecialityArray.getJSONObject(k);
                                innerSpeciality = qualificationObject.getJSONObject("speciality");
                                doctor_speciality = innerSpeciality.getString("speciality_designation");
                                specialityList.add(doctor_speciality);

                            }

                            Log.e("tag" , "hospital doc list speciality : "+specialityList.size());


                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }



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
                            hosDoctorGetterSetter.setDoctorRowRatingtext(rating);




                            hosDoctorGetterSetter.setDoctorRowSpeciality(specialityList);
                            hosDoctorGetterSetter.setDoctorRowQualification(qualificationList);
                            hospitalDetailDocList.add(hosDoctorGetterSetter);
                        }


                        if(hospitalDetailDocList.size() == 0)
                        {
                            HospitalDocList.emptyViewHosDocList.setVisibility(View.VISIBLE);
                            HospitalDocList.recyclerView_hospitals_detail_Doc.setVisibility(View.GONE);
                        }
                        else
                        {
                            hosDoctorListRecycleView = new HospitalDocListRecyclerView(HospitalDetail.this , hospitalDetailDocList);
                            HospitalDocList.recyclerView_hospitals_detail_Doc.setAdapter(hosDoctorListRecycleView);
                            hosDoctorListRecycleView.notifyDataSetChanged();
                        }


                        hospitalDocListServiceHasRun = true ;

                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(HospitalDetail.this, "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                Toast.makeText(HospitalDetail.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Log.e("tag" , " offset in : " +offset);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("hospital_id", hospitalIdForService);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(HospitalDetail.this) .addToRequestQueue(strReq, cancel_req_tag);
    }


    public void getNextHospitalDocList(final String offset)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "DocHosList";

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.HOSPITALS_DOCTOR_LIST_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Hospital Doc Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDoctorInHospital = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");



                        if (nextDocList.size()>1){
                            nextDocList.clear();
                        }


                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            hosDoctorGetterSetter = new HospitalDoctorGetterSetter();
                            qualificationList = new ArrayList();
                            specialityList = new ArrayList();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");


                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");


                            hosDoctorGetterSetter.setDoctorRowId(doctor_id);
                            hosDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            hosDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            hosDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            hosDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            hosDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            hosDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            hosDoctorGetterSetter.setDoctorRowOfferDiscount(doctor_offer_any_discount);
                            hosDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            hosDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);
                            hosDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            hosDoctorGetterSetter.setDoctorRowDistance((String.valueOf(HospitalsInformation.roundAboutHosKm)));
                            hosDoctorGetterSetter.setDoctorRowNoOfThumbsUp("20");


                            JSONArray doctorSpecialityArray = basicInfoObject.getJSONArray("speciality");
                            for (int k = 0; k < doctorSpecialityArray.length(); k++) {
                                JSONObject qualificationObject = doctorSpecialityArray.getJSONObject(k);
                                innerSpeciality = qualificationObject.getJSONObject("speciality");
                                doctor_speciality = innerSpeciality.getString("speciality_designation");
                                specialityList.add(doctor_speciality);

                            }

                            Log.e("tag" , "hospital doc list speciality : "+specialityList.size());


                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }



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
                            hosDoctorGetterSetter.setDoctorRowRatingtext(rating);

                            hosDoctorGetterSetter.setDoctorRowSpeciality(specialityList);
                            hosDoctorGetterSetter.setDoctorRowQualification(qualificationList);
                            // hospitalDetailDocList.add(hosDoctorGetterSetter);

                            nextDocList.add(hosDoctorGetterSetter);
                        }

                        List<HospitalDoctorGetterSetter> loadNextData = HospitalDoctorGetterSetter.creatData(nextDocList.size(), nextDocList);
                        hosDoctorListRecycleView.addAll(loadNextData);
                        hosDoctorListRecycleView.notifyDataSetChanged();


                        Log.e("tag" , "nextDocList list size" +nextDocList.size());


                        // hosDoctorListRecycleView.notifyDataSetChanged();


                        //callBack.onSuccess(nextDocList);

                        if (HospitalDocList.bar.getVisibility() == View.VISIBLE){
                            HospitalDocList.bar.setVisibility(View.GONE);
                        }


                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(HospitalDetail.this, "error "+ errorMsg, Toast.LENGTH_LONG).show();

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
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Log.e("tag" , " offset in next : " +offset);


                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("hospital_id", hospitalIdForService);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(HospitalDetail.this) .addToRequestQueue(strReq, cancel_req_tag);
    }


    // onPagination scroll listener
    public void hosDocPaginationScrollListener(){

        HospitalDocList.recyclerView_hospitals_detail_Doc.addOnScrollListener(new PaginationScrollListener(HospitalDocList.linearLayoutManager, totalDoctorInHospital) {


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

                Log.e("TAG", "Total Data count: " +totalDoctorInHospital );
                Log.e("TAG", "Total Data count layout: " +HospitalDocList.linearLayoutManager.getItemCount() );
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

                return (totalDoctorInHospital-1 == hosDoctorListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalDoctorInHospital-1 == hosDoctorListRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    private void loadNextPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        getNextHospitalDocList(offset);

        if (HospitalDocList.bar.getVisibility()== View.GONE){
            HospitalDocList.bar.setVisibility(View.VISIBLE);
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

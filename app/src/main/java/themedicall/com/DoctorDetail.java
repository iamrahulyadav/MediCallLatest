package themedicall.com;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.DocGalleryImageRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.DocImageGalleryGetterSetter;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.CustomTextView;
import themedicall.com.Globel.Glob;
import themedicall.com.Interfaces.LandLineInferface;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorDetail extends AppCompatActivity implements LandLineInferface {

    public static boolean doctorBoiServiceHasRun  = false;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    public static ImageView doctorRowProfileImg  , doctorRowDiscountedImg , verifiedDoc ;
    public static ImageView doctorRowPhoneImg ;
    public static TextView doctorRowId , doctorRowName , doctorRowSpeciality , doctorRowDegree , doctorRowFees , doctorRowDistance
            , doctorRowClaimProfile , doctorRowNoOfThumbsUp , doctorRowExperience , doctorRowNoOfViews;
    public static RatingBar doctorRowRating ;
    LinearLayout shareDoctorLayout ;
    CustomProgressDialog dialog;
    public static LinearLayout doctorDetailLayout ;
    static ImageView im_pencila_icon;
    String docId;
    String docExperience;
    String docName;
    String docSpeciality;
    String docDegree;
    String docFees;
    String doctorThumbsUp;
    String doctorDistance;
    String discountDr;
    Bitmap profileBitmap ;





    ///

    JSONObject basicInfoObject;
    private static final String TAG = "Doc Bio";
    String doctor_id ,doctor_full_name , doctor_experience , doctor_min_fee , docor_max_fee , doctor_gender , doctor_img , doctor_cover_photo , doctor_video ,
            doctor_about_me , doctor_publications ,doctor_achievements , doctor_extra_curricular_activities , experience_status_title , speciality_designation ,
            service_title, registration_title , qualification_title , doctor_gallery_img , institutions_doctor_title , expertise_title , hospital_affiliation , hospital_landline , hospital_services;
    ArrayList<DocImageGalleryGetterSetter> DocGalleryImgList;
    ArrayList<HospitalServiceGetterSetter> servicesList;
    ArrayList<HospitalServiceGetterSetter> servicesListMoreButton;
    ArrayList specialityDesignationList , registrationList , qualificationList , institutionsList , expertiseList;
    StringBuilder builderQualification , builderRegistration , builderInstitutions , builderExpertise ;
    String prefixQualification = "";
    String prefixRegistration = "";
    String prefixInstitutions = "";
    String prefixExpertise = "";
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();
        getDocInfo();
        //setDocInfo();
        getDocBioService();



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

    public void getDocBioService()
    {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(viewPager.getCurrentItem() == 1) {

                    if (doctorBoiServiceHasRun) {
                        Log.e("tag ", "Doc Bio All ready run");
                    }
                    else
                    {
                        getDoctorBio();

                    }

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getDocInfo()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            profileBitmap = (Bitmap) intent.getParcelableExtra("docProfile");
            docId= (String) bd.get("docId");
            docExperience=(String)bd.get("docExperience");
            docName=(String)bd.get("docName");
            docSpeciality=(String)bd.get("docSpeciality");
            docDegree=(String)bd.get("docDegree");
            docFees=(String)bd.get("docFees");
            doctorThumbsUp=(String)bd.get("doctorThumbsUp");
            doctorDistance=(String)bd.get("doctorDistance");
            discountDr=(String)bd.get("discountDr");
            Log.e("tag" , "discount dr yes or no : "+discountDr );
        }
    }

    public void setDocInfo()
    {
        doctorRowProfileImg.setImageBitmap(profileBitmap);
        doctorRowId.setText(docId);
        doctorRowName.setText(docName);
        doctorRowSpeciality.setText(docSpeciality);
        doctorRowDegree.setText(docDegree);
        doctorRowFees.setText(docFees);
        doctorRowNoOfThumbsUp.setText(doctorThumbsUp);
        doctorRowDistance.setText(doctorDistance);

        if (docExperience.contains("00")) {
            doctorRowExperience.setVisibility(View.GONE);
        } else {
            doctorRowExperience.setText(docExperience);
        }


        if(discountDr.equals("Yes"))
        {
            doctorRowDiscountedImg.setVisibility(View.VISIBLE);
        } else {
            doctorRowDiscountedImg.setVisibility(View.GONE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DocPrecticeDetailFragment(), "Practice");
        adapter.addFragment(new DocBioGraphyFragment(), "Bio");
        adapter.addFragment(new DocReviewsFragment(), "Reviews");
        adapter.addFragment(new DocPakcageFragment(), "Package");
        adapter.addFragment(new DocAnswers(), "Answers");
        adapter.addFragment(new DocBlog(), "Blog");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void landline(ArrayList<HospitalLandLineListGetterSetter> landLineList) {
        Log.e("TAG", "the array list size is: " + landLineList.size());

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

        networkDialog = new Dialog(DoctorDetail.this);
        dialog=new CustomProgressDialog(DoctorDetail.this , 1);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(6);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        doctorRowRating = (RatingBar) findViewById(R.id.doctorRowRating) ;
        doctorDetailLayout = (LinearLayout) findViewById(R.id.doctorDetailLayout);
        doctorDetailLayout.setVisibility(View.INVISIBLE);


        doctorRowProfileImg = (ImageView) findViewById(R.id.doctorRowProfileImg);
        doctorRowPhoneImg = (ImageView) findViewById(R.id.doctorRowPhone);
        doctorRowDiscountedImg = (ImageView) findViewById(R.id.doctorRowDiscountedImg);
        verifiedDoc = (ImageView) findViewById(R.id.verifiedDoc);
        doctorRowId = (TextView) findViewById(R.id.doctorRowId);
        doctorRowExperience = (TextView) findViewById(R.id.doctorRowExperience);
        doctorRowName = (TextView) findViewById(R.id.doctorRowName);
        doctorRowSpeciality = (TextView) findViewById(R.id.doctorRowSpeciality);
        doctorRowDegree = (TextView) findViewById(R.id.doctorRowDegree);
        doctorRowFees = (TextView) findViewById(R.id.doctorRowFees);
        doctorRowDistance = (TextView) findViewById(R.id.doctorRowDistance);
        doctorRowClaimProfile = (TextView) findViewById(R.id.doctorRowClaimProfile);
        doctorRowNoOfThumbsUp = (TextView) findViewById(R.id.doctorRowNoOfThumbUp);
        doctorRowNoOfViews = (TextView) findViewById(R.id.doctorRowNoOfViews);
        shareDoctorLayout = (LinearLayout) findViewById(R.id.shareDoctorLayout);
        im_pencila_icon = (ImageView) findViewById(R.id.im_pencila_icon);
        shareDoctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id = doctorRowId.getText().toString();
                Log.e("tag" , "doc id is "+id);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "View Doctor Profile click on this link \n" ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"MediCall");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText + DocPrecticeDetailFragment.doctor_url);
                view.getContext().startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

            }
        });


        specialityDesignationList = new ArrayList();
        registrationList = new ArrayList();
        qualificationList = new ArrayList();
        institutionsList = new ArrayList();
        expertiseList = new ArrayList();
        servicesList = new ArrayList<HospitalServiceGetterSetter>();
        servicesListMoreButton = new ArrayList<HospitalServiceGetterSetter>();
        DocGalleryImgList = new ArrayList<DocImageGalleryGetterSetter>();
        builderQualification = new StringBuilder();
        builderRegistration = new StringBuilder();
        builderExpertise = new StringBuilder();
        builderInstitutions = new StringBuilder();
    }





    public void getDoctorBio()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Doc Bio";

        dialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.DOCTOR_BIO_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Doc Bio Response: " + response.toString());

                dialog.hide();

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        basicInfoObject = new JSONObject(response);

                        JSONObject data = basicInfoObject.getJSONObject("doctors");

                        doctor_id = data.getString("doctor_id");
                        doctor_full_name = data.getString("doctor_full_name");
                        doctor_experience = data.getString("doctor_experience");
                        doctor_min_fee = data.getString("doctor_min_fee");
                        docor_max_fee = data.getString("docor_max_fee");
                        doctor_gender = data.getString("doctor_gender");
                        doctor_img = data.getString("doctor_img");
                        doctor_cover_photo = data.getString("doctor_cover_photo");
                        doctor_video = data.getString("doctor_video");
                        doctor_about_me = data.getString("doctor_about_me");
                        doctor_publications = data.getString("doctor_publications");
                        doctor_achievements = data.getString("doctor_achievements");
                        doctor_extra_curricular_activities = data.getString("doctor_extra_curricular_activities");

                        JSONObject experienceObject = data.getJSONObject("experience_status");
                        if(experienceObject == null)
                        {
                            Log.d("tag" , "area is null");
                        }
                        else
                        {
                            experience_status_title  = experienceObject.getString("experience_status_title");
                        }


                        JSONArray galleryArray = data.getJSONArray("gallery");
                        for (int k = 0; k < galleryArray.length(); k++) {
                            JSONObject galleryObject = galleryArray.getJSONObject(k);
                            doctor_gallery_img = galleryObject.getString("doctor_gallery_img");
                            DocGalleryImgList.add(new DocImageGalleryGetterSetter(doctor_gallery_img));
                        }



                        JSONArray specialityArray = data.getJSONArray("speciality");
                        for (int k = 0; k < specialityArray.length(); k++) {
                            JSONObject specialityObject = specialityArray.getJSONObject(k);
                            JSONObject innerSpecialityObject = specialityObject.getJSONObject("speciality");
                            speciality_designation = innerSpecialityObject.getString("speciality_designation");
                            specialityDesignationList.add(speciality_designation);

                        }

//                        JSONArray servicesArray = data.getJSONArray("services");
//                        for (int k = 0; k < servicesArray.length(); k++) {
//                            JSONObject servicesObject = servicesArray.getJSONObject(k);
//                            JSONObject innerServicesObject = servicesObject.getJSONObject("services");
//                            service_title = innerServicesObject.getString("service_title");
//
//                            if(k <= 3)
//                            {
//                                servicesListMoreButton.add(new HospitalServiceGetterSetter(service_title));
//                            }
//
//                            servicesList.add(new HospitalServiceGetterSetter(service_title));
//                        }

                        JSONArray registrationArray = data.getJSONArray("registration");
                        for (int k = 0; k < registrationArray.length(); k++) {
                            JSONObject registrationObject = registrationArray.getJSONObject(k);
                            JSONObject innerregistrationObject = registrationObject.getJSONObject("registration");
                            registration_title = innerregistrationObject.getString("registration_title");
                            registrationList.add(registration_title);
                        }

                        JSONArray qualificationArray = data.getJSONArray("qualifications");
                        for (int k = 0; k < qualificationArray.length(); k++) {
                            JSONObject qualificationObject = qualificationArray.getJSONObject(k);
                            JSONObject innerqualificationObject = qualificationObject.getJSONObject("qualifications");
                            qualification_title = innerqualificationObject.getString("qualification_title");
                            qualificationList.add(qualification_title);
                        }

                        JSONArray expertiseArray = data.getJSONArray("expertise");
                        for (int k = 0; k < expertiseArray.length(); k++) {
                            JSONObject expertiseObject = expertiseArray.getJSONObject(k);
                            JSONObject innerExpertiseObjectObject = expertiseObject.getJSONObject("expertise");
                            expertise_title = innerExpertiseObjectObject.getString("service_title");
                            expertiseList.add(expertise_title);
                        }

                        JSONArray institutionsArray = data.getJSONArray("institutions");
                        for (int k = 0; k < institutionsArray.length(); k++) {
                            JSONObject institutionsObject = institutionsArray.getJSONObject(k);
                            JSONObject innerInstitutionsObject = institutionsObject.getJSONObject("institutions");
                            institutions_doctor_title = innerInstitutionsObject.getString("institutions_doctor_title");
                            institutionsList.add(institutions_doctor_title);
                        }

                        Log.e("tag" , "list institutionsList "+institutionsList.size());
                        Log.e("tag" , "list expertiseList "+expertiseList.size());

                        Log.d("tag" , "doctor_about_me "+doctor_about_me);
                        Log.d("tag" , "experience_status_title "+experience_status_title);
                        Log.d("tag " , "speciality Designation List" + specialityDesignationList.toString());
                        Log.d("tag " , "services List " + servicesList.toString());
                        Log.d("tag " , "registration List" + registrationList.toString());
                        Log.d("tag " , "qualification List" + qualificationList.toString());



                        if(doctor_about_me.equals("null"))
                        {
                            DocBioGraphyFragment.doctorAboutLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioAbout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioAboutLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocBioGraphyFragment.docBioAbout.setText(doctor_about_me);
                            CustomTextView customTextView = new CustomTextView();
                            customTextView.makeTextViewResizable(DocBioGraphyFragment.docBioAbout , 2, "View More", true);
                        }


                        if(doctor_achievements.equals("null"))
                        {
                            DocBioGraphyFragment.doctorAchievementsLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioAchievement.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioAchievementLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocBioGraphyFragment.docBioAchievement.setText(doctor_achievements);
                        }

                        if(doctor_publications.equals("null"))
                        {
                            DocBioGraphyFragment.doctorPublicationLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioPublications.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioPublicationsLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocBioGraphyFragment.docBioPublications.setText(doctor_publications);
                        }


                        if(doctor_extra_curricular_activities.equals("null"))
                        {
                            DocBioGraphyFragment.doctorOtherExtraLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioExtracurricular.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioExtracurricularLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocBioGraphyFragment.docBioExtracurricular.setText(doctor_extra_curricular_activities);
                        }




                        if(DocGalleryImgList.size() == 0)
                        {
                            DocBioGraphyFragment.doctorGalleryLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.recycler_view_doc_gallery_img.setVisibility(View.GONE);
                            DocBioGraphyFragment.recycler_view_doc_gallery_img_line.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocGalleryImageRecycleView docGalleryImageRecycleView = new DocGalleryImageRecycleView(DoctorDetail.this , DocGalleryImgList);
                            DocBioGraphyFragment.recycler_view_doc_gallery_img.setAdapter(docGalleryImageRecycleView);
                            docGalleryImageRecycleView.notifyDataSetChanged();
                        }

                        if(qualificationList.size() == 0)
                        {
                            DocBioGraphyFragment.doctorQualificationLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioQualification.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioQualificationLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : qualificationList) {

                                builderQualification.append(prefixQualification);
                                prefixQualification = "\n";
                                builderQualification.append("\u2022 " +str);
                            }

                            DocBioGraphyFragment.docBioQualification.setText(builderQualification.toString());
                        }


                        if(registrationList.size() == 0)
                        {
                            DocBioGraphyFragment.doctorRegistrationLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioRegistration.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioRegistrationLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : registrationList) {
                                builderRegistration.append(prefixRegistration);
                                prefixRegistration = "\n";
                                builderRegistration.append("\u2022 " + str);
                            }
                            DocBioGraphyFragment.docBioRegistration.setText(builderRegistration.toString());
                        }


                        if(expertiseList.size() == 0)
                        {
                            DocBioGraphyFragment.doctorExpertiseLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioExpertise.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioExpertiseLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : expertiseList) {
                                builderExpertise.append(prefixExpertise);
                                prefixExpertise = "\n";
                                builderExpertise.append("\u2022 " + str);
                            }
                            DocBioGraphyFragment.docBioExpertise.setText(builderExpertise.toString());
                        }


                        if(institutionsList.size() == 0)
                        {
                            DocBioGraphyFragment.doctorInstitutionLayout.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioInstitution.setVisibility(View.GONE);
                            DocBioGraphyFragment.docBioInstitutionLine.setVisibility(View.GONE);
                        }
                        else
                        {

                            for (Object str : institutionsList) {
                                builderInstitutions.append(prefixInstitutions);
                                prefixInstitutions = "\n";
                                builderInstitutions.append("\u2022 " + str);
                            }
                            DocBioGraphyFragment.docBioInstitution.setText(builderInstitutions.toString());
                        }


//                        if(servicesListMoreButton.size() == 0)
//                        {
//                            doctorServicesLayout.setVisibility(View.GONE);
//                            doc_Services_recycler_view.setVisibility(View.GONE);
//                            recyclerViewMore.setVisibility(View.GONE);
//
//                        }
//                        else
//                        {
//                            HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesListMoreButton);
//                            doc_Services_recycler_view.setAdapter(hospitalsServiceListRecycleView);
//                            hospitalsServiceListRecycleView.notifyDataSetChanged();
//                            if(servicesList.size() <= 3)
//                            {
//                                recyclerViewMore.setVisibility(View.VISIBLE);
//                            }
//                        }


                        DocBioGraphyFragment.bioDetailPage.setVisibility(View.VISIBLE);
                        doctorBoiServiceHasRun = true ;

                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(DoctorDetail.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Hospital info Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", docId);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(DoctorDetail.this).addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    public void onBackPressed() {
        doctorBoiServiceHasRun = false ;
        super.onBackPressed();
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

package themedicall.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import themedicall.com.Adapter.DocGalleryImageRecycleView;
import themedicall.com.GetterSetter.DocImageGalleryGetterSetter;
import themedicall.com.GetterSetter.HospitalServiceGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.CustomTextView;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DocBioGraphyFragment extends Fragment{

    public static RecyclerView recycler_view_doc_gallery_img;
    public static RecyclerView doc_Services_recycler_view;
    ArrayList<DocImageGalleryGetterSetter> DocGalleryImgList;
    ArrayList<HospitalServiceGetterSetter> servicesList;
    ArrayList<HospitalServiceGetterSetter> servicesListMoreButton;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    View customActionBarView ;
    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    private static final String TAG = "Doc Bio";
    String  city;
    String content = "<font color='#000000'>\u2022</font>";
    public static TextView  docBioAbout , docBioQualification , docBioRegistration , docBioInstitution , docBioExpertise , docBioAchievement , docBioWelfare , docBioPublications ,  docBioExtracurricular ;
    public static TextView  docBioAboutHeading , recycler_view_doc_gallery_img_heading , docBioVideoHeading , docBioQualificationHeading , docBioRegistrationHeading , docBioInstitutionHeading,
            docBioExpertiseHeading , docBioAchievementHeading , docBioWelfareHeading , docBioPublicationsHeading , docBioExtracurricularHeading , doc_Services_recycler_view_heading;
    public static ImageView docBioVideo , docBioAboutLine , recycler_view_doc_gallery_img_line , docBioVideoLine , docBioQualificationLine , docBioRegistrationLine , docBioInstitutionLine ,
            docBioExpertiseLine  , docBioAchievementLine , docBioWelfareLine , docBioPublicationsLine , docBioExtracurricularLine;

    String doctor_id ,doctor_full_name , doctor_experience , doctor_min_fee , docor_max_fee , doctor_gender , doctor_img , doctor_cover_photo , doctor_video ,
            doctor_about_me , doctor_publications ,doctor_achievements , doctor_extra_curricular_activities , experience_status_title , speciality_designation , service_title, registration_title , qualification_title , doctor_gallery_img , institutions_doctor_title , expertise_title , hospital_affiliation , hospital_landline , hospital_services;

    public static LinearLayout doctorAboutLayout , doctorGalleryLayout , doctorQualificationLayout , doctorRegistrationLayout , doctorInstitutionLayout , doctorExpertiseLayout , doctorAchievementsLayout , doctorPublicationLayout , doctorOtherExtraLayout , doctorServicesLayout , bioDetailPage ;

    ArrayList specialityDesignationList , registrationList , qualificationList , institutionsList , expertiseList;
    StringBuilder builderQualification , builderRegistration , builderInstitutions , builderExpertise ;
    String prefixQualification = "";
    String prefixRegistration = "";
    String prefixInstitutions = "";
    String prefixExpertise = "";
    TextView recyclerViewMore , recyclerViewLess;
    String docId ;
    CustomProgressDialog dialog;


    public DocBioGraphyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_doc_bio_graphy, container, false);

        initiate(view);
       // getDoctorBio();
//        viewMoreServices();
//        viewLessServices();
        return view;
    }



    public void initiate(View view)
    {


        Intent intent = getActivity().getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            docId = (String) bd.get("docId");
        }
        dialog=new CustomProgressDialog(getActivity(), 1);

        recyclerViewMore = (TextView) view.findViewById(R.id.recyclerViewMore);
        recyclerViewLess = (TextView) view.findViewById(R.id.recyclerViewLess);

        doctorAboutLayout = (LinearLayout) view.findViewById(R.id.doctorAboutLayout);
        doctorGalleryLayout = (LinearLayout) view.findViewById(R.id.doctorGalleryLayout);
        doctorQualificationLayout = (LinearLayout) view.findViewById(R.id.doctorQualificationLayout);
        doctorRegistrationLayout = (LinearLayout) view.findViewById(R.id.doctorRegistrationLayout);
        doctorInstitutionLayout = (LinearLayout) view.findViewById(R.id.doctorInstitutionLayout);
        doctorExpertiseLayout = (LinearLayout) view.findViewById(R.id.doctorExpertiseLayout);
        doctorAchievementsLayout = (LinearLayout) view.findViewById(R.id.doctorAchievementsLayout);
        doctorPublicationLayout = (LinearLayout) view.findViewById(R.id.doctorPublicationLayout);
        doctorOtherExtraLayout = (LinearLayout) view.findViewById(R.id.doctorOtherExtraLayout);
        doctorServicesLayout = (LinearLayout) view.findViewById(R.id.doctorServicesLayout);
        bioDetailPage = (LinearLayout) view.findViewById(R.id.bioDetailPage);
        bioDetailPage.setVisibility(View.GONE);


        builderQualification = new StringBuilder();
        builderRegistration = new StringBuilder();
        builderExpertise = new StringBuilder();
        builderInstitutions = new StringBuilder();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        docBioAbout = (TextView) view.findViewById(R.id.docBioAbout);
        docBioQualification = (TextView) view.findViewById(R.id.docBioQualification);
        docBioRegistration = (TextView) view.findViewById(R.id.docBioRegistration);
        docBioInstitution = (TextView) view.findViewById(R.id.docBioInstitution);
        docBioExpertise = (TextView) view.findViewById(R.id.docBioExpertise);
        docBioAchievement = (TextView) view.findViewById(R.id.docBioAchievement);
        docBioWelfare = (TextView) view.findViewById(R.id.docBioWelfare);
        docBioPublications = (TextView) view.findViewById(R.id.docBioPublications);
        docBioExtracurricular = (TextView) view.findViewById(R.id.docBioExtracurricular);


        //
        docBioAboutHeading = (TextView) view.findViewById(R.id.docBioAboutHeading);
        recycler_view_doc_gallery_img_heading = (TextView) view.findViewById(R.id.recycler_view_doc_gallery_img_heading);
        docBioVideoHeading = (TextView) view.findViewById(R.id.docBioVideoHeading);
        docBioInstitution = (TextView) view.findViewById(R.id.docBioInstitution);
        docBioQualificationHeading = (TextView) view.findViewById(R.id.docBioQualificationHeading);
        docBioRegistrationHeading = (TextView) view.findViewById(R.id.docBioRegistrationHeading);
        docBioInstitutionHeading = (TextView) view.findViewById(R.id.docBioInstitutionHeading);
        docBioExpertiseHeading = (TextView) view.findViewById(R.id.docBioExpertiseHeading);
        docBioAchievementHeading = (TextView) view.findViewById(R.id.docBioAchievementHeading);
        docBioWelfareHeading = (TextView) view.findViewById(R.id.docBioWelfareHeading);
        docBioPublicationsHeading = (TextView) view.findViewById(R.id.docBioPublicationsHeading);
        docBioExtracurricularHeading = (TextView) view.findViewById(R.id.docBioExtracurricularHeading);
        doc_Services_recycler_view_heading = (TextView) view.findViewById(R.id.doc_Services_recycler_view_heading);
        //

        //

        docBioVideo = (ImageView) view.findViewById(R.id.docBioVideo);
        docBioAboutLine = (ImageView) view.findViewById(R.id.docBioAboutLine);
        recycler_view_doc_gallery_img_line = (ImageView) view.findViewById(R.id.recycler_view_doc_gallery_img_line);
        docBioVideoLine = (ImageView) view.findViewById(R.id.docBioVideoLine);
        docBioQualificationLine = (ImageView) view.findViewById(R.id.docBioQualificationLine);
        docBioRegistrationLine = (ImageView) view.findViewById(R.id.docBioRegistrationLine);
        docBioInstitutionLine = (ImageView) view.findViewById(R.id.docBioInstitutionLine);
        docBioExpertiseLine = (ImageView) view.findViewById(R.id.docBioExpertiseLine);
        docBioAchievementLine = (ImageView) view.findViewById(R.id.docBioAchievementLine);
        docBioWelfareLine = (ImageView) view.findViewById(R.id.docBioWelfareLine);
        docBioPublicationsLine = (ImageView) view.findViewById(R.id.docBioPublicationsLine);
        docBioExtracurricularLine = (ImageView) view.findViewById(R.id.docBioExtracurricularLine);

        //

        recycler_view_doc_gallery_img = (RecyclerView) view.findViewById(R.id.recycler_view_doc_gallery_img);
        recycler_view_doc_gallery_img.setHasFixedSize(true);
        recycler_view_doc_gallery_img.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));

        doc_Services_recycler_view = (RecyclerView) view.findViewById(R.id.doc_Services_recycler_view);
        doc_Services_recycler_view.setHasFixedSize(true);
        doc_Services_recycler_view.setLayoutManager(new GridLayoutManager(getContext() , 2 ,GridLayoutManager.VERTICAL, false ));

        specialityDesignationList = new ArrayList();
        registrationList = new ArrayList();
        qualificationList = new ArrayList();
        institutionsList = new ArrayList();
        expertiseList = new ArrayList();
        servicesList = new ArrayList<HospitalServiceGetterSetter>();
        servicesListMoreButton = new ArrayList<HospitalServiceGetterSetter>();
        DocGalleryImgList = new ArrayList<DocImageGalleryGetterSetter>();


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
                            doctorAboutLayout.setVisibility(View.GONE);
                            docBioAbout.setVisibility(View.GONE);
                            docBioAboutLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            docBioAbout.setText(doctor_about_me);
                            CustomTextView customTextView = new CustomTextView();
                            customTextView.makeTextViewResizable(docBioAbout , 2, "View More", true);
                        }


                        if(doctor_achievements.equals("null"))
                        {
                            doctorAchievementsLayout.setVisibility(View.GONE);
                            docBioAchievement.setVisibility(View.GONE);
                            docBioAchievementLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            docBioAchievement.setText(doctor_achievements);
                        }

                        if(doctor_publications.equals("null"))
                        {
                            doctorPublicationLayout.setVisibility(View.GONE);
                            docBioPublications.setVisibility(View.GONE);
                            docBioPublicationsLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            docBioPublications.setText(doctor_publications);
                        }


                        if(doctor_extra_curricular_activities.equals("null"))
                        {
                            doctorOtherExtraLayout.setVisibility(View.GONE);
                            docBioExtracurricular.setVisibility(View.GONE);
                            docBioExtracurricularLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            docBioExtracurricular.setText(doctor_extra_curricular_activities);
                        }




                        if(DocGalleryImgList.size() == 0)
                        {
                            doctorGalleryLayout.setVisibility(View.GONE);
                            recycler_view_doc_gallery_img.setVisibility(View.GONE);
                            recycler_view_doc_gallery_img_line.setVisibility(View.GONE);
                        }
                        else
                        {
                            DocGalleryImageRecycleView docGalleryImageRecycleView = new DocGalleryImageRecycleView(getActivity() , DocGalleryImgList);
                            recycler_view_doc_gallery_img.setAdapter(docGalleryImageRecycleView);
                            docGalleryImageRecycleView.notifyDataSetChanged();
                        }

                        if(qualificationList.size() == 0)
                        {
                            doctorQualificationLayout.setVisibility(View.GONE);
                            docBioQualification.setVisibility(View.GONE);
                            docBioQualificationLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : qualificationList) {

                                builderQualification.append(prefixQualification);
                                prefixQualification = "\n";
                                builderQualification.append("\u2022 " +str);
                            }

                            docBioQualification.setText(builderQualification.toString());
                        }


                        if(registrationList.size() == 0)
                        {
                            doctorRegistrationLayout.setVisibility(View.GONE);
                            docBioRegistration.setVisibility(View.GONE);
                            docBioRegistrationLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : registrationList) {
                                builderRegistration.append(prefixRegistration);
                                prefixRegistration = "\n";
                                builderRegistration.append("\u2022 " + str);
                            }
                            docBioRegistration.setText(builderRegistration.toString());
                        }


                        if(expertiseList.size() == 0)
                        {
                            doctorExpertiseLayout.setVisibility(View.GONE);
                            docBioExpertise.setVisibility(View.GONE);
                            docBioExpertiseLine.setVisibility(View.GONE);
                        }
                        else
                        {
                            for (Object str : expertiseList) {
                                builderExpertise.append(prefixExpertise);
                                prefixExpertise = "\n";
                                builderExpertise.append("\u2022 " + str);
                            }
                            docBioExpertise.setText(builderExpertise.toString());
                        }


                        if(institutionsList.size() == 0)
                        {
                            doctorInstitutionLayout.setVisibility(View.GONE);
                            docBioInstitution.setVisibility(View.GONE);
                            docBioInstitutionLine.setVisibility(View.GONE);
                        }
                        else
                        {

                            for (Object str : institutionsList) {
                                builderInstitutions.append(prefixInstitutions);
                                prefixInstitutions = "\n";
                                builderInstitutions.append("\u2022 " + str);
                            }
                            docBioInstitution.setText(builderInstitutions.toString());
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


                       // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
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
        AppSingleton.getInstance(getContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


//    public void viewMoreServices()
//    {
//        recyclerViewMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesList);
//                doc_Services_recycler_view.setAdapter(hospitalsServiceListRecycleView);
//                hospitalsServiceListRecycleView.notifyDataSetChanged();
//                recyclerViewLess.setVisibility(View.VISIBLE);
//                recyclerViewMore.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    public void viewLessServices()
//    {
//        recyclerViewLess.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HospitalsServiceListRecycleView hospitalsServiceListRecycleView = new HospitalsServiceListRecycleView(servicesListMoreButton);
//                doc_Services_recycler_view.setAdapter(hospitalsServiceListRecycleView);
//                hospitalsServiceListRecycleView.notifyDataSetChanged();
//                recyclerViewMore.setVisibility(View.VISIBLE);
//                recyclerViewLess.setVisibility(View.GONE);
//            }
//        });
//    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}

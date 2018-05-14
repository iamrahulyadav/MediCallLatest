package themedicall.com.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.GetterSetter.CustomeTagsGeterSeter;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAllDoctorDetailService extends Service {

    JSONObject mJsonObject;
    JSONArray jsonArray;
    Button bt_restart;


    static public ArrayList<CitiesGetterSetter> specialistIdList, registrationIdList, instituationIdList;
    static public ArrayList<CustomeTagsGeterSeter> subSpecialistIdList, servicesIdList, qualificationIdList, expertieseIdList;
    static public ArrayList<ArrayList<HashMap<String, String>>> precticeDataList;
    static public String doctor_id, doctor_full_name, doctor_experience, doctor_dob, experience_status_id, city_id, doctor_img, doctor_cover_photo, doctor_video, doctor_about_me,
            doctor_is_blood_donor, blood_group_id, doctor_offer_any_discount, doctor_discount_for_check_up, doctor_discount_for_procedure, doctor_discount_for_other,
            doctor_detail_for_other_discount, doctor_want_to_join_medicall_welfare_panel, doctor_social_media_awareness, doctor_queries_answered, doctor_medical_camp, doctor_blood_camp,
            doctor_health_article, doctor_other_activity_for_medicall, doctor_other_activity_detail_for_medicall, doctor_available_for_video_consultation, doctor_available_for_home_care_service,
            doctor_publications, doctor_achievements, doctor_extra_curricular_activities, experience_status;

    public static ArrayList<CustomeTagsGeterSeter> allSubspecialitiesList, allServicesList, allQualificationsList;
    public static ArrayList<CitiesGetterSetter> allRegistrationList, allInstituationList, listGallaryImages;

    public GetAllDoctorDetailService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("TAG", "the Service starting...");
        init();
        getDoctorAllDetail();

        return super.onStartCommand(intent, flags, startId);

    }

    private void init() {

        specialistIdList = new ArrayList<>();
        subSpecialistIdList = new ArrayList<>();
        servicesIdList = new ArrayList<>();
        qualificationIdList = new ArrayList<>();
        instituationIdList = new ArrayList<>();
        expertieseIdList = new ArrayList<>();
        registrationIdList = new ArrayList<>();
        precticeDataList = new ArrayList<>();
        allSubspecialitiesList = new ArrayList<>();
        allServicesList = new ArrayList<>();
        allQualificationsList = new ArrayList<>();
        allRegistrationList = new ArrayList<>();
        allInstituationList = new ArrayList<>();
        listGallaryImages = new ArrayList<>();


    }

    public void getDoctorAllDetail() {

        // Tag used to cancel the request
        String cancel_req_tag = "register";


        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.GET_DOCTOR_FULL_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Doc Detail. Response: " + response.toString());

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        mJsonObject = new JSONObject(response);
                        //jsonArray = jsonObject.getJSONArray("doctor");
                        String total = mJsonObject.getString("doctor");
                        Log.e("TAg", "the json result: " + total);


                        JSONObject jsonObject = new JSONObject(total);
                        doctor_id = jsonObject.getString("doctor_id");
                        doctor_full_name = jsonObject.getString("doctor_full_name");
                        doctor_experience = jsonObject.getString("doctor_experience");
                        doctor_dob = jsonObject.getString("doctor_dob");
                        experience_status_id = jsonObject.getString("experience_status_id");
                        city_id = jsonObject.getString("city_id");
                        doctor_img = jsonObject.getString("doctor_img");

                        doctor_cover_photo = jsonObject.getString("doctor_cover_photo");
                        doctor_video = jsonObject.getString("doctor_video");
                        doctor_about_me = jsonObject.getString("doctor_about_me");
                        doctor_is_blood_donor = jsonObject.getString("doctor_is_blood_donor");
                        blood_group_id = jsonObject.getString("blood_group_id");
                        doctor_offer_any_discount = jsonObject.getString("doctor_offer_any_discount");
                        //doctor_offer_any_discount = "No";
                        doctor_discount_for_check_up = jsonObject.getString("doctor_discount_for_check_up");
                        doctor_discount_for_procedure = jsonObject.getString("doctor_discount_for_procedure");
                        doctor_discount_for_other = jsonObject.getString("doctor_discount_for_other");
                        doctor_detail_for_other_discount = jsonObject.getString("doctor_detail_for_other_discount");
                        doctor_want_to_join_medicall_welfare_panel = jsonObject.getString("doctor_want_to_join_medicall_welfare_panel");
                        doctor_social_media_awareness = jsonObject.getString("doctor_social_media_awareness");
                        doctor_queries_answered = jsonObject.getString("doctor_queries_answered");
                        doctor_medical_camp = jsonObject.getString("doctor_medical_camp");
                        doctor_blood_camp = jsonObject.getString("doctor_blood_camp");
                        doctor_health_article = jsonObject.getString("doctor_health_article");
                        doctor_other_activity_for_medicall = jsonObject.getString("doctor_other_activity_for_medicall");
                        Log.e("TAG", "the doctor other activities: " + doctor_other_activity_for_medicall);
                        doctor_other_activity_detail_for_medicall = jsonObject.getString("doctor_other_activity_detail_for_medicall");
                        Log.e("TAG", "the doctor other activities 12: " + doctor_other_activity_detail_for_medicall);
                        doctor_available_for_video_consultation = jsonObject.getString("doctor_available_for_video_consultation");
                        doctor_available_for_home_care_service = jsonObject.getString("doctor_available_for_home_care_service");
                        doctor_publications = jsonObject.getString("doctor_publications");
                        doctor_achievements = jsonObject.getString("doctor_achievements");
                        doctor_extra_curricular_activities = jsonObject.getString("doctor_extra_curricular_activities");
                        experience_status = jsonObject.getString("experience_status");

                        Log.e("TAG", "the text for other check: " + doctor_want_to_join_medicall_welfare_panel);

                        if (doctor_experience != null) {
                            //doctor_experience = doctor_experience.replaceAll("\\D+", "");
                            doctor_experience = getOnlyNumberFromString(doctor_experience);
                            Log.e("TAg", "the experience is: " + doctor_experience);
                            if (doctor_experience.equals("00")){
                                doctor_experience = "";
                            }

                        }
                        if (doctor_full_name.equals("null")) {
                            doctor_full_name = "";
                        }
                        if (doctor_experience.equals("null")) {
                            doctor_experience = "";
                        }
                        if (experience_status_id.equals("null")) {
                            experience_status_id = "";
                        }
                        if (doctor_cover_photo.equals("null")) {
                            doctor_cover_photo = "";
                        }
                        if (doctor_video.equals("null")) {
                            doctor_video = "";
                        }
                        if (doctor_about_me.equals("null")) {
                            doctor_about_me = "";
                        }
                        if (doctor_is_blood_donor.equals("null")) {
                            doctor_is_blood_donor = "";
                        }
                        if (doctor_offer_any_discount.equals("null")) {
                            doctor_offer_any_discount = "";
                        }
                        if (doctor_discount_for_check_up.equals("null")) {
                            doctor_discount_for_check_up = "";
                        }
                        if (doctor_discount_for_procedure.equals("null")) {
                            doctor_discount_for_procedure = "";
                        }
                        if (doctor_discount_for_other.equals("null")) {
                            doctor_discount_for_other = "";
                        }
                        if (doctor_detail_for_other_discount.equals("null")) {
                            doctor_detail_for_other_discount = "";
                        }
                        if (doctor_want_to_join_medicall_welfare_panel.equals("null")) {
                            doctor_want_to_join_medicall_welfare_panel = "";
                        }
                        if (doctor_want_to_join_medicall_welfare_panel.equals("No")) {
                            doctor_want_to_join_medicall_welfare_panel = "";
                        }

                        if (doctor_social_media_awareness.equals("null")) {
                            doctor_social_media_awareness = "";
                        }
                        if (doctor_queries_answered.equals("null")) {
                            doctor_queries_answered = "";
                        }
                        if (doctor_medical_camp.equals("null")) {
                            doctor_medical_camp = "";
                        }
                        if (doctor_blood_camp.equals("null")) {
                            doctor_blood_camp = "";
                        }
                        if (doctor_health_article.equals("null")) {
                            doctor_health_article = "";
                        }
                        if (doctor_other_activity_for_medicall.equals("null")) {
                            doctor_other_activity_for_medicall = "";
                        }
                        if (doctor_other_activity_detail_for_medicall.equals("null")) {
                            doctor_other_activity_detail_for_medicall = "";
                        }
                        if (doctor_available_for_video_consultation.equals("null")) {
                            doctor_available_for_video_consultation = "";
                        }
                        if (doctor_available_for_home_care_service.equals("null")) {
                            doctor_available_for_home_care_service = "";
                        }
                        if (doctor_publications.equals("null")) {
                            doctor_publications = "";
                        }
                        if (doctor_achievements.equals("null")) {
                            doctor_achievements = "";
                        }
                        if (doctor_extra_curricular_activities.equals("null")) {
                            doctor_extra_curricular_activities = "";
                        }
                        if (experience_status.equals("null")) {
                            experience_status = "";
                        }

                        jsonArray = jsonObject.getJSONArray("speciality");
                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject specialistyObjects = jsonArray.getJSONObject(i);
                            String speciality_id = specialistyObjects.getString("speciality_id");
                            String innerSpecialisty = specialistyObjects.getString("speciality");
                            JSONObject innerObject = new JSONObject(innerSpecialisty);
                            String spID = innerObject.getString("speciality_id");
                            String spTitl = innerObject.getString("speciality_title");

                            Log.e("TAg", "the value on index id is: " + spID);
                            Log.e("TAg", "the value on index title is: " + spTitl);

                            specialistIdList.add(new CitiesGetterSetter(spID, spTitl));
                        }

                        jsonArray = jsonObject.getJSONArray("sub_speciality");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject subspecialistyObjects = jsonArray.getJSONObject(i);
                            String sub_speciality_id = subspecialistyObjects.getString("sub_speciality_id");
                            String sub_speciality_title = subspecialistyObjects.getString("sub_speciality_title");
                            String specialityID = subspecialistyObjects.getString("speciality_id");

                            Log.e("Tag", "ye dekho usman: " + sub_speciality_id);
                            Log.e("Tag", "ye dekho usman: " + sub_speciality_title);
                            subSpecialistIdList.add(new CustomeTagsGeterSeter(sub_speciality_id, sub_speciality_title, specialityID));
                        }

                        jsonArray = jsonObject.getJSONArray("services");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject servicesObject = jsonArray.getJSONObject(i);
                            String services_id = servicesObject.getString("service_id");
                            String service_title = servicesObject.getString("service_title");
                            String specialityID = servicesObject.getString("speciality_id");
                            Log.e("Tag", "here is data from server from sub services id: " + services_id);
                            Log.e("Tag", "here is data from server from sub services title: " + service_title);



                            servicesIdList.add(new CustomeTagsGeterSeter(services_id, service_title, specialityID));
                        }


                        jsonArray = jsonObject.getJSONArray("expertise");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject expertisesObject = jsonArray.getJSONObject(i);
                            String expertise_id = expertisesObject.getString("service_id");
                            String service_title = expertisesObject.getString("service_title");
                            String specialityID = expertisesObject.getString("speciality_id");
                            Log.e("Tag", "here is data from server from sub experty id: " + expertise_id);
                            Log.e("Tag", "here is data from server from sub experty title: " + service_title);


                            expertieseIdList.add(new CustomeTagsGeterSeter(expertise_id, service_title, specialityID));
                        }

                        Log.e("TAG", "the size of expertiese is: " + expertieseIdList.size());

                        jsonArray = jsonObject.getJSONArray("registration");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject registrationObject = jsonArray.getJSONObject(i);

                            String innerRegistration = registrationObject.getString("registration");
                            JSONObject innerObject = new JSONObject(innerRegistration);
                            String registrationId = innerObject.getString("registration_id");
                            String registrationTitle = innerObject.getString("registration_title");

                            Log.e("Tag", "here is data from server from registration id: " + registrationId);
                            Log.e("Tag", "here is data from server from registratino title: " + registrationTitle);

                            registrationIdList.add(new CitiesGetterSetter(registrationId, registrationTitle));
                        }

                        jsonArray = jsonObject.getJSONArray("qualifications");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject qualificationsObject = jsonArray.getJSONObject(i);
                            String qualification_id = qualificationsObject.getString("qualification_id");
                            String qualification_title = qualificationsObject.getString("qualification_title");
                            String specialityID = qualificationsObject.getString("speciality_id");
                            Log.e("Tag", "here is data from server from qualification id: " + qualification_id);
                            Log.e("Tag", "here is data from server from qualification title: " + qualification_title);
                            qualificationIdList.add(new CustomeTagsGeterSeter(qualification_id, qualification_title, specialityID));
                        }

                        jsonArray = jsonObject.getJSONArray("institutions");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject institutionsObject = jsonArray.getJSONObject(i);
                            String innerSpecialisty = institutionsObject.getString("institutions");
                            JSONObject innerObject = new JSONObject(innerSpecialisty);
                            String instituationID = innerObject.getString("institutions_doctor_id");
                            String instituationTitle = innerObject.getString("institutions_doctor_title");

                            Log.e("Tag", "here is data from server from instituation id: " + instituationID);
                            Log.e("Tag", "here is data from server from instituation title: " + instituationTitle);

                            instituationIdList.add(new CitiesGetterSetter(instituationID, instituationTitle));

                        }


                        jsonArray = jsonObject.getJSONArray("hospitals");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            ArrayList<HashMap<String, String>> precticeData = new ArrayList<>();

                            JSONObject practicesObject = jsonArray.getJSONObject(i);
                            String doctor_hospital_min_fee = practicesObject.getString("doctor_hospital_min_fee");
                            String doctor_hospital_max_fee = practicesObject.getString("doctor_hospital_max_fee");
                            //getting discount fields
                            String doctor_offer_any_discount = practicesObject.getString("doctor_offer_any_discount");
                            String doctor_discount_for_check_up = practicesObject.getString("doctor_discount_for_check_up");
                            String doctor_discount_for_procedure = practicesObject.getString("doctor_discount_for_procedure");
                            String doctor_discount_for_other = practicesObject.getString("doctor_discount_for_other");
                            String doctor_detail_for_other_discount = practicesObject.getString("doctor_detail_for_other_discount");

                            if (doctor_offer_any_discount.equals("No")){
                                doctor_discount_for_check_up = "";
                                doctor_discount_for_procedure = "";
                                doctor_discount_for_other = "";
                                doctor_detail_for_other_discount = "";
                            }
                            if (doctor_offer_any_discount.equals("Yes")){
                                if(doctor_discount_for_check_up.equals("0")){doctor_discount_for_check_up = "";}
                                if (doctor_discount_for_procedure.equals("0")){doctor_discount_for_procedure = "";}
                                if (doctor_discount_for_other.equals("0")){doctor_discount_for_other = "";}
                            }


                            String hospitals = practicesObject.getString("hospitals");

                            JSONObject innerObject = new JSONObject(hospitals);
                            String hospital_id = innerObject.getString("hospital_id");
                            String hospital_name = innerObject.getString("hospital_name");


                            Log.e("TAg", "the hospital id is: " + hospital_id);
                            Log.e("TAg", "the hospital id is title: " + hospital_name);
                            Log.e("TAg", "the hospital id min fee: " + doctor_hospital_min_fee);
                            Log.e("TAg", "the hospital id max fee: " + doctor_hospital_max_fee);

                            JSONArray innerHospitalData = innerObject.getJSONArray("practices");

                            for (int j = 0; j<innerHospitalData.length(); j++){
                                HashMap<String, String> timingPrecticeData = new HashMap<>();

                                JSONObject innerHopitalPrecticeObject = innerHospitalData.getJSONObject(j);
                                String doctor_timing_start_time = innerHopitalPrecticeObject.getString("doctor_timing_start_time");
                                String doctor_timing_end_time = innerHopitalPrecticeObject.getString("doctor_timing_end_time");
                                String week_day_id = innerHopitalPrecticeObject.getString("week_day_id");

                                Log.e("TAg", "the hospital id is start time: " + doctor_timing_start_time);
                                Log.e("TAg", "the hospital id is end time: " + doctor_timing_end_time);
                                Log.e("TAg", "the hospital id is weedayid: " + week_day_id);

                                timingPrecticeData.put("hospital_id", hospital_id);
                                timingPrecticeData.put("hospital_name", hospital_name);
                                timingPrecticeData.put("doctor_hospital_min_fee", doctor_hospital_min_fee);
                                timingPrecticeData.put("doctor_hospital_max_fee", doctor_hospital_max_fee);
                                //puting discounts feilds in array
                                timingPrecticeData.put("doctor_offer_any_discount", doctor_offer_any_discount);
                                timingPrecticeData.put("doctor_discount_for_check_up", doctor_discount_for_check_up);
                                timingPrecticeData.put("doctor_discount_for_procedure", doctor_discount_for_procedure);
                                timingPrecticeData.put("doctor_discount_for_other", doctor_discount_for_other);
                                timingPrecticeData.put("doctor_detail_for_other_discount", doctor_detail_for_other_discount);

                                //timings and weekday id
                                timingPrecticeData.put("doctor_timing_start_time", doctor_timing_start_time);
                                timingPrecticeData.put("doctor_timing_end_time", doctor_timing_end_time);
                                timingPrecticeData.put("week_day_id", week_day_id);


                                precticeData.add(timingPrecticeData);

                            }

                            precticeDataList.add(precticeData);

                        }

                        jsonArray = mJsonObject.getJSONArray("all_subspecs");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject all_subspecsObject = jsonArray.getJSONObject(i);
                            String all_subspecs_id = all_subspecsObject.getString("sub_speciality_id");
                            String all_subspecs_title = all_subspecsObject.getString("sub_speciality_title");
                            String specialityId = all_subspecsObject.getString("speciality_id");


                            allSubspecialitiesList.add(new CustomeTagsGeterSeter(all_subspecs_id, all_subspecs_title, specialityId));
                        }

                        jsonArray = mJsonObject.getJSONArray("all_services");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject all_servicesObject = jsonArray.getJSONObject(i);
                            String service_id = all_servicesObject.getString("service_id");
                            String service_title = all_servicesObject.getString("service_title");
                            String specialityId = all_servicesObject.getString("speciality_id");


                            allServicesList.add(new CustomeTagsGeterSeter(service_id, service_title, specialityId));
                        }

                        jsonArray = mJsonObject.getJSONArray("all_qualifications");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject all_qualificationsObject = jsonArray.getJSONObject(i);
                            String qualification_id = all_qualificationsObject.getString("qualification_id");
                            String qualification_title = all_qualificationsObject.getString("qualification_title");
                            String specialityId = all_qualificationsObject.getString("speciality_id");


                            allQualificationsList.add(new CustomeTagsGeterSeter(qualification_id, qualification_title, specialityId));
                        }

                        jsonArray = mJsonObject.getJSONArray("all_institution");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject all_institutionObject = jsonArray.getJSONObject(i);
                            String institutions_doctor_id = all_institutionObject.getString("institutions_doctor_id");
                            String institutions_doctor_title = all_institutionObject.getString("institutions_doctor_title");


                            allInstituationList.add(new CitiesGetterSetter(institutions_doctor_id, institutions_doctor_title));
                        }

                        jsonArray = mJsonObject.getJSONArray("all_registrations");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject all_registrationsObject = jsonArray.getJSONObject(i);
                            String registration_id = all_registrationsObject.getString("registration_id");
                            String registration_title = all_registrationsObject.getString("registration_title");


                            allRegistrationList.add(new CitiesGetterSetter(registration_id, registration_title));
                        }

                        jsonArray = jsonObject.getJSONArray("gallery");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject gallaryImages = jsonArray.getJSONObject(i);
                            String image_id = gallaryImages.getString("doctor_gallery_id");
                            String image_url = gallaryImages.getString("doctor_gallery_img");
                            Log.e("TAG", "the size for array g : " + image_url);

                            listGallaryImages.add(new CitiesGetterSetter(image_id, image_url));
                        }

                        //

                        Log.e("TAG", "the size for array list specialisty: " + specialistIdList.size());
                        Log.e("TAG", "the size for array list subSpecialistIdList: " + subSpecialistIdList.size());
                        Log.e("TAG", "the size for array list servicesIdList: " + servicesIdList.size());
                        Log.e("TAG", "the size for array list qualificationIdList: " + qualificationIdList.size());
                        Log.e("TAG", "the size for array list expertieseIdList: " + expertieseIdList.size());
                        Log.e("TAG", "the size for array list instituationIdList: " + instituationIdList.size());
                        Log.e("TAG", "the size for array list registrationIdList: " + registrationIdList.size());
                        Log.e("TAG", "the size for array list precticeDataList: " + precticeDataList.size());
                        Log.e("TAG", "the size for array list gallary Images: " + listGallaryImages.size());

                        Log.e("TAG", "the size for array list all institute: " + allInstituationList.size());
                        Log.e("TAG", "the size for array list all registration: " + allRegistrationList.size());


                        //  Toast.makeText(getApplicationContext(), " Service Completed!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setAction(Glob.MY_ACTION);
                        intent.putExtra("DATAPASSED", 1);//sending notification that data received in service
                        sendBroadcast(intent);


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
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                String userId = "12523";
                SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                if (sharedPreferences != null) {
                    userId = sharedPreferences.getString("myid", null);
                    Log.e("TAg", "the user id is from shared pref: " + userId);
                    if (userId != null) {

                        Log.e("TAg", "the user id is: " + userId);
                    }
                }


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", userId);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public String getOnlyNumberFromString(String str) {
        StringBuffer sBuffer = new StringBuffer();
        Pattern p = Pattern.compile("[0-9]+.[0-9]*|[0-9]*.[0-9]+|[0-9]+");
        Matcher m = p.matcher(str);
        while(m.find())

        {
            sBuffer.append(m.group());
        }
        return sBuffer.toString();
    }

}

package themedicall.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.HospitalDocListRecyclerView;
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


public class HospitalDocList extends Fragment {
    public static RecyclerView recyclerView_hospitals_detail_Doc;
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
    public static LinearLayoutManager linearLayoutManager ;
    CustomProgressDialog dialog;
    public static TextView emptyViewHosDocList ;
    String hospitalId;
    Double rating ;

    public static ProgressBar bar ;
    private int totalDoctorInHospital;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;


    public HospitalDocList() {
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
        View view = inflater.inflate(R.layout.fragment_hospital_doc_list, container, false);


        initiate(view);
//        getHospitalDocList();
//        hosDocPaginationScrollListener();
        return view;
    }

    public void initiate(View view)
    {

        Intent intent = getActivity().getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            hospitalId = (String) bd.get("hospital_id");
            Log.e("tag", "get id using intent: " + hospital_id);

        }


        bar = (ProgressBar) view.findViewById(R.id.loadmore_hos_doc_progress);
        dialog=new CustomProgressDialog(getActivity(), 1);
        emptyViewHosDocList = (TextView) view.findViewById(R.id.emptyViewHosDocList);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

//        recyclerView_hospitals_detail_Doc = (RecyclerView) view.findViewById(R.id.recycler_view_hospitals_detail);
//        recyclerView_hospitals_detail_Doc.setHasFixedSize(true);
//        recyclerView_hospitals_detail_Doc.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));


        recyclerView_hospitals_detail_Doc = (RecyclerView) view.findViewById(R.id.recycler_view_hospitals_detail);
        recyclerView_hospitals_detail_Doc.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_hospitals_detail_Doc.setLayoutManager(linearLayoutManager);
        recyclerView_hospitals_detail_Doc.setAdapter(hosDoctorListRecycleView);

        hospitalDetailDocList = new ArrayList<>();
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
                            emptyViewHosDocList.setVisibility(View.VISIBLE);
                            recyclerView_hospitals_detail_Doc.setVisibility(View.GONE);
                        }
                        else
                        {
                            hosDoctorListRecycleView = new HospitalDocListRecyclerView(getContext() , hospitalDetailDocList);
                            recyclerView_hospitals_detail_Doc.setAdapter(hosDoctorListRecycleView);
                            hosDoctorListRecycleView.notifyDataSetChanged();
                        }




                       // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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

                Log.e("tag" , " offset in : " +offset);

                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("hospital_id", hospitalId);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getContext()) .addToRequestQueue(strReq, cancel_req_tag);
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

                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
                        }


                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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
                params.put("hospital_id", hospitalId);
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getContext()) .addToRequestQueue(strReq, cancel_req_tag);
    }


    // onPagination scroll listener
    public void hosDocPaginationScrollListener(){

        recyclerView_hospitals_detail_Doc.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalDoctorInHospital) {


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

        if (bar.getVisibility()== View.GONE){
            bar.setVisibility(View.VISIBLE);
        }


    }




}

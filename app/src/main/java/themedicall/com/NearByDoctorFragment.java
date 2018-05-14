package themedicall.com;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import themedicall.com.Adapter.FindDoctorListRecycleView;
import themedicall.com.GetterSetter.FindDoctorGetterSetter;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.PaginationScrollListener;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class NearByDoctorFragment extends Fragment {


    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject innerQualificationObject;
    JSONObject hospitalListObject;
    JSONArray basicInfoArray;
    private final String TAG = "NearbyDocList";
    public static String  doctor_id ,  doctor_offer_any_discount , doctor_full_name  , doctor_experience , doctor_mobile , doctor_min_fee , docor_max_fee , doctor_img , doctor_status , doctor_qualification , hospital_id , hospital_name , hospital_image ,  doctor_verified_status , doctor_profile_percent , doctor_views , doctor_url ,  hospital_landline_id ,  hospital_landline_number;
    Double  hospital_lat ,hospital_lng ;
    FindDoctorGetterSetter findDoctorGetterSetter ;
    List<FindDoctorGetterSetter> nexPageList;


    public static RecyclerView recyclerView_nearBy_Doc;
    public List<FindDoctorGetterSetter> nearDocList;
    ArrayList<HospitalMultipleDocGetterSetter> docHospitalList;
    ArrayList qualificationList;
    ArrayList<HospitalLandLineListGetterSetter> landLineList;
    ArrayList<HospitalLandLineListGetterSetter> tempLandLineList;
    String tempList , tempName;
    FindDoctorListRecycleView findDoctorListRecycleView ;
    public static LinearLayoutManager linearLayoutManager ;



    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    private int totalDataOnServer;
    public static ProgressBar bar;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    public static TextView emptyViewNearByDoc ;
    Double rating ;
    Double NearbyDocDistanceINKM ;
    double roundAboutKm ;
    boolean nearByDoctorServiceHasRun = false;

    public NearByDoctorFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_near_by_doctor, container, false);

        initiate(view);

        return view;

    }

    public void initiate(View view)
    {
        emptyViewNearByDoc = (TextView) view.findViewById(R.id.emptyViewNearByDoc);
        dialog=new CustomProgressDialog(getActivity(), 1);
        bar = (ProgressBar) view.findViewById(R.id.loadMore_nearby_doc_progress);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        recyclerView_nearBy_Doc = (RecyclerView) view.findViewById(R.id.recycler_view_nearby_doc);
        recyclerView_nearBy_Doc.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_nearBy_Doc.setLayoutManager(linearLayoutManager);
        recyclerView_nearBy_Doc.setAdapter(findDoctorListRecycleView);
        nearDocList = new ArrayList<>();

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            Log.e("tag" , "fragment visible");
            if(nearByDoctorServiceHasRun)
            {
                Log.e("tag" , "fragment near by doctor service already run");
            }
            else
            {
                getNearbyDocList();
            }
            nearbyDocPaginationScrollListener();
        }
        else
        {
            Log.e("tag" , "fragment not visible");
        }
    }



    public void getNearbyDocList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocList";

        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loadingImage.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Near by Doctor Response: " + response.toString());
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
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            nexPageList = new ArrayList<>();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");


                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


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
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);

                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");


                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }


                                    Log.e("tag", "hospital number size nearby : " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size nearby abc : " + tempLandLineList.size());

                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));

                                }


                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, NearbyDocLocation);


                                    NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000;

                                    roundAboutKm = round(NearbyDocDistanceINKM, 1);

                                }



                            }
                            Log.e("tag" , "Nearby hospital in distance "+roundAboutKm);
                            Log.e("tag" , "Nearby hospital in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);
                            nearDocList.add(findDoctorGetterSetter);

                        }


                        if(nearDocList.size() == 0)
                        {
                            NearByDoctorFragment.emptyViewNearByDoc.setVisibility(View.VISIBLE);
                            NearByDoctorFragment.recyclerView_nearBy_Doc.setVisibility(View.GONE);
                        }
                        else
                        {
                            findDoctorListRecycleView = new FindDoctorListRecycleView(getActivity() , nearDocList);
                            NearByDoctorFragment.recyclerView_nearBy_Doc.setAdapter(findDoctorListRecycleView);
                            findDoctorListRecycleView.notifyDataSetChanged();
                        }


                        nearByDoctorServiceHasRun = true ;

                        // Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //loadingImage.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();

                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()) .addToRequestQueue(strReq, cancel_req_tag);
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    public void getNearbyDocListNext(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "DocList";

        //progressDialog.setMessage("Adding you ...");

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_DOCTOR_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Doctor Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("doctors");



                        if (nexPageList.size()>1){
                            nexPageList.clear();
                        }

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            findDoctorGetterSetter = new FindDoctorGetterSetter();
                            docHospitalList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            qualificationList = new ArrayList();
                            tempLandLineList = new ArrayList<HospitalLandLineListGetterSetter>();



                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            doctor_id = basicInfoObject.getString("doctor_id");
                            doctor_offer_any_discount = basicInfoObject.getString("doctor_offer_any_discount");
                            doctor_verified_status = basicInfoObject.getString("doctor_verified_status");
                            doctor_full_name = basicInfoObject.getString("doctor_full_name");
                            doctor_experience = basicInfoObject.getString("doctor_experience");
                            doctor_mobile = basicInfoObject.getString("doctor_mobile");
                            doctor_min_fee = basicInfoObject.getString("doctor_min_fee");
                            docor_max_fee = basicInfoObject.getString("docor_max_fee");
                            doctor_img = basicInfoObject.getString("doctor_img");
                            doctor_profile_percent = basicInfoObject.getString("doctor_profile_percent");
                            doctor_views = basicInfoObject.getString("doctor_views");
                            doctor_url = basicInfoObject.getString("doctor_url");
                            Log.e("tag" , "rating rating doctor_views : " +doctor_views);
                            JSONObject statusObject = basicInfoObject.getJSONObject("experience_status");
                            doctor_status  = statusObject.getString("experience_status_title");

                            findDoctorGetterSetter.setDoctorRowShareLink(doctor_url);
                            findDoctorGetterSetter.setDoctorRowId(doctor_id);
                            findDoctorGetterSetter.setDoctorRowName(doctor_full_name);
                            findDoctorGetterSetter.setDoctorRowMinFees(doctor_min_fee);
                            findDoctorGetterSetter.setDoctorRowMaxFees(docor_max_fee);
                            findDoctorGetterSetter.setDoctorRowExperience(doctor_experience);
                            findDoctorGetterSetter.setDoctorRowProfileImg(doctor_img);
                            findDoctorGetterSetter.setDoctorRowSpeciality(FindDoctor.speciality_name);
                            findDoctorGetterSetter.setDoctorRowStatus(doctor_status);
                            findDoctorGetterSetter.setDoctor_offer_any_discount(doctor_offer_any_discount);
                            findDoctorGetterSetter.setDoctorRowVerifiedStatus(doctor_verified_status);
                            findDoctorGetterSetter.setDoctorRowNoOfThumbsUp("0");
                            findDoctorGetterSetter.setDoctorRowNoOfViews(doctor_views);


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
                            findDoctorGetterSetter.setDoctorRowRatingtext(rating);


                            JSONArray doctorQualificationArray = basicInfoObject.getJSONArray("qualifications");
                            for (int k = 0; k < doctorQualificationArray.length(); k++) {
                                JSONObject qualificationObject = doctorQualificationArray.getJSONObject(k);
                                innerQualificationObject = qualificationObject.getJSONObject("qualifications");
                                doctor_qualification = innerQualificationObject.getString("qualification_title");
                                qualificationList.add(doctor_qualification);

                            }

                            JSONArray hospitalArray = basicInfoObject.getJSONArray("hospitals");

                            if(hospitalArray.length() == 0)
                            {
                                Log.e("tag" , "no hospital found");
                            }
                            else {

                                for (int j = 0; j < hospitalArray.length(); j++) {
                                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);

                                    landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                                    Log.e("tag", "check land line list after creation : " + landLineList.size());

                                    hospitalListObject = hospitalObject.getJSONObject("hospitals");
                                    hospital_id = hospitalListObject.getString("hospital_id");
                                    hospital_name = hospitalListObject.getString("hospital_name");
                                    hospital_image = hospitalListObject.getString("hospital_img");
                                    hospital_lat = hospitalListObject.getDouble("hospital_lat");
                                    hospital_lng = hospitalListObject.getDouble("hospital_lng");


                                    JSONArray landLineArray = hospitalListObject.getJSONArray("landline");
                                    for (int k = 0; k < landLineArray.length(); k++) {

                                        JSONObject landLineObject = landLineArray.getJSONObject(k);
                                        hospital_landline_id = landLineObject.getString("hospital_id");
                                        hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                        landLineList.add(new HospitalLandLineListGetterSetter(hospital_name, hospital_landline_number));

                                    }


                                    Log.e("tag", "hospital number size nearby: " + hospital_name + " " + landLineList.size());

                                    for (int n = 0; n < landLineList.size(); n++) {
                                        tempList = landLineList.get(n).getHos_land_line_number();
                                        tempName = landLineList.get(n).getHos_name();

                                        HospitalLandLineListGetterSetter abc = new HospitalLandLineListGetterSetter(tempName, tempList);
                                        tempLandLineList.add(abc);
                                    }

                                    Log.e("tag", "temp hospital number size nearby abc : " + tempLandLineList.size());

                                    docHospitalList.add(new HospitalMultipleDocGetterSetter(hospital_id, hospital_image, hospital_name, hospital_lat, hospital_lng));

                                }


                                if(currentLang == 0.0)
                                {
                                    roundAboutKm = 0.0;
                                }
                                else
                                {
                                    LatLng NearbyDocLocation = new LatLng(docHospitalList.get(0).getHospitalRowDocLat(), docHospitalList.get(0).getHospitalRowDocLang());


                                    //Double NearbyDocDistanceInMeter = SphericalUtil.computeDistanceBetween(Home.userCurrentLocation, NearbyDocLocation);

                                    Double distance = CalculationByDistance(currentLatLang, NearbyDocLocation);


                                    // Double NearbyDocDistanceINKM = NearbyDocDistanceInMeter / 1000 ;

                                    roundAboutKm = round(distance, 1);

                                }




                            }

                            Log.e("tag" , "Nearby hospital in distance "+roundAboutKm);
                            //Log.e("tag" , "Nearby hospital in distance in Km "+NearbyDocDistanceINKM );




                            findDoctorGetterSetter.setDoctorRowDistance(roundAboutKm);
                            findDoctorGetterSetter.setDoctorRowDegree(qualificationList);
                            findDoctorGetterSetter.setHospitalList(docHospitalList);
                            findDoctorGetterSetter.setLandLineList(tempLandLineList);

                            //topDocList.add(findDoctorGetterSetter);

                            nexPageList.add(findDoctorGetterSetter);

                        }

                        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(nexPageList.size(), nexPageList);
                        findDoctorListRecycleView.addAll(loadNextData);
                        findDoctorListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // callBack.onSuccess(nexPageList);

                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity() , "error "+ errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Doctor Error: " + error.getMessage());
                Toast.makeText(getActivity() , error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("speciality_id", FindDoctor.speciality_id);
                params.put("city", FindDoctor.city);
                params.put("offset", offset);
                params.put("lat", String.valueOf(Home.lat));
                params.put("lng", String.valueOf(Home.lang));
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()) .addToRequestQueue(strReq, cancel_req_tag);

        //return topDocList;
    }
    //

    // onPagination scroll listener
    public void nearbyDocPaginationScrollListener(){

        NearByDoctorFragment.recyclerView_nearBy_Doc.addOnScrollListener(new PaginationScrollListener(NearByDoctorFragment.linearLayoutManager, totalDataOnServer) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextNearbyPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalDataOnServer );
                Log.e("TAG", "Total Data count layout: " +NearByDoctorFragment.linearLayoutManager.getItemCount() );
                if (indicator == 1){

                    return TOTAL_PAGES;
                }
                else {
                    return 0;
                }

            }

            @Override
            public boolean isLastPage() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;




            }

            @Override
            public boolean isLoading() {

                return (totalDataOnServer-1 == findDoctorListRecycleView.getItemCount()) ? true : false;




            }
        });
    }

    private void loadNextNearbyPage(String offset) {
        Log.d(TAG, "loadNextPage: " + currentPage);

        getNearbyDocListNext(offset);


        if (bar.getVisibility()== View.GONE){
            bar.setVisibility(View.VISIBLE);
        }


    }

    // end of nearby doc code


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










}

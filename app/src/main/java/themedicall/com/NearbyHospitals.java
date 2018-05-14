package themedicall.com;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import themedicall.com.Adapter.HospitalsListRecycleView;
import themedicall.com.GetterSetter.HospitalLandLineListGetterSetter;
import themedicall.com.GetterSetter.HospitalMultipleDocGetterSetter;
import themedicall.com.GetterSetter.HospitalsGetterSetter;
import themedicall.com.GetterSetter.SuggestionGetterSetter;
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

import static android.content.Context.MODE_PRIVATE;


public class NearbyHospitals extends Fragment {

    public static RecyclerView recyclerView_hospitals_nearby ;
    public static TextView emptyViewNearHos;
    public static ProgressBar bar;
    public static LinearLayoutManager linearLayoutManager;

    List<HospitalsGetterSetter> nexPageList;
    List<HospitalsGetterSetter> hospitalsList;
    ArrayList<HospitalMultipleDocGetterSetter> hospitalDocList;
    ArrayList landLineList;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon;
    View customActionBarView ;
    ProgressDialog progressDialog;
    JSONObject basicInfoObject;
    JSONObject docObject;
    JSONArray basicInfoArray;
    private static final String TAG = "Hospitals";
    String  hospital_id , hospital_name  , hospital_addr , hospital_img  ,   hospital_landline_number , doctor_id , doctor_full_name , doctor_image , hospital_views , hospital_share_url , hospital_offer_any_discount;
    Double hospital_lat , hospital_lng;
    String hospitalId , hospitalName ;
    HospitalsGetterSetter dm ;
    String  city;
    StringRequest strReq;
    String cancel_req_tag = "Hospitals";
    public static ArrayList<SuggestionGetterSetter> HospitalNameList = new ArrayList<SuggestionGetterSetter>();
    boolean nearByHospitalsServiceHasRun = false;



    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    private int totalDataOnServer;
    HospitalsListRecycleView hospitalsListRecycleView ;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;

    public  NearbyHospitals() {
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
        View view = inflater.inflate(R.layout.fragment_nearby_hospitals, container, false);
        initiate(view);
        return view;
    }

    public void initiate(View view)
    {
        dialog=new CustomProgressDialog(getActivity(), 1);

        emptyViewNearHos = (TextView) view.findViewById(R.id.emptyViewNearHos);
        recyclerView_hospitals_nearby = (RecyclerView) view.findViewById(R.id.recycler_view_nearby_hospitals);
        recyclerView_hospitals_nearby.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_hospitals_nearby.setLayoutManager(linearLayoutManager);
        bar = (ProgressBar) view.findViewById(R.id.loadmore_nearby_progress);



        hospitalsList = new ArrayList<>();
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
            if(nearByHospitalsServiceHasRun)
            {
                Log.e("tag" , "nearby hospital service already run");
            }
            else
            {
                getNearByHospitals();
            }
            nearByHospitalsPaginationScrollListener();
        }
        else
        {
            Log.e("tag" , "fragment not visible");
        }
    }


    public void getNearByHospitals()
    {

        String cancel_req_tag = "Hospitals";
        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        dialog.show();
        // NearbyHospitals.loading.setVisibility(View.VISIBLE);

        strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_HOSPITALS_LISTING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Hospital Response: " + response.toString());
                //hideDialog();
                //NearbyHospitals.loading.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                try {



                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("hospitals");


                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            dm = new HospitalsGetterSetter();
                            hospitalDocList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            landLineList = new ArrayList<HospitalLandLineListGetterSetter>();
                            nexPageList = new ArrayList<>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            hospital_id = basicInfoObject.getString("hospital_id");
                            hospital_name = basicInfoObject.getString("hospital_name");
                            hospital_addr = basicInfoObject.getString("hospital_addr");
                            hospital_img = basicInfoObject.getString("hospital_img");
                            hospital_lat = basicInfoObject.getDouble("hospital_lat");
                            hospital_lng = basicInfoObject.getDouble("hospital_lng");
                            hospital_views = basicInfoObject.getString("hospital_views");
                            hospital_share_url = basicInfoObject.getString("hospital_share_url");
                            hospital_offer_any_discount = basicInfoObject.getString("hospital_offer_any_discount");

                            Log.d("tag " , "hos name "+hospital_name);


                            LatLng HospitalLocation = new LatLng(hospital_lat, hospital_lng);


                            Double HospitalDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, HospitalLocation);


                            Double pharmacyDistanceINKM = HospitalDistanceInMeter / 1000 ;

                            double roundAboutKm = round(pharmacyDistanceINKM, 1);

                            Log.e("tag" , "Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Pharmacy in distance in Km "+pharmacyDistanceINKM );





                            dm.setHospitalId(hospital_id);
                            dm.setHospitalProfileName(hospital_name);
                            dm.setHospitalProfileAddress(hospital_addr);
                            dm.setHospitalProfileImg(hospital_img);
                            dm.setHospitalProfileNoOfDoc("0");
                            dm.setHospitalKm(roundAboutKm);
                            dm.setHospitalProfileNoOfViews(hospital_views);
                            dm.setHospitalProfileShareLink(hospital_share_url);
                            dm.setHospitalProfileDiscount(hospital_offer_any_discount);

                            JSONArray landLineArray = basicInfoObject.getJSONArray("landline");
                            for (int j = 0; j < landLineArray.length(); j++) {

                                JSONObject landLineObject = landLineArray.getJSONObject(j);
                                hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                landLineList.add(new HospitalLandLineListGetterSetter(hospital_name , hospital_landline_number));
                            }
//
                            JSONArray jsonArrayDoctor = basicInfoObject.getJSONArray("doctors");
                            for (int k = 0; k < jsonArrayDoctor.length(); k++) {
                                JSONObject hosDocObject = jsonArrayDoctor.getJSONObject(k);
                                docObject = hosDocObject.getJSONObject("doctors");


                                doctor_id = docObject.getString("doctor_id");
                                doctor_full_name = docObject.getString("doctor_full_name");
                                doctor_image = docObject.getString("doctor_img");

                                Log.d("tag" , "doc id "+doctor_id);
                                Log.d("tag" , "doc full_name "+doctor_full_name);
                                Log.d("tag" , "doc image "+doctor_image);

                                if(jsonArrayDoctor.length() > 0)
                                {
                                    dm.setHospitalProfileNoOfDoc(String.valueOf(jsonArrayDoctor.length()));
                                }
                                hospitalDocList.add(new HospitalMultipleDocGetterSetter(doctor_id, doctor_image , doctor_full_name , 1.3 , 3.5));

//                                if(k >= 2)
//                                {
//                                    break;
//                                }

                            }

                            dm.setLandLineList(landLineList);
                            dm.setAllDocList(hospitalDocList);

                            hospitalsList.add(dm);


                        }

                        if(hospitalsList.size() == 0)
                        {
                            emptyViewNearHos.setVisibility(View.VISIBLE);
                            recyclerView_hospitals_nearby.setVisibility(View.GONE);
                        }
                        else
                        {
                            hospitalsListRecycleView = new HospitalsListRecycleView(getActivity() , hospitalsList);
                            recyclerView_hospitals_nearby.setAdapter(hospitalsListRecycleView);
                            hospitalsListRecycleView.notifyDataSetChanged();
                        }



                        nearByHospitalsServiceHasRun = true ;

                        //getHospitalsNames();
                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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
                Log.e(TAG, "Nearby Hospital Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //NearbyHospitals.loading.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", Hospitals.city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                params.put("offset", offset);


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


    public void getNearByHospitalsNext(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospitals";

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_HOSPITALS_LISTING_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        basicInfoObject = new JSONObject(response);
                        String total = basicInfoObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalDataOnServer = Integer.parseInt(total);
                        basicInfoArray = basicInfoObject.getJSONArray("hospitals");

                        if (nexPageList.size()>1){
                            nexPageList.clear();
                        }

                        for (int i = 0; i < basicInfoArray.length(); i++) {

                            dm = new HospitalsGetterSetter();
                            hospitalDocList = new ArrayList<HospitalMultipleDocGetterSetter>();
                            landLineList = new ArrayList<HospitalLandLineListGetterSetter>();

                            JSONObject basicInfoObject = basicInfoArray.getJSONObject(i);

                            hospital_id = basicInfoObject.getString("hospital_id");
                            hospital_name = basicInfoObject.getString("hospital_name");
                            hospital_addr = basicInfoObject.getString("hospital_addr");
                            hospital_img = basicInfoObject.getString("hospital_img");
                            hospital_lat = basicInfoObject.getDouble("hospital_lat");
                            hospital_lng = basicInfoObject.getDouble("hospital_lng");
                            hospital_views = basicInfoObject.getString("hospital_views");
                            hospital_share_url = basicInfoObject.getString("hospital_share_url");
                            hospital_offer_any_discount = basicInfoObject.getString("hospital_offer_any_discount");

                            Log.d("tag " , "hos name "+hospital_name);


                            LatLng HospitalLocation = new LatLng(hospital_lat, hospital_lng);


                            Double HospitalDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, HospitalLocation);

                            //Double distance = CalculationByDistance(currentLatLang , HospitalLocation);


                            Double pharmacyDistanceINKM = HospitalDistanceInMeter / 1000 ;

                            double roundAboutKm = round(pharmacyDistanceINKM, 1);

                            Log.e("tag" , "Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Pharmacy in distance in Km "+pharmacyDistanceINKM );

                            dm.setHospitalId(hospital_id);
                            dm.setHospitalProfileName(hospital_name);
                            dm.setHospitalProfileAddress(hospital_addr);
                            dm.setHospitalProfileImg(hospital_img);
                            dm.setHospitalProfileNoOfDoc("0");
                            dm.setHospitalKm(roundAboutKm);
                            dm.setHospitalProfileNoOfViews(hospital_views);
                            dm.setHospitalProfileShareLink(hospital_share_url);
                            dm.setHospitalProfileDiscount(hospital_offer_any_discount);


                            JSONArray landLineArray = basicInfoObject.getJSONArray("landline");
                            for (int j = 0; j < landLineArray.length(); j++) {

                                JSONObject landLineObject = landLineArray.getJSONObject(j);
                                hospital_landline_number = landLineObject.getString("hospital_landline_number");

                                landLineList.add(new HospitalLandLineListGetterSetter(hospital_name , hospital_landline_number));
                            }
//
                            JSONArray jsonArrayDoctor = basicInfoObject.getJSONArray("doctors");
                            for (int k = 0; k < jsonArrayDoctor.length(); k++) {
                                JSONObject hosDocObject = jsonArrayDoctor.getJSONObject(k);
                                docObject = hosDocObject.getJSONObject("doctors");


                                doctor_id = docObject.getString("doctor_id");
                                doctor_full_name = docObject.getString("doctor_full_name");
                                doctor_image = docObject.getString("doctor_img");

                                Log.d("tag" , "doc id "+doctor_id);
                                Log.d("tag" , "doc full_name "+doctor_full_name);
                                Log.d("tag" , "doc image "+doctor_image);

                                if(jsonArrayDoctor.length() > 0)
                                {
                                    dm.setHospitalProfileNoOfDoc(String.valueOf(jsonArrayDoctor.length()));
                                }
                                hospitalDocList.add(new HospitalMultipleDocGetterSetter(doctor_id, doctor_image , doctor_full_name , 1.1 , 2.2));

//                                if(k >= 2)
//                                {
//                                    break;
//                                }

                            }

                            dm.setLandLineList(landLineList);
                            dm.setAllDocList(hospitalDocList);

                            nexPageList.add(dm);


                        }

                        List<HospitalsGetterSetter> loadNextData = HospitalsGetterSetter.creatData(nexPageList.size(), nexPageList);
                        hospitalsListRecycleView.addAll(loadNextData);
                        hospitalsListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getActivity() , " You are successfully Added!", Toast.LENGTH_SHORT).show();
                        //callBack.onSuccess(nexPageList);


                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //   Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", Hospitals.city);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                params.put("offset", offset);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        //AppSingleton.getInstance(this).addToRequestQueue(strReq, cancel_req_tag);
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);


        //return hospitalsList;
    }

    //



    //
    // onPagination scroll listener
    public void nearByHospitalsPaginationScrollListener(){

        recyclerView_hospitals_nearby.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalDataOnServer) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);




                loadNextNearByHospitalPage(offset);

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

                return (totalDataOnServer-1 == hospitalsListRecycleView.getItemCount()) ? true : false;




            }

            @Override
            public boolean isLoading() {

                return (totalDataOnServer-1 == hospitalsListRecycleView.getItemCount()) ? true : false;




            }
        });
    }

    private void loadNextNearByHospitalPage(String offset) {
        Log.d(TAG, "loadNextPage: " + currentPage);


        getNearByHospitalsNext(offset);


        if (bar.getVisibility()== View.GONE){
            bar.setVisibility(View.VISIBLE);
        }


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}

package themedicall.com;


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
import themedicall.com.Adapter.LabsListRecycleView;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.LabsGetterSetter;
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


public class NearByLabsFragment extends Fragment {

    public static RecyclerView recyclerView_nearBy_labs;
    List<LabsGetterSetter> nearByLabsList;
    List<LabsGetterSetter> nextNearByLabsList;
    public static TextView emptyViewNearbyLabList ;
    public static ProgressBar bar ;
    public static LinearLayoutManager linearLayoutManager ;


    StringRequest strReq;
    private static final String TAG = "Nearby Lab";
    String lab_name , lab_address , lab_img , lab_id , lab_contact , lab_offer_discount;
    Double lab_lat , lab_lng;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    public static SharedPreferences.Editor cityAndLatLngEditor;
    Double currentLat , currentLang;
    LatLng currentLatLang ;

    LabsListRecycleView labsListRecycleView;
    public boolean nearbyLabsServiceHasRun = false;


    private int totalLab;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    MyReceiverForNetworkDialog myReceiver;
    SharedPreferences sharedPreferences ;
    public static String city;



    public NearByLabsFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_near_by_labs, container, false);




        initiate(view);
        //getAllLabs();

        return view;
    }

    public void initiate(View view)
    {

        dialog=new CustomProgressDialog(getActivity(), 1);

        emptyViewNearbyLabList = (TextView) view.findViewById(R.id.emptyViewNearbyLabList);
        bar = (ProgressBar) view.findViewById(R.id.loadmore_nearby_lab_progress);
        recyclerView_nearBy_labs = (RecyclerView) view.findViewById(R.id.recycler_view_nearby_labs);
        recyclerView_nearBy_labs.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recyclerView_nearBy_labs.setLayoutManager(linearLayoutManager);
        nearByLabsList = new ArrayList<>();

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
        {
            Log.e("tag" , "fragment visible");
            if(nearbyLabsServiceHasRun)
            {
                Log.e("tag" , "fragment nearby lab service already run");
            }
            else
            {
                getNearByLabs();
            }

            nearByLabPaginationScrollListener();
        }
        else
        {
            Log.e("tag" , "fragment not visible");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }




    public void getNearByLabs()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Nearby Lab";


        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_LAB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Nearby Lab Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalLab = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("labs");


                        for (int i = 0; i < labArray.length(); i++) {
                            nextNearByLabsList = new ArrayList<>();

                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            lab_name = labInnerObject.getString("lab_name");
                            lab_address = labInnerObject.getString("lab_address");
                            lab_img = labInnerObject.getString("lab_img");
                            lab_id = labInnerObject.getString("lab_id");
                            lab_contact = labInnerObject.getString("lab_contact");
                            lab_lat = labInnerObject.getDouble("lab_lat");
                            lab_lng = labInnerObject.getDouble("lab_lng");
                            lab_offer_discount = labInnerObject.getString("lab_offer_discount");


                            LatLng allLabLocation = new LatLng(lab_lat, lab_lng);


                            Double nearbyLabDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, allLabLocation);


                            Double nearbyDistanceINKM = nearbyLabDistanceInMeter / 1000 ;

                            double roundAboutKm = round(nearbyDistanceINKM, 1);

                            Log.e("tag" , "Near by lab in distance "+roundAboutKm);
                            Log.e("tag" , "Near by lab in distance in Km "+nearbyDistanceINKM );


                            nearByLabsList.add(new LabsGetterSetter(lab_id , lab_img , roundAboutKm , lab_name , lab_address , lab_contact , lab_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName));


                        }


                        if(nearByLabsList.size() == 0)
                        {
                            NearByLabsFragment.emptyViewNearbyLabList.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            labsListRecycleView = new LabsListRecycleView(getActivity() , nearByLabsList);
                            NearByLabsFragment.recyclerView_nearBy_labs.setAdapter(labsListRecycleView);
                            labsListRecycleView.notifyDataSetChanged();
                        }




                        nearbyLabsServiceHasRun = true ;

                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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
                Log.e(TAG, "Nearby lab Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", Labs.city);
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
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }



    public void nextGetNearByLabs(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Nearby Lab";


        strReq = new StringRequest(Request.Method.POST, Glob.NEARBY_LAB_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Nearby Lab Response: " + response.toString());
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalLab = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("labs");

                        if (nextNearByLabsList.size()>1){
                            nextNearByLabsList.clear();
                        }


                        for (int i = 0; i < labArray.length(); i++) {


                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            lab_name = labInnerObject.getString("lab_name");
                            lab_address = labInnerObject.getString("lab_address");
                            lab_img = labInnerObject.getString("lab_img");
                            lab_id = labInnerObject.getString("lab_id");
                            lab_contact = labInnerObject.getString("lab_contact");
                            lab_lat = labInnerObject.getDouble("lab_lat");
                            lab_lng = labInnerObject.getDouble("lab_lng");
                            lab_offer_discount = labInnerObject.getString("lab_offer_discount");


                            LatLng allLabLocation = new LatLng(lab_lat, lab_lng);


                            Double nearbyLabDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, allLabLocation);


                            Double nearbyDistanceINKM = nearbyLabDistanceInMeter / 1000 ;

                            double roundAboutKm = round(nearbyDistanceINKM, 1);

                            Log.e("tag" , "Near by lab in distance "+roundAboutKm);
                            Log.e("tag" , "Near by lab in distance in Km "+nearbyDistanceINKM );


                            nearByLabsList.add(new LabsGetterSetter(lab_id , lab_img , roundAboutKm , lab_name , lab_address , lab_contact , lab_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName));

                            nextNearByLabsList.add(new LabsGetterSetter(lab_id , lab_img , roundAboutKm , lab_name , lab_address , lab_contact , lab_offer_discount));

                        }

                        List<LabsGetterSetter> loadNextData = LabsGetterSetter.creatData(nextNearByLabsList.size(), nextNearByLabsList);
                        labsListRecycleView.addAll(loadNextData);
                        labsListRecycleView.notifyDataSetChanged();

//                        labsListRecycleView = new LabsListRecycleView(Labs.this , nearByLabsList);
//                        NearByLabsFragment.recyclerView_nearBy_labs.setAdapter(labsListRecycleView);

                        // callBack.onSuccess(nextNearByLabsList);

                        if (bar.getVisibility() == View.VISIBLE){
                            bar.setVisibility(View.GONE);
                        }


                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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
                Log.e(TAG, "Nearby lab Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", Labs.city);
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
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    // onPagination scroll listener
    public void nearByLabPaginationScrollListener(){

        recyclerView_nearBy_labs.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalLab) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextNearByLabPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalLab );
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

                return (totalLab-1 == labsListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalLab-1 == labsListRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    //


    //
    private void loadNextNearByLabPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        nextGetNearByLabs(offset);


/*
        // List<BloodReqiredGetterSetter> myCurrentArrayList = gettingNextPageForData();


      //  Log.e("TAG", "MY Current 123: " + myCurrentArrayList.size());


        List<FindDoctorGetterSetter> loadNextData = FindDoctorGetterSetter.creatData(myCurrentArrayList.size(), myCurrentArrayList);

        Log.e("TAG", "MY Current: " + loadNextData.size());
        Log.e("TAG", "MY Current list list: " + myCurrentArrayList.size());


        loadNextData.subList(loadNextData.size() - myCurrentArrayList.size(), loadNextData.size()).clear();

        //List<BloodReqiredGetterSetter> currntList = loadNextData.subList(loadNextData.size()-20, loadNextData.size()).clear();
        Log.e(TAG, "MY Current Total Item Count abc : " + myCurrentArrayList.size());
*/


     /*   findDoctorListRecycleView.removeLoadingFooter();

            isLoading = false;


        findDoctorListRecycleView.addAll(loadNextData);


            if (currentPage != TOTAL_PAGES) {
                findDoctorListRecycleView.addLoadingFooter();


            } else {
                isLastPage = true;

            }*/

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

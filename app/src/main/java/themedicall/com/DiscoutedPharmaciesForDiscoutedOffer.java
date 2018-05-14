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
import themedicall.com.Adapter.PharmacyListRecycleView;
import themedicall.com.GetterSetter.PharmacyGetterSetter;
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

public class DiscoutedPharmaciesForDiscoutedOffer extends Fragment {


    RecyclerView recycler_view_discounted_pharmacy_for_dis_offers;
    TextView emptyViewDiscountedPharmacyListForDisOffers;
    ProgressBar bar;
    List<PharmacyGetterSetter> discountedPharmacyList;
    List<PharmacyGetterSetter> nextDiscountedPharmacyList;
    LinearLayoutManager linearLayoutManager ;
    StringRequest strReq;
    private static final String TAG = "Discounted Pharmacy";

    String pharmacies_name , pharmacies_address , pharmacies_img , pharmacies_id , pharmacies_contact, pharmacy_offer_discount;
    double  pharmacies_lat , pharmacies_lng ;
    CustomProgressDialog dialog;
    SharedPreferences sharedPreferencesCityAndLatLng;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    PharmacyListRecycleView pharmacyListRecycleView;



    private int totalPharmacy;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 40;
    private int currentPage = PAGE_START;
    private String offset = "0";
    private boolean isLoadingFinished = false;
    int indicator = -1;
    SharedPreferences sharedPreferences ;
    boolean discountedPharmacyHasRun = false ;

    public DiscoutedPharmaciesForDiscoutedOffer() {
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
        View view = inflater.inflate(R.layout.fragment_discouted_pharmacies_for_discouted_offer, container, false);

        initiate(view);

        return view;
    }

    public void initiate(View view)
    {

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);
        Log.e("tag"  , "currentLatLang in discounted pharmacy : " +currentLang);

        dialog=new CustomProgressDialog(getActivity() , 1);
        emptyViewDiscountedPharmacyListForDisOffers = (TextView) view.findViewById(R.id.emptyViewDiscountedPharmacyListForDisOffers);
        bar = (ProgressBar) view.findViewById(R.id.loadmore_discounted_pharmacy_for_dis_offer_progress);
        recycler_view_discounted_pharmacy_for_dis_offers = (RecyclerView) view.findViewById(R.id.recycler_view_discounted_pharmacy_for_dis_offers);
        recycler_view_discounted_pharmacy_for_dis_offers.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        recycler_view_discounted_pharmacy_for_dis_offers.setLayoutManager(linearLayoutManager);
        discountedPharmacyList = new ArrayList<>();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if(isVisibleToUser)
        {
            Log.e("tag" , "fragment visible");
            if(discountedPharmacyHasRun)
            {
                Log.e("tag" , "fragment discounted pharmacy service already run");
            }
            else
            {
                getDiscountedPharmacy();
                Log.e("tag" , "fragment discounted pharmacy service run again and again");
            }

            discountedPharmacyPaginationScrollListener();
        }
        else
        {
            Log.e("tag" , "fragment not visible");
        }


        super.setUserVisibleHint(isVisibleToUser);
    }




    public void getDiscountedPharmacy()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Discounted Pharmacy";

        dialog.show();

        strReq = new StringRequest(Request.Method.POST, Glob.DISCOUNTED_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Discounted Pharmacy Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    String total = jObj.getString("total");
                    Log.e("TAG", " The total available data is: " + total);
                    totalPharmacy = Integer.parseInt(total);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        for (int i = 0; i < labArray.length(); i++) {

                            nextDiscountedPharmacyList = new ArrayList<>();

                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng allPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            Double allPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang , allPharmacyLocation);


                            Double allPharmacyDistanceINKM = allPharmacyDistanceInMeter / 1000 ;

                            double roundAboutKm = round(allPharmacyDistanceINKM, 1);

                            Log.e("tag" , "Discounted Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Discounted Pharmacy in distance in Km "+allPharmacyDistanceINKM );

                            discountedPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);



                            //HospitalNameList.add(new CitiesGetterSetter(hospitalId , hospitalName))
                        }


                        if(discountedPharmacyList.size() == 0)
                        {
                            emptyViewDiscountedPharmacyListForDisOffers.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            pharmacyListRecycleView = new PharmacyListRecycleView(getActivity() , discountedPharmacyList);
                            recycler_view_discounted_pharmacy_for_dis_offers.setAdapter(pharmacyListRecycleView);
                            pharmacyListRecycleView.notifyDataSetChanged();
                        }

                        discountedPharmacyHasRun = true ;

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
                Log.e(TAG, "Discounted Pharmacy Error: " + error.getMessage());
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
                params.put("city", DiscountedOffers.city);
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



    public void NextgetDiscountedPharmacy(final String offset)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Discounted Pharmacy";


        strReq = new StringRequest(Request.Method.POST, Glob.DISCOUNTED_PHARMACY_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Discounted Pharmacy Response: " + response.toString());
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        String total = labObject.getString("total");
                        Log.e("TAG", " The total available data is: " + total);
                        totalPharmacy = Integer.parseInt(total);
                        JSONArray labArray = labObject.getJSONArray("pharmacies");


                        if (nextDiscountedPharmacyList.size()>1){
                            nextDiscountedPharmacyList.clear();
                        }

                        for (int i = 0; i < labArray.length(); i++) {


                            JSONObject labInnerObject = labArray.getJSONObject(i);

                            pharmacies_name = labInnerObject.getString("pharmacy_name");
                            pharmacies_address = labInnerObject.getString("pharmacy_address");
                            pharmacies_img = labInnerObject.getString("pharmacy_img");
                            pharmacies_id = labInnerObject.getString("pharmacy_id");
                            pharmacies_contact = labInnerObject.getString("pharmacy_contact");
                            pharmacies_lat = labInnerObject.getDouble("pharmacy_lat");
                            pharmacies_lng = labInnerObject.getDouble("pharmacy_lng");
                            pharmacy_offer_discount = labInnerObject.getString("pharmacy_offer_discount");


                            LatLng allPharmacyLocation = new LatLng(pharmacies_lat, pharmacies_lng);


                            Double allPharmacyDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang , allPharmacyLocation);


                            Double allPharmacyDistanceINKM = allPharmacyDistanceInMeter / 1000 ;

                            double roundAboutKm = round(allPharmacyDistanceINKM, 1);

                            Log.e("tag" , "Discounted Pharmacy in distance "+roundAboutKm);
                            Log.e("tag" , "Discounted Pharmacy in distance in Km "+allPharmacyDistanceINKM );

                            discountedPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));

                            //Log.d("tag " , "hos name "+hospitalName);


                            nextDiscountedPharmacyList.add(new PharmacyGetterSetter(pharmacies_id , pharmacies_img , roundAboutKm , pharmacies_name , pharmacies_address , pharmacies_contact , pharmacy_offer_discount));
                        }


                        List<PharmacyGetterSetter> loadNextData = PharmacyGetterSetter.creatData(nextDiscountedPharmacyList.size(), nextDiscountedPharmacyList);
                        pharmacyListRecycleView.addAll(loadNextData);
                        pharmacyListRecycleView.notifyDataSetChanged();

//                        PharmacyListRecycleView pharmacyListRecycleView = new PharmacyListRecycleView(Pharmacy.this , allPharmacyList);
//                        AllPharmacyFragment.recyclerView_all_pharmacy.setAdapter(pharmacyListRecycleView);

                        //callBack.onSuccess(nextNearByPharmacyList);

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
                Log.e(TAG, "Discounted Pharmacy Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("city", DiscountedOffers.city);
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
    public void discountedPharmacyPaginationScrollListener(){

        recycler_view_discounted_pharmacy_for_dis_offers.addOnScrollListener(new PaginationScrollListener(linearLayoutManager, totalPharmacy) {


            @Override
            protected void loadMoreItems() {


                isLoading = true;
                currentPage += 1;
                int currentOffest = Integer.parseInt(offset);
                currentOffest = currentOffest + 10;
                offset = Integer.toString(currentOffest);

                loadNextAllPharmacyPage(offset);

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

                Log.e("TAG", "Total Data count: " +totalPharmacy );
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

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }

            @Override
            public boolean isLoading() {

                return (totalPharmacy-1 == pharmacyListRecycleView.getItemCount()) ? true : false;

            }
        });
    }

    //


    //
    private void loadNextAllPharmacyPage(String offset) {
        Log.e(TAG, "loadNextPage: " + currentPage);

        //  List<FindDoctorGetterSetter> myCurrentArrayList = getDocListNext();

        NextgetDiscountedPharmacy(offset);

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

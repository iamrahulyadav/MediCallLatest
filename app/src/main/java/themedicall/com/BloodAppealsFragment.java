package themedicall.com;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import themedicall.com.Adapter.BloodAppealAdapter;
import themedicall.com.GetterSetter.BloodAppealsGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class BloodAppealsFragment extends Fragment {

    RecyclerView bloodAppealRecyclerView;
    LinearLayoutManager linearLayoutManager ;
    ArrayList<BloodAppealsGetterSetter> bloodAppealList ;
    CustomProgressDialog dialog;
    StringRequest strReq;
    String cancel_req_tag = "appeal blood";
    private static final String TAG = "appeal blood";
    String appeal_blood_id , appeal_blood_contact , appeal_blood_hospital_name , appeal_blood_detaills , appeal_blood_description , appeal_blood_lat , appeal_blood_lng  , ended_at , status , appeal_blood_location;

    double lat , lang ;
    boolean bloodAppealServiceHasRun = false ;
    SharedPreferences sharedPreferencesCityAndLatLng;
    public static SharedPreferences.Editor cityAndLatLngEditor;
    Double currentLat , currentLang;
    LatLng currentLatLang ;
    BloodAppealAdapter bloodAppealAdapter;
    SharedPreferences sharedPreferencesForBloodCurrentCity ;
    String cityForBlood;
    private int requestCount = 1;
    int last_page = 0;
    public BloodAppealsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blood_appeals, container, false);

        initiate(view);
        getBloodAppeal(requestCount);
        bloodAppealAdapter = new BloodAppealAdapter(getActivity() , bloodAppealList);
        bloodAppealRecyclerView.setAdapter(bloodAppealAdapter);
        scrollListener();

        return view;
    }

    public void initiate(View view)
    {

        dialog=new CustomProgressDialog(getActivity() , 1);
        bloodAppealRecyclerView = (RecyclerView) view.findViewById(R.id.bloodAppealRecyclerView);
        bloodAppealRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        bloodAppealRecyclerView.setLayoutManager(linearLayoutManager);
        bloodAppealList = new ArrayList<>();

        sharedPreferencesCityAndLatLng = getActivity().getSharedPreferences("CityPreferences" , MODE_PRIVATE);
        currentLat = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lat" , "0"));
        currentLang = Double.valueOf(sharedPreferencesCityAndLatLng.getString("lang" , "0"));
        Log.e("tag" , "lat in sp "+currentLat);
        Log.e("tag" , "lang in sp "+currentLang);
        currentLatLang = new LatLng(currentLat , currentLang);
        Log.e("tag"  , "currentLatLang in blood appeal : " +currentLang);

        sharedPreferencesForBloodCurrentCity = getActivity().getSharedPreferences("CityForBlood" , MODE_PRIVATE);
        cityForBlood = sharedPreferencesForBloodCurrentCity.getString("bloodCity" , "0");
        Blood.locationFilter.setText(cityForBlood);

        Log.e("tag" , "city for blood : "+cityForBlood);


    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//
//        if(isVisibleToUser)
//        {
//            Log.e("tag" , "fragment visible");
//            if(bloodAppealServiceHasRun)
//            {
//                Log.e("tag" , "fragment discounted hospital service already run");
//            }
//            else
//            {
//                getBloodAppeal();
//            }
//
//        }
//        else
//        {
//            Log.e("tag" , "fragment not visible");
//        }
//
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    public void getBloodAppeal(final int count)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "Blood Appeal";

        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.GET_APPEAL_BLOOD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Blood Appeal Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject labObject = new JSONObject(response);
                        JSONObject appealObject = labObject.getJSONObject("appeals");
                        last_page = appealObject.getInt("last_page");

                        Log.e("tag" , "last page is : "+last_page);

                        JSONArray appealArray = appealObject.getJSONArray("data");


                        for (int i = 0; i < appealArray.length(); i++) {

                            JSONObject labInnerObject = appealArray.getJSONObject(i);
//

                            appeal_blood_id = labInnerObject.getString("appeal_blood_id");
                            appeal_blood_contact = labInnerObject.getString("appeal_blood_contact");
                            appeal_blood_location = labInnerObject.getString("appeal_blood_location");
                            appeal_blood_hospital_name = labInnerObject.getString("appeal_blood_hospital_name");
                            appeal_blood_detaills = labInnerObject.getString("appeal_blood_detaills");
                            appeal_blood_description = labInnerObject.getString("appeal_blood_description");
                            appeal_blood_lat = labInnerObject.getString("appeal_blood_lat");
                            appeal_blood_lng = labInnerObject.getString("appeal_blood_lng");
                            ended_at = labInnerObject.getString("ended_at");
                            status = labInnerObject.getString("status");
                            appeal_blood_location = labInnerObject.getString("appeal_blood_location");
//

                            JSONObject bloodGroupObject = labInnerObject.getJSONObject("blood_group");
                            String blood_group_title = bloodGroupObject.getString("blood_group_title");


                            JSONObject userNameObject = labInnerObject.getJSONObject("user");
                            String user_full_name = userNameObject.getString("user_full_name");

                            Log.e("tag" , "blood appeal id "+appeal_blood_id);
                            Log.e("tag" , "blood appeal contact "+appeal_blood_contact);
                            Log.e("tag" , "blood appeal location "+appeal_blood_location);
                            Log.e("tag" , "blood appeal hospital name "+appeal_blood_hospital_name);
                            Log.e("tag" , "blood appeal detaills "+appeal_blood_detaills);
                            Log.e("tag" , "blood appeal description "+appeal_blood_description);
                            Log.e("tag" , "blood appeal blood_group_title "+blood_group_title);
                            Log.e("tag" , "blood appeal lat "+appeal_blood_lat);
                            Log.e("tag" , "blood appeal lng "+appeal_blood_lng);


                            if(appeal_blood_lng.equals("null") && appeal_blood_lng.equals("null"))
                            {
                               Log.e("tag" , "lat long null");

                                 lat = 31.373511215762186 ;
                                 lang = 74.18717723339796 ;
                            }
                            else
                            {
                                 lat = Double.valueOf(appeal_blood_lat) ;
                                 lang = Double.valueOf(appeal_blood_lng) ;
                            }


//


//
//
//
                            LatLng allLabLocation = new LatLng(lat , lang);


                            Double allLabDistanceInMeter = SphericalUtil.computeDistanceBetween(currentLatLang, allLabLocation);


                            Double allLabDistanceINKM = allLabDistanceInMeter / 1000 ;

                            double roundAboutKm = round(allLabDistanceINKM, 1);

                            Log.e("tag" , "Blood Appeal in distance "+roundAboutKm);
                            Log.e("tag" , "Blood Appeal in distance in Km "+allLabDistanceINKM );
//

                            bloodAppealList.add(new BloodAppealsGetterSetter(appeal_blood_id , blood_group_title , user_full_name , appeal_blood_contact , appeal_blood_detaills , appeal_blood_description  , status  , ended_at , appeal_blood_location , roundAboutKm));
//



                        }

                        Log.e("tag" , "blood appeal list size " + bloodAppealList.size());



                        bloodAppealAdapter.notifyDataSetChanged();

                        requestCount++;

                        bloodAppealServiceHasRun = true;


                       // Toast.makeText(getActivity() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

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
                Log.e(TAG, "Blood Appeal Error: " + error.getMessage());
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
                params.put("city", cityForBlood);
                params.put("lat", String.valueOf(currentLat));
                params.put("lng", String.valueOf(currentLang));
                params.put("page", String.valueOf(count));
                params.put("paginator", "10");

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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void scrollListener()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bloodAppealRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isLastItemDisplaying(recyclerView)) {
                        if(requestCount <= last_page) {
                            getBloodAppeal(requestCount);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "no further record found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bloodAppealRecyclerView.setOnScrollChangeListener( new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (isLastItemDisplaying(bloodAppealRecyclerView)) {


                        if(requestCount <= last_page)
                        {
                            getBloodAppeal(requestCount);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "no further record found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }
}

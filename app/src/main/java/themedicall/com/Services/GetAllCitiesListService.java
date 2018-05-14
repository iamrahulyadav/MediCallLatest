package themedicall.com.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.CitiesListCustomAdapter;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllCitiesListService extends Service {

    float value = 0;
    String cityName ;
    String cityId;
    CitiesListCustomAdapter citiesListCustomAdapter ;
    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String cancel_req_tag = "City Service";
    private static final String TAG = "City Service";
    public static List<CitiesGetterSetter> CityList;




    public GetAllCitiesListService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initiate();
        getCitiesService();

        return super.onStartCommand(intent, flags, startId);
    }


    public void initiate()
    {
        CityList = new ArrayList<>();
    }
    public void getCitiesService()
    {


        strReq = new StringRequest(Request.Method.GET, Glob.URL+"get-cities/"+Glob.Key, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "City Service Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("cities");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            cityId = finalobject.getString("city_id");
                            cityName = finalobject.getString("city_title");
                            CityList.add(new CitiesGetterSetter(cityId , cityName));

                        }


                        Log.e("TAg", "the cities size " + CityList.size());
                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                        if (dbHelper.getCount()<1) {
                            if (CityList.size() > 1) {
                                for (CitiesGetterSetter CGS : CityList) {
                                    long isInderted = dbHelper.insertCitiesInTable(CGS);
                                    if (isInderted > -1) {
                                        Log.e("TAG", "City inserted to table");
                                    }
                                }
                            }
                        }
                        Log.e("tag" , "cities added");


                        stopService(new Intent(getApplicationContext() , GetAllCitiesListService.class));

                        //Toast.makeText(getApplicationContext() , "Cities Added Successfully!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), "else part of service "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "City Service Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();

                //getCitiesService();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



}

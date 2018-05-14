package themedicall.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewLabTestReport extends AppCompatActivity {

    TextView patientNumber , caseNumber ;
    Button viewLabReportBtn ;
    JsonObjectRequest strReq;
    private static final String TAG = "View Lab Repor";
    CustomProgressDialog dialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lab_test_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiate();
        clickListener();

    }

    public void initiate()
    {
        dialog=new CustomProgressDialog(ViewLabTestReport.this, 1);
        patientNumber = (TextView) findViewById(R.id.patientNumber);
        caseNumber = (TextView) findViewById(R.id.caseNumber);
        viewLabReportBtn = (Button) findViewById(R.id.viewLabReportBtn);
    }

    public void clickListener()
    {



        viewLabReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String patientNumberStr =patientNumber.getText().toString();
                final String caseNumberStr =caseNumber.getText().toString();
                Log.e("tag" , "patient Number : "+patientNumberStr);
                Log.e("tag" , "case number : "+caseNumberStr);

//                if(patientNumberStr.equals(""))
//                {
//                    patientNumber.setError("Please enter patient number");
//                }
//                else if(caseNumberStr.equals(""))
//                {
//                    caseNumber.setError("Please enter case number" );
//                }
//                else
//                {
                    chugtaiLabQuickLoginResponse(patientNumberStr , caseNumberStr);

                    Log.e("tag" , "okay");
              //  }

            }
        });
    }






    public void chugtaiLabQuickLoginResponse(final String patientNumberStr, final String caseNumberStr)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "View Lab Report";

        dialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("PatientNumber", "19401-18-103068433");
        params.put("CaseNumber", "19401-03-01");

        strReq = new JsonObjectRequest(Request.Method.POST, Glob.CHUGTAI_LAB_QUICK_LOGIN  , new JSONObject(params) , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("tag", "Quick Login Response "+response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));

                    String Status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    Log.e("tag" , "tag quick login status "+Status);
                    Log.e("tag" , "tag quick login message "+message);

                    JSONObject jsonObjectToGetSessionId = jsonObject.getJSONObject("data");
                    String sessionId = jsonObjectToGetSessionId.getString("sessionID");
                    String userId = jsonObjectToGetSessionId.getString("userID");
                    String patientNumber = jsonObjectToGetSessionId.getString("patientNumber");

                    Log.e("tag" , "tag patient number in quick login : "+patientNumber);
                    Log.e("tag" , "tag user id in quick login : "+userId);
                    Log.e("tag" , "tag session id in quick login : "+sessionId);



                    chugtaiLabPatientResponseResponse(patientNumber , userId , sessionId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "view lab report Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }



        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "tmxUser:AdminPass123.";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Log.e("tag" , "basic auth : "+auth);
                headers.put("Authorization", auth);

                //  headers.put("Content-Type", "application/json");

                return headers;
            }


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("PatientNumber", "19401-18-103068433");
                params.put("CaseNumber", "19401-03-01");
                return params;
            }



        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }




    public void chugtaiLabPatientResponseResponse(String patientNumber, String userId , String sessionId)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "View Lab Report";

        dialog.show();


        Log.e("tag" , "tag patient number in patient report : "+patientNumber);
        Log.e("tag" , "tag user id in patient report : "+userId);
        Log.e("tag" , "tag session id in patient report: "+sessionId);


        Map<String, String> params = new HashMap<String, String>();
        params.put("PatientNumber", patientNumber);
        params.put("UserID", userId);
        params.put("SessionID ", sessionId);

        strReq = new JsonObjectRequest(Request.Method.POST, Glob.CHUGTAI_LAB_PATIENT_REPORT  , new JSONObject(params) , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.e("tag", "Patient Record Response "+response.toString());
                try {

                    JSONObject jsonObject = new JSONObject(String.valueOf(response));

                    String Status = jsonObject.getString("Status");
                    String message = jsonObject.getString("Message");

                    Log.e("tag" , "tag patient Record status "+Status);
                    Log.e("tag" , "tag patient Record message "+message);

//                    JSONObject jsonObjectToGetSessionId = jsonObject.getJSONObject("data");
//                    String sessionId = jsonObjectToGetSessionId.getString("sessionID");
//
//                    Log.e("tag" , "tag session id + "+sessionId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "patient Record Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }



        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "tmxUser:AdminPass123.";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Log.e("tag" , "basic auth : "+auth);
                headers.put("Authorization", auth);

                //  headers.put("Content-Type", "application/json");

                return headers;
            }


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("PatientNumber", "19401-18-103068433");
                params.put("CaseNumber", "19401-03-01");
                return params;
            }



        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }






}

package themedicall.com;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppealBlood extends AppCompatActivity {
    Spinner  appealBloodGroupSpinner , appealBloodUnitSpinner , appealBloodUrgencySpinner;
    String  appealBloodGroupSpinnerStr , appealBloodUnitSpinnerStr , appealBloodUrgencySpinnerStr;
    EditText appealBloodName , appealBloodContact , appealBloodHospitalName , appealBloodLocation ,  appealBloodAnyDetail ;
    String appealBloodNameStr , appealBloodContactStr , appealBloodHospitalNameStr , appealBloodLocationStr , appealBloodAnyDetailStr ;
    Button appealBloodSubmit;
    String[] bloodGroupArray = { "Blood Group" ,"A+" , "A-" , "B+" , "B-" , "AB+" , "AB-" , "O+" , "O-"};
    String[] bloodUnitArray = { "Blood Unit" ,"1 Unit" , "2 Unit" , "3 Unit" , "4 Unit" , "5 Unit" , "6 Unit" , "7 Unit" , "8 Unit" , "9 Unit" , "10 Unit"};
    String[] bloodUrgencyArray = { "Urgency" ,"Within 1 day" , "Within 2 days" , "Within 3 days" , "Within 4 days" , "Within 5 days" , "Within 6 days" , "Within 1 week"};
    ArrayAdapter<String> bloodGroupAdapter , bloodUnitAdapter , bloodUrgencyAdapter;
    String city;
    String Area;
    String Address;
    double lat;
    double lng ;
    int request = 1 ;
    CustomProgressDialog dialog;
    private final String TAG = "Appeal Blood";
    String mBloodgroupID ;
    String mUrgencyID ;
    int signInRequestCode = 200 ;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_blood);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();
        setArrayToSpinner();
        clickListener();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void initiate()
    {
        networkDialog = new Dialog(AppealBlood.this);
        dialog=new CustomProgressDialog(AppealBlood.this, 1);
        appealBloodGroupSpinner = (Spinner) findViewById(R.id.appealBloodGroup);
        appealBloodUnitSpinner = (Spinner) findViewById(R.id.appealBloodUnit);
        appealBloodUrgencySpinner = (Spinner) findViewById(R.id.appealBloodUrgency);
        appealBloodName = (EditText) findViewById(R.id.appealBloodName);
        appealBloodContact = (EditText) findViewById(R.id.appealBloodContact);
        appealBloodHospitalName = (EditText) findViewById(R.id.appealBloodHospitalName);
        appealBloodAnyDetail = (EditText) findViewById(R.id.appealBloodAnyDetail);
        appealBloodLocation = (EditText) findViewById(R.id.appealBloodLocation);
        appealBloodSubmit = (Button) findViewById(R.id.appealBloodSubmit);



        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null) {

            String userId = sharedPreferences.getString("userid", null);
            if (userId != null) {
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL + profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);

                appealBloodName.setText(userFullName);
                appealBloodName.setFocusable(false);
                appealBloodName.setClickable(false);

            }
            else
            {
                appealBloodName.setText("");
                appealBloodName.setFocusable(true);
                appealBloodName.setClickable(true);
            }
        }

    }

    public void setArrayToSpinner()
    {
        bloodGroupAdapter = new ArrayAdapter<String>(AppealBlood.this , R.layout.spinner_list , R.id.spinnerList , bloodGroupArray);
        appealBloodGroupSpinner.setAdapter(bloodGroupAdapter);

        bloodUnitAdapter = new ArrayAdapter<String>(AppealBlood.this , R.layout.spinner_list , R.id.spinnerList , bloodUnitArray);
        appealBloodUnitSpinner.setAdapter(bloodUnitAdapter);

        bloodUrgencyAdapter = new ArrayAdapter<String>(AppealBlood.this , R.layout.spinner_list , R.id.spinnerList , bloodUrgencyArray);
        appealBloodUrgencySpinner.setAdapter(bloodUrgencyAdapter);
    }

    public void clickListener()
    {
        appealBloodLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewHospitalActivity = new Intent(AppealBlood.this, MapForAppealBlood.class);
                startActivityForResult(createNewHospitalActivity , request);
            }
        });


        appealBloodSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appealBloodGroupSpinnerStr = appealBloodGroupSpinner.getSelectedItem().toString();
                appealBloodUnitSpinnerStr = appealBloodUnitSpinner.getSelectedItem().toString();
                appealBloodUrgencySpinnerStr = appealBloodUrgencySpinner.getSelectedItem().toString();


                appealBloodNameStr = appealBloodName.getText().toString();
                appealBloodContactStr = appealBloodContact.getText().toString();
                appealBloodLocationStr = appealBloodLocation.getText().toString();
                appealBloodHospitalNameStr = appealBloodHospitalName.getText().toString();
                appealBloodAnyDetailStr = appealBloodAnyDetail.getText().toString();


                if (appealBloodGroupSpinnerStr.equals("A+")){mBloodgroupID = "1";}
                if (appealBloodGroupSpinnerStr.equals("A-")){mBloodgroupID = "2";}
                if (appealBloodGroupSpinnerStr.equals("B+")){mBloodgroupID = "3";}
                if (appealBloodGroupSpinnerStr.equals("B-")){mBloodgroupID = "4";}
                if (appealBloodGroupSpinnerStr.equals("O+")){mBloodgroupID = "5";}
                if (appealBloodGroupSpinnerStr.equals("O-")){mBloodgroupID = "6";}
                if (appealBloodGroupSpinnerStr.equals("AB+")){mBloodgroupID = "7";}
                if (appealBloodGroupSpinnerStr.equals("AB-")){mBloodgroupID = "8";}





                if (appealBloodUrgencySpinnerStr.equals("Within 1 day")){mUrgencyID = "1";}
                if (appealBloodUrgencySpinnerStr.equals("Within 2 days")){mUrgencyID = "2";}
                if (appealBloodUrgencySpinnerStr.equals("Within 3 days")){mUrgencyID = "3";}
                if (appealBloodUrgencySpinnerStr.equals("Within 4 days")){mUrgencyID = "4";}
                if (appealBloodUrgencySpinnerStr.equals("Within 5 days")){mUrgencyID = "5";}
                if (appealBloodUrgencySpinnerStr.equals("Within 6 days")){mUrgencyID = "6";}
                if (appealBloodUrgencySpinnerStr.equals("Within 1 week")){mUrgencyID = "7";}




                if(appealBloodGroupSpinnerStr.equals("Blood Group"))
                {
                    Toast.makeText(AppealBlood.this, "Please select blood group", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodUnitSpinnerStr.equals("Blood Unit"))
                {
                    Toast.makeText(AppealBlood.this, "Please select blood unit", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodNameStr.equals(""))
                {
                    Toast.makeText(AppealBlood.this, "Please enter full name", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodContactStr.equals(""))
                {
                    Toast.makeText(AppealBlood.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodUrgencySpinnerStr.equals("Urgency"))
                {
                    Toast.makeText(AppealBlood.this, "Please select urgency", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodLocationStr.equals(""))
                {
                    Toast.makeText(AppealBlood.this, "Please select location", Toast.LENGTH_SHORT).show();
                }
                else if(appealBloodHospitalNameStr.equals(""))
                {
                    Toast.makeText(AppealBlood.this, "Please enter hospital / clinic name", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Log.e("tag" , "Appeal blood group : " + appealBloodGroupSpinnerStr);
                    Log.e("tag" , "Appeal blood unit : " + appealBloodUnitSpinnerStr);
                    Log.e("tag" , "Appeal blood name : " + appealBloodNameStr);
                    Log.e("tag" , "Appeal blood contact no : " + appealBloodContactStr);
                    Log.e("tag" , "Appeal blood urgency : " + appealBloodUrgencySpinnerStr);
                    Log.e("tag" , "Appeal blood location : " + appealBloodLocationStr);
                    Log.e("tag" , "Appeal blood hos / clinic name : " + appealBloodHospitalNameStr);
                    Log.e("tag" , "Appeal blood any detail : " + appealBloodAnyDetailStr);
                    checkLogin();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == request)
        {
            if(data == null)
            {
                Log.e("tag" , "data is null in onActivity Result for map");
            }
            else
            {
                city = data.getStringExtra("City");
                Area = data.getStringExtra("Area");
                Address = data.getStringExtra("Address");
                lat = data.getDoubleExtra("lat"  , 0.0);
                lng = data.getDoubleExtra("lng" , 0.0);

                appealBloodLocation.setText(Address);

                Log.e("tag" , "onActivityResult City : " + city);
                Log.e("tag" , "onActivityResult Area : " + Area);
                Log.e("tag" , "onActivityResult Address : " + Address);
                Log.e("tag" , "onActivityResult lat : " + lat);
                Log.e("tag" , "onActivityResult lng : " + lng);
            }

        }

        else if(requestCode == signInRequestCode)
        {
            if(data == null)
            {
                Log.e("tag" , "data is null in onActivity Result for sign in");
            }
            else
            {

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void appealBlood(final String userId)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Appeal Blood";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.APPEAL_BLOOD , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext() , "Appeal for Blood Successfully!", Toast.LENGTH_SHORT).show();



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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), R.string.internetConnection, Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                String description = appealBloodUnitSpinnerStr + " of " + appealBloodGroupSpinnerStr + " Required " + appealBloodUrgencySpinnerStr + " at " + appealBloodHospitalNameStr + ".";
                Log.e("tag" , "Appeal blood Description : "+description);

                Map<String, String> params = new HashMap<String, String>();

                Log.e("tag" , "mBloodgroupID " +mBloodgroupID);
                Log.e("tag" , "mUrgencyID " +mUrgencyID);
                Log.e("tag" , "user id " +userId);


                params.put("key", Glob.Key);
                params.put("user_id", userId);
                params.put("blood_group", mBloodgroupID);
                params.put("blood_unit", appealBloodUnitSpinnerStr);
                params.put("name", appealBloodNameStr);
                params.put("contact", appealBloodContactStr);
                params.put("urgency", mUrgencyID);
                params.put("lcation", appealBloodLocationStr);
                params.put("hospital_name", appealBloodHospitalNameStr);
                params.put("detail", appealBloodAnyDetailStr);
                params.put("decription", description);
                params.put("city", city);
                params.put("area", Area);
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service



    public void checkLogin()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL+profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);

                appealBlood(userId);


            }
            else {

                    Intent intent = new Intent(AppealBlood.this , SignIn.class);
                    startActivityForResult(intent , signInRequestCode);
            }
        }
    }


    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        myReceiver = new MyReceiverForNetworkDialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onStart();
    }


    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onResume();
    }

    public void networkChange(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new CheckConnectivity(), intentFilter);
    }


    public class MyReceiverForNetworkDialog extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub

            int datapassed = intent.getIntExtra("DATAPASSED", 0);


            if (datapassed == 1234){


                networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                networkDialog.setContentView(R.layout.internet_connection_dialog);
                networkDialog.setCancelable(false);
                TextView enable = (TextView) networkDialog.findViewById(R.id.enable);
                TextView exit = (TextView) networkDialog.findViewById(R.id.exit);
                networkDialog.show();

                enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(i);
                    }
                });


                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }

            if(datapassed == 12345)
            {
                Log.e("tag", "data passed form check connectivity : "+datapassed);
                networkDialog.dismiss();
            }

        }
    }



}

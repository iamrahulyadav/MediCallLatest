package themedicall.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PinVerification extends AppCompatActivity {
    CustomProgressDialog dialog;

    EditText et_pin_code;
    TextView tv_resend_code;
    Button bt_verify;

    RelativeLayout rl_et_pin,rl_after_success_verified;
    Button bt_verify_now, bt_verify_later;

    String mCode, mUserID;
    private int signUpPosition;

    SharedPreferences sharedPreferencesBloodDonor, sharedPreferencesDoctor, sharedPreferencesPatient, sharedPreferencesHospital, sharedPreferencesLab, sharedPreferencesPharmacy, sharedPreferencesHealthProfessional, sharedPreferencesOther;
    SharedPreferences prefPinTagPresist;

    private static final String TAG = "PinVerification";
    ProgressDialog progressDialog;


    String mProfileImage=null;
    String mClaimeeID = "";
    String mClaimeeName = "";
    String mFrome = "";
    CustomProgressDialog mClaimDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        btVerifygPin();
        resentCode();

    }

    private void init(){


        dialog=new CustomProgressDialog(PinVerification.this, 1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        rl_et_pin = (RelativeLayout) findViewById(R.id.rl_et_pin);
        rl_after_success_verified = (RelativeLayout) findViewById(R.id.rl_after_success_verified);
        et_pin_code = (EditText) findViewById(R.id.et_pin_code);
        tv_resend_code = (TextView) findViewById(R.id.tv_resend_code);
        bt_verify = (Button) findViewById(R.id.bt_verify);

        bt_verify_now = (Button) findViewById(R.id.bt_verify_now);
        bt_verify_later = (Button) findViewById(R.id.bt_verify_later);

        Intent getingData = getIntent();

        if (getingData.getExtras()!=null) {
            mCode = getingData.getExtras().getString("code");
            mUserID = getingData.getExtras().getString("user_id");
            signUpPosition = getingData.getExtras().getInt("signupposition");
            mClaimeeID = getingData.getStringExtra("claimee_id");
            mClaimeeName = getingData.getStringExtra("claimee_name");
            mFrome = getingData.getStringExtra("from");

            Log.e("TAG", "the from text is aa: "+ mFrome);

            if (mCode.equals("00")){//00 for taging for verifying already register user
                resendCodeServiceCall();
            }
        }

        prefPinTagPresist = getSharedPreferences("pinPresist", 0);
        SharedPreferences.Editor editor = prefPinTagPresist.edit();
        editor.putInt("value", 101);
        editor.commit();



        //share preff for login With facebbok
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null) {


            String pImage = sharedPreferences.getString("profile_img", null);
            Log.e("TAG", "user profile image of share itent : " + pImage);

            if (pImage!=null){
                mProfileImage = pImage;
            }
        }

    }

    private void btVerifygPin(){

        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pinFromET  = et_pin_code.getText().toString();
                Log.e("TAG", "THE CODE IS: " + mCode);


                if (!pinFromET.equals(mCode)){
                    Toast.makeText(PinVerification.this, "Incorrect Pin Code", Toast.LENGTH_SHORT).show();
                }else {

                    verifyingPinServiceCall();

                }
            }
        });


    }//end of button veryButton Handler

    public void verifyingPinServiceCall()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please Wait ...");
        //showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.PIN_VERIFICATION , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        rl_et_pin.setVisibility(View.GONE);
                        //bt_verify_now.setVisibility(View.GONE);

                        final String user_id = jObj.getString("user_id");
                        String user_name = jObj.getString("user_name");
                        final String user_table = jObj.getString("user_table");
                        String user_email = jObj.getString("user_email");
                        String user_phone = jObj.getString("user_phone");
                        final String full_name = jObj.getString("full_name");
                        String id = jObj.getString("id");
                        String verified_status = jObj.getString("verified_status");
                        String profile_img = jObj.getString("profile_img");
                        String experience_status = null;
                        if (user_table.equals("doctors")){
                            experience_status = jObj.getString("experience_status");
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putString("username", user_name);
                        editor.putString("userid", user_id);
                        editor.putString("usertable", user_table);
                        if (user_table.equals("doctors")){
                            editor.putString("experience_status", experience_status);
                        }
                        editor.putString("useremail", user_email);
                        editor.putString("userphone", user_phone);
                        editor.putString("userfullname", full_name);
                        editor.putString("myid", id);
                        editor.putString("verified_status", verified_status);
                        if (mProfileImage!=null){
                            editor.putString("profile_img", mProfileImage);
                        }else {
                            editor.putString("profile_img", profile_img);
                        }
                        editor.putString("sigupposition", Integer.toString(signUpPosition));
                        editor.commit();


                        if (signUpPosition == 0) {
                            startIncreasing();
                            onClickVerifyLater();
                            onClickUpdateProfileClick();

                            if (user_table.equals("doctors")) {
                                if (mClaimeeID.length()>2) {

                                    if (mFrome.equals("claim")) {
                                        Log.e("TAG", "here the id for claimmee: " + mClaimeeID);
                                        Log.e("TAG", "here the id for claimmer: " + id);
                                        Log.e("TAG", "here the id for claimmee name: " + mClaimeeName);

                                        //calling claim api
                                        cliamProfileSendingDataService(id, mClaimeeID, full_name, mClaimeeName);

                                    }

                                    else if (mFrome.equals("report")){

                                        final Dialog reportDialog = new Dialog(PinVerification.this);
                                        reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        reportDialog.setContentView(R.layout.custome_dialog_doctor_report);
                                        TextView tv_dialog_title = (TextView) reportDialog.findViewById(R.id.tv_dialog_title);
                                        final EditText et_dialog_report = (EditText) reportDialog.findViewById(R.id.et_dialog_report);
                                        Button bt_dialog_submit_report = (Button) reportDialog.findViewById(R.id.bt_dialog_submit_report);


                                        //submit button click listener
                                        bt_dialog_submit_report.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                String reportText = et_dialog_report.getText().toString();
                                                if (reportText.length()==0){
                                                    et_dialog_report.setError("Should not be Empty");

                                                }else
                                                {
                                                    reportDialog.dismiss();
                                                    Log.e("TAG","The report Text is: " + reportText);
                                                    //calling report api
                                                    reportProfileSendingDataService(user_id, mClaimeeID, reportText, full_name, mClaimeeName);

                                                }

                                            }
                                        });


                                        reportDialog.setCancelable(false);
                                        reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                        reportDialog.show();
                                    }

                                }
                                else {

                                }
                            }

                            Log.e("TAG", "the selected user position is: " + signUpPosition);

                        }
                        else {


                            //other user report herer
                            if (mFrome.equals("report")) {
                                final Dialog reportDialog = new Dialog(PinVerification.this);
                                reportDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                reportDialog.setContentView(R.layout.custome_dialog_doctor_report);
                                TextView tv_dialog_title = (TextView) reportDialog.findViewById(R.id.tv_dialog_title);
                                final EditText et_dialog_report = (EditText) reportDialog.findViewById(R.id.et_dialog_report);
                                Button bt_dialog_submit_report = (Button) reportDialog.findViewById(R.id.bt_dialog_submit_report);


                                //submit button click listener
                                bt_dialog_submit_report.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String reportText = et_dialog_report.getText().toString();
                                        if (reportText.length() == 0) {
                                            et_dialog_report.setError("Should not be Empty");

                                        } else {
                                            reportDialog.dismiss();
                                            Log.e("TAG", "The report Text is: " + reportText);
                                            //calling report api
                                            reportProfileSendingDataService(user_id, mClaimeeID, reportText, full_name, mClaimeeName);

                                        }

                                    }
                                });


                                reportDialog.setCancelable(false);
                                reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                reportDialog.show();


                            }
                            else {

                                Toast.makeText(PinVerification.this, "Only Doctor Can Claim Doctor Profile", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(PinVerification.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(PinVerification.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {



                Map<String, String> params = new HashMap<String, String>();


                params.put("key", Glob.Key);
                params.put("user_id", mUserID);





                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(PinVerification.this).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void resentCode(){

        tv_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendCodeServiceCall();
            }
        });
    }

    //web service fro resend code
    private void resendCodeServiceCall(){


        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        //showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.RESENT_VERIFICATION_CODE , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response resend: " + response.toString());
                //hideDialog();
                dialog.show();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(PinVerification.this , "Code Sent Successfully", Toast.LENGTH_SHORT).show();
                        dialog.hide();

                        mCode = jObj.getString("code");
                        Log.e("TAG", "The code is: " + mCode);

                        if (sharedPreferencesBloodDonor!=null){
                            SharedPreferences.Editor editor = sharedPreferencesBloodDonor.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }
                        if (sharedPreferencesDoctor!=null){
                            SharedPreferences.Editor editor = sharedPreferencesDoctor.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }
                        if (sharedPreferencesPatient!=null){
                            SharedPreferences.Editor editor = sharedPreferencesPatient.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }
                        if (sharedPreferencesHospital!=null){
                            SharedPreferences.Editor editor = sharedPreferencesHospital.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }
                        if (sharedPreferencesLab!=null){
                            SharedPreferences.Editor editor = sharedPreferencesLab.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }

                        if (sharedPreferencesPharmacy!=null){
                            SharedPreferences.Editor editor = sharedPreferencesPharmacy.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }

                        if (sharedPreferencesHealthProfessional!=null){
                            SharedPreferences.Editor editor = sharedPreferencesHealthProfessional.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }
                        if (sharedPreferencesOther!=null){
                            SharedPreferences.Editor editor = sharedPreferencesOther.edit();
                            editor.putString("code", mCode);
                            editor.commit();
                        }



                    } else {


                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(PinVerification.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(PinVerification.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {



                Map<String, String> params = new HashMap<String, String>();


                params.put("key", Glob.Key);
                params.put("user_id", mUserID);





                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(PinVerification.this).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service


    public void startIncreasing()
    {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.grow_image);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                bt_verify.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_after_success_verified.setVisibility(View.VISIBLE);
                anim.setFillAfter(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rl_after_success_verified.setVisibility(View.VISIBLE);
        rl_after_success_verified.startAnimation(anim);
    }

    public void  onClickVerifyLater()
    {
        bt_verify_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void onClickUpdateProfileClick()
    {

        bt_verify_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (signUpPosition == 0) {
                    Intent updateProfile = new Intent(PinVerification.this, UpdateDoctorProfile.class);
                    startActivity(updateProfile);
                    finish();
                }

                if (signUpPosition == 1) {

                    Intent updateProfile = new Intent(PinVerification.this, MedicalRecord.class);
                    //startActivity(updateProfile);
                    finish();
                }
                if (signUpPosition == 6) {
                    Intent updateProfile = new Intent(PinVerification.this, MedicalRecord.class);
                    //startActivity(updateProfile);
                    finish();
                }

            }
        });
    }

    //claim profile API service
    private void cliamProfileSendingDataService(final String myid, final String climId, final String doctorName, final String calimeeName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        mClaimDialog = new CustomProgressDialog(PinVerification.this, 1);
        mClaimDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.CLAI_PROFILE_MURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Calim Profile URL: " + response.toString());
                mClaimDialog.hide();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("error_message");
                        if (message.equals("Claimed Successfully")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(PinVerification.this);
                            alert.setTitle("Profiel Claim in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + doctorName + " Your Claim to "+ calimeeName + " Submitted Succesfully, We Will Notify You Soon");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            alert.show();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(PinVerification.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(PinVerification.this, "Server Connection Failed", Toast.LENGTH_SHORT).show();
                mClaimDialog.cancel();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", myid);
                params.put("claimed_id", climId);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppSingleton.getInstance(PinVerification.this).addToRequestQueue(strReq, cancel_req_tag);
    }


    //claim report user api API service
    private void reportProfileSendingDataService(final String reporterID, final String toReportID, final String reportText, final String reporterName, final String toReportName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog = new CustomProgressDialog(PinVerification.this, 1);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.REPORT_DOCTOR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Calim Profile URL: " + response.toString());
                dialog.hide();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("error_message");
                        if (message.equals("Reported Successfully")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(PinVerification.this);
                            alert.setTitle("Your Report in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + reporterName + " Your Report Against "+ toReportName + " Submitted Succesfully, We Will Notify You Soon");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    finish();

                                }
                            });

                            alert.show();
                            alert.setCancelable(false);
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(PinVerification.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(PinVerification.this, "Server Connection Failed", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("doctor_id", toReportID);
                params.put("user_id", reporterID);
                params.put("report", reportText);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppSingleton.getInstance(PinVerification.this).addToRequestQueue(strReq, cancel_req_tag);
    }

}

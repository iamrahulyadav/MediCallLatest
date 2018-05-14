package themedicall.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class PinVerificationForForgotPassword extends AppCompatActivity {

    CustomProgressDialog dialog;
    EditText et_pin_code;
    TextView tv_resend_code;
    Button bt_verify, bt_new_password_submit;

    EditText et_new_password, et_confirm_password;
    TextView tv_description_text;



    RelativeLayout rl_et_pin,rl_after_success_verified, rl_new_password;
    Button bt_verify_now, bt_verify_later;

    String mCode, mUserID;
    private int signUpPosition;

    SharedPreferences sharedPreferencesBloodDonor, sharedPreferencesDoctor, sharedPreferencesPatient, sharedPreferencesHospital, sharedPreferencesLab, sharedPreferencesPharmacy, sharedPreferencesHealthProfessional, sharedPreferencesOther;
    SharedPreferences prefPinTagPresist;

    private static final String TAG = "PinVerification";
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_verification_for_forgot_password);
        getSupportActionBar().setTitle("Reset Password");


        init();
        btVerifygPin();
        resentCode();
        gentrateNewPasswordRequest();
        onClickUpdateProfileClick();


    }// endo fo onCreate


    private void init(){


        dialog=new CustomProgressDialog(PinVerificationForForgotPassword.this, 1);

        rl_et_pin = (RelativeLayout) findViewById(R.id.rl_et_pin);
        rl_after_success_verified = (RelativeLayout) findViewById(R.id.rl_after_success_verified);
        tv_description_text = (TextView) findViewById(R.id.tv_description_text);
        rl_new_password = (RelativeLayout) findViewById(R.id.rl_new_password);
        et_pin_code = (EditText) findViewById(R.id.et_pin_code);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        tv_resend_code = (TextView) findViewById(R.id.tv_resend_code);
        bt_verify = (Button) findViewById(R.id.bt_verify);
        bt_new_password_submit = (Button) findViewById(R.id.bt_new_password_submit);

        bt_verify_now = (Button) findViewById(R.id.bt_verify_now);
        bt_verify_later = (Button) findViewById(R.id.bt_verify_later);

        Intent getingData = getIntent();

        if (getingData.getExtras()!=null) {

            mCode = getingData.getExtras().getString("pinCode");
            mUserID = getingData.getExtras().getString("user_id");
            Log.e("TAG", "user id : " + mUserID);
            Log.e("TAG", "user codemCode : " + mCode);

            if (mCode.equals("00")){//00 for taging for verifying already register user
                //resendCodeServiceCall();
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
                    Toast.makeText(PinVerificationForForgotPassword.this, "Incorrect Pin Code", Toast.LENGTH_SHORT).show();
                }else {


                    Log.e("TAG", "the data matach successfully");

                    rl_et_pin.setVisibility(View.GONE);
                    rl_new_password.setVisibility(View.VISIBLE);
                    bt_verify.setVisibility(View.GONE);
                    bt_new_password_submit.setVisibility(View.VISIBLE);
                    tv_description_text.setVisibility(View.GONE);



                    // verifyingPinServiceCall();

                }
            }
        });


    }//end of button veryButton Handler
    public void verifyingPinServiceCall(final String userId)
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
                        startIncreasing();
                        onClickVerifyLater();
                        onClickUpdateProfileClick();



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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {



                Map<String, String> params = new HashMap<String, String>();


                params.put("key", Glob.Key);
                params.put("user_id", userId);


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

                        Toast.makeText(getApplicationContext() , "Code Sent Successfully", Toast.LENGTH_SHORT).show();
                        dialog.hide();

                        mCode = jObj.getString("code");


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
                Toast.makeText(getApplicationContext(),
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service


    public void startIncreasing()
    {
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.grow_image);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rl_after_success_verified.setVisibility(View.VISIBLE);
                anim.setFillAfter(true);
                bt_new_password_submit.setVisibility(View.GONE);

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


                Intent updateProfile = new Intent(PinVerificationForForgotPassword.this, SignIn.class);
                startActivity(updateProfile);
                finish();

            }
        });
    }

    private void gentrateNewPasswordRequest(){

        bt_new_password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_new_password = (EditText) findViewById(R.id.et_new_password);
                et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
                String etNewPassword = et_new_password.getText().toString();
                String etConfirmPassword = et_confirm_password.getText().toString();

                if (etNewPassword.length()==0){
                    et_new_password.setError("Should Not be Empty");
                }
                else if (etConfirmPassword.length()==0){
                    et_confirm_password.setError("Should Not be Empty");

                }
                else if (etNewPassword.length()<6){
                    et_new_password.setError("Should More Than 5 Cherecters");
                }
                else if (!etNewPassword.equals(etConfirmPassword)){
                    Toast.makeText(PinVerificationForForgotPassword.this, "Password Not Match", Toast.LENGTH_SHORT).show();
                }

                else {

                    updateNewPassword(mUserID, etNewPassword);

                    //


                    //call new password services
                }
            }
        });
    }


    //web service fro resend code
    private void updateNewPassword(final String mUserID, final String newPassword){


        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.RESET_PASSWORD , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "password changed response: " + response.toString());
                //hideDialog();
                dialog.show();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        dialog.hide();


                        rl_new_password.setVisibility(View.GONE);
                        startIncreasing();

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
                Toast.makeText(getApplicationContext(),
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
                params.put("password", newPassword);


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


}


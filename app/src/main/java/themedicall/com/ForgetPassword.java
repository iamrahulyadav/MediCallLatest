package themedicall.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgetPassword extends AppCompatActivity {

    EditText et_user_text_for_forgot_password;
    private String textFromFeild;
    private Button bt_submit;
    CustomProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
        btLoginHandler();
        startMobileWithOnlyNumber92();

    }// end of onCreate



    private void init(){
        et_user_text_for_forgot_password = (EditText) findViewById(R.id.et_user_text_for_forgot_password);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        dialog=new CustomProgressDialog(ForgetPassword.this, 1);

    }

    public void btLoginHandler(){

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFromFeild =   et_user_text_for_forgot_password.getText().toString();

                if (textFromFeild.length() == 0){
                    et_user_text_for_forgot_password.setError("Field Should not be empty");

                }
                else if (textFromFeild.contains(" ")){
                    et_user_text_for_forgot_password.setError("Field should bot contain white spaces");
                }
                else {

                    if (textFromFeild.startsWith("0")){

                        textFromFeild = textFromFeild.substring(1);
                        textFromFeild  = "92"+textFromFeild;
                        Log.e("TAg", "the Mobile Number is: " + textFromFeild);
                    }

                    Log.e("TAg", "the text from field is:  " + textFromFeild);
                    //calling web Services
                    forgotPasswordRequest(textFromFeild);

                }

            }
        });
    }//end of btLogin Handler


    //calling werb serviec for Login
    public void forgotPasswordRequest(final String textField)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.FORGOT_PASSWORD , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Forgot Password: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String userID = jObj.getString("user_id");
                        String pinCode = jObj.getString("code");

                        Intent pinCodeVerificationClass = new Intent(ForgetPassword.this, PinVerificationForForgotPassword.class);
                        pinCodeVerificationClass.putExtra("user_id", userID);
                        pinCodeVerificationClass.putExtra("pinCode", pinCode);
                        startActivity(pinCodeVerificationClass);
                        finish();


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
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();

                params.put("key", Glob.Key);
                params.put("text", textField);



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


    public void startMobileWithOnlyNumber92()
    {

        et_user_text_for_forgot_password.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(ForgetPassword.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_text_for_forgot_password.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorForgetPasswordEmail.setText(x);
                        et_user_text_for_forgot_password.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    et_user_text_for_forgot_password.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
                }



            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });//end for login editText

    }


}

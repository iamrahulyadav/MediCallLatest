package themedicall.com;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SignIn extends AppCompatActivity{
    EditText doctorSignInEmail , doctorSignInPass;
    TextView doctorSignInForgetPass ;
    Button doctorSignInBtn  , doctorSignUpButton;
    RelativeLayout doctorSignInFbBtn, doctorSignInGmailBtn;
    TextView tv_google, tv_fb;
    ProgressDialog progressDialog;
    String mSignInText, mSigninPassword;
    CustomProgressDialog dialog;

    RelativeLayout doctorSignInFb;
    CallbackManager callbackManager;
    private AccessTokenTracker mAccessTokenTracker;

    private static final String EMAIL = "email";
    private boolean isLogin = false;


    RelativeLayout iv_circular;

    //google sign in
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    String mClaimeeID = "";
    String mClimeeName = "";
    String mFrome = "";
    CustomProgressDialog mClaimDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custome_login_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initiate();
        clickListener();
        btLoginHandler();
        startMobileWithOnlyNumber92();




        //google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignIn();
        signOut();

    }

    public void initiate()
    {

        iv_circular = (RelativeLayout) findViewById(R.id.iv_circular);
        iv_circular.bringToFront();

        String claimeeId = getIntent().getStringExtra("claimee_id");
        Log.e("TAG", "the claimedId is: " + claimeeId);
        mFrome = getIntent().getStringExtra("from");
        if (mFrome==null){
            mFrome = "";
        }
        Log.e("TAG", "the from value is: " + mFrome);


        if (claimeeId!=null){
            mClimeeName = getIntent().getStringExtra("claimee_name");

            mClaimeeID = claimeeId;
        }




        dialog=new CustomProgressDialog(SignIn.this, 1);
        doctorSignInEmail = (EditText) findViewById(R.id.doctorSignInEmail);
        doctorSignInPass = (EditText) findViewById(R.id.doctorSignInPass);
        doctorSignInForgetPass = (TextView) findViewById(R.id.doctorSignInForgetPass);
        doctorSignInBtn = (Button) findViewById(R.id.doctorSignInBtn);
        doctorSignInFbBtn = (RelativeLayout) findViewById(R.id.doctorSignInFb);
        doctorSignInGmailBtn = (RelativeLayout) findViewById(R.id.doctorSignInGmail);
        tv_google = (TextView) findViewById(R.id.tv_google);
        tv_fb = (TextView) findViewById(R.id.tv_fb);
        doctorSignUpButton = (Button) findViewById(R.id.doctorSignUpButton);

        progressDialog = new ProgressDialog(this);


        doctorSignInFb = (RelativeLayout) findViewById(R.id.doctorSignInFb);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        loginWithFacebookClick();
        setupFacebook();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i("TAG", "The Facebook response sucess result: " + loginResult);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("TAG", "The Facebook response email: " + response);



                                String serverResponse = response.toString();
                                if (serverResponse.contains("email")){


                                    try {
                                        String id = object.getString("id");
                                        String email = object.getString("email");
                                        // String birthday = object.getString("birthday"); // 01/31/1980 format
                                        String gender = object.getString("gender");
                                        String name = object.getString("name");

                                        Log.e("TAG", "The Facebook response id: " + id);
                                        Log.e("TAG", "The Facebook response email: " + email);
                                        // Log.i("TAG", "The Facebook response birthday: " + birthday);
                                        Log.e("TAG", "The Facebook response gender: " + gender);
                                        Log.e("TAG", "The Facebook response name: " + name);
                                        Log.e("TAG", "The Facebook response user image: " + "https://graph.facebook.com/" + id+ "/picture?type=large");

                                        String profileImage = "https://graph.facebook.com/" + id+ "/picture?type=large";

                                        callingSocialLoginServer(id, name, profileImage, email, gender);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else {

                                    // Application code
                                    try {
                                        String id = object.getString("id");
                                        //  String email = object.getString("email");
                                        // String birthday = object.getString("birthday"); // 01/31/1980 format
                                        String gender = object.getString("gender");
                                        String name = object.getString("name");

                                        Log.e("TAG", "The Facebook response id: " + id);
                                        //  Log.e("TAG", "The Facebook response email: " + email);
                                        // Log.i("TAG", "The Facebook response birthday: " + birthday);
                                        Log.e("TAG", "The Facebook response gender: " + gender);
                                        Log.e("TAG", "The Facebook response name: " + name);
                                        Log.e("TAG", "The Facebook response user image: " + "https://graph.facebook.com/" + id + "/picture?type=large");

                                        String profileImage = "https://graph.facebook.com/" + id+ "/picture?type=large";
                                        callingSocialLoginServer(id, name, profileImage, "", gender);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void setupFacebook() {


        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                Log.i("TAg", "the current token of user old: " + oldAccessToken);
                Log.i("TAg", "the current token of user: " + currentAccessToken);

                if (isLogin){
                    LoginManager.getInstance().logOut();
                    isLogin = false;
                    tv_fb.setText("Sign in with Facebook");
                }
                else {

                    isLogin = true;
                    tv_fb.setText("Log out");
                }
            }
        };

    }

    private void loginWithFacebookClick()
    {
        doctorSignInFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(
                        SignIn.this,
                        Arrays.asList("public_profile", "email", "user_birthday", "user_friends")
                );
            }
        });
    }

    public void clickListener()
    {
        doctorSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFrome.equals("claim")){
                    Intent intent = new Intent(SignIn.this , MedicSignup.class);
                    intent.putExtra("item_position" , 0);
                    intent.putExtra("claimee_id", mClaimeeID);
                    intent.putExtra("claimee_name", mClimeeName);
                    intent.putExtra("from", mFrome);
                    startActivity(intent);
                    finish();


                }else if (mFrome.equals("medistuy")){

                    Intent intent = new Intent(SignIn.this , MedicSignup.class);
                    intent.putExtra("item_position" , 0);
                    startActivity(intent);
                    finish();

                }
                else {
                    Intent intent = new Intent(SignIn.this , SelectSignUpOptions.class);
                    intent.putExtra("claimee_id", mClaimeeID);
                    intent.putExtra("claimee_name", mClimeeName);
                    intent.putExtra("from", mFrome);
                    startActivity(intent);
                    finish();
                }


            }
        });


        doctorSignInForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this , ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });
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


    public void btLoginHandler(){

        doctorSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignInText =   doctorSignInEmail.getText().toString();
                mSigninPassword = doctorSignInPass.getText().toString();

                if (mSignInText.length() == 0){
                    doctorSignInEmail.setError("Field Should not be empty");

                }
                else if (mSignInText.contains(" ")){
                    doctorSignInEmail.setError("Field should bot contain white spaces");
                }
                else if (mSigninPassword.length() == 0){
                    doctorSignInPass.setError("Field should not be empty");
                }
                else if (mSigninPassword.length()<5){
                    doctorSignInPass.setError("Password leangth should be more then 5 charecters");
                }
                else {

                    if (mSignInText.startsWith("0")){

                        mSignInText = mSignInText.substring(1);
                        mSignInText  = "92"+mSignInText;
                        Log.e("TAg", "the Mobile Number is: " + mSignInText);
                    }


                    //calling web Services
                    signUpRegistration(mSignInText ,mSigninPassword);

                }

            }
        });
    }//end of btLogin Handler

    //calling werb serviec for Login
    public void signUpRegistration(final String textField, final String password)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        //  showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.SIGNIN , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext() , "Login Successfully!", Toast.LENGTH_SHORT).show();

                        String userNname =  jObj.getString("user_name");
                        final String userID = jObj.getString("user_id");
                        String userTable = jObj.getString("user_table");
                        String userEmail = jObj.getString("user_email");
                        String userPhone = jObj.getString("user_phone");
                        final String userFullName = jObj.getString("full_name");
                        String my_id = jObj.getString("id");
                        String verified_status = jObj.getString("verified_status");
                        String profile_img = jObj.getString("profile_img");
                        String experience_status = null;
                        if (userTable.equals("doctors")){
                            experience_status = jObj.getString("experience_status");
                        }


                        Log.e("TAg", "the user name: " + userNname);
                        Log.e("TAg", "the user userID: " + userID);
                        Log.e("TAg", "the user userTable: " + userTable);
                        Log.e("TAg", "the user userPhone: " + userPhone);
                        Log.e("TAg", "the user my_id: " + my_id);
                        Log.e("TAg", "the user varified Status: " + verified_status);
                        Log.e("TAg", "the user profile_image: " + profile_img);


                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putString("username", userNname);
                        editor.putString("userid", userID);
                        editor.putString("usertable", userTable);
                        if (userTable.equals("doctors")){
                            editor.putString("experience_status", experience_status);
                        }
                        editor.putString("useremail", userEmail);
                        editor.putString("userphone", userPhone);
                        editor.putString("userfullname", userFullName);
                        editor.putString("myid", my_id);
                        editor.putString("verified_status", verified_status);
                        editor.putString("profile_img", profile_img);
                        editor.commit();

                        //startActivity(home);
                        //finish();

                        if (userTable.equals("doctors")) {
                            if (mClimeeName.length()>2) {
                                if (mFrome.equals("claim")) {
                                    cliamProfileSendingDataService(my_id, mClaimeeID, userFullName, mClimeeName);
                                }
                                else if (mFrome.equals("report")){

                                    final Dialog reportDialog = new Dialog(SignIn.this);
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
                                                reportProfileSendingDataService(userID, mClaimeeID, reportText, userFullName, mClimeeName);

                                            }

                                        }
                                    });


                                    reportDialog.setCancelable(false);
                                    reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                    reportDialog.show();


                                }
                            }
                            else {
                                finish();
                            }

                        }else {


                            if (mClimeeName.length()>2) {
                                if (mFrome.equals("report")){

                                    final Dialog reportDialog = new Dialog(SignIn.this);
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
                                                reportProfileSendingDataService(userID, mClaimeeID, reportText, userFullName, mClimeeName);

                                            }

                                        }
                                    });


                                    reportDialog.setCancelable(false);
                                    reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                    reportDialog.show();


                                }
                                else {
                                    finish();
                                }
                            }
                            else {
                                finish();
                            }

                            //finish();
                        }



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
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();

                params.put("key", Glob.Key);
                params.put("text", textField);
                params.put("password", password);






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

    public void startMobileWithOnlyNumber92()
    {

        doctorSignInEmail.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(SignIn.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    doctorSignInEmail.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorSignInEmail.setText(x);
                        doctorSignInEmail.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    doctorSignInEmail.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
             handleSignInResult(task);
        }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            String id = account.getId();
            String photoUrl = account.getPhotoUrl().toString();

            Log.e(TAG, "the Google sign in result email: " + email);
            Log.e(TAG, "the Google sign in result name: " + name);
            Log.e(TAG, "the Google sign in result id: " + id);
            Log.e(TAG, "the Google sign in result photo url: " + photoUrl);


            SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String profileImage = photoUrl;

            callingSocialLoginServer(id, name, photoUrl, email, "");


            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //  mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            tv_google.setText("Sign Out");
            // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);



        } else {
            // mStatusTextView.setText(R.string.signed_out);


            tv_google.setText("Login With Google");
            //  findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void googleSignIn()
    {
        doctorSignInGmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Log.e("TAG", "text on the button is: " + doctorSignInGmailBtn.getText().toString());

                if (tv_google.getText().toString().equals("Sign Out")){

                    Log.e("TAG", "button presses sign out");
                    signOut();
                }
                if (tv_google.getText().toString().equals("Login With Google")){
                    Log.e("TAG", "button presses sign in");

                    signIn();
                }



            }
        });


    }

    private void callingSocialLoginServer(final String socialId, final String fullname, final String profile_image, final String email, final String gender){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        //  showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.SIGNIN_WITH_SOCIAL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext() , "Login Successfully!", Toast.LENGTH_SHORT).show();

                        String userNname =  jObj.getString("user_name");
                        final String userID = jObj.getString("user_id");
                        String userTable = jObj.getString("user_table");
                        String userEmail = jObj.getString("user_email");
                        String userPhone = jObj.getString("user_phone");
                        final String userFullName = jObj.getString("full_name");
                        String my_id = jObj.getString("id");
                        String verified_status = jObj.getString("verified_status");
                        String profile_img = jObj.getString("profile_img");
                        String experience_status = null;
                        if (userTable.equals("doctors")){
                            experience_status = jObj.getString("experience_status");
                        }


                        Log.e("TAg", "the user name: " + userNname);
                        Log.e("TAg", "the user userID: " + userID);
                        Log.e("TAg", "the user userTable: " + userTable);
                        Log.e("TAg", "the user userPhone: " + userPhone);
                        Log.e("TAg", "the user my_id: " + my_id);
                        Log.e("TAg", "the user varified Status: " + verified_status);
                        Log.e("TAg", "the user profile_image: " + profile_img);


                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.putString("username", userNname);
                        editor.putString("userid", userID);
                        editor.putString("usertable", userTable);
                        if (userTable.equals("doctors")){
                            editor.putString("experience_status", experience_status);
                        }
                        editor.putString("useremail", userEmail);
                        editor.putString("userphone", userPhone);
                        editor.putString("userfullname", userFullName);
                        editor.putString("myid", my_id);
                        editor.putString("verified_status", verified_status);
                        editor.putString("profile_img", profile_img);
                        editor.commit();
                        Intent home = new Intent(SignIn.this, Home.class);
                        //startActivity(home);
                        //  finish();

                        if (userTable.equals("doctors")) {
                            if (mClimeeName.length()>2) {
                                if (mFrome.equals("claim")) {
                                    cliamProfileSendingDataService(my_id, mClaimeeID, userFullName, mClimeeName);
                                }
                                else if (mFrome.equals("report")){

                                    final Dialog reportDialog = new Dialog(SignIn.this);
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
                                                reportProfileSendingDataService(userID, mClaimeeID, reportText, userFullName, mClimeeName);

                                            }

                                        }
                                    });


                                    reportDialog.setCancelable(false);
                                    reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                    reportDialog.show();


                                }
                            }
                            else {
                                finish();
                            }

                        }else {


                            if (mClimeeName.length()>2) {
                                if (mFrome.equals("report")){

                                    final Dialog reportDialog = new Dialog(SignIn.this);
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
                                                reportProfileSendingDataService(userID, mClaimeeID, reportText, userFullName, mClimeeName);

                                            }

                                        }
                                    });


                                    reportDialog.setCancelable(false);
                                    reportDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                                    reportDialog.show();


                                }
                                else {
                                    finish();
                                }
                            }
                            else {
                                finish();
                            }

                            //finish();
                        }


                    } else {


                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if (email.length()>1) {
                            editor.clear();
                            editor.putString("useremail", email);
                            editor.putString("userfullname", fullname);
                            editor.putString("profile_img", profile_image);
                            editor.putString("gender", gender);
                            editor.putString("socialid", socialId);
                            editor.commit();

                        }else {

                            editor.clear();
                            editor.putString("userfullname", fullname);
                            editor.putString("profile_img", profile_image);
                            editor.putString("gender", gender);
                            editor.putString("socialid", socialId);
                            editor.commit();
                        }

                        Intent home = new Intent(SignIn.this, SelectSignUpOptions.class);
                        home.putExtra("claimee_id", mClaimeeID);
                        home.putExtra("claimee_name", mClimeeName);
                        home.putExtra("from", mFrome);
                        startActivity(home);
                        finish();

                        String errorMsg = jObj.getString("error_message");
                        // Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

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
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();

                params.put("key", Glob.Key);
                params.put("id", socialId);







                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of seocial login serveri


    //claim profile API service
    private void cliamProfileSendingDataService(final String myid, final String climId, final String doctorName, final String calimeeName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        mClaimDialog = new CustomProgressDialog(SignIn.this, 1);
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(SignIn.this);
                            alert.setTitle("Profiel Claim in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + doctorName + " Your Claim to "+ calimeeName + " Submitted Succesfully, We Will Notify You Soon");
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
                        Toast.makeText(SignIn.this, errorMsg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(SignIn.this, "Server Connection Failed", Toast.LENGTH_SHORT).show();
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

        AppSingleton.getInstance(SignIn.this).addToRequestQueue(strReq, cancel_req_tag);
    }


    //claim report user api API service
    private void reportProfileSendingDataService(final String reporterID, final String toReportID, final String reportText, final String reporterName, final String toReportName){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        dialog = new CustomProgressDialog(SignIn.this, 1);
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(SignIn.this);
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
                        Toast.makeText(SignIn.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(SignIn.this, "Server Connection Failed", Toast.LENGTH_SHORT).show();
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

        AppSingleton.getInstance(SignIn.this).addToRequestQueue(strReq, cancel_req_tag);
    }

}

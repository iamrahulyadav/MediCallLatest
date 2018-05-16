package themedicall.com;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

public class SignupAsUser extends AppCompatActivity implements  SearchView.OnQueryTextListener{
    EditText signUpUserName  , signUpFullName ,  signUpMobile, signUpDob , signUpEmail, signUpPass ,  signUpConfirmPass , signUpUniName , signUpUniRegistrationNo;
    RadioGroup signUpGenderRadioGroup;
    RadioButton signUpGenderMale , signUpGenderFeMale ;
    Button signUpSelectStatus , signUpSelectCity  , signUpBtn;
    Spinner btSignUpSelectDesignation , btSignUpSelectBloodGroup;
    RelativeLayout signUpSelectDesignationLayout , signUpSelectSpecialityLayout  , signUpSelectSubSpecialityLayout , signUpSelectCityLayout, signUpSelectBloodGroupLayout ;
    Calendar myCalendar = Calendar.getInstance();
    ArrayAdapter<String> cityAdapter;
    LinearLayout signUpGenderLayout;
    SearchView search_view ;


    private String  userChoosenTask;

    String[] city;
    int signUpPosition;
    private String bloodgroupId;
    private String experienceStatusId;
    String City_id;
    String[] doctorStatus = { "Status" ,"Specialist" , "Trainee Specialist" , "General Practitioner" , "Student Doctor"};
    String[] bloodGroupArray = { "Blood Group" ,"A+" , "A-" , "B+" , "B-" , "AB+" , "AB-" , "O+" , "O-"};
    ArrayAdapter<String> doctorStatusAdapter , bloodGroupAdapter;

    String signUpUserNameText , signUpFullNameText , signUpMobileText, signUpDobText , signUpEmailText , signUpPassText , signUpConfirmPassText , signUpSelectedRadioText,
            signUpSelectStatusText , signUpSelectSpecialityText  , signUpSelectSubSpecialityText , signUpSelectCityText , signUpSelectAreaText , signUpRadioButtonText,
            signUpSelectBloodGroupText, signUpSelectDoctorStatusText , signUpUniNameText , signUpUniRegistrationNoText;
    int signUpGenderRadioGroupInt ;
    RadioButton genderRadioButton;
    private static final String TAG = "RegisterActivity";
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferencesBloodDonor, sharedPreferencesDoctor, sharedPreferencesPatient, sharedPreferencesHospital, sharedPreferencesLab, sharedPreferencesPharmacy, sharedPreferencesHealthProfessional, sharedPreferencesOther;
    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<CitiesGetterSetter> CityList = new ArrayList<CitiesGetterSetter>();
    String cityName ;
    String cityId;
    String Sign_Up_URL;
    CustomCityNewAdapter customCityNewAdapter;
    CustomProgressDialog dialog;


    private static final String userNameError=  "Username must be start With Alphabet Or Underscore";
    private static final String fullNameError=  "Please Enter Full Name";
    private static final String mobileNoError=  "Please Enter Complete Mobile Number";
    private static final String dobError=  "Please Enter Date of Birth";
    private static final String selectStatusError=  "Please Select Doctor Status";
    private static final String selectCityError=  "Please Select City";
    private static final String selectBloodgropError = "Please Select Bloodgroup";
    private static final String emailError=  "Please Enter Valid Email Address";
    private static final String passwordError=  "Please Enter Password At Least 6 Character";
    private static final String passwordConfirmError=  "Password not Match";
    private static final String uniNameError=  "Please Enter University Name";
    private static final String uniRegistrationNoError=  "Please Enter University Registration #";

    String mUserSocialId = "";

    String claimee_id = "";
    String mClaimee_name = "";
    String mFrome = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_as_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();
        setDoctorDob();
        setSelectDocStatus();
        setSelectCity();
        selectingBloodGroup();
        signUpPositionHideShowFields();

        startMobileWithOnlyNumber3();

        signUpUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!userNameValidator(signUpUserName
                            .getText().toString()) || signUpUserName.length() <= 3)
                    {
                        signUpUserName.setError(userNameError);
                    }
                    else
                    {
                        checkUserNameService();
                    }
                }
            }
        });

    }
    public void initiate()
    {

        signUpPosition= getIntent().getIntExtra("item_position",0);
        String claimeeId = getIntent().getStringExtra("claimee_id");
        Log.e("TAg", "the claimee id here is: " + claimeeId);
        if (claimeeId!=null){
            claimee_id = claimeeId;
            mClaimee_name = getIntent().getStringExtra("claimee_name");
            mFrome =  getIntent().getStringExtra("from");

            Log.e("TAG", "the frome text is: " + mFrome);
        }

        //Toast.makeText(this, "item Position "+signUpPosition , Toast.LENGTH_SHORT).show();

        dialog=new CustomProgressDialog(SignupAsUser.this, 1);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        signUpUserName = (EditText) findViewById(R.id.signUpUserName);
        signUpFullName = (EditText) findViewById(R.id.signUpName);
        signUpMobile = (EditText) findViewById(R.id.signUpMobile);
        signUpDob = (EditText) findViewById(R.id.signUpDob);
        signUpEmail = (EditText) findViewById(R.id.signUpEmail);
        signUpPass = (EditText) findViewById(R.id.signUpPass);
        signUpConfirmPass = (EditText) findViewById(R.id.signUpConfirmPass);
        signUpUniName = (EditText) findViewById(R.id.signUpUniName);
        signUpUniRegistrationNo = (EditText) findViewById(R.id.signUpUniRegistrationNo);



        signUpGenderRadioGroup = (RadioGroup) findViewById(R.id.signUpGenderRadioGroup);
        signUpGenderMale = (RadioButton) findViewById(R.id.signUpGenderMale);
        signUpGenderFeMale = (RadioButton) findViewById(R.id.signUpGenderFeMale);
        signUpSelectStatus = (Button) findViewById(R.id.signUpSelectDesignation);
        signUpSelectCity = (Button) findViewById(R.id.signUpSelectCity);
        btSignUpSelectBloodGroup = (Spinner) findViewById(R.id.btSignUpSelectBloodGroup);
        btSignUpSelectDesignation = (Spinner) findViewById(R.id.btSignUpSelectDesignation);
        signUpSelectDesignationLayout = (RelativeLayout) findViewById(R.id.signUpSelectDesignationLayout);
        signUpSelectSpecialityLayout = (RelativeLayout) findViewById(R.id.signUpSelectSpecialityLayout);
        signUpSelectSubSpecialityLayout = (RelativeLayout) findViewById(R.id.signUpSelectSubSpecialityLayout);
        signUpSelectCityLayout = (RelativeLayout) findViewById(R.id.signUpSelectCityLayout);
        signUpSelectBloodGroupLayout = (RelativeLayout) findViewById(R.id.signUpSelectBloodGroupLayout);

        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        city = getResources().getStringArray(R.array.city);

        signUpGenderLayout = (LinearLayout) findViewById(R.id.signUpGenderLayout);
        signUpGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.signUpGenderMale){

                }
                else {

                }
            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null) {

            String fullName="", usermeail="", userGender="";
            String profile_iamge = sharedPreferences.getString("profile_img", null);
            if (profile_iamge != null) {
                if (profile_iamge.contains("facebook")) {
                    fullName = sharedPreferences.getString("userfullname", null);
                    usermeail = sharedPreferences.getString("useremail", null);
                    userGender = sharedPreferences.getString("gender", null);
                    mUserSocialId = sharedPreferences.getString("socialid", "");
                }
                else if (profile_iamge.contains("google")){
                    fullName = sharedPreferences.getString("userfullname", null);
                    usermeail = sharedPreferences.getString("useremail", null);
                    mUserSocialId = sharedPreferences.getString("socialid", "");
                }


                signUpFullName.setText(fullName);
                if (usermeail!=null){
                    signUpEmail.setText(usermeail);
                }
                if (userGender.equals("male")){
                    signUpGenderRadioGroup.check(R.id.signUpGenderMale);
                }else if (userGender.equals("female")){
                    signUpGenderRadioGroup.check(R.id.signUpGenderFeMale);
                }
            }
        }

    }//end of initialization method

    public void getText()
    {

        signUpUserNameText = signUpUserName.getText().toString();
        signUpFullNameText = signUpFullName.getText().toString();
        signUpMobileText = signUpMobile.getText().toString();
        signUpDobText = signUpDob.getText().toString();
        signUpEmailText = signUpEmail.getText().toString();
        signUpPassText = signUpPass.getText().toString();
        signUpSelectStatusText = signUpSelectStatus.getText().toString();
        signUpSelectCityText = signUpSelectCity.getText().toString();
        signUpSelectBloodGroupText = btSignUpSelectBloodGroup.getSelectedItem().toString();
        signUpGenderRadioGroupInt = signUpGenderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = (RadioButton) findViewById(signUpGenderRadioGroupInt);
        signUpRadioButtonText = genderRadioButton.getText().toString();

    }


    public void setDoctorDob()
    {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //preventing user to select future date
                view.setMaxDate(System.currentTimeMillis());

                updateLabel();
            }
        };

        signUpDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog =  new DatePickerDialog(SignupAsUser.this,R.style.CustomDatePickerDialogTheme,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000-1);

                datePickerDialog.show();
            }


        });


    }


    public void setSelectDocStatus()
    {


        doctorStatusAdapter = new ArrayAdapter<String>(SignupAsUser.this , R.layout.spinner_list , R.id.spinnerList , doctorStatus);
        btSignUpSelectDesignation.setAdapter(doctorStatusAdapter);

    }






    public void setSelectCity()
    {

        signUpSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SignupAsUser.this);
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Select City");
                search_view = (SearchView) dialog.findViewById(R.id.search_view);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                ListView cityListView = (ListView) dialog.findViewById(R.id.cityList);

                dialog.show();


                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                List cityList = databaseHelper.getAllPots();
                Log.e("TAG", "the city list count: " + databaseHelper.getCount());
                Log.e("TAG", "the city list from db: " + cityList.size());
                customCityNewAdapter = new CustomCityNewAdapter(getApplicationContext(), cityList);
                //customCityNewAdapter = new CustomCityNewAdapter(getApplicationContext(), GetAllCitiesListService.CityList);
                cityListView.setAdapter(customCityNewAdapter);
                search_view.setOnQueryTextListener(SignupAsUser.this);


                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();

                        City_id = city_id.getText().toString();
                        // Toast.makeText(SignupAsUser.this, "id "+City_id, Toast.LENGTH_SHORT).show();
                        signUpSelectCity.setText(City);
                        Log.e("TAG", "selected city name: " + City);
                        Log.e("TAG", "selected city id: " + City_id);
                        dialog.dismiss();

                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }//end of city selection

    @Override
    public boolean onQueryTextSubmit(String s) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        customCityNewAdapter.getFilter().filter(s);
        return false;
    }



    //creating list Dialog for blood groups

    public void selectingBloodGroup(){


        bloodGroupAdapter = new ArrayAdapter<String>(SignupAsUser.this , R.layout.spinner_list , R.id.spinnerList , bloodGroupArray);
        btSignUpSelectBloodGroup.setAdapter(bloodGroupAdapter);

    }



    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }


    public static boolean userNameValidator(final String userName) {

        Pattern pattern;
        Matcher matcher;

        final String USER_NAME_PATTERN = "^[_A-Za-z]+[0-9]*$";

        pattern = Pattern.compile(USER_NAME_PATTERN);
        matcher = pattern.matcher(userName);
        return matcher.matches();
    }

    public static boolean mobileNumberValidator(final String mobileNumber) {

        Pattern pattern;
        Matcher matcher;

        final String MOBILE_NUMBER_PATTERN = "^3[0-9]*$";

        pattern = Pattern.compile(MOBILE_NUMBER_PATTERN);
        matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        signUpDob.setText(sdf.format(myCalendar.getTime()));
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



    public void signUpPositionHideShowFields() {
        if(signUpPosition == 1)
        {
            getSupportActionBar().setTitle("Sign Up Non Medic");

            signUpSelectDesignationLayout.setVisibility(View.GONE);
            signUpSelectBloodGroupLayout.setVisibility(View.VISIBLE);
            signUpSelectCity.setHint("City of practice");
            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!userNameValidator(signUpUserName.getText().toString()))
                    {
                        signUpUserName.setError(userNameError);
                    }
                    else if(signUpFullName.getText().toString().equals(""))
                    {
                        signUpFullName.setError(fullNameError);
                    }
                    else if(!mobileNumberValidator(signUpMobile.getText().toString()) || signUpMobile.length() > 10)
                    {
                        signUpMobile.setError(mobileNoError);
                    }

                    else if(signUpSelectCity.getText().toString().equals("City"))
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select City", Toast.LENGTH_SHORT).show();
                        // signUpSelectCity.setError(selectCityError);
                    }
                    else if(btSignUpSelectBloodGroup.getSelectedItem().toString().equals("Blood Group"))
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select Bloodgroup", Toast.LENGTH_SHORT).show();
                        // btSignUpSelectBloodGroup.setError(selectBloodgropError);
                    }
                    else if(signUpGenderRadioGroup.getCheckedRadioButtonId() == -1)
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        signUpDob.setError(null);
                    }
                    else if(!emailValidator(signUpEmail.getText().toString()))
                    {
                        signUpEmail.setError(emailError);
                    }
                    else if(signUpPass.getText().toString().equals("") || signUpPass.length() <= 5)
                    {
                        signUpPass.setError(passwordError);
                    }
                    else if(!signUpConfirmPass.getText().toString().equals(signUpPass.getText().toString()))
                    {
                        signUpConfirmPass.setError(passwordConfirmError);
                    }
                    else {

                        signUpUserNameText =   signUpUserName.getText().toString();
                        signUpFullNameText = signUpFullName.getText().toString();
                        signUpMobileText = signUpMobile.getText().toString();
                        signUpEmailText = signUpEmail.getText().toString();
                        signUpPassText = signUpPass.getText().toString();
                        signUpConfirmPassText = signUpConfirmPass.getText().toString();
                        signUpSelectCityText = signUpSelectCity.getText().toString();
                        signUpSelectBloodGroupText = btSignUpSelectBloodGroup.getSelectedItem().toString();
                        int selectedId = signUpGenderRadioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        signUpSelectedRadioText = radioButton.getText().toString();



                        if (signUpSelectBloodGroupText.equals("A+")){bloodgroupId = "1";}
                        if (signUpSelectBloodGroupText.equals("A-")){bloodgroupId = "2";}
                        if (signUpSelectBloodGroupText.equals("B+")){bloodgroupId = "3";}
                        if (signUpSelectBloodGroupText.equals("B-")){bloodgroupId = "4";}
                        if (signUpSelectBloodGroupText.equals("O+")){bloodgroupId = "5";}
                        if (signUpSelectBloodGroupText.equals("O-")){bloodgroupId = "6";}
                        if (signUpSelectBloodGroupText.equals("AB+")){bloodgroupId = "7";}
                        if (signUpSelectBloodGroupText.equals("AB-")){bloodgroupId = "8";}


                        Sign_Up_URL = Glob.SIGNUP_PATIENT;
                        Log.i("TAG", "Current URL IS: " + Sign_Up_URL );

                        signUpRegistration();

                    }
                }
            });
        }

        else if(signUpPosition == 6)
        {
            getSupportActionBar().setTitle("Sign Up As Blood Donor");

            signUpSelectBloodGroupLayout.setVisibility(View.VISIBLE);

            //setting button clickListner for signup
            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if(!userNameValidator(signUpUserName.getText().toString()))
                    {
                        signUpUserName.setError(userNameError);
                    }
                    else if(signUpFullName.getText().toString().equals(""))
                    {
                        signUpFullName.setError(fullNameError);
                    }
                    else if(!mobileNumberValidator(signUpMobile.getText().toString()) || signUpMobile.length() > 10)
                    {
                        signUpMobile.setError(mobileNoError);
                    }
                    else if(signUpSelectCity.getText().toString().equals("City"))
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select City", Toast.LENGTH_SHORT).show();
                        // signUpSelectCity.setError(selectCityError);
                    }
                    else if(btSignUpSelectBloodGroup.getSelectedItem().toString().equals("Blood Group"))
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select Bloodgroup", Toast.LENGTH_SHORT).show();
                        // btSignUpSelectBloodGroup.setError(selectBloodgropError);
                    }
                    else if(signUpGenderRadioGroup.getCheckedRadioButtonId() == -1)
                    {
                        Toast.makeText(SignupAsUser.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                        signUpDob.setError(null);
                    }

                    else if(!emailValidator(signUpEmail.getText().toString()))
                    {
                        signUpEmail.setError(emailError);
                    }
                    else if(signUpPass.getText().toString().equals("") || signUpPass.length() <= 5)
                    {
                        signUpPass.setError(passwordError);
                    }
                    else if(!signUpConfirmPass.getText().toString().equals(signUpPass.getText().toString()))
                    {
                        signUpConfirmPass.setError(passwordConfirmError);
                    }
                    else {

                        signUpUserNameText =   signUpUserName.getText().toString();
                        signUpFullNameText = signUpFullName.getText().toString();
                        signUpMobileText = signUpMobile.getText().toString();
                        signUpDobText = signUpDob.getText().toString();
                        signUpEmailText = signUpEmail.getText().toString();
                        signUpPassText = signUpPass.getText().toString();
                        signUpConfirmPassText = signUpConfirmPass.getText().toString();
                        signUpSelectCityText = signUpSelectCity.getText().toString();
                        signUpSelectBloodGroupText = btSignUpSelectBloodGroup.getSelectedItem().toString();
                        int selectedId = signUpGenderRadioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) findViewById(selectedId);
                        signUpSelectedRadioText = radioButton.getText().toString();

                        if (signUpSelectBloodGroupText.equals("A+")){bloodgroupId = "1";}
                        if (signUpSelectBloodGroupText.equals("A-")){bloodgroupId = "2";}
                        if (signUpSelectBloodGroupText.equals("B+")){bloodgroupId = "3";}
                        if (signUpSelectBloodGroupText.equals("B-")){bloodgroupId = "4";}
                        if (signUpSelectBloodGroupText.equals("O+")){bloodgroupId = "5";}
                        if (signUpSelectBloodGroupText.equals("O-")){bloodgroupId = "6";}
                        if (signUpSelectBloodGroupText.equals("AB+")){bloodgroupId = "7";}
                        if (signUpSelectBloodGroupText.equals("AB-")){bloodgroupId = "8";}

                        Sign_Up_URL = Glob.SIGNUP_BLOOD_DONOR;
                        Log.i("TAG", "Current URL IS: " + Sign_Up_URL );
                        signUpRegistration();

                    }

                }
            });

            signUpSelectDesignationLayout.setVisibility(View.GONE);
        }

    }

    public void signUpRegistration()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please Wait ...");
        //showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Sign_Up_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(getApplicationContext() , "Successfully Registered!", Toast.LENGTH_SHORT).show();
                        String userID =  jObj.getString("user_id");
                        String CODE = jObj.getString("code");



                        Log.e("TAG", "USER RESPONSE RECORD: " + CODE);

                        if (signUpPosition == 1){

                            sharedPreferencesPatient = getSharedPreferences("rpatient", 0);
                            SharedPreferences.Editor editorPatient = sharedPreferencesPatient.edit();
                            editorPatient.putString("userid", userID);
                            editorPatient.putString("code", CODE);
                            editorPatient.putString("username", signUpUserNameText);
                            editorPatient.putString("fullname", signUpFullNameText);
                            editorPatient.putString("mobile", "92"+signUpMobileText);
                            editorPatient.putString("dob", signUpDobText);
                            editorPatient.putString("gender", signUpSelectedRadioText);
                            editorPatient.putString("email", signUpEmailText);
                            editorPatient.putString("password", signUpPassText);
                            editorPatient.putString("city", signUpSelectCityText);
                            editorPatient.putString("bloodgroup", signUpSelectBloodGroupText);
                            editorPatient.putInt("signupposition", signUpPosition);
                            editorPatient.commit();

                            // Launch login activity
                            Intent intent = new Intent(SignupAsUser.this, PinVerification.class);
                            intent.putExtra("user_id", userID);
                            intent.putExtra("code", CODE);
                            intent.putExtra("signupposition", signUpPosition);
                            intent.putExtra("claimee_id", claimee_id);
                            intent.putExtra("claimee_name", mClaimee_name);
                            intent.putExtra("from", mFrome);
                            startActivity(intent);
                            finish();

                        }


                        if (signUpPosition == 6) {

                            sharedPreferencesBloodDonor = getSharedPreferences("rbloodonor", 0);
                            SharedPreferences.Editor editorBloodDonor = sharedPreferencesBloodDonor.edit();
                            editorBloodDonor.putString("userid", userID);
                            editorBloodDonor.putString("code", CODE);
                            editorBloodDonor.putString("username", signUpUserNameText);
                            editorBloodDonor.putString("fullname", signUpFullNameText);
                            editorBloodDonor.putString("mobile", "92"+signUpMobileText);
                            editorBloodDonor.putString("dob", signUpDobText);
                            editorBloodDonor.putString("gender", signUpSelectedRadioText);
                            editorBloodDonor.putString("email", signUpEmailText);
                            editorBloodDonor.putString("password", signUpPassText);
                            editorBloodDonor.putString("city", signUpSelectCityText);
                            editorBloodDonor.putString("bloodgroup", signUpSelectBloodGroupText);
                            editorBloodDonor.putInt("signupposition", signUpPosition);
                            editorBloodDonor.commit();

                            // Launch login activity
                            Intent intent = new Intent(SignupAsUser.this, PinVerification.class);
                            intent.putExtra("user_id", userID);
                            intent.putExtra("code", CODE);
                            intent.putExtra("signupposition", signUpPosition);
                            intent.putExtra("claimee_id", claimee_id);
                            intent.putExtra("claimee_name", mClaimee_name);
                            intent.putExtra("from", mFrome);
                            startActivity(intent);
                            finish();

                        }
                    }
                    else {

                        String errorMsg = jObj.getString("error_message");
                        final String userStaus = jObj.getString("user_status");
                        final String userID = jObj.getString("user_id");

                        Log.e("TAg", "user_status: " + userStaus);
                        Log.e("TAg", "user_status id: " + userID);

                        if (userStaus.equals("0")){

                            AlertDialog.Builder alert = new AlertDialog.Builder(SignupAsUser.this);
                            alert.setTitle("User Already Exist!");
                            alert.setMessage("Mobile Number/Email already exist \nPlease Verify its you by receiving \nVerification Code on Registered Number");
                            alert.setPositiveButton("Send Code", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(SignupAsUser.this, PinVerification.class);
                                    intent.putExtra("user_id", userID);
                                    intent.putExtra("code", "00");//00 for taging for verifying already register user
                                    intent.putExtra("signupposition", signUpPosition);
                                    intent.putExtra("claimee_id", claimee_id);
                                    intent.putExtra("claimee_name", mClaimee_name);
                                    intent.putExtra("from", mFrome);
                                    startActivity(intent);
                                    finish();
                                    dialogInterface.dismiss();
                                }
                            });
                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alert.show();
                        }

                        Log.e("TAg", "user_status: " + userStaus);
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
                dialog.show();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();



                if(signUpPosition == 1)
                {
                    params.put("key", Glob.Key);
                    params.put("username", signUpUserNameText);
                    params.put("fullname", signUpFullNameText);
                    params.put("mobilenumber", "92"+signUpMobileText);
                   // params.put("dob", signUpDobText);
                    params.put("city", City_id);
                    params.put("gender", signUpSelectedRadioText);
                    params.put("email", signUpEmailText);
                    params.put("password", signUpPassText);
                    params.put("bloodgroup", bloodgroupId);
                    params.put("social_id", mUserSocialId);

                }
                else if(signUpPosition == 6)
                {

                    params.put("key", Glob.Key);
                    params.put("username", signUpUserNameText);
                    params.put("fullname", signUpFullNameText);
                    params.put("mobilenumber", "92"+signUpMobileText);
                    //params.put("dob", "");
                    params.put("city", City_id);
                    params.put("gender", signUpSelectedRadioText);
                    params.put("email", signUpEmailText);
                    params.put("password", signUpPassText);
                    params.put("bloodgroup", bloodgroupId);
                    params.put("social_id", mUserSocialId);

                }

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

    public void checkUserNameService()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        //showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.URL+"is-username-unique", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        signUpUserName.setError(errorMsg);
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



                signUpUserNameText = signUpUserName.getText().toString();


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("user_name", signUpUserNameText);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void startMobileWithOnlyNumber3()
    {

        signUpMobile.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0"))
                {

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }

                if (x.startsWith("1")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(SignupAsUser.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
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
    public void onBackPressed() {
        Intent selectSignIn = new Intent(SignupAsUser.this, SelectSignUpOptions.class);
        if (mFrome.length()>2){

            finish();
            super.onBackPressed();

        }else {

            selectSignIn.putExtra("claimee_id", "");
            selectSignIn.putExtra("claimee_name", "");
            selectSignIn.putExtra("from", "");
            startActivity(selectSignIn);
            finish();
            super.onBackPressed();

        }
    }
}

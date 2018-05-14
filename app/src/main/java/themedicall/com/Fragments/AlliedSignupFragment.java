package themedicall.com.Fragments;

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
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import themedicall.com.Adapter.CitiesListCustomAdapter;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;
import themedicall.com.PinVerification;
import themedicall.com.R;
import themedicall.com.SelectSignUpOptions;
import themedicall.com.SignUpForAliedHealth;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Shoaib Anwar on 12-Apr-18.
 */

public class AlliedSignupFragment extends Fragment implements  SearchView.OnQueryTextListener{


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
    LinearLayout ll_pmdc_photo , pmdc_select_picture_layout;
    FrameLayout pmdcImageFrameLayout ;
    ImageView iv_pmdc , pmdcImageFromCamera , pmdcImageFromGallery , removePmdcDescriptionImg;
    EditText et_pmdc_number;
    String pmdcNumber;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Uri imageUri = null;
    Bitmap bitmap1;


    int signUpPosition;

    private String  userChoosenTask;

    String[] city;
    private String experienceStatusId;
    String City_id;
    List<String> doctorStatus;
    ArrayAdapter<String> doctorStatusAdapter;

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

    List<CitiesGetterSetter> CGSForAliedHealthCategories;
    String selectedStatusId;


    String mUserSocialId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =inflater.inflate(R.layout.activity_sign_up_for_alied_health, container, false);

        initiate(v);
        //getText();
        setDoctorDob();

        setSelectCity();
        signUpPositionHideShowFields(v);
        //getCitiesService();

        pmdfPhotoSelection();

        startMobileWithOnlyNumber3();

        //getting alied health categoreis
        aliedHealthCategoriesCallingFromServer();


        //SignUpBtnClick();


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


        return v;
    }

    public void initiate(View view)
    {


        signUpPosition = getActivity().getIntent().getIntExtra("item_position",-1);




        dialog=new CustomProgressDialog(getActivity(), 1);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        signUpUserName = (EditText) view.findViewById(R.id.signUpUserName);
        signUpFullName = (EditText) view.findViewById(R.id.signUpName);
        signUpMobile = (EditText) view.findViewById(R.id.signUpMobile);
        signUpDob = (EditText) view.findViewById(R.id.signUpDob);
        signUpEmail = (EditText) view.findViewById(R.id.signUpEmail);
        signUpPass = (EditText) view.findViewById(R.id.signUpPass);
        signUpConfirmPass = (EditText) view.findViewById(R.id.signUpConfirmPass);
        signUpUniName = (EditText) view.findViewById(R.id.signUpUniName);
        signUpUniRegistrationNo = (EditText) view.findViewById(R.id.signUpUniRegistrationNo);



        signUpGenderRadioGroup = (RadioGroup) view.findViewById(R.id.signUpGenderRadioGroup);
        signUpGenderMale = (RadioButton) view.findViewById(R.id.signUpGenderMale);
        signUpGenderFeMale = (RadioButton) view.findViewById(R.id.signUpGenderFeMale);
        signUpSelectStatus = (Button) view.findViewById(R.id.signUpSelectDesignation);
        signUpSelectCity = (Button) view.findViewById(R.id.signUpSelectCity);
        btSignUpSelectBloodGroup = (Spinner) view.findViewById(R.id.btSignUpSelectBloodGroup);
        btSignUpSelectDesignation = (Spinner) view.findViewById(R.id.btSignUpSelectDesignation);
        signUpSelectDesignationLayout = (RelativeLayout) view.findViewById(R.id.signUpSelectDesignationLayout);
        signUpSelectSpecialityLayout = (RelativeLayout) view.findViewById(R.id.signUpSelectSpecialityLayout);
        signUpSelectSubSpecialityLayout = (RelativeLayout) view.findViewById(R.id.signUpSelectSubSpecialityLayout);
        signUpSelectCityLayout = (RelativeLayout) view.findViewById(R.id.signUpSelectCityLayout);
        signUpSelectBloodGroupLayout = (RelativeLayout) view.findViewById(R.id.signUpSelectBloodGroupLayout);

        pmdcImageFrameLayout = (FrameLayout) view.findViewById(R.id.pmdcImageFrameLayout);
        ll_pmdc_photo = (LinearLayout) view.findViewById(R.id.ll_pmdc_photo);
        pmdc_select_picture_layout = (LinearLayout) view.findViewById(R.id.pmdc_select_picture_layout);
        iv_pmdc = (ImageView) view.findViewById(R.id.iv_pmdc);
        pmdcImageFromCamera = (ImageView) view.findViewById(R.id.pmdcImageFromCamera);
        pmdcImageFromGallery = (ImageView) view.findViewById(R.id.pmdcImageFromGallery);
        removePmdcDescriptionImg = (ImageView) view.findViewById(R.id.removePmdcDescriptionImg);
        et_pmdc_number = (EditText) view.findViewById(R.id.et_pmdc_number);

        signUpBtn = (Button) view.findViewById(R.id.signUpBtn);
        city = getResources().getStringArray(R.array.city);

        signUpGenderLayout = (LinearLayout) view.findViewById(R.id.signUpGenderLayout);
        signUpGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.signUpGenderMale){

                }
                else {

                }
            }
        });


        doctorStatus = Arrays.asList(getResources().getStringArray(R.array.alied_health_status));


        CGSForAliedHealthCategories = new ArrayList<>();


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
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
                DatePickerDialog datePickerDialog =    new DatePickerDialog(getActivity(),R.style.CustomDatePickerDialogTheme,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000-1);

                datePickerDialog.show();
            }


        });


    }


    public void setSelectDocStatus() {

        btSignUpSelectDesignation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvSelectedID = (TextView) view.findViewById(R.id.city_id);
                TextView tvSelectedTitle = (TextView) view.findViewById(R.id.city_title);
                String id = tvSelectedID.getText().toString();
                String title = tvSelectedTitle.getText().toString();

                Log.e("TAG", "The selected item is: " + id);
                Log.e("TAG", "The selected item title: " + title);
                int paddingDp = 0;
                float density = getApplicationContext().getResources().getDisplayMetrics().density;
                int paddingPixel = (int)(paddingDp * density);
                tvSelectedTitle.setPadding(0,paddingPixel,0,0);
                tvSelectedTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hintColor));

                selectedStatusId = id;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void setSelectCity()
    {
        signUpSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
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
                // search_view.setOnQueryTextListener(getActivity());


                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();

                        City_id = city_id.getText().toString();
                        // Toast.makeText(SignUp.this, "id "+City_id, Toast.LENGTH_SHORT).show();
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
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        signUpDob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void signUpPositionHideShowFields(final View view)
    {


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
                else if(signUpMobile.length()<10)
                {
                    signUpMobile.setError(mobileNoError);
                }
                else if(signUpDob.getText().toString().equals(""))
                {
                    signUpDob.setError(dobError);
                }
                else if(signUpSelectCity.getText().toString().equals("City"))
                {
                    Toast.makeText(getActivity(), "Please Select City", Toast.LENGTH_SHORT).show();
                    // signUpSelectCity.setError(selectCityError);
                }
                else if(signUpGenderRadioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();

                }
                else if (selectedStatusId.equals("0")){
                    Toast.makeText(getActivity(), "Please Select your Status", Toast.LENGTH_SHORT).show();
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

                else  if (et_pmdc_number.getText().length() == 0){
                    et_pmdc_number.setError("Please Enter PMDC No");
                }


                    /*else if (imageUri==null){
                        Toast.makeText(SignUp.this, "Please Upload PMDC Photo", Toast.LENGTH_SHORT).show();
                    }*/

                else {

                    signUpUserNameText =   signUpUserName.getText().toString();
                    signUpFullNameText = signUpFullName.getText().toString();
                    signUpMobileText = signUpMobile.getText().toString();
                    Log.e("TAG", "Mobile Number: " + signUpMobileText );
                    signUpDobText = signUpDob.getText().toString();
                    signUpEmailText = signUpEmail.getText().toString();
                    signUpPassText = signUpPass.getText().toString();
                    signUpConfirmPassText = signUpConfirmPass.getText().toString();
                    signUpSelectCityText = signUpSelectCity.getText().toString();
                    int selectedId = signUpGenderRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
                    signUpSelectedRadioText = radioButton.getText().toString();
                    signUpUniNameText = signUpUniName.getText().toString();
                    signUpUniRegistrationNoText = signUpUniRegistrationNo.getText().toString();
                    pmdcNumber = et_pmdc_number.getText().toString();


                    Log.e("TAG", "Image URI : " + imageUri );

                    if(imageUri!=null) {
                        File getealPathOfImageUri = new File(getRealPathFromURI(imageUri));
                        String imgePath = getealPathOfImageUri.toString();
                        Log.e("TAG", "Image Path : " + imgePath);
                    }

                    Log.e("TAG", "the sign up button click Username: " + signUpUserNameText);
                    Log.e("TAG", "the sign up button click full name: " + signUpFullNameText);
                    Log.e("TAG", "the sign up button click mobile number: " + signUpMobileText);
                    Log.e("TAG", "the sign up button click dob: " + signUpDobText);
                    Log.e("TAG", "the sign up button click email: " + signUpEmailText);
                    Log.e("TAG", "the sign up button click password: " + signUpPassText);
                    Log.e("TAG", "the sign up button click city: " + signUpSelectCityText);
                    Log.e("TAG", "the sign up button click  pmdc: " + pmdcNumber);
                    Log.e("TAG", "the sign up button click  Gender: " + signUpSelectedRadioText);
                    Log.e("TAG", "the sign up button click  Status ID: " + selectedStatusId);


                    signUpRegistration(signUpUserNameText, signUpFullNameText, signUpMobileText, signUpDobText, City_id, signUpSelectedRadioText, signUpEmailText,
                            signUpPassText, selectedStatusId, pmdcNumber, signUpUniNameText);

                }

            }
        });


    }

    public void signUpRegistration(final String signUpUserNameText, final String signUpFullNameText, final String signUpMobileText, final String signUpDobText, final String city_id, final String signUpSelectedRadioText, final String signUpEmailText, final String signUpPassText, final String experienceStatusId, final String pmdcNumber, final String signUpUniNameText)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please Wait ...");
        //showDialog();
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.ALLIED_HEALTH_SIGNUP , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "alied health registration response: " + response.toString());
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


                        Log.e("TAG", "Image URI : " + imageUri );

                        if (imageUri!=null) {
                            File getealPathOfImageUri = new File(getRealPathFromURI(imageUri));
                            String imgePath = getealPathOfImageUri.toString();
                            Log.e("TAG", "Image Path : " + imgePath);
                            //calling image uplaod url
                            //signUpdoctorwithImage(imgePath, drID);
                        }

                           /* sharedPreferencesDoctor = getSharedPreferences("rdoctor", 0);
                            SharedPreferences.Editor editorDoctor = sharedPreferencesDoctor.edit();
                            editorDoctor.putString("userid", userID);
                            editorDoctor.putString("code", CODE);
                            editorDoctor.putString("username", getActivity().signUpUserNameText);
                            editorDoctor.putString("fullname", getActivity().signUpFullNameText);
                            editorDoctor.putString("mobile", "92"+ getActivity().signUpMobileText);
                            editorDoctor.putString("dob", getActivity().signUpDobText);
                            editorDoctor.putString("gender", getActivity().signUpSelectedRadioText);
                            editorDoctor.putString("email", getActivity().signUpEmailText);
                            editorDoctor.putString("password", getActivity().signUpPassText);
                            editorDoctor.putString("city", signUpSelectCityText);
                            editorDoctor.putString("bloodgroup", signUpSelectBloodGroupText);
                            editorDoctor.putString("doctorstatus", signUpSelectDoctorStatusText);

                            editorDoctor.commit();

                            // Launch login activity
                            Intent intent = new Intent(getActivity(), PinVerification.class);
                            intent.putExtra("user_id", userID);
                            intent.putExtra("code", CODE);
                            //intent.putExtra("signupposition", signUpPosition);
                            startActivity(intent);
                            finish();*/


                        // Launch login activity
                        Intent intent = new Intent(getActivity(), PinVerification.class);
                        intent.putExtra("user_id", userID);
                        intent.putExtra("code", CODE);
                        intent.putExtra("signupposition", signUpPosition);
                        startActivity(intent);
                        getActivity(). finish();




                    }
                    else {

                        String errorMsg = jObj.getString("error_message");
                        final String userStaus = jObj.getString("user_status");
                        final String userID = jObj.getString("user_id");

                        Log.e("TAg", "user_status: " + userStaus);
                        Log.e("TAg", "user_status id: " + userID);

                        if (userStaus.equals("0")){

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("User Already Exist!");
                            alert.setMessage("Mobile Number/Email already exist \nPlease Verify its you by receiving \nVerification Code on Registered Number");
                            alert.setPositiveButton("Send Code", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(getActivity(), PinVerification.class);
                                    intent.putExtra("user_id", userID);
                                    intent.putExtra("code", "00");//00 for taging for verifying already register user
                                    //intent.putExtra("signupposition", signUpPosition);
                                    startActivity(intent);
                                    getActivity().finish();
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



                Log.e("TAG", "the sign up username: " + signUpUserNameText);
                Log.e("TAG", "the sign up fulname: " + signUpFullNameText);
                Log.e("TAG", "the sign up mobile number: " + signUpMobileText);
                Log.e("TAG", "the sign up dob: " + signUpDobText);
                Log.e("TAG", "the sign up city id: " + city_id);
                Log.e("TAG", "the sign up gender: " + signUpSelectedRadioText);
                Log.e("TAG", "the sign up email: " + signUpEmailText);
                Log.e("TAG", "the sign up password: " + signUpPassText);
                Log.e("TAG", "the sign up status id: " + experienceStatusId);
                Log.e("TAG", "the sign up pmdc: " + pmdcNumber);
                Log.e("TAG", "the sign up sign up uni name: " + mUserSocialId);






                params.put("key", Glob.Key);
                params.put("username", signUpUserNameText);
                params.put("fullname", signUpFullNameText);
                params.put("phone", "92"+ signUpMobileText);
                params.put("dob", signUpDobText);
                params.put("city", city_id);
                params.put("gender", signUpSelectedRadioText);
                params.put("email", signUpEmailText);
                params.put("password", signUpPassText);
                params.put("status", experienceStatusId);
                params.put("registration_no", pmdcNumber);
                params.put("social_id", mUserSocialId);

                //  params.put("pmdc_picture", experienceStatusId);

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


    //selectino pmd photo handler
    private void pmdfPhotoSelection(){

//        ll_pmdc_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                selectImage();
//            }
//        });


        pmdcImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });


        pmdcImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
            }
        });


        removePmdcDescriptionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_pmdc.setImageDrawable(null);
                pmdcImageFrameLayout.setVisibility(View.GONE);
                pmdc_select_picture_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Select Photo From Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Select Photo From Gallery")) {
                    userChoosenTask ="Select Photo From Gallery";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Select Photo From Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAg", "The Request code is: " + requestCode);

        if (data!=null) {

            //  if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }

    }


    //selecting image from galary
    private void onSelectFromGalleryResult(Intent data) {


        if (data!=null) {
            imageUri = data.getData();

            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                pmdcImageFrameLayout.setVisibility(View.VISIBLE);
                pmdc_select_picture_layout.setVisibility(View.GONE);
                iv_pmdc.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //getting image form camera
    private void onCaptureImageResult(Intent data) {

        try {


            bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            pmdcImageFrameLayout.setVisibility(View.VISIBLE);
            pmdc_select_picture_layout.setVisibility(View.GONE);
            iv_pmdc.setImageBitmap(bitmap1);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void signUpdoctorwithImage(String imagePath, String drId){


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Glob.UPLOAD_DR_PMDC_PIC )
                    .addFileToUpload(imagePath, "pmdc_picture") //Adding file
                    .addParameter("key", Glob.Key) //Adding text parameter to the request
                    .addParameter("doctor_id", drId)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    //getting real path of image uri
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }

                if (x.startsWith("1")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    signUpMobile.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(getActivity(), "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
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


    private void aliedHealthCategoriesCallingFromServer(){

        // Tag used to cancel the request
        String cancel_req_tag = "register";


        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.ALIED_HEALTH_CATEGOREIS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Categories Response: " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("categories");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            String  allied_health_category_id = finalobject.getString("allied_health_category_id");
                            String allied_health_category_name = finalobject.getString("allied_health_category_name");



                            Log.e("TAg", "alied health category id: " + allied_health_category_id);
                            Log.e("TAg", "alied health category name: " + allied_health_category_name);


                            CGSForAliedHealthCategories.add(new CitiesGetterSetter(allied_health_category_id, allied_health_category_name));

                        }

                    } else {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CitiesGetterSetter temp = new CitiesGetterSetter("0", "Select Status");
                CGSForAliedHealthCategories.add(0, temp);

                CitiesListCustomAdapter citiesListCustomAdapter = new CitiesListCustomAdapter(getActivity(), CGSForAliedHealthCategories);
                //doctorStatusAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_list, R.id.spinnerList, doctorStatus);
                btSignUpSelectDesignation.setAdapter(citiesListCustomAdapter);

                Log.e("TAG", "The size of alied health category list is: " + CGSForAliedHealthCategories.size());


                setSelectDocStatus();

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


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


}

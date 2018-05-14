package themedicall.com;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpAmbulance extends AppCompatActivity {
    String[] ambulanceTypeArray = { "Select Ambulance Type" , "Personal" , "Service"};
    String[] ambulanceServiceArray = { "Select Service" , "Edhi" , "Edhi 1" , "Edhi 2" , "Edhi 3" , "Edhi 4" , "Edhi 5"};
    ArrayAdapter<String> ambulanceTypeAdapter , ambulanceServiceAdapter;
    EditText ambulanceUserName ,ambulanceName , ambulanceMobile , ambulanceNumber , ambulanceVehicleNumber , ambulanceLocation , ambulanceAddress , ambulanceAbout , ambulanceEmail ,
            ambulancePass , ambulanceConfirmPass;

    String ambulanceUserNameStr ,ambulanceNameStr , ambulanceMobileStr , ambulanceNumberStr , ambulanceVehicleNumberStr , ambulanceLocationStr , ambulanceAddressStr , ambulanceAboutStr
            , ambulanceEmailStr , ambulancePassStr , ambulanceConfirmPassStr;
    private static final String userNameError=  "Username must started With Alphabet Or Underscore";

    ImageView  ambulanceImageFromCamera , ambulanceImageFromGallery , ambulanceImage , removeAmbulanceImg;
    FrameLayout ambulanceImageFrameLayout ;
    LinearLayout ambulanceSelectPictureLayout ;
    Button ambulanceSignUpBtn ;
    Spinner selectAmbulanceType , selectAmbulanceService;
    String selectAmbulanceTypeStr , selectAmbulanceServiceStr;
    RelativeLayout SelectAmbulanceTypeLayout , selectAmbulanceServiceLayout ;
    LinearLayout ambulancePhotoLayout ;
    private static final String TAG = "SignUpAmbulance";
    CustomProgressDialog dialog;
    int request = 2 ;
    String city;
    String Area;
    String Address;
    double lat;
    double lng ;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap bitmap1;
    Uri imageUri = null;
    private String  userChoosenTask;
    String profileImagePath;
    boolean isImageLoadingFromDevice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upambulance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkReadExternalStoragePermission();
        checkWriteExternalPermission();
        initiate();
        checkUniqueUserName();
        setArrayToSpinner();
        spinnerClickListener();
        startMobileWithOnlyNumber3();
        clickListenerForCaptureAndSelected();
        submitBtnClick();


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
        dialog=new CustomProgressDialog(SignUpAmbulance.this, 1);

        ambulanceUserName = (EditText) findViewById(R.id.ambulanceUserName);
        ambulanceName = (EditText) findViewById(R.id.ambulanceName);
        ambulanceName.setVisibility(View.GONE);
        ambulanceMobile = (EditText) findViewById(R.id.ambulanceMobile);
        ambulanceNumber = (EditText) findViewById(R.id.ambulanceNumber);
        ambulanceVehicleNumber = (EditText) findViewById(R.id.ambulanceVehicleNumber);

        ambulanceLocation = (EditText) findViewById(R.id.ambulanceLocation);
        ambulanceAddress = (EditText) findViewById(R.id.ambulanceAddress);
        ambulanceAbout = (EditText) findViewById(R.id.ambulanceAbout);
        ambulanceEmail = (EditText) findViewById(R.id.ambulanceEmail);
        ambulancePass = (EditText) findViewById(R.id.ambulancePass);
        ambulanceConfirmPass = (EditText) findViewById(R.id.ambulanceConfirmPass);
        ambulanceImageFromCamera = (ImageView) findViewById(R.id.ambulanceImageFromCamera);
        ambulanceImageFromGallery = (ImageView) findViewById(R.id.ambulanceImageFromGallery);
        ambulanceImage = (ImageView) findViewById(R.id.ambulanceImage);
        removeAmbulanceImg = (ImageView) findViewById(R.id.removeAmbulanceImg);
        ambulanceImageFrameLayout = (FrameLayout) findViewById(R.id.ambulanceImageFrameLayout);
        SelectAmbulanceTypeLayout = (RelativeLayout) findViewById(R.id.SelectAmbulanceTypeLayout);
        selectAmbulanceServiceLayout = (RelativeLayout) findViewById(R.id.selectAmbulanceServiceLayout);
        ambulanceSelectPictureLayout = (LinearLayout) findViewById(R.id.ambulanceSelectPictureLayout);
        selectAmbulanceServiceLayout.setVisibility(View.GONE);


        selectAmbulanceType = (Spinner) findViewById(R.id.selectAmbulanceType);
        selectAmbulanceService = (Spinner) findViewById(R.id.selectAmbulanceService);


        ambulanceSignUpBtn = (Button) findViewById(R.id.ambulanceSignUpBtn);

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


    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    public void checkUniqueUserName()
    {
        ambulanceUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!userNameValidator(ambulanceUserName.getText().toString()) || ambulanceUserName.length() <= 3)
                    {
                        ambulanceUserName.setError(userNameError);
                    }
                    else
                    {
                        checkUserNameService();
                    }
                }
            }
        });
    }


    public void setArrayToSpinner()
    {
        ambulanceTypeAdapter = new ArrayAdapter<String>(SignUpAmbulance.this , R.layout.spinner_list , R.id.spinnerList , ambulanceTypeArray);
        selectAmbulanceType.setAdapter(ambulanceTypeAdapter);

        ambulanceServiceAdapter = new ArrayAdapter<String>(SignUpAmbulance.this , R.layout.spinner_list , R.id.spinnerList , ambulanceServiceArray);
        selectAmbulanceService.setAdapter(ambulanceServiceAdapter);

    }

    public void spinnerClickListener()
    {
        selectAmbulanceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i)
               {
                   case 0:
                       ambulanceName.setVisibility(View.GONE);
                       selectAmbulanceServiceLayout.setVisibility(View.GONE);
                       ambulanceName.setText("");
                       selectAmbulanceService.setSelection(0);
                       break;
                   case 1:
                       ambulanceName.setVisibility(View.VISIBLE);
                       selectAmbulanceService.setSelection(0);
                       selectAmbulanceServiceLayout.setVisibility(View.GONE);
                       break;
                   case 2:
                       ambulanceName.setVisibility(View.GONE);
                       ambulanceName.setText("");
                       selectAmbulanceServiceLayout.setVisibility(View.VISIBLE);
                       break;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    public void checkUserNameService()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "SignUpAmbulance";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.URL+"is-username-unique", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "User Name Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        ambulanceUserName.setError(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "User Name Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                ambulanceUserNameStr = ambulanceUserName.getText().toString();


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("user_name", ambulanceUserNameStr);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void clickListenerForCaptureAndSelected()
    {
        ambulanceImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });


        ambulanceImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();

            }
        });


        removeAmbulanceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambulanceImage.setImageDrawable(null);
                ambulanceSelectPictureLayout.setVisibility(View.VISIBLE);
                ambulanceImageFrameLayout.setVisibility(View.GONE);

            }
        });

    }


    public void submitBtnClick()
    {


        ambulanceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createNewHospitalActivity = new Intent(SignUpAmbulance.this, MapForAppealBlood.class);
                startActivityForResult(createNewHospitalActivity , request);
            }
        });


        ambulanceSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ambulanceUserNameStr = ambulanceUserName.getText().toString();
                ambulanceNameStr = ambulanceName.getText().toString();
                ambulanceMobileStr = ambulanceMobile.getText().toString();
                ambulanceNumberStr = ambulanceNumber.getText().toString();
                ambulanceVehicleNumberStr = ambulanceVehicleNumber.getText().toString();
                ambulanceLocationStr = ambulanceLocation.getText().toString();
                ambulanceAddressStr = ambulanceAddress.getText().toString();
                ambulanceAboutStr = ambulanceAbout.getText().toString();
                ambulanceEmailStr = ambulanceEmail.getText().toString();
                ambulancePassStr = ambulancePass.getText().toString();
                ambulanceConfirmPassStr = ambulanceConfirmPass.getText().toString();
                selectAmbulanceTypeStr = selectAmbulanceType.getSelectedItem().toString();
                selectAmbulanceServiceStr = selectAmbulanceService.getSelectedItem().toString();



                if(!userNameValidator(ambulanceUserNameStr))
                {
                    ambulanceUserName.setError(userNameError);
                }
                else if(selectAmbulanceTypeStr.equals("Select Ambulance Type"))
                {
                    Toast.makeText(SignUpAmbulance.this, "Please select ambulance type", Toast.LENGTH_SHORT).show();
                }
                else if(selectAmbulanceTypeStr.equals("Personal") && ambulanceNameStr.equals(""))
                {
                    ambulanceName.setError("please enter service name");
                }
                else if(selectAmbulanceTypeStr.equals("Service") && selectAmbulanceServiceStr.equals("Select Service"))
                {
                    Toast.makeText(SignUpAmbulance.this, "Please select service", Toast.LENGTH_SHORT).show();
                }
                else if(!mobileNumberValidator(ambulanceMobileStr) || ambulanceMobileStr.equals("") || ambulanceMobileStr.length() > 10)
                {
                    ambulanceMobile.setError("Please enter mobile no");
                }
                else if(ambulanceNumberStr.equals(""))
                {
                    ambulanceNumber.setError("please enter ambulance licence #");
                }
                else if(ambulanceVehicleNumberStr.equals(""))
                {
                    ambulanceVehicleNumber.setError("please enter ambulance vehicle #");
                }
                else if(ambulanceLocationStr.equals(""))
                {
                    ambulanceLocation.setError("please select location");
                }
                else if(ambulanceAddressStr.equals(""))
                {
                    ambulanceAddress.setError("please enter complete address");
                }
                else if(!emailValidator(ambulanceEmailStr))
                {
                    ambulanceEmail.setError("please enter valid email address");
                }
                else if(ambulancePassStr.equals("") || ambulancePass.length() <= 5)
                {
                    ambulancePass.setError("Please Enter Password At Least 6 Character");
                }
                else if(!ambulanceConfirmPassStr.equals(ambulancePassStr))
                {
                    ambulanceConfirmPass.setError("Password not Match");
                }
                else
                {
                    ambulanceUserNameStr = ambulanceUserName.getText().toString();
                    ambulanceNameStr = ambulanceName.getText().toString();
                    ambulanceMobileStr = ambulanceMobile.getText().toString();
                    ambulanceNumberStr = ambulanceNumber.getText().toString();
                    ambulanceVehicleNumberStr = ambulanceVehicleNumber.getText().toString();
                    ambulanceLocationStr = ambulanceLocation.getText().toString();
                    ambulanceAddressStr = ambulanceAddress.getText().toString();
                    ambulanceAboutStr = ambulanceAbout.getText().toString();
                    ambulanceEmailStr = ambulanceEmail.getText().toString();
                    ambulancePassStr = ambulancePass.getText().toString();
                    ambulanceConfirmPassStr = ambulanceConfirmPass.getText().toString();
                    selectAmbulanceTypeStr = selectAmbulanceType.getSelectedItem().toString();
                    selectAmbulanceServiceStr = selectAmbulanceService.getSelectedItem().toString();

                    Log.e("tag" , "ambulance sign up ambulance user name : "+ambulanceUserNameStr);
                    Log.e("tag" , "ambulance sign up ambulance type : "+selectAmbulanceTypeStr);
                    Log.e("tag" , "ambulance sign up ambulance service type : "+selectAmbulanceServiceStr);
                    Log.e("tag" , "ambulance sign up ambulance service name : "+ambulanceNameStr);
                    Log.e("tag" , "ambulance sign up ambulance phone : "+ambulanceMobileStr);
                    Log.e("tag" , "ambulance sign up ambulance license # : "+ambulanceNumberStr);
                    Log.e("tag" , "ambulance sign up ambulance vehicle # : "+ambulanceVehicleNumberStr);
                    Log.e("tag" , "ambulance sign up ambulance google address : "+ambulanceLocationStr);
                    Log.e("tag" , "ambulance sign up ambulance local address : "+ambulanceAddressStr);
                    Log.e("tag" , "ambulance sign up ambulance about : "+ambulanceAboutStr);
                    Log.e("tag" , "ambulance sign up ambulance email : "+ambulanceEmailStr);
                    Log.e("tag" , "ambulance sign up ambulance pass : "+ambulancePassStr);

                }
            }
        });



    }


    public void startMobileWithOnlyNumber3()
    {

        ambulanceMobile.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();
                if(x.startsWith("0"))
                {

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }

                if (x.startsWith("1")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(SignUpAmbulance.this, "Pleae enter number starting with 3", Toast.LENGTH_SHORT).show();
                    ambulanceMobile.setText("");
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


    public void checkWriteExternalPermission()
    {
        if (ContextCompat.checkSelfPermission(SignUpAmbulance.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SignUpAmbulance.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    private void checkReadExternalStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(SignUpAmbulance.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SignUpAmbulance.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    checkWriteExternalPermission();
                    checkReadExternalStoragePermission();
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

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_FILE)
        {
            onSelectFromGalleryResult(data);
        }
        else if (requestCode == REQUEST_CAMERA)
        {
            onCaptureImageResult(data);
        }
        else if(requestCode == request)
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

                ambulanceLocation.setText(Address);

                Log.e("tag" , "onActivityResult City : " + city);
                Log.e("tag" , "onActivityResult Area : " + Area);
                Log.e("tag" , "onActivityResult Address : " + Address);
                Log.e("tag" , "onActivityResult lat : " + lat);
                Log.e("tag" , "onActivityResult lng : " + lng);
            }

        }
    }

    private void onSelectFromGalleryResult(Intent data) {


        if(data == null)
        {
            Log.e("tag" , "back with out selecting image");
        }
        else
        {
            imageUri = data.getData();

            try {
                profileImagePath = getRealPathFromURI(imageUri);
                isImageLoadingFromDevice = true;
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ambulanceImageFrameLayout.setVisibility(View.VISIBLE);
                ambulanceSelectPictureLayout.setVisibility(View.GONE);
                ambulanceImage.setImageBitmap(bitmap1);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    //getting image form camera
    private void onCaptureImageResult(Intent data) {


        if(data == null)
        {
            Log.e("tag" , "return back with out capturing image : "+data );
        }
        else
        {
            try {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageUri = getImageUri(SignUpAmbulance.this , photo);
                Log.e("tag" , "capture image uri above : "+ imageUri);
                profileImagePath = getRealPathFromURI(imageUri);
                isImageLoadingFromDevice = true;


                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ambulanceImageFrameLayout.setVisibility(View.VISIBLE);
                ambulanceSelectPictureLayout.setVisibility(View.GONE);
                ambulanceImage.setImageBitmap(bitmap1);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getRealPathFromURI(Uri contentURI)
    {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
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



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}

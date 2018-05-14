package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class JobsRequestFragment extends Fragment {
    EditText jobTitle , mobileNo , email , jobLocation , preferredLocation , keySkills , experience , description , requirements;
    Spinner job_request_age_spinner;
    Button ageRequired , qualification , speciality;
    RadioGroup genderRadioGroup;
    RadioButton genderMale , genderFemale ;
    private static final String IMAGE_DIRECTORY_NAME = "Medi Call";
    private Uri fileUri; // file url to store image/video
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap bitmap1;
    Uri imageUri = null;
    private String  userChoosenTask;
    String profileImagePath;
    boolean isImageLoadingFromDevice = false;
    int request = 2 ;
    String city;
    String Area;
    String Address;
    double lat;
    double lng ;
    ImageView  jobImageFromCamera , jobImageFromGallery , jobImage , removejobImg;
    FrameLayout jobImageFrameLayout ;
    LinearLayout jobSelectPictureLayout ;
    public JobsRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs_request, container, false);

        checkWriteExternalPermission();
        checkReadExternalStoragePermission();
        initiate(view);
        clickListenerForCaptureAndSelected();


        return view;
    }

    public void initiate(View view)
    {
        jobTitle = (EditText) view.findViewById(R.id.job_request_title);
        job_request_age_spinner = (Spinner) view.findViewById(R.id.job_request_age_spinner);
        mobileNo = (EditText) view.findViewById(R.id.job_request_phone);
        email = (EditText) view.findViewById(R.id.job_request_email);
        jobLocation = (EditText) view.findViewById(R.id.job_request_locaton);
        preferredLocation = (EditText) view.findViewById(R.id.job_request_preferred_location);
        keySkills = (EditText) view.findViewById(R.id.job_request_key_skills);
        experience = (EditText) view.findViewById(R.id.job_request_experience);
        description = (EditText) view.findViewById(R.id.job_request_description);
        requirements = (EditText) view.findViewById(R.id.job_request_any_requirement);

        qualification = (Button) view.findViewById(R.id.job_request_qualification);
        speciality = (Button) view.findViewById(R.id.job_request_speciality);

        genderRadioGroup = (RadioGroup) view.findViewById(R.id.job_request_gender_radioGroup);
        genderMale = (RadioButton) view.findViewById(R.id.job_request_male_radioBtn);
        genderFemale = (RadioButton) view.findViewById(R.id.job_request_female_radioBtn);

        jobImageFromCamera = (ImageView) view.findViewById(R.id.jobImageFromCamera);
        jobImageFromGallery = (ImageView) view.findViewById(R.id.jobImageFromGallery);
        jobImage = (ImageView) view.findViewById(R.id.jobImage);
        removejobImg = (ImageView) view.findViewById(R.id.removejobImg);
        jobImageFrameLayout = (FrameLayout) view.findViewById(R.id.jobImageFrameLayout);
        jobSelectPictureLayout = (LinearLayout) view.findViewById(R.id.jobSelectPictureLayout);

    }


    public void clickListenerForCaptureAndSelected()
    {
        jobImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });


        jobImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();

            }
        });


        removejobImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobImage.setImageDrawable(null);
                jobSelectPictureLayout.setVisibility(View.VISIBLE);
                jobImageFrameLayout.setVisibility(View.GONE);

            }
        });

    }


    public void checkWriteExternalPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    private void checkReadExternalStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
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

                jobLocation.setText(Address);

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
                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                jobImageFrameLayout.setVisibility(View.VISIBLE);
                jobSelectPictureLayout.setVisibility(View.GONE);
                jobImage.setImageBitmap(bitmap1);
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
                imageUri = getImageUri(getActivity() , photo);
                Log.e("tag" , "capture image uri above : "+ imageUri);
                profileImagePath = getRealPathFromURI(imageUri);
                isImageLoadingFromDevice = true;


                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                jobImageFrameLayout.setVisibility(View.VISIBLE);
                jobSelectPictureLayout.setVisibility(View.GONE);
                jobImage.setImageBitmap(bitmap1);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String getRealPathFromURI(Uri contentURI)
    {
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



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }




}

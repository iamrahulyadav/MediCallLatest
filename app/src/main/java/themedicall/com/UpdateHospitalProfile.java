package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import themedicall.com.Adapter.GalaryImageAdapter;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UpdateHospitalProfile extends AppCompatActivity {
    ImageView hosProfileImg , hosLicenseImg , hosCoverImg , removeHosLicenseImage , removeHosCoverImage;
    LinearLayout selectHosLicenseImgLayout , selectHosCoverImgLayout;
    LinearLayout hos_license_image_frameLayout , hos_cover_image_frameLayout ;

    int btnClicked=0;
    int hosProfileImgButton = 1;
    int hosLicenseImgButton = 2;
    int hosCoverImgButton = 3;

    private int REQUEST_CAMERA_PROFILE = 11, REQUEST_CAMERA_LICENSE = 12 ,REQUEST_CAMERA_COVER = 13 ;
    private  int SELECT_FILE_PROFILE = 21 , SELECT_FILE_LICENSE = 22 , SELECT_FILE_COVER = 23;
    private String  userChoosenTask;
    Bitmap bitmap1;
    private static final String IMAGE_DIRECTORY_NAME = "Medi Call";
    private Uri fileUri; // file url to store image/video


    private GalaryImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    private int Request_Code = 101;

    ArrayList<String> selectedurl;
    ArrayList<String> selectedItems;
    ArrayList<String> ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hospital_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        checkReadExternalStoragePermission();
        checkWriteExternalPermission();
        initiate();
        clickListener();

    }

    public void initiate()
    {
        hosProfileImg = (ImageView) findViewById(R.id.hospitalProfileImg);
        hosLicenseImg = (ImageView) findViewById(R.id.hos_license_img);
        hosCoverImg = (ImageView) findViewById(R.id.hos_cover_img);
        removeHosLicenseImage = (ImageView) findViewById(R.id.remove_hos_license_image);
        removeHosCoverImage = (ImageView) findViewById(R.id.remove_hos_cover_image);
        selectHosLicenseImgLayout = (LinearLayout) findViewById(R.id.select_hos_license_image_layout);
        selectHosCoverImgLayout = (LinearLayout) findViewById(R.id.select_hos_cover_image_layout);
        hos_license_image_frameLayout = (LinearLayout) findViewById(R.id.hos_license_image_frameLayout);
        hos_cover_image_frameLayout = (LinearLayout) findViewById(R.id.hos_cover_image_frameLayout);
    }

    public void clickListener()
    {
        hosProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClicked = hosProfileImgButton ;
                selectImage();
            }
        });

        selectHosLicenseImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClicked = hosLicenseImgButton ;
                selectImage();
            }
        });

        selectHosCoverImgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClicked = hosCoverImgButton ;
                selectImage();
            }
        });

        removeHosLicenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hosLicenseImg.setImageDrawable(null);
                selectHosLicenseImgLayout.setVisibility(View.VISIBLE);
                hos_license_image_frameLayout.setVisibility(View.GONE);
            }
        });

        removeHosCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hosCoverImg.setImageDrawable(null);
                selectHosCoverImgLayout.setVisibility(View.VISIBLE);
                hos_cover_image_frameLayout.setVisibility(View.GONE);
            }
        });


    }



    public void checkWriteExternalPermission()
    {
        if (ContextCompat.checkSelfPermission(UpdateHospitalProfile.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateHospitalProfile.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    private void checkReadExternalStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(UpdateHospitalProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateHospitalProfile.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }
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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Select Photo From Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHospitalProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(UpdateHospitalProfile.this);

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

    private void galleryIntent()
    {
        if(btnClicked == hosProfileImgButton)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE_PROFILE);
        }
        else if(btnClicked == hosLicenseImgButton)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE_LICENSE);
        }
        else if(btnClicked == hosCoverImgButton)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE_COVER);
        }
    }

    private void cameraIntent()
    {

        if(btnClicked == hosProfileImgButton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CAMERA_PROFILE);
        }
        else if(btnClicked == hosLicenseImgButton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CAMERA_LICENSE);
        }
        else if(btnClicked == hosCoverImgButton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CAMERA_COVER);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE_PROFILE)
                onSelectFromGalleryResult(data , SELECT_FILE_PROFILE);
            else if (requestCode == SELECT_FILE_LICENSE)
                onSelectFromGalleryResult(data , SELECT_FILE_LICENSE);
            else if (requestCode == SELECT_FILE_COVER)
                onSelectFromGalleryResult(data , SELECT_FILE_COVER);
            else if (requestCode == REQUEST_CAMERA_PROFILE)
                previewCapturedImage(REQUEST_CAMERA_PROFILE);
            else if (requestCode == REQUEST_CAMERA_LICENSE)
                previewCapturedImage(REQUEST_CAMERA_LICENSE);
            else if (requestCode == REQUEST_CAMERA_COVER)
                previewCapturedImage(REQUEST_CAMERA_COVER);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }




    private void  previewCapturedImage(int requestCode) {

        if(requestCode == REQUEST_CAMERA_PROFILE)
        {
            try {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                hosProfileImg.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        else if(requestCode == REQUEST_CAMERA_LICENSE)
        {
            try {

                hos_license_image_frameLayout.setVisibility(View.VISIBLE);
                selectHosLicenseImgLayout.setVisibility(View.GONE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                hosLicenseImg.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        else if(requestCode == REQUEST_CAMERA_COVER)
        {
            try {

                hos_cover_image_frameLayout.setVisibility(View.VISIBLE);
                selectHosCoverImgLayout.setVisibility(View.GONE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                hosCoverImg.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


    }

    private void onSelectFromGalleryResult(Intent data , int requestCode) {
        if (data != null) {
            if(requestCode == SELECT_FILE_PROFILE)
            {
                try {

                    bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hosProfileImg.setImageBitmap(bitmap1);
            }

            else if(requestCode == SELECT_FILE_LICENSE)
            {
                try {
                    hos_license_image_frameLayout.setVisibility(View.VISIBLE);
                    selectHosLicenseImgLayout.setVisibility(View.GONE);

                    bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hosLicenseImg.setImageBitmap(bitmap1);
            }

            else if(requestCode == SELECT_FILE_COVER)
            {
                try {
                    hos_cover_image_frameLayout.setVisibility(View.VISIBLE);
                    selectHosCoverImgLayout.setVisibility(View.GONE);

                    bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hosCoverImg.setImageBitmap(bitmap1);
            }


        }
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


}

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UpdateLabProfile extends AppCompatActivity {

    LinearLayout selectLabPictureLayout , labPictureLayout;
    ImageView labProfileImg , labPrescriptionImg , labRemoveImage ;

    int btnClicked=0;
    int labProfileImgButton = 1;
    int labPrescriptionImgButton = 2;

    private int REQUEST_CAMERA_PROFILE = 11, REQUEST_CAMERA_PRESCRIPTION = 12  ;
    private  int SELECT_FILE_PROFILE = 21 , SELECT_FILE_PRESCRIPTION = 22 ;
    private String  userChoosenTask;
    Bitmap bitmap1;
    private static final String IMAGE_DIRECTORY_NAME = "Medi Call";
    private Uri fileUri; // file url to store image/video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lab_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkReadExternalStoragePermission();
        checkWriteExternalPermission();
        initiate();
        clickListener();


    }

    public void initiate()
    {
        selectLabPictureLayout = (LinearLayout) findViewById(R.id.select_lab_picture_layout);
        labPictureLayout = (LinearLayout) findViewById(R.id.lab_picture_layout);

        labPrescriptionImg = (ImageView) findViewById(R.id.lab_prescription_img);
        labRemoveImage = (ImageView) findViewById(R.id.labRemoveImage);
        labProfileImg = (ImageView) findViewById(R.id.labProfileImg);


    }

    public void clickListener()
    {
        labProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClicked = labProfileImgButton ;
                selectImage();
            }
        });

        selectLabPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClicked = labPrescriptionImgButton ;
                selectImage();
            }
        });


        labRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labPrescriptionImg.setImageDrawable(null);
                selectLabPictureLayout.setVisibility(View.VISIBLE);
                labPictureLayout.setVisibility(View.GONE);
            }
        });


    }



    public void checkWriteExternalPermission()
    {
        if (ContextCompat.checkSelfPermission(UpdateLabProfile.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateLabProfile.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    private void checkReadExternalStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(UpdateLabProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateLabProfile.this,
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

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateLabProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(UpdateLabProfile.this);

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
        if(btnClicked == labProfileImgButton)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE_PROFILE);
        }
        else if(btnClicked == labPrescriptionImgButton)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE_PRESCRIPTION);
        }

    }

    private void cameraIntent()
    {

        if(btnClicked == labProfileImgButton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CAMERA_PROFILE);
        }
        else if(btnClicked == labPrescriptionImgButton)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_CAMERA_PRESCRIPTION);
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
            else if (requestCode == SELECT_FILE_PRESCRIPTION)
                onSelectFromGalleryResult(data , SELECT_FILE_PRESCRIPTION);
            else if (requestCode == REQUEST_CAMERA_PROFILE)
                previewCapturedImage(REQUEST_CAMERA_PROFILE);
            else if (requestCode == REQUEST_CAMERA_PRESCRIPTION)
                previewCapturedImage(REQUEST_CAMERA_PRESCRIPTION);

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
                labProfileImg.setImageBitmap(bitmap);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        else if(requestCode == REQUEST_CAMERA_PRESCRIPTION)
        {
            try {

                labPictureLayout.setVisibility(View.VISIBLE);
                selectLabPictureLayout.setVisibility(View.GONE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                labPrescriptionImg.setImageBitmap(bitmap);
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
                labProfileImg.setImageBitmap(bitmap1);
            }

            else if(requestCode == SELECT_FILE_PRESCRIPTION)
            {
                try {
                    labPictureLayout.setVisibility(View.VISIBLE);
                    selectLabPictureLayout.setVisibility(View.GONE);

                    bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                labPrescriptionImg.setImageBitmap(bitmap1);
            }



        }
    }

}

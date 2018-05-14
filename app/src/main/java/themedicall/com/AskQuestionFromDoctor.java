package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import themedicall.com.Adapter.GalaryImageAdapter;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.ItemOffsetDecoration;
import themedicall.com.Globel.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AskQuestionFromDoctor extends AppCompatActivity {
    EditText typeQuestion , speciality , age ;
    Button city , submitBtn;
    Switch shareMedicalRecord , postAnonymously;
    LinearLayout SelectImageLayout  , imageLayout ;
    ImageView diseaseImage;
    ImageView removeImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
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
        setContentView(R.layout.activity_ask_question_from_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        selectedItems = new ArrayList<String>();
        selectedurl = new ArrayList<String>();
        ss = new ArrayList<String>();

        populateImagesFromGallery();

        checkWriteExternalPermission();
        checkReadExternalStoragePermission();
        initiate();
        clickListener();



    }

    public void initiate()
    {

        typeQuestion = (EditText) findViewById(R.id.aks_ques_type_question);
        speciality = (EditText) findViewById(R.id.aks_ques_speciality);
        age = (EditText) findViewById(R.id.aks_ques_age);

        city = (Button) findViewById(R.id.aks_ques_city);
        submitBtn = (Button) findViewById(R.id.ask_ques_submitBtn);

        shareMedicalRecord = (Switch) findViewById(R.id.aks_ques_share_record_switch);
        postAnonymously = (Switch) findViewById(R.id.aks_ques_post_anonymously_switch);

        SelectImageLayout = (LinearLayout) findViewById(R.id.aks_ques_select_picture_layout);
        imageLayout = (LinearLayout) findViewById(R.id.aks_ques_picture);

        diseaseImage = (ImageView) findViewById(R.id.aks_ques_img);
        removeImage = (ImageView) findViewById(R.id.removeImage);



    }

    public void clickListener()
    {
        SelectImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diseaseImage.setImageDrawable(null);
                SelectImageLayout.setVisibility(View.VISIBLE);
                imageLayout.setVisibility(View.GONE);

            }
        });
    }



    public void checkWriteExternalPermission()
    {
        if (ContextCompat.checkSelfPermission(AskQuestionFromDoctor.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AskQuestionFromDoctor.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
    }

    private void checkReadExternalStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(AskQuestionFromDoctor.this,
                READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AskQuestionFromDoctor.this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(userChoosenTask.equals("Take Photo"))
//                        cameraIntent();
//                    else if(userChoosenTask.equals("Select Photo From Gallery"))
//                        galleryIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
//    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Select Photo From Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AskQuestionFromDoctor.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AskQuestionFromDoctor.this);

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
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);

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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                previewCapturedImage();
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




    private void  previewCapturedImage() {
        try {

            imageLayout.setVisibility(View.VISIBLE);
            SelectImageLayout.setVisibility(View.GONE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            diseaseImage.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                imageLayout.setVisibility(View.VISIBLE);
                SelectImageLayout.setVisibility(View.GONE);

                bitmap1 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        diseaseImage.setImageBitmap(bitmap1);
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

    public void btnChoosePhotosClick(View v){

        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {
            Toast.makeText(AskQuestionFromDoctor.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
            //Log.e(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
            for (int i = 0; i<selectedItems.size(); i++){
                Log.e("TAG", "Selected " + selectedItems.get(i));

                ss.add(selectedItems.get(i));
            }

            if (selectedItems.size()>50){
                Toast.makeText(this, "Images should not be more then 50", Toast.LENGTH_SHORT).show();
            }else {

                Intent i = new Intent();
                i.putStringArrayListExtra("list", ss);
                setResult(Request_Code, i);
                Log.e("TAG", "Array Size = " + ss.size());
                finish();

                //startActivity(i);


            }

        }
    }

    private void populateImagesFromGallery() {
        if (!mayRequestGalleryImages()) {
            return;
        }

        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    private boolean mayRequestGalleryImages() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
            //promptStoragePermission();
            showPermissionRationaleSnackBar();
        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
        }

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_FOR_STORAGE_PERMISSION: {

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        populateImagesFromGallery();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                            showPermissionRationaleSnackBar();
                        } else {
                            Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                break;
            }
        }
    }

    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");

        ArrayList<String> imageUrls = new ArrayList<String>();

        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            Log.e("TAG", "Image URLs: " + imageUrls.get(i));

        }

        return imageUrls;
    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new GalaryImageAdapter(this, imageUrls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
    }

    private void showPermissionRationaleSnackBar() {
        Snackbar.make(findViewById(R.id.button1), getString(R.string.app_name),
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(AskQuestionFromDoctor.this,
                        new String[]{READ_EXTERNAL_STORAGE},
                        REQUEST_FOR_STORAGE_PERMISSION);
            }
        }).show();

    }

    @Override
    public void onBackPressed() {


        selectedItems = imageAdapter.getCheckedItems();
        //selectedurl = imageAdapter.getCheckedImageUrl();

        ss = new ArrayList<>();

        for (int i = 0; i<selectedItems.size(); i++){

            //Log.e("TAG", "MY URLS: " + selectedurl.get(i));

            ss.add(selectedItems.get(i));

        }

        //Intent i  = new Intent(MainActivity.this, SelectImage.class);

        if (selectedItems.size()>10){
            Toast.makeText(this, "Images should not be more then 10", Toast.LENGTH_SHORT).show();
        }else {

            Intent i = new Intent();
            i.putStringArrayListExtra("list", ss);
            setResult(Request_Code, i);
            Log.e("TAG", "Array Size = " + ss.size());
            finish();

            //startActivity(i);


            Toast.makeText(AskQuestionFromDoctor.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();


            Log.d(AskQuestionFromDoctor.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
        }
        super.onBackPressed();
    }

}

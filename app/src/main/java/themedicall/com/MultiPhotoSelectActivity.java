package themedicall.com;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import themedicall.com.Adapter.GalaryImageAdapter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.ItemOffsetDecoration;


import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MultiPhotoSelectActivity extends AppCompatActivity {

    private GalaryImageAdapter imageAdapter;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    private int Request_Code = 101;

    ArrayList<CitiesGetterSetter> selectedurl;
    ArrayList<String> selectedItems;
    ArrayList<CitiesGetterSetter> ss;
    int preArraySize;
    int remaingAcount = 10;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_photo_select);

        selectedItems = new ArrayList<>();
        selectedurl = new ArrayList<>();
        ss = new ArrayList<>();

        populateImagesFromGallery();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MultiPhotoSelectActivity.this ,R.color.whiteColor)));
        getSupportActionBar().setTitle("Select Photos");

        Intent i = getIntent();
        preArraySize = i.getExtras().getInt("arraySize");

        Log.e("TAG", "The Preve size of Array: " + preArraySize);
    }

    public void btnChoosePhotosClick(View v){

        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        remaingAcount = remaingAcount - preArraySize;

        if (selectedItems!= null && selectedItems.size() > 0) {

            //Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
            //Log.e(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());


            if (selectedItems.size()>remaingAcount){
                Toast.makeText(this, "Images should not be more then " + " " + remaingAcount, Toast.LENGTH_SHORT).show();
                remaingAcount = 10;
            }
            else {

                for (int i = 0; i<selectedItems.size(); i++){
                    Log.e("TAG", "Selected " + selectedItems.get(i));

                    ss.add(new CitiesGetterSetter("0", selectedItems.get(i)));
                }

                Intent i = new Intent();
                i.putExtra("list", ss);
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
        Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the permission
                ActivityCompat.requestPermissions(MultiPhotoSelectActivity.this,
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

            ss.add(new CitiesGetterSetter("0", selectedItems.get(i)));

        }

        //Intent i  = new Intent(MainActivity.this, SelectImage.class);

        if (selectedItems.size()>10){
            Toast.makeText(this, "Images should not be more then 10", Toast.LENGTH_SHORT).show();
        }else {

            Intent i = new Intent();
            i.putExtra("list", ss);
            setResult(Request_Code, i);
            Log.e("TAG", "Array Size = " + ss.size());
            finish();

            //startActivity(i);


            Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();


            Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
        }
        super.onBackPressed();
    }
}
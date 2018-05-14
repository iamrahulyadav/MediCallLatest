package themedicall.com;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.ChugtaiLabFilterAdapter;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.GetterSetter.ChugtaiLabGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class OrderLabsFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static LinearLayout parentLayoutForLabTest;

    LinearLayout selectImageLayout , ImageLayout;
    ImageView labImg , removeImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String  userChoosenTask;
    Bitmap bitmap1;
    private static final String IMAGE_DIRECTORY_NAME = "Medi Call";
    private Uri fileUri; // file url to store image/video
    Button addFieldBtn , orderLabBtn , viewLabReportBtn ;
    ListView cityListView;
    SearchView search_view;
    CustomCityNewAdapter customCityNewAdapter;
    String lab_test_id ;
    String id;
    String lab_test_name;
    String lab_test_rate;
    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String cancel_req_tag = "Chugtai Lab Service";
    final String TAG = "Chugtai Lab Service";
    public static List<ChugtaiLabGetterSetter> cugtaiLabTestList;
    CustomProgressDialog dialog;
    public static TextView labTestTotalAmount ;

    EditText bookLabTestName , bookLabTestMobile , bookLabTestAddress  , bookLabTestInstruction ;
    String bookLabTestNameStr , bookLabTestMobileStr , bookLabTestAddressStr  , bookLabTestInstructionStr , bookLabTestCityStr ;
    Button bookLabTestCity ;
    ImageView labImageFromCamera , labImageFromGallery , labDescriptionImageImage , removeLabDescriptionImg;
    FrameLayout labImageFrameLayout ;
    Uri imageUri = null;



    public OrderLabsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_labs, container, false);


          checkReadExternalStoragePermission();
          checkWriteExternalPermission();
          initiate(view);
          getChugtaiLabTestList();
          clickListener();
          removePreviousView(view);
          SelectCity();

        return view;
    }

    public void initiate(View view)
    {

        bookLabTestName = (EditText) view.findViewById(R.id.bookLabTestName);
        bookLabTestMobile = (EditText) view.findViewById(R.id.bookLabTestMobile);
        bookLabTestAddress = (EditText) view.findViewById(R.id.bookLabTestAddress);
        bookLabTestInstruction = (EditText) view.findViewById(R.id.bookLabTestInstruction);
        bookLabTestCity = (Button) view.findViewById(R.id.bookLabTestCity);

        labTestTotalAmount = (TextView) view.findViewById(R.id.labTestTotalAmount);
        cugtaiLabTestList = new ArrayList<>();
        parentLayoutForLabTest = (LinearLayout) view.findViewById(R.id.bookLabTestCustomRow);
        dialog=new CustomProgressDialog(getActivity(), 1);

        selectImageLayout = (LinearLayout) view.findViewById(R.id.lab_select_picture_layout);
        ImageLayout = (LinearLayout) view.findViewById(R.id.lab_picture_layout);
        labImg = (ImageView) view.findViewById(R.id.lab_prescription_job_img);
        removeImage = (ImageView) view.findViewById(R.id.removeImage);
        addFieldBtn = (Button) view.findViewById(R.id.addFieldBtn);
        orderLabBtn = (Button) view.findViewById(R.id.orderLabBtn);
        viewLabReportBtn = (Button) view.findViewById(R.id.viewLabReportBtn);

        labImageFromCamera = (ImageView) view.findViewById(R.id.labImageFromCamera);
        labImageFromGallery = (ImageView) view.findViewById(R.id.labImageFromGallery);
        labDescriptionImageImage = (ImageView) view.findViewById(R.id.labDescriptionImageImage);
        removeLabDescriptionImg = (ImageView) view.findViewById(R.id.removeLabDescriptionImg);
        labImageFrameLayout = (FrameLayout) view.findViewById(R.id.labImageFrameLayout);


    }


    public void comingSoonDialog()
    {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_coming_soon_dialog);
        TextView close = (TextView) dialog.findViewById(R.id.closeDialog);
        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    public void removePreviousView(View view)
    {

        int totalViewCount = OrderLabsFragment.parentLayoutForLabTest.getChildCount();
        if (totalViewCount > 0){
            OrderLabsFragment.parentLayoutForLabTest.removeAllViews();
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ad_book_lab_test_custom_row , null);
        OrderLabsFragment.parentLayoutForLabTest.addView(rowView, OrderLabsFragment.parentLayoutForLabTest.getChildCount()-1);

        final AutoCompleteTextView bookLabTestAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.bookLabTestAutoComplete);
        final TextView lab_id_tv = (TextView) view.findViewById(R.id.lab_id_tv);
        final TextView id_tv = (TextView) view.findViewById(R.id.id_tv);
        final TextView lab_test_fee_tv = (TextView) view.findViewById(R.id.lab_test_fee_tv);
        final LinearLayout priceLayout = (LinearLayout) view.findViewById(R.id.priceLayout);
        final ChugtaiLabFilterAdapter chugtaiLabFilterAdapter = new ChugtaiLabFilterAdapter(getActivity()  , OrderLabsFragment.cugtaiLabTestList);
        bookLabTestAutoComplete.setAdapter(chugtaiLabFilterAdapter);
        Log.e("TAg","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");



        bookLabTestAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ChugtaiLabGetterSetter chugtaiLabGetterSetter = (ChugtaiLabGetterSetter) chugtaiLabFilterAdapter.getItem(i);
                String lab_id = chugtaiLabGetterSetter.getLab_test_id();
                String id = chugtaiLabGetterSetter.getId();
                String price = chugtaiLabGetterSetter.getLab_test_rate();
                String name = chugtaiLabGetterSetter.getLab_test_name();
                bookLabTestAutoComplete.setText(name);
                lab_test_fee_tv.setText(price);
                priceLayout.setVisibility(View.VISIBLE);

                Log.e("TAG", "my selected chugtai lab id : " + lab_id);
                Log.e("TAG", "my selected chugtai id : " + id);
                Log.e("TAG", "my selected chugtai price : " + price);


                Labs.totalAmount = Labs.totalAmount += Integer.parseInt(price) ;
                Log.e("TAG", "my selected chugtai total amount : " + Labs.totalAmount);
                OrderLabsFragment.labTestTotalAmount.setText(String.valueOf(Labs.totalAmount));
            }

        });
    }


    public void clickListener()
    {
//        selectImageLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });



        viewLabReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , ViewLabTestReport.class);
                startActivity(intent);
            }
        });


        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labImg.setImageDrawable(null);
                selectImageLayout.setVisibility(View.VISIBLE);
                ImageLayout.setVisibility(View.GONE);

            }
        });

        labImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });

        labImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
            }
        });

        removeLabDescriptionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                labDescriptionImageImage.setImageDrawable(null);
                labImageFrameLayout.setVisibility(View.GONE);
                selectImageLayout.setVisibility(View.VISIBLE);
            }
        });


        orderLabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookLabTestNameStr = bookLabTestName.getText().toString();
                bookLabTestMobileStr = bookLabTestMobile.getText().toString();
                bookLabTestAddressStr = bookLabTestAddress.getText().toString();
                bookLabTestCityStr = bookLabTestCity.getText().toString();
                bookLabTestInstructionStr = bookLabTestInstruction.getText().toString();


                if(bookLabTestNameStr.equals(""))
                {
                    bookLabTestName.setError("Please enter user name");
                }
                else if(bookLabTestMobileStr.equals(""))
                {
                    bookLabTestMobile.setError("Please enter mobile number");
                }
                else if(bookLabTestAddressStr.equals(""))
                {
                    bookLabTestAddress.setError("Please enter complete address");
                }
                else if(bookLabTestCityStr.equals("City"))
                {
                    Toast.makeText(getContext() , "Please Select city", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Log.e("tag" , "order lab user name : "+bookLabTestNameStr);
                    Log.e("tag" , "order lab user mobile : "+bookLabTestMobileStr);
                    Log.e("tag" , "order lab user address : "+bookLabTestAddressStr);
                    Log.e("tag" , "order lab user city : "+bookLabTestCityStr);
                    Log.e("tag" , "order lab user instruction : "+bookLabTestInstructionStr);


                }
            }
        });

    }


    public void SelectCity()
    {

        bookLabTestCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Select City");
                cityListView = (ListView) dialog.findViewById(R.id.cityList);
                search_view = (SearchView) dialog.findViewById(R.id.search_view);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                dialog.show();



                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                List cityList = databaseHelper.getAllPots();
                Log.e("TAG", "the city list count: " + databaseHelper.getCount());
                Log.e("TAG", "the city list from db: " + cityList.size());

                customCityNewAdapter = new CustomCityNewAdapter(getActivity(), cityList);
                cityListView.setAdapter(customCityNewAdapter);
                search_view.setOnQueryTextListener(OrderLabsFragment.this);


                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        //Toast.makeText(Home.this, "id "+City_id, Toast.LENGTH_SHORT).show();

                        bookLabTestCity.setText(City);

                        dialog.dismiss();

                    }
                });

            }
        });



    }

    @Override
    public boolean onQueryTextSubmit(String s) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        customCityNewAdapter.getFilter().filter(s);
        return false;
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

        //  if (resultCode == Activity.RESULT_OK) {
        if (requestCode == SELECT_FILE)
            onSelectFromGalleryResult(data);
        else if (requestCode == REQUEST_CAMERA)
            onCaptureImageResult(data);


    }


    //selecting image from galary
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Log.d("", "onSelectFromGalleryResult: license file");


     /*   Long tsLong = System.currentTimeMillis() / 1000;
        timestamp1 = tsLong.toString();*/

        if(data == null)
        {
            Log.e("tag" , "back with out selecting image");
        }
        else
        {
            imageUri = data.getData();

            try {
                //profileImg.setVisibility(View.VISIBLE);

                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                labImageFrameLayout.setVisibility(View.VISIBLE);
                selectImageLayout.setVisibility(View.GONE);
                labDescriptionImageImage.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    //getting image form camera
    private void onCaptureImageResult(Intent data) {

        try {

            bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            labImageFrameLayout.setVisibility(View.VISIBLE);
            selectImageLayout.setVisibility(View.GONE);
            labDescriptionImageImage.setImageBitmap(bitmap1);


        } catch (IOException e) {
            e.printStackTrace();
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

            ImageLayout.setVisibility(View.VISIBLE);
            selectImageLayout.setVisibility(View.GONE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            labImg.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    public void getChugtaiLabTestList()
    {

        dialog.show();

        strReq = new StringRequest(Request.Method.POST, Glob.CHUGTAI_LAB_TESTS_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "chugtai lab tests Service Response: " + response.toString());

                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("tests");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            lab_test_id = finalobject.getString("lab_test_id");
                            id = finalobject.getString("id");
                            lab_test_name = finalobject.getString("lab_test_name");
                            lab_test_rate = finalobject.getString("lab_test_rate");
                            cugtaiLabTestList.add(new ChugtaiLabGetterSetter(lab_test_id , id , lab_test_name , lab_test_rate));

                        }



                        Log.e("tag" , "cities lab list size "+ cugtaiLabTestList.size());

                        //Toast.makeText(getActivity() , "lab test  Added Successfully!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        //Toast.makeText(getActivity(), "else part of service "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "chugtai lab Service Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //getCitiesService();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }







}

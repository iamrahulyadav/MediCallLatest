package themedicall.com;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;
import themedicall.com.Adapter.CitiesListCustomAdapter;
import themedicall.com.Adapter.ClaimDoctorProfileAdapter;
import themedicall.com.Adapter.CustomAutoCompleteAdapterForBioRegistration;
import themedicall.com.Adapter.CustomCityNewAdapter;
import themedicall.com.Adapter.CustomeAutoCompleteAdapter;
import themedicall.com.Adapter.CustomeAutocompleteAdapterForTags;
import themedicall.com.Adapter.ImageAdapter;
import themedicall.com.Adapter.OtherItemAddingAdapter;
import themedicall.com.Adapter.SpecialityCustomAdapter;
import themedicall.com.GetterSetter.ClaimProfileGetterSetter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.GetterSetter.CustomeTagsGeterSeter;

import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomAutoCompleteView;
import themedicall.com.Globel.DatabaseHelper;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;
import themedicall.com.Interfaces.AddingNewCategoryInterface;
import themedicall.com.Interfaces.ClaimButtonInterface;
import themedicall.com.Services.GetAllDoctorDetailService;
import themedicall.com.Services.UploadProfileImageService;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class BasicInfoUpdateDocProfile extends Fragment implements SearchView.OnQueryTextListener, AddingNewCategoryInterface {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String  userChoosenTask;
    static Uri imageUri = null;
    Bitmap bitmap1;
    String timestamp1;
    ImageView profileImg;
    public static ImageView ivSelectMulitiPhoto;
    public static GridView PhoneImageGrid;
    public static org.apmem.tools.layouts.FlowLayout rl_custom_specialist_tags;
    public static org.apmem.tools.layouts.FlowLayout ll_custom_for_supspecialist;
    public static org.apmem.tools.layouts.FlowLayout fl_dr_services;
    public static org.apmem.tools.layouts.FlowLayout fl_dr_qualifications;
    public static AutoCompleteTextView ac_sup_specialisty;
    public static AutoCompleteTextView ac_sup_services;
    public static AutoCompleteTextView ac_sup_qualifications;
    Calendar myCalendar;
    MyReceiver myReceiver;
    CustomCityNewAdapter customCityNewAdapter;
    SearchView search_view ;
    public static String profileImagePath;
    public static boolean isImageLoadingFromDevice = false;
    String mySpecialistId, myUserId, myCatTigle;

    CustomAutoCompleteView myAutoComplete;
    // adapter for auto-complete
    ArrayAdapter<CitiesGetterSetter> myAdapter;

    ArrayList<CitiesGetterSetter> temArrayListForAvailableSpecialitiesInviews;

    int Request_Code = 101;
    public static int CAMERA_CODE = 111;
    static ArrayList<CitiesGetterSetter> fetchList;
    ArrayList<CitiesGetterSetter> tempList;
    ArrayList <String> checkedSpeciality;
    ArrayList <String> checkedSpecialityId;
    ArrayList<CitiesGetterSetter> checkedSepcialistTitleAndIDList;
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONArray subService, subQualification, institution, registrations;
    JSONArray claimDoctersJsonArray;

    public static EditText etFullName, etMobile, etDob, signUpDob;
    public static Button  btSpecialist, btSubCategory, btCity;
    public static Spinner btBloodGroup , btStatus;
    CitiesListCustomAdapter citiesListCustomAdapter;

    static String specialistId;
    String allSelectedSpecialityID = null;
    static String mCityID;
    static String mBloodgroupID;
    static String mStatusID;

    ArrayList<CitiesGetterSetter> allSpeciality;

    public ArrayList<CustomeTagsGeterSeter> subSpecialistArrayList;
    static public ArrayList<CustomeTagsGeterSeter> subServicesList;
    public ArrayList<CustomeTagsGeterSeter> subQualificationsList;
    static public ArrayList<CitiesGetterSetter> subInstitutions;
    static public ArrayList<CitiesGetterSetter> subRegistrations;

    public static ArrayList<ClaimProfileGetterSetter> claimProfileGetterSetterArrayList;

    String PROFILE_IMAGE_URL;

    Button btExit, btSave;

    static String mspecialities, msub_specialities, mservices, mqualifications, minstitutions, mexpertise, mregistrations, mpreticesListData;
    static String mDayID;


    private String mOtherItemId=null, mOtherItemTitle=null;
    private String mParamTitle = null;
    private String URL_FOR_OTHER_SERVICE;
    private int INDICATOR;
    private boolean isNewSpeciality;
    String[] doctorStatus = { "Specialist" , "Trainee Specialist" , "General Practitioner" , "Student Doctor"};
    String[] bloodGroupArray = {"Blood Group"  ,"A+" , "A-" , "B+" , "B-"  , "O+" , "O-" , "AB+" , "AB-"};

    ArrayAdapter<String> doctorStatusAdapter , bloodGroupAdapter;

    public BasicInfoUpdateDocProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =inflater.inflate(R.layout.fragment_basic_info_update_doc_profile, container, false);
        profileImg = (ImageView) v.findViewById(R.id.profileImg);
        ivSelectMulitiPhoto = (ImageView) v.findViewById(R.id.iv_select_muliti_photo);
        PhoneImageGrid = (GridView) v.findViewById(R.id.PhoneImageGrid);

        etFullName  = (EditText) v.findViewById(R.id.signUpName) ;
        etMobile  = (EditText) v.findViewById(R.id.signUpMobile) ;
        etDob  = (EditText) v.findViewById(R.id.signUpDob) ;

        signUpDob = (EditText) v.findViewById(R.id.signUpDob);
        btStatus = (Spinner) v.findViewById(R.id.signUpSelectDesignation);
        btSpecialist = (Button) v.findViewById(R.id.signUpSelectSpeciality);
        btSubCategory = (Button) v.findViewById(R.id.signUpSelectSubSpeciality);
        btCity = (Button) v.findViewById(R.id.signUpSelectCity);
        btBloodGroup = (Spinner) v.findViewById(R.id.bt_blood_group);

        rl_custom_specialist_tags = (org.apmem.tools.layouts.FlowLayout) v.findViewById(R.id.rl_custom_specialist_tags);
        ll_custom_for_supspecialist = (org.apmem.tools.layouts.FlowLayout) v.findViewById(R.id.ll_custom_for_supspecialist);
        fl_dr_services =  (org.apmem.tools.layouts.FlowLayout) v.findViewById(R.id.fl_dr_services);
        fl_dr_qualifications =  (org.apmem.tools.layouts.FlowLayout) v.findViewById(R.id.fl_dr_qualifications);
        ac_sup_specialisty = (AutoCompleteTextView) v.findViewById(R.id.ac_sup_specialisty);
        ac_sup_services = (AutoCompleteTextView) v.findViewById(R.id.ac_sup_services);
        ac_sup_qualifications = (AutoCompleteTextView) v.findViewById(R.id.ac_sup_qualifications);


        btExit = (Button) v.findViewById(R.id.bt_exit);
        btSave = (Button) v.findViewById(R.id.bt_save);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        checkedSpeciality = new ArrayList<>();
        checkedSpecialityId = new ArrayList<>();
        checkedSepcialistTitleAndIDList = new ArrayList<>();

        subSpecialistArrayList = new ArrayList<>();
        subServicesList = new ArrayList<>();
        subQualificationsList = new ArrayList<>();
        subInstitutions = new ArrayList<>();
        subRegistrations = new ArrayList<>();
        claimProfileGetterSetterArrayList = new ArrayList<>();

        myCalendar = Calendar.getInstance();

        etFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btExitClickListener();
        return v;
    }

    public void checkWriteExternalPermission()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
    }

    private void checkReadExternalStoragePermission()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Glob.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getContext());

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

/*

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
*/


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("TAg", "The Request code is: data " + data);
        Log.e("TAg", "The Request code is: " + requestCode);
        Log.e("tag" , "capture image uri in onActivityResult : "+imageUri);

        //  if (resultCode == Activity.RESULT_OK) {
        if (requestCode == SELECT_FILE) {
            onSelectFromGalleryResult(data);
        }
        else if (requestCode == REQUEST_CAMERA) {
            onCaptureImageResult(data);
        }

        //}
        else if (requestCode == CAMERA_CODE) {

            Log.e("TAG", "DATA : " + data);

            ImageFromCamera(data);
            Log.e("TAG", "SSSize: " + fetchList.size());

            if (fetchList != null) {

                for (int i = 0; i < fetchList.size(); i++) {

                    Log.e("TAG", "MY URLS: " + fetchList.get(i));
                }
            }

            if (fetchList != null) {

                if (fetchList.size() >= 10) {

                    ivSelectMulitiPhoto.setVisibility(View.GONE);
                }

                Log.e("TAg", "Current Size of Fetch List " + fetchList.size());
                if (fetchList.size() > 4) {


                    int dipAmount = 220;
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                    int converter = (int) (dipAmount * displayMetrics.density + 0.5f); // in dp
                    ViewGroup.LayoutParams layoutParams = PhoneImageGrid.getLayoutParams();
                    layoutParams.height = converter;
                    PhoneImageGrid.setLayoutParams(layoutParams);
                }
                if (fetchList.size() > 8) {


                    int dipAmount = 320;
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                    int converter = (int) (dipAmount * displayMetrics.density + 0.5f); // in dp
                    ViewGroup.LayoutParams layoutParams = PhoneImageGrid.getLayoutParams();
                    layoutParams.height = converter;
                    PhoneImageGrid.setLayoutParams(layoutParams);
                }


                PhoneImageGrid.setAdapter(new ImageAdapter(getActivity(), fetchList, 0));
            }


        }//end of camera result for multiphoto
        else if (requestCode == Request_Code) {

            Log.e("TAG", "Default: " + fetchList.size());
            if (fetchList.size() == 0) {
                Bundle extras = data.getExtras();

                if (extras != null) {
                    fetchList = (ArrayList<CitiesGetterSetter>) extras.getSerializable("list");
                    //fetchList = extras.getParcelable("list");
                }
            } else {

                Bundle extras1 = data.getExtras();

                if (extras1 != null) {
                    tempList = (ArrayList<CitiesGetterSetter>) extras1.getSerializable("list");
                    //tempList = extras1.getParcelable("list");

                    int currentSizeOfList = fetchList.size();
                    int selectedListeSize = tempList.size();

                    int totalsize = currentSizeOfList + selectedListeSize;
                    if (totalsize < 11) {
                        //adding the previouce array to current array
                        fetchList.addAll(tempList);

                    } else {
                        Toast.makeText(getActivity(), "You can Upload Maxinum 10 images", Toast.LENGTH_SHORT).show();
                    }


                }
            }


            //fetchList = getIntent().getStringArrayListExtra("list");
            Log.e("TAG", "MY URLS ss: " + fetchList);

            if (fetchList != null) {

                settingImageToAdpter(fetchList);

            }

        }//end of multi gallary image selector


    }//end of onActivity result

    private void onCaptureImageResult(Intent data) {

        Log.e("tag" , "capture image uri 123 data: "+ data);

        if(data!= null) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(getActivity(), photo);
            // bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            Log.e("tag" , "capture image uri above : "+ imageUri);
            profileImagePath = getRealPathFromURI(imageUri);
            isImageLoadingFromDevice = true;

            profileImg.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(imageUri).transform(new CircleTransformPicasso()).into(profileImg);
            // profileImg.setImageBitmap(bitmap1);
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp1 = tsLong.toString();
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (data!=null) {
            Uri image = data.getData();
            Log.e("TAG", "onSelectFromGalleryResult: license file");

            try {
                profileImg.setVisibility(View.VISIBLE);

                profileImagePath = getRealPathFromURI(image);
                isImageLoadingFromDevice = true;

                //bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);

                Picasso.with(getActivity()).load(image).transform(new CircleTransformPicasso()).into(profileImg);
                // profileImg.setImageBitmap(bitmap1);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void callingImageSelectListener(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Alert!");
        alert.setMessage("Select Image From gallary or from Camera");
        alert.setPositiveButton("Gallary", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //starting activity for galary
                Intent galryIntent = new Intent(getActivity(), MultiPhotoSelectActivity.class);
                int size =  fetchList.size();
                galryIntent.putExtra("arraySize", size);
                startActivityForResult(galryIntent, Request_Code);
            }
        });

        alert.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //calling camera Activity
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CODE);
            }
        });

        alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alert.show();


    }

    public void ImageFromCamera(Intent data){

        if (data != null) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            int height = photo.getHeight();
            int weidth = photo.getWidth();

            Log.e("TAG", "IMAGE HEIGHT Old " + height);
            Log.e("TAG", "IMAGE Weidht Old " + weidth);

            photo = Bitmap.createScaledBitmap(photo, 512, 512, true);

            int heightnew = photo.getHeight();
            int weidthnew = photo.getWidth();

            Log.e("TAG", "IMAGE HEIGHT New " + heightnew);
            Log.e("TAG", "IMAGE Weidht New " + heightnew);


            Uri tempUri = getImageUri(getActivity(), photo);
            Log.e("TAG", "IMAGE URI " + tempUri);
            File imageFileFromCamera = new File(getRealPathFromURI(tempUri));
            Log.e("TAG", "Image FILE URL: " + imageFileFromCamera);


            if (fetchList.size()!=0){
                //fetchList.clear();
                tempList.clear();
                tempList.add(new CitiesGetterSetter("0", imageFileFromCamera.toString()));
                Log.e("TAG", "test list Previouce Size: " + fetchList.size());
                fetchList.addAll(tempList);
                Log.e("TAG", "test list Current Size Size: " + fetchList.size());
            }else {

                fetchList.add(new CitiesGetterSetter("0", imageFileFromCamera.toString()));
                Log.e("TAG", "test Test Test: " + fetchList.size());
            }
        }

    }//end of image From Camera

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    public void setSelectCity()
    {

        btCity.setOnClickListener(new View.OnClickListener() {
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


                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                List cityList = databaseHelper.getAllPots();
                Log.e("TAG", "the city list count: " + databaseHelper.getCount());
                Log.e("TAG", "the city list from db: " + cityList.size());
                customCityNewAdapter = new CustomCityNewAdapter(getActivity(), cityList);


                //customCityNewAdapter = new CustomCityNewAdapter(getActivity() , GetAllCitiesListService.CityList);
                cityListView.setAdapter(customCityNewAdapter);
                search_view.setOnQueryTextListener(BasicInfoUpdateDocProfile.this);

                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        String City_id = city_id.getText().toString();

                        mCityID = City_id;


                        //Toast.makeText(getActivity(), "id "+City_id, Toast.LENGTH_SHORT).show();

                        btCity.setText(City);
                        dialog.dismiss();

                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
                    }
                });

//                search_view = (SearchView) dialog.findViewById(R.id.search_view);
//
//                searchCity.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//                        // When user changed the Text
//                        citiesListCustomAdapter.filter(cs.toString());
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//                                                  int arg3) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable arg0) {
//                        // TODO Auto-generated method stub
//                    }
//                });
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

    public void getDataFromSharePrefernce(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userName = sharedPreferences.getString("userfullname", null);
            String userEmail = sharedPreferences.getString("useremail", null);
            String mobileNumber  = sharedPreferences.getString("userphone", null);

            // etFullName.setText(userName);



            etMobile.setText(mobileNumber);

        }
    }



    public class GettingDataFromServer extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Tag used to cancel the request
            String cancel_req_tag = "register";

            /*progressDialog.setMessage("Please Wait ...");
            showDialog();*/

            UpdateDoctorProfile.dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {

                URL url = new URL(Glob.GETDATAFORSPECIALISTY); //"https://www.pk.house/app_webservices/franchiser_listing.php");


                connection = (HttpURLConnection) url.openConnection();
                // connection.setSSLSocketFactory(new ClientSSLSocketFactory(connection.getSSLSocketFactory()));
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                String userFullName = sharedPreferences.getString("userfullname", null);
                String myid = sharedPreferences.getString("myid", null);

                if (etFullName.getText().length()>0){

                    userFullName = etFullName.getText().toString();
                }

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("key", Glob.Key)
                        .appendQueryParameter("myid", myid)
                        .appendQueryParameter("fullname", userFullName)
                        .appendQueryParameter("specialities", specialistId)
                        .appendQueryParameter("claim_specialities", allSelectedSpecialityID);



                Log.e("TAg", "the ides are here: " + specialistId);

                String query = builder.build().getEncodedQuery().toString();

                // Open connection for sending data
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                connection.connect();

                int response_code = connection.getResponseCode();


                // Check if successful connection made
                if (response_code == HttpsURLConnection.HTTP_OK) {


                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    Log.e("TAG", "RESULT 123 33: " + result);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                connection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //hideDialog();
            UpdateDoctorProfile.dialog.dismiss();

            Log.e("TAG", "Server Respoonse: " + result);

            if (result!=null) {
                try {


                    JSONObject jObj = new JSONObject(result);
                    //boolean error = jObj.getBoolean("error");

                    //if (!error) {


                    jsonObject = new JSONObject(result);
                    jsonArray = jsonObject.getJSONArray("subspecs");
                    Log.e("Shoaib", "the Total subspecs " + jsonArray.length());
                    subService = jsonObject.getJSONArray("services");
                    Log.e("Shoaib", "the Total services " + subService.length());
                    subQualification = jsonObject.getJSONArray("qualifications");
                    Log.e("Shoaib", "the Total qualifications " + subQualification.length());
                    institution = jsonObject.getJSONArray("institution");
                    Log.e("Shoaib", "the Total institution " + institution.length());
                    registrations = jsonObject.getJSONArray("registrations");
                    Log.e("Shoaib", "the Total registrations " + registrations.length());

                    String totalClaimDoctors = jsonObject.getString("total_doctors");
                    Log.e("Shoaib", "the Total Doctors for claim " + totalClaimDoctors);

                    claimDoctersJsonArray = jsonObject.getJSONArray("doctors");
                    Log.e("Shoaib", "the Total Doctors for claim length " + claimDoctersJsonArray.length());

                    if (subSpecialistArrayList.size()>0){subSpecialistArrayList.clear();}
                    if(subServicesList.size()>0){subServicesList.clear();}
                    if(subQualificationsList.size()>0){subQualificationsList.clear();}
                    if (subInstitutions.size()>0){subInstitutions.clear();}
                    if (subRegistrations.size()>0){subRegistrations.clear();}
                    if (claimProfileGetterSetterArrayList.size()>0){claimProfileGetterSetterArrayList.clear();}


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject finalobject = jsonArray.getJSONObject(i);

                        String supSpecialistId = finalobject.getString("sub_speciality_id");
                        String subSpecialistTitle = finalobject.getString("sub_speciality_title");
                        String specialistyId = finalobject.getString("speciality_id");
                        Log.e("TAG", "Title is: " + subSpecialistTitle);

                        subSpecialistArrayList.add(new CustomeTagsGeterSeter(supSpecialistId , subSpecialistTitle, specialistyId));

                    }

                    for (int i = 0; i < subService.length(); i++) {

                        JSONObject finalobject = subService.getJSONObject(i);

                        String supServiceId = finalobject.getString("service_id");
                        String subServiceTitle = finalobject.getString("service_title");
                        String specialistyId = finalobject.getString("speciality_id");

                        subServicesList.add(new CustomeTagsGeterSeter(supServiceId , subServiceTitle, specialistyId));

                    }

                    for (int i = 0; i < subQualification.length(); i++) {

                        JSONObject finalobject = subQualification.getJSONObject(i);

                        String supQualificationId = finalobject.getString("qualification_id");
                        String subQualificationTitle = finalobject.getString("qualification_title");
                        String specialistyId = finalobject.getString("speciality_id");

                        subQualificationsList.add(new CustomeTagsGeterSeter(supQualificationId , subQualificationTitle, specialistyId));

                    }

                    for (int i = 0; i < institution.length(); i++) {

                        JSONObject finalobject = institution.getJSONObject(i);

                        String supQualificationId = finalobject.getString("institutions_doctor_id");
                        String subQualificationTitle = finalobject.getString("institutions_doctor_title");

                        subInstitutions.add(new CitiesGetterSetter(supQualificationId , subQualificationTitle));

                    }


                    for (int i = 0; i < registrations.length(); i++) {

                        JSONObject finalobject = registrations.getJSONObject(i);
                        String supQualificationId = finalobject.getString("registration_id");
                        String subQualificationTitle = finalobject.getString("registration_title");

                        subRegistrations.add(new CitiesGetterSetter(supQualificationId , subQualificationTitle));

                    }


                    Log.e("TAg", "for claim doctor list size: " + claimDoctersJsonArray.length());
                    //for claimDoctors
                    for (int i = 0; i<claimDoctersJsonArray.length(); i++){
                        JSONObject listObjec = claimDoctersJsonArray.getJSONObject(i);
                        String doctor_id = listObjec.getString("doctor_id");
                        String doctor_full_name = listObjec.getString("doctor_full_name");
                        String doctor_img = listObjec.getString("doctor_img");

                        Log.e("TAg", "for claim doctor id is: " + doctor_id);
                        Log.e("TAg", "for claim doctor name is: " + doctor_full_name);


                        ArrayList<String> specialitites = new ArrayList<>();
                        ArrayList<String> qualifications = new ArrayList<>();


                        JSONArray specialityClaimArray = listObjec.getJSONArray("speciality");
                        Log.e("TAg", "for claim doctor speciality size: " + specialityClaimArray.length());


                        for (int j = 0; j<specialityClaimArray.length(); j++){

                            JSONObject specialistyObjects = specialityClaimArray.getJSONObject(j);
                            String innerSpecialisty = specialistyObjects.getString("speciality");
                            JSONObject innerObject = new JSONObject(innerSpecialisty);
                            String speciality_designation = innerObject.getString("speciality_designation");
                            Log.e("TAg", "for claim doctor here speciality_designation is: " + speciality_designation);
                            specialitites.add(speciality_designation);

                        }

                        JSONArray qualificationClaimArray = listObjec.getJSONArray("qualifications");

                        for (int j = 0; j<qualificationClaimArray.length(); j++){
                            JSONObject qualificationsInnerObject = qualificationClaimArray.getJSONObject(j);
                            String qualificationsTitle = qualificationsInnerObject.getString("qualifications");
                            JSONObject QT = new JSONObject(qualificationsTitle);
                            String speciality_designation = QT.getString("qualification_title");
                            Log.e("TAg", "for claim doctor here qualification_title is: " + speciality_designation);
                            qualifications.add(speciality_designation);
                        }


                        claimProfileGetterSetterArrayList.add(new ClaimProfileGetterSetter(doctor_img, doctor_full_name, doctor_id, qualifications, specialitites));
                    }


                    Log.e("TAG", "calim list is: " + claimProfileGetterSetterArrayList.size());
                    dialogForClaimProfile("Claim Profile", claimProfileGetterSetterArrayList);

                    Log.e("TAG", "shoaib anwar is best: " + subSpecialistArrayList.size());
                    Log.e("TAG", "shoaib anwar is best: " + subServicesList.size());
                    Log.e("TAG", "shoaib anwar is best: " + subQualificationsList.size());
                    Log.e("TAG", "shoaib anwar is best: " + subInstitutions.size());
                    Log.e("TAG", "shoaib anwar is best: " + subRegistrations.size());

/*

                    // Log.e("TAG", "shoaib anwar is best: " + BasicInfoUpdateDocProfile.subRegistrations.size());

                    final CustomAutoCompleteAdapterForBioRegistration adapter1 = new CustomAutoCompleteAdapterForBioRegistration(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subRegistrations);
                    BioUpdateDoctorProfile.bioRegistration.setAdapter(adapter1);
                    BioUpdateDoctorProfile.bioRegistration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter1.getItem(i);
                            String text = citiesGetterSetter.getName();
                            String id = citiesGetterSetter.getId();
                            Log.e("TAG", "Selected Name tha 1: " + text);
                            Log.e("TAG", "Selected ID tha 1: " + id);
ad

                            if (id.equals("-1")){

                                BioUpdateDoctorProfile.bioRegistration.setText("");
                                Log.e("TAG", "Do Other TASK");

                            }
                            else {

                                View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                                TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                                TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);

                                subSpecialTitl.setText(text);
                                subSpecialID.setText(id);

                                //fl_for_reegistration,fl_for_experties;

                                BioUpdateDoctorProfile.fl_for_reegistration.setVisibility(View.VISIBLE);
                                BioUpdateDoctorProfile.fl_for_reegistration.addView(child);
                                BioUpdateDoctorProfile.bioRegistration.setText("");

                                onClickForSubRegistrationsCrossTag();

                            }
                        }
                    });
                    /*/
/***********
 //


 final CustomeAutoCompleteAdapter adapter2 = new CustomeAutoCompleteAdapter(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subInstitutions);
 BioUpdateDoctorProfile.bioInstitution.setAdapter(adapter2);
 BioUpdateDoctorProfile.bioInstitution.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter2.getItem(i);
String text = citiesGetterSetter.getName();
String id = citiesGetterSetter.getId();
Log.e("TAG", "Selected Name: " + text);
Log.e("TAG", "Selected ID: " + id);

if (id.equals("-1")){

BioUpdateDoctorProfile.bioInstitution.setText("");
Log.e("TAG", "Do Other TASK");
//dialogForAddingNewRegistration();

}
else {

View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
subSpecialTitl.setText(text);
subSpecialID.setText(id);

//fl_for_reegistration,fl_for_experties;

BioUpdateDoctorProfile.fl_for_institution.setVisibility(View.VISIBLE);
BioUpdateDoctorProfile.fl_for_institution.addView(child);
BioUpdateDoctorProfile.bioInstitution.setText("");

}

}
});
 /*/
/***********


 final CustomeAutocompleteAdapterForTags adapter3 = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subServicesList);
 BioUpdateDoctorProfile.bioExpertise.setAdapter(adapter3);
 BioUpdateDoctorProfile.bioExpertise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
@Override
public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter) adapter3.getItem(i);
String text = tagsGetterSetter.getName();
String id = tagsGetterSetter.getId();
String specialityId = tagsGetterSetter.getSpecialityId();
Log.e("TAG", "Selected Name: " + text);
Log.e("TAG", "Selected ID: " + id);


View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
tv_speciality_id.setText(specialityId);
subSpecialTitl.setText(text);
subSpecialID.setText(id);

//fl_for_reegistration,fl_for_experties;

BioUpdateDoctorProfile.fl_for_experties.setVisibility(View.VISIBLE);
BioUpdateDoctorProfile.fl_for_experties.addView(child);
BioUpdateDoctorProfile.bioExpertise.setText("");


}
});
 /*/
/***********
 //

 */

              /*      BioUpdateDoctorProfile bioUpdateDoctorProfile = new BioUpdateDoctorProfile();
                    bioUpdateDoctorProfile.settingAdapterForbioRegistration();
                    bioUpdateDoctorProfile.settingAdapterForbiobioInstitution();
                    bioUpdateDoctorProfile.settingAdapterForbiobioExpertise();*/


                    autocompletForSubSpecialisty();
                    autocompleteSubServices();
                    autocompleteForSunQualification();
                    autocompleteSubExperties();











                   /* CustomeAutoCompleteAdapter citiesListCustomAdapter = new CustomeAutoCompleteAdapter(getActivity(), subSpecialistArrayList);
                        ac_sup_specialisty.setAdapter(citiesListCustomAdapter);
*/


                   /* } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //etFullName.setText(GetAllDoctorDetailService.doctor_full_name.toString());

            }
        }

    }//end of GettingDataFromServer

    //getting list of sepecialisst
    public void getSpecialityList(final String specialistId)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET, Glob.SELECT_SPECIALITY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());
                hideDialog();

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("subspecs");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            String supId = finalobject.getString("sub_speciality_id");
                            String subTitle = finalobject.getString("sub_speciality_title");

                        }


                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("specialities", specialistId);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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
                new DatePickerDialog(getActivity(),R.style.CustomDatePickerDialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }


        });


    }
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        signUpDob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        etMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        doctorStatusAdapter = new ArrayAdapter<String>(getActivity() , R.layout.spinner_list , R.id.spinnerList , doctorStatus);
        btStatus.setAdapter(doctorStatusAdapter);

        if (btStatus.getSelectedItem().equals("Specialist")){mStatusID = "1";}
        if (btStatus.getSelectedItem().equals("Trainee Specialist")){mStatusID = "2";}
        if (btStatus.getSelectedItem().equals("General Practitioner")){mStatusID = "3";}
        if (btStatus.getSelectedItem().equals("Student Doctor")){mStatusID = "4";}

//        btStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final List<String> stausList = Arrays.asList(getResources().getStringArray(R.array.doctorstatus));
//
//                final Dialog dialog = new Dialog(getActivity());
//                dialog.setContentView(R.layout.custom_citylist_search);
//                dialog.setTitle("Select Status");
//                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
//                bt_dilaog_done.setVisibility(View.GONE);
//                ListView cityListView = (ListView) dialog.findViewById(R.id.cityList);
//                dialog.show();
//
//
//                cityListView.setAdapter(new DoctorStatusAdapter(getActivity() , stausList));
//
//                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
//                        // TextView city_id = (TextView) view.findViewById(R.id.city_id);
//                        String City = city_title.getText().toString();
//                        //String City_id = city_id.getText().toString();
//
//                        //Toast.makeText(SignUp.this, "id "+City_id, Toast.LENGTH_SHORT).show();
//
//                        btStatus.setText(City);
//
//                        //seting doctor specialization id
//                        if (btStatus.getText().equals("Specialist")){mStatusID = "1";}
//                        if (btStatus.getText().equals("Trainee Specialist")){mStatusID = "2";}
//                        if (btStatus.getText().equals("General Practitioner")){mStatusID = "3";}
//                        if (btStatus.getText().equals("Student Doctor")){mStatusID = "4";}
//
//                        dialog.dismiss();
//
//                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
//                search_view.setVisibility(View.GONE);
//
//            }
//        });


        btSpecialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temArrayListForAvailableSpecialitiesInviews  = new ArrayList<>();
                if(temArrayListForAvailableSpecialitiesInviews.size()>0) {
                    temArrayListForAvailableSpecialitiesInviews.clear();
                }


                //adding current seelcted item in tht list
                int mViewCount = rl_custom_specialist_tags.getChildCount();
                if (mViewCount>0){
                    for (int i = 0; i<mViewCount; i++){
                        View vv = rl_custom_specialist_tags.getChildAt(i);
                        TextView spId = (TextView) vv.findViewById(R.id.tv_id);
                        TextView spTag = (TextView) vv.findViewById(R.id.tv_tag);

                        String ttid = spId.getText().toString();
                        String ttTag = spTag.getText().toString();

                        temArrayListForAvailableSpecialitiesInviews.add(new CitiesGetterSetter(ttid, ttTag));

                        checkedSepcialistTitleAndIDList.add(new CitiesGetterSetter(ttid, ttTag));
                    }}

                //dialog for shwoing speciality list
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_citylist_search);
                dialog.setTitle("Select Specialisties");
                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
                search_view.setVisibility(View.GONE);
                ListView specialist = (ListView) dialog.findViewById(R.id.cityList);

                Button btDilaogDone = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                btDilaogDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        rl_custom_specialist_tags.setVisibility(View.VISIBLE);
                        // btSpecialist.setVisibility(View.GONE);

                        ArrayList<HashMap<String, String>> listOfID = new ArrayList<>();
                        for (String spcialistyId: checkedSpecialityId){
                            Log.e("TAg", "speciality Ids: " + spcialistyId);
                            HashMap<String, String> id = new HashMap<>();
                            id.put("id", spcialistyId);
                            listOfID.add(id);
                            //GetAllDoctorDetailService.specialistIdList.add(spcialistyId);
                        }

                        Log.e("TAG", "the the of array: " + listOfID.size());

                        JSONObject prectices = new JSONObject();
                        JSONArray jsonArraySpecialistID = new JSONArray(listOfID);
                        try {
                            prectices.put("specialities", jsonArraySpecialistID);
                            specialistId = prectices.toString();
                            //aa = a.toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        for (CitiesGetterSetter getSet : checkedSepcialistTitleAndIDList){

                            View child = getLayoutInflater().inflate(R.layout.custome_tag_layout_specialist, null);
                            TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                            TextView specialistId = (TextView) child.findViewById(R.id.tv_id);
                            String tagTitle = getSet.getName();
                            String tagId = getSet.getId();
                            int voiewCount = rl_custom_specialist_tags.getChildCount();
                            Log.e("TAG", "The total Available count are: " + voiewCount);

                            boolean isDublicat = false;
                            for (int i = 0; i<voiewCount; i++){
                                View vv = rl_custom_specialist_tags.getChildAt(i);
                                TextView spId = (TextView) vv.findViewById(R.id.tv_id);
                                String ttid = spId.getText().toString();
                                Log.e("TAG", "The available specialities: " + ttid);
                                if (tagId.equals(ttid)){
                                    Log.e("TAG", "The item is already available: " + ttid);
                                    isDublicat = true;
                                }
                            }

                            if (!isDublicat) {
                                specialistTilt.setText(tagTitle);
                                specialistId.setText(tagId);
                                rl_custom_specialist_tags.addView(child);
                                isDublicat = false;
                            }
                        }

                        //
                        allSpeciality = new ArrayList<>();
                        int voiewCount = rl_custom_specialist_tags.getChildCount();
                        Log.e("TAG", "The total Available count are: " + voiewCount);

                        if(voiewCount>0) {
                            if (allSelectedSpecialityID!=null){
                                allSelectedSpecialityID = null;
                            }
                            ArrayList<HashMap<String, String>> listOfIDAllSelectedSpecialities = new ArrayList<>();
                            for (int j = 0; j < voiewCount; j++) {
                                View vv = rl_custom_specialist_tags.getChildAt(j);
                                TextView spId = (TextView) vv.findViewById(R.id.tv_id);
                                TextView spTitle = (TextView) vv.findViewById(R.id.tv_tag);
                                String ttid = spId.getText().toString();
                                String ttTag = spTitle.getText().toString();
                                Log.e("TAG", "total available specialities: " + ttid);

                                HashMap<String, String> id = new HashMap<>();
                                id.put("id", ttid);
                                listOfIDAllSelectedSpecialities.add(id);
                            }

                            Log.e("TAG", "the the of array 123: " + listOfIDAllSelectedSpecialities.size());
                            JSONObject precticesAllSelectedSpecialities = new JSONObject();

                            JSONArray jsonArraySpecialistIDSelected = new JSONArray(listOfIDAllSelectedSpecialities);
                            try {
                                precticesAllSelectedSpecialities.put("specialities", jsonArraySpecialistIDSelected);
                                allSelectedSpecialityID = precticesAllSelectedSpecialities.toString();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //
                        Log.e("TAG", "The all selected Array list is: " + allSelectedSpecialityID);


                        //calling weServies
                        if (checkedSpecialityId.size()>0){

                            // gettingNextPageForData(aa);
                            isNewSpeciality = true;
                            GettingDataFromServer abc = new GettingDataFromServer();
                            abc.execute();

                            // getSpecialityList(specialistId);
                        }


                        dialog.dismiss();

                        Log.e("TAG", "Current current current size unchecked: " + checkedSepcialistTitleAndIDList.size());


                        //adding current seelcted item in tht list

                      /* int sizeofTemArray =  temArrayListForAvailableSpecialitiesInviews.size();
                        if (sizeofTemArray>0){
                            for (int ij = 0; ij<sizeofTemArray; ij++){
                                View vv = rl_custom_specialist_tags.getChildAt(ij);
                                TextView spId = (TextView) vv.findViewById(R.id.tv_id);
                               final String ttid = spId.getText().toString();
                               if (temArrayListForAvailableSpecialitiesInviews.size()>0) {
                                   for (CitiesGetterSetter cgs : temArrayListForAvailableSpecialitiesInviews) {

                                       String idSpe = cgs.getId().toLowerCase();
                                       if (idSpe.equals(ttid)) {

                                           removingSubViesOfSpeciality(vv, ttid);

                                       }
                                   }
                               }

                            }}*/



                        //removingSubViesOfSpeciality()


                        if (checkedSpeciality.size()>0){
                            checkedSpeciality.clear();
                        }
                        if (checkedSpecialityId.size()>0){
                            checkedSpecialityId.clear();
                        }
                        if ( checkedSepcialistTitleAndIDList.size()>0){
                            checkedSepcialistTitleAndIDList.clear();
                        }

                        onClickForTagCross();
                    }



                });
                dialog.show();



                ArrayList<String> specialityid = new ArrayList<>();
                int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
                Log.e("TAG", "the size of selected selected  " + sizeSpecialistyArray);
                for (int s = 0; s < sizeSpecialistyArray; s++) {
                    CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(s);
                    String specialityId = citGS.getId();
                    String specialityTitle = citGS.getName();
                    specialityid.add(specialityId);
                    Log.e("TAG", "the size of selected selected  id " + specialityId);


                }
                Log.e("TAG", "Specialisty current available size: " + specialityid.size());

                if (UpdateDoctorProfile.specialityList.size()>0) {

                    ArrayList<HashMap<String, String>> selectedSpecialityList = new ArrayList<>();

                    int countSpecialitiesFromView = rl_custom_specialist_tags.getChildCount();
                    Log.e("TAG", "the speciality in views are: " + countSpecialitiesFromView);
                    if(countSpecialitiesFromView>0) {

                        for (int i = 0; i < countSpecialitiesFromView; i++) {
                            View myView = rl_custom_specialist_tags.getChildAt(i);
                            TextView specialityId = (TextView) myView.findViewById(R.id.tv_id);
                            TextView specialityName = (TextView) myView.findViewById(R.id.tv_tag);

                            HashMap<String, String> seletedSpeciality = new HashMap<>();

                            Log.e("TAG", "the speciality in views id: " + specialityId.getText());
                            Log.e("TAG", "the speciality in views title: " + specialityName.getText());

                            seletedSpeciality.put("sp_id", specialityId.getText().toString());
                            seletedSpeciality.put("sp_tag", specialityName.getText().toString());

                            selectedSpecialityList.add(seletedSpeciality);
                        }
                    }


                    specialist.setAdapter(new SpecialityCustomAdapter(getActivity(), UpdateDoctorProfile.specialityList, specialityid, selectedSpecialityList));
                    specialist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    specialist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Log.e("TAG", "Current current current size : " + checkedSepcialistTitleAndIDList.size());

                            TextView specialist_title = (TextView) view.findViewById(R.id.city_title);
                            TextView specilaist_id = (TextView) view.findViewById(R.id.city_id);
                            final String specialityId = specilaist_id.getText().toString();
                            final String specialist = specialist_title.getText().toString();

                            //String City_id = city_id.getText().toString();
                            //Toast.makeText(SignUp.this, "id "+City_id, Toast.LENGTH_SHORT).show();
                            //  btSpecialist.setText(specialist);
                            Log.e("TAg", "the specialist id: " + specilaist_id.getText().toString());

                            final CheckBox cb = (CheckBox) view.findViewById(R.id.cb_specialist);
                            TextView tv = (TextView) view.findViewById(R.id.city_title);

                            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                    Log.e("TAG", "the current check status is: " + b);
                                    if (b){

                                        checkedSepcialistTitleAndIDList.add(new CitiesGetterSetter(specialityId, specialist));
                                        checkedSpeciality.add(specialist);
                                        checkedSpecialityId.add(specialityId);

                                        Log.e("TAG", "Current current current size checked: " + checkedSepcialistTitleAndIDList.size());
                                    }
                                    else if (!b){

                                        Toast.makeText(getActivity(), "Plesase Remove From Tag Cross Button", Toast.LENGTH_SHORT).show();
                                        cb.setChecked(true);

                                     /*   for(int j = 0; j < checkedSepcialistTitleAndIDList.size(); j++)
                                        {
                                            CitiesGetterSetter obj = checkedSepcialistTitleAndIDList.get(j);

                                            String speId = obj.getId().toString();
                                            Log.e("TAG", "the value of selected specaility id : " + speId);
                                            Log.e("TAG", "the value of selected specaility id seecond: " + specialityId);
                                            if(obj.getId().equals(specialityId)){
                                                //found, delete.
                                                checkedSepcialistTitleAndIDList.remove(j);
                                                break;
                                            }

                                        }*/

                                        checkedSepcialistTitleAndIDList.remove(new CitiesGetterSetter(specialityId, specialist));

                                        checkedSpeciality.remove(specialist);
                                        checkedSpecialityId.remove(specialityId);


                                    }
                                }
                            });

                            cb.performClick();

                          /*  if (cb.isChecked()) {
                                checkedSepcialistTitleAndIDList.add(new CitiesGetterSetter(specialityId, specialist));
                                checkedSpeciality.add(specialist);
                                checkedSpecialityId.add(specialityId);


                            } else if (!cb.isChecked()) {
                                checkedSepcialistTitleAndIDList.remove(new CitiesGetterSetter(specialityId, specialist));
                                checkedSpeciality.remove(specialist);
                                checkedSpecialityId.remove(specialityId);
                            }*/

                            //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }


        });//end of button speciality

        btCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        fetchList= new ArrayList<>();
        tempList = new ArrayList<>();
        checkWriteExternalPermission();
        checkReadExternalStoragePermission();

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ivSelectMulitiPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callingImageSelectListener();

            }
        });




        setSelectCity();
        getDataFromSharePrefernce();
        setDoctorDob();
        selectingBloodGroup();


    }

    public void selectingBloodGroup()
    {


        bloodGroupAdapter = new ArrayAdapter<String>(getActivity() , R.layout.spinner_list , R.id.spinnerList , bloodGroupArray);
        btBloodGroup.setAdapter(bloodGroupAdapter);
        btBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e("The", "the selected Blood groud is: " + btBloodGroup.getSelectedItem().toString());


                if (btBloodGroup.getSelectedItem().equals("A+")){mBloodgroupID = "1";}
                if (btBloodGroup.getSelectedItem().equals("A-")){mBloodgroupID = "2";}
                if (btBloodGroup.getSelectedItem().equals("B+")){mBloodgroupID = "3";}
                if (btBloodGroup.getSelectedItem().equals("B-")){mBloodgroupID = "4";}
                if (btBloodGroup.getSelectedItem().equals("O+")){mBloodgroupID = "5";}
                if (btBloodGroup.getSelectedItem().equals("O-")){mBloodgroupID = "6";}
                if (btBloodGroup.getSelectedItem().equals("AB+")){mBloodgroupID = "7";}
                if (btBloodGroup.getSelectedItem().equals("AB-")){mBloodgroupID = "8";}
                if(btBloodGroup.getSelectedItem().equals("Blood Group")){mBloodgroupID = "9";}

                Log.e("The", "the selected Blood groud id is: " + mBloodgroupID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



//        final List<String> Lines = Arrays.asList(getResources().getStringArray(R.array.bloodgroups));
//
//        btBloodGroup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                final Dialog dialog = new Dialog(getActivity());
//                dialog.setContentView(R.layout.custom_citylist_search);
//                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
//                bt_dilaog_done.setVisibility(View.GONE);
//                dialog.setTitle("Select Bloodgroup");
//                ListView cityListView = (ListView) dialog.findViewById(R.id.cityList);
//                dialog.show();
//
//
//                cityListView.setAdapter(new BloodgroupCustomAdapter(getActivity() , Lines));
//
//                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
//                        // TextView city_id = (TextView) view.findViewById(R.id.city_id);
//                        String City = city_title.getText().toString();
//                        //String City_id = city_id.getText().toString();
//
//                        //Toast.makeText(SignUp.this, "id "+City_id, Toast.LENGTH_SHORT).show();
//
//                        btBloodGroup.setText(City);
//
//
//                        if (btBloodGroup.getText().equals("A+")){mBloodgroupID = "1";}
//                        if (btBloodGroup.getText().equals("A-")){mBloodgroupID = "2";}
//                        if (btBloodGroup.getText().equals("B+")){mBloodgroupID = "3";}
//                        if (btBloodGroup.getText().equals("B-")){mBloodgroupID = "4";}
//                        if (btBloodGroup.getText().equals("O+")){mBloodgroupID = "5";}
//                        if (btBloodGroup.getText().equals("O-")){mBloodgroupID = "6";}
//                        if (btBloodGroup.getText().equals("AB+")){mBloodgroupID = "7";}
//                        if (btBloodGroup.getText().equals("AB-")){mBloodgroupID = "8";}
//
//
//                        dialog.dismiss();
//
//                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
//                search_view.setVisibility(View.GONE);
//            }
//        });

    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            if (datapassed == 1) {

                UpdateDoctorProfile.dialog.hide();
                etFullName.setText(GetAllDoctorDetailService.doctor_full_name);
                if (GetAllDoctorDetailService.doctor_dob.equals("null")){
                    etDob.setText("");
                }else {
                    etDob.setText(GetAllDoctorDetailService.doctor_dob);
                }
                BioUpdateDoctorProfile.et_video_url.setText(GetAllDoctorDetailService.doctor_video);
                String statusId = GetAllDoctorDetailService.experience_status_id;


                Log.e("TAG", "Doctor Status id is: "+ GetAllDoctorDetailService.experience_status_id);
                Log.e("TAG", "PFROFILE IMAGE URL: " + GetAllDoctorDetailService.doctor_img);
                String imgeUrl = Glob.IMAGE_BACK_URL+GetAllDoctorDetailService.doctor_img;
                PROFILE_IMAGE_URL = imgeUrl;

                if (GetAllDoctorDetailService.doctor_img.contains("male_doctor") || GetAllDoctorDetailService.doctor_img.contains("female_doctor")){

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                    if (sharedPreferences!=null){
                        String pImage = sharedPreferences.getString("profile_img", null);
                        Log.e("TAG", "Shared Profile Image is: " + pImage);

                        if (!pImage.equals(null)){
                            if (pImage.contains("facebook") || pImage.contains("google")){
                                Picasso.with(getActivity()).load(pImage).transform(new CircleTransformPicasso()).into(profileImg);
                            }


                        }
                    }
                }else {
                    Picasso.with(getActivity()).load(PROFILE_IMAGE_URL).transform(new CircleTransformPicasso()).into(profileImg);
                }

                Glide
                        .with(getActivity())
                        .load(PROFILE_IMAGE_URL)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(300,300) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                Log.e("TAG", "The bitmap: " + resource);
                                //Uri imageURIFromBitmap = getImageUri(getActivity(), resource);
                                String imageURIFromBitmap =  bitmapToUriConverter(resource);
                                //String imageAddress = imageURIFromBitmap.toString();
                                Log.e("TAG", "the image path of server image: " + imageURIFromBitmap);


                                //profileImg.setImageBitmap(resource);
                                profileImagePath = imageURIFromBitmap;
                                isImageLoadingFromDevice = false;

                            }
                        });



                if (statusId.equals("1")) {
                    btStatus.setSelection(0);
                    mStatusID = "1";
                }
                if (statusId.equals("2")) {
                    btStatus.setSelection(1);
                    mStatusID = "2";
                }
                if (statusId.equals("3")) {
                    btStatus.setSelection(2);
                    mStatusID = "3";
                }
                if (statusId.equals("4")) {
                    btStatus.setSelection(3);
                    mStatusID = "4";
                }

                btSaveClickListener();

            }

            if (datapassed == 1234){

                Toast.makeText(arg0, "Yahoo .... ", Toast.LENGTH_SHORT).show();
            }

            String cityId = GetAllDoctorDetailService.city_id;
            DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
            CitiesGetterSetter cityData = databaseHelper.getSinglecity(cityId);
            btCity.setText(cityData.getName().toString());
            mCityID = cityId;
            /*for (CitiesGetterSetter cityData : GetAllCitiesListService.CityList) {
                if (cityData.getId().equals(cityId)) {
                    btCity.setText(cityData.getName().toString());
                    mCityID = cityId;
                }
            }//end for city list
*/
            String bloodgroupId = GetAllDoctorDetailService.blood_group_id;

            Log.e("tag" , "blood group id is : "+bloodgroupId);

            if (bloodgroupId.equals("9")) {
                btBloodGroup.setSelection(0);
                mBloodgroupID = "9";
            }
            if (bloodgroupId.equals("1")) {
                btBloodGroup.setSelection(1);
                mBloodgroupID = "1";
            }
            if (bloodgroupId.equals("2")) {
                btBloodGroup.setSelection(2);
                mBloodgroupID = "2";
            }
            if (bloodgroupId.equals("3")) {
                btBloodGroup.setSelection(3);
                mBloodgroupID = "3";
            }
            if (bloodgroupId.equals("4")) {
                btBloodGroup.setSelection(4);
                mBloodgroupID = "4";
            }
            if (bloodgroupId.equals("5")) {
                btBloodGroup.setSelection(5);
                mBloodgroupID = "5";
            }
            if (bloodgroupId.equals("6")) {
                btBloodGroup.setSelection(6);
                mBloodgroupID = "6";
            }
            if (bloodgroupId.equals("7")) {
                btBloodGroup.setSelection(7);
                mBloodgroupID = "7";
            }
            if (bloodgroupId.equals("8")) {
                btBloodGroup.setSelection(8);
                mBloodgroupID = "8";
            }//end of blood group

            //adding specialisty from serveer with dymanic views

            /*CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter.getItem(i);
            String text = citiesGetterSetter.getName();
            String id = citiesGetterSetter.getId();
            Log.e("TAG", "Selected Name: " + text);
            Log.e("TAG", "Selected ID: " + id);*/

            int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
            if (sizeSpecialistyArray>0) {
                for (int i = 0; i < sizeSpecialistyArray; i++) {
                    View child = getLayoutInflater().inflate(R.layout.custome_tag_layout_specialist, null);
                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);
                    CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(i);

                    specialistTilt.setText(citGS.getName());
                    specialistId.setText(citGS.getId());

                    rl_custom_specialist_tags.setVisibility(View.VISIBLE);
                    rl_custom_specialist_tags.addView(child);
                    //ac_sup_specialisty.setText("");
                }
            }

            subSpecialistArrayList = GetAllDoctorDetailService.allSubspecialitiesList;
            subQualificationsList = GetAllDoctorDetailService.allQualificationsList;
            subInstitutions = GetAllDoctorDetailService.allInstituationList;
            subRegistrations = GetAllDoctorDetailService.allRegistrationList;
            subServicesList = GetAllDoctorDetailService.allServicesList;



            //callling autocomplete for views
            autocompletForSubSpecialisty();
            autocompleteSubServices();
            autocompleteForSunQualification();
            autocompleteSubExperties();


            int sizeSubSpecialistyArray = GetAllDoctorDetailService.subSpecialistIdList.size();
            if (sizeSubSpecialistyArray>0){
                for (int i = 0; i < sizeSubSpecialistyArray; i++) {

                    View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                    TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                    TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                    TextView specialistyId  = (TextView) child.findViewById(R.id.tv_speciality_id);

                    subSpecialTitl.setText(GetAllDoctorDetailService.subSpecialistIdList.get(i).getName());
                    subSpecialID.setText(GetAllDoctorDetailService.subSpecialistIdList.get(i).getId());
                    specialistyId.setText(GetAllDoctorDetailService.subSpecialistIdList.get(i).getSpecialityId());

                    ll_custom_for_supspecialist.setVisibility(View.VISIBLE);
                    ll_custom_for_supspecialist.addView(child);
                    ac_sup_specialisty.setText("");

                }
            }

            //
            int sizeSubServicesArray = GetAllDoctorDetailService.servicesIdList.size();
            if (sizeSubServicesArray>0){
                for (int i = 0; i < sizeSubServicesArray; i++) {

                    View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                    TextView subServiceTitl = (TextView) child.findViewById(R.id.tv_tag);
                    TextView subServiceID = (TextView) child.findViewById(R.id.tv_id);
                    TextView specialistyId  = (TextView) child.findViewById(R.id.tv_speciality_id);
                    subServiceTitl.setText(GetAllDoctorDetailService.servicesIdList.get(i).getName());
                    subServiceID.setText(GetAllDoctorDetailService.servicesIdList.get(i).getId());
                    specialistyId.setText(GetAllDoctorDetailService.servicesIdList.get(i).getSpecialityId());

                    fl_dr_services.setVisibility(View.VISIBLE);
                    fl_dr_services.addView(child);
                    ac_sup_services.setText("");
                }
            }


            int sizeQualificationArray = GetAllDoctorDetailService.qualificationIdList.size();
            if(sizeQualificationArray>0) {
                for (int i = 0; i < sizeQualificationArray; i++) {

                    View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                    TextView subQualificationTitl = (TextView) child.findViewById(R.id.tv_tag);
                    TextView subQaulificationID = (TextView) child.findViewById(R.id.tv_id);
                    TextView specialistyId = (TextView) child.findViewById(R.id.tv_speciality_id);
                    subQualificationTitl.setText(GetAllDoctorDetailService.qualificationIdList.get(i).getName());
                    subQaulificationID.setText(GetAllDoctorDetailService.qualificationIdList.get(i).getId());
                    specialistyId.setText(GetAllDoctorDetailService.qualificationIdList.get(i).getSpecialityId());


                    fl_dr_qualifications.setVisibility(View.VISIBLE);
                    fl_dr_qualifications.addView(child);
                    ac_sup_qualifications.setText("");

                }
            }
            //
            int sizeRegistrationArray = GetAllDoctorDetailService.registrationIdList.size();
            if (sizeRegistrationArray>0) {
                for (int i = 0; i < sizeRegistrationArray; i++) {

                    View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                    TextView subRegistrationTitl = (TextView) child.findViewById(R.id.tv_tag);
                    TextView subRegistrationID = (TextView) child.findViewById(R.id.tv_id);
                    subRegistrationTitl.setText(GetAllDoctorDetailService.registrationIdList.get(i).getName());
                    subRegistrationID.setText(GetAllDoctorDetailService.registrationIdList.get(i).getId());

                    BioUpdateDoctorProfile.fl_for_reegistration.setVisibility(View.VISIBLE);
                    BioUpdateDoctorProfile.fl_for_reegistration.addView(child);
                    BioUpdateDoctorProfile.bioRegistration.setText("");


                }
            }

            int sizeInstituationsArray = GetAllDoctorDetailService.instituationIdList.size();
            for (int i = 0; i < sizeInstituationsArray; i++) {

                View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                TextView subInstituationTitl = (TextView) child.findViewById(R.id.tv_tag);
                TextView subInstituationID = (TextView) child.findViewById(R.id.tv_id);
                subInstituationTitl.setText(GetAllDoctorDetailService.instituationIdList.get(i).getName());
                subInstituationID.setText(GetAllDoctorDetailService.instituationIdList.get(i).getId());

                BioUpdateDoctorProfile.fl_for_institution.setVisibility(View.VISIBLE);
                BioUpdateDoctorProfile.fl_for_institution.addView(child);
                BioUpdateDoctorProfile.bioInstitution.setText("");

            }

            int sizeExpertiesArray = GetAllDoctorDetailService.expertieseIdList.size();
            if (sizeExpertiesArray>0){
                for (int i = 0; i < sizeExpertiesArray; i++) {

                    View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                    TextView subExpertiesTitl = (TextView) child.findViewById(R.id.tv_tag);
                    TextView subexpertieseID = (TextView) child.findViewById(R.id.tv_id);
                    TextView specialistyId  = (TextView) child.findViewById(R.id.tv_speciality_id);

                    subExpertiesTitl.setText(GetAllDoctorDetailService.expertieseIdList.get(i).getName());
                    subexpertieseID.setText(GetAllDoctorDetailService.expertieseIdList.get(i).getId());
                    specialistyId.setText(GetAllDoctorDetailService.expertieseIdList.get(i).getSpecialityId());

                    BioUpdateDoctorProfile.fl_for_experties.setVisibility(View.VISIBLE);
                    BioUpdateDoctorProfile.fl_for_experties.addView(child);
                    BioUpdateDoctorProfile.bioExpertise.setText("");

                }
            }

            BioUpdateDoctorProfile.bioAboutMe.setText(GetAllDoctorDetailService.doctor_about_me);
            BioUpdateDoctorProfile.bioAchievements.setText(GetAllDoctorDetailService.doctor_achievements);
            BioUpdateDoctorProfile.bioPublications.setText(GetAllDoctorDetailService.doctor_publications);

            BioUpdateDoctorProfile.bioExtraCurricular.setText(GetAllDoctorDetailService.doctor_extra_curricular_activities);
            BioUpdateDoctorProfile.bioWorkExperience.setText(GetAllDoctorDetailService.doctor_experience);
            BioUpdateDoctorProfile.checkUpDiscount.setText(GetAllDoctorDetailService.doctor_discount_for_check_up);
            BioUpdateDoctorProfile.procedureDiscount.setText(GetAllDoctorDetailService.doctor_discount_for_procedure);
            BioUpdateDoctorProfile.otherDiscount.setText(GetAllDoctorDetailService.doctor_detail_for_other_discount);
            BioUpdateDoctorProfile.writeAboutOtherPercent.setText(GetAllDoctorDetailService.doctor_detail_for_other_discount);
            BioUpdateDoctorProfile.writeAboutOtherWelfarePanel.setText(GetAllDoctorDetailService.doctor_want_to_join_medicall_welfare_panel);


            String offerAnyDiscountSwitchState = GetAllDoctorDetailService.doctor_offer_any_discount;
            String welFareChennalCheckSwitchState = GetAllDoctorDetailService.doctor_want_to_join_medicall_welfare_panel;
            String availableForvideoConsultationSwitchState = GetAllDoctorDetailService.doctor_available_for_video_consultation;
            String availableForHomeCareServicesSwitchState = GetAllDoctorDetailService.doctor_available_for_home_care_service;
            String wanttoBecomeBloodDonorSwitchState = GetAllDoctorDetailService.doctor_is_blood_donor;

            if (offerAnyDiscountSwitchState.equals("Yes")){
                BioUpdateDoctorProfile.discountPackageGetText = "Yes";
                BioUpdateDoctorProfile.discountPackageSwitch.setChecked(true);
            }
            if (welFareChennalCheckSwitchState.equals("Yes")){
                BioUpdateDoctorProfile.welfarePanelGetText = "Yes";
                BioUpdateDoctorProfile.welfarePanelSwitch.setChecked(true);
            }
            if (availableForvideoConsultationSwitchState.equals("Yes")){
                BioUpdateDoctorProfile.videoConsultationGetText = "Yes";
                BioUpdateDoctorProfile.videoConsultationSwitch.setChecked(true);
            }
            if (availableForHomeCareServicesSwitchState.equals("Yes")){
                BioUpdateDoctorProfile.homeCareServiceGetText = "Yes";
                BioUpdateDoctorProfile.homeCareServiceSwitch.setChecked(true);
            }
            if (wanttoBecomeBloodDonorSwitchState.equals("Yes")){
                BioUpdateDoctorProfile.becomeBloodDonorGetText = "Yes";
                BioUpdateDoctorProfile.becomeBloodDonorSwitch.setChecked(true);
            }

            String socialMedicaCheckboxState = GetAllDoctorDetailService.doctor_social_media_awareness;
            String quriesAnwerCheckboxState = GetAllDoctorDetailService.doctor_queries_answered;
            String medicallCampchCheckboxState = GetAllDoctorDetailService.doctor_medical_camp;
            String bloodCamcheckboxState  = GetAllDoctorDetailService.doctor_blood_camp;
            String healthArticleCheckBoxState = GetAllDoctorDetailService.doctor_health_article;
            String otherCheckBoxState = GetAllDoctorDetailService.doctor_other_activity_for_medicall;

            if (socialMedicaCheckboxState.equals("Yes")){
                BioUpdateDoctorProfile.socialMediaCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.socialMediaCheckBox.setChecked(true);
            }
            if (quriesAnwerCheckboxState.equals("Yes")){
                BioUpdateDoctorProfile.queriesAnswerCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.queriesAnswerCheckBox.setChecked(true);
            }
            if (medicallCampchCheckboxState.equals("Yes")){
                BioUpdateDoctorProfile.medicalCampCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.medicalCampCheckBox.setChecked(true);
            }
            if (bloodCamcheckboxState.equals("Yes")){
                BioUpdateDoctorProfile.bloodCampCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.bloodCampCheckBox.setChecked(true);
            }
            if (healthArticleCheckBoxState.equals("Yes")){
                BioUpdateDoctorProfile.healthArticleCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.healthArticleCheckBox.setChecked(true);
            }
            if (otherCheckBoxState.equals("Yes")){
                BioUpdateDoctorProfile.otherCheckBoxGetText = "Yes";
                BioUpdateDoctorProfile.otherCheckBox.setChecked(true);
            }

            allRegistrationAutoCompleteWithListener();
            bioInstitutionAutoCompleteWithListener();
            bioExpertiseAutoCompleteWithListener();

            onClickForTagCross();
            onClickForSubSpecialityCrossTag();
            onClickForSubServicesCrossTag();
            onClickForSubQualificationsCrossTag();
            onClickForSubRegistrationsCrossTag();
            onClickForSubExpertiseCrossTag();
            onClickForSubInstituationsCrossTag();


            Log.e("TAg", "Gallery Image Size: " + GetAllDoctorDetailService.listGallaryImages.size());

      /*     for(CitiesGetterSetter gSG : GetAllDoctorDetailService.listGallaryImages){

                String galleryImageUrlPath = gSG.getName().toString();
                final String imageId = gSG.getId();
                String finalPath = Glob.IMAGE_BACK_URL+galleryImageUrlPath;
               Log.e("TAG", "Gallery Image: " + finalPath);

               Glide
                       .with(getActivity())
                       .load(finalPath)
                       .asBitmap()
                       .into(new SimpleTarget<Bitmap>(300,300) {
                           @Override
                           public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                               Log.e("TAG", "The bitmap: " + resource);
                               //Uri imageURIFromBitmap = getImageUri(getActivity(), resource);
                               String imageURIFromBitmap =  bitmapToUriConverter(resource);
                               Log.e("TAG", "the image path of server image: " + imageURIFromBitmap);

                               BioUpdateDoctorProfile.fetchList.add(new CitiesGetterSetter(imageId, imageURIFromBitmap));
                               settingImageToAdpter(BioUpdateDoctorProfile.fetchList);
                               Log.e("TAG", "Feteched Array List Size 1: " + BioUpdateDoctorProfile.fetchList.size());

                           }
                       });

           }*/



        }



    }

    @Override
    public void onStart() {

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        getActivity().registerReceiver(myReceiver, intentFilter);

        super.onStart();
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(myReceiver);

        super.onStop();
    }

    //autocomplet for subSpeciality
    public void autocompletForSubSpecialisty()
    {

        final CustomeAutocompleteAdapterForTags adapter = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subSpecialistArrayList);
        ac_sup_specialisty.setAdapter(adapter);
        //setting OnItemclick
        ac_sup_specialisty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter)adapter.getItem(i);
                String text = tagsGetterSetter.getName();
                String id = tagsGetterSetter.getId();

                /////////////////////////////////////////////////////////
                if (id.equals("-1")){

                    ac_sup_specialisty.setText("");
                    Log.e("TAG", "Do Other TASK");

                    ArrayList<CitiesGetterSetter> speciality = new ArrayList<>();
                    int voiewCount = rl_custom_specialist_tags.getChildCount();
                    Log.e("TAG", "The total Available count are: " + voiewCount);

                    if(voiewCount>0) {
                        for (int j = 0; j < voiewCount; j++) {
                            View vv = rl_custom_specialist_tags.getChildAt(j);
                            TextView spId = (TextView) vv.findViewById(R.id.tv_id);
                            TextView spTitle = (TextView) vv.findViewById(R.id.tv_tag);
                            String ttid = spId.getText().toString();
                            String ttTag = spTitle.getText().toString();
                            Log.e("TAG", "The available specialities: " + ttid);
                            speciality.add(new CitiesGetterSetter(ttid, ttTag));

                        }
                    }
                    dialogSelectSpecialtyForOther(speciality, 1);// 1 indicate that this is sub-speciality
                }
                else {

                    boolean isMatched = false;
                    int totalviewForSubSpecialist = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();
                    if (totalviewForSubSpecialist>0){
                        for (int s = 0; s<totalviewForSubSpecialist; s++){
                            View v =  BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);

                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();

                            if (mTitle.equals(text)){
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }
                        }
                    }

                    Log.e("TAG", "Is Match found or not: " + isMatched);
                    if (!isMatched){
                        String specialityId = tagsGetterSetter.getSpecialityId();
                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                        tv_speciality_id.setText(specialityId);

                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);


                        ll_custom_for_supspecialist.setVisibility(View.VISIBLE);
                        ll_custom_for_supspecialist.addView(child);
                        ac_sup_specialisty.setText("");

                        onClickForSubSpecialityCrossTag();

                    }
                    else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        ac_sup_specialisty.setText("");
                    }
                }
            }

        });
    }

    //auatoComplet for subservices
    public void autocompleteSubServices()
    {

        final CustomeAutocompleteAdapterForTags adapterService = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subServicesList);
        ac_sup_services.setAdapter(adapterService);
        //setting OnItemclick
        ac_sup_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter) adapterService.getItem(i);
                String text = tagsGetterSetter.getName();
                String id = tagsGetterSetter.getId();
                String specialityId = tagsGetterSetter.getSpecialityId();

                if (id.equals("-1")){
                    ac_sup_services.setText("");
                    Log.e("TAG", "Do Other TASK");

                    ArrayList<CitiesGetterSetter> speciality = new ArrayList<>();

                    int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
                    Log.e("TAG", "the size of selected selected  " + sizeSpecialistyArray);
                    for (int s = 0; s < sizeSpecialistyArray; s++) {
                        CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(s);
                        speciality.add(new CitiesGetterSetter( citGS.getId(), citGS.getName()));


                    }
                    dialogSelectSpecialtyForOther(speciality, 2); // 2 indicate this is  services
                }
                else {

                    boolean isMatched = false;
                    int totalviewForServices = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();
                    if (totalviewForServices>0){
                        for (int s = 0; s<totalviewForServices; s++){

                            View v =  BasicInfoUpdateDocProfile.fl_dr_services.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)){
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched){
                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                        tv_speciality_id.setText(specialityId);
                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);
                        fl_dr_services.setVisibility(View.VISIBLE);
                        fl_dr_services.addView(child);
                        ac_sup_services.setText("");

                        onClickForSubServicesCrossTag();

                    }
                    else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        ac_sup_services.setText("");
                    }
                }
            }

        });
    }

    //auto complete for subQualifications
    public void autocompleteForSunQualification()
    {


        //adapter autocomplete for sub-qualification
        final CustomeAutocompleteAdapterForTags adapterQualifications = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subQualificationsList);
        ac_sup_qualifications.setAdapter(adapterQualifications);
        //setting OnItemclick
        ac_sup_qualifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter)adapterQualifications.getItem(i);
                String text = tagsGetterSetter.getName();
                String id = tagsGetterSetter.getId();
                String specialityId = tagsGetterSetter.getSpecialityId();

                if (id.equals("-1")){
                    ac_sup_qualifications.setText("");
                    Log.e("TAG", "Do Other TASK");

                    ArrayList<CitiesGetterSetter> speciality = new ArrayList<>();

                    int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
                    Log.e("TAG", "the size of selected selected  " + sizeSpecialistyArray);
                    for (int s = 0; s < sizeSpecialistyArray; s++) {
                        CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(s);
                        speciality.add(new CitiesGetterSetter( citGS.getId(), citGS.getName()));


                    }
                    dialogSelectSpecialtyForOther(speciality, 3); // 3 indicate this is qualification
                }
                else {

                    boolean isMatched = false;
                    int totalviewForQualifiations = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();
                    if (totalviewForQualifiations>0){
                        for (int s = 0; s<totalviewForQualifiations; s++){

                            View v =  BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)){
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched) {
                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                        tv_speciality_id.setText(specialityId);
                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);


                        fl_dr_qualifications.setVisibility(View.VISIBLE);
                        fl_dr_qualifications.addView(child);
                        ac_sup_qualifications.setText("");

                        onClickForSubQualificationsCrossTag();

                    }
                    else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        ac_sup_qualifications.setText("");
                    }

                }
            }

        });
    }

    //auto complete for sub experties

    public void autocompleteSubExperties()
    {

        final CustomeAutocompleteAdapterForTags adapterExperties = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subServicesList);
        BioUpdateDoctorProfile.bioExpertise.setAdapter(adapterExperties);
        //setting OnItemclick
        BioUpdateDoctorProfile.bioExpertise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter) adapterExperties.getItem(i);
                String text = tagsGetterSetter.getName();
                String id = tagsGetterSetter.getId();
                String specialityId = tagsGetterSetter.getSpecialityId();

                if (id.equals("-1")){
                    BioUpdateDoctorProfile.bioExpertise.setText("");
                    Log.e("TAG", "Do Other TASK");

                    ArrayList<CitiesGetterSetter> speciality = new ArrayList<>();

                    int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
                    Log.e("TAG", "the size of selected selected  " + sizeSpecialistyArray);
                    for (int s = 0; s < sizeSpecialistyArray; s++) {
                        CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(s);
                        speciality.add(new CitiesGetterSetter( citGS.getId(), citGS.getName()));

                    }
                    dialogSelectSpecialtyForOther(speciality, 2); // 2 indicate this is  services
                }
                else {

                    boolean isMatched = false;
                    int totalviewForServices = BioUpdateDoctorProfile.fl_for_experties.getChildCount();
                    if (totalviewForServices>0){
                        for (int s = 0; s<totalviewForServices; s++){

                            View v =  BioUpdateDoctorProfile.fl_for_experties.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)){
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched){
                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                        tv_speciality_id.setText(specialityId);
                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);
                        BioUpdateDoctorProfile.fl_for_experties.setVisibility(View.VISIBLE);
                        BioUpdateDoctorProfile.fl_for_experties.addView(child);
                        BioUpdateDoctorProfile.bioExpertise.setText("");

                        onClickForSubExpertiseCrossTag();
                    }
                    else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        BioUpdateDoctorProfile.bioExpertise.setText("");
                    }
                }
            }

        });
    }



    public void onRemovingSpecialisity(View v){
        Toast.makeText(getActivity(), "Remove Called", Toast.LENGTH_SHORT).show();
        //medicineCustomRow.removeView((View) (v).getParent());
    }

    //click for sub specialist
    public void onClickForSubSpecialityCrossTag(){
        int totalViesForSubSpecialists = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();
        for (int i = 0; i<totalViesForSubSpecialists; i++){
            final View child = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }

    }

    //click for sub services
    public void onClickForSubServicesCrossTag(){

        int totalViesForSubServices = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();
        for (int i = 0; i<totalViesForSubServices; i++){
            final View child = BasicInfoUpdateDocProfile.fl_dr_services.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }
    }

    //click for sub fl_for_reegistration
    public void onClickForSubRegistrationsCrossTag(){

        int totalViesForSubRegistrations = BioUpdateDoctorProfile.fl_for_reegistration.getChildCount();
        for (int i = 0; i<totalViesForSubRegistrations; i++){
            final View child = BioUpdateDoctorProfile.fl_for_reegistration.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BioUpdateDoctorProfile.fl_for_reegistration.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }
    }


    //click for sub fl_for_institution
    public void onClickForSubInstituationsCrossTag(){

        int totalViesForSubRegistrations = BioUpdateDoctorProfile.fl_for_institution.getChildCount();
        for (int i = 0; i<totalViesForSubRegistrations; i++){
            final View child = BioUpdateDoctorProfile.fl_for_institution.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BioUpdateDoctorProfile.fl_for_institution.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }
    }

    //click for sub fl_for_experties
    public void onClickForSubExpertiseCrossTag(){

        int totalViesForSubExpertiese = BioUpdateDoctorProfile.fl_for_experties.getChildCount();
        for (int i = 0; i<totalViesForSubExpertiese; i++){
            final View child = BioUpdateDoctorProfile.fl_for_experties.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BioUpdateDoctorProfile.fl_for_experties.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }
    }


    //click for sub qualifications
    public void onClickForSubQualificationsCrossTag(){

        int totalViesForSubQualification = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();
        for (int i = 0; i<totalViesForSubQualification; i++){
            final View child = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildAt(i);
            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) child.getParent();
                            parent.removeView(child);



                        }
                    });
                    alert.show();

                }
            });

        }
    }


    //click for specialist
    public void onClickForTagCross(){



        int totalviewForSpecialist = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildCount();
        Log.e("TAg", "Total Views: " + totalviewForSpecialist);
        for (int i = 0; i<totalviewForSpecialist; i++){
            final View child =  BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildAt(i);

            ImageView iv = (ImageView) child.findViewById(R.id.iv_tag_crose);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView specialistTilt = (TextView) child.findViewById(R.id.tv_tag);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);
                    final String mSpecialistyId = specialistId.getText().toString();

                    Log.e("TAg", "the specialist title: " + specialistTilt.getText().toString());
                    Log.e("TAg", "the specialist Id: " + specialistId.getText().toString());
                    final int total = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildCount();

                    Log.e("TAg", "Total View Count: " + total);

                    AlertDialog.Builder alert  = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do You Realy Want To Remove " + "'"+specialistTilt.getText().toString()+"'");
                    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();

                        }
                    });

                    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            removingSubViesOfSpeciality(child, mSpecialistyId);


                        }
                    });
                    alert.show();


                }
            });



        }
    }// end of corse click for speciality

    public String bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        String path = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            //  options.inSampleSize = calculateInSampleSize(options, 300, 300);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 400, 400,
                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    // Context.MODE_WORLD_READABLE)
                    Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            path = realPath;
            Log.e("TAG", "absolute path: " + realPath);
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return path;
    }

    public void settingImageToAdpter(ArrayList<CitiesGetterSetter> fetchList){

        if (fetchList.size() >= 10) {

            ivSelectMulitiPhoto.setVisibility(View.GONE);
        }

        Log.e("TAg", "Current Size of Fetch List " + fetchList.size());
        if (fetchList.size() > 4) {


            int dipAmount = 220;
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            int converter = (int) (dipAmount * displayMetrics.density + 0.5f); // in dp
            ViewGroup.LayoutParams layoutParams = PhoneImageGrid.getLayoutParams();
            layoutParams.height = converter;
            PhoneImageGrid.setLayoutParams(layoutParams);
        }
        if (fetchList.size() > 8) {


            int dipAmount = 320;
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            int converter = (int) (dipAmount * displayMetrics.density + 0.5f); // in dp
            ViewGroup.LayoutParams layoutParams = PhoneImageGrid.getLayoutParams();
            layoutParams.height = converter;
            PhoneImageGrid.setLayoutParams(layoutParams);
        }


        for (int i = 0; i < fetchList.size(); i++) {

            Log.e("TAG", "MY URLS: " + fetchList.get(i));

        }


        PhoneImageGrid.setAdapter(new ImageAdapter(getActivity(), fetchList, 0));
    }

    public void btExitClickListener()
    {
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });
    }

    public void btSaveClickListener()
    {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UpdateProfileOnServer();

            }
        });
    }

    //update serviceCalling

    public void UpdateProfileOnServer()
    {
        BioUpdateDoctorProfile.bioAboutMeGetText = BioUpdateDoctorProfile.bioAboutMe.getText().toString();
        BioUpdateDoctorProfile.bioAchievementsGetText = BioUpdateDoctorProfile.bioAchievements.getText().toString();
        BioUpdateDoctorProfile.bioPublicationsGetText = BioUpdateDoctorProfile.bioPublications.getText().toString();
        BioUpdateDoctorProfile.bioExtraCurricularGetText = BioUpdateDoctorProfile.bioExtraCurricular.getText().toString();
        BioUpdateDoctorProfile.bioWorkExperienceGetText = BioUpdateDoctorProfile.bioWorkExperience.getText().toString();
        BioUpdateDoctorProfile.checkUpDiscountGetText = BioUpdateDoctorProfile.checkUpDiscount.getText().toString();
        BioUpdateDoctorProfile.procedureDiscountGetText = BioUpdateDoctorProfile.procedureDiscount.getText().toString();
        BioUpdateDoctorProfile.otherDiscountGetText = BioUpdateDoctorProfile.otherDiscount.getText().toString();
        BioUpdateDoctorProfile.writeAboutOtherPercentGetText = BioUpdateDoctorProfile.writeAboutOtherPercent.getText().toString();
        BioUpdateDoctorProfile.writeAboutOtherWelfarePanelGetText = BioUpdateDoctorProfile.writeAboutOtherWelfarePanel.getText().toString();

        int totalviewForSpecialist = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildCount();
        int totalviewForSubSpecialist = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();
        int totalviewForServices = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();
        int totalviewForQualifiations = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();
        int totalViewForRegistration = BioUpdateDoctorProfile.fl_for_reegistration.getChildCount();
        int totalViewForInstitution = BioUpdateDoctorProfile.fl_for_institution.getChildCount();
        int totalViewForExperties  = BioUpdateDoctorProfile.fl_for_experties.getChildCount();
        Log.e("TAG", "the Total Count of View Pager: " + UpdateDoctorProfile.viewPager.getAdapter().getCount());
        int pageCount = UpdateDoctorProfile.viewPager.getAdapter().getCount();
        int totalViewCount = 0;
        if(pageCount==2){

        }else {
            totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();
        }


        ArrayList<HashMap<String, String>> listOfID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfSpecialistID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfSubSpecialistID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfServicesID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOQualificationsID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfRegistrationID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfInstituatinoID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfExpertiesID = new ArrayList<>();
        ArrayList<HashMap<String, JSONObject>> precticeDataRecord = new ArrayList<>();
        ArrayList<HashMap<String, String>> precticeTimingList = new ArrayList<>();



        //////

        if (totalviewForSpecialist>0){
            for (int i = 0; i<totalviewForSpecialist; i++){
                View view =  BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);

                String specialistTitlt = titl.getText().toString();
                String specialistId = id.getText().toString();

                Log.e("TAG", "My Specialist selected Title: " + specialistTitlt);
                Log.e("TAG", "My Specialist selected ID: " + specialistId);
                mid.put("id", specialistId);
                listOfSpecialistID.add(mid);

            }
        }


        JSONObject specialistIds = new JSONObject();

        JSONArray jsonArraySpecialistID = new JSONArray(listOfSpecialistID);
        try {
            specialistIds.put("specialities", jsonArraySpecialistID);

            BioUpdateDoctorProfile.specialities = specialistIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }




        if (totalviewForSubSpecialist>0){
            for (int i = 0; i<totalviewForSubSpecialist; i++){
                HashMap<String, String> mid = new HashMap<>();
                View view =  BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildAt(i);
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String subspecialistTitlt = titl.getText().toString();
                String subspecialistId = id.getText().toString();

                Log.e("TAG", "My Sub Specialist selected Title: " + subspecialistTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + subspecialistId);


                mid.put("id", subspecialistId);
                listOfSubSpecialistID.add(mid);

            }
        }

        //



        JSONObject subSpecialistIds = new JSONObject();

        JSONArray jsonArraySubSpecialistID = new JSONArray(listOfSubSpecialistID);
        try {
            subSpecialistIds.put("sub_specialities", jsonArraySubSpecialistID);

            BioUpdateDoctorProfile.sub_specialities  = subSpecialistIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //


        if (totalviewForServices>0){
            for (int i = 0; i<totalviewForServices; i++){
                View view =  BasicInfoUpdateDocProfile.fl_dr_services.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String serviceTitlt = titl.getText().toString();
                String servicesId = id.getText().toString();

                Log.e("TAG", "My Sub Specialist selected Title: " + serviceTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + servicesId);

                mid.put("id", servicesId);
                listOfServicesID.add(mid);

            }
        }


        JSONObject subServicesIds = new JSONObject();

        JSONArray jsonArrayServicesID = new JSONArray(listOfServicesID);
        try {
            subServicesIds.put("services", jsonArrayServicesID);

            BioUpdateDoctorProfile.services  = subServicesIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (totalviewForQualifiations>0){
            for (int i = 0; i<totalviewForQualifiations; i++){
                View view =  BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String qualificationTitlt = titl.getText().toString();
                String qualificationId = id.getText().toString();

                Log.e("TAG", "My Sub Specialist selected Title: " + qualificationTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + qualificationId);

                mid.put("id", qualificationId);
                listOQualificationsID.add(mid);

            }
        }

        JSONObject subqualificationIds = new JSONObject();

        JSONArray jsonArrayQualificationID = new JSONArray(listOQualificationsID);
        try {
            subqualificationIds.put("qualifications", jsonArrayQualificationID);

            BioUpdateDoctorProfile.qualifications  = subqualificationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (totalViewForRegistration>0){
            for (int i = 0; i<totalViewForRegistration; i++){
                View view =  BioUpdateDoctorProfile.fl_for_reegistration.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String qualificationTitlt = titl.getText().toString();
                String qualificationId = id.getText().toString();



                Log.e("TAG", "My Sub Specialist selected Title: " + qualificationTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + qualificationId);

                mid.put("id", qualificationId);
                listOfRegistrationID.add(mid);


            }
        }


        JSONObject subRegistrationIds = new JSONObject();

        JSONArray jsonArrayRegistrationID = new JSONArray(listOfRegistrationID);
        try {
            subRegistrationIds.put("registrations", jsonArrayRegistrationID);

            BioUpdateDoctorProfile.registrations  = subRegistrationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (totalViewForInstitution>0){
            for (int i = 0; i<totalViewForInstitution; i++){
                View view =  BioUpdateDoctorProfile.fl_for_institution.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();
                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String qualificationTitlt = titl.getText().toString();
                String qualificationId = id.getText().toString();

                Log.e("TAG", "My Sub Specialist selected Title: " + qualificationTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + qualificationId);

                mid.put("id", qualificationId);
                listOfInstituatinoID.add(mid);

            }
        }


        JSONObject subInstituationIds = new JSONObject();

        JSONArray jsonArrayInstituationID = new JSONArray(listOfInstituatinoID);
        try {
            subInstituationIds.put("institutions", jsonArrayInstituationID);

            BioUpdateDoctorProfile.institutions  = subInstituationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalViewForExperties>0){
            for (int i = 0; i<totalViewForExperties; i++){
                View view =  BioUpdateDoctorProfile.fl_for_experties.getChildAt(i);
                HashMap<String, String> mid = new HashMap<>();

                TextView titl = (TextView) view.findViewById(R.id.tv_tag);
                TextView id = (TextView) view.findViewById(R.id.tv_id);
                String qualificationTitlt = titl.getText().toString();
                String qualificationId = id.getText().toString();

                Log.e("TAG", "My Sub Specialist selected Title: " + qualificationTitlt);
                Log.e("TAG", "My Sub Specialist selected ID: " + qualificationId);

                mid.put("id", qualificationId);
                listOfExpertiesID.add(mid);

            }
        }


        JSONObject subExpertiesIds = new JSONObject();

        JSONArray jsonArrayExpertieseID = new JSONArray(listOfExpertiesID);
        try {
            subExpertiesIds.put("expertise", jsonArrayExpertieseID);

            BioUpdateDoctorProfile.expertise  = subExpertiesIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean allDiscountFieldsFill = true;

        for (int s = 0; s<totalViewCount; s++){
            if (precticeTimingList.size()>0){
                precticeTimingList.clear();
            }
            View views =  PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(s);
            EditText editText = (EditText) views.findViewById(R.id.editText);
            EditText etMinPrice = (EditText) views.findViewById(R.id.et_minprice);
            EditText etMaxPrice =  (EditText) views.findViewById(R.id.et_maxprice);
            EditText etStartTime = (EditText) views.findViewById(R.id.et_startTime);
            EditText etEndTime = (EditText) views.findViewById(R.id.et_endtime);

            //discount part
            SwitchCompat discountPackageSwitch = (SwitchCompat) views.findViewById(R.id.discountPackageSwitch);
            TableLayout discountTableLayout = (TableLayout) views.findViewById(R.id.discountTableLayout);
            EditText checkUpDiscount = (EditText) views.findViewById(R.id.checkUpPercent);
            EditText procedureDiscount = (EditText) views.findViewById(R.id.procedurePercent);
            EditText otherDiscount = (EditText) views.findViewById(R.id.otherPercent);
            EditText writeAboutOtherPercent = (EditText) views.findViewById(R.id.writeAboutOtherPercent) ;

            Button btDay = (Button) views.findViewById(R.id.bt_select_day);
            TextView tv_hospital_id = (TextView)views.findViewById(R.id.tv_hospital_id);
            LinearLayout ll_prectice_day_time = (LinearLayout)views.findViewById(R.id.ll_prectice_day_time);

            int totalInnerItemCount = ll_prectice_day_time.getChildCount();


            for (int i = 0; i<totalInnerItemCount; i++){

                View v = ll_prectice_day_time.getChildAt(i);

                EditText etInterStartTime = (EditText) v.findViewById(R.id.et_startTime);
                EditText etInterEndTime = (EditText) v.findViewById(R.id.et_endtime);
                Button btInterDay = (Button) v.findViewById(R.id.bt_select_day);

                String interStarTime = etInterStartTime.getText().toString();
                String interEndTime = etInterEndTime.getText().toString();
                String interDay = btInterDay.getText().toString();

                HashMap<String, String> inderDataList = new HashMap<>();

                if (interStarTime.length()>0 && interEndTime.length()>0) {
                    String startTimeIn24 = BioUpdateDoctorProfile.convertTo24Hour(interStarTime);
                    String endTimeIn24 = BioUpdateDoctorProfile.convertTo24Hour(interEndTime);
                    startTimeIn24 = startTimeIn24 + ":00";
                    endTimeIn24 = endTimeIn24 + ":00";
                    Log.e("TAG", "the time in 24 is: " + startTimeIn24);

                    if (interDay.equals("Monday")) {mDayID = "1";}
                    if (interDay.equals("Tuesday")) {mDayID = "2";}
                    if (interDay.equals("Wednesday")) {mDayID = "3";}
                    if (interDay.equals("Thursday")) {mDayID = "4";}
                    if (interDay.equals("Friday")) {mDayID = "5";}
                    if (interDay.equals("Saturday")) {mDayID = "6";}
                    if (interDay.equals("Sunday")) {mDayID = "7";}

                    inderDataList.put("day", mDayID);
                    inderDataList.put("startime", startTimeIn24);
                    inderDataList.put("endtime", endTimeIn24);

                    //ading record into main list
                    precticeTimingList.add(inderDataList);
                }

            }

            String workPlaceName = editText.getText().toString();
            String days = btDay.getText().toString();
            String startTime = etStartTime.getText().toString();
            String endTime = etEndTime.getText().toString();
            String minPrince  = etMinPrice.getText().toString();
            String maxPrice = etMaxPrice.getText().toString();
            String hospitalId = tv_hospital_id.getText().toString();



            HashMap<String, JSONObject> dataList = new HashMap<>();

            Log.e("TAg", "the time is here : " + startTime);

            if (startTime.length()>0 && endTime.length()>0) {
                String startTimeIn24 = BioUpdateDoctorProfile.convertTo24Hour(startTime);
                String endTimeIn24 = BioUpdateDoctorProfile.convertTo24Hour(endTime);
                startTimeIn24 = startTimeIn24 + ":00";
                endTimeIn24 = endTimeIn24 + ":00";
                Log.e("TAG", "the time in 24 is: " + startTimeIn24);

                if (days.equals("Monday")) {
                    mDayID = "1";
                }
                if (days.equals("Tuesday")) {
                    mDayID = "2";
                }
                if (days.equals("Wednesday")) {
                    mDayID = "3";
                }
                if (days.equals("Thursday")) {
                    mDayID = "4";
                }
                if (days.equals("Friday")) {
                    mDayID = "5";
                }
                if (days.equals("Saturday")) {
                    mDayID = "6";
                }
                if (days.equals("Sunday")) {
                    mDayID = "7";
                }


                String isCheck;
                String textCheckup, textProcedureDiscount, textOther, textOtherDetail;
                if (discountPackageSwitch.isChecked()){

                    textCheckup = checkUpDiscount.getText().toString();
                    textProcedureDiscount = procedureDiscount.getText().toString();
                    textOther = otherDiscount.getText().toString();
                    textOtherDetail = writeAboutOtherPercent.getText().toString();

                    isCheck = "Yes";


                    Log.e("TAG", "the text dicount field checkup: " + checkUpDiscount.getText().toString());
                    Log.e("TAG", "the text dicount field procedureDiscount: " + procedureDiscount.getText().toString());
                    Log.e("TAG", "the text dicount field Other: " + otherDiscount.getText().toString());
                    Log.e("TAG", "the text dicount field Other detail: " + writeAboutOtherPercent.getText().toString());

                    if (checkUpDiscount.getText().toString().length()==0){

                        textCheckup = "0";
                    }
                    if (procedureDiscount.getText().toString().length()==0){
                        textProcedureDiscount = "0";
                    }
                    if (otherDiscount.getText().toString().length()==0){
                        textOther = "0";
                    }


                }
                else {

                    textCheckup = "Shoaib Anwar";
                    textProcedureDiscount = "Shoaib Anwar";
                    textOther = "Shoaib Anwar";
                    textOtherDetail = "Shoaib Anwar";

                    Log.e("TAG", "the text dicount field Not offer discount: " + checkUpDiscount.getText().toString());
                    isCheck = "No";
                }


                if (discountPackageSwitch.isChecked()){
                    if (textCheckup.equals("0") && textProcedureDiscount.equals("0") && textOther.equals("0") ){
                        Toast.makeText(getActivity(), "Please Enter Discount", Toast.LENGTH_SHORT).show();
                        allDiscountFieldsFill = false;
                    }
                    else {
                        allDiscountFieldsFill = true;
                    }

                }


                try {
                    JSONObject a = new JSONObject();
                    JSONObject b = new JSONObject();
                    JSONObject c = new JSONObject();
                    JSONObject d = new JSONObject();

                    dataList.put("workplace", a.put("workplace", hospitalId));
                    //dataList.put("day", mDayID);
                    //dataList.put("startime", startTimeIn24);
                    //dataList.put("endtime", endTimeIn24);
                    dataList.put("minprice", b.put("minprice", minPrince));
                    dataList.put("maxprice", c.put("maxprice", maxPrice));

                    dataList.put("checkUpDiscount", d.put("checkUpDiscount", isCheck));
                    dataList.put("checkUpDiscount", d.put("checkup", textCheckup));
                    dataList.put("checkUpDiscount", d.put("procedureDiscount", textProcedureDiscount));
                    dataList.put("checkUpDiscount", d.put("Other", textOther));
                    dataList.put("checkUpDiscount", d.put("OtherDetail", textOtherDetail));


                    //making jjson Array with object timing
                    JSONObject jb = new JSONObject();
                    JSONArray jary = new JSONArray(precticeTimingList);
                    jb.put("timing", jary);
                    dataList.put("timing", jb);
                    precticeDataRecord.add(dataList);

                    // dataList.put("id", hospitalId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("TAG", "Hospital ID IS: " + hospitalId);


            }
        }


        if (allDiscountFieldsFill) {
            JSONObject prectices = new JSONObject();
            JSONArray precticeArray = new JSONArray(precticeDataRecord);
            try {
                prectices.put("prectice", precticeArray);
                BioUpdateDoctorProfile.preticesListData = prectices.toString();

                Log.e("TAg", "the TimingList is: " + BioUpdateDoctorProfile.preticesListData);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //getSpecialityList();
            SendingDataToServer sendingDataToServer = new SendingDataToServer();
            sendingDataToServer.execute();

            String drID;

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);

            drID = sharedPreferences.getString("myid", null);


            Log.e("TAG", "The size of image Array: " + BioUpdateDoctorProfile.fetchList.size());
            for(int i = 0; i< BioUpdateDoctorProfile.fetchList.size(); i++){
                CitiesGetterSetter galleryImages = BioUpdateDoctorProfile.fetchList.get(i);

                String filePath = galleryImages.getName().toString();
                String imageId = galleryImages.getId();

                Log.e("TAG", "doctor Profile Image Url: " + filePath);
                Log.e("TAG", "doctor Profile Image id: " + imageId);
                if (imageId.equals("0")){

                    bt_get_dynamic_view_data(filePath, drID);
                }
            }

            //uploadingProfileImage(profileImagePath, drID);

            if (isImageLoadingFromDevice) {
                //if (profileImagePath.equals(GetAllDoctorDetailService.doctor_img))
                uploadingProfileImage(profileImagePath, drID);
            }

        }//end of all discount field fill check


    }//end of update profile text

    public class SendingDataToServer extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait ...");
            showDialog();*/

            UpdateDoctorProfile.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {

                URL url = new URL(Glob.UPDATE_DOCTOR); //"https://www.pk.house/app_webservices/franchiser_listing.php");

                connection = (HttpURLConnection) url.openConnection();
                // connection.setSSLSocketFactory(new ClientSSLSocketFactory(connection.getSSLSocketFactory()));
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);


                String hospitalId;

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);

                String userID = sharedPreferences.getString("myid", null);
                Log.e("TAG", "User Id is is is: " + userID);
                String mName = BasicInfoUpdateDocProfile.etFullName.getText().toString();
                String mMobile = BasicInfoUpdateDocProfile.etMobile.getText().toString();
                String mDob = BasicInfoUpdateDocProfile.etDob.getText().toString();
                String mVideoUrl = BioUpdateDoctorProfile.et_video_url.getText().toString();


                if (BioUpdateDoctorProfile.bioWorkExperienceGetText.length()>0) {
                    double experience = Double.parseDouble(BioUpdateDoctorProfile.bioWorkExperienceGetText);
                    Log.e("TAg", "experience 123 " + experience);
                    if (experience > 1) {
                        BioUpdateDoctorProfile.bioWorkExperienceGetText = BioUpdateDoctorProfile.bioWorkExperienceGetText + " Years";
                    }
                    else {
                        BioUpdateDoctorProfile.bioWorkExperienceGetText = BioUpdateDoctorProfile.bioWorkExperienceGetText + " Year";
                    }

                }else {
                    BioUpdateDoctorProfile.bioWorkExperienceGetText = BioUpdateDoctorProfile.bioWorkExperienceGetText + "00";
                }
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("key", Glob.Key)
                        .appendQueryParameter("doctor_id",userID)
                        .appendQueryParameter("doctor_name",mName)
                        .appendQueryParameter("doctor_mobile",mMobile)
                        .appendQueryParameter("doctor_dob",mDob)
                        .appendQueryParameter("doctor_video",mVideoUrl)

                        .appendQueryParameter("doctor_about_me",BioUpdateDoctorProfile.bioAboutMeGetText)
                        .appendQueryParameter("city_id", mCityID)
                        .appendQueryParameter("doctor_blood_group", mBloodgroupID)
                        .appendQueryParameter("doctor_offer_any_discount",BioUpdateDoctorProfile.discountPackageGetText)
                        .appendQueryParameter("doctor_experience",BioUpdateDoctorProfile.bioWorkExperienceGetText)
                        .appendQueryParameter("experience_status_id", mStatusID)
                        .appendQueryParameter("doctor_is_blood_donor",BioUpdateDoctorProfile.becomeBloodDonorGetText)
                        .appendQueryParameter("doctor_discount_for_check_up",BioUpdateDoctorProfile.checkUpDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_procedure",BioUpdateDoctorProfile.procedureDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_other",BioUpdateDoctorProfile.otherDiscountGetText)
                        .appendQueryParameter("doctor_detail_for_other_discount",BioUpdateDoctorProfile.writeAboutOtherPercentGetText)
                        .appendQueryParameter("doctor_want_to_join_medicall_welfare_panel",BioUpdateDoctorProfile.welfarePanelGetText)
                        .appendQueryParameter("doctor_social_media_awareness",BioUpdateDoctorProfile.socialMediaCheckBoxGetText)
                        .appendQueryParameter("doctor_queries_answered",BioUpdateDoctorProfile.queriesAnswerCheckBoxGetText)
                        .appendQueryParameter("doctor_medical_camp",BioUpdateDoctorProfile.medicalCampCheckBoxGetText)
                        .appendQueryParameter("doctor_blood_camp",BioUpdateDoctorProfile.bloodCampCheckBoxGetText)
                        .appendQueryParameter("doctor_health_article",BioUpdateDoctorProfile.healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_other_activity_for_medicall",BioUpdateDoctorProfile.healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_available_for_video_consultation",BioUpdateDoctorProfile.videoConsultationGetText)
                        .appendQueryParameter("doctor_available_for_home_care_service",BioUpdateDoctorProfile.homeCareServiceGetText)
                        .appendQueryParameter("doctor_publications", BioUpdateDoctorProfile.bioPublicationsGetText)
                        .appendQueryParameter("doctor_extra_curricular_activities", BioUpdateDoctorProfile.bioExtraCurricularGetText)
                        //listing
                        .appendQueryParameter("practices", BioUpdateDoctorProfile.preticesListData)
                        .appendQueryParameter("specialities",BioUpdateDoctorProfile.specialities)
                        .appendQueryParameter("sub_specialities",BioUpdateDoctorProfile.sub_specialities)
                        .appendQueryParameter("services",BioUpdateDoctorProfile.services)
                        .appendQueryParameter("qualifications",BioUpdateDoctorProfile.qualifications)
                        .appendQueryParameter("institutions",BioUpdateDoctorProfile.institutions)
                        .appendQueryParameter("expertise",BioUpdateDoctorProfile.expertise)
                        .appendQueryParameter("registrations",BioUpdateDoctorProfile.registrations)
                        .appendQueryParameter("achievements",BioUpdateDoctorProfile.bioAchievementsGetText);



                String query = builder.build().getEncodedQuery().toString();

                // Open connection for sending data
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                connection.connect();

                int response_code = connection.getResponseCode();


                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {


                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    Log.e("TAG", "RESULT 123 33: " + result);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }
                else{

                    return("unsuccessful");
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                connection.disconnect();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // hideDialog();
            UpdateDoctorProfile.dialog.dismiss();

            Log.e("TAG", "Server Respoonse: " + result);

            if (result!=null) {
                try {


                    JSONObject jObj = new JSONObject(result);


                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String errorMsg = jObj.getString("error_message");
                        if (errorMsg.equals("Updated Successfully")){

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Update Successfully!");
                            alert.setMessage("Thank you " + BasicInfoUpdateDocProfile.etFullName.getText().toString() + " Your Profile Updated Successfully"
                                    +"\nYou will be logout please login again for changes");
                            alert.setPositiveButton("Logout Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    //log out scenrioa here
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    Intent intent = new Intent();
                                    intent.putExtra("succeess", "yes");
                                    getActivity().setResult(Activity.RESULT_OK, intent);
                                    getActivity().finish();

                                }
                            });

                            alert.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getActivity().finish();
                                }
                            });
                            alert.setCancelable(false);
                            //alert.show();


                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userfullname", BasicInfoUpdateDocProfile.etFullName.getText().toString());
                            editor.commit();

                            Intent intent = new Intent();
                            intent.setAction(Glob.FRAGMENT_SWITCHING_ACTION);
                            intent.putExtra("updated", 1);//sending notification that data received in service
                            getActivity().sendBroadcast(intent);



                        }
                        //Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();



                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(getActivity(), "There is problem with server connectivity", Toast.LENGTH_SHORT).show();
            }
        }

    }//end of GettingDataFromServer

    public void bt_get_dynamic_view_data(String imagePath, String drId){


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Glob.UPLOADGALLARY_IMAGES )
                    .addFileToUpload(imagePath, "picture") //Adding file
                    .addParameter("key", Glob.Key) //Adding text parameter to the request
                    .addParameter("doctor_id", drId)
                    .addParameter("id", "0")
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload


        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadingProfileImage(String imagePath, String drId){

        Intent i = new Intent(getActivity(), UploadProfileImageService.class);
        i.putExtra("imagePath", imagePath);
        i.putExtra("drId", drId);
        getActivity().startService(i);




        /*//Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Glob.UPLOADPROFILE_IMAGE )
                    .addFileToUpload(imagePath, "picture") //Adding file
                    .addParameter("key", Glob.Key) //Adding text parameter to the request
                    .addParameter("doctor_id", drId)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {

                            UpdateDoctorProfile.dialog.dismiss();
                            Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            UpdateDoctorProfile.dialog.dismiss();

                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e("TAG", "the response from server for upload image: " + serverResponse.getBodyAsString());

                            if (responseFromServer!=null) {
                                try {

                                    JSONObject jObj = new JSONObject(responseFromServer);

                                    boolean error = jObj.getBoolean("error");

                                    if (!error) {

                                        String image_url = jObj.getString("image_url");
                                        Log.e("TAG", "the uploaded image response is: " + image_url);

                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("profile_img", image_url);
                                        editor.commit();

                                        *//*Intent intent = new Intent();
                                        intent.setAction("imageLoaded");
                                        intent.putExtra("loaded", 1);//sending notification that data received in service
                                        getActivity().sendBroadcast(intent);
*//*
                                        //sending boradcast for image upload complete

                                    } else {

                                        String errorMsg = jObj.getString("error_message");
                                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload

            UpdateDoctorProfile.dialog.show();

        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }//end of multipart Service



    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void allRegistrationAutoCompleteWithListener()
    {
        final CustomAutoCompleteAdapterForBioRegistration adapter1 = new CustomAutoCompleteAdapterForBioRegistration(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subRegistrations);
        BioUpdateDoctorProfile.bioRegistration.setAdapter(adapter1);
        BioUpdateDoctorProfile.bioRegistration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter1.getItem(i);
                String text = citiesGetterSetter.getName();
                String id = citiesGetterSetter.getId();
                Log.e("TAG", "Selected Name: " + text);
                Log.e("TAG", "Selected ID 124: " + id);

                if (id.equals("-1")){

                    BioUpdateDoctorProfile.bioRegistration.setText("");
                    Log.e("TAG", "Do Other TASK");
                    dialogForAddingNewRegistration("Please Enter Registration");

                }
                else {

                    boolean isMatched = false;
                    int totalViewForRegistration = BioUpdateDoctorProfile.fl_for_reegistration.getChildCount();
                    if (totalViewForRegistration > 0) {
                        for (int s = 0; s < totalViewForRegistration; s++) {
                            View v = BioUpdateDoctorProfile.fl_for_reegistration.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)) {
                                Log.e("TAG", "Item is already Availabale");
                                Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched) {

                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);

                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);

                        //fl_for_reegistration,fl_for_experties;

                        BioUpdateDoctorProfile.fl_for_reegistration.setVisibility(View.VISIBLE);
                        BioUpdateDoctorProfile.fl_for_reegistration.addView(child);
                        BioUpdateDoctorProfile.bioRegistration.setText("");
                        onClickForSubRegistrationsCrossTag();

                    }
                    //Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                    BioUpdateDoctorProfile.bioRegistration.setText("");
                }
            }
        });
    }

    public void bioInstitutionAutoCompleteWithListener()
    {
        final CustomeAutoCompleteAdapter adapter2 = new CustomeAutoCompleteAdapter(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subInstitutions);
        BioUpdateDoctorProfile.bioInstitution.setAdapter(adapter2);
        BioUpdateDoctorProfile.bioInstitution.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter2.getItem(i);
                String text = citiesGetterSetter.getName();
                String id = citiesGetterSetter.getId();

                if (id.equals("-1")){

                    BioUpdateDoctorProfile.bioInstitution.setText("");
                    Log.e("TAG", "Do Other TASK");
                    dialogForAddingNewRegistration("Please Enter Instituation");

                }
                else {


                    boolean isMatched = false;
                    int totalViewForInstitution = BioUpdateDoctorProfile.fl_for_institution.getChildCount();
                    if (totalViewForInstitution > 0) {
                        for (int s = 0; s < totalViewForInstitution; s++) {
                            View v = BioUpdateDoctorProfile.fl_for_institution.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)) {
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched) {

                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);

                        //fl_for_reegistration,fl_for_experties;

                        BioUpdateDoctorProfile.fl_for_institution.setVisibility(View.VISIBLE);
                        BioUpdateDoctorProfile.fl_for_institution.addView(child);
                        BioUpdateDoctorProfile.bioInstitution.setText("");
                        onClickForSubInstituationsCrossTag();

                    } else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        BioUpdateDoctorProfile.bioInstitution.setText("");
                    }

                }
            }
        });
    }

    public void bioExpertiseAutoCompleteWithListener()
    {
        final CustomeAutocompleteAdapterForTags adapter3 = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_bio_update_doctor_profile, R.id.city_title, BasicInfoUpdateDocProfile.subServicesList);
        BioUpdateDoctorProfile.bioExpertise.setAdapter(adapter3);
        BioUpdateDoctorProfile.bioExpertise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                CustomeTagsGeterSeter tagsGetterSetter = (CustomeTagsGeterSeter) adapter3.getItem(i);
                String text = tagsGetterSetter.getName();
                String id = tagsGetterSetter.getId();
                String specialityId = tagsGetterSetter.getSpecialityId();
                Log.e("TAG", "Selected Name tha tha: " + text);
                Log.e("TAG", "Selected ID: " + id);


                if (id.equals("-1")){
                    BioUpdateDoctorProfile.bioExpertise.setText("");
                    Log.e("TAG", "Do Other TASK");

                    ArrayList<CitiesGetterSetter> speciality = new ArrayList<>();

                    int sizeSpecialistyArray = GetAllDoctorDetailService.specialistIdList.size();
                    Log.e("TAG", "the size of selected selected  " + sizeSpecialistyArray);
                    for (int s = 0; s < sizeSpecialistyArray; s++) {
                        CitiesGetterSetter citGS = GetAllDoctorDetailService.specialistIdList.get(s);
                        speciality.add(new CitiesGetterSetter( citGS.getId(), citGS.getName()));

                    }
                    dialogSelectSpecialtyForOther(speciality, 4); // 4 indicate this is expertiese
                }
                else {

                    boolean isMatched = false;
                    int totalViewForExperties  = BioUpdateDoctorProfile.fl_for_experties.getChildCount();
                    if (totalViewForExperties>0){
                        for (int s = 0; s<totalViewForExperties; s++){

                            View v =  BioUpdateDoctorProfile.fl_for_experties.getChildAt(s);
                            TextView ID = (TextView) v.findViewById(R.id.tv_id);
                            TextView title = (TextView) v.findViewById(R.id.tv_tag);
                            String subspecialistId = ID.getText().toString();
                            String mTitle = title.getText().toString();
                            if (mTitle.equals(text)){
                                Log.e("TAG", "Item is already Availabale");
                                isMatched = true;
                            }

                        }
                    }

                    if (!isMatched) {
                        View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                        TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                        TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                        TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                        tv_speciality_id.setText(specialityId);
                        subSpecialTitl.setText(text);
                        subSpecialID.setText(id);

                        //fl_for_reegistration,fl_for_experties;
                        BioUpdateDoctorProfile.fl_for_experties.setVisibility(View.VISIBLE);
                        BioUpdateDoctorProfile.fl_for_experties.addView(child);
                        BioUpdateDoctorProfile.bioExpertise.setText("");

                        onClickForSubExpertiseCrossTag();

                    }
                    else {
                        Toast.makeText(getActivity(), "Item is already added", Toast.LENGTH_SHORT).show();
                        BioUpdateDoctorProfile.bioExpertise.setText("");
                    }
                }
            }
        });
    }

    public void dialogSelectSpecialtyForOther(List<CitiesGetterSetter> spGC, final int indeicator)
    {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_layout_dialog_for_adding_other_item);

        final ListView lv_specialisty_selector = (ListView) dialog.findViewById(R.id.lv_specialisty_selector);
        Button bt_dialog_continue = (Button) dialog.findViewById(R.id.bt_dialog_continue);
        OtherItemAddingAdapter otherItemAddingAdapter = new OtherItemAddingAdapter(getActivity(), spGC, this);
        lv_specialisty_selector.setAdapter(otherItemAddingAdapter);
        //setting button click listener
        bt_dialog_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (mOtherItemId == null){
                    Toast.makeText(getActivity(), "Please Select Speciality", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();

                    //custome dialog user input
                    final Dialog userInputDialog =  new Dialog(getActivity());
                    userInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    userInputDialog.setContentView(R.layout.custom_layout_dialog_entring_category);

                    TextView tvDialogtitle = (TextView) userInputDialog.findViewById(R.id.tv_dialog_title);
                    final EditText etUserInput = (EditText) userInputDialog.findViewById(R.id.et_user_category);
                    Button btDialogSubmit = (Button) userInputDialog.findViewById(R.id.bt_dialog_submit);
                    if (indeicator==1) {
                        tvDialogtitle.setText("Please Enter Your Sub Speciality");
                    }
                    if (indeicator==2) {
                        tvDialogtitle.setText("Please Enter Your Sub Services");
                    }
                    if (indeicator==3) {
                        tvDialogtitle.setText("Please Enter Your Sub Qualification");
                    }
                    if (indeicator == 4){
                        tvDialogtitle.setText("Please Enter Your Sub Expertiese");
                    }
                    //onClick for dialog Submit button
                    btDialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String mUserInput = etUserInput.getText().toString();
                            if (mUserInput.length()==0){
                                Toast.makeText(getActivity(), "Field Should not be Empty", Toast.LENGTH_SHORT).show();
                                etUserInput.setError("should not be empty");
                            }
                            else {


                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                                String myId = sharedPreferences.getString("myid", null);

                                //calling api here
                                Log.e("TAG", "furrr id: " + mOtherItemId);
                                Log.e("TAG", "furrr title: " + mOtherItemTitle);
                                Log.e("TAG", "furrr userInput: " + mUserInput);
                                Log.e("TAG", "furrr My Id: " + myId);

                                //getting user id from share pref
                                userInputDialog.dismiss();




                                if (indeicator==1) {

                                    mParamTitle = "sub_speciality_title";
                                    mySpecialistId = mOtherItemId;
                                    myUserId = myId;
                                    myCatTigle = mUserInput;
                                    URL_FOR_OTHER_SERVICE =  Glob.AddingNewOtherSubSpeciality;
                                }
                                if (indeicator==2){

                                    mParamTitle = "service_title";
                                    mySpecialistId = mOtherItemId;
                                    myUserId = myId;
                                    myCatTigle = mUserInput;
                                    URL_FOR_OTHER_SERVICE =  Glob.AddingNewOtherSubServices;
                                }
                                if (indeicator == 3){

                                    mParamTitle = "qualification_title";
                                    mySpecialistId = mOtherItemId;
                                    myUserId = myId;
                                    myCatTigle = mUserInput;
                                    URL_FOR_OTHER_SERVICE =  Glob.AddingNewOtherQualification;
                                }
                                if (indeicator == 4){

                                    mParamTitle = "service_title";
                                    mySpecialistId = mOtherItemId;
                                    myUserId = myId;
                                    myCatTigle = mUserInput;
                                    URL_FOR_OTHER_SERVICE =  Glob.AddingNewOtherSubServices;
                                }


                                INDICATOR = indeicator;

                                ServiceForAddingNewRecordForOther addingNewRecordForOther = new ServiceForAddingNewRecordForOther();
                                addingNewRecordForOther.execute();
                                //addingNewItemService(myId, mOtherItemId, mUserInput);


                                mOtherItemId = null;
                                mOtherItemTitle = null;

                            }
                        }
                    });

                    userInputDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                    userInputDialog.show();
                    userInputDialog.setCancelable(true);

                }
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimatinoLeftRight;
        dialog.show();


    }//end of dialog adding new item to server

    //overding method of interface
    @Override
    public void setValues(String id, String title) {
        mOtherItemId = id;
        mOtherItemTitle = title;
    }



    class ServiceForAddingNewRecordForOther extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {

                URL url = new URL(URL_FOR_OTHER_SERVICE);

                connection = (HttpURLConnection) url.openConnection();
                // connection.setSSLSocketFactory(new ClientSSLSocketFactory(connection.getSSLSocketFactory()));
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);



                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("key", Glob.Key)
                        .appendQueryParameter("speciality_id", mySpecialistId)
                        .appendQueryParameter("doctor_id", myUserId)
                        .appendQueryParameter(mParamTitle, myCatTigle);



                String query = builder.build().getEncodedQuery().toString();

                // Open connection for sending data
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                connection.connect();

                int response_code = connection.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    Log.e("TAG", "RESULT 123 33: " + result);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());
                }
                else{

                    return("unsuccessful");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "exception";
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            hideDialog();

            Log.e("TAG", "Server Respoonse: " + result);

            if (result!=null) {
                try {
                    JSONObject jObj = new JSONObject(result);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        if (INDICATOR == 1) {
                            String sub_speciality_id = jObj.getString("sub_speciality_id");
                            String sub_speciality_title = jObj.getString("sub_speciality_title");
                            String speciality_id = jObj.getString("speciality_id");
                            Log.e("TAG", "the sub speciality id: " + sub_speciality_id);
                            Log.e("TAG", "the sub speciality title: " + sub_speciality_title);
                            Log.e("TAG", "the speciality id: " + speciality_id);

                            View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                            TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                            TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                            TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);

                            tv_speciality_id.setText(speciality_id);
                            subSpecialTitl.setText(sub_speciality_title);
                            subSpecialID.setText(sub_speciality_id);
                            ll_custom_for_supspecialist.setVisibility(View.VISIBLE);
                            ll_custom_for_supspecialist.addView(child);
                            ac_sup_specialisty.setText("");
                            onClickForSubSpecialityCrossTag();
                        }
                        if (INDICATOR == 2){

                            String sub_services_id = jObj.getString("service_id");
                            String sub_services_title = jObj.getString("service_title");
                            String speciality_id = jObj.getString("speciality_id");
                            Log.e("TAG", "the sub speciality id: " + sub_services_id);
                            Log.e("TAG", "the sub speciality title: " + sub_services_title);
                            Log.e("TAG", "the speciality id: " + speciality_id);

                            View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                            TextView subServicesTitl = (TextView) child.findViewById(R.id.tv_tag);
                            TextView subServicesID = (TextView) child.findViewById(R.id.tv_id);
                            TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                            tv_speciality_id.setText(speciality_id);
                            subServicesTitl.setText(sub_services_title);
                            subServicesID.setText(sub_services_id);

                            fl_dr_services.setVisibility(View.VISIBLE);
                            fl_dr_services.addView(child);
                            ac_sup_services.setText("");

                            onClickForSubServicesCrossTag();


                        }
                        if (INDICATOR == 3){

                            String sub_qualification_id = jObj.getString("qualification_id");
                            String sub_qualification_title = jObj.getString("qualification_title");
                            String speciality_id = jObj.getString("speciality_id");
                            Log.e("TAG", "the sub speciality id: " + sub_qualification_id);
                            Log.e("TAG", "the sub speciality title: " + sub_qualification_title);
                            Log.e("TAG", "the speciality id: " + speciality_id);

                            View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                            TextView subQualificationTitl = (TextView) child.findViewById(R.id.tv_tag);
                            TextView subQualificatoinID = (TextView) child.findViewById(R.id.tv_id);
                            TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);

                            tv_speciality_id.setText(speciality_id);
                            subQualificationTitl.setText(sub_qualification_title);
                            subQualificatoinID.setText(sub_qualification_id);

                            fl_dr_qualifications.setVisibility(View.VISIBLE);
                            fl_dr_qualifications.addView(child);
                            ac_sup_qualifications.setText("");


                            onClickForSubQualificationsCrossTag();
                        }
                        if (INDICATOR == 4){

                            String sub_services_id = jObj.getString("service_id");
                            String sub_services_title = jObj.getString("service_title");
                            String speciality_id = jObj.getString("speciality_id");
                            Log.e("TAG", "the sub speciality id: " + sub_services_id);
                            Log.e("TAG", "the sub speciality title: " + sub_services_title);
                            Log.e("TAG", "the speciality id: " + speciality_id);


                            View child = getLayoutInflater().inflate(R.layout.custom_layout_sub_specialist, null);
                            TextView subSpecialTitl = (TextView) child.findViewById(R.id.tv_tag);
                            TextView subSpecialID = (TextView) child.findViewById(R.id.tv_id);
                            TextView tv_speciality_id = (TextView) child.findViewById(R.id.tv_speciality_id);
                            tv_speciality_id.setText(speciality_id);
                            subSpecialTitl.setText(sub_services_title);
                            subSpecialID.setText(sub_services_id);

                            //fl_for_reegistration,fl_for_experties;
                            BioUpdateDoctorProfile.fl_for_experties.setVisibility(View.VISIBLE);
                            BioUpdateDoctorProfile.fl_for_experties.addView(child);
                            BioUpdateDoctorProfile.bioExpertise.setText("");

                            onClickForSubExpertiseCrossTag();

                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(getActivity(), "There is problem with server connectivity", Toast.LENGTH_SHORT).show();
            }
        }

    }//end of service for adding new record on server



    public void dialogForAddingNewRegistration(final String title)
    {
        //custome dialog user input
        final Dialog userInputDialog =  new Dialog(getActivity());
        userInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userInputDialog.setContentView(R.layout.custom_layout_dialog_entring_category);

        TextView tvDialogtitle = (TextView) userInputDialog.findViewById(R.id.tv_dialog_title);
        final EditText etUserInput = (EditText) userInputDialog.findViewById(R.id.et_user_category);
        Button btDialogSubmit = (Button) userInputDialog.findViewById(R.id.bt_dialog_submit);

        tvDialogtitle.setText(title);

        //onClick for dialog Submit button
        btDialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mUserInput = etUserInput.getText().toString();
                if (mUserInput.length()==0){
                    Toast.makeText(getActivity(), "Field Should not be Empty", Toast.LENGTH_SHORT).show();
                    etUserInput.setError("should not be empty");
                }
                else {


                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                    String myId = sharedPreferences.getString("myid", null);

                    //calling api here
                    Log.e("TAG", "furrr id: " + mOtherItemId);
                    Log.e("TAG", "furrr title: " + mOtherItemTitle);
                    Log.e("TAG", "furrr userInput: " + mUserInput);
                    Log.e("TAG", "furrr My Id: " + myId);

                    //getting user id from share pref
                    userInputDialog.dismiss();

                    ServiceForAddingNewRecordForOther addingNewRecordForOther = new ServiceForAddingNewRecordForOther();
                    //addingNewRecordForOther.execute();

                }
            }
        });

        userInputDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        userInputDialog.show();

    }

    private void dialogForClaimProfile(final String title, ArrayList<ClaimProfileGetterSetter> adList){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_claim_dialog);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(title);
        ImageView bt_cross_cancel_dialog = (ImageView)dialog.findViewById(R.id.bt_cross_cancel_dialog);
        RecyclerView rv_custom_dialog_layout = (RecyclerView) dialog.findViewById(R.id.rv_custom_dialog_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL, false);
        rv_custom_dialog_layout.setLayoutManager(linearLayoutManager);

        bt_cross_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ClaimDoctorProfileAdapter claimDoctorProfileAdapter = new ClaimDoctorProfileAdapter(getActivity(), adList, new ClaimButtonInterface() {
            @Override
            public void claimButtonClick(View v, int position, String id) {
                Log.e("TAG", "clicked doctor id is: " + id);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                String myid = sharedPreferences.getString("myid", null);
                Log.e("TAG", "clicked doctor my id: " + myid);

                cliamProfileSendingDataService(myid, id);

                dialog.dismiss();
            }
        });

        rv_custom_dialog_layout.setAdapter(claimDoctorProfileAdapter);

        //if (isNewSpeciality) {
        if (adList.size()>0) {

             /*  Window dialogWindow = dialog.getWindow();
               WindowManager.LayoutParams lp = dialogWindow.getAttributes();
               lp.height = 800;
               dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
               dialogWindow.setAttributes(lp);*/

            dialog.setCancelable(false);
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }
        // }
    }

    private void cliamProfileSendingDataService(final String myid, final String climId){

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.CLAI_PROFILE_MURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Calim Profile URL: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        String message = jObj.getString("error_message");
                        if (message.equals("Claimed Successfully")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Profiel Claim in Process");
                            alert.setIcon(android.R.drawable.ic_dialog_alert);

                            alert.setMessage("Thank You! " + etFullName.getText().toString() + " Your claim submitted succesfully, we will notify you soon, Please continue completing your profile");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();

                                }
                            });

                            alert.show();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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

        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void removingSubViesOfSpeciality(final View child, final String mSpecialistyId){

        int totalSubSpe = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();
        int totalSubSer = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();
        int totalSubQua = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();
        int totalSubExper = BioUpdateDoctorProfile.fl_for_experties.getChildCount();

        Log.e("TAg", "total sub Specialisties: " + totalSubSpe);
        Log.e("TAg", "total sub services: " + totalSubSer);
        Log.e("TAg", "total sub qualifications: " + totalSubQua);

        if (totalSubSpe > 0) {
            for (int subspe = totalSubSpe; subspe >= 0; subspe--) {

                View childSubSpe = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildAt(subspe);

                Log.e("TAG", "the the the the id total available size " + totalSubSpe);

                if (childSubSpe!=null) {
                    TextView subSpecialityId = (TextView) childSubSpe.findViewById(R.id.tv_speciality_id);
                    TextView subSpecialistyTitle = (TextView) childSubSpe.findViewById(R.id.tv_tag);

                    String subSepId = subSpecialityId.getText().toString();
                    Log.e("TAG", "the the the the id is: speciality " + mSpecialistyId);
                    Log.e("TAG", "the the the the id is: sub speiciality " + subSepId);

                    if (mSpecialistyId.equals(subSepId)) {
                        //removing sub qualificaiton
                        Log.e("TAG", "It is Equal subspecialist ");
                        BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.removeView(childSubSpe);


                    }
                }

            }
        }


        if (totalSubSer > 0){
            for (int subSer = totalSubSer; subSer >= 0; subSer--) {
                View childSubSer = BasicInfoUpdateDocProfile.fl_dr_services.getChildAt(subSer);

                if (childSubSer!=null){
                    TextView subServiceId = (TextView) childSubSer.findViewById(R.id.tv_speciality_id);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);
                    String speID = specialistId.getText().toString();
                    String subserId = subServiceId.getText().toString();

                    Log.e("TAG", "the the the the id is: speciality " + speID);
                    Log.e("TAG", "the the the the id is: services " + subserId);

                    if (mSpecialistyId.equals(subserId)) {

                        //removing sub service
                        Log.e("TAG", "It is Equal ");

                        BasicInfoUpdateDocProfile.fl_dr_services.removeView(childSubSer);

                    }
                }

            }
        }


        if (totalSubQua>0) {
            for (int subQua = totalSubQua; subQua >= 0; subQua--) {

                View childSubQua = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildAt(subQua);
                if (childSubQua!=null) {
                    TextView subQualificationId = (TextView) childSubQua.findViewById(R.id.tv_speciality_id);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    String speID = specialistId.getText().toString();
                    String subQuaId = subQualificationId.getText().toString();

                    Log.e("TAG", "the the the the id is Speciality: " + speID);
                    Log.e("TAG", "the the the the id is: Qualification" + subQuaId);

                    if (mSpecialistyId.equals(subQuaId)) {

                        //removing qualification

                        Log.e("TAG", "It is Equal ");
                        BasicInfoUpdateDocProfile.fl_dr_qualifications.removeView(childSubQua);


                    }
                }
            }
        }
        if (totalSubExper>0) {
            for (int subExp = totalSubExper; subExp >= 0; subExp--) {

                View childSubQua = BioUpdateDoctorProfile.fl_for_experties.getChildAt(subExp);
                if (childSubQua!=null) {
                    TextView subExpertiesId = (TextView) childSubQua.findViewById(R.id.tv_speciality_id);
                    TextView specialistId = (TextView) child.findViewById(R.id.tv_id);

                    String speID = specialistId.getText().toString();
                    String subExpId = subExpertiesId.getText().toString();

                    Log.e("TAG", "the the the the id is Speciality: " + speID);
                    Log.e("TAG", "the the the the id is: Qualification" + subExpId);

                    if (mSpecialistyId.equals(subExpId)) {

                        //removing qualification

                        Log.e("TAG", "It is Equal ");
                        BioUpdateDoctorProfile.fl_for_experties.removeView(childSubQua);


                    }
                }
            }
        }

        //

        //removing view tag
        ViewGroup parent = (ViewGroup) child.getParent();
        parent.removeView(child);

        int totalRemaingSpecialities = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildCount();
        Log.e("TAG", "Remaining Specialisites are " + totalRemaingSpecialities );

        if (totalRemaingSpecialities>0) {
            ArrayList<HashMap<String, String>> listOfID = new ArrayList<>();
            for (int shoaib = 0; shoaib < totalRemaingSpecialities; shoaib++) {

                View childSpecialities = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildAt(shoaib);
                TextView specialistTilt = (TextView) childSpecialities.findViewById(R.id.tv_tag);
                TextView specialistId = (TextView) childSpecialities.findViewById(R.id.tv_id);

                Log.e("TAg", "OMG this is speciality title: " + specialistTilt.getText().toString());
                Log.e("TAg", "OMG this is speciality Id: " + specialistId.getText().toString());
                String speId = specialistId.getText().toString();

                HashMap<String, String> id = new HashMap<>();
                id.put("id", speId);
                listOfID.add(id);

            }

            Log.e("TAG", "the the of array 33: " + listOfID.size());
            JSONObject prectices = new JSONObject();

            JSONArray jsonArraySpecialistID = new JSONArray(listOfID);
            try {
                prectices.put("specialities", jsonArraySpecialistID);
                BasicInfoUpdateDocProfile.specialistId = prectices.toString();
                //aa = a.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //calling weServies
            if (listOfID.size() > 0) {

                // gettingNextPageForData(aa);
                isNewSpeciality = false;
                GettingDataFromServer abc = new GettingDataFromServer();
                abc.execute();

                Log.e("TAG", "Service Calling here ");
            }

        }
        else if (totalRemaingSpecialities == 0){


            subSpecialistArrayList.clear();
            subServicesList.clear();
            subQualificationsList.clear();

            final CustomeAutocompleteAdapterForTags adapterSubSpe = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subSpecialistArrayList);
            ac_sup_specialisty.setAdapter(adapterSubSpe);

            final CustomeAutocompleteAdapterForTags adapterServices = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subServicesList);
            ac_sup_services.setAdapter(adapterServices);

            final CustomeAutocompleteAdapterForTags adapterQualifications = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subQualificationsList);
            ac_sup_qualifications.setAdapter(adapterQualifications);

            final CustomeAutocompleteAdapterForTags adapterRegistrations = new CustomeAutocompleteAdapterForTags(getActivity(), R.layout.fragment_basic_info_update_doc_profile, R.id.city_title, subServicesList);
            BioUpdateDoctorProfile.bioExpertise.setAdapter(adapterRegistrations);


        }
    }
}

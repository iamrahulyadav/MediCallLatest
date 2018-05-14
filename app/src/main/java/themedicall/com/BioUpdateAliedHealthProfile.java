package themedicall.com;

/**
 * Created by Shoaib Anwar on 31-Mar-18.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import themedicall.com.Adapter.ImageAdapter;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.Globel.Glob;
import themedicall.com.Globel.Utility;
import themedicall.com.Services.GetAllDoctorDetailService;
import themedicall.com.Services.UploadProfileImageService;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;



public class BioUpdateAliedHealthProfile extends Fragment {
    public Context activity;

    static  EditText bioAboutMe, bioAchievements , bioPublications , bioExtraCurricular, bioWorkExperience
            , checkUpDiscount , procedureDiscount , otherDiscount , writeAboutOtherPercent , writeAboutOtherWelfarePanel, et_video_url;
    static AutoCompleteTextView bioRegistration, bioInstitution, bioExpertise;
    static String bioAboutMeGetText , bioRegistrationGetText , bioInstitutionGetText , bioExpertiseGetText , bioAchievementsGetText , bioPublicationsGetText , bioExtraCurricularGetText ,
            bioWorkExperienceGetText, checkUpDiscountGetText , procedureDiscountGetText , otherDiscountGetText , writeAboutOtherPercentGetText , writeAboutOtherWelfarePanelGetText ;
    static SwitchCompat discountPackageSwitch ,welfarePanelSwitch , videoConsultationSwitch , homeCareServiceSwitch , becomeBloodDonorSwitch;
    static String discountPackageGetText = "No" ,welfarePanelGetText = "No", videoConsultationGetText = "No" , homeCareServiceGetText = "No"   , becomeBloodDonorGetText = "No" ;

    public ProgressDialog progressDialog;

    static org.apmem.tools.layouts.FlowLayout fl_for_reegistration, fl_for_institution,fl_for_experties;

    LinearLayout welfarePanelLayout ;
    static CheckBox socialMediaCheckBox , queriesAnswerCheckBox , medicalCampCheckBox , bloodCampCheckBox , healthArticleCheckBox , otherCheckBox ;
    static String socialMediaCheckBoxGetText = "No", queriesAnswerCheckBoxGetText = "No", medicalCampCheckBoxGetText = "No", bloodCampCheckBoxGetText = "No", healthArticleCheckBoxGetText = "No", otherCheckBoxGetText = "No";
    TableLayout discountTableLayout ;
    Button updateProfile, bt_exit;


    static String specialities, sub_specialities, services, qualifications, institutions, expertise, registrations, preticesListData;
    static String mDayID;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String  userChoosenTask;
    static Uri imageUri = null;
    Bitmap bitmap1;
    String timestamp1;
    public static GridView PhoneImageGrid;
    public static ImageView ivSelectMulitiPhoto;
    int Request_Code = 101;
    public static int CAMERA_CODE = 111;
    static ArrayList<CitiesGetterSetter> fetchList;
    ArrayList<CitiesGetterSetter> tempList;

    MyReceiver myReceiver;

    public BioUpdateAliedHealthProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_bio_update_doctor_profile, container, false);
        initiate(view);
        hideShowSwitchWidgets();
        getCheckBoxValue();
        showOtherDiscountPackageEditText();
        updateProfileButton();
        btExitClickListener();


        // getSpecialityList();

        settingAdapterForbioRegistration();
        settingAdapterForbiobioInstitution();
        settingAdapterForbiobioExpertise();



        return view ;
    }
    public void initiate(View view)
    {


        bioAboutMe = (EditText) view.findViewById(R.id.bioAboutMe);
        bioRegistration = (AutoCompleteTextView) view.findViewById(R.id.bioRegistration);
        bioInstitution = (AutoCompleteTextView) view.findViewById(R.id.bioInstitution);
        bioExpertise = (AutoCompleteTextView) view.findViewById(R.id.bioExpertise);
        bioAchievements = (EditText) view.findViewById(R.id.bioAchievements);
        bioPublications = (EditText) view.findViewById(R.id.bioPublications);
        bioExtraCurricular = (EditText) view.findViewById(R.id.bioExtraCurricular);
        bioWorkExperience = (EditText) view.findViewById(R.id.bioWorkExperience);
        checkUpDiscount = (EditText) view.findViewById(R.id.checkUpPercent);
        procedureDiscount = (EditText) view.findViewById(R.id.procedurePercent);
        otherDiscount = (EditText) view.findViewById(R.id.otherPercent);
        writeAboutOtherPercent = (EditText) view.findViewById(R.id.writeAboutOtherPercent);
        et_video_url = (EditText) view.findViewById(R.id.et_video_url);
        writeAboutOtherPercent.setVisibility(View.GONE);
        writeAboutOtherWelfarePanel = (EditText) view.findViewById(R.id.writeAboutOtherWelfarePanel);
        writeAboutOtherWelfarePanel.setVisibility(View.GONE);

        discountPackageSwitch = (SwitchCompat) view.findViewById(R.id.discountPackageSwitch);
        welfarePanelSwitch = (SwitchCompat) view.findViewById(R.id.welfarePanelSwitch);
        videoConsultationSwitch = (SwitchCompat) view.findViewById(R.id.videoConsultationSwitch);
        homeCareServiceSwitch = (SwitchCompat) view.findViewById(R.id.homeCareServiceSwitch);
        becomeBloodDonorSwitch = (SwitchCompat) view.findViewById(R.id.becomeBloodDonorSwitch);

        welfarePanelLayout = (LinearLayout) view.findViewById(R.id.welfarePanelLayout);
        welfarePanelLayout.setVisibility(View.GONE);

        socialMediaCheckBox = (CheckBox) view.findViewById(R.id.socialMediaCheckBox);
        queriesAnswerCheckBox = (CheckBox) view.findViewById(R.id.queriesAnswerCheckBox);
        medicalCampCheckBox = (CheckBox) view.findViewById(R.id.medicalCampCheckBox);
        bloodCampCheckBox = (CheckBox) view.findViewById(R.id.bloodCampCheckBox);
        healthArticleCheckBox = (CheckBox) view.findViewById(R.id.healthArticleCheckBox);
        otherCheckBox = (CheckBox) view.findViewById(R.id.otherCheckBox);

        discountTableLayout = (TableLayout) view.findViewById(R.id.discountTableLayout);
        discountTableLayout.setVisibility(View.GONE);

        updateProfile = (Button) view.findViewById(R.id.updateProfile);
        bt_exit = (Button) view.findViewById(R.id.bt_exit);

        progressDialog = new ProgressDialog(getActivity());


        fl_for_reegistration = (org.apmem.tools.layouts.FlowLayout) view.findViewById(R.id.fl_for_reegistration);
        fl_for_institution = (org.apmem.tools.layouts.FlowLayout) view.findViewById(R.id.fl_for_institution);
        fl_for_experties = (org.apmem.tools.layouts.FlowLayout) view.findViewById(R.id.fl_for_experties);

        ivSelectMulitiPhoto = (ImageView) view.findViewById(R.id.iv_select_muliti_photo);
        PhoneImageGrid = (GridView) view.findViewById(R.id.PhoneImageGrid);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchList= new ArrayList<>();
        tempList = new ArrayList<>();
        checkWriteExternalPermission();
        checkReadExternalStoragePermission();


        ivSelectMulitiPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callingImageSelectListener();

            }
        });

    }

    public void hideShowSwitchWidgets()
    {

        discountPackageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    discountTableLayout.setVisibility(View.VISIBLE);
                    discountPackageGetText = discountPackageSwitch.getTextOn().toString();
                    //Log.e("tag" , "discountPackageStr on " + discountPackageGetText);
                }
                else
                {
                    discountPackageGetText = discountPackageSwitch.getTextOff().toString();
                    discountTableLayout.setVisibility(View.GONE);
                    //Log.e("tag" , "discountPackageStr off " + discountPackageGetText);
                    writeAboutOtherPercent.setVisibility(View.GONE);
                    writeAboutOtherPercent.setText("");
                    checkUpDiscount.setText("");
                    procedureDiscount.setText("");
                    otherDiscount.setText("");
                }
            }
        });


        welfarePanelSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    welfarePanelLayout.setVisibility(View.VISIBLE);
                    welfarePanelGetText = welfarePanelSwitch.getTextOn().toString();



                }
                else
                {
                    writeAboutOtherWelfarePanel.setVisibility(View.GONE);
                    welfarePanelLayout.setVisibility(View.GONE);
                    socialMediaCheckBox.setChecked(false);
                    queriesAnswerCheckBox.setChecked(false);
                    medicalCampCheckBox.setChecked(false);
                    bloodCampCheckBox.setChecked(false);
                    healthArticleCheckBox.setChecked(false);
                    otherCheckBox.setChecked(false);
                    welfarePanelGetText = welfarePanelSwitch.getTextOff().toString();
                    //Log.e("tag" , "welfarePanelStr off " + welfarePanelGetText);
                }
            }
        });

        videoConsultationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    videoConsultationGetText = videoConsultationSwitch.getTextOn().toString();
                    //Log.e("tag" , "videoConsultationStr on " + videoConsultationGetText);

                }
                else
                {
                    videoConsultationGetText = videoConsultationSwitch.getTextOff().toString();
                    //Log.e("tag" , "videoConsultationStr off " + videoConsultationGetText);

                }


            }
        });


        homeCareServiceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    homeCareServiceGetText = homeCareServiceSwitch.getTextOn().toString();
                    //Log.e("tag" , "homeCareServiceStr on " + homeCareServiceGetText);

                }
                else
                {
                    homeCareServiceGetText = homeCareServiceSwitch.getTextOff().toString();
                    //Log.e("tag" , "homeCareServiceStr off " + homeCareServiceGetText);
                }

            }
        });


        becomeBloodDonorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    becomeBloodDonorGetText = becomeBloodDonorSwitch.getTextOn().toString();
                    //Log.e("tag" , "becomeBloodDonorStr on " + becomeBloodDonorGetText);

                }
                else
                {
                    becomeBloodDonorGetText = becomeBloodDonorSwitch.getTextOff().toString();
                    //Log.e("tag" , "becomeBloodDonorStr off " + becomeBloodDonorGetText);

                }
            }
        });
    }

    public void getCheckBoxValue()
    {
        socialMediaCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    socialMediaCheckBoxGetText = "Yes";
                    //Log.e("tag" , "socialMediaCheckBoxStr if " +socialMediaCheckBoxGetText);
                }
                else
                {
                    socialMediaCheckBoxGetText = "No";
                    //Log.e("tag" , "socialMediaCheckBoxStr else " +socialMediaCheckBoxGetText);
                }
            }
        });

        queriesAnswerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    queriesAnswerCheckBoxGetText = "Yes";
                    //Log.e("tag" , "queriesAnswerCheckBoxStr if " +queriesAnswerCheckBoxGetText);
                }
                else
                {
                    queriesAnswerCheckBoxGetText = "No";
                    //Log.e("tag" , "queriesAnswerCheckBoxStr else " +queriesAnswerCheckBoxGetText);
                }
            }
        });

        medicalCampCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    medicalCampCheckBoxGetText = "Yes";
                    //Log.e("tag" , "medicalCampCheckBoxStr if " +medicalCampCheckBoxGetText);
                }
                else
                {
                    medicalCampCheckBoxGetText = "No";
                    //Log.e("tag" , "medicalCampCheckBoxStr else " +medicalCampCheckBoxGetText);
                }
            }
        });


        bloodCampCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    bloodCampCheckBoxGetText = "Yes";
                    //Log.e("tag" , "bloodCampCheckBoxStr if " +bloodCampCheckBoxGetText);
                }
                else
                {
                    bloodCampCheckBoxGetText = "No";
                    //Log.e("tag" , "bloodCampCheckBoxStr else " +bloodCampCheckBoxGetText);
                }
            }
        });

        healthArticleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    healthArticleCheckBoxGetText = "Yes";
                    //Log.e("tag" , "healthArticleCheckBoxStr if " +healthArticleCheckBoxGetText);
                }
                else
                {
                    healthArticleCheckBoxGetText = "No";
                    // Log.e("tag" , "healthArticleCheckBoxStr else " +healthArticleCheckBoxGetText);
                }
            }
        });


        otherCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    writeAboutOtherWelfarePanel.setVisibility(View.VISIBLE);
                    otherCheckBoxGetText = otherCheckBox.getText().toString();
                    //Log.e("tag" , "otherCheckBoxStr if " +otherCheckBoxGetText);
                }
                else
                {
                    otherCheckBoxGetText = "";
                    //Log.e("tag" , "otherCheckBoxStr else " +otherCheckBoxGetText);
                    writeAboutOtherWelfarePanel.setVisibility(View.GONE);
                    writeAboutOtherWelfarePanel.setText("");

                }
            }
        });

    }

    public void showOtherDiscountPackageEditText()
    {


        otherDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    writeAboutOtherPercent.setVisibility(View.VISIBLE);
                }

            }

        });
    }

    public void updateProfileButton()
    {

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateProfileOnServer();
            }
        });

    }

    public void UpdateProfileOnServer(){
        bioAboutMeGetText = bioAboutMe.getText().toString();
        // bioRegistrationGetText = bioRegistration.getText().toString();
        //bioInstitutionGetText = bioInstitution.getText().toString();
        //bioExpertiseGetText = bioExpertise.getText().toString();
        bioAchievementsGetText = bioAchievements.getText().toString();
        bioPublicationsGetText = bioPublications.getText().toString();
        bioExtraCurricularGetText = bioExtraCurricular.getText().toString();
        bioWorkExperienceGetText = bioWorkExperience.getText().toString();
        checkUpDiscountGetText = checkUpDiscount.getText().toString();
        procedureDiscountGetText = procedureDiscount.getText().toString();
        otherDiscountGetText = otherDiscount.getText().toString();
        writeAboutOtherPercentGetText = writeAboutOtherPercent.getText().toString();
        writeAboutOtherWelfarePanelGetText = writeAboutOtherWelfarePanel.getText().toString();


        Log.e("tag " , "bioAboutMeGetText " + bioAboutMeGetText);
        Log.e("tag " , "bioRegistrationGetText " + bioRegistrationGetText);
        Log.e("tag " , "bioInstitutionGetText " + bioInstitutionGetText);
        Log.e("tag " , "bioExpertiseGetText " + bioExpertiseGetText);
        Log.e("tag " , "bioAchievementsGetText " + bioAchievementsGetText);
        Log.e("tag " , "bioPublicationsGetText " + bioPublicationsGetText);
        Log.e("tag " , "bioExtraCurricularGetText " + bioExtraCurricularGetText);
        Log.e("tag " , "bioWorkExperienceGetText " + bioWorkExperienceGetText);
        Log.e("tag " , "checkUpDiscountGetText " + checkUpDiscountGetText);
        Log.e("tag " , "procedureDiscountGetText " + procedureDiscountGetText);
        Log.e("tag " , "otherDiscountGetText " + otherDiscountGetText);
        Log.e("tag " , "writeAboutOtherPercentGetText " + writeAboutOtherPercentGetText);
        Log.e("tag " , "writeAboutOtherWelfarePanelGetText " + writeAboutOtherWelfarePanelGetText);


        Log.e("tag " , "discountPackageGetText " + discountPackageGetText);
        Log.e("tag " , "welfarePanelGetText " + welfarePanelGetText);
        Log.e("tag " , "videoConsultationGetText " + videoConsultationGetText);
        Log.e("tag " , "homeCareServiceGetText " + homeCareServiceGetText);
        Log.e("tag " , "becomeBloodDonorGetText " + becomeBloodDonorGetText);




        Log.e("tag " , "socialMediaCheckBoxGetText " + socialMediaCheckBoxGetText);
        Log.e("tag " , "queriesAnswerCheckBoxGetText " + queriesAnswerCheckBoxGetText);
        Log.e("tag " , "medicalCampCheckBoxGetText " + medicalCampCheckBoxGetText);
        Log.e("tag " , "bloodCampCheckBoxGetText " + bloodCampCheckBoxGetText);
        Log.e("tag " , "bloodCampCheckBoxGetText " + bloodCampCheckBoxGetText);
        Log.e("tag " , "healthArticleCheckBoxGetText " + healthArticleCheckBoxGetText);
        Log.e("tag " , "otherCheckBoxGetText " + otherCheckBoxGetText);


        //*************

        int totalviewForSpecialist = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getChildCount();
        int totalviewForSubSpecialist = BasicInfoUpdateDocProfile.ll_custom_for_supspecialist.getChildCount();
        int totalviewForServices = BasicInfoUpdateDocProfile.fl_dr_services.getChildCount();
        int totalviewForQualifiations = BasicInfoUpdateDocProfile.fl_dr_qualifications.getChildCount();
        int totalViewForRegistration = BioUpdateAliedHealthProfile.fl_for_reegistration.getChildCount();
        int totalViewForInstitution = BioUpdateAliedHealthProfile.fl_for_institution.getChildCount();
        int totalViewForExperties  = BioUpdateAliedHealthProfile.fl_for_experties.getChildCount();
        Log.e("TAG", "the Total Count of View Pager: " + UpdateAliedHelathProfile.viewPager.getAdapter().getCount());
        int pageCount = UpdateAliedHelathProfile.viewPager.getAdapter().getCount();
        int totalViewCount = 0;
        if(pageCount==2){

        }else {
            totalViewCount = PrecticeDetailUpdateAliedHealthProfile.medicineCustomRow.getChildCount();
        }

        ArrayList<HashMap<String, String>> listOfID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfSpecialistID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfSubSpecialistID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfServicesID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOQualificationsID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfRegistrationID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfInstituatinoID = new ArrayList<>();
        ArrayList<HashMap<String, String>> listOfExpertiesID = new ArrayList<>();
        ArrayList<HashMap<String, String>> precticeTimingList = new ArrayList<>();
        ArrayList<HashMap<String, JSONObject>> precticeDataRecord = new ArrayList<>();



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

            specialities = specialistIds.toString();
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

            sub_specialities  = subSpecialistIds.toString();
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

            services  = subServicesIds.toString();
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

            qualifications  = subqualificationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        if (totalViewForRegistration>0){
            for (int i = 0; i<totalViewForRegistration; i++){
                View view =  BioUpdateAliedHealthProfile.fl_for_reegistration.getChildAt(i);
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

            registrations  = subRegistrationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (totalViewForInstitution>0){
            for (int i = 0; i<totalViewForInstitution; i++){
                View view =  BioUpdateAliedHealthProfile.fl_for_institution.getChildAt(i);
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

            institutions  = subInstituationIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalViewForExperties>0){
            for (int i = 0; i<totalViewForExperties; i++){
                View view =  BioUpdateAliedHealthProfile.fl_for_experties.getChildAt(i);
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

            expertise  = subExpertiesIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        boolean allDiscountFieldsFill = true;
        for (int s = 0; s<totalViewCount; s++){
            if (precticeTimingList.size()>0){
                precticeTimingList.clear();
            }
            View views =  PrecticeDetailUpdateAliedHealthProfile.medicineCustomRow.getChildAt(s);
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
                    String startTimeIn24 = BioUpdateAliedHealthProfile.convertTo24Hour(interStarTime);
                    String endTimeIn24 = BioUpdateAliedHealthProfile.convertTo24Hour(interEndTime);
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
                String startTimeIn24 = BioUpdateAliedHealthProfile.convertTo24Hour(startTime);
                String endTimeIn24 = BioUpdateAliedHealthProfile.convertTo24Hour(endTime);
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
                BioUpdateAliedHealthProfile.preticesListData = prectices.toString();

                Log.e("TAg", "the TimingList is: " + BioUpdateAliedHealthProfile.preticesListData);

            } catch (JSONException e) {
                e.printStackTrace();
            }



            //getSpecialityList();
            GettingDataFromServer gettingDataFromServer = new GettingDataFromServer();
            gettingDataFromServer.execute();

            String drID;

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);

            drID = sharedPreferences.getString("myid", null);

            Log.e("TAG", "The size of image Array: " + fetchList.size());
            for(int i = 0; i< fetchList.size(); i++){
                CitiesGetterSetter galleryImages = fetchList.get(i);
                String filePath = galleryImages.getName().toString();
                String imageId = galleryImages.getId();
                if (imageId.equals("0")){

                    bt_get_dynamic_view_data(filePath, drID);
                }
            }

            Log.e("TAG", "The size of image Array: " + BasicInfoUpdateDocProfile.profileImagePath);
            String profileImagePath = BasicInfoUpdateDocProfile.profileImagePath;

            if (BasicInfoUpdateDocProfile.isImageLoadingFromDevice) {

                uploadingProfileImage(profileImagePath, drID);
            }


        }//end of all discount field fill check

    }



    public  void settingAdapterForbioRegistration()
    {

        bioRegistration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });
    }
    public  void settingAdapterForbiobioInstitution()
    {


    }

    public  void settingAdapterForbiobioExpertise()
    {


        bioExpertise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }






    public class GettingDataFromServer extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Please Wait ...");
//            showDialog();
            UpdateAliedHelathProfile.dialog.show();

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
                String mVideoUrl = et_video_url.getText().toString();



                if (BioUpdateAliedHealthProfile.bioWorkExperienceGetText.length()>0) {
                    double experience = Double.parseDouble(BioUpdateAliedHealthProfile.bioWorkExperienceGetText);
                    Log.e("TAg", "experience 123 "+ experience);
                    if (experience>1) {
                        BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText + " Years";
                    }
                    else {
                        BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText+" Year";
                    }
                }
                else {
                    BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText + "00";
                }

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("key", Glob.Key)
                        .appendQueryParameter("doctor_id",userID)
                        .appendQueryParameter("doctor_name",mName)
                        .appendQueryParameter("doctor_mobile",mMobile)
                        .appendQueryParameter("doctor_dob",mDob)
                        .appendQueryParameter("doctor_video",mVideoUrl)

                        .appendQueryParameter("doctor_about_me",bioAboutMeGetText)
                        .appendQueryParameter("city_id", BasicInfoUpdateDocProfile.mCityID)
                        .appendQueryParameter("doctor_blood_group", BasicInfoUpdateDocProfile.mBloodgroupID)
                        .appendQueryParameter("doctor_offer_any_discount",discountPackageGetText)
                        .appendQueryParameter("doctor_experience",bioWorkExperienceGetText)
                        .appendQueryParameter("experience_status_id", BasicInfoUpdateDocProfile.mStatusID)
                        .appendQueryParameter("doctor_is_blood_donor",becomeBloodDonorGetText)
                        .appendQueryParameter("doctor_discount_for_check_up",checkUpDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_procedure",procedureDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_other",otherDiscountGetText)
                        .appendQueryParameter("doctor_detail_for_other_discount",writeAboutOtherPercentGetText)
                        .appendQueryParameter("doctor_want_to_join_medicall_welfare_panel",welfarePanelGetText)
                        .appendQueryParameter("doctor_social_media_awareness",socialMediaCheckBoxGetText)
                        .appendQueryParameter("doctor_queries_answered",queriesAnswerCheckBoxGetText)
                        .appendQueryParameter("doctor_medical_camp",medicalCampCheckBoxGetText)
                        .appendQueryParameter("doctor_blood_camp",bloodCampCheckBoxGetText)
                        .appendQueryParameter("doctor_health_article",healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_other_activity_for_medicall",healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_available_for_video_consultation",videoConsultationGetText)
                        .appendQueryParameter("doctor_available_for_home_care_service",homeCareServiceGetText)
                        .appendQueryParameter("doctor_publications", bioPublicationsGetText)
                        .appendQueryParameter("doctor_extra_curricular_activities", bioExtraCurricularGetText)
                        //listing
                        .appendQueryParameter("practices", preticesListData)
                        .appendQueryParameter("specialities",specialities)
                        .appendQueryParameter("sub_specialities",sub_specialities)
                        .appendQueryParameter("services",services)
                        .appendQueryParameter("qualifications",qualifications)
                        .appendQueryParameter("institutions",institutions)
                        .appendQueryParameter("expertise",expertise)
                        .appendQueryParameter("registrations",registrations)
                        .appendQueryParameter("achievements",bioAchievementsGetText);



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

            UpdateAliedHelathProfile.dialog.dismiss();

            Log.e("TAG", "Server Respoonse: " + result);

            if (result!=null) {
                try {


                    JSONObject jObj = new JSONObject(result);


                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        // Toast.makeText(getActivity(), " You are successfully Added!", Toast.LENGTH_SHORT).show();
                        String errorMsg = jObj.getString("error_message");
                        if (errorMsg.equals("Updated Successfully")){

                            updateProfileCompletedDialog();
                            Log.e("tag" , "update profile");

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
                UpdateAliedHelathProfile.dialog.dismiss();

            }
        }

    }//end of GettingDataFromServer


    public void updateProfileCompletedDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Update Successfully!");
        alert.setMessage("Thank you " + BasicInfoUpdateDocProfile.etFullName.getText().toString() + " Your Profile Updated Successfully");

        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getActivity().finish();
            }
        });
        alert.setCancelable(false);
        alert.show();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userfullname", BasicInfoUpdateDocProfile.etFullName.getText().toString());
        editor.commit();
    }


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
                            UpdateAliedHelathProfile.dialog.dismiss();
                            Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            UpdateAliedHelathProfile.dialog.dismiss();
                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e("TAG", "the response from server for upload image: " + serverResponse.getBodyAsString());

                            if (responseFromServer!=null) {
                                try {

                                    JSONObject jObj = new JSONObject(responseFromServer);

                                    boolean error = jObj.getBoolean("error");

                                    if (!error) {

                                        String image_url = jObj.getString("image_url");
                                        Log.e("TAG", "the uploaded image response is: " + image_url);

                                        updateProfileCompletedDialog();


                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("profile_img", image_url);
                                        editor.commit();

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

                    UpdateAliedHelathProfile.dialog.show();


        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

    public static String convertTo24Hour(String Time) {


        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;

    }

    public void btExitClickListener()
    {
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });
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

        if (requestCode == CAMERA_CODE) {

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


    }

    public void callingImageSelectListener(){

        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity());
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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Select Photo From Gallery",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
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


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            if (datapassed == 1) {

                Log.e("TAg", "Gallery Image Size: " + GetAllDoctorDetailService.listGallaryImages.size());


                for(CitiesGetterSetter gSG : GetAllDoctorDetailService.listGallaryImages) {

                    String galleryImageUrlPath = gSG.getName().toString();
                    final String imageId = gSG.getId();
                    String finalPath = Glob.IMAGE_BACK_URL + galleryImageUrlPath;
                    Log.e("TAG", "Gallery Image: " + finalPath);

                    Glide
                            .with(getActivity())
                            .load(finalPath)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>(300, 300) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    Log.e("TAG", "The bitmap: " + resource);
                                    //Uri imageURIFromBitmap = getImageUri(getActivity(), resource);
                                    String imageURIFromBitmap = bitmapToUriConverter(resource);
                                    Log.e("TAG", "the image path of server image: " + imageURIFromBitmap);

                                    fetchList.add(new CitiesGetterSetter(imageId, imageURIFromBitmap));
                                    settingImageToAdpter(fetchList);
                                    Log.e("TAG", "Feteched Array List Size 1: " + fetchList.size());

                                }
                            });
                }

            }

        }
    }


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

}

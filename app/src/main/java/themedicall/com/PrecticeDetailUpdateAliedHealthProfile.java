package themedicall.com;

/**
 * Created by Shoaib Anwar on 31-Mar-18.
 */


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.CustomeAutoCompleteAdapter;
import themedicall.com.Adapter.HospitalFilterAdapterForPrecticeUpdateDoc;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.GetterSetter.HospitalSearchFilterGetterSetter;
import themedicall.com.Globel.Glob;

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
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class PrecticeDetailUpdateAliedHealthProfile extends Fragment {

    public static Button btAdmore;
    public static LinearLayout medicineCustomRow;
    public static ImageView iv_delete_item;
    public static AutoCompleteTextView editTextBig;
    public static FrameLayout framelayout;
    public static Button bt_select_day;
    public static TextView tv_hospital_id;
    public Button btExit, btSave;
    static String mDayID;
    ProgressDialog progressDialog;
    MyReceiver myReceiver;

    Button bt_getValues;
    Dialog myHospitalAutoCompleteDialog;

    ArrayList<HospitalSearchFilterGetterSetter> HospitalNameFilter ;
    private Timer timer;
    JSONObject object;
    JSONArray hospitalArray;
    String  hospital_id , hospital_name  , hospital_address;

    SwitchCompat discountPackageSwitch;
    TableLayout discountTableLayout;
    EditText writeAboutOtherPercent;
    EditText checkUpDiscount;
    EditText procedureDiscount;
    EditText otherDiscount;
    private int timerHandler = 2;
    Handler mHandler;
    private String textToSearch;
    AutoCompleteTextView autoCompleteTextView ;
    TextView hospId ;
    ProgressBar precticeProgress;

    public PrecticeDetailUpdateAliedHealthProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_prectice_detail_update_doc_profile, container, false);
        btAdmore = (Button) view.findViewById(R.id.addFieldBtn);
        medicineCustomRow = (LinearLayout) view.findViewById(R.id.medicineCustomRow);
        iv_delete_item = (ImageView) view.findViewById(R.id.iv_delete_item);
        editTextBig = (AutoCompleteTextView) view.findViewById(R.id.editText);
        framelayout = (FrameLayout) view.findViewById(R.id.framelayout);
        bt_select_day = (Button) view.findViewById(R.id.bt_select_day);
        bt_select_day.setText("Monday");
        tv_hospital_id = (TextView) view.findViewById(R.id.tv_hospital_id);
        btExit = (Button) view.findViewById(R.id.bt_exit);
        btSave = (Button) view.findViewById(R.id.updateProfile);
        framelayout = (FrameLayout) view.findViewById(R.id.framelayout);
        precticeProgress = (ProgressBar) view.findViewById(R.id.precticeProgress);

        HospitalNameFilter = new ArrayList<HospitalSearchFilterGetterSetter>();


        discountPackageSwitch = (SwitchCompat) view.findViewById(R.id.discountPackageSwitch);
        discountTableLayout = (TableLayout) view.findViewById(R.id.discountTableLayout);
        discountTableLayout.setVisibility(View.GONE);
        writeAboutOtherPercent = (EditText) view.findViewById(R.id.writeAboutOtherPercent) ;
        writeAboutOtherPercent.setVisibility(View.GONE);
        checkUpDiscount = (EditText) view.findViewById(R.id.checkUpPercent);
        procedureDiscount = (EditText) view.findViewById(R.id.procedurePercent);
        otherDiscount = (EditText) view.findViewById(R.id.otherPercent);

        hideShowSwitchWidgets(discountPackageSwitch, discountTableLayout, checkUpDiscount, procedureDiscount, otherDiscount, writeAboutOtherPercent);
        showOtherDiscountPackageEditText(otherDiscount, writeAboutOtherPercent);

        /*bt_getValues = (Button) view.findViewById(R.id.bt_getValues);*/



     /*   btAdmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddFieldLab(view);
            }
        });*/


        // final CustomeAutoCompleteAdapter adapter = new CustomeAutoCompleteAdapter(getActivity(), R.layout.fragment_prectice_detail_update_doc_profile, R.id.city_title, GettingAllHospitalListService.hospitalList);
        // editText.setAdapter(adapter);

        //addListInAutompleteHospitalName(adapter);

        // hospitalNameTextChangeListener(editText, tv_hospital_id);
        btExitClickListener();
        addTitmingButtoncClickHanlder();
        removeTitmingButtoncClickHanlder();
        setClickForAutoCompleForHospitalNameFilter(editTextBig, tv_hospital_id);


        //gettingAllValuesFromViewTest();

        return view;
    }


    public void addListInAutompleteHospitalName(final CustomeAutoCompleteAdapter adapter)
    {

        //setting OnItemclick
        editTextBig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter) adapter.getItem(i);
                String text = citiesGetterSetter.getName();
                String id = citiesGetterSetter.getId();
                Log.e("TAG", "Selected Name: " + text);
                Log.e("TAG", "Hospital Selected ID: " + id);
                if (id.equals("-1")){

                    AlertDialog.Builder alertAddingNewHospital = new AlertDialog.Builder(getActivity());
                    alertAddingNewHospital.setTitle("Add New Work place!");
                    alertAddingNewHospital.setIcon(android.R.drawable.ic_dialog_alert);
                    alertAddingNewHospital.setMessage("Do you want add your work place.");
                    alertAddingNewHospital.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent createNewHospitalActivity = new Intent(getActivity(), MapActivityForSelectingHospital.class);
                            startActivity(createNewHospitalActivity);
                        }
                    });
                    alertAddingNewHospital.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertAddingNewHospital.show();
                    editTextBig.setText("");

                }else {
                    tv_hospital_id.setText(id);
                }

            }

        });
    }

    public void addingDymanicView(int totalViewSize, ArrayList<ArrayList<HashMap<String, String>>> precticeListData) {


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //final View rowView = inflater.inflate(R.layout.custom_docter_update_practice_fragment, null);

        //removing all views befor adding new views

        if (totalViewSize > 0){
            PrecticeDetailUpdateAliedHealthProfile.medicineCustomRow.removeAllViews();
        }

        for (int s = 0; s<totalViewSize; s++){
            // Add the new row before the add field button.

            View rowView = inflater.inflate(R.layout.custom_docter_update_practice_fragment, null);

            final AutoCompleteTextView editText = (AutoCompleteTextView) rowView.findViewById(R.id.editText);

            //*******************************************************************************************************************************************************

            EditText et_startTime = (EditText) rowView.findViewById(R.id.et_startTime);
            final TextView tv_hospital_id = (TextView) rowView.findViewById(R.id.tv_hospital_id);
            EditText et_endtime = (EditText) rowView.findViewById(R.id.et_endtime);
            EditText et_minprice = (EditText) rowView.findViewById(R.id.et_minprice);
            EditText et_maxprice = (EditText) rowView.findViewById(R.id.et_maxprice);
            Button bt_select_day = (Button) rowView.findViewById(R.id.bt_select_day);
            SwitchCompat discountPackageSwitch = (SwitchCompat) rowView.findViewById(R.id.discountPackageSwitch);
            TableLayout discountTableLayout = (TableLayout) rowView.findViewById(R.id.discountTableLayout);
            discountTableLayout.setVisibility(View.GONE);
            EditText writeAboutOtherPercent = (EditText) rowView.findViewById(R.id.writeAboutOtherPercent) ;
            writeAboutOtherPercent.setVisibility(View.GONE);

            EditText checkUpDiscount = (EditText) rowView.findViewById(R.id.checkUpPercent);
            EditText procedureDiscount = (EditText) rowView.findViewById(R.id.procedurePercent);
            EditText otherDiscount = (EditText) rowView.findViewById(R.id.otherPercent);

            hideShowSwitchWidgets(discountPackageSwitch, discountTableLayout, checkUpDiscount, procedureDiscount, otherDiscount, writeAboutOtherPercent);
            showOtherDiscountPackageEditText(otherDiscount, writeAboutOtherPercent);


            setClickForAutoCompleForHospitalNameFilter(editText, tv_hospital_id);


            ImageView iv_delete_item = (ImageView) rowView.findViewById(R.id.iv_delete_item);

            String hospitalId = precticeListData.get(s).get(0).get("hospital_id");
            String hospitalName = precticeListData.get(s).get(0).get("hospital_name");
            String doctorMinFee = precticeListData.get(s).get(0).get("doctor_hospital_min_fee");
            String doctorMaxFee = precticeListData.get(s).get(0).get("doctor_hospital_max_fee");
            //extracting discount data
            String doctor_offer_any_discount = precticeListData.get(s).get(0).get("doctor_offer_any_discount");
            String doctor_discount_for_check_up = precticeListData.get(s).get(0).get("doctor_discount_for_check_up");
            String doctor_discount_for_procedure = precticeListData.get(s).get(0).get("doctor_discount_for_procedure");
            String doctor_discount_for_other = precticeListData.get(s).get(0).get("doctor_discount_for_other");
            String doctor_detail_for_other_discount = precticeListData.get(s).get(0).get("doctor_detail_for_other_discount");




            Log.e("TAG", "timing of doctor " + hospitalId);
            Log.e("TAG", "timing of doctor " + hospitalName);
            String firstStartime = precticeListData.get(s).get(0).get("doctor_timing_start_time");
            String firstEndtime = precticeListData.get(s).get(0).get("doctor_timing_end_time");
            String firstWeekDayId = precticeListData.get(s).get(0).get("week_day_id");


            if (firstWeekDayId.equals("1")){firstWeekDayId = "Monday";}
            if (firstWeekDayId.equals("2")){firstWeekDayId = "Tuesday";}
            if (firstWeekDayId.equals("3")){firstWeekDayId = "Wednesday";}
            if (firstWeekDayId.equals("4")){firstWeekDayId = "Thursday";}
            if (firstWeekDayId.equals("5")){firstWeekDayId = "Friday";}
            if (firstWeekDayId.equals("6")){firstWeekDayId = "Saturday";}
            if (firstWeekDayId.equals("7")){firstWeekDayId = "Sunday";}

            firstStartime = timeFormteIn12Hr(firstStartime);
            firstEndtime = timeFormteIn12Hr(firstEndtime);


            tv_hospital_id.setText(hospitalId);
            editText.setText(hospitalName);
            et_startTime.setText(firstStartime);
            et_endtime.setText(firstEndtime);
            et_minprice.setText(doctorMinFee);
            et_maxprice.setText(doctorMaxFee);
            //for discount views
            if (doctor_offer_any_discount.equals("Yes")){
                discountPackageSwitch.setChecked(true);
                checkUpDiscount.setText(doctor_discount_for_check_up);
                procedureDiscount.setText(doctor_discount_for_procedure);
                otherDiscount.setText(doctor_discount_for_other);
                if (otherDiscount.getText().length()!=0){
                    writeAboutOtherPercent.setVisibility(View.VISIBLE);
                    writeAboutOtherPercent.setText(doctor_detail_for_other_discount);
                }



            }
            bt_select_day.setText(firstWeekDayId);

            //iv_delete_item.setText(precticeListData.get(s).get("week_day_id"));
            PrecticeDetailUpdateAliedHealthProfile.medicineCustomRow.addView(rowView, PrecticeDetailUpdateAliedHealthProfile.medicineCustomRow.getChildCount()-1);

            //hospitalNameTextChangeListener(editText, tv_hospital_id);



            final LinearLayout ll_prectice_day_time = (LinearLayout) rowView.findViewById(R.id.ll_prectice_day_time);

            if (precticeListData.get(s).size()>1){
                Log.e("TAg", "the ospital Namee: " + hospitalName);

                for (int j = 1; j<precticeListData.get(s).size(); j++) {
                    View timingView = getLayoutInflater().inflate(R.layout.custom_layout_add_timings, null);
                    ll_prectice_day_time.addView(timingView);

                    String doctorStarttime = precticeListData.get(s).get(j).get("doctor_timing_start_time");
                    String doctorEndtime = precticeListData.get(s).get(j).get("doctor_timing_end_time");
                    String weekDayId = precticeListData.get(s).get(j).get("week_day_id");

                    EditText inner_et_satrtTime = (EditText) timingView.findViewById(R.id.et_startTime);
                    EditText inner_et_endtime = (EditText) timingView.findViewById(R.id.et_endtime);
                    Button inner_bt_select_day = (Button) timingView.findViewById(R.id.bt_select_day);

                    doctorStarttime = timeFormteIn12Hr(doctorStarttime);
                    doctorEndtime = timeFormteIn12Hr(doctorEndtime);

                    if (weekDayId.equals("1")){weekDayId = "Monday";}
                    if (weekDayId.equals("2")){weekDayId = "Tuesday";}
                    if (weekDayId.equals("3")){weekDayId = "Wednesday";}
                    if (weekDayId.equals("4")){weekDayId = "Thursday";}
                    if (weekDayId.equals("5")){weekDayId = "Friday";}
                    if (weekDayId.equals("6")){weekDayId = "Saturday";}
                    if (weekDayId.equals("7")){weekDayId = "Sunday";}

                    inner_et_satrtTime.setText(doctorStarttime);
                    inner_et_endtime.setText(doctorEndtime);
                    inner_bt_select_day.setText(weekDayId);

                    Log.e("TAG", "timing of doctor start: " + doctorStarttime);
                    Log.e("TAG", "timing of doctor end: " + doctorEndtime);
                    Log.e("TAG", "timing of doctor weekday id: " + weekDayId);
                }

            }
        }

        addTitmingButtoncClickHanlder();

    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            if (datapassed == 1) {

                Log.e("TAg", "the size of PrecticeDetail List is: " + GetAllDoctorDetailService.precticeDataList.size());
                if (GetAllDoctorDetailService.precticeDataList.size()>0) {

                    addingDymanicView(GetAllDoctorDetailService.precticeDataList.size(), GetAllDoctorDetailService.precticeDataList);
                  /*  final CustomeAutoCompleteAdapter adapter = new CustomeAutoCompleteAdapter(getActivity(), R.layout.fragment_prectice_detail_update_doc_profile, R.id.city_title, GettingAllHospitalListService.hospitalList);
                    editText.setAdapter(adapter);*/

                    settingAdatper();

                }

                onDeletViewClickListner();
                btSaveClickListener();
                addTitmingButtoncClickHanlder();
                removeTitmingButtoncClickHanlder();


            }
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

    public String timeFormteIn12Hr(String time){

        DateFormat f1 = new SimpleDateFormat("kk:mm");
        Date d = null;
        try {
            d = f1.parse(time);
            DateFormat f2 = new SimpleDateFormat("h:mm a");
            time = f2.format(d).toUpperCase(); // "12:18am"

        } catch (ParseException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;

    }


    public void onDeletViewClickListner(){

        final int totalViewCount = medicineCustomRow.getChildCount();

        for (int i = 0; i<totalViewCount; i++){
            final View child = medicineCustomRow.getChildAt(i);
            ImageView ivDelet = (ImageView) child.findViewById(R.id.iv_delete_item);
            ivDelet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int tVies = medicineCustomRow.getChildCount();

                    child.postInvalidate();

                    if (tVies == 1) {
                        Toast.makeText(getActivity(), "No More Item to Remove", Toast.LENGTH_SHORT).show();
                    } else {
                        ViewGroup parent = (ViewGroup) child.getParent();
                        parent.removeView(child);



                        parent.invalidate();
                        medicineCustomRow.invalidate();
                        medicineCustomRow.postInvalidate();
                        btAdmore.postInvalidate();
                        btAdmore.invalidate();



                    }

                }
            });
        }
    }

    //add_remove_timing_view_click_handling
    public void addTitmingButtoncClickHanlder()
    {

        final int totalViewCount = medicineCustomRow.getChildCount();



        for (int i = 0; i<totalViewCount; i++){
            final View child = medicineCustomRow.getChildAt(i);
            final LinearLayout ll_prectice_day_time = (LinearLayout) child.findViewById(R.id.ll_prectice_day_time);
            ImageView addTiming = (ImageView) child.findViewById(R.id.iv_add_timing);


            //click listner for add timing
            addTiming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("TAg", "the image is clicked");

                    View timingView = getLayoutInflater().inflate(R.layout.custom_layout_add_timings, null);
                    ll_prectice_day_time.addView(timingView);

                    final int totalViewForRemoveTiming = ll_prectice_day_time.getChildCount();
                    if (totalViewForRemoveTiming>1) {

                        final View lldaytimePrev = ll_prectice_day_time.getChildAt(totalViewForRemoveTiming-2);
                        final View lldaytimeNext = ll_prectice_day_time.getChildAt(totalViewForRemoveTiming-1);
                        final Button btSelectDayPrev = (Button) lldaytimePrev.findViewById(R.id.bt_select_day);
                        final Button btSelectDayNext = (Button) lldaytimeNext.findViewById(R.id.bt_select_day);
                        final EditText et_startTimePreve = (EditText) lldaytimePrev.findViewById(R.id.et_startTime);
                        final EditText et_endtimePreve = (EditText) lldaytimePrev.findViewById(R.id.et_endtime);
                        final EditText et_startTimeNext = (EditText) lldaytimeNext.findViewById(R.id.et_startTime);
                        final EditText et_endtimeNext = (EditText) lldaytimeNext.findViewById(R.id.et_endtime);

                        if ( btSelectDayPrev.getText().toString().equals("Monday")){btSelectDayNext.setText("Tuesday");}
                        if ( btSelectDayPrev.getText().toString().equals("Tuesday")){btSelectDayNext.setText("Wednesday");}
                        if ( btSelectDayPrev.getText().toString().equals("Wednesday")){btSelectDayNext.setText("Thursday");}
                        if ( btSelectDayPrev.getText().toString().equals("Thursday")){btSelectDayNext.setText("Friday");}
                        if ( btSelectDayPrev.getText().toString().equals("Friday")){btSelectDayNext.setText("Saturday");}
                        if ( btSelectDayPrev.getText().toString().equals("Saturday")){btSelectDayNext.setText("Sunday");}
                        if ( btSelectDayPrev.getText().toString().equals("Sunday")){btSelectDayNext.setText("Monday");}

                        String preStartTime = et_startTimePreve.getText().toString();
                        String prevEndtime = et_endtimePreve.getText().toString();
                        et_startTimeNext.setText(preStartTime);
                        et_endtimeNext.setText(prevEndtime);


                    }

                    removeTitmingButtoncClickHanlder();
                    addTitmingButtoncClickHanlder();


                }
            });
        }

    }

    public void removeTitmingButtoncClickHanlder()
    {

        final int totalViewCount = medicineCustomRow.getChildCount();

        for (int i = 0; i<totalViewCount; i++){
            final View child = medicineCustomRow.getChildAt(i);
            final LinearLayout ll_prectice_day_time = (LinearLayout) child.findViewById(R.id.ll_prectice_day_time);
            final int totalViewForRemoveTiming = ll_prectice_day_time.getChildCount();

            if (totalViewForRemoveTiming>1) {
                for (int s = 0; s<totalViewForRemoveTiming; s++){
                    final View timingView = ll_prectice_day_time.getChildAt(s);
                    final ImageView ivRemoveTiming = (ImageView) timingView.findViewById(R.id.iv_remove_timing);
                    ivRemoveTiming.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //removing view tag
                            ViewGroup parent = (ViewGroup) timingView.getParent();
                            parent.removeView(timingView);
                            Log.e("TAG", "Total views in relative layout: " + totalViewForRemoveTiming);

                            medicineCustomRow.invalidate();
                            medicineCustomRow.postInvalidate();
                            btAdmore.postInvalidate();
                            btAdmore.invalidate();


                        }
                    });
                }
            }
        }

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


    public void btExitClickListener()
    {
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();
            }
        });
    }

    //

    public void UpdateProfileOnServer()
    {
        BioUpdateAliedHealthProfile.bioAboutMeGetText = BioUpdateAliedHealthProfile.bioAboutMe.getText().toString();
        BioUpdateAliedHealthProfile.bioAchievementsGetText = BioUpdateAliedHealthProfile.bioAchievements.getText().toString();
        BioUpdateAliedHealthProfile.bioPublicationsGetText = BioUpdateAliedHealthProfile.bioPublications.getText().toString();
        BioUpdateAliedHealthProfile.bioExtraCurricularGetText = BioUpdateAliedHealthProfile.bioExtraCurricular.getText().toString();
        BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperience.getText().toString();
        BioUpdateAliedHealthProfile.checkUpDiscountGetText = BioUpdateAliedHealthProfile.checkUpDiscount.getText().toString();
        BioUpdateAliedHealthProfile.procedureDiscountGetText = BioUpdateAliedHealthProfile.procedureDiscount.getText().toString();
        BioUpdateAliedHealthProfile.otherDiscountGetText = BioUpdateAliedHealthProfile.otherDiscount.getText().toString();
        BioUpdateAliedHealthProfile.writeAboutOtherPercentGetText = BioUpdateAliedHealthProfile.writeAboutOtherPercent.getText().toString();
        BioUpdateAliedHealthProfile.writeAboutOtherWelfarePanelGetText = BioUpdateAliedHealthProfile.writeAboutOtherWelfarePanel.getText().toString();


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

            BioUpdateAliedHealthProfile.specialities = specialistIds.toString();
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

            BioUpdateAliedHealthProfile.sub_specialities  = subSpecialistIds.toString();
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

            BioUpdateAliedHealthProfile.services  = subServicesIds.toString();
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

            BioUpdateAliedHealthProfile.qualifications  = subqualificationIds.toString();
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

            BioUpdateAliedHealthProfile.registrations  = subRegistrationIds.toString();
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

            BioUpdateAliedHealthProfile.institutions  = subInstituationIds.toString();
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
            BioUpdateAliedHealthProfile.expertise  = subExpertiesIds.toString();
            //aa = a.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }



        boolean allDiscountFieldsFill = true;

        for (int s = 0; s<totalViewCount; s++){
            if (precticeTimingList.size()>0){
                precticeTimingList.clear();
            }
            View views =  medicineCustomRow.getChildAt(s);
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



            //
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
                    Log.e("TAG", "the time in 24 is end: " + startTimeIn24);

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

            Log.e("TAg", "Hospital id is : " + hospitalId);

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

            }
        }//end of for loop



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
            SendingDataToServer sendingDataToServer = new SendingDataToServer();
            sendingDataToServer.execute();

            String drID;

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);

            drID = sharedPreferences.getString("myid", null);


            Log.e("TAG", "The size of image Array: " + BioUpdateAliedHealthProfile.fetchList.size());
            for (int i = 0; i < BioUpdateAliedHealthProfile.fetchList.size(); i++) {
                CitiesGetterSetter galleryImages = BioUpdateAliedHealthProfile.fetchList.get(i);
                String filePath = galleryImages.getName().toString();

                String imageId = galleryImages.getId();
                if (imageId.equals("0")) {
                    bt_get_dynamic_view_data(filePath, drID);
                }
            }

            //uploadingProfileImage(BasicInfoUpdateDocProfile.profileImagePath, drID);
            if (BasicInfoUpdateDocProfile.isImageLoadingFromDevice) {
                uploadingProfileImage(BasicInfoUpdateDocProfile.profileImagePath, drID);
            }

        }//end of all discount field fill check

    }//end of update profile button



    public class SendingDataToServer extends AsyncTask<String , Void ,String> {
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
                String mVideoUrl = BioUpdateAliedHealthProfile.et_video_url.getText().toString();

                if (BioUpdateAliedHealthProfile.bioWorkExperienceGetText.length()>0) {
                    double experience = Double.parseDouble(BioUpdateAliedHealthProfile.bioWorkExperienceGetText);
                    Log.e("TAg", "experience 123 "+ experience);
                    if (experience>1){
                        BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText+" Years";
                    }
                    else {
                        BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText+" Year";
                    }
                }else {
                    BioUpdateAliedHealthProfile.bioWorkExperienceGetText = BioUpdateAliedHealthProfile.bioWorkExperienceGetText + "00";
                }

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("key", Glob.Key)
                        .appendQueryParameter("doctor_id",userID)
                        .appendQueryParameter("doctor_name",mName)
                        .appendQueryParameter("doctor_mobile",mMobile)
                        .appendQueryParameter("doctor_dob",mDob)
                        .appendQueryParameter("doctor_video",mVideoUrl)
                        .appendQueryParameter("doctor_about_me",BioUpdateAliedHealthProfile.bioAboutMeGetText)
                        .appendQueryParameter("city_id", BasicInfoUpdateDocProfile.mCityID)
                        .appendQueryParameter("doctor_blood_group", BasicInfoUpdateDocProfile.mBloodgroupID)
                        .appendQueryParameter("doctor_offer_any_discount",BioUpdateAliedHealthProfile.discountPackageGetText)
                        .appendQueryParameter("doctor_experience",BioUpdateAliedHealthProfile.bioWorkExperienceGetText)
                        .appendQueryParameter("experience_status_id", BasicInfoUpdateDocProfile.mStatusID)
                        .appendQueryParameter("doctor_is_blood_donor",BioUpdateAliedHealthProfile.becomeBloodDonorGetText)
                        .appendQueryParameter("doctor_discount_for_check_up",BioUpdateAliedHealthProfile.checkUpDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_procedure",BioUpdateAliedHealthProfile.procedureDiscountGetText)
                        .appendQueryParameter("doctor_discount_for_other",BioUpdateAliedHealthProfile.otherDiscountGetText)
                        .appendQueryParameter("doctor_detail_for_other_discount",BioUpdateAliedHealthProfile.writeAboutOtherPercentGetText)
                        .appendQueryParameter("doctor_want_to_join_medicall_welfare_panel",BioUpdateAliedHealthProfile.welfarePanelGetText)
                        .appendQueryParameter("doctor_social_media_awareness",BioUpdateAliedHealthProfile.socialMediaCheckBoxGetText)
                        .appendQueryParameter("doctor_queries_answered",BioUpdateAliedHealthProfile.queriesAnswerCheckBoxGetText)
                        .appendQueryParameter("doctor_medical_camp",BioUpdateAliedHealthProfile.medicalCampCheckBoxGetText)
                        .appendQueryParameter("doctor_blood_camp",BioUpdateAliedHealthProfile.bloodCampCheckBoxGetText)
                        .appendQueryParameter("doctor_health_article",BioUpdateAliedHealthProfile.healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_other_activity_for_medicall",BioUpdateAliedHealthProfile.healthArticleCheckBoxGetText)
                        .appendQueryParameter("doctor_available_for_video_consultation",BioUpdateAliedHealthProfile.videoConsultationGetText)
                        .appendQueryParameter("doctor_available_for_home_care_service",BioUpdateAliedHealthProfile.homeCareServiceGetText)
                        .appendQueryParameter("doctor_publications", BioUpdateAliedHealthProfile.bioPublicationsGetText)
                        .appendQueryParameter("doctor_extra_curricular_activities", BioUpdateAliedHealthProfile.bioExtraCurricularGetText)
                        //listing
                        .appendQueryParameter("practices", BioUpdateAliedHealthProfile.preticesListData)
                        .appendQueryParameter("specialities",BioUpdateAliedHealthProfile.specialities)
                        .appendQueryParameter("sub_specialities",BioUpdateAliedHealthProfile.sub_specialities)
                        .appendQueryParameter("services",BioUpdateAliedHealthProfile.services)
                        .appendQueryParameter("qualifications",BioUpdateAliedHealthProfile.qualifications)
                        .appendQueryParameter("institutions",BioUpdateAliedHealthProfile.institutions)
                        .appendQueryParameter("expertise",BioUpdateAliedHealthProfile.expertise)
                        .appendQueryParameter("registrations",BioUpdateAliedHealthProfile.registrations)
                        .appendQueryParameter("achievements",BioUpdateAliedHealthProfile.bioAchievementsGetText);



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
                            // alert.show();

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
                        Toast.makeText(getActivity(), "Server Not Responding", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                UpdateAliedHelathProfile.dialog.dismiss();

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

                            UpdateAliedHelathProfile.dialog.dismiss();
                            Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
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
    }//end of upload photo


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    //



    @Override
    public void onResume() {
        super.onResume();

    }

    public void settingAdatper()
    {
        // if (GettingAllHospitalListService.hospitalList.size()>0){

        if (medicineCustomRow.getChildCount()>1){

            for (int i = 0; i<medicineCustomRow.getChildCount(); i++) {

                View view =  medicineCustomRow.getChildAt(i);
                final AutoCompleteTextView editText = (AutoCompleteTextView) view.findViewById(R.id.editText);
                final TextView hostId = (TextView) view.findViewById(R.id.tv_hospital_id);


                setClickForAutoCompleForHospitalNameFilter(editText, hostId);


                //final CustomeAutoCompleteAdapter adapter = new CustomeAutoCompleteAdapter(getActivity(), R.layout.fragment_prectice_detail_hospitals, R.id.city_title, GettingAllHospitalListService.hospitalList);


                //hospitalNameTextChangeListener(editText, hostId);
                // editText.setAdapter(adapter);


                   /* editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            CitiesGetterSetter citiesGetterSetter = (CitiesGetterSetter)adapter.getItem(i);
                            String text = citiesGetterSetter.getName();
                            String id = citiesGetterSetter.getId();
                            Log.e("TAG", "Selected Name: " + text);
                            Log.e("TAG", "hos Selected ID: " + id);

                            if (id.equals("-1")){

                                AlertDialog.Builder alertAddingNewHospital = new AlertDialog.Builder(getActivity());
                                alertAddingNewHospital.setTitle("Add New Work place!");
                                alertAddingNewHospital.setIcon(android.R.drawable.ic_dialog_alert);
                                alertAddingNewHospital.setMessage("Do you want add your work place.");
                                alertAddingNewHospital.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent createNewHospitalActivity = new Intent(getActivity(), MapActivityForSelectingHospital.class);
                                        startActivity(createNewHospitalActivity);

                                    }
                                });
                                alertAddingNewHospital.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                alertAddingNewHospital.show();
                                editText.setText("");

                            }else {
                                hostId.setText(id);
                            }


                        }

                    });*/



            }
        }

        //}
    }

    public void hospitalNameTextChangeListener(final AutoCompleteTextView editText, final TextView hospID, final AutoCompleteTextView autocompleteMain, final Dialog dilaog, final ProgressBar precticeProgress){

        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(final Editable editable) {



                if(editable.toString().length() >= 3)
                {

                    Log.e("tag" , "text "+editable.toString());
                    Log.e("tag" , "web service call "+editable.toString());
                    HospitalNameFilter.clear();
                    useHandler(editable.toString() , editText , hospID, autocompleteMain, dilaog, precticeProgress);
                    //getHospitalsNameFilter(editable.toString());

                }



          /*      // user typed: start the timer
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        //Toast.makeText(Hospitals.this, "text in afterTextChanged "+editable.toString(), Toast.LENGTH_SHORT).show();
                        if(editable.toString().length() >= 4)
                        {
                            Log.e("tag" , "text "+editable.toString());
                            Log.e("tag" , "web service call "+editable.toString());
                            HospitalNameFilter.clear();
                            getHospitalsNameFilter(editable.toString(), editText, hospID);
                        }
                        else
                        {
                            Log.e("tag" , "web service not call "+editable.toString());
                        }


                    }
                }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask*/

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //Toast.makeText(Hospitals.this, "text in onTextChanged "+charSequence.toString(), Toast.LENGTH_SHORT).show();
                if (timer != null) {
                    timer.cancel();
                }


            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  Toast.makeText(Hospitals.this, "text in beforeTextChanged "+charSequence.toString(), Toast.LENGTH_SHORT).show();

            }


        });
    }//end of hospital text change fuc

    public void getHospitalsNameFilter(final String filterName, final AutoCompleteTextView editText, final  TextView hospitalId, final AutoCompleteTextView mainAutoComplete, final Dialog myHospitalAutoCompleteDialog, final ProgressBar precticeProgress)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospital Name Filter";
        precticeProgress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.GET_HOS_NAME_FILTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                precticeProgress.setVisibility(View.GONE);
                Log.e("TAG", "Hospital Filter Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        object = new JSONObject(response);


                        hospitalArray = jObj.getJSONArray("hospital_names");

                        for (int i = 0; i < hospitalArray.length(); i++) {

                            JSONObject practiceObject = hospitalArray.getJSONObject(i);

                            hospital_id = practiceObject.getString("hospital_id");
                            hospital_name = practiceObject.getString("hospital_name");
                            hospital_address = practiceObject.getString("hospital_addr");

                            HospitalNameFilter.add(new HospitalSearchFilterGetterSetter(hospital_id , hospital_name , hospital_address));
                        }

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                        if (sharedPreferences!=null){

                            String userId = sharedPreferences.getString("userid", null);
                            if (userId!=null){
                                String verifyStatus = sharedPreferences.getString("verified_status", null);
                                if (verifyStatus.equals("1")) {
                                    HospitalSearchFilterGetterSetter CTGS = new HospitalSearchFilterGetterSetter("-1", "Other", "Add New Workplace");
                                    HospitalNameFilter.add(CTGS);
                                }
                            }
                        }

                        HospitalFilterAdapterForPrecticeUpdateDoc hospitalFilterAdapter = new HospitalFilterAdapterForPrecticeUpdateDoc(getActivity(), HospitalNameFilter);
                        editText.setAdapter(hospitalFilterAdapter);
                        hospitalFilterAdapter.notifyDataSetChanged();
                        editText.showDropDown();

                        settingOnItemClickListenerForHospitalAdapter(editText, hospitalFilterAdapter, hospitalId, mainAutoComplete, myHospitalAutoCompleteDialog);


                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity() , "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                precticeProgress.setVisibility(View.GONE);
                Log.e("TAG", "Hospital Filter Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                //params.put("city", "Lahore");
                params.put("name", filterName);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()) .addToRequestQueue(strReq, cancel_req_tag);
    }

    public void settingOnItemClickListenerForHospitalAdapter(final AutoCompleteTextView editText, final HospitalFilterAdapterForPrecticeUpdateDoc SSFGS, final TextView hosId, final AutoCompleteTextView mainAutoComplete, final Dialog myHospitalAutoCompleteDialog){

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mHandler.removeCallbacks(mRunnableStartMainActivity);

                TextView hos_id_filter = (TextView) view.findViewById(R.id.hos_id_filter);
                TextView hos_name_filter = (TextView) view.findViewById(R.id.hos_name_filter);

                myHospitalAutoCompleteDialog.dismiss();

                String id = hos_id_filter.getText().toString();
                String text = hos_name_filter.getText().toString();

                Log.e("TAG", "my selected hospital name: " + id);
                Log.e("TAG", "my selected hospital id  333 : " + text);


                if (id.equals("-1")){
                    AlertDialog.Builder alertAddingNewHospital = new AlertDialog.Builder(getActivity());
                    alertAddingNewHospital.setTitle("Add New Work place!");
                    alertAddingNewHospital.setIcon(android.R.drawable.ic_dialog_alert);
                    alertAddingNewHospital.setMessage("Do you want add your work place.");
                    alertAddingNewHospital.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            Log.e("TAG", "Here to going to add new hospital");
                            Intent createNewHospitalActivity = new Intent(getActivity(), MapActivityForSelectingHospital.class);
                            startActivity(createNewHospitalActivity);

                        }
                    });
                    alertAddingNewHospital.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertAddingNewHospital.show();
                    mainAutoComplete.setText("");

                }else {


                    //********************************************************************************************************


                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usercrad", 0);
                    String drID = sharedPreferences.getString("myid", null);

                    Log.e("TAG", " 123 Selected ID: " + id);
                    Log.e("TAG", " 123 Selected ID Docter: " + drID);

                    //requestClaimProfileData(id, drID);

                    Log.e("TAG", "the selected Text is from auto complete: " + text);

                    hosId.setText("");
                    hosId.setText(id);
                    mainAutoComplete.setText("");
                    mainAutoComplete.setText(text);


                }

            }

        });
    }




    //request claim profile data
    private void requestClaimProfileData(final String hospitalId, final String drId){

        // Tag used to cancel the request
        String cancel_req_tag = "Hospital Name Filter";

        StringRequest strReq = new StringRequest(Request.Method.POST, "URL", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Claim Response: " + response.toString());

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        object = new JSONObject(response);


                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getActivity() , "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Hospital Filter Error: " + error.getMessage());
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                //Toast.makeText(FindDoctor.this, "speciality_id in service"+speciality_id, Toast.LENGTH_SHORT).show();
                params.put("key", Glob.Key);
                params.put("HospitalID", hospitalId);
                params.put("drID", drId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()) .addToRequestQueue(strReq, cancel_req_tag);

    }


    //Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", " Calls");
            timerHandler--;
           /* mHandler = new Handler();
            mHandler.postDelayed(this, 1000);*/

            Log.e("TAG", "runnin timmer is: " + timerHandler);

            if (timerHandler == 0){


                Log.e("TAG", "Seached Text Is: " + timerHandler);
                mHandler.removeCallbacks(mRunnableStartMainActivity);
                timerHandler = 2;
                if (textToSearch.length()>=3) {
                    getHospitalsNameFilter(textToSearch , autoCompleteTextView , hospId, editTextBig, myHospitalAutoCompleteDialog, precticeProgress);
                }

            }
        }
    };

    //handler for the starign activity

    public void useHandler(String text, AutoCompleteTextView editText, TextView hospID, AutoCompleteTextView mainAutoComplete, final Dialog dialog, final ProgressBar proProgress) {

        textToSearch = text;
        autoCompleteTextView = editText;
        editTextBig = mainAutoComplete;
        precticeProgress = proProgress;
        myHospitalAutoCompleteDialog = dialog;
        hospId = hospID;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnableStartMainActivity, 1000);

    }


    public void hideShowSwitchWidgets(final SwitchCompat discountPackageGetText, final TableLayout discountTableLayout, final EditText checkUpDiscount, final EditText procedureDiscount, final EditText otherDiscount, final EditText writeAboutOtherPercent)
    {

        discountPackageGetText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    discountTableLayout.setVisibility(View.VISIBLE);
                    // discountPackageGetText = discountPackageSwitch.getTextOn().toString();
                    //Log.e("tag" , "discountPackageStr on " + discountPackageGetText);
                }
                else
                {
                    //  discountPackageGetText = discountPackageSwitch.getTextOff().toString();
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

    }


    public void showOtherDiscountPackageEditText(final EditText otherDiscount, final EditText writeAboutOtherPercent)
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

    private void setClickForAutoCompleForHospitalNameFilter(final AutoCompleteTextView editText, final TextView tv_hospital_id){

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog autoDialog = new Dialog(getActivity());
                autoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = autoDialog.getWindow();
                window.setGravity(Gravity.TOP);
                autoDialog.setContentView(R.layout.autocomplete_dialog_like_google_for_hospitals);
                AutoCompleteTextView et_hospital_text = (AutoCompleteTextView) autoDialog.findViewById(R.id.et_hospital_text);
                ProgressBar precticeProgress = (ProgressBar) autoDialog.findViewById(R.id.precticeProgress);

                hospitalNameTextChangeListener(et_hospital_text, tv_hospital_id, editText, autoDialog, precticeProgress);

                autoDialog.show();



            }
        });
    }

}

package themedicall.com;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.DaysSelectAdapter;
import themedicall.com.Adapter.HospitalFilterAdapterForPrecticeUpdateDoc;
import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.GetterSetter.HospitalSearchFilterGetterSetter;
import themedicall.com.GetterSetter.SelectSpecialityGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.Services.GetAllDoctorDetailService;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.net.ssl.HttpsURLConnection;

public class UpdateDoctorProfile extends AppCompatActivity{



    private TabLayout tabLayout;
    public static ViewPager viewPager;

    String aa;


    BasicInfoUpdateDocProfile basic;
    BioUpdateDoctorProfile fragBio;


    private int timerHandler = 2;
    Handler mHandler;
    private String textToSearch;
    AutoCompleteTextView autoCompleteTextView ;
    TextView hospId ;
    Dialog myHospitalAutoCompleteDialog;
    AutoCompleteTextView mainAutoComplete;
    ProgressBar mProgressBar;

    ArrayList<HashMap<String, String>> precticeDataRecord;
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String specialityId ;
    String specialityName ;
    String specialityImg ;
    String hospitalId;
    String hospitalName;
    public static ArrayList<SelectSpecialityGetterSetter> specialityList;
    public static ArrayList<CitiesGetterSetter> hospitalList;

    MyReceiver myReceiver;

    static CustomProgressDialog dialog;


    ArrayList<HospitalSearchFilterGetterSetter> HospitalNameFilter ;
    private Timer timer;
    JSONObject object;
    JSONArray hospitalArray;
    String  hospital_id , hospital_name  , hospital_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();

        startService(new Intent(UpdateDoctorProfile.this, GetAllDoctorDetailService.class));

        //settingDataToFieldsFromSharePref();

        tabsSelectionListener();
        getSpecialityList();
        spcilistClickListner();
        // getAllHospitals();
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
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        precticeDataRecord = new ArrayList<>();
        basic = new BasicInfoUpdateDocProfile();
        fragBio = new BioUpdateDoctorProfile();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        specialityList = new ArrayList<>();
        hospitalList = new ArrayList<>();

        HospitalNameFilter = new ArrayList<HospitalSearchFilterGetterSetter>();



        dialog = new CustomProgressDialog(UpdateDoctorProfile.this, 1);

        if (!dialog.isShowing()) {
            dialog.show();
        }


    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BasicInfoUpdateDocProfile(), "Basic Info");
        //adapter.addFragment(new PrecticeDetailUpdateDocProfile(), "Practice");

        String experience_status = null;
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){
            experience_status = sharedPreferences.getString("experience_status", null);

            if (experience_status!=null){
            if (experience_status.equals("4")) {

                // adapter.addFragment(new PrecticeDetailUpdateDocProfile(), "Practice");

            }else {
                adapter.addFragment(new PrecticeDetailUpdateDocProfile(), "Practice");
            }
            }
        }



        adapter.addFragment(new BioUpdateDoctorProfile(), "Bio");
        viewPager.setAdapter(adapter);


    }


    private void tabsSelectionListener(){

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.e("TAG", "The Tab Postion is: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }//enf for tab selec listener

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void onAddFieldLab(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.custom_docter_update_practice_fragment
                , null);

        // Add the new row before the add field button.
        int count = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();
        PrecticeDetailUpdateDocProfile.medicineCustomRow.addView(rowView, PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount() - count);
        Log.d("tag" , "Press Delete Btn " + PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount());


        if (PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount()>1){

            for (int i = 0; i<PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount(); i++) {

                View view =  PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(i);
                final AutoCompleteTextView editText = (AutoCompleteTextView) view.findViewById(R.id.editText);
                final TextView hostId = (TextView) view.findViewById(R.id.tv_hospital_id);

                //
                setClickForAutoCompleForHospitalNameFilter(editText, hostId);
                //

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


                //editText.setAdapter(adapter);
                Log.e("TAg","TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                // hospitalNameTextChangeListener(editText, hostId);

            }
        }



        onDeletViewClickListner();
        addTitmingButtoncClickHanlder();
        removeTitmingButtoncClickHanlder();


    }


    public void onRemovingSpecialisity(View v){


        int indexOfMyView = ((RelativeLayout) v.getParent()).indexOfChild(v);

        // int totalviewForSpecialist = BasicInfoUpdateDocProfile.rl_custom_specialist_tags.indexOfChild(v);
        Toast.makeText(this, "Remove Called " + indexOfMyView , Toast.LENGTH_SHORT).show();
    /*    View view =  BasicInfoUpdateDocProfile.rl_custom_specialist_tags.getFocusedChild();
        TextView titl = (TextView) view.findViewById(R.id.tv_tag);
        TextView id = (TextView) view.findViewById(R.id.tv_id);

        String specialistTitlt = titl.getText().toString();
        String specialistId = id.getText().toString();

        Toast.makeText(this, "Remove Called " + specialistTitlt , Toast.LENGTH_SHORT).show();*/

        //medicineCustomRow.removeView((View) (v).getParent());
    }

    public void onDeleteLab(View v) {

        int totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();


        if (totalViewCount==1) {
            Toast.makeText(this, "No More Item to Remove", Toast.LENGTH_SHORT).show();
        }

        else {

            PrecticeDetailUpdateDocProfile.medicineCustomRow.removeView((View) (v).getParent());
        }
        //Log.d("tag" , "Press Delete Btn " + PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount());

    }




    public void gettingNextPageForData(final String listData)
    {


        // Tag used to cancel the request
        String cancel_req_tag = "register";

    /*    progressDialog.setMessage("Please Waite ...");
        showDialog();*/

        StringRequest strReq = new StringRequest(Request.Method.POST, "http://docbookpk.com/test/medi-app-api/test" , new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Response: " + response.toString());


            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(UpdateDoctorProfile.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("text", "testing");
                ;

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue

        AppSingleton.getInstance(this).addToRequestQueue(strReq, cancel_req_tag);
        // Adding request to request queue
        //AppSingleton.getInstance(UpdateDoctorProfile.this()).addToRequestQueue(strReq, cancel_req_tag);



        // return requiredBloodList;


    }


    //
    public class GettingDataFromServer extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            try {

                URL url = new URL("http://docbookpk.com/test/medi-app-api/test"); //"https://www.pk.house/app_webservices/franchiser_listing.php");


                connection = (HttpURLConnection) url.openConnection();
                // connection.setSSLSocketFactory(new ClientSSLSocketFactory(connection.getSSLSocketFactory()));
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("text", aa);




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

            Log.e("TAG", "Server Respoonse: " + result);

        }



    }//end of GettingDataFromServer


    //

    public void startTimePicker(final View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(UpdateDoctorProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.e("TAG", selectedHour + ":" + selectedMinute);

                String aTime = timeFormteIn12Hr(selectedHour, selectedMinute);
                EditText etStarttime = (EditText) v.findViewById(R.id.et_startTime);
                etStarttime.setText(aTime);
                // eReminderTime.setText( selectedHour + ":" + selectedMinute);

                //startAlarmForMorning(selectedHour, selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void endTimePicker(final View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(UpdateDoctorProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.e("TAG", selectedHour + ":" + selectedMinute);

                final String aTime = timeFormteIn12Hr(selectedHour, selectedMinute);
                final EditText etEndTime = (EditText) v.findViewById(R.id.et_endtime);

                ///

                //
                final int totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();
                for (int i = 0; i<totalViewCount; i++){
                    final View child = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(i);
                    EditText etMyFirstTime = (EditText)child.findViewById(R.id.et_startTime);
                    EditText etMyEndTime = (EditText)child.findViewById(R.id.et_endtime);
                    if (etEndTime.equals(etMyEndTime)){

                        String second = etMyEndTime.getText().toString();
                        String first = etMyFirstTime.getText().toString();
                        Log.e("TAg", "Timing from the view first: " + first);
                        Log.e("TAg", "Timing from the view second: " + aTime);


                        if (first.equals(aTime)){
                            Toast.makeText(UpdateDoctorProfile.this, "Start And End Time Should not be Same", Toast.LENGTH_SHORT).show();
                            etEndTime.setText("");
                            break;
                        }
                        else {

                            //etStarttime.setText(aTime);
                        }
                    }


                }
                //


                ///
                etEndTime.setText(aTime);


                // eReminderTime.setText( selectedHour + ":" + selectedMinute);

                //startAlarmForMorning(selectedHour, selectedMinute);
            }
        }, hour, minute, false);//Yes 24 hour time

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public String timeFormteIn12Hr(int selectedHour, int selectedMinute){

        String timeSet = "";
        if (selectedHour > 12) {
            selectedHour -= 12;
            timeSet = "PM";
        } else if (selectedHour == 0) {
            selectedHour += 12;
            timeSet = "AM";
        } else if (selectedHour == 12){
            timeSet = "PM";
        }else{
            timeSet = "AM";
        }

        String min = "";
        if (selectedMinute < 10)
            min = "0" + selectedMinute ;
        else
            min = String.valueOf(selectedMinute);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(selectedHour).append(':')
                .append(min ).append(" ").append(timeSet).toString();

        return aTime;

    }

    //seletingDays
    public void selectDaysFromDropDown(final View mView)

    {
        final List<String> dayslit = Arrays.asList(getResources().getStringArray(R.array.days));

        final Dialog dialog = new Dialog(UpdateDoctorProfile.this);
        dialog.setContentView(R.layout.custom_citylist_search);
        dialog.setTitle("Please Select Day");
        Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
        bt_dilaog_done.setVisibility(View.GONE);
        ListView cityListView = (ListView) dialog.findViewById(R.id.cityList);
        dialog.show();


        cityListView.setAdapter(new DaysSelectAdapter(UpdateDoctorProfile.this , dayslit));

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView city_title = (TextView) view.findViewById(R.id.city_title);
                // TextView city_id = (TextView) view.findViewById(R.id.city_id);
                String days = city_title.getText().toString();

                Button btDay = (Button) mView.findViewById(R.id.bt_select_day);
                btDay.setText(days);
                dialog.dismiss();

                //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
            }
        });

        SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
        search_view.setVisibility(View.GONE);


    }







    public void settingDataToFieldsFromSharePref(){

        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userName = sharedPreferences.getString("username", null);
            String userEmail = sharedPreferences.getString("useremail", null);

            //basic.etFullName.setText(userName);
            //fragBio.et_bio_aboutme.setText(userEmail);



        }
    }


    //getting list of sepecialisst
    public void getSpecialityList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        /*progressDialog.setMessage("Please Wait ...");
        showDialog();*/
        dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET, Glob.SELECT_SPECIALITY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());
                //hideDialog();
                // dialog.dismiss();

                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("specilaities");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            specialityId = finalobject.getString("speciality_id");
                            specialityName = finalobject.getString("speciality_designation");
                            specialityImg = finalobject.getString("speciality_icon");


                            Log.e("TAg", "data to check from server the sepeciality id is: " + specialityId);
                            Log.e("TAg", "data to check from server the sepeciality title is: " + specialityName);

                            specialityList.add(new SelectSpecialityGetterSetter(specialityId , specialityName , specialityImg));

                        }






                        /*SelectSpecialityListRecycleView selectSpecialityListRecycleView = new SelectSpecialityListRecycleView(SelectDiseaseForFindDoc.this  , specialityList);
                        recyclerView_speciality_list.setAdapter(selectSpecialityListRecycleView);
                        selectSpecialityListRecycleView.notifyDataSetChanged();*/

                        //Toast.makeText(UpdateDoctorProfile.this , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(UpdateDoctorProfile.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(UpdateDoctorProfile.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                //dialog.dismiss();
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

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(UpdateDoctorProfile.this).addToRequestQueue(strReq, cancel_req_tag);
    }
    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    //spcialist click Listner
    private void spcilistClickListner()
    {

    }



    @Override
    protected void onDestroy() {
        Intent stopService = new Intent(UpdateDoctorProfile.this, GetAllDoctorDetailService.class);
        stopService(stopService);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent stopService = new Intent(UpdateDoctorProfile.this, GetAllDoctorDetailService.class);
        stopService(stopService);
        super.onBackPressed();
    }

    public void onDeletViewClickListner(){

        final int totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();

        for (int i = 0; i<totalViewCount; i++){
            final View child = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(i);
            ImageView ivDelet = (ImageView) child.findViewById(R.id.iv_delete_item);
            final TextView textview_line = (TextView) child.findViewById(R.id.textview_line);
            ivDelet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int tVies = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();
                    child.postInvalidate();
                    if (tVies == 1) {
                        Toast.makeText(UpdateDoctorProfile.this, "No More Item to Remove", Toast.LENGTH_SHORT).show();
                    } else {
                        ViewGroup parent = (ViewGroup) child.getParent();
                        parent.removeView(child);
                        parent.invalidate();

                    }

                }
            });
        }

    }

    //add_remove_timing_view_click_handling
    public void addTitmingButtoncClickHanlder()
    {

        final int totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();



        for (int i = 0; i<totalViewCount; i++){
            final View child = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(i);
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

        final int totalViewCount = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildCount();

        for (int i = 0; i<totalViewCount; i++){
            final View child = PrecticeDetailUpdateDocProfile.medicineCustomRow.getChildAt(i);
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

                        }
                    });
                }
            }
        }

    }


    //broadcast receiverClass

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            int datareceived = arg1.getIntExtra("updated", 0);


            if (datareceived == 1) {


                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }

        }
    }

    @Override
    public void onStart() {

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.FRAGMENT_SWITCHING_ACTION);
        registerReceiver(myReceiver, intentFilter);
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();

    }

    //


    public void hospitalNameTextChangeListener(final AutoCompleteTextView editText, final TextView hospID, final AutoCompleteTextView autocompleteMain, final Dialog dilaog, final ProgressBar progressBar){

        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void afterTextChanged(final Editable editable) {


                if(editable.toString().length() >= 3)
                {

                    Log.e("tag" , "text "+editable.toString());
                    Log.e("tag" , "web service call "+editable.toString());
                    HospitalNameFilter.clear();
                    useHandler(editable.toString() , editText , hospID, autocompleteMain, dilaog, progressBar);
                    //getHospitalsNameFilter(editable.toString());

                }



                /*
                // user typed: start the timer
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
                            getHospitalsNameFilter(editable.toString(), editText, hospID, autocompleteMain, dilaog);
                        }
                        else
                        {
                            Log.e("tag" , "web service not call "+editable.toString());
                        }


                    }
                }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
*/
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

    public void getHospitalsNameFilter(final String filterName, final AutoCompleteTextView editText, final  TextView hospitalId, final AutoCompleteTextView mainAutoComplete, final Dialog dialog, final ProgressBar progressBar)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "Hospital Name Filter";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.GET_HOS_NAME_FILTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
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


                        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
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

                        HospitalFilterAdapterForPrecticeUpdateDoc hospitalFilterAdapter = new HospitalFilterAdapterForPrecticeUpdateDoc(UpdateDoctorProfile.this, HospitalNameFilter);
                        editText.setAdapter(hospitalFilterAdapter);
                        hospitalFilterAdapter.notifyDataSetChanged();
                        editText.showDropDown();


                        settingOnItemClickListenerForHospitalAdapter(editText, hospitalFilterAdapter, hospitalId, mainAutoComplete, dialog, HospitalNameFilter);

                        Log.e("tag" , "hospital filter size size "+HospitalNameFilter.size());
                        // Log.e("tag" , "hospital Name and Id "+hashMap.toString());

                        //Toast.makeText(getContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(UpdateDoctorProfile.this , "error "+ errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
                // params.put("city", "Lahore");
                params.put("name", filterName);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(UpdateDoctorProfile.this) .addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void settingOnItemClickListenerForHospitalAdapter(final AutoCompleteTextView editText, final HospitalFilterAdapterForPrecticeUpdateDoc SSFGS, final TextView hosId, final AutoCompleteTextView mainAutoComplete, final Dialog dialog, final ArrayList<HospitalSearchFilterGetterSetter> hospitalNameFilter){

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //mHandler.removeCallbacks(mRunnableStartMainActivity);

                TextView hos_id_filter = (TextView) view.findViewById(R.id.hos_id_filter);
                TextView hos_name_filter = (TextView) view.findViewById(R.id.hos_name_filter);

                dialog.dismiss();
                hospitalNameFilter.clear();

                String id = hos_id_filter.getText().toString();
                String text = hos_name_filter.getText().toString();

                Log.e("TAG", "my selected hospital name: " + id);
                Log.e("TAG", "my selected hospital id  333 : " + text);

                if (id.equals("-1")){

                    AlertDialog.Builder alertAddingNewHospital = new AlertDialog.Builder(UpdateDoctorProfile.this);
                    alertAddingNewHospital.setTitle("Add New Work place!");
                    alertAddingNewHospital.setIcon(android.R.drawable.ic_dialog_alert);
                    alertAddingNewHospital.setMessage("Do you want add your work place.");
                    alertAddingNewHospital.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Log.e("TAG", "Here to going to add new hospital");
                            Intent createNewHospitalActivity = new Intent(UpdateDoctorProfile.this, MapActivityForSelectingHospital.class);
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

                    hosId.setText("");
                    hosId.setText(id);
                    mainAutoComplete.setText("");
                    mainAutoComplete.setText(text);
                }


                Log.e("TAG", "Selected Name: " + text);
                Log.e("TAG", " 123 Selected ID: " + id);



            }

        });
    }

    public void hideShowSwitchWidgets(final SwitchCompat discountPackageGetText, final TableLayout discountTableLayout, final EditText checkUpDiscount, final EditText procedureDiscount, final EditText otherDiscount, final EditText writeAboutOtherPercent) {

        discountPackageGetText.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    discountTableLayout.setVisibility(View.VISIBLE);
                    // discountPackageGetText = discountPackageSwitch.getTextOn().toString();
                    //Log.e("tag" , "discountPackageStr on " + discountPackageGetText);
                } else {
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

    private void setClickForAutoCompleForHospitalNameFilter(final AutoCompleteTextView editText, final TextView hostId){

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final Dialog autoDialog = new Dialog(UpdateDoctorProfile.this);
                autoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Window window = autoDialog.getWindow();
                window.setGravity(Gravity.TOP);
                autoDialog.setContentView(R.layout.autocomplete_dialog_like_google_for_hospitals);
                AutoCompleteTextView et_hospital_text = (AutoCompleteTextView) autoDialog.findViewById(R.id.et_hospital_text);
                ProgressBar precticeProgress = (ProgressBar) autoDialog.findViewById(R.id.precticeProgress);

                hospitalNameTextChangeListener(et_hospital_text, hostId, editText, autoDialog, precticeProgress);

                autoDialog.show();


            }
        });
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

                    getHospitalsNameFilter(textToSearch , autoCompleteTextView , hospId, mainAutoComplete, myHospitalAutoCompleteDialog, mProgressBar);
                }



            }
        }
    };


    //handler for the starign activity

    public void useHandler(String text, AutoCompleteTextView editText, TextView hospID, AutoCompleteTextView mainAC, final Dialog dialog, final ProgressBar progressBar) {

        textToSearch = text;
        autoCompleteTextView = editText;
        mainAutoComplete = mainAC;
        myHospitalAutoCompleteDialog = dialog;
        mProgressBar = progressBar;
        hospId = hospID;
        mHandler = new Handler();
        mHandler.postDelayed(mRunnableStartMainActivity, 1000);

    }

}

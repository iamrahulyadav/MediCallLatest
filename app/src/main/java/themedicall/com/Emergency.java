package themedicall.com;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;

public class Emergency extends NavigationDrawer {
    View customActionBarView ;
    ImageView emergencyAmbulance , emergencyCenters , emergencyFirstAid , emergencyBloodAppeal , emergencyContacts  , bottomnavigationmedistudy , bottomnavigationmedipedia;
    // Custom Action bar
    Button locationFilter;
    ImageView userIcon , doctorFilterImage;
    ImageView searchViewImg ;
    RelativeLayout.LayoutParams params;
    AutoCompleteTextView autoCompleteSearch ;
    FrameLayout autoCompleteSearchLayout ;
    Button ClearSearchBtn ;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_emergency);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_emergency, null, false);
        drawer.addView(view, 0);

        initiate();
        clickListener();
        handleSearchView();
        setImageInActionbar();

    }
    public void initiate()
    {
        networkDialog = new Dialog(Emergency.this);
        emergencyAmbulance = (ImageView) findViewById(R.id.emergencyAmbulance);
        emergencyCenters = (ImageView) findViewById(R.id.emergencyCenters);
        emergencyFirstAid = (ImageView) findViewById(R.id.emergencyFirstAid);
        emergencyBloodAppeal = (ImageView) findViewById(R.id.emergencyBloodAppeal);
        emergencyContacts = (ImageView) findViewById(R.id.emergencyContacts);
        bottomnavigationmedistudy = (ImageView) findViewById(R.id.bottomnavigationmedistudy);
        bottomnavigationmedipedia = (ImageView) findViewById(R.id.bottomnavigationmedipedia);


        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        locationFilter.setVisibility(View.GONE);
        doctorFilterImage.setVisibility(View.GONE);
        searchViewImg.setVisibility(View.GONE);

        autoCompleteSearch = (AutoCompleteTextView) customActionBarView.findViewById(R.id.autoCompleteSearch);


        autoCompleteSearchLayout = (FrameLayout) findViewById(R.id.autoCompleteSearchLayout);
        ClearSearchBtn = (Button) findViewById(R.id.ClearSearchBtn);

    }

    public void clickListener()
    {

        emergencyCenters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Emergency.this , EmergencyCenter.class);
                startActivity(intent);
            }
        });


        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Emergency.this , EmergencyContact.class);
                startActivity(intent);
            }
        });

        emergencyBloodAppeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Emergency.this , Blood.class);
                intent.putExtra("viewPagerPosition" , "1");
                startActivity(intent);
            }
        });

        emergencyFirstAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comingSoonDialog();
            }
        });

        emergencyAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Emergency.this , SignUp.class);
//                intent.putExtra("item_position" , 7);
//                startActivity(intent);

                comingSoonDialog();
            }
        });

        bottomnavigationmedistudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               comingSoonDialog();
            }
        });

        bottomnavigationmedipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             comingSoonDialog();
            }
        });
    }


    public void comingSoonDialog()
    {
        final Dialog dialog = new Dialog(Emergency.this);
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

    public void handleSearchView()
    {
        searchViewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationFilter.setVisibility(View.GONE);
                userIcon.setVisibility(View.GONE);
                searchViewImg.setVisibility(View.GONE);
                autoCompleteSearchLayout.setVisibility(View.VISIBLE);
                autoCompleteSearch.setHint("Search Emergency Contact");
                autoCompleteSearch.setFocusableInTouchMode(true);
                autoCompleteSearch.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(autoCompleteSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        ClearSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(autoCompleteSearch.getText().toString().equals(""))
                {
                    locationFilter.setVisibility(View.VISIBLE);
                    userIcon.setVisibility(View.VISIBLE);
                    searchViewImg.setVisibility(View.VISIBLE);
                    autoCompleteSearchLayout.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(autoCompleteSearch.getWindowToken(), 0);
                }
                else
                {
                    autoCompleteSearch.setText("");
                }
            }
        });
    }




    public void setImageInActionbar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);
                final String userTable = sharedPreferences.getString("usertable", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL+profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);


                Picasso.with(this).load(PROFILE_IMAGE_URL).placeholder(R.drawable.loginuser).transform(new CircleTransformPicasso()).into(userIcon);

                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (userTable.equals(getResources().getString(R.string.doctors))) {

                            Intent intent = new Intent(Emergency.this , UpdateDoctorProfile.class);
                            startActivity(intent);

                            //starting service for getting all data from server
                        }

                        if (userTable.equals(getResources().getString(R.string.patients))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }

                        if (userTable.equals(getResources().getString(R.string.labs))) {


                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }


                        if (userTable.equals(getResources().getString(R.string.hospitals))) {

                           /* Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.pharmacies))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.blood_donors))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.ambulances))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.health_professionals))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                    }
                });

            }
            else {
                imageView.setImageResource(R.drawable.loginuser);
                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Emergency.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        myReceiver = new MyReceiverForNetworkDialog();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onStart();
    }


    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Glob.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        networkChange();
        super.onResume();
    }

    public void networkChange(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new CheckConnectivity(), intentFilter);
    }


    public class MyReceiverForNetworkDialog extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub

            int datapassed = intent.getIntExtra("DATAPASSED", 0);


            if (datapassed == 1234){


                networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                networkDialog.setContentView(R.layout.internet_connection_dialog);
                networkDialog.setCancelable(false);
                TextView enable = (TextView) networkDialog.findViewById(R.id.enable);
                TextView exit = (TextView) networkDialog.findViewById(R.id.exit);
                networkDialog.show();

                enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(i);
                    }
                });


                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

            }

            if(datapassed == 12345)
            {
                Log.e("tag", "data passed form check connectivity : "+datapassed);
                networkDialog.dismiss();
            }

        }
    }


}

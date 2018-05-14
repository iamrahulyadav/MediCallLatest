package themedicall.com;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.Glob;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    private int STORAGE_PERMISSION_CODE = 23;
    View customActionBarView ;

    MenuItem userLoginState;
    MenuItem createAccount;
    ImageView imageView;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
//        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
//        customActionBarView =getSupportActionBar().getCustomView();
//        getSupportActionBar().setElevation(0);





        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();
        userLoginState = menu.findItem(R.id.logIn);
        createAccount = menu.findItem(R.id.free_consultation);
        View hView =  navigationView.getHeaderView(0);
        imageView  = (ImageView) hView.findViewById(R.id.iv_header);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.navigation_drawer, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }


//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            if (NavigationDrawer.this instanceof Home) {
            } else {
                Intent intent = new Intent(NavigationDrawer.this, Home.class);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.logIn) {
            if (userLoginState.getTitle().toString().equals(getResources().getString(R.string.login))) {
                Intent intent = new Intent(NavigationDrawer.this, SignIn.class);
                startActivity(intent);
            } else {

                SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                if (sharedPreferences != null) {

                    String userId = sharedPreferences.getString("userid", null);
                    if (userId != null) {


                        createAccount.setTitle(getResources().getString(R.string.free_consultation));
                        createAccount.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.free_consultation_avatar));
                        String userFullName = sharedPreferences.getString("userfullname", null);
                        String userTable = sharedPreferences.getString("usertable", null);
                        if (userTable.equals(getResources().getString(R.string.doctors))) {

                            Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivityForResult(intent, 123);

                            //starting service for getting all data from server
                        }

                        if (userTable.equals(getResources().getString(R.string.patients))) {

//                            Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
//                            startActivity(intent);

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
                            startActivity(intent);
*/
                        }
                        if (userTable.equals(getResources().getString(R.string.ambulances))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.health_professionals))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.others))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }


                    }
                }

            }
        } else if (id == R.id.bonus) {

        } else if (id == R.id.free_consultation) {
            if (createAccount.getTitle().equals("Create Account")) {
                Intent intent = new Intent(NavigationDrawer.this, SelectSignUpOptions.class);
                intent.putExtra("claimee_id", "");
                intent.putExtra("claimee_name","");
                intent.putExtra("from", "");
                startActivity(intent);
            }
            if (createAccount.getTitle().equals("Free Consultation")) {
                freeConsultaionDialog();
            }
        } else if (id == R.id.blog) {

        } else if (id == R.id.emergency_contacts) {
            Intent intent = new Intent(NavigationDrawer.this, EmergencyContact.class);
            startActivity(intent);
        } else if (id == R.id.rate_us) {
            showRateDialog(this);
        } else if (id == R.id.share_app) {
            shareApp();
        } else if(id == R.id.tutorial) {
            Intent intent = new Intent(NavigationDrawer.this , AppIntoTutorial.class);
            intent.putExtra("tutorial" , "tutorial");
            startActivity(intent);
        }else if (id == R.id.contact_us) {
            Intent intent = new Intent(NavigationDrawer.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.privacy) {
            Intent intent = new Intent(NavigationDrawer.this, PrivacyPolicy.class);
            startActivity(intent);
        } else if (id == R.id.logout) {

            SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
            SharedPreferences.Editor logout = sharedPreferences.edit();
            logout.clear();
            logout.commit();
            userLoginState.setTitle(getResources().getString(R.string.login));
            imageView.setImageResource(R.drawable.doctor);

            //create account text setting
            createAccount.setTitle(getResources().getString(R.string.creat_account));
            createAccount.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.creat_account_icon));


            Intent i = new Intent(NavigationDrawer.this, Home.class);
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void freeConsultaionDialog()
    {
        final Dialog dialog = new Dialog(NavigationDrawer.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_call_dialog);
        TextView yes = (TextView) dialog.findViewById(R.id.yesCall);
        TextView no = (TextView) dialog.findViewById(R.id.noCall);
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeConsultationCall();
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public void freeConsultationCall()
    {

        //First checking if the app is already having the permission
        if (isPhoneCallAllowed()) {
            //If permission is already having then showing the toast
            // Toast.makeText(Home.this, "You already have the permission", Toast.LENGTH_LONG).show();
            //Existing the method with return
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:03108808880"));
            if (ActivityCompat.checkSelfPermission(NavigationDrawer.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

            return;
        }
        else
        {
            //If the app has not the permission then asking for the permission
            requestStoragePermission();

        }

    }

    public void shareApp()
    {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = "Please Download this App \n https://play.google.com/store/apps/details?id=medicall.com";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Medicall Android App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
    }


//    public static void showRateDialog(final Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setTitle("Rate application")
//                .setMessage("Please, rate this app at Play store")
//                .setPositiveButton("RATE", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (context != null) {
//                            String link = "market://details?id=";
//                            try {
//                                // play market available
//                                context.getPackageManager()
//                                        .getPackageInfo("com.android.vending", 0);
//                                // not available
//                            } catch (PackageManager.NameNotFoundException e) {
//                                e.printStackTrace();
//                                // should use browser
//                                link = "https://play.google.com/store/apps/details?id=com.the.medicall";
//                            }
//                            // starts external action
//                            context.startActivity(new Intent(Intent.ACTION_VIEW,
//                                    Uri.parse(link + context.getPackageName())));
//                        }
//                    }
//                })
//                .setNegativeButton("CANCEL", null);
//        builder.show();
//    }


    public void showRateDialog(final Context context)
    {
        final Dialog dialog = new Dialog(NavigationDrawer.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_rating_dialog);
        TextView rateApp = (TextView) dialog.findViewById(R.id.rateApp);
        TextView rateCancel = (TextView) dialog.findViewById(R.id.rateCancel);
        dialog.show();

        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context != null) {
                    String link = "market://details?id=";
                    try {
                        // play market available
                        context.getPackageManager()
                                .getPackageInfo("com.android.vending", 0);
                        // not available
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        // should use browser
                        link = "https://play.google.com/store/apps/details?id=medicall.com";
                    }
                    // starts external action
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link + context.getPackageName())));
                }



                dialog.dismiss();
            }
        });

        rateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    //We are calling this method to check the permission status
    private boolean isPhoneCallAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            //If permission is not granted returning false
            return false;
        }


    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                // Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                //  Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }




    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){
            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);
                String userTable = sharedPreferences.getString("usertable", null);


                createAccount.setTitle(getResources().getString(R.string.free_consultation));
                createAccount.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.freeconsultation));


                StringBuffer stringbf = new StringBuffer();
                Matcher m = Pattern.compile(
                        "([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(userFullName);

                while (m.find()) {
                    m.appendReplacement(
                            stringbf, m.group(1).toUpperCase() + m.group(2).toLowerCase());
                }

                //userFullName =  userFullName.substring(0, 1).toUpperCase() + userFullName.substring(1);
                userLoginState.setTitle(m.appendTail(stringbf).toString());

                imageView.setImageResource(R.drawable.doctor);
                menu.findItem(R.id.logout).setVisible(true);
                String PROFILE_IMAGE_URL = null;
                if (userTable.equals("doctors")) {
                    PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL + profile_img;
                    Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);
                }
                if (userTable.equals("patients")) {
                    PROFILE_IMAGE_URL = Glob.IMAGE_URL_PATIENT + profile_img;
                    Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);
                }
                if (userTable.equals("blood_donors")) {
                    PROFILE_IMAGE_URL = Glob.IMAGE_URL_DONOR + profile_img;
                    Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);
                }


                if (profile_img.contains("facebook") || profile_img.contains("google")){
                    Picasso.with(this).load(profile_img).transform(new CircleTransformPicasso()).into(imageView);
                }else {
                    Picasso.with(this).load(PROFILE_IMAGE_URL).transform(new CircleTransformPicasso()).into(imageView);
                }

            }
            else {
                imageView.setImageResource(R.drawable.defaultprofile);
            }
        }
    }


}

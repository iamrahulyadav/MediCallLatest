package themedicall.com;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import themedicall.com.Adapter.BloodgroupCustomAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MedicalRecord extends AppCompatActivity {
    ImageView previousDiagnosedCondition , vaccination , allergies , uploadFiles;

    EditText signUpName, signUpDob, bloodgroup;
    Calendar myCalendar = Calendar.getInstance();

    static String mBloodgroupID;

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_medical_record);
        setContentView(R.layout.activity_medical_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initiate();
        // clickListener();
        //setDoctorDob();
        //selectingBloodGroup();
        tabsSelectionListener();
    }

    public void initiate()
    {
        /*previousDiagnosedCondition = (ImageView) findViewById(R.id.previousDiagnosedCondition);
        vaccination = (ImageView) findViewById(R.id.vaccination);
        allergies = (ImageView) findViewById(R.id.allergies);
        uploadFiles = (ImageView) findViewById(R.id.uploadFiles);
        signUpName = (EditText) findViewById(R.id.signUpName);
        signUpDob = (EditText) findViewById(R.id.signUpDob);
        bloodgroup = (EditText) findViewById(R.id.bloodgroup);
*/
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                // signUpName.setText(userFullName);
            }
        }


    }

    public void clickListener()
    {
        previousDiagnosedCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDiseaseDialog();

            }
        });


        vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        allergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAllergyDialog();
            }
        });



        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUploadDialog();

            }
        });
    }

    public void addDiseaseDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.disease_medical_record_form_dialog);
        dialog.setTitle("Please Enter Disease");
        dialog.show();

    }

    public void addUploadDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.upload_medical_record_form_dialog);
        dialog.setTitle("Please Upload");
        dialog.show();

    }


    public void addAllergyDialog()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.allergy_medical_record_form_dialog);
        dialog.setTitle("Please Enter Allergy");
        dialog.show();

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
                view.setMaxDate(myCalendar.getTimeInMillis());


                updateLabel();
            }
        };

        signUpDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MedicalRecord.this, R.style.CustomDatePickerDialogTheme,  date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }


        });


    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        signUpDob.setText(sdf.format(myCalendar.getTime()));
    }


    public void selectingBloodGroup()
    {


        final List<String> Lines = Arrays.asList(getResources().getStringArray(R.array.bloodgroups));

        bloodgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(MedicalRecord.this);
                dialog.setContentView(R.layout.custom_citylist_search);
                Button bt_dilaog_done = (Button) dialog.findViewById(R.id.bt_dilaog_done);
                bt_dilaog_done.setVisibility(View.GONE);
                dialog.setTitle("Select Bloodgroup");
                ListView cityListView = (ListView) dialog.findViewById(R.id.cityList);
                dialog.show();


                cityListView.setAdapter(new BloodgroupCustomAdapter(MedicalRecord.this , Lines));

                cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        TextView city_title = (TextView) view.findViewById(R.id.city_title);
                        // TextView city_id = (TextView) view.findViewById(R.id.city_id);
                        String City = city_title.getText().toString();
                        //String City_id = city_id.getText().toString();

                        //Toast.makeText(SignUp.this, "id "+City_id, Toast.LENGTH_SHORT).show();

                        bloodgroup.setText(City);


                        if (bloodgroup.getText().equals("A+")){mBloodgroupID = "1";}
                        if (bloodgroup.getText().equals("A-")){mBloodgroupID = "2";}
                        if (bloodgroup.getText().equals("B+")){mBloodgroupID = "3";}
                        if (bloodgroup.getText().equals("B-")){mBloodgroupID = "4";}
                        if (bloodgroup.getText().equals("O+")){mBloodgroupID = "5";}
                        if (bloodgroup.getText().equals("O-")){mBloodgroupID = "6";}
                        if (bloodgroup.getText().equals("AB+")){mBloodgroupID = "7";}
                        if (bloodgroup.getText().equals("AB-")){mBloodgroupID = "8";}


                        dialog.dismiss();

                        //Toast.makeText(SignIn.this, "Pos "+text, Toast.LENGTH_SHORT).show();
                    }
                });

                SearchView search_view = (SearchView) dialog.findViewById(R.id.search_view);
                search_view.setVisibility(View.GONE);
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpdatePatientBasicInfoFragment(), "Basic Info");
        adapter.addFragment(new UpdatePatientHealthRecord(), "Health Record");
        // adapter.addFragment(new BioUpdateDoctorProfile(), "Upload");
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


}

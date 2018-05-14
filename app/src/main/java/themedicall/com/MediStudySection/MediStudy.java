package themedicall.com.MediStudySection;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyAdapter.MediStudyCoreAdapter;
import themedicall.com.MediStudyGetterSetter.MediStudyCoreGetterSetter;
import themedicall.com.MediStudyGetterSetter.MediStudyYearMonthGetterSetter;
import themedicall.com.NavigationDrawer;
import themedicall.com.R;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

public class MediStudy extends NavigationDrawer {

    Spinner programSpinner ;
    RadioGroup modeRadioGroup ;
    ImageView mockUpExam , pastPaperExam , subjectExam , guidancePapers ,flashCardImg  , clinicalsImg , notesImg , videoImg , examResultImg , examDateResultImg , examBookmakedImg , contributorsImg;
    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArrayProgram;
    JSONArray jsonArraySpeciality;
    JSONArray jsonArraySubject;
    JSONArray jsonArraySubjectUnit;
    JSONArray jsonArrayDifficultyLevel;
    JSONArray jsonArrayPaperType;
    JSONArray jsonArrayYearMonth;
    String cancel_req_tag = "MedStudy TAG";
    private static final String TAG = "MediStudy Service";
    public ArrayList<MediStudyCoreGetterSetter> programList;
    public ArrayList<MediStudyCoreGetterSetter> specialityList;
    public ArrayList<MediStudyCoreGetterSetter> subjectList;
    public ArrayList<MediStudyCoreGetterSetter> subjectUnitList;
    public ArrayList<MediStudyCoreGetterSetter> difficultyLevelList;
    public ArrayList<MediStudyCoreGetterSetter> paperTypeList;
    public ArrayList<MediStudyYearMonthGetterSetter> yearMonthList;
    CustomProgressDialog dialog;
    String programId ;
    String subjectId ;
    String subjectUnitId = "-2";
    String specialityId ;
    String yearId ;
    String monthId ;
    int numberOfQuestion = 0;
    int numberOfMins = 0;

    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_medi_study);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_medi_study, null, false);
        drawer.addView(view, 0);

        getProgramIdFromPreviousActivity();
        initiate();
        getDifficultyLevelPaperTypeService(programId);
        setSpecialityList();
        setSubjectList();
        //getProgramSpecialitySubjectService();
        clickListener();
        flashSpotDiagnoseSlideClickListener();
    }

    public void getProgramIdFromPreviousActivity()
    {
        Intent intent = getIntent();
        programId = intent.getStringExtra("programId");
        Log.e("tag" , "get program id from program list activity : "+programId);

    }

    public void initiate()
    {
        networkDialog = new Dialog(MediStudy.this);
        dialog=new CustomProgressDialog(MediStudy.this , 1);
        programSpinner = (Spinner) findViewById(R.id.programSpinner);
        modeRadioGroup = (RadioGroup) findViewById(R.id.modeRadioGroup);
        mockUpExam = (ImageView) findViewById(R.id.mockUpExam);
        pastPaperExam = (ImageView) findViewById(R.id.pastPaperExam);
        subjectExam = (ImageView) findViewById(R.id.subjectExam);
        guidancePapers = (ImageView) findViewById(R.id.guidancePapers);
        flashCardImg = (ImageView) findViewById(R.id.flashCardImg);
        clinicalsImg = (ImageView) findViewById(R.id.clinicalsImg);
        notesImg = (ImageView) findViewById(R.id.notesImg);


        videoImg = (ImageView) findViewById(R.id.videoImg);
        examResultImg = (ImageView) findViewById(R.id.examResultImg);
        examDateResultImg = (ImageView) findViewById(R.id.examDateResultImg);
        examBookmakedImg = (ImageView) findViewById(R.id.examBookmakedImg);
        contributorsImg = (ImageView) findViewById(R.id.contributorsImg);



        programList = new ArrayList<>();
        programList.add(new MediStudyCoreGetterSetter("0" , "Select Program"));
        specialityList = new ArrayList<>();
        subjectList = new ArrayList<>();
        subjectUnitList = new ArrayList<>();
        difficultyLevelList = new ArrayList<>();
        difficultyLevelList.add(new MediStudyCoreGetterSetter("0" , "Select Difficulty Level"));
        paperTypeList = new ArrayList<>();
        paperTypeList.add(new MediStudyCoreGetterSetter("0" , "Select Paper Type"));
        yearMonthList = new ArrayList<>();

    }

    public void setSpecialityList()
    {
        specialityList.add(new MediStudyCoreGetterSetter("0" , "Select Speciality"));
        specialityList.add(new MediStudyCoreGetterSetter("1" , "Medicine"));
        specialityList.add(new MediStudyCoreGetterSetter("2" , "Surgery & Allied"));
        specialityList.add(new MediStudyCoreGetterSetter("3" , "Radiology"));
        specialityList.add(new MediStudyCoreGetterSetter("4" , "Pathology"));
        specialityList.add(new MediStudyCoreGetterSetter("5" , "Anesthesia"));
        specialityList.add(new MediStudyCoreGetterSetter("6" , "Ophthalmology"));
        specialityList.add(new MediStudyCoreGetterSetter("7" , "Community"));
        specialityList.add(new MediStudyCoreGetterSetter("8" , "Dentistry"));
        specialityList.add(new MediStudyCoreGetterSetter("9" , "Obs & Gynae"));
        specialityList.add(new MediStudyCoreGetterSetter("10" , "ENT"));

    }


    public void setSubjectList()
    {
        subjectList.add(new MediStudyCoreGetterSetter("0" , "Select Subject"));
        subjectList.add(new MediStudyCoreGetterSetter("1" , "Anatomy"));
        subjectList.add(new MediStudyCoreGetterSetter("2" , "Physiology"));
        subjectList.add(new MediStudyCoreGetterSetter("3" , "Biochemistry"));
        subjectList.add(new MediStudyCoreGetterSetter("4" , "Pathology"));
        subjectList.add(new MediStudyCoreGetterSetter("5" , "Pharmacology"));
        subjectList.add(new MediStudyCoreGetterSetter("6" , "Behavioral sciences"));
        subjectList.add(new MediStudyCoreGetterSetter("7" , "Forensic medicine"));
        subjectList.add(new MediStudyCoreGetterSetter("8" , "Ophthalmology"));
        subjectList.add(new MediStudyCoreGetterSetter("9" , "ENT"));
        subjectList.add(new MediStudyCoreGetterSetter("10" , "Community"));
        subjectList.add(new MediStudyCoreGetterSetter("11" , "Surgery"));
        subjectList.add(new MediStudyCoreGetterSetter("12" , "Medicine"));
    }

    public void clickListener() {

        mockUpExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (programId.equals("0") ) {
                    Toast.makeText(MediStudy.this, "Please select program", Toast.LENGTH_SHORT).show();
                } else if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MediStudy.this, "Please select test mode", Toast.LENGTH_SHORT).show();
                } else {

                    int selectedId = modeRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                    String radioButtonText = selectedRadioButton.getText().toString();

                    if (radioButtonText.equals("Preparatory Mode")) {
                        preparatoryModeDialog("mockUpExam");
                    } else {
                        examModeDialog("mockUpExam");
                    }
                }
            }
        });


        pastPaperExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(programSpinner.getSelectedItem().toString().equals("Select Program"))
//                {
//                    Toast.makeText(MediStudy.this, "Please select program", Toast.LENGTH_SHORT).show();
//                }
                if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MediStudy.this, "Please select test mode", Toast.LENGTH_SHORT).show();
                } else {

                    int selectedId = modeRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                    String radioButtonText = selectedRadioButton.getText().toString();

                    if (radioButtonText.equals("Preparatory Mode")) {
                        preparatoryModeDialog("pastPaperExam");
                    } else {
                        examModeDialog("pastPaperExam");

                    }
                }

            }
        });


        subjectExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(programSpinner.getSelectedItem().toString().equals("Select Program"))
//                {
//                    Toast.makeText(MediStudy.this, "Please select program", Toast.LENGTH_SHORT).show();
//                }
                if (modeRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MediStudy.this, "Please select test mode", Toast.LENGTH_SHORT).show();
                } else {

                    int selectedId = modeRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedId);
                    String radioButtonText = selectedRadioButton.getText().toString();

                    if (radioButtonText.equals("Preparatory Mode")) {
                        preparatoryModeDialog("subjectExam");
                    } else {
                        examModeDialog("subjectExam");

                    }
                }

            }
        });

    }

    public void preparatoryModeDialog(final String paper)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.preparatory_mode_dialog);
        SeekBar numOfQueSeekBar = dialog.findViewById(R.id.numOfQuestion);
        SeekBar minForExamSeekBar = dialog.findViewById(R.id.minForExam);
        Spinner selectSpecialitySpinner = dialog.findViewById(R.id.selectSpecialitySpinner);
        Spinner selectSubjectSpinner = dialog.findViewById(R.id.selectSubjectSpinner);
        final Spinner selectYearSpinner = dialog.findViewById(R.id.selectYearSpinner);
        final Spinner selectMonthSpinner = dialog.findViewById(R.id.selectMonthSpinner);
        Spinner difficultyLevelSpinner = dialog.findViewById(R.id.difficultyLevelSpinner);
        Spinner paperTypeSpinner = dialog.findViewById(R.id.paperTypeSpinner);
        final Spinner subjectUnitSpinner = dialog.findViewById(R.id.subjectUnitSpinner);
        LinearLayout selectSpecialitySpinnerLayout = dialog.findViewById(R.id.selectSpecialitySpinnerLayout);
        LinearLayout selectSubjectSpinnerLayout = dialog.findViewById(R.id.selectSubjectSpinnerLayout);
        LinearLayout selectYearSpinnerLayout = dialog.findViewById(R.id.selectYearSpinnerLayout);
        LinearLayout selectMonthSpinnerLayout = dialog.findViewById(R.id.selectMonthSpinnerLayout);
        LinearLayout difficultyLevelSpinnerLayout = dialog.findViewById(R.id.difficultyLevelSpinnerLayout);
        LinearLayout paperTypeSpinnerLayout = dialog.findViewById(R.id.paperTypeSpinnerLayout);
        final LinearLayout subjectUnitSpinnerLayout = dialog.findViewById(R.id.subjectUnitSpinnerLayout);
        Button startExam = dialog.findViewById(R.id.startExam);
        dialog.show();

        if(paper.equals("mockUpExam"))
        {
            selectYearSpinnerLayout .setVisibility(View.GONE);
            selectMonthSpinnerLayout.setVisibility(View.GONE);
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
            selectSubjectSpinnerLayout.setVisibility(View.GONE);
        }

        else if(paper.equals("pastPaperExam"))
        {
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
        }

        else if(paper.equals("subjectExam"))
        {
            selectSpecialitySpinnerLayout .setVisibility(View.GONE);
            selectYearSpinnerLayout .setVisibility(View.GONE);
            selectMonthSpinnerLayout.setVisibility(View.GONE);
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
        }


        MediStudyCoreAdapter specialityAdapter = new MediStudyCoreAdapter(getApplicationContext(), specialityList);
        selectSpecialitySpinner.setAdapter(specialityAdapter);
        specialityAdapter.notifyDataSetChanged();


        selectSpecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                specialityId = specialityList.get(i).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final ArrayList<String> yearList = new ArrayList<>();


        Log.e("tag" , "year month list size is : "+yearMonthList.size());


        for (int i = 0 ; i < yearMonthList.size() ; i++)
        {
            yearList.add(yearMonthList.get(i).getYear());
        }

        Log.e("tag" , "year list size is : "+yearList.size());


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title , yearList);
        selectYearSpinner.setAdapter(yearAdapter);


        selectYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MediStudy.this, "selected spinner item is : "+i, Toast.LENGTH_SHORT).show();

                yearId = selectYearSpinner.getSelectedItem().toString();


                ArrayList<String> monthArray = new ArrayList<>();
                monthArray = yearMonthList.get(i).getMonthArray();

                Log.e("tag" , "month arrry size is "+monthArray.size());

                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title , monthArray);
                selectMonthSpinner.setAdapter(monthAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        selectMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String monthName = selectMonthSpinner.getSelectedItem().toString();

                switch (monthName)
                {
                    case "January":
                        monthId = "1";
                        break;
                    case "February":
                        monthId = "2";
                        break;
                    case "March":
                        monthId = "3";
                        break;
                    case "April":
                        monthId = "4";
                        break;
                    case "May":
                        monthId = "5";
                        break;
                    case "June":
                        monthId = "6";
                        break;
                    case "July":
                        monthId = "7";
                        break;
                    case "August":
                        monthId = "8";
                        break;
                    case "September":
                        monthId = "9";
                        break;
                    case "October":
                        monthId = "10";
                        break;
                    case "November":
                        monthId = "11";
                        break;
                    case "December":
                        monthId = "12";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title ,getResources().getStringArray(R.array.month_array));
        selectMonthSpinner.setAdapter(monthAdapter);


        MediStudyCoreAdapter subjectAdapter = new MediStudyCoreAdapter(getApplicationContext(), subjectList);
        selectSubjectSpinner.setAdapter(subjectAdapter);
        subjectAdapter.notifyDataSetChanged();


        selectSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subjectId = subjectList.get(i).getId();


                if(subjectId.equals("0"))
                {
                    Toast.makeText(MediStudy.this, "Please select subject", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getSubjectUnitService(subjectId , subjectUnitSpinnerLayout , subjectUnitSpinner);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        subjectUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectUnitId = subjectUnitList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MediStudyCoreAdapter difficultyLevelAdapter = new MediStudyCoreAdapter(getApplicationContext(), difficultyLevelList);
        difficultyLevelSpinner.setAdapter(difficultyLevelAdapter);
        difficultyLevelAdapter.notifyDataSetChanged();


        MediStudyCoreAdapter paperTypeAdapter = new MediStudyCoreAdapter(getApplicationContext(), paperTypeList);
        paperTypeSpinner.setAdapter(paperTypeAdapter);
        paperTypeAdapter.notifyDataSetChanged();


        numOfQueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numberOfQuestion = i;
                // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //*  Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MediStudy.this, "Question : " + numberOfQuestion * 40 , Toast.LENGTH_SHORT).show();
                numberOfQuestion = numberOfQuestion * 50;

            }
         });

        minForExamSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                numberOfMins = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(MediStudy.this, "Mins : " + numberOfMins * 30 , Toast.LENGTH_SHORT).show();
                numberOfMins = numberOfMins * 30;

            }
        });

        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(paper.equals("mockUpExam"))
                {
                    if(numberOfQuestion == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Question", Toast.LENGTH_SHORT).show();
                    }
                    else if(numberOfMins == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Mins", Toast.LENGTH_SHORT).show();
                    }
                    else if(programId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Program", Toast.LENGTH_SHORT).show();
                    }
                    else if(specialityId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Speciality", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MediStudy.this , MediStudyPreparatoryMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("numberOfQuestion" , String.valueOf(numberOfQuestion));
                        intent.putExtra("numberOfMins" , String.valueOf(numberOfMins));
                        intent.putExtra("programId" , programId);
                        intent.putExtra("specialityId" , specialityId);
                        startActivity(intent);

                        Log.e("tag" , "Program id is  : "+ programId);
                        Log.e("tag" , "speciality id is  : "+specialityId);

                        dialog.dismiss();
                    }
                }
                else if(paper.equals("pastPaperExam"))
                {
                    if(numberOfQuestion == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Question", Toast.LENGTH_SHORT).show();
                    }
                    else if(numberOfMins == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Mins", Toast.LENGTH_SHORT).show();
                    }
                    else if(programId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Program", Toast.LENGTH_SHORT).show();

                    }
                    else if(specialityId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Speciality", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectUnitSpinnerLayout.getVisibility() == View.VISIBLE && subjectUnitId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject Unit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MediStudy.this , MediStudyPreparatoryMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("numberOfQuestion" , String.valueOf(numberOfQuestion));
                        intent.putExtra("numberOfMins" , String.valueOf(numberOfMins));
                        intent.putExtra("specialityId" , specialityId);
                        intent.putExtra("programId" , programId);
                        intent.putExtra("yearId" , yearId);
                        intent.putExtra("monthId" , monthId);
                        intent.putExtra("subjectId" , subjectId);
                        intent.putExtra("subjectUnitId" , subjectUnitId);
                        startActivity(intent);

                        Log.e("tag" , "speciality id is  : "+specialityId);
                        Log.e("tag" , "Program id is  : "+ programId);
                        Log.e("tag" , "year id is  : "+yearId);
                        Log.e("tag" , "month id is  : "+monthId);
                        Log.e("tag" , "subject id is  : "+subjectId);
                        Log.e("tag" , "subject unit id is  : "+subjectUnitId);

                        dialog.dismiss();
                    }
                }
                else if(paper.equals("subjectExam"))
                {
                    if(numberOfQuestion == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Question", Toast.LENGTH_SHORT).show();
                    }
                    else if(numberOfMins == 0)
                    {
                        Toast.makeText(MediStudy.this, "Please Select Number Of Mins", Toast.LENGTH_SHORT).show();
                    }
                    else if(subjectId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectUnitSpinnerLayout.getVisibility() == View.VISIBLE && subjectUnitId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject Unit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MediStudy.this , MediStudyPreparatoryMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("numberOfQuestion" , String.valueOf(numberOfQuestion));
                        intent.putExtra("numberOfMins" , String.valueOf(numberOfMins));
                        intent.putExtra("programId" , programId);
                        intent.putExtra("subjectId" , subjectId);
                        intent.putExtra("subjectUnitId" , subjectUnitId);
                        startActivity(intent);
                        Log.e("tag" , "subject id is  : "+subjectId);
                        Log.e("tag" , "subject unit id is  : "+subjectUnitId);

                        dialog.dismiss();

                    }
                }
            }
        });
    }


    public void examModeDialog(final String paper)
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exam_mode_dialog);
        Spinner selectSpecialitySpinner = dialog.findViewById(R.id.selectSpecialitySpinner);
        Spinner selectSubjectSpinner = dialog.findViewById(R.id.selectSubjectSpinner);
        final Spinner selectYearSpinner = dialog.findViewById(R.id.selectYearSpinner);
        final Spinner selectMonthSpinner = dialog.findViewById(R.id.selectMonthSpinner);
        Spinner difficultyLevelSpinner = dialog.findViewById(R.id.difficultyLevelSpinner);
        Spinner paperTypeSpinner = dialog.findViewById(R.id.paperTypeSpinner);
        final Spinner subjectUnitSpinner = dialog.findViewById(R.id.subjectUnitSpinner);
        LinearLayout selectSpecialitySpinnerLayout = dialog.findViewById(R.id.selectSpecialitySpinnerLayout);
        LinearLayout selectSubjectSpinnerLayout = dialog.findViewById(R.id.selectSubjectSpinnerLayout);
        LinearLayout selectYearSpinnerLayout = dialog.findViewById(R.id.selectYearSpinnerLayout);
        LinearLayout selectMonthSpinnerLayout = dialog.findViewById(R.id.selectMonthSpinnerLayout);
        LinearLayout difficultyLevelSpinnerLayout = dialog.findViewById(R.id.difficultyLevelSpinnerLayout);
        LinearLayout paperTypeSpinnerLayout = dialog.findViewById(R.id.paperTypeSpinnerLayout);
        final LinearLayout subjectUnitSpinnerLayout = dialog.findViewById(R.id.subjectUnitSpinnerLayout);
        Button startExam = dialog.findViewById(R.id.startExam);
        dialog.show();

        if(paper.equals("mockUpExam"))
        {
            selectYearSpinnerLayout .setVisibility(View.GONE);
            selectMonthSpinnerLayout.setVisibility(View.GONE);
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
            selectSubjectSpinnerLayout.setVisibility(View.GONE);
            subjectUnitSpinnerLayout.setVisibility(View.GONE);

        }
        else if(paper.equals("pastPaperExam"))
        {
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
        }

        else if(paper.equals("subjectExam"))
        {
            selectSpecialitySpinnerLayout .setVisibility(View.GONE);
            selectYearSpinnerLayout .setVisibility(View.GONE);
            selectMonthSpinnerLayout.setVisibility(View.GONE);
            difficultyLevelSpinnerLayout.setVisibility(View.GONE);
            paperTypeSpinnerLayout.setVisibility(View.GONE);
        }



        MediStudyCoreAdapter specialityAdapter = new MediStudyCoreAdapter(getApplicationContext(), specialityList);
        selectSpecialitySpinner.setAdapter(specialityAdapter);
        specialityAdapter.notifyDataSetChanged();


        selectSpecialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                specialityId = specialityList.get(i).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final ArrayList<String> yearList = new ArrayList<>();


        Log.e("tag" , "year month list size is : "+yearMonthList.size());


        for (int i = 0 ; i < yearMonthList.size() ; i++)
        {
            yearList.add(yearMonthList.get(i).getYear());
        }

        Log.e("tag" , "year list size is : "+yearList.size());


        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title , yearList);
        selectYearSpinner.setAdapter(yearAdapter);


        selectYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                yearId = selectYearSpinner.getSelectedItem().toString();


                ArrayList<String> monthArray = new ArrayList<>();
                monthArray = yearMonthList.get(i).getMonthArray();

                Log.e("tag" , "month arrry size is "+monthArray.size());

                ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title , monthArray);
                selectMonthSpinner.setAdapter(monthAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        selectMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String monthName = selectMonthSpinner.getSelectedItem().toString();

                switch (monthName)
                {
                    case "January":
                        monthId = "1";
                        break;
                    case "February":
                        monthId = "2";
                        break;
                    case "March":
                        monthId = "3";
                        break;
                    case "April":
                        monthId = "4";
                        break;
                    case "May":
                        monthId = "5";
                        break;
                    case "June":
                        monthId = "6";
                        break;
                    case "July":
                        monthId = "7";
                        break;
                    case "August":
                        monthId = "8";
                        break;
                    case "September":
                        monthId = "9";
                        break;
                    case "October":
                        monthId = "10";
                        break;
                    case "November":
                        monthId = "11";
                        break;
                    case "December":
                        monthId = "12";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MediStudy.this , R.layout.custom_list_item, R.id.city_title ,getResources().getStringArray(R.array.month_array));
        selectMonthSpinner.setAdapter(monthAdapter);


        MediStudyCoreAdapter subjectAdapter = new MediStudyCoreAdapter(getApplicationContext(), subjectList);
        selectSubjectSpinner.setAdapter(subjectAdapter);
        subjectAdapter.notifyDataSetChanged();


        selectSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subjectId = subjectList.get(i).getId();


                if(subjectId.equals("0"))
                {
                    Toast.makeText(MediStudy.this, "Please select subject", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getSubjectUnitService(subjectId , subjectUnitSpinnerLayout , subjectUnitSpinner);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        subjectUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subjectUnitId = subjectUnitList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        MediStudyCoreAdapter difficultyLevelAdapter = new MediStudyCoreAdapter(getApplicationContext(), difficultyLevelList);
        difficultyLevelSpinner.setAdapter(difficultyLevelAdapter);
        difficultyLevelAdapter.notifyDataSetChanged();


        MediStudyCoreAdapter paperTypeAdapter = new MediStudyCoreAdapter(getApplicationContext(), paperTypeList);
        paperTypeSpinner.setAdapter(paperTypeAdapter);
        paperTypeAdapter.notifyDataSetChanged();


        startExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(paper.equals("mockUpExam"))
                {
                    if(programId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Program", Toast.LENGTH_SHORT).show();

                    }
                    else if(specialityId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Speciality", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Intent intent = new Intent(MediStudy.this , MediStudyExamMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("programId" , programId);
                        intent.putExtra("specialityId" , specialityId);
                        startActivity(intent);

                        Log.e("tag" , "Program id is  : "+ programId);
                        Log.e("tag" , "speciality id is  : "+specialityId);

                        dialog.dismiss();
                    }
                }
                else if(paper.equals("pastPaperExam"))
                {
                    if(programId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Program", Toast.LENGTH_SHORT).show();

                    }
                    else if(specialityId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Speciality", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectUnitSpinnerLayout.getVisibility() == View.VISIBLE && subjectUnitId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject Unit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MediStudy.this , MediStudyExamMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("specialityId" , specialityId);
                        intent.putExtra("programId" , programId);
                        intent.putExtra("yearId" , yearId);
                        intent.putExtra("monthId" , monthId);
                        intent.putExtra("subjectId" , subjectId);
                        intent.putExtra("subjectUnitId" , subjectUnitId);
                        startActivity(intent);

                        Log.e("tag" , "speciality id is  : "+specialityId);
                        Log.e("tag" , "Program id is  : "+ programId);
                        Log.e("tag" , "year id is  : "+yearId);
                        Log.e("tag" , "month id is  : "+monthId);
                        Log.e("tag" , "subject id is  : "+subjectId);
                        Log.e("tag" , "subject unit id is  : "+subjectUnitId);

                        dialog.dismiss();

                    }
                }
                else if(paper.equals("subjectExam"))
                {
                     if(subjectId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject", Toast.LENGTH_SHORT).show();

                    }
                    else if(subjectUnitSpinnerLayout.getVisibility() == View.VISIBLE && subjectUnitId.equals("0"))
                    {
                        Toast.makeText(MediStudy.this, "Please Select Subject Unit", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        Intent intent = new Intent(MediStudy.this , MediStudyExamMode.class);
                        intent.putExtra("paperType" , paper);
                        intent.putExtra("programId" , programId);
                        intent.putExtra("subjectId" , subjectId);
                        intent.putExtra("subjectUnitId" , subjectUnitId);
                        startActivity(intent);

                        Log.e("tag" , "subject id is  : "+subjectId);
                        Log.e("tag" , "subject unit id is  : "+subjectUnitId);

                        dialog.dismiss();

                    }
                }






            }
        });
    }


    public void getProgramSpecialitySubjectService()
    {

        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.URL+"get-medistudy-core-list", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "MediStudy core Service Response: " + response.toString());

                dialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArrayProgram = jsonObject.getJSONArray("programs");
                        jsonArraySpeciality = jsonObject.getJSONArray("specialities");
                        jsonArraySubject = jsonObject.getJSONArray("subjects");


                        for (int i = 0; i < jsonArrayProgram.length(); i++) {

                            JSONObject finalobject = jsonArrayProgram.getJSONObject(i);

                            String specialityId  = finalobject.getString("id");
                            String specialityName = finalobject.getString("name");
                            programList.add(new MediStudyCoreGetterSetter(specialityId , specialityName));

                        }

                        MediStudyCoreAdapter mediStudyCoreAdapter = new MediStudyCoreAdapter(getApplicationContext(), programList);
                        programSpinner.setAdapter(mediStudyCoreAdapter);
                        mediStudyCoreAdapter.notifyDataSetChanged();


                        programSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                programId = programList.get(i).getId();



                                if(!programId.equals("0"))
                                {
                                   // getDifficultyLevelPaperTypeService(programId);
                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });



                        for (int i = 0; i < jsonArraySpeciality.length(); i++) {

                            JSONObject finalobject = jsonArraySpeciality.getJSONObject(i);

                            String subjectId  = finalobject.getString("id");
                            String subjectName = finalobject.getString("name");
                            specialityList.add(new MediStudyCoreGetterSetter(subjectId , subjectName));

                        }


                        for (int i = 0; i < jsonArraySubject.length(); i++) {

                            JSONObject finalobject = jsonArraySubject.getJSONObject(i);

                            String programId  = finalobject.getString("id");
                            String programName = finalobject.getString("name");
                            subjectList.add(new MediStudyCoreGetterSetter(programId , programName));

                        }


                        Log.e("tag" , "program list size is : "+programList.size());
                        Log.e("tag" , "speciality list size is : "+specialityList.size());
                        Log.e("tag" , "subject list size is : "+subjectList.size());

                    } else {
                        String errorMsg = jObj.getString("error_message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "MediStudy core Service Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void getDifficultyLevelPaperTypeService(final String programId)
    {

        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.URL+"get-data-for-programs", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Difficulty Level Paper Type Service Response: " + response.toString());

                dialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArrayDifficultyLevel = jsonObject.getJSONArray("levels");
                        jsonArrayPaperType = jsonObject.getJSONArray("paper_types");
                        jsonArrayYearMonth = jsonObject.getJSONArray("years");


                        for (int i = 0; i < jsonArrayDifficultyLevel.length(); i++) {

                            JSONObject finalobject = jsonArrayDifficultyLevel.getJSONObject(i);

                            String levelId  = finalobject.getString("id");
                            String levelName = finalobject.getString("name");
                            difficultyLevelList.add(new MediStudyCoreGetterSetter(levelId , levelName));

                        }


                        for (int i = 0; i < jsonArrayPaperType.length(); i++) {

                            JSONObject finalobject = jsonArrayPaperType.getJSONObject(i);

                            String typeId  = finalobject.getString("id");
                            String typeName = finalobject.getString("name");
                            paperTypeList.add(new MediStudyCoreGetterSetter(typeId , typeName));

                        }


                        for (int i = 0; i < jsonArrayYearMonth.length(); i++) {
                            ArrayList<String> monthArray = new ArrayList<>();

                            JSONObject finalobject = jsonArrayYearMonth.getJSONObject(i);
                            String year  = finalobject.getString("year");
                            Log.e("tag" , "year array year is : "+year);
                            JSONArray jsonArray = finalobject.getJSONArray("months");
                            for (int j = 0 ; j < jsonArray.length() ; j++)
                            {
                                JSONObject object = jsonArray.getJSONObject(j);
                                String month = object.getString("month");
                                Log.e("tag" , "year array month is : "+month);

                                switch (month)
                                {
                                    case "1":
                                        monthArray.add("January");
                                        break;
                                    case "2":
                                        monthArray.add("February");
                                        break;
                                    case "3":
                                        monthArray.add("March");
                                        break;
                                    case "4":
                                        monthArray.add("April");
                                        break;
                                    case "5":
                                        monthArray.add("May");
                                        break;
                                    case "6":
                                        monthArray.add("June");
                                        break;
                                    case "7":
                                        monthArray.add("July");
                                        break;
                                    case "8":
                                        monthArray.add("August");
                                        break;
                                    case "9":
                                        monthArray.add("September");
                                        break;
                                    case "10":
                                        monthArray.add("October");
                                        break;
                                    case "11":
                                        monthArray.add("November");
                                        break;
                                    case "12":
                                        monthArray.add("December");
                                        break;

                                }
                            }

                            yearMonthList.add(new MediStudyYearMonthGetterSetter(year , monthArray));

                        }

                        Log.e("tag" , "year month array list size is : "+yearMonthList.size());
                        Log.e("tag" , "difficulty level list size is : "+difficultyLevelList.size());
                        Log.e("tag" , "paper type list size is : "+paperTypeList.size());

                    } else {
                        String errorMsg = jObj.getString("error_message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "MediStudy core Service Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("program_id", programId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    public void getSubjectUnitService(final String subjectId, final LinearLayout subjectUnitSpinnerLayout, final Spinner subjectUnitSpinner)
    {

        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.URL+"get-data-for-subjects", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "subject unit Service Response: " + response.toString());

                dialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        subjectUnitList.clear();

                        jsonObject = new JSONObject(response);
                        jsonArraySubjectUnit = jsonObject.getJSONArray("units");


                        if(jsonArraySubjectUnit.length() == 0)
                        {
                            Log.e("tag" , "json array subject unit is null");
                            subjectUnitSpinnerLayout.setVisibility(View.GONE);
                            subjectUnitList.clear();

                        }
                        else
                        {

                            subjectUnitList.add(new MediStudyCoreGetterSetter("0" , "Select Subject Unit"));
                            subjectUnitList.add(new MediStudyCoreGetterSetter("-1" , "Random"));

                            for (int i = 0; i < jsonArraySubjectUnit.length(); i++) {

                                JSONObject finalobject = jsonArraySubjectUnit.getJSONObject(i);

                                String unitId  = finalobject.getString("id");
                                String unitName = finalobject.getString("name");
                                subjectUnitList.add(new MediStudyCoreGetterSetter(unitId , unitName));

                            }

                            MediStudyCoreAdapter subjectUnitAdapter = new MediStudyCoreAdapter(getApplicationContext(), subjectUnitList);
                            subjectUnitSpinner.setAdapter(subjectUnitAdapter);
                            subjectUnitAdapter.notifyDataSetChanged();



                            subjectUnitSpinnerLayout.setVisibility(View.VISIBLE);
                            Log.e("tag" , "subject unit list size is : "+subjectUnitList.size());
                        }


                    } else {
                        String errorMsg = jObj.getString("error_message");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "subject unit Service Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("subject_id", subjectId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void flashSpotDiagnoseSlideClickListener()
    {


        guidancePapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashDiagnoseSlidesDialog("1");
            }
        });


        flashCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("2");
                comingSoonDialog();
            }
        });

        notesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("3");
                comingSoonDialog();

            }
        });

        clinicalsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });


        videoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });


        examResultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });

        examDateResultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });

        examBookmakedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });

        contributorsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //flashDiagnoseSlidesDialog("4");
                comingSoonDialog();

            }
        });
    }

    public void comingSoonDialog()
    {
        final Dialog dialog = new Dialog(MediStudy.this);
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


    public void flashDiagnoseSlidesDialog(final String tagId) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.flash_spot_slide_dialog);
        Spinner flashSpotSideSubjectSpinner = dialog.findViewById(R.id.flashSpotSideSubjectSpinner);
        LinearLayout flashSpotSideSubjectSpinnerLayout = dialog.findViewById(R.id.flashSpotSideSubjectSpinnerLayout);
        Button flashSpotSideSubmitBtn = dialog.findViewById(R.id.flashSpotSideSubmitBtn);
        dialog.show();

        MediStudyCoreAdapter subjectAdapter = new MediStudyCoreAdapter(getApplicationContext(), subjectList);
        flashSpotSideSubjectSpinner.setAdapter(subjectAdapter);
        subjectAdapter.notifyDataSetChanged();


        flashSpotSideSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                subjectId = subjectList.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        flashSpotSideSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subjectId.equals("0"))
                {
                    Toast.makeText(MediStudy.this, "Please select subject", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(MediStudy.this , MediStudyFlashSpotSlides.class);
                    intent.putExtra("programId" , programId);
                    intent.putExtra("subjectId" , subjectId);
                    intent.putExtra("tagId" , tagId);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });


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

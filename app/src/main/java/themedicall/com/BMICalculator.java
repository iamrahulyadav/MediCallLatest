package themedicall.com;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import de.nitri.gauge.Gauge;

public class BMICalculator extends AppCompatActivity {

    private TextView result;

    EditText heightFeet , heightInch , heightInCM , weightInKgLB ;
    Spinner heightSpinner , weightSpinner ;
    Button calculateBMIBtn ;
    String [] heightArray = {"FT+IN" , "CM"};
    String [] weightArray = {"KG" , "LB"};
    ArrayAdapter<String> heightAdapter , weightAdapter ;
    Float totalHeightInCM  , totalWeight ;
    Gauge gauge1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmicalculator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initiate();
        setArrayToAdapter();
        spinnerClickListener();
        calculateBMIBtnClick();
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

        gauge1 = (Gauge) findViewById(R.id.gauge);
        result = (TextView) findViewById(R.id.result);
        heightFeet = (EditText) findViewById(R.id.heightFeet);
        heightInch = (EditText) findViewById(R.id.heightInch);
        heightInCM = (EditText) findViewById(R.id.heightInCM);
        weightInKgLB = (EditText) findViewById(R.id.weightInKgLB);
        heightSpinner = (Spinner) findViewById(R.id.heightSpinner);
        weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        calculateBMIBtn = (Button) findViewById(R.id.calculateBMIBtn);
    }

    public void setArrayToAdapter()
    {
        heightAdapter = new ArrayAdapter<String>(BMICalculator.this , R.layout.spinner_list , R.id.spinnerList , heightArray);
        heightSpinner.setAdapter(heightAdapter);

        weightAdapter = new ArrayAdapter<String>(BMICalculator.this , R.layout.spinner_list , R.id.spinnerList , weightArray);
        weightSpinner.setAdapter(weightAdapter);

    }

    public void spinnerClickListener()
    {
        heightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        heightFeet.setVisibility(View.VISIBLE);
                        heightInch.setVisibility(View.VISIBLE);
                        heightInCM.setVisibility(View.GONE);
                        heightInCM.setText("");
                        break;
                    case 1:
                        heightFeet.setVisibility(View.GONE);
                        heightInch.setVisibility(View.GONE);
                        heightFeet.setText("");
                        heightInch.setText("");
                        heightInCM.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        weightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        weightInKgLB.setHint("KG");
                        weightInKgLB.setText("");
                        weightInKgLB.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3)});
                        break;
                    case 1:
                        weightInKgLB.setHint("LB");
                        weightInKgLB.setText("");
                        weightInKgLB.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3)});
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void calculateBMIBtnClick()
    {
        calculateBMIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(heightSpinner.getSelectedItem().equals("FT+IN") && heightFeet.getText().toString().equals(""))
                {
                    Toast.makeText(BMICalculator.this, "Please enter height in feet and inch", Toast.LENGTH_SHORT).show();
                }
                else if(heightSpinner.getSelectedItem().equals("FT+IN") && heightInch.getText().toString().equals(""))
                {
                    Toast.makeText(BMICalculator.this, "Please enter height in feet and inch", Toast.LENGTH_SHORT).show();
                }
                else if(heightSpinner.getSelectedItem().equals("CM") && heightInCM.getText().toString().equals(""))
                {
                    Toast.makeText(BMICalculator.this, "Please enter height in centimeter", Toast.LENGTH_SHORT).show();
                }
                else if(weightSpinner.getSelectedItem().equals("KG") && weightInKgLB.getText().toString().equals(""))
                {
                    Toast.makeText(BMICalculator.this, "Please enter weight in kilogram", Toast.LENGTH_SHORT).show();
                }
                else if(weightSpinner.getSelectedItem().equals("LB") && weightInKgLB.getText().toString().equals(""))
                {
                    Toast.makeText(BMICalculator.this, "Please enter weight in pounds", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String heightSpinnerText = heightSpinner.getSelectedItem().toString();
                    String weightSpinnerText = weightSpinner.getSelectedItem().toString();
                    String heightFeetText = heightFeet.getText().toString();
                    String heightInchText = heightInch.getText().toString();
                    String heightInCMText = heightInCM.getText().toString();
                    String weightInKgLBText = weightInKgLB.getText().toString();


                    Log.e("tag", "BMI Calculator height spinner text : "+heightSpinnerText );
                    Log.e("tag", "BMI Calculator weight spinner text : "+weightSpinnerText );
                    Log.e("tag", "BMI Calculator weight in feet text : "+heightFeetText );
                    Log.e("tag", "BMI Calculator weight in inch text : "+heightInchText );
                    Log.e("tag", "BMI Calculator weight in centimeter text : "+heightInCMText );
                    Log.e("tag", "BMI Calculator height in KG or LB text : "+weightInKgLBText );

                    if(heightSpinnerText.equals("FT+IN"))
                    {
                        int feetInInt = Integer.parseInt(heightFeetText);
                        int inchInInt = Integer.parseInt(heightInchText);
                        float feetInCM = (float) (feetInInt * 30.48);
                        float inchInCM = (float) (inchInInt * 2.54);
                        totalHeightInCM = feetInCM + inchInCM ;

                        Log.e("tag" , "Feet and inches in centimeter in FT+IN: " + totalHeightInCM);
                    }
                     if(heightSpinnerText.equals("CM"))
                    {
                        totalHeightInCM = Float.parseFloat(heightInCMText);

                        Log.e("tag" , "Feet and inches in centimeter in CM: " + totalHeightInCM);

                    }

                    if(weightSpinnerText.equals("LB"))
                    {
                        int weightInInt = Integer.parseInt(weightInKgLBText);
                        float weightInKG = (float) (weightInInt * 0.453592) ;
                        totalWeight = weightInKG;
                        Log.e("tag" , "weight convert pound to kilogram in LB : " + totalWeight);
                    }

                     if(weightSpinnerText.equals("KG"))
                    {
                        totalWeight = Float.parseFloat(weightInKgLBText);
                        Log.e("tag" , "weight convert pound to kilogram in KG : " + totalWeight);
                    }

                    calculateBMI(totalHeightInCM  , totalWeight);

//                    else
//                    {
//                        Log.e("tag" , "check Feet and inches in centimeter : " + totalHeightInCM);
//                        Log.e("tag" , "check weight convert pound to kilogram : " + totalWeight);
//                    }
                }
            }
        });
    }


    public static int roundFloat(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }


    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public void calculateBMI(Float float1, Float float2) {

            float heightValue = Float.parseFloat(String.valueOf(float1)) / 100;
            float weightValue = Float.parseFloat(String.valueOf(float2));

            float bmi = weightValue / (heightValue * heightValue);
            int convertFloatToInt = Math.round(bmi);
            Log.e("tag" , "convert float to int : "+convertFloatToInt);
            gauge1.moveToValue(convertFloatToInt);
            Float roundOfFloatToTwoDecimal = round(bmi , 1);
            Log.e("tag" , "round of float result is : "+roundOfFloatToTwoDecimal);

            gauge1.setLowerText(String.valueOf(roundOfFloatToTwoDecimal));
            displayBMI(roundOfFloatToTwoDecimal);


    }
    private void displayBMI(float bmi) {
        String bmiLabel = "";

        if (Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight";
        } else if (Float.compare(bmi, 15f) > 0  &&  Float.compare(bmi, 16f) <= 0) {
            bmiLabel = "Severely underweight";
        } else if (Float.compare(bmi, 16f) > 0  &&  Float.compare(bmi, 18.5f) <= 0) {
            bmiLabel = "Underweight";
        } else if (Float.compare(bmi, 18.5f) > 0  &&  Float.compare(bmi, 25f) <= 0) {
            bmiLabel = "Normal";
        } else if (Float.compare(bmi, 25f) > 0  &&  Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight";
        } else if (Float.compare(bmi, 30f) > 0  &&  Float.compare(bmi, 35f) <= 0) {
            bmiLabel = "Obese class i";
        } else if (Float.compare(bmi, 35f) > 0  &&  Float.compare(bmi, 40f) <= 0) {
            bmiLabel = "Obese class ii";
        } else {
            bmiLabel = "Obese class iii";
        }

        bmiLabel = "BMI Category: " + bmiLabel;
        result.setVisibility(View.VISIBLE);
        result.setText(bmiLabel);
    }


}

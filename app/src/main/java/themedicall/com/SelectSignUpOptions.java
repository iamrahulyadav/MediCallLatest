package themedicall.com;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.internal.SignInHubActivity;

import themedicall.com.Adapter.SelectSignUpOptionListRecycleView;
import themedicall.com.GetterSetter.SelectSIgnUpOptionGetterSetter;


import java.util.ArrayList;
import java.util.List;

public class SelectSignUpOptions extends AppCompatActivity {
    RecyclerView recyclerView_sign_in_option;
    List<SelectSIgnUpOptionGetterSetter> singUpOptionList;
    TextView skipEnter;
    ImageView signupdoctor , signuppatient , signuphospital , signuplab , signuppharmacy , signuphealthprofessional , signupblood , signupambulance ;


    String mClaimeeID = "";
    String mClimeeName = "";
    String mFrome = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sign_up_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.landingScreenBackgroundColor)));

        initiate();
        signUpBtnClickListener();
        //prepareHappyUserList();
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


        Intent intent = getIntent();

        String claimeeId = intent.getStringExtra("claimee_id");

        if (claimeeId==null){

            finish();
        }else {
            Log.e("TAG", "then the claimeed is is here: " + claimeeId);

            if (claimeeId != null || !claimeeId.equals(null) || claimeeId.length() > 0) {
                mClimeeName = getIntent().getStringExtra("claimee_name");
                mFrome = getIntent().getStringExtra("from");
                mClaimeeID = claimeeId;
            }

        }

        singUpOptionList = new ArrayList<>();
        skipEnter = (TextView) findViewById(R.id.skipAndEnter);

        signupdoctor = (ImageView) findViewById(R.id.signupdoctor);
        signuppatient = (ImageView) findViewById(R.id.signuppatient);
        signuphospital = (ImageView) findViewById(R.id.signuphospital);
        signuplab = (ImageView) findViewById(R.id.signuplab);
        signuppharmacy = (ImageView) findViewById(R.id.signuppharmacy);
        signuphealthprofessional = (ImageView) findViewById(R.id.signuphealthprofessional);
        signupblood = (ImageView) findViewById(R.id.signupblood);
        signupambulance = (ImageView) findViewById(R.id.signupambulance);

    }


    public void signUpBtnClickListener()
    {
        signupdoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(SelectSignUpOptions.this , SignUp.class);
                Intent intent = new Intent(SelectSignUpOptions.this , MedicSignup.class);
                intent.putExtra("item_position" , 0);
                intent.putExtra("claimee_id", mClaimeeID);
                intent.putExtra("claimee_name", mClimeeName);
                intent.putExtra("from", mFrome);
                startActivity(intent);
                finish();
            }
        });

        signuppatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSignUpOptions.this , SignupAsUser.class);
                intent.putExtra("item_position" , 1);
                intent.putExtra("claimee_id", mClaimeeID);
                intent.putExtra("claimee_name", mClimeeName);
                intent.putExtra("from", mFrome);
                startActivity(intent);
                finish();
            }
        });


        signuphospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectSignUpOptions.this , SignUp.class);
//                intent.putExtra("item_position" , 2);
//                startActivity(intent);
                comingSoonDialog();
            }
        });


        signuplab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectSignUpOptions.this , SignUp.class);
//                intent.putExtra("item_position" , 3);
//                startActivity(intent);
                comingSoonDialog();
            }
        });


        signuppharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectSignUpOptions.this , SignUp.class);
//                intent.putExtra("item_position" , 4);
//                startActivity(intent);
                comingSoonDialog();
            }
        });


        signuphealthprofessional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSignUpOptions.this , SignUpForAliedHealth.class);
                intent.putExtra("item_position" , 5);
                startActivity(intent);
                finish();
                // comingSoonDialog();
            }
        });


        signupblood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSignUpOptions.this , SignupAsUser.class);
                intent.putExtra("item_position" , 6);
                intent.putExtra("claimee_id", mClaimeeID);
                intent.putExtra("claimee_name", mClimeeName);
                intent.putExtra("from", mFrome);
                startActivity(intent);
                finish();
            }
        });


        signupambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectSignUpOptions.this , SignUpAmbulance.class);
//                startActivity(intent);
                comingSoonDialog();
            }
        });


        skipEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSignUpOptions.this , Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }

    public void comingSoonDialog()
    {
        final Dialog dialog = new Dialog(SelectSignUpOptions.this);
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



    public void prepareHappyUserList()
    {
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signupdoctor , "0" , "Doctor"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signuppatient , "1" , "Patient"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signuphospital , "2" , "Hospital"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signuplab , "3" , "Lab"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signuppharmacy , "4" , "Pharmacy"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signuphealthprofessional , "5" , "Health Professional"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signupblood , "6" , "Blood Donor"));
        singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.signupambulance , "7" , "Ambulance"));
        //singUpOptionList.add(new SelectSIgnUpOptionGetterSetter(R.drawable.othersignup , "7" , "Other"));


        SelectSignUpOptionListRecycleView selectSignUpOptionListRecycleView = new SelectSignUpOptionListRecycleView(singUpOptionList);
        recyclerView_sign_in_option.setAdapter(selectSignUpOptionListRecycleView);
        selectSignUpOptionListRecycleView.notifyDataSetChanged();
    }





}

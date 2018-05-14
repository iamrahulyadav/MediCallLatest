package themedicall.com.MediStudySection;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daasuu.ahp.AnimateHorizontalProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import themedicall.com.Blog;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyGetterSetter.MediStudyQuestionGetterSetter;
import themedicall.com.R;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

public class MediStudyPreparatoryModeSkipped extends AppCompatActivity {
    public static final int TEST_PASS_LIMIT = 1;
    public static final int TEST_QUES_LIMIT = 150;
    LinearLayout QueOptionALayout , QueOptionBLayout , QueOptionCLayout , QueOptionDLayout , QueOptionELayout , QueOptionFLayout;
    TextView noOfQuestion   , totalSkippedQuestion , timer  , question ,  questionAText , questionBText , questionCText , questionDText , questionEText  , questionFText;
    Button skipBtn , viewAnswerBtn  , testFinishSkipped;
    ImageView previousBtn , nextBtn , reportQuestion , bookmarkQuestion , notesQuestion , shareQuestion , discussionQuestion;
    ImageView optionAImage , optionBImage , optionCImage , optionDImage , optionEImage , optionFImage ;
    TextView optionAExplanation , optionBExplanation , optionCExplanation , optionDExplanation , optionEExplanation , optionFExplanation ;
    AnimateHorizontalProgressBar progressBar;
    int currentQuestion = 0;
    int msqsListSize = 0;
    boolean userSelectAnswer = false;
    String correctAnswer = null;
    String explanation = null;
    UserTestResults userTestResults;
    public CountDownTimer countDownTimer;
    long remainingSecond;
    CustomProgressDialog dialog;
    LinearLayout mcqsMainLayout;
    ArrayList<Integer> keys ;
    long remainSecond;
    int totalQuestion = 0;
    int currentFlagQuestionNumber =0;
    private static final String TAG = "Show Flagged";
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    public Map.Entry<Integer,String> entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_study_preparatory_mode_skipped);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("MediCall > FCPS-1");

        initiate();
        startTime();
        optionButtonClickListener();
        displayQuestionAndAnswer();
        clickListener();
    }

    public void initiate()
    {
        dialog=new CustomProgressDialog(MediStudyPreparatoryModeSkipped.this , 1);
        networkDialog = new Dialog(MediStudyPreparatoryModeSkipped.this);

        QueOptionALayout = (LinearLayout) findViewById(R.id.QueOptionALayout);
        QueOptionBLayout = (LinearLayout) findViewById(R.id.QueOptionBLayout);
        QueOptionCLayout = (LinearLayout) findViewById(R.id.QueOptionCLayout);
        QueOptionDLayout = (LinearLayout) findViewById(R.id.QueOptionDLayout);
        QueOptionELayout = (LinearLayout) findViewById(R.id.QueOptionELayout);
        QueOptionFLayout = (LinearLayout) findViewById(R.id.QueOptionFLayout);

        noOfQuestion = (TextView) findViewById(R.id.noOfQuestion);
        totalSkippedQuestion = (TextView) findViewById(R.id.totalSkippedQuestion);
        timer = (TextView) findViewById(R.id.timer);
        question = (TextView) findViewById(R.id.question);

        questionAText = (TextView) findViewById(R.id.questionAText);
        questionBText = (TextView) findViewById(R.id.questionBText);
        questionCText = (TextView) findViewById(R.id.questionCText);
        questionDText = (TextView) findViewById(R.id.questionDText);
        questionEText = (TextView) findViewById(R.id.questionEText);
        questionFText = (TextView) findViewById(R.id.questionFText);

        optionAExplanation = (TextView) findViewById(R.id.optionAExplanation);
        optionBExplanation = (TextView) findViewById(R.id.optionBExplanation);
        optionCExplanation = (TextView) findViewById(R.id.optionCExplanation);
        optionDExplanation = (TextView) findViewById(R.id.optionDExplanation);
        optionEExplanation = (TextView) findViewById(R.id.optionEExplanation);
        optionFExplanation = (TextView) findViewById(R.id.optionFExplanation);

        optionAImage = (ImageView) findViewById(R.id.optionAImage);
        optionBImage = (ImageView) findViewById(R.id.optionBImage);
        optionCImage = (ImageView) findViewById(R.id.optionCImage);
        optionDImage = (ImageView) findViewById(R.id.optionDImage);
        optionEImage = (ImageView) findViewById(R.id.optionEImage);
        optionFImage = (ImageView) findViewById(R.id.optionFImage);

        previousBtn = (ImageView) findViewById(R.id.previousBtn);
        skipBtn = (Button) findViewById(R.id.skipBtn);
        skipBtn.setVisibility(View.GONE);
        viewAnswerBtn = (Button) findViewById(R.id.viewAnswerBtn);
        viewAnswerBtn.setVisibility(View.GONE);
        testFinishSkipped = (Button) findViewById(R.id.testFinishSkipped);
        reportQuestion = (ImageView) findViewById(R.id.reportQuestion);
        bookmarkQuestion = (ImageView) findViewById(R.id.bookmarkQuestion);
        notesQuestion = (ImageView) findViewById(R.id.notesQuestion);
        shareQuestion = (ImageView) findViewById(R.id.shareQuestion);
        discussionQuestion = (ImageView) findViewById(R.id.discussionQuestion);
        nextBtn = (ImageView) findViewById(R.id.nextBtn);

        //mcqsArrayList = new ArrayList<>();
        userTestResults = new UserTestResults();

        progressBar = (AnimateHorizontalProgressBar) findViewById(R.id.animate_progress_bar);
        progressBar.setMax(msqsListSize);
        progressBar.setProgress(currentQuestion);
        mcqsMainLayout = (LinearLayout) findViewById(R.id.mcqsMainLayout);
        mcqsMainLayout.setVisibility(View.VISIBLE);
        keys = new ArrayList<>();
        userTestResults = (UserTestResults) getIntent().getSerializableExtra("object");
        remainSecond = getIntent().getLongExtra("remainSecond" , 0);
        totalQuestion = getIntent().getIntExtra("totalQuestion" , 0);

        for (Map.Entry<Integer, String> e : userTestResults.selectedSkipAnswerText.entrySet()) {
            int key = e.getKey();
            keys.add(key);
        }

        currentFlagQuestionNumber = keys.get(currentQuestion);
    }


    public void startTime()
    {
        countDownTimer = new CountDownTimer(remainSecond , 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                remainingSecond = millisUntilFinished;

            }

            public void onFinish() {
                finishTest();
            }
        }.start();
    }

    public void optionButtonClickListener()
    {
        QueOptionALayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                QueOptionALayout.setPressed(true);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                userSelectAnswer = true;

                QueOptionALayout.setBackgroundResource(R.drawable.question_option_button_click_background);

                return true;
            }
        });


        QueOptionBLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {




                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(true);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                userSelectAnswer = true;

                QueOptionBLayout.setBackgroundResource(R.drawable.question_option_button_click_background);



                return true;
            }
        });

        QueOptionCLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(true);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                userSelectAnswer = true;

                QueOptionCLayout.setBackgroundResource(R.drawable.question_option_button_click_background);


                return true;
            }
        });

        QueOptionDLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(true);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);
                userSelectAnswer = true;

                QueOptionDLayout.setBackgroundResource(R.drawable.question_option_button_click_background);


                return true;
            }
        });


        QueOptionELayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(true);
                QueOptionFLayout.setPressed(false);
                userSelectAnswer = true;

                QueOptionELayout.setBackgroundResource(R.drawable.question_option_button_click_background);


                return true;
            }
        });


        QueOptionFLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {



                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(true);
                userSelectAnswer = true;

                QueOptionFLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

                return true;
            }
        });

    }

    public void displayQuestionAndAnswer()
    {

        int questionNumberIncremented = currentFlagQuestionNumber +1;

        correctAnswer =  MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getCorrectAnswer();
        noOfQuestion.setText(questionNumberIncremented + " / "+totalQuestion);
        question.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getQuestion());
        //explanation =  MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getExplanation();
        String explain =  MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getExplanation();
        if(explain.equals("null") || explain.equals(""))
        {
            explanation = "Explanation Not Provided";
        }
        else
        {
            explanation = explain ;
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionA().equals("null")||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionA().equals("null"))
        {
            QueOptionALayout.setVisibility(View.GONE);
        }
        else
        {
            questionAText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionA());
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionB().equals("null")||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionB().equals("null"))
        {
            QueOptionBLayout.setVisibility(View.GONE);
        }
        else
        {
            questionBText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionB());
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionC().equals("null") ||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionC().equals("null"))
        {
            QueOptionCLayout.setVisibility(View.GONE);
        }
        else
        {
            questionCText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionC());
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionD().equals("null")||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionD().equals("null"))
        {
            QueOptionDLayout.setVisibility(View.GONE);
        }
        else
        {
            questionDText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionD());
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionE().equals("null")||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionE().equals("null"))
        {
            QueOptionELayout.setVisibility(View.GONE);
        }
        else
        {
            questionEText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionE());
        }

        if(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionF().equals("null")||MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionF().equals("null"))
        {
            QueOptionFLayout.setVisibility(View.GONE);
        }
        else
        {
            questionFText.setText(MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getOptionF());
        }




//        questionAText.setText(mcqsArrayList.get(currentQuestion).getOptionA());
//        questionBText.setText(mcqsArrayList.get(currentQuestion).getOptionB());
//        questionCText.setText(mcqsArrayList.get(currentQuestion).getOptionC());
//        questionDText.setText(mcqsArrayList.get(currentQuestion).getOptionD());
//        questionEText.setText(mcqsArrayList.get(currentQuestion).getOptionE());
//        questionFText.setText(mcqsArrayList.get(currentQuestion).getOptionF());


    }

    public void nextButton(View view)
    {

        QueOptionALayout.setBackgroundResource(R.drawable.question_option_background);
        QueOptionBLayout.setBackgroundResource(R.drawable.question_option_background);
        QueOptionCLayout.setBackgroundResource(R.drawable.question_option_background);
        QueOptionDLayout.setBackgroundResource(R.drawable.question_option_background);
        QueOptionELayout.setBackgroundResource(R.drawable.question_option_background);
        QueOptionFLayout.setBackgroundResource(R.drawable.question_option_background);

        optionAImage.setVisibility(View.GONE);
        optionBImage.setVisibility(View.GONE);
        optionCImage.setVisibility(View.GONE);
        optionDImage.setVisibility(View.GONE);
        optionEImage.setVisibility(View.GONE);
        optionFImage.setVisibility(View.GONE);

        optionAExplanation.setVisibility(View.GONE);
        optionBExplanation.setVisibility(View.GONE);
        optionCExplanation.setVisibility(View.GONE);
        optionDExplanation.setVisibility(View.GONE);
        optionEExplanation.setVisibility(View.GONE);
        optionFExplanation.setVisibility(View.GONE);



        if(!userSelectAnswer)
        {
            Toast.makeText(getApplicationContext(),"Please select Answer",Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setProgress(currentFlagQuestionNumber);


        if (QueOptionALayout.isPressed()) {

            String userAnswer = questionAText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option A");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option A");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is " + correctAnswer + " user anser is " + userAnswer);
        }

        else if (QueOptionBLayout.isPressed()) {

            String userAnswer = questionBText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option B");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option B");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is B" + correctAnswer + " user anser is " + userAnswer);
        }
        else if (QueOptionCLayout.isPressed()) {

            String userAnswer = questionCText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option C");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option C");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is C" + correctAnswer + " user anser is " + userAnswer);
        }
        else if (QueOptionDLayout.isPressed()) {

            String userAnswer = questionDText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option D");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option D");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is D" + correctAnswer + " user anser is " + userAnswer);
        }
        else if (QueOptionELayout.isPressed()) {

            String userAnswer = questionEText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option E");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option E");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is E" + correctAnswer + " user anser is " + userAnswer);
        }
        else if (QueOptionFLayout.isPressed()) {

            String userAnswer = questionFText.getText().toString().trim();
            userTestResults.selectedAnswerText.put(currentFlagQuestionNumber, userAnswer);

            if (correctAnswer.equals(userAnswer)) {

                Log.e("tag", "Correct Answer For Option F");
                userTestResults.numberOfCorrectAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfWrongAnswers.remove(currentFlagQuestionNumber);

            } else {
                Log.d("tag", "Wrong Answer For Option F");
                userTestResults.numberOfWrongAnswers.put(currentFlagQuestionNumber, 1);
                userTestResults.numberOfCorrectAnswers.remove(currentFlagQuestionNumber);

            }
            Log.d("tag", "Correct Answer is F" + correctAnswer + " user anser is " + userAnswer);
        }
//            else
//            {
//                Toast.makeText(this, "you successfully skip the question", Toast.LENGTH_SHORT).show();
//            }



        currentQuestion ++;

        Log.d(TAG,"Current Question Number is "+currentFlagQuestionNumber);
        Log.d(TAG,"Current Question is  "+currentQuestion);

        if( currentQuestion >= userTestResults.selectedSkipAnswerText.size() )
        {
            //finishTest();
            ShowUserProgressDialog();
            return;
        }
        else
        {
            currentFlagQuestionNumber = keys.get(currentQuestion);
            Log.d(TAG,"Quesion Key is "+currentFlagQuestionNumber);
        }

        Log.d("tag","Current Question Number is "+currentQuestion);



        QueOptionALayout.setPressed(false);
        QueOptionBLayout.setPressed(false);
        QueOptionCLayout.setPressed(false);
        QueOptionDLayout.setPressed(false);
        QueOptionELayout.setPressed(false);
        QueOptionFLayout.setPressed(false);


        QueOptionALayout.setVisibility(View.VISIBLE);
        QueOptionBLayout.setVisibility(View.VISIBLE);
        QueOptionCLayout.setVisibility(View.VISIBLE);
        QueOptionDLayout.setVisibility(View.VISIBLE);
        QueOptionELayout.setVisibility(View.VISIBLE);
        QueOptionFLayout.setVisibility(View.VISIBLE);


        userSelectAnswer = false;


        displayQuestionAndAnswer();


        getSelectedNextAnswerAndDisplayToUser();

        //}

    }





    public void getSelectedNextAnswerAndDisplayToUser()
    {
        // now show the selected answer if select by the user

        if( !userTestResults.selectedAnswerText.isEmpty() && userTestResults.selectedAnswerText.containsKey(currentFlagQuestionNumber))
        {

            String userAnswerSelected =  userTestResults.selectedAnswerText.get(currentFlagQuestionNumber);
            // check that user has selected this answer
            userSelectAnswer = true;

            Log.d("tag","User Next selected answer is "+userAnswerSelected+" Question Number is "+currentFlagQuestionNumber +"list size is "+userTestResults.selectedAnswerText.size());

            if(questionAText.getText().toString().trim().equals(userAnswerSelected))
            {

                QueOptionALayout.setPressed(true);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionALayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }

            else if(questionBText.getText().toString().trim().equals(userAnswerSelected))
            {

                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(true);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionBLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
            else  if(questionCText.getText().toString().trim().equals(userAnswerSelected))
            {


                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(true);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionCLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
            else if(questionDText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(true);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionDLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
            else if(questionEText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(true);
                QueOptionFLayout.setPressed(false);

                QueOptionELayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
            else if(questionFText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(true);

                QueOptionFLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
        }
        //}

    }

    public void previousButton(View v)
    {


        if(currentQuestion == 0)
        {
            return;
        }

        currentQuestion --;

        currentFlagQuestionNumber = keys.get(currentQuestion);


        // increment the progress bar
        progressBar.setProgress(currentFlagQuestionNumber);

        QueOptionALayout.setBackgroundResource(R.drawable.question_option_button_click_background);
        QueOptionBLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
        QueOptionCLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
        QueOptionDLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
        QueOptionELayout.setBackgroundResource(R.drawable.question_option_button_click_background);
        QueOptionFLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

        QueOptionALayout.setVisibility(View.VISIBLE);
        QueOptionBLayout.setVisibility(View.VISIBLE);
        QueOptionCLayout.setVisibility(View.VISIBLE);
        QueOptionDLayout.setVisibility(View.VISIBLE);
        QueOptionELayout.setVisibility(View.VISIBLE);
        QueOptionFLayout.setVisibility(View.VISIBLE);


        // call display question and answer method
        // to display the question and answers...

        displayQuestionAndAnswer();
        getSelectedPreviousAnswerAndDisplayToUser();

    }


    public void getSelectedPreviousAnswerAndDisplayToUser()
    {
        // check if user already flag the question..
        // if yes then show it as Skip question..
        // now show the selected answer if select by the user

        if( !userTestResults.selectedAnswerText.isEmpty())
        {
            String userAnswerSelected =  userTestResults.selectedAnswerText.get(currentFlagQuestionNumber);

            // check that user has selected this answer
            userSelectAnswer = true;


            Log.d("tag ","User previously selected answer is "+userAnswerSelected);
            if(questionAText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(true);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionALayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }

            else if(questionBText.getText().toString().trim().equals(userAnswerSelected))
            {

                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(true);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionBLayout.setBackgroundResource(R.drawable.question_option_button_click_background);

            }
            else  if(questionCText.getText().toString().trim().equals(userAnswerSelected))
            {


                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(true);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionCLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
            }
            else if(questionDText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(true);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(false);

                QueOptionDLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
            }
            else if(questionEText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(true);
                QueOptionFLayout.setPressed(false);

                QueOptionELayout.setBackgroundResource(R.drawable.question_option_button_click_background);
            }
            else if(questionFText.getText().toString().trim().equals(userAnswerSelected))
            {
                QueOptionALayout.setPressed(false);
                QueOptionBLayout.setPressed(false);
                QueOptionCLayout.setPressed(false);
                QueOptionDLayout.setPressed(false);
                QueOptionELayout.setPressed(false);
                QueOptionFLayout.setPressed(true);

                QueOptionFLayout.setBackgroundResource(R.drawable.question_option_button_click_background);
            }

        }

    }


    public void skipButton(View v)
    {

//                if(getPressedStateButton(QueOptionALayout))
//                {
//                    if(skipBtn.getText().toString().trim().equals("UnSkip"))
//                    {
//                        Log.d("tag", "flagButtonClick: Remove the Skip Question.");
//                        // if ti exist remove it..
//                        if(userTestResults.selectedSkipAnswerText.containsKey(currentQuestion))
//                            userTestResults.selectedSkipAnswerText.remove(currentQuestion);
//
//
//                        skipBtn.setText("Skip");
//                    }
//
//                    else {
//                        userTestResults.selectedSkipAnswerText.put(currentQuestion, questionAText.getText().toString().trim());
//                        skipBtn.setText("UnSkip");
//                        Log.d("tag", "Skip answer is " + userTestResults.selectedSkipAnswerText.size());
//                    }
//
//                }
//                else if(getPressedStateButton(QueOptionBLayout))
//                {
//
//                    if(skipBtn.getText().toString().trim().equals("UnSkip"))
//                    {
//                        Log.d("tag", "flagButtonClick: Remove the Skip Question.");
//
//                        if(userTestResults.selectedSkipAnswerText.containsKey(currentQuestion))
//                            userTestResults.selectedSkipAnswerText.remove(currentQuestion);
//
//                        skipBtn.setText("Skip");
//                    }
//
//                    else {
//                        userTestResults.selectedSkipAnswerText.put(currentQuestion, questionBText.getText().toString().trim());
//                        skipBtn.setText("UnSkip");
//                        Log.d("tag", "Skip answer is " + userTestResults.selectedSkipAnswerText.size());
//
//                    }
//                }
//                else if(getPressedStateButton(QueOptionCLayout))
//                {
//
//                    if(skipBtn.getText().toString().trim().equals("UnSkip"))
//                    {
//                        Log.d("tag", "flagButtonClick: Remove the Skip Question.");
//
//                        if(userTestResults.selectedSkipAnswerText.containsKey(currentQuestion))
//                            userTestResults.selectedSkipAnswerText.remove(currentQuestion);
//
//                        skipBtn.setText("Skip");
//                    }
//                    else {
//
//                        userTestResults.selectedSkipAnswerText.put(currentQuestion, questionCText.getText().toString().trim());
//                        skipBtn.setText("UnSkip");
//                        Log.d("tag", "Skip answer is " + userTestResults.selectedSkipAnswerText.size());
//
//                    }
//                }
//                else if(getPressedStateButton(QueOptionDLayout))
//                {
//
//                    if(skipBtn.getText().toString().trim().equals("UnSkip"))
//                    {
//                        Log.d("tag", "flagButtonClick: Remove the Skip Question.");
//
//                        if(userTestResults.selectedSkipAnswerText.containsKey(currentQuestion))
//                            userTestResults.selectedSkipAnswerText.remove(currentQuestion);
//
//                        skipBtn.setText("Skip");
//                    }
//                    else {
//                        userTestResults.selectedSkipAnswerText.put(currentQuestion, questionDText.getText().toString().trim());
//                        skipBtn.setText("UnSkip");
//                        Log.d("tag", "Skip answer is " + userTestResults.selectedSkipAnswerText.size());
//
//                    }
//
//                }

        if(getPressedStateButton(QueOptionALayout) || getPressedStateButton(QueOptionBLayout) || getPressedStateButton(QueOptionCLayout) || getPressedStateButton(QueOptionDLayout) || getPressedStateButton(QueOptionELayout) || getPressedStateButton(QueOptionFLayout))
        {
            Toast.makeText(this, "You cannot skip this question because you choose the answer", Toast.LENGTH_SHORT).show();
        }
        else
        {

            if(skipBtn.getText().toString().trim().equals("UnSkip"))
            {
                Log.d("tag", "flagButtonClick: Remove the Skip Question.");

                if(userTestResults.selectedSkipAnswerText.containsKey(currentQuestion))
                    userTestResults.selectedSkipAnswerText.remove(currentQuestion);

                skipBtn.setText("Skip");
                userSelectAnswer = false;
                totalSkippedQuestion.setText(String.valueOf(userTestResults.selectedSkipAnswerText.size()));


            }
            else {
                userTestResults.selectedSkipAnswerText.put(currentQuestion, questionDText.getText().toString().trim());
                skipBtn.setText("UnSkip");
                userSelectAnswer = true;
                nextButton(v);

                Log.e("tag", "Skip answer is " + userTestResults.selectedSkipAnswerText.size());

                totalSkippedQuestion.setText(String.valueOf(userTestResults.selectedSkipAnswerText.size()));

            }

        }

    }


    public void viewAnswerButton(View view)
    {


        if(!userSelectAnswer)
        {
            Toast.makeText(getApplicationContext(),"Please select any one option to view correct answer",Toast.LENGTH_SHORT).show();
            return;

        }
        else {



            if(questionAText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for A");
                QueOptionALayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionAImage.setImageResource(R.drawable.testtick);
                optionAImage.setVisibility(View.VISIBLE);
                optionAExplanation.setText(explanation);
                optionAExplanation.setVisibility(View.VISIBLE);

            }
            else if(questionBText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for B");
                QueOptionBLayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionBImage.setImageResource(R.drawable.testtick);
                optionBImage.setVisibility(View.VISIBLE);
                optionBExplanation.setText(explanation);
                optionBExplanation.setVisibility(View.VISIBLE);


            }
            else if(questionCText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for C");
                QueOptionCLayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionCImage.setImageResource(R.drawable.testtick);
                optionCImage.setVisibility(View.VISIBLE);
                optionCExplanation.setText(explanation);
                optionCExplanation.setVisibility(View.VISIBLE);


            }
            else if(questionDText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for D");
                QueOptionDLayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionDImage.setImageResource(R.drawable.testtick);
                optionDImage.setVisibility(View.VISIBLE);
                optionDExplanation.setText(explanation);
                optionDExplanation.setVisibility(View.VISIBLE);


            }
            else if(questionEText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for E");
                QueOptionELayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionEImage.setImageResource(R.drawable.testtick);
                optionEImage.setVisibility(View.VISIBLE);
                optionEExplanation.setText(explanation);
                optionEExplanation.setVisibility(View.VISIBLE);

            }
            else if(questionFText.getText().toString().equals(correctAnswer))
            {
                Log.e("tag" , "already select correct answer for F");
                QueOptionFLayout.setBackgroundResource(R.drawable.correct_answer_background);
                optionFImage.setImageResource(R.drawable.testtick);
                optionFImage.setVisibility(View.VISIBLE);
                optionFExplanation.setText(explanation);
                optionFExplanation.setVisibility(View.VISIBLE);

            }



            if(questionAText.isPressed())
            {
                if(questionAText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for A");
                }
                else
                {
                    QueOptionALayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionAImage.setImageResource(R.drawable.testcross);
                    optionAImage.setVisibility(View.VISIBLE);
                }
            }

            else if(questionBText.isPressed())
            {
                if(questionBText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for B");
                }
                else
                {
                    QueOptionBLayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionBImage.setImageResource(R.drawable.testcross);
                    optionBImage.setVisibility(View.VISIBLE);

                }
            }

            else if(questionCText.isPressed())
            {
                if(questionCText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for C");
                }
                else
                {
                    QueOptionCLayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionCImage.setImageResource(R.drawable.testcross);
                    optionCImage.setVisibility(View.VISIBLE);

                }
            }

            else if(questionDText.isPressed())
            {
                if(questionDText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for C");
                }
                else
                {
                    QueOptionDLayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionDImage.setImageResource(R.drawable.testcross);
                    optionDImage.setVisibility(View.VISIBLE);

                }
            }

            else if(questionEText.isPressed())
            {
                if(questionEText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for E");
                }
                else
                {
                    QueOptionELayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionEImage.setImageResource(R.drawable.testcross);
                    optionEImage.setVisibility(View.VISIBLE);

                }
            }

            else if(questionFText.isPressed())
            {
                if(questionFText.getText().toString().equals(correctAnswer))
                {
                    Log.e("tag" , "already select correct answer for E");
                }
                else
                {
                    QueOptionFLayout.setBackgroundResource(R.drawable.wrong_answer_background);
                    optionFImage.setImageResource(R.drawable.testcross);
                    optionFImage.setVisibility(View.VISIBLE);

                }
            }

        }

    }


    public boolean getPressedStateButton(LinearLayout l)
    {
        if(l.isPressed())
            return true;
        else
            return false;
    }


    public void ShowSkipDialog(){


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.test_skipped_dialog);
        Button skippedQuestionYes = (Button) dialog.findViewById(R.id.skippedQuestionYes);
        Button skippedQuestionNo = (Button) dialog.findViewById(R.id.skippedQuestionNo);
        dialog.show();
        dialog.setCancelable(false);

        skippedQuestionYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediStudyPreparatoryModeSkipped.this,MediStudyExamModeSkipped.class);
                i.putExtra("object",userTestResults);
                i.putExtra("remainSecond",remainingSecond);
                startActivity(i);
                finish();
            }
        });


        skippedQuestionNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(MediStudyExamMode.this);
//        builder.setTitle("Skipped Questions");
//        builder.setCancelable(false);
//
//        builder.setMessage("Do you want to Review Skipped Questions ");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                Intent i = new Intent(MediStudyExamMode.this,MediStudyExamModeSkipped.class);
//                i.putExtra("object",userTestResults);
//                i.putExtra("remainSecond",remainingSecond);
//                startActivity(i);
//                finish();
//            }
//        })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        dialog.dismiss();
//                        ShowUserProgressDialog();
//
//
//                    }
//                });
//
//        // Create the AlertDialog object and return it
//        builder.create().show();

    }

    ///////////////
    public void ShowUserProgressDialog(){


        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.test_progress_dialog);
        TextView totalCorrectAnswer = (TextView) dialog.findViewById(R.id.totalCorrectAnswer);
        TextView totalWrongAnswer = (TextView) dialog.findViewById(R.id.totalWrongAnswer);
        TextView testResult = (TextView) dialog.findViewById(R.id.testResult);
        Button progressOk = (Button) dialog.findViewById(R.id.progressOk);
        dialog.show();
        dialog.setCancelable(false);



        int correctAnsewerSize = userTestResults.numberOfCorrectAnswers.size() ;

        int p1 = correctAnsewerSize*100;
        int percentage = p1/msqsListSize;

        Log.e("tag" , "test percentage new formula  : "+percentage);

        if(percentage >= 75) {
            totalCorrectAnswer.setText(String.valueOf(userTestResults.numberOfCorrectAnswers.size()));
            totalWrongAnswer.setText(String.valueOf(userTestResults.numberOfWrongAnswers.size()));
            testResult.setText("Pass");
        }
        else
        {
            totalCorrectAnswer.setText(String.valueOf(userTestResults.numberOfCorrectAnswers.size()));
            totalWrongAnswer.setText(String.valueOf(userTestResults.numberOfWrongAnswers.size()));
            testResult.setText("Fail");
        }

        progressOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(MediStudyExamMode.this);
//        builder.setTitle("Current Test");
//        builder.setCancelable(false);
//        if(userTestResults.numberOfCorrectAnswers.size() > TEST_PASS_LIMIT)
//        {
//
//            builder.setMessage(" Progress \n" +
//                    " Correct Answers " + userTestResults.numberOfCorrectAnswers.size()+
//                    " \n Wrong Answers "+ userTestResults.numberOfWrongAnswers.size()+
//                    " \n Test Pass ");
//            Log.d("tag", "Wrong answers are "+userTestResults.numberOfWrongAnswers.size());
//
//        }
//        else
//        {
//            builder.setMessage(" Progress \n" +
//                    " Correct Answers " + userTestResults.numberOfCorrectAnswers.size()+
//                    " \n Wrong Answers "+ userTestResults.numberOfWrongAnswers.size()+
//                    " \n Test Fail ");
//            Log.d("tag", " Correct answers are " + userTestResults.numberOfCorrectAnswers.size());
//
//
//        }
//
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // FIRE ZE MISSILES!
//                dialog.dismiss();
//                // finish the activity..
//                finish();
//
//            }
//        })
//                .setNegativeButton("Review Answers", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//
//
//                        Toast.makeText(MediStudyExamMode.this, "reviews are", Toast.LENGTH_SHORT).show();
//
//
//
////                        // insert data into database...
////                        if (userTestResults.numberOfCorrectAnswers.size() > TEST_PASS_LIMIT) {
////
////
////                            Log.d("tag", "Wrong answers are " + userTestResults.numberOfWrongAnswers.size());
////                            // insert test data into database
////                            if (Splash.dbHelperClass.insertTestRecord("takeTest", "1", "1", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
////                                Log.d(TAG, "Data inserted Successfully");
////                            } else {
////                                Log.d(TAG, "Data not inserted");
////                            }
////                        } else {
////
////                            Log.d(TAG, " Correct answers are " + userChoiceTakeTest.numberOfCorrectAnswers.size());
////                            // insert test data into database
////                            if (Splash.dbHelperClass.insertTestRecord("takeTest", "1", "0", getCurrentDate(), "1", Integer.toString(userChoiceTakeTest.numberOfCorrectAnswers.size()), Integer.toString(userChoiceTakeTest.numberOfWrongAnswers.size()))) {
////                                Log.d(TAG, "Data inserted Successfully");
////                            } else {
////                                Log.d(TAG, "Data not inserted");
////                            }
////
////                        }
////
////
////                        Intent i = new Intent(RandomTest.this, ReviewAnswers.class);
////                        i.putExtra("object", userChoiceTakeTest);
////                        startActivity(i);
////
////                        // finish the activity..
////                        finish();
//
//                    }
//                });
//
//
//        // Create the AlertDialog object and return it
//        builder.create().show();
    }


    private void finishTest()
    {


        if(userTestResults.selectedSkipAnswerText.size() > 0)
        {
            Log.e("tag" , "test not over");
            nextBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.GONE);
            skipBtn.setVisibility(View.GONE);
            viewAnswerBtn.setVisibility(View.GONE);
            testFinishSkipped.setVisibility(View.VISIBLE);
            testFinishSkipped.setText("Skipped");

        }
        else
        {
            timer.setText("Finish!");
            countDownTimer.cancel();
            nextBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.GONE);
            skipBtn.setVisibility(View.GONE);
            viewAnswerBtn.setVisibility(View.GONE);
            testFinishSkipped.setVisibility(View.VISIBLE);
            testFinishSkipped.setText("Finish");

        }

        // stop the timer
        Log.e("tag" , "remaining time is : "+remainingSecond );
        // finish the Test..
        //nextBtn.setText("Finish");

        Log.d("tag", "Number of Correct Answer is " + userTestResults.numberOfCorrectAnswers.size());
        Log.d("tag", "Number of Skipped Questions is " + userTestResults.selectedSkipAnswerText.size());
        Log.d("tag", "Number of Wrong Answer is " + userTestResults.numberOfWrongAnswers.size());

        if(userTestResults.selectedSkipAnswerText.size() > 0) {
            ShowSkipDialog();
        }
        else {
            ShowUserProgressDialog();
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


    public void clickListener()
    {
        reportQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportQuestionDialog("report");
            }
        });

        bookmarkQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = null;
                SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                if (sharedPreferences!=null){
                    userId = sharedPreferences.getString("userid", null);
                }
                String questionId = MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getQuestionId();

                bookmarkedQuestionApi(userId  , questionId);

            }
        });


        notesQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportQuestionDialog("notes");
            }
        });


        shareQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebookAppGroup();
            }
        });


        discussionQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebookAppGroup();

            }
        });
    }

    private void openFacebookAppGroup() {
        String facebookUrl = "https://www.facebook.com/groups/MediCallers/";
        String facebookID = "1716383438664609";

        try {
            int versionCode = getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if(!facebookID.isEmpty()) {
                // open the Facebook app using facebookID (fb://profile/facebookID or fb://page/facebookID)
                Uri uri = Uri.parse("fb://group/" + facebookID);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else if (versionCode >= 3002850 && !facebookUrl.isEmpty()) {
                // open Facebook app using facebook url
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // Facebook is not installed. Open the browser
                Uri uri = Uri.parse(facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            Uri uri = Uri.parse(facebookUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public void reportQuestionDialog(final String type)
    {
        final Dialog dialog = new Dialog(MediStudyPreparatoryModeSkipped.this);
        dialog.setContentView(R.layout.report_question_dialog);
        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        final EditText reportText = dialog.findViewById(R.id.reportQuestionText);
        final EditText reportQuestionTitle = dialog.findViewById(R.id.reportQuestionTitle);
        Button reportQuestionSubmitBtn = dialog.findViewById(R.id.reportQuestionSubmitBtn);
        dialog.show();

        if(type.equals("report"))
        {
            dialogTitle.setText("Report Error");
            reportQuestionTitle.setHint("Report Title");
            reportText.setHint("Report this question");
        }
        else
        {
            dialogTitle.setText("Question Notes");
            reportQuestionTitle.setHint("Notes Title");
            reportText.setHint("Write Notes");
        }


        reportQuestionSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reportText.getText().toString().equals(""))
                {
                    reportText.setError("field should not be empty");
                }
                else
                {
                    String userId = null;
                    SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
                    if (sharedPreferences!=null){
                        userId = sharedPreferences.getString("userid", null);
                    }
                    String title = reportQuestionTitle.getText().toString();
                    String report = reportText.getText().toString();
                    String questionId = MediStudyPreparatoryMode.mcqsArrayList.get(currentFlagQuestionNumber).getQuestionId();




                    Log.e("tag" , "report title is : "+title);
                    Log.e("tag" , "report text is : "+report);
                    Log.e("tag" , "report question Id is : "+questionId);
                    Log.e("tag" , "report userId is : "+userId);

                    if(type.equals("report"))
                    {
                        reportQuestion(dialog , userId , title , report , questionId);
                    }
                    else
                    {
                        notesQuestion(dialog , userId , title , report , questionId);
                    }
                }
            }
        });
    }


    public void reportQuestion(final Dialog alertDialog, final String userId , final String title, final String report , final String id)
    {


        // Tag used to cancel the request
        String cancel_req_tag = "report request";
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.MEDI_STUDY_REPORT_QUESTION , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "report request Response : " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , "Report submit Successfully!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else {

                        String errorMsg = jObj.getString("error_message");

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "report result Error: " + error.getMessage());
                // Toast.makeText(MediStudyExamMode.this, R.string.internetConnection, Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("user_id", userId);
                params.put("mcq_id", id);
                params.put("report_heading", title);
                params.put("report_text", report);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(MediStudyPreparatoryModeSkipped.this).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service


    public void notesQuestion(final Dialog alertDialog,  final String userId  ,final String title , final String report ,final String id)
    {


        // Tag used to cancel the request
        String cancel_req_tag = "notes request";
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.MEDI_STUDY_NOTES_QUESTION , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "notes request Response : " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , "Notes Submit Successfully!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else {

                        String errorMsg = jObj.getString("error_message");

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "notes Error: " + error.getMessage());
                // Toast.makeText(MediStudyExamMode.this, R.string.internetConnection, Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("user_id", userId);
                params.put("mcq_id", id);
                params.put("note_heading", title);
                params.put("note_description", report);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(MediStudyPreparatoryModeSkipped.this).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service





    public void bookmarkedQuestionApi(final String userId  , final String id)
    {


        // Tag used to cancel the request
        String cancel_req_tag = "bookmarked request";
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, Glob.MEDI_STUDY_BOOKMARKED_QUESTION , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "notes request Response : " + response.toString());
                //hideDialog();
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , "bookmarked Successfully!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");

                        Toast.makeText(MediStudyPreparatoryModeSkipped.this , errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "bookmarked Error: " + error.getMessage());
                // Toast.makeText(MediStudyExamMode.this, R.string.internetConnection, Toast.LENGTH_LONG).show();
                //hideDialog();
                dialog.dismiss();
            }
        }) {



            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                params.put("key", Glob.Key);
                params.put("user_id", userId);
                params.put("mcq_id", id);


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(MediStudyPreparatoryModeSkipped.this).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service



}

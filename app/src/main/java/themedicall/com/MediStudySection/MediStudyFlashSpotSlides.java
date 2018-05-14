package themedicall.com.MediStudySection;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.List;
import java.util.Map;

import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyAdapter.MediStudyFlashSpotSlidesAdapter;
import themedicall.com.MediStudyGetterSetter.MediStudyFlashSpotSlidesGetterSetter;
import themedicall.com.R;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

public class MediStudyFlashSpotSlides extends AppCompatActivity {
    RecyclerView recycler_view_flash_spot_slides;
    List<MediStudyFlashSpotSlidesGetterSetter> flashSpotSlidesList;
    LinearLayoutManager linearLayoutManager ;
    String cancel_req_tag = "FlashSpotSlides Service";
    private static final String TAG = "FlashSpotSlides Service";
    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArray;
    CustomProgressDialog dialog;
    String programId ;
    String subjectId ;
    String tagId ;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    private int requestCount = 1;
    int last_page = 0;
    MediStudyFlashSpotSlidesAdapter mediStudyFlashSpotSlidesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_study_flash_spot_slides);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getIdsFromPreviousActivity();
        initiate();
        getFlashSpotSlide(requestCount , tagId , subjectId , programId);
        mediStudyFlashSpotSlidesAdapter =  new MediStudyFlashSpotSlidesAdapter(MediStudyFlashSpotSlides.this , flashSpotSlidesList);
        recycler_view_flash_spot_slides.setAdapter(mediStudyFlashSpotSlidesAdapter);
        scrollListener();
    }

    public void initiate()
    {
        networkDialog = new Dialog(MediStudyFlashSpotSlides.this);
        recycler_view_flash_spot_slides = (RecyclerView) findViewById(R.id.recycler_view_flash_spot_slides);
        recycler_view_flash_spot_slides.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MediStudyFlashSpotSlides.this , LinearLayoutManager.VERTICAL, false);
        recycler_view_flash_spot_slides.setLayoutManager(linearLayoutManager);
        flashSpotSlidesList = new ArrayList<>();
        dialog=new CustomProgressDialog(MediStudyFlashSpotSlides.this , 1);

    }

    public void getIdsFromPreviousActivity()
    {
        Intent intent = getIntent();
        programId = intent.getStringExtra("programId");
        subjectId = intent.getStringExtra("subjectId");
        tagId = intent.getStringExtra("tagId");

        Log.e("tag" , "programId is : "+programId);
        Log.e("tag" , "subjectId is : "+subjectId);
        Log.e("tag" , "tagId is : "+tagId);

    }

    public void setStaticDataToAdapter()
    {

        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));
        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));
        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));
        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));
        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));
        flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter("id" , "Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text Sample Text " , "test"));

        MediStudyFlashSpotSlidesAdapter mediStudyFlashSpotSlidesAdapter =  new MediStudyFlashSpotSlidesAdapter(MediStudyFlashSpotSlides.this , flashSpotSlidesList);
        recycler_view_flash_spot_slides.setAdapter(mediStudyFlashSpotSlidesAdapter);
        mediStudyFlashSpotSlidesAdapter.notifyDataSetChanged();

    }


    public void getFlashSpotSlide(final int count, final String tagId, final String subjectId, final String programId)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "Flash Spot Slide";

        Log.e("tag" , "programId is in service  : "+programId);
        Log.e("tag" , "subjectId is in service : "+subjectId);
        Log.e("tag" , "tagId is in service : "+tagId);

        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.GET_SLIDES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("tag", "Flash Spot Slide Response: " + response.toString());
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        JSONObject slide = new JSONObject(response);
                        JSONObject slideObject = slide.getJSONObject("slides");
                        last_page = slideObject.getInt("last_page");

                        Log.e("tag" , "last page is : "+last_page);

                        JSONArray slideArray = slideObject.getJSONArray("data");


                        for (int i = 0; i < slideArray.length(); i++) {

                            JSONObject slideInnerObject = slideArray.getJSONObject(i);

                            String id = slideInnerObject.getString("id");
                            String text = slideInnerObject.getString("text");
                            String picture = slideInnerObject.getString("picture");




                            flashSpotSlidesList.add(new MediStudyFlashSpotSlidesGetterSetter(id , text , picture));




                        }

                        Log.e("tag" , "slide list size is" + flashSpotSlidesList.size());



                        mediStudyFlashSpotSlidesAdapter.notifyDataSetChanged();

                        requestCount++;


                        // Toast.makeText(getActivity() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(MediStudyFlashSpotSlides.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Flash Spot Slide Error: " + error.getMessage());
                //Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url


                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", Glob.Key);
                params.put("paginator", "10");
                params.put("page", String.valueOf(count));
                params.put("tag_id", tagId);
                params.put("subject_id", subjectId);
                params.put("program_id", programId);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        // Adding request to request queue
        AppSingleton.getInstance(MediStudyFlashSpotSlides.this).addToRequestQueue(strReq, cancel_req_tag);
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

    public void scrollListener()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            recycler_view_flash_spot_slides.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (isLastItemDisplaying(recyclerView)) {
                        if(requestCount <= last_page) {
                            getFlashSpotSlide(requestCount, tagId, subjectId, programId);
                        }
                        else
                        {
                            Toast.makeText(MediStudyFlashSpotSlides.this, "no further record found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recycler_view_flash_spot_slides.setOnScrollChangeListener( new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (isLastItemDisplaying(recycler_view_flash_spot_slides)) {


                        if(requestCount <= last_page)
                        {
                            getFlashSpotSlide(requestCount, tagId, subjectId, programId);
                        }
                        else
                        {
                            Toast.makeText(MediStudyFlashSpotSlides.this , "no further record found", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

}

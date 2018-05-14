package themedicall.com.MediStudySection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import themedicall.com.Blog;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.MediStudyAdapter.MediStudyDownloadBookAdapter;
import themedicall.com.MediStudyGetterSetter.MediStudyDownloadBookGetterSetter;
import themedicall.com.R;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

public class MediStudyBookDownload extends AppCompatActivity {
    RecyclerView recycler_view_download_book;
    List<MediStudyDownloadBookGetterSetter> downloadBookList;
    LinearLayoutManager linearLayoutManager ;
    String cancel_req_tag = "City Service";
    private static final String TAG = "City Service";
    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArray;
    CustomProgressDialog dialog;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_study_book_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiate();
        getMediBook();

    }


    public void initiate()
    {
        networkDialog = new Dialog(MediStudyBookDownload.this);
        recycler_view_download_book = (RecyclerView) findViewById(R.id.recycler_view_download_book);
        recycler_view_download_book.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(MediStudyBookDownload.this , LinearLayoutManager.VERTICAL, false);
        recycler_view_download_book.setLayoutManager(linearLayoutManager);
        downloadBookList = new ArrayList<>();
        dialog=new CustomProgressDialog(MediStudyBookDownload.this , 1);

    }

    public void getMediBook()
    {
        dialog.show();
        strReq = new StringRequest(Request.Method.POST, Glob.MEDI_STUDY_GET_BOOKS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "medibook Service Response: " + response.toString());
                dialog.dismiss();

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("books");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            String id = finalobject.getString("id");
                            String name = finalobject.getString("name");
                            String author = finalobject.getString("author");
                            String link = finalobject.getString("link");
                            String description = finalobject.getString("description");
                            String photo = finalobject.getString("photo");

                            JSONObject subjectObject = finalobject.getJSONObject("subject");
                            String subjectName = subjectObject.getString("name");

                            Log.e("tag" , "medi book id is : "+id);
                            Log.e("tag" , "medi book name is : "+name);
                            Log.e("tag" , "medi book author is : "+author);
                            Log.e("tag" , "medi book link is : "+link);
                            Log.e("tag" , "medi book description is : "+description);
                            Log.e("tag" , "medi book photo is : "+photo);
                            Log.e("tag" , "medi book subjectName is : "+subjectName);

                            downloadBookList.add(new MediStudyDownloadBookGetterSetter(id , photo , link , name , author , description , "0" , subjectName));

                        }

                        MediStudyDownloadBookAdapter mediStudyDownloadBookAdapter = new MediStudyDownloadBookAdapter(MediStudyBookDownload.this , downloadBookList);
                        recycler_view_download_book.setAdapter(mediStudyDownloadBookAdapter);
                        mediStudyDownloadBookAdapter.notifyDataSetChanged();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), "else part of service "+errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e(TAG, "medibook Service Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
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

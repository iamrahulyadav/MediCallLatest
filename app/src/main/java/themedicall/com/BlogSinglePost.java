package themedicall.com;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogSinglePost extends AppCompatActivity {
    TextView singlePostTitle ;
    WebView singlePostDescription;
    String postId , titleName ;
    private final String TAG = "Single Post";
    CustomProgressDialog dialog;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getPostIdFromAdapter();
        initiate();
        getSinglePost(postId);

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
        networkDialog = new Dialog(BlogSinglePost.this);

        singlePostTitle = (TextView) findViewById(R.id.singlePostTitle);
        singlePostDescription = (WebView) findViewById(R.id.singlePostDescription);
        dialog=new CustomProgressDialog(BlogSinglePost.this, 1);

    }

    public void getPostIdFromAdapter()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {

            postId =(String) bd.getString("postId");
            titleName =(String) bd.getString("titleName");
            getSupportActionBar().setTitle(titleName);
            Log.e("tag" , "specific post id is : "+postId);
            Log.e("tag" , "specific post title is : "+titleName);

        }
    }

    public void getSinglePost(String postId)
    {
        String cancel_req_tag = "Single post";
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, "https://themedicall.com/blog/wp-json/wp/v2/posts/"+postId+"?fields=title,content" , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Single post Response: " + response.toString());
                dialog.dismiss();

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject titleObject = jsonObject.getJSONObject("title");
                    String title = titleObject.getString("rendered");


                    JSONObject descriptionObject = jsonObject.getJSONObject("content");
                    String description = descriptionObject.getString("rendered");


                    Log.e("tag" , "here is post title : "+title);
                    Log.e("tag" , "here is post description : "+description);


                    singlePostTitle.setText(title);
                    singlePostDescription.loadData(description.toString() , "text/html","UTF-8");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Blog Categories Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), R.string.internetConnection, Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        }) {

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of get blog categories service


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

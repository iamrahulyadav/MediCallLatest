package themedicall.com;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.SelectSpecialityListRecycleView;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.SelectSpecialityGetterSetter;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;

import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSpecialityForFindDoc extends NavigationDrawer {

    RecyclerView recyclerView_speciality_list;
    List<SelectSpecialityGetterSetter> specialityList;
    private static final String TAG = "SelectSpeciality";
    ProgressDialog progressDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String specialityId ;
    String specialityName ;
    String specialityImg ;
    CustomProgressDialog dialog;
    Dialog noInternetDialog;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_speciality_for_find_doc);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_select_speciality_for_find_doc, null, false);
        drawer.addView(view, 0);

        initiate();
        getSpecialityList();

    }
    public void initiate()
    {
        networkDialog = new Dialog(SelectSpecialityForFindDoc.this);
        dialog=new CustomProgressDialog(SelectSpecialityForFindDoc.this, 1);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        recyclerView_speciality_list = (RecyclerView) findViewById(R.id.recycler_view_speciality_list);
        recyclerView_speciality_list.setHasFixedSize(true);
        recyclerView_speciality_list.setLayoutManager(new GridLayoutManager(SelectSpecialityForFindDoc.this , 2 ,GridLayoutManager.VERTICAL, false ));
        specialityList = new ArrayList<>();


    }

    public void getSpecialityList()
    {

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        //progressDialog.setMessage("Adding you ...");
        //showDialog();
        //loading.setVisibility(View.VISIBLE);
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, Glob.SELECT_SPECIALITY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //hideDialog();
                //loading.setVisibility(View.GONE);
                dialog.dismiss();
                try {


                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("specilaities");


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            specialityId = finalobject.getString("speciality_id");
                            specialityName = finalobject.getString("speciality_designation");
                            specialityImg = finalobject.getString("speciality_icon");

                            specialityList.add(new SelectSpecialityGetterSetter(specialityId , specialityName , specialityImg));

                        }

                        SelectSpecialityListRecycleView selectSpecialityListRecycleView = new SelectSpecialityListRecycleView(SelectSpecialityForFindDoc.this  , specialityList);
                        recyclerView_speciality_list.setAdapter(selectSpecialityListRecycleView);
                        selectSpecialityListRecycleView.notifyDataSetChanged();

                        //Toast.makeText(getApplicationContext() , " You are successfully Added!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jObj.getString("error_message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                //noInternetAccessDialog();
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
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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







    public void noInternetAccessDialog()
    {
        noInternetDialog = new Dialog(SelectSpecialityForFindDoc.this);
        noInternetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noInternetDialog.setContentView(R.layout.no_interner_access_dialog);
        noInternetDialog.setCancelable(false);
        TextView retry = (TextView) noInternetDialog.findViewById(R.id.retry);
        TextView exitDialog = (TextView) noInternetDialog.findViewById(R.id.exitDialog);
        noInternetDialog.show();

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpecialityList();
            }
        });


        exitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

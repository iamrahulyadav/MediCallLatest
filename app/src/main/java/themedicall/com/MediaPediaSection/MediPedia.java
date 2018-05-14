package themedicall.com.MediaPediaSection;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.MediPediaAdapter.GenericBrandCompanyAdapter;
import themedicall.com.NavigationDrawer;
import themedicall.com.R;
import themedicall.com.VolleyLibraryFiles.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MediPedia extends NavigationDrawer {

    ListAdapter adapter;
    LayoutInflater layoutInflater;
    View view;
    GridLayoutManager gridLayoutManager;
    //DividerItemDecoration dividerItemDecoration;
    SearchManager searchManager;
    SearchView search;
    RadioGroup radioGroup;
    GenericBrandCompanyAdapter genericBrandCompanyAdapter;
    RecyclerView recyclerView;
    static boolean gBool = true;
    static boolean bBool = false;
    static boolean cBool = false;

    String generic = "Generic";
    String brand = "Brand";
    String company = "Company";
    CustomProgressDialog dialog;

    public static ArrayList<ListInfo> genericNames = new ArrayList<ListInfo>();
    public static ArrayList<ListInfo> brandNames = new ArrayList<ListInfo>();
    public static ArrayList<ListInfo> companyNames = new ArrayList<ListInfo>();
    ListInfo g;
    ListInfo b;
    ListInfo c;
    /*ArrayList<String> genericsList = new ArrayList<>();
    ArrayList<String> brandsList = new ArrayList<>();
    ArrayList<String> companysList = new ArrayList<>();*/
    //ListView mainList;

    String company_id ;
    String company_name ;

    StringRequest strReq ;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String cancel_req_tag = "City Service";
    private static final String TAG = "City Service";
    ArrayList<ListInfo> listInfos;
    Boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_medi_pedia);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_medi_pedia, null, false);
        drawer.addView(view, 0);


        initiate();
        //radioGroupCheckChangeListener();
        onQueryTextListener();
    }


    private void onQueryTextListener() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MediPedia.this, "SearchView: " + query, Toast.LENGTH_LONG).show();
                genericBrandCompanyAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Toast.makeText(MainActivity.this,"SearchView: "+newText,Toast.LENGTH_LONG).show();
                genericBrandCompanyAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void radioGroupCheckChangeListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.generic_radio){
                    Toast.makeText(MediPedia.this, "Generic ", Toast.LENGTH_SHORT).show();
                    genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,genericNames , generic);
                    recyclerView.setAdapter(genericBrandCompanyAdapter);
                    Log.e("MainActivity","genericBrandCompanyAdapter " + genericBrandCompanyAdapter.infoList.get(0).getName());
                    gBool = true;
                    bBool = false;
                    cBool = false;
                    //.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, genericsList));
                }else if (i == R.id.brand_radio){
                    genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,brandNames , brand);
                    recyclerView.setAdapter(genericBrandCompanyAdapter);
                    Toast.makeText(MediPedia.this, "Brand ", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity","genericBrandCompanyAdapter " + genericBrandCompanyAdapter.infoList.get(0).getName());
                    gBool = false;
                    bBool = true;
                    cBool = false;
                    //mainList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, brandsList));
                }else if (i == R.id.company_radio){
                    Toast.makeText(MediPedia.this, "Company ", Toast.LENGTH_SHORT).show();
//                    genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MainActivity.this,companyNames);
//                    recyclerView.setAdapter(genericBrandCompanyAdapter);
                    Log.e("MainActivity","genericBrandCompanyAdapter " + genericBrandCompanyAdapter.infoList.get(0).getName());
                    gBool = false;
                    bBool = false;
                    cBool = true;

                    if (!loaded) {
                        getCompaniesService();
                    }
//                    else{
//                        genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,listInfos, company);
//                        recyclerView.setAdapter(genericBrandCompanyAdapter);
//                    }

                    //mainList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, companysList));
                }else if (i == -1){
                    Toast.makeText(MediPedia.this, "No Radio Button Selected ", Toast.LENGTH_SHORT).show();
                    /*genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MainActivity.this,genericNames);
                    recyclerView.setAdapter(genericBrandCompanyAdapter);*/
                    //mainList.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, genericsList));
                    //radioGroup.clearCheck();
                }
            }
        });
    }

    public void initiate(){

        dialog=new CustomProgressDialog(MediPedia.this, 1);
        listInfos = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(MediPedia.this , 2);
        //dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), gridLayoutManager.getOrientation());
        genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,genericNames, generic);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        genericNames.add(new ListInfo("Huzaifa",""+1));
        companyNames.add(new ListInfo("Huzaifa",""+1));
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setQueryHint("Search");
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(genericBrandCompanyAdapter);
        radioGroup.check(R.id.brand_radio);

        brandNames.add(new ListInfo("1","PLASIL"));
        brandNames.add(new ListInfo("1","CRAFILM"));
        brandNames.add(new ListInfo("1","FAMIDOL"));
        brandNames.add(new ListInfo("1","ZANIDIP"));
        brandNames.add(new ListInfo("1","GENURIN FORTE"));
        brandNames.add(new ListInfo("1","CINQUIN"));
        brandNames.add(new ListInfo("1","CLOMID"));
        brandNames.add(new ListInfo("1","POLYMALT"));
        brandNames.add(new ListInfo("1","ESKEM"));
        brandNames.add(new ListInfo("1","COSMOQUIN"));
        brandNames.add(new ListInfo("1","DOLOPRIN-75"));
        brandNames.add(new ListInfo("1","LEVOPRAID"));
        brandNames.add(new ListInfo("1","LORTEM"));
        brandNames.add(new ListInfo("1","CALDREE"));
        brandNames.add(new ListInfo("1","CYRIN"));
        brandNames.add(new ListInfo("1","MIXEL"));
        brandNames.add(new ListInfo("1","AUGMENTIN"));
        brandNames.add(new ListInfo("1","BRUFEN"));
        brandNames.add(new ListInfo("1","ASIPIRIN"));
        brandNames.add(new ListInfo("1","SYNFLEX"));
        brandNames.add(new ListInfo("1","PANADOL"));
        brandNames.add(new ListInfo("1","FEXET-D"));
        brandNames.add(new ListInfo("1","CORIC"));
        brandNames.add(new ListInfo("1","NOVATUS"));
        brandNames.add(new ListInfo("1","RIFIN-P"));
        brandNames.add(new ListInfo("1","RIFUCIN"));
        brandNames.add(new ListInfo("1","VELOXIN"));
        brandNames.add(new ListInfo("1","XIBEN"));

        genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,brandNames , brand);
        recyclerView.setAdapter(genericBrandCompanyAdapter);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    //Companies List
    public void getCompaniesService()
    {
        dialog.show();

        strReq = new StringRequest(Request.Method.POST, Glob.url   , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "City Service Response: " + response.toString());

                dialog.dismiss();

                try {


                    jsonObject = new JSONObject(response);
                    String total = jsonObject.getString("total");

                    Log.e("tag" , "total data "+total);

                    boolean error = jsonObject.getBoolean("error");

                    if (!error) {

                        jsonArray = jsonObject.getJSONArray("companies");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject finalobject = jsonArray.getJSONObject(i);

                            company_id = finalobject.getString("company_id");
                            company_name = finalobject.getString("company_name");

                            listInfos.add(new ListInfo(company_id , company_name));
                        }

                        Log.e("tag" , "total compnies size "+listInfos.size());
                        genericBrandCompanyAdapter = new GenericBrandCompanyAdapter(MediPedia.this,listInfos, company);
                        recyclerView.setAdapter(genericBrandCompanyAdapter);
                        loaded = true;

                        //Toast.makeText(getApplicationContext() , "Cities Added Successfully!", Toast.LENGTH_SHORT).show();

                    } else {

                        String errorMsg = jsonObject.getString("error_message");
                        Toast.makeText(getApplicationContext(), "else part of service "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "City Service Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "onErrorResponse "+error.getMessage(), Toast.LENGTH_LONG).show();
                dialog.dismiss();

                //getCitiesService();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url



                Map<String, String> params = new HashMap<String, String>();
                //params.put("doctor_user_name", signUpUserNameText);
                params.put("key", "Vx0cbjkzfQpyTObY8vfqgN1us");
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

}

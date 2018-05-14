package themedicall.com;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import themedicall.com.Adapter.BlogHeadingAdapter;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.GetterSetter.BlogHeadingGetterSetter;
import themedicall.com.GetterSetter.BlogPostGetterSetter;
import themedicall.com.Globel.CircleTransformPicasso;
import themedicall.com.Globel.CustomProgressDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.VolleyLibraryFiles.AppSingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Blog extends NavigationDrawer {

    ArrayList<BlogHeadingGetterSetter> allSampleData;
    View customActionBarView ;
    SearchView searchView;
    Button locationFilter;
    ImageView userIcon , doctorFilterImage , searchViewImg;
    RelativeLayout.LayoutParams params;
    private final String TAG = "SignIn";
    CustomProgressDialog dialog;
    ArrayList<BlogHeadingGetterSetter> blogCategoriesList ;
    ArrayList<BlogHeadingGetterSetter> blogCategoriesList1 ;

    BlogHeadingGetterSetter blogHeadingGetterSetter ;
    BlogPostGetterSetter blogPostGetterSetter;
    RecyclerView my_recycler_view ;
    int count ;
    String finalImageOfPost = null;
    JSONArray postJsonArray;
    boolean isLast = false;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;

    ArrayList<HashMap<String, String>> mainIdAndTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_blog);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM );
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_navigation_drawer);
        customActionBarView =getSupportActionBar().getCustomView();
        getSupportActionBar().setElevation(0);


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_blog, null, false);
        drawer.addView(view, 0);


        initiate();
        //createDummyData();
        getBlogCategories();
        setImageInActionbar();
    }

    public void initiate()
    {
        networkDialog = new Dialog(Blog.this);
        dialog=new CustomProgressDialog(Blog.this, 1);
        allSampleData = new ArrayList<BlogHeadingGetterSetter>();
        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        locationFilter = (Button) customActionBarView.findViewById(R.id.locationFilter);
        userIcon = (ImageView) customActionBarView.findViewById(R.id.userIcon);
        doctorFilterImage = (ImageView) customActionBarView.findViewById(R.id.doctorFilterImage);
        searchViewImg = (ImageView) customActionBarView.findViewById(R.id.searchViewImg);
        searchView = (SearchView) customActionBarView.findViewById(R.id.searchView);

        params = (RelativeLayout.LayoutParams)userIcon.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        locationFilter.setVisibility(View.GONE);
        doctorFilterImage.setVisibility(View.GONE);
        searchViewImg.setVisibility(View.GONE);
        blogCategoriesList = new ArrayList<>();
        blogCategoriesList1 = new ArrayList<>();
        mainIdAndTitle = new ArrayList<>();

    }



    public void createDummyData() {
        for (int i = 1; i <= 5; i++) {

            blogHeadingGetterSetter = new BlogHeadingGetterSetter();
            blogHeadingGetterSetter.setTitleName("Section " + i);
            blogHeadingGetterSetter.setTitleId( ""+i);

            ArrayList<BlogPostGetterSetter> singleItem = new ArrayList<BlogPostGetterSetter>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new BlogPostGetterSetter("id " + j, "Title " + j , "http://themedicall.com/blog/wp-content/uploads/2018/01/symptoms2.png"));
            }

            blogHeadingGetterSetter.setAllBlogInCategory(singleItem);
            allSampleData.add(blogHeadingGetterSetter);
        }
        BlogHeadingAdapter adapter = new BlogHeadingAdapter(Blog.this, allSampleData);
        my_recycler_view.setAdapter(adapter);

    }

    //calling werb serviec for get blog categories
    public void getBlogCategories()
    {
        String cancel_req_tag = "blog";
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, Glob.BLOG_CATEGORIES , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Blog Categories Response: " + response.toString());
                dialog.dismiss();

                try {
                    JSONArray jsonArray =  new JSONArray(response);

                    for (int count = 0; count < jsonArray.length(); count++) {

                        HashMap<String, String> idAndtitle  = new HashMap<>();

                        JSONObject obj = jsonArray.getJSONObject(count);
                        String id = obj.getString("id");
                        String categoryName = obj.getString("name");

                        Log.e("tag" , "category id : "+id);
                        Log.e("tag" , "category name : "+categoryName);

                      /*  blogHeadingGetterSetter.setTitleId(id);
                        blogHeadingGetterSetter.setTitleName(categoryName);*/

                        idAndtitle.put("id", id);
                        idAndtitle.put("name", categoryName);

                        //adding in new list
                        mainIdAndTitle.add(idAndtitle);

                        // getPostOfCategories(id);
                        Log.e("tag" , "log in category service");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i<mainIdAndTitle.size(); i++){

                    final int j = i;

                    BlogHeadingGetterSetter blogHeadingGetterSetter = new BlogHeadingGetterSetter();
                    ArrayList<BlogPostGetterSetter> postList ;
                    postList = new ArrayList<>();

                    getPostOfCategories(mainIdAndTitle.get(i).get("id"), mainIdAndTitle.get(i).get("name"), blogHeadingGetterSetter, postList, new CallBackPost() {
                        @Override
                        public void onSuccess(BlogHeadingGetterSetter blogHeadingGetterSetter) {

                            blogCategoriesList.add(blogHeadingGetterSetter);
                            if (blogCategoriesList.size() == j+1);{

                                BlogHeadingAdapter adapter = new BlogHeadingAdapter(Blog.this, blogCategoriesList);
                                my_recycler_view.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }
                        @Override
                        public void onFail(String msg) {

                        }
                    });

                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Blog Categories Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), R.string.internetConnection, Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        }) {

        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of get blog categories service


    public void getPostOfCategories(final String id, final String title, final BlogHeadingGetterSetter blogHeadingGetterSetter, final ArrayList<BlogPostGetterSetter> myPostList ,final CallBackPost callBackPost) {

        // Tag used to cancel the request
        String cancel_req_tag = "blog";

        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, "http://themedicall.com/blog/wp-json/wp/v2/posts?categories=" + id + "&fields=id,title,featured_media,better_featured_image", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Post of Categories Response: " + response.toString());
                dialog.dismiss();

                try {
                    postJsonArray = new JSONArray(response);

                    Log.e("tag" , "log in post service");

                    for (count = 0; count < postJsonArray.length(); count++) {

                        BlogPostGetterSetter blogPostGetterSetter = new BlogPostGetterSetter();

                        JSONObject obj = postJsonArray.getJSONObject(count);
                        String id = obj.getString("id");
                        String featured_media = obj.getString("featured_media");

                        JSONObject titleObject = obj.getJSONObject("title");
                        String title = titleObject.getString("rendered");



                        JSONObject betterFeaturedImageObject = obj.getJSONObject("better_featured_image");
                        String image_source_url = betterFeaturedImageObject.getString("source_url");



                        Log.e("tag", "blog post id : " + id);
                        Log.e("tag", "blog post featured_media : " + featured_media);
                        Log.e("tag", "blog post title : " + title);
                        Log.e("tag", "blog post image url : " + image_source_url);



                        blogPostGetterSetter.setId(id);
                        blogPostGetterSetter.setTitle(title);
                        blogPostGetterSetter.setUrl(image_source_url);
                        myPostList.add(blogPostGetterSetter);

                    }

                    Log.e("TAG", "the size of post list data is: " + myPostList.size());

                    if(myPostList.size()>0) {

                        blogHeadingGetterSetter.setTitleId(id);
                        blogHeadingGetterSetter.setTitleName(title);
                        blogHeadingGetterSetter.setAllBlogInCategory(myPostList);
                        callBackPost.onSuccess(blogHeadingGetterSetter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                Toast.makeText(getApplicationContext(), "data fetch of blog post !", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Post of Categories Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), R.string.internetConnection, Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        }) {

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public interface CallBackPost {
        void onSuccess(BlogHeadingGetterSetter blogHeadingGetterSetter);
        void onFail(String msg);
    }


    public void setImageInActionbar()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
        if (sharedPreferences!=null){

            String userId = sharedPreferences.getString("userid", null);
            if (userId!=null){
                String userFullName = sharedPreferences.getString("userfullname", null);
                String profile_img = sharedPreferences.getString("profile_img", null);
                final String userTable = sharedPreferences.getString("usertable", null);

                final String PROFILE_IMAGE_URL = Glob.IMAGE_BACK_URL+profile_img;
                Log.e("TAg", "the Profile Image is url is: " + PROFILE_IMAGE_URL);


                Picasso.with(this).load(PROFILE_IMAGE_URL).placeholder(R.drawable.loginuser).transform(new CircleTransformPicasso()).into(userIcon);

                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (userTable.equals(getResources().getString(R.string.doctors))) {

                            Intent intent = new Intent(Blog.this , UpdateDoctorProfile.class);
                            startActivity(intent);

                            //starting service for getting all data from server
                        }

                        if (userTable.equals(getResources().getString(R.string.patients))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }

                        if (userTable.equals(getResources().getString(R.string.labs))) {


                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }


                        if (userTable.equals(getResources().getString(R.string.hospitals))) {

                           /* Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.pharmacies))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.blood_donors))) {

                            /*Intent intent = new Intent(NavigationDrawer.this, MedicalRecord.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.ambulances))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                        if (userTable.equals(getResources().getString(R.string.health_professionals))) {

                          /*  Intent intent = new Intent(NavigationDrawer.this, UpdateDoctorProfile.class);
                            startActivity(intent);*/

                        }
                    }
                });

            }
            else {
                imageView.setImageResource(R.drawable.loginuser);
                userIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Blog.this , SignIn.class);
                        startActivity(intent);
                    }
                });
            }
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

}

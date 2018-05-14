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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import themedicall.com.Adapter.BlogPostForSpecificCategoryAdapter;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.GetterSetter.BlogPostGetterSetter;
import themedicall.com.Globel.Glob;

import java.util.ArrayList;

public class BlogPostForSpecificCategory extends AppCompatActivity {
    RecyclerView recycler_view_post_for_specific_category ;
    ArrayList<BlogPostGetterSetter> postArray ;
    String categoryName ;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post_for_specific_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getPostListFromAdapter();
        initiate();

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

        networkDialog = new Dialog(BlogPostForSpecificCategory.this);

        recycler_view_post_for_specific_category = findViewById(R.id.recycler_view_post_for_specific_category);
        recycler_view_post_for_specific_category.setHasFixedSize(true);
        recycler_view_post_for_specific_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        BlogPostForSpecificCategoryAdapter blogPostForSpecificCategory = new BlogPostForSpecificCategoryAdapter(this , postArray);
        recycler_view_post_for_specific_category.setAdapter(blogPostForSpecificCategory);
        blogPostForSpecificCategory.notifyDataSetChanged();
    }

    public void getPostListFromAdapter()
    {
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {

            postArray =(ArrayList) bd.getParcelableArrayList("postList");
            categoryName = (String) bd.get("categoryName");
            getSupportActionBar().setTitle(categoryName);
            Log.e("tag" , "post list size in post for specific category : "+postArray.size());

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

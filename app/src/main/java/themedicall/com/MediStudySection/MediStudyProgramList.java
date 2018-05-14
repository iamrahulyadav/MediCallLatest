package themedicall.com.MediStudySection;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import themedicall.com.Blog;
import themedicall.com.BroadCasts.CheckConnectivity;
import themedicall.com.BroadCasts.MyReceiverForNetworkDialog;
import themedicall.com.Globel.Glob;
import themedicall.com.Home;
import themedicall.com.MediPediaAdapter.GenericBrandCompanyAdapter;
import themedicall.com.MediPediaGetterSetter.ListInfo;
import themedicall.com.R;

public class MediStudyProgramList extends AppCompatActivity {
    ImageView programmbbs , programfcps , programmdms , programnebone , programnebtwo , programusmle , programplab , programbsn , mediStudyDownloadImg , mediStudyForumImg;
    Dialog networkDialog;
    MyReceiverForNetworkDialog myReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_study_program_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiate();
        clickListener();

    }

    public void initiate()
    {
        programmbbs = (ImageView) findViewById(R.id.programmbbs);
        programfcps = (ImageView) findViewById(R.id.programfcps);
        programmdms = (ImageView) findViewById(R.id.programmdms);
        programnebone = (ImageView) findViewById(R.id.programnebone);
        programnebtwo = (ImageView) findViewById(R.id.programnebtwo);
        programusmle = (ImageView) findViewById(R.id.programusmle);
        programplab = (ImageView) findViewById(R.id.programplab);
        programbsn = (ImageView) findViewById(R.id.programbsn);
        mediStudyDownloadImg = (ImageView) findViewById(R.id.mediStudyDownloadImg);
        mediStudyForumImg = (ImageView) findViewById(R.id.mediStudyForumImg);
        networkDialog = new Dialog(MediStudyProgramList.this);

    }

    public void clickListener()
    {
        programmbbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "5");
//                startActivity(intent);

                comingSoonDialog();
            }
        });

        programfcps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
                intent.putExtra("programId" , "1");
                startActivity(intent);
            }
        });

        programmdms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "2");
//                startActivity(intent);

                comingSoonDialog();

            }
        });


        programnebone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "3");
//                startActivity(intent);

                comingSoonDialog();

            }
        });

        programnebtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "4");
//                startActivity(intent);

                comingSoonDialog();

            }
        });

        programusmle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "9");
//                startActivity(intent);

                comingSoonDialog();

            }
        });

        programplab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "8");
//                startActivity(intent);

                comingSoonDialog();

            }
        });

        programbsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudy.class);
//                intent.putExtra("programId" , "6");
//                startActivity(intent);


                comingSoonDialog();


            }
        });

        mediStudyDownloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MediStudyProgramList.this , MediStudyBookDownload.class);
//                startActivity(intent);

                comingSoonDialog();

            }
        });

        mediStudyForumImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebookAppGroup();
            }
        });
    }


    public void comingSoonDialog()
    {
        final Dialog dialog = new Dialog(MediStudyProgramList.this);
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

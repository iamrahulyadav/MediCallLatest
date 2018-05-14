package themedicall.com.BroadCasts;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import themedicall.com.R;
public class MyReceiverForNetworkDialog extends BroadcastReceiver {
    Dialog dialog;
    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

        int datapassed = intent.getIntExtra("DATAPASSED", 0);
        dialog = new Dialog(context);


        if (datapassed == 1234){


            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.internet_connection_dialog);
            dialog.setCancelable(false);
            TextView enable = (TextView) dialog.findViewById(R.id.enable);
            TextView exit = (TextView) dialog.findViewById(R.id.exit);
            dialog.show();

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
                    System.exit(0);
                }
            });

        }

        if(datapassed == 12345)
        {
            Log.e("tag", "data passed form check connectivity : "+datapassed);
            dialog.cancel();
        }

    }
}
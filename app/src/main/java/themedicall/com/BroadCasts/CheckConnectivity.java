package themedicall.com.BroadCasts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import themedicall.com.Globel.Glob;
import themedicall.com.Globel.NetworkUtil;

public class CheckConnectivity extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        if (status.equals("Not connected to Internet")){

            createNetErrorDialog(context);
        }
        else if(status.equals("Wifi enabled"))
        {
            removeNetErrorDialog(context);
        }
        else if(status.equals("Mobile data enabled"))
        {
            removeNetErrorDialog(context);
        }

        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        Log.e("TAG", "the intern connection status is: " + status);
    }

    protected void createNetErrorDialog(final Context context) {

        Intent intent = new Intent();
        intent.setAction(Glob.MY_ACTION);
        intent.putExtra("DATAPASSED", 1234);//sending netwrok state
        context.sendBroadcast(intent);

    }

    protected void removeNetErrorDialog(final Context context) {

        Intent intent = new Intent();
        intent.setAction(Glob.MY_ACTION);
        intent.putExtra("DATAPASSED", 12345);//sending netwrok state
        context.sendBroadcast(intent);

    }


}
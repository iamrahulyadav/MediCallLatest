package themedicall.com.BroadCasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import themedicall.com.Globel.DatabaseHelperForMedAlarm;
import themedicall.com.Home;

/**
 * Created by Muhammad Adeel on 3/22/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    static Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("TAG", "Allarm Is running");


        int alarmId = intent.getExtras().getInt("alarmId");
        Log.e("TAG", "alarmId: " + alarmId);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMMMyyyyhh:mm:ss");
        String date = sdf.format(calendar.getTime());
        Log.e("tag" , "calender time is get time in millis in alarm receiver : "+ date);


        DatabaseHelperForMedAlarm databaseHelperForMedAlarm = new DatabaseHelperForMedAlarm(context);
        String data = databaseHelperForMedAlarm.getMedNameAndQuantity(date);

        String[] separated = data.split(",");
        String medName = separated[0];
        String quantity = separated[1];
        Log.e("tag" , "medicine name and quantity is in alarm receiver: "+ data);
        Log.e("tag" , "medicine name is in alarm receiver : "+ medName);
        Log.e("tag" , "medicine quantity is in receiver : "+ quantity);




//        intent = new Intent();
//        intent.setClass(context, Home.class); //Test is a dummy class name where to redirect
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("alarmId", alarmId);
//        context.startActivity(intent);



        //MainActivity.getTextView2().setText("Enough Rest. Do Work Now!");
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, uri);
        ringtone.play();
    }
}

package themedicall.com.Globel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import themedicall.com.GetterSetter.CitiesGetterSetter;
import themedicall.com.GetterSetter.MedAlarmGetterSetter;

/**
 * Created by Muhammad Adeel on 3/28/2018.
 */

public class DatabaseHelperForMedAlarm extends SQLiteOpenHelper {
    public Context mContext;
    String name , quantity ;
    String noOfMedTakeString;
    String noOfMedSkipString;

    public static final String DATABASE_NAME = "MedAlarm.db";
    private static final int DatabaseVersion = 2;
    private static final String NAME_OF_TABLE = "alarmTable";
    public static final String Col_1 = "med_id";
    public static final String Col_2 = "med_name";
    public static final String Col_3 = "med_quantity";
    public static final String Col_4 = "med_time";
    public static final String Col_5 = "med_date_with_time";
    public static final String Col_6 = "med_take";
    public static final String Col_7 = "med_skip";
    public static final String Col_8 = "med_date";

    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE + "(" + Col_1 + " integer PRIMARY KEY AUTOINCREMENT," + Col_2 + " TEXT," + Col_3 + " TEXT," + Col_4 + " TEXT," +  Col_5 + " TEXT," +  Col_6 + " TEXT," + Col_7 + " TEXT," + Col_8 + " TEXT" +")";


    public DatabaseHelperForMedAlarm(Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.mContext = context;


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CALL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);

    }

    //inserting post in databse
    public long insertDataInTable(String medicineName, String spinnerText, String alarmTime, String dateWithTime, String medicineTake, String medicineSkip , String date) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_2, medicineName);
        values.put(Col_3, spinnerText);
        values.put(Col_4, alarmTime);
        values.put(Col_5, dateWithTime);
        values.put(Col_6, medicineTake);
        values.put(Col_7, medicineSkip);
        values.put(Col_8, date);
        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();

        return result;

    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        return db.rawQuery(query, null).getCount();
    }


    public String getMedNameAndQuantity(String date)
    {
        //String query = "SELECT * FROM " + NAME_OF_TABLE + " Where " + Col_5 + "=" + date;

        String query = "SELECT * FROM alarmTable Where med_date_with_time = '" + date + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                name = c.getString(c.getColumnIndex(Col_2));
                quantity = c.getString(c.getColumnIndex(Col_3));

                Log.e("tag", "getMedNameAndQuantity:  " + name  + "  " +quantity);
            }
        }

        db.close();
        return name + " , " + quantity;
    }

    public List<MedAlarmGetterSetter> getMedicine(String time, String date)
    {
        String query = "SELECT * FROM alarmTable Where med_time = '" + time + "'" + "AND med_date = '" + date + "'";
        List<MedAlarmGetterSetter> medicine = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                String medName = c.getString(c.getColumnIndex(Col_2));
                String medTake = c.getString(c.getColumnIndex(Col_6));
                String medSkip = c.getString(c.getColumnIndex(Col_7));
                medicine.add(new MedAlarmGetterSetter(medName, medTake , medSkip));
            }
        }

        db.close();
        return medicine;
    }


    public String NoOfMedTake(String time, String date)
    {
        String query = "SELECT * FROM alarmTable Where med_time = '" + time + "'" + "AND med_date = '" + date + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
               //   noOfMedTakeString = c.getString(c.getColumnIndex(Col_6));
                noOfMedTakeString = String.valueOf(c.getCount());
            }
        }
        db.close();
        return noOfMedTakeString;
    }

    public String NoOfMedSkip(String time, String date)
    {
        String query = "SELECT * FROM alarmTable Where med_time = '" + time + "'" + "AND med_date = '" + date + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                //noOfMedSkipString = c.getString(c.getColumnIndex(Col_7));
                noOfMedSkipString = String.valueOf(c.getCount());
            }
        }
        db.close();
        return noOfMedSkipString;
    }


}

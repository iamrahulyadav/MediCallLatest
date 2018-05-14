package themedicall.com.Globel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import themedicall.com.GetterSetter.CitiesGetterSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shoaib anwar on 05-Feb-18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public Context mContext;

    public static final String DATABASE_NAME = "cities.db";
    private static final int DatabaseVersion = 1;
    private static final String NAME_OF_TABLE = "cityTable";
    public static final String Col_1 = "id";
    public static final String Col_2 = "cityid";
    public static final String Col_3 = "cityname";

    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE + "(" + Col_1 + " integer PRIMARY KEY AUTOINCREMENT," + Col_2 + " TEXT, " + Col_3 + " TEXT" + ")";


    public DatabaseHelper(Context context) {
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
    public long insertCitiesInTable(CitiesGetterSetter post) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_2, post.getId());
        values.put(Col_3, post.getName());
        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();
        return result;

    }



    /* fetching records from Database Table*/
    public List<CitiesGetterSetter> getAllPots() {
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        List<CitiesGetterSetter> cityGS = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                String cityId = c.getString(c.getColumnIndex(Col_2));
                String cityName = c.getString(c.getColumnIndex(Col_3));
                cityGS.add(new CitiesGetterSetter(cityId, cityName));
            }
        }

        db.close();
        return cityGS;

    }

    //fetch signle city from db
    public CitiesGetterSetter getSinglecity(String cityId) {
        String query = "SELECT * FROM " + NAME_OF_TABLE + " Where " + Col_2 + "=" + cityId;
        CitiesGetterSetter cityGS = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                String cID = c.getString(c.getColumnIndex(Col_2));
                String cityName = c.getString(c.getColumnIndex(Col_3));
                cityGS = new CitiesGetterSetter(cID, cityName);
            }
        }

        db.close();
        return cityGS;

    }


    //Updatating post
    public boolean updateTable(int id, String postToUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,postToUpdate);
        db.update(NAME_OF_TABLE, contentValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    //deleting post
    public boolean deleteFromTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_OF_TABLE, Col_1 + "=" + rowId, null);
        db.close();

        return true;

    }

    public int getCount(){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        return db.rawQuery(query, null).getCount();
    }





}
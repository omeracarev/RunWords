package org.newest.runwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String STATICS = "STATICS";

    public static final String STATICS_ID = "STATICS_ID";

    public static final String STATICS_COR = "STATICS_COR";

    public static final String STATICS_WRO = "STATICS_WRO";

    public static final String STATICS_RUN = "STATICS_RUN";

    public static final String STATICS_TIME = "STATICS_TIME";

    public static final String CURRENTS = "CURRENTS";

    public static final String CURRENTS_ID = "CURRENTS_ID";

    public static final String CURRENTS_COR = "CURRENTS_COR";

    public static final String CURRENTS_WRO = "CURRENTS_WRO";

    public static final String CURRENTS_RUN = "CURRENTS_RUN";

    public static final String ADS = "ADS";

    public static final String AD_ID = "AD_ID";

    public static final String AD_TYPE = "AD_TYPE";
    public static final String AD_QUOTA = "AD_QUOTA";

    public static final String AD_DATE = "AD_DATE";

    public static final String USERTABLE   = "USERTABLE";

    public static final String USER_ID     = "USER_ID";

    public static final String USER_NAME   = "USER_NAME";

    public static final String USER_WHICH  = "USER_WHICH";





    public static final String DATABASE_NAME = "runwords.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + STATICS + " (" + STATICS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STATICS_COR + " TEXT, " + STATICS_WRO + " TEXT, " + STATICS_RUN + " TEXT," + STATICS_TIME + " TEXT) ";
        db.execSQL(createTableStatement);

        String createTableStatement2 = "CREATE TABLE " + CURRENTS + " (" + CURRENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CURRENTS_COR + " TEXT, " + CURRENTS_WRO + " TEXT, " + CURRENTS_RUN + " TEXT)";
        db.execSQL(createTableStatement2);

        String createTableStatement3 = "CREATE TABLE " + ADS + " (" + AD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AD_QUOTA + " TEXT, " + AD_DATE + " TEXT," + AD_TYPE + " TEXT)";
        db.execSQL(createTableStatement3);

        String createTableStatement4 = "CREATE TABLE IF NOT EXISTS " + USERTABLE + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, " + USER_WHICH + " TEXT)";
        db.execSQL(createTableStatement4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (newVersion > oldVersion){
            if (oldVersion < 2 && newVersion >= 2){
                String createTableStatement4 = "CREATE TABLE IF NOT EXISTS " + USERTABLE + " (" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, " + USER_WHICH + " TEXT)";
                sqLiteDatabase.execSQL(createTableStatement4);
            }

        }

    }

    public static void deleteDatabase(Context mContext) {
        mContext.deleteDatabase(DATABASE_NAME);
    }

    public boolean AddUser(checkUsers checkUsers){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USER_NAME, checkUsers.getUsername());
        long insert = db.insert(USERTABLE, null, cv);

        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean DeleteUser(checkUsers checkUsers){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+USERTABLE+" WHERE "+ USER_ID +" = "+ checkUsers.getUserid();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }

    }

    public List<checkUsers> getCheckUsers(){
        List<checkUsers> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ USERTABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                checkUsers checkUsers = new checkUsers(id,name);
                returnList.add(checkUsers);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return  returnList;
    }

    public boolean AddStatics(Statics statics){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STATICS_COR,statics.getCorcount());
        cv.put(STATICS_WRO,statics.getWrocount());
        cv.put(STATICS_RUN,statics.getRuncount());
        cv.put(STATICS_TIME,statics.getTimecount());
        long insert = db.insert(STATICS, null, cv);

        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean AddCurrents(Currents currents){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CURRENTS_COR,currents.getCorcount());
        cv.put(CURRENTS_WRO,currents.getWrocount());
        cv.put(CURRENTS_RUN,currents.getRuncount());
        long insert = db.insert(CURRENTS, null, cv);

        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean AddAds(Ads ads){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AD_QUOTA,ads.getAd_quota());
        cv.put(AD_DATE,ads.getAd_date());
        cv.put(AD_TYPE,ads.getAd_type());
        long insert = db.insert(ADS, null, cv);

        if (insert == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }

    public boolean deleteStatics(Statics statics){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+STATICS+" WHERE "+ STATICS_ID +" = "+ statics.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean deleteCurrents(Currents currents){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+CURRENTS+" WHERE "+ CURRENTS_ID +" = "+ currents.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean deleteAds(Ads ads){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+ADS+" WHERE "+ AD_ID +" = "+ ads.getAd_id();
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public List<Statics> getStatics(){
        List<Statics> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ STATICS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String curcount = cursor.getString(1);
                String wrocount = cursor.getString(2);
                String runcount = cursor.getString(3);
                String timecount = cursor.getString(4);

                Statics newnotarama = new Statics(id,curcount,wrocount,runcount,timecount);
                returnList.add(newnotarama);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return  returnList;
    }

    public List<Currents> getCurrents(){
        List<Currents> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ CURRENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String curcount = cursor.getString(1);
                String wrocount = cursor.getString(2);
                String runcount = cursor.getString(3);

                Currents newnotarama = new Currents(id,curcount,wrocount,runcount);
                returnList.add(newnotarama);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return  returnList;
    }

    public List<Ads> getAds(){
        List<Ads> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM "+ ADS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String curcount = cursor.getString(1);
                String wrocount = cursor.getString(2);
                String runcount = cursor.getString(3);

                Ads newnotarama = new Ads(id,curcount,wrocount,runcount);
                returnList.add(newnotarama);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return  returnList;
    }
}

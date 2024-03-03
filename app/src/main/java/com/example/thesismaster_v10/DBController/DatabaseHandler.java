package com.example.thesismaster_v10.DBController;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.thesismaster_v10.Model.ThesisModel;
import com.example.thesismaster_v10.ThesisActivity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "Thesismaster.db";
    private static final String THESIS_TABLE = "thesistable";
    private static final String ID = "id";
    private static final String THESIS = "thesis";
    private static final String DUEDATE = "duedate";
    private static final String USER = "user";
    private static final String STARS = "stars";
    private static final String CREATE_THESIS_TABLE = "CREATE TABLE " + THESIS_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + THESIS + " TEXT, "
            + DUEDATE + " TEXT, " + USER + " TEXT, " + STARS + " INTEGER)";


    private SQLiteDatabase MyDB;

    public DatabaseHandler(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL(CREATE_THESIS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS " + THESIS_TABLE);
        onCreate(MyDB);
    }
    public void openDatabase() {
        MyDB = this.getWritableDatabase();
    }


    public void insertThesis(ThesisModel thesis){
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(THESIS, thesis.getThesis());
        contentValues.put(DUEDATE, thesis.getDuedate());
        contentValues.put(USER, thesis.getUser());
        contentValues.put(STARS, thesis.getStars());
        MyDB.insert(THESIS_TABLE, null, contentValues);
    }


    public List<ThesisModel> getAllUserThesis(String userLog){
        List<ThesisModel> thesisList = new ArrayList<>();
        String userDB;
        //SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cur = null;
        MyDB.beginTransaction();
        try{
            cur = MyDB.query(THESIS_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        userDB = cur.getString(cur.getColumnIndex(USER));
                        if ((userDB!=null) && (userDB.equals(userLog))) {
                            ThesisModel thesis = new ThesisModel();

                            thesis.setId(cur.getInt(cur.getColumnIndex(ID)));
                            thesis.setThesis(cur.getString(cur.getColumnIndex(THESIS)));
                            thesis.setDuedate(cur.getString(cur.getColumnIndex(DUEDATE)));
                            thesis.setUser(cur.getString(cur.getColumnIndex(USER)));
                            thesis.setStars(cur.getInt(cur.getColumnIndex(STARS)));

                            thesisList.add(thesis);
                        }
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            MyDB.endTransaction();
            assert cur != null;
            cur.close();
        }
        return thesisList;
    }


    public void updateThesis(int id, String thesis) {
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("thesis", thesis);
        MyDB.update("thesistable", contentValues, "id = ?", new String[] {String.valueOf(id)});
    }
    public void updateDueDate(int id, String duedate) {
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("duedate", duedate);
        MyDB.update("thesistable", contentValues, "id = ?", new String[] {String.valueOf(id)});
    }

    public void updateStars(String thesis, int stars) {
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("stars", stars);
        MyDB.update("thesistable", contentValues, "thesis = ?", new String[]{thesis});
    }

    public void deleteThesis(int id){
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("thesistable", "id = ?", new String[] {String.valueOf(id)});
    }

    public Boolean checkthesis (String thesis){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from thesistable where thesis = ?", new String[]{thesis});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public int checkStars (String thesis){
        //SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = null;;
        int stars = 0;

        MyDB.beginTransaction();
        try{
            cursor = MyDB.rawQuery("Select * from thesistable where thesis = ?", new String[]{thesis});
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        stars = cursor.getInt(cursor.getColumnIndex(STARS));

                    }
                    while(cursor.moveToNext());
                }
            }
        }
        finally {
            MyDB.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return stars;
    }
}

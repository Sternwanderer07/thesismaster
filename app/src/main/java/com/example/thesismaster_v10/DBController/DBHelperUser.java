package com.example.thesismaster_v10.DBController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thesismaster_v10.Model.UserModel;

public class DBHelperUser extends SQLiteOpenHelper {

    public static final String DBNAME = "UserLogin.db";
    private static final int VERSION = 1;
    private static final String USER_TABLE = "usertable";
    private static final String ID = "id";
    private static final String USER = "username";
    private static final String PASSWORD = "password";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER + " TEXT, "
            + PASSWORD + " TEXT)";

    private SQLiteDatabase db;

    public DBHelperUser(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }
    public void openDatabase() {
        db = this.getWritableDatabase();
    }
    public Boolean insertUserData (UserModel user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER, user.getUsername());
        contentValues.put(PASSWORD, user.getPassword());
        long result = db.insert(USER_TABLE, null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    public boolean checkusername (String username){
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from usertable where username = ?", new String[]{username});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public boolean checkusernamepassword (String username, String password){
        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from usertable where username = ? and password = ?", new String[]{username,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
}

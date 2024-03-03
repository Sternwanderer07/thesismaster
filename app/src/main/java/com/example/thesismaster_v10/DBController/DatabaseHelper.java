package com.example.thesismaster_v10.DBController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thesismaster_v10.Model.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DBNAME = "Question.db";

    private static final String Q_TABLE = "questiontable";
    private static final String ID = "id";
    private static final String QUESTION = "question";
    private static final String STATUS = "status";
    private static final String THESIS = "thesis";
    private static final String THEME = "theme";
    private static final String QNOTE = "qnote";

    private static final String CREATE_Q_TABLE = "CREATE TABLE " + Q_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION + " TEXT, " + STATUS + " INTEGER, " + THESIS + " TEXT, " + THEME + " TEXT, " + QNOTE + " TEXT)";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context){
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_Q_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS" + Q_TABLE);
        onCreate(database);
    }

    public void openDatabase(){
        database = this.getWritableDatabase();
    }

    public void insertQuestion(QuestionModel question){
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION, question.getQuestion());
        contentValues.put(STATUS, question.getStatus());
        contentValues.put(THESIS, question.getThesis());
        contentValues.put(THEME, question.getTheme());
        contentValues.put(QNOTE, question.getQnote());

        database.insert(Q_TABLE, null, contentValues);
    }

    public List<QuestionModel> getAllThesisQuestions(String thesis){
        List<QuestionModel> questionList = new ArrayList<>();
        Cursor cur = null;
        database.beginTransaction();
        try{
            cur = database.query(Q_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        if (cur.getString(cur.getColumnIndex(THESIS)).equals(thesis)) {
                            QuestionModel question = new QuestionModel();
                            question.setId(cur.getInt(cur.getColumnIndex(ID)));
                            question.setQuestion(cur.getString(cur.getColumnIndex(QUESTION)));
                            question.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                            question.setThesis(cur.getString(cur.getColumnIndex(THESIS)));
                            question.setTheme(cur.getString(cur.getColumnIndex(THEME)));
                            question.setQnote(cur.getString(cur.getColumnIndex(QNOTE)));
                            questionList.add(question);
                        }
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            assert cur != null;
            cur.close();
        }
        return questionList;
    }
    public List<QuestionModel> getAllDoneThesisQuestions(String thesis){
        List<QuestionModel> questionList = new ArrayList<>();
        Cursor cur = null;
        database.beginTransaction();
        try{
            cur = database.query(Q_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        if (cur.getString(cur.getColumnIndex(THESIS)).equals(thesis)) {

                                if (cur.getInt(cur.getColumnIndex(STATUS)) == 1) {
                                    QuestionModel question = new QuestionModel();
                                    question.setId(cur.getInt(cur.getColumnIndex(ID)));
                                    question.setQuestion(cur.getString(cur.getColumnIndex(QUESTION)));
                                    question.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                                    question.setThesis(cur.getString(cur.getColumnIndex(THESIS)));
                                    question.setTheme(cur.getString(cur.getColumnIndex(THEME)));
                                    question.setQnote(cur.getString(cur.getColumnIndex(QNOTE)));
                                    questionList.add(question);
                                }

                        }
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            assert cur != null;
            cur.close();
        }
        return questionList;
    }

    public List<QuestionModel> getAllOpenQuestions(String thesis, String theme){

        List<QuestionModel> questionList = new ArrayList<>();
        Cursor cur = null;
        database.beginTransaction();
        try{
            cur = database.query(Q_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        if (cur.getString(cur.getColumnIndex(THESIS)).equals(thesis)){
                            if (cur.getString(cur.getColumnIndex(THEME)).equals(theme)){
                                if (cur.getInt(cur.getColumnIndex(STATUS)) == 0){
                                    QuestionModel question = new QuestionModel();
                                    question.setId(cur.getInt(cur.getColumnIndex(ID)));
                                    question.setQuestion(cur.getString(cur.getColumnIndex(QUESTION)));
                                    question.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                                    question.setThesis(cur.getString(cur.getColumnIndex(THESIS)));
                                    question.setTheme(cur.getString(cur.getColumnIndex(THEME)));
                                    question.setQnote(cur.getString(cur.getColumnIndex(QNOTE)));
                                    questionList.add(question);
                                }
                            }
                        }
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            assert cur != null;
            cur.close();
        }
        return questionList;
    }

    public List<QuestionModel> getAllDoneQuestions(String thesis, String theme){
        List<QuestionModel> questionList = new ArrayList<>();
        Cursor cur = null;
        database.beginTransaction();
        try{
            cur = database.query(Q_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        if (cur.getString(cur.getColumnIndex(THESIS)).equals(thesis)) {
                            if (cur.getString(cur.getColumnIndex(THEME)).equals(theme)) {
                                if (cur.getInt(cur.getColumnIndex(STATUS)) == 1) {
                                    QuestionModel question = new QuestionModel();
                                    question.setId(cur.getInt(cur.getColumnIndex(ID)));
                                    question.setQuestion(cur.getString(cur.getColumnIndex(QUESTION)));
                                    question.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                                    question.setThesis(cur.getString(cur.getColumnIndex(THESIS)));
                                    question.setTheme(cur.getString(cur.getColumnIndex(THEME)));
                                    question.setQnote(cur.getString(cur.getColumnIndex(QNOTE)));
                                    questionList.add(question);
                                }
                            }
                        }
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            database.endTransaction();
            assert cur != null;
            cur.close();
        }
        return questionList;
    }
    public void updateStatus(int id, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        database.update(Q_TABLE, contentValues, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateQuestion(int id, String question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION, question);
        database.update(Q_TABLE, contentValues, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateQnote(int id, String qnote) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QNOTE, qnote);
        database.update(Q_TABLE, contentValues, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        database.delete(Q_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}

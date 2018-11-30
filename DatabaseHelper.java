package com.example.violetang.navigationbuttom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "todo.db";
    private static final String TASK_TABLE_NAME = "task_table";
    private static final String T_ID = "T_ID";
    private static final String T_Name = "T_NAME";
    private static final String T_Desc = "T_DES";
    private static final String T_Date = "T_DATE";
    private static final String T_Status = "T_STATUS";
    private static final String T_List = "T_LIST";

    private String create_task_table_statement = "CREATE TABLE " + TASK_TABLE_NAME + " ( "
            + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T_Name + " TEXT NOT NULL, "
            + T_Desc + " TEXT, "
            + T_Date + " TEXT, "
            + T_Status +" INTEGER, "
            + T_List + " TEXT " + " );";

    private static final String LIST_TABLE_NAME = "task_table";
    private static final String L_ID = "L_ID";
    private static final String L_Name = "L_NAME";
    private static final String L_Desc = "L_DES";

    private String create_list_table_statement = "CREATE TABLE " + LIST_TABLE_NAME + " ( "
            + L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + L_Name + "TEXT NOT NULL, "
            + L_Desc + "TEXT" + " );";
    private String createUnsortedList_statement = "INSERT INTO " + LIST_TABLE_NAME
            + " ( " + L_ID + ", " + L_Name + ", " + L_Desc + " ) "
            + "VALUES ( 0, 'Unsorted', " + L_Desc + " );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //sql create all the tables and cols
        //create the task table
        db.execSQL(create_task_table_statement);
        db.execSQL(create_list_table_statement);
        db.execSQL(createUnsortedList_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //changes in database;
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        onCreate(db);
    }

    //parameter can be a task object
    public boolean addTaskData( String name, String des, String date, String list){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(T_Name, name);
        contentValues.put(T_Desc, des);
        contentValues.put(T_Date, date);
        contentValues.put(T_Status, 0);
        contentValues.put(T_List, list);

        long result = db.insert(TASK_TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addListData( String name, String des ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(L_Name, name);
        contentValues.put(L_Desc, des);

        long result = db.insert(LIST_TABLE_NAME,null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public  Cursor getTodoTask(String date, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TASK_TABLE_NAME
                        + " WHERE T_Date = ?" + " AND T_Status = ?",
                new String[]{date, String.valueOf(status)});
    }

    public  Cursor getTaskByList(String listname, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TASK_TABLE_NAME
                        + " WHERE " + T_List + " = ?"
                        + " AND " + T_Status + " = ?",
                new String[] {listname, String.valueOf(status)});
    }

    public Cursor getListByID(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + LIST_TABLE_NAME + " WHERE " + L_ID + " = ?",
                new String[] {String.valueOf(ID)});
    }

    public Cursor getAllLists(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + LIST_TABLE_NAME, null);
    }

    public Cursor editTask(int ID, String name, String des, String date, String list){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("UPDATE " + TASK_TABLE_NAME
                        + " SET " + T_Name + " = ?, "
                        + T_Desc + " = ?, "
                        + T_Date + " = ?, "
                        + T_List + " = ? "
                        + " WHERE " + T_ID + " = ?",
                new String[] {name, des, date, list, String.valueOf(ID)});
    }

    public boolean setTaskComplete(){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.rawQuery("UPDATE " + TASK_TABLE_NAME
                    + " SET " + T_Status + " = 1",
                    null);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public boolean setTaskIncomplete(){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.rawQuery("UPDATE " + TASK_TABLE_NAME
                            + " SET " + T_Status + " = 0",
                    null);
            return true;
        }
        catch(Exception e){

            return false;
        }
    }

    public void deleteTaskbyID(int TaskID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TASK_TABLE_NAME
                        + " WHERE " + T_ID + " = ?",
                new String[] {String.valueOf(TaskID)});
    }

    public void deleteListbyID(int ListID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + LIST_TABLE_NAME
                        + " WHERE " + L_ID + " = ?",
                new String[] {String.valueOf(ListID)});
    }
}

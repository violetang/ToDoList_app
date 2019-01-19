package com.example.violetang.navigationbuttom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author: Jiali & Jack
 * Date: Nov. 2018
 * Description: SQLite - create tables, queries
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "todo.db"; // Create the database


    //Task table: id,name,des, date, status, list
    private static final String TASK_TABLE_NAME = "task_table";
    private static final String TASK_COL0_ID= "ID";
    private static final String TASK_COL1_NAME= "NAME";
    private static final String TASK_COL2_DES= "DES";
    private static final String TASK_COL3_DATE= "DATE";
    private static final String TASK_COL4_STATUS = "STATUS";
    private static final String TASK_COL5_LIST= "LIST";

    private String create_task_table_statement =
            "CREATE TABLE "+ TASK_TABLE_NAME +" ( "+
                    TASK_COL0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    TASK_COL1_NAME+ " TEXT NOT NULL, "+
                    TASK_COL2_DES+" TEXT, "+
                    TASK_COL3_DATE+" TEXT, "+
                    TASK_COL4_STATUS+" INTEGER NOT NULL, "+
                    TASK_COL5_LIST+" INTEGER );";


    //List table
    private static final String LIST_TABLE_NAME = "list_table";
    private static final String L_ID = "L_ID";
    private static final String L_Name = "L_NAME";
    private static final String L_Desc = "L_DES";

    private String create_list_table_statement =
            "CREATE TABLE "+ LIST_TABLE_NAME +" ( "+
                    L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    L_Name+ " TEXT NOT NULL, "+
                    L_Desc+" TEXT );";

    //person table
    private static final String PERSON_TABLE_NAME = "person_table";
    private static final String PERSON_INFO_ID ="ID";
    private static final String PERSON_INFO_USER = "USERNAME";
    private static final String PERSON_INFO_NAME = "NICKNAME";
    private static final String PERSON_INFO_EMAIL = "EMAIL";
    private static final String PERSON_INFO_INSTRUCTION = "INSTRUCTION";

    private String create_person_table_statement =
            "CREATE TABLE IF NOT EXISTS "+ PERSON_TABLE_NAME +" ( "+
                    PERSON_INFO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    PERSON_INFO_USER + " TEXT NOT NULL, "+
                    PERSON_INFO_NAME+ " TEXT NOT NULL, "+
                    PERSON_INFO_EMAIL+" TEXT, "+
                    PERSON_INFO_INSTRUCTION + " TEXT );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_task_table_statement);
        db.execSQL(create_list_table_statement);
        db.execSQL(create_person_table_statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //changes in database;
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db);
    }

    //Add task by name, des, date
    public boolean addTaskData( String name, String des, String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COL1_NAME,name);
        contentValues.put(TASK_COL2_DES,des);
        contentValues.put(TASK_COL3_DATE,date);
        contentValues.put(TASK_COL4_STATUS,0);

        long result = db.insert(TASK_TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Add task by name, des, list_id
    public boolean addTaskInList( String name, String des, int list_id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COL1_NAME,name);
        contentValues.put(TASK_COL2_DES,des);
        contentValues.put(TASK_COL5_LIST,list_id);
        contentValues.put(TASK_COL4_STATUS,0);

        long result = db.insert(TASK_TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //Get task by date and status
    public Cursor getTaskbyDate(String date, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+ TASK_TABLE_NAME +
                        " WHERE  "+ TASK_COL3_DATE+ "= ?" +
                        " AND "+ TASK_COL4_STATUS+" = ?",
                new String[]{date, String.valueOf(status)});
    }

    //get task by id
    public Cursor getTaskbyId(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+
                TASK_TABLE_NAME+
                " WHERE "+ TASK_COL0_ID + " = ?", new String[]{String.valueOf(index)});

        return cursor;
    }

    //delete task by id
    public void deleteTaskbyId(int index){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "
                        + TASK_TABLE_NAME
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {String.valueOf(index)});
    }

    //update task without list
    public void updateTaskByIdNoList(int index, String name, String des, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + TASK_TABLE_NAME + " SET "
                        + TASK_COL1_NAME + " = ?, "
                        + TASK_COL2_DES + " = ?, "
                        + TASK_COL3_DATE + " = ? "
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {name, des, date, String.valueOf(index)});
    }

    //update task with list
    public void updateTaskByIdWithList(int index, String name, String des, int list){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + TASK_TABLE_NAME + " SET "
                        + TASK_COL1_NAME + " = ?, "
                        + TASK_COL2_DES + " = ?, "
                        + TASK_COL5_LIST + " = ? "
                        + " WHERE " + TASK_COL0_ID + " = ?",
                new String[] {name, des, String.valueOf(list), String.valueOf(index)});
    }

    //Add list
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

    //return lists
    public Cursor getLists(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+LIST_TABLE_NAME, null);
        return cursor;
    }

    //return list by id
    public Cursor getListById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+
                LIST_TABLE_NAME+
                " WHERE "+ L_ID + " = ?", new String[]{String.valueOf(id)});

        return cursor;

    }

    //delete list by id
    public void deleteListbyId(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "
                        + LIST_TABLE_NAME
                        + " WHERE " + L_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    //update list by id
    public void updateListById(int id, String name, String des){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + LIST_TABLE_NAME + " SET "
                        + L_Name + " = ?, "
                        + L_Desc + " = ? "
                        + " WHERE " + L_ID + " = ?",
                new String[] {name, des, String.valueOf(id)});
    }


    //get task by list_id and status
    public  Cursor getTaskByList(int listid, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+ TASK_TABLE_NAME +
                        " WHERE "+ TASK_COL5_LIST + " = ?" +
                        " AND "+ TASK_COL4_STATUS+" = ?",
                new String[]{String.valueOf(listid), String.valueOf(status)});
    }

    public Cursor showTaskData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor task_data = db.rawQuery("SELECT * FROM "+TASK_TABLE_NAME, null);
        return task_data;
    }


    //change task status to 1
    public void setTaskComplete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 1"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                    new String[] {String.valueOf(id)});
    }

    //change task status to 0
    public void setTaskIncomplete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TASK_TABLE_NAME
                        + " SET " + TASK_COL4_STATUS + " = 0"
                        + " WHERE " + TASK_COL0_ID + " = ?",
                    new String[] {String.valueOf(id)});
    }

    //Method to add person info
    public boolean addPersonData(String userName, String name, String email, String instruction){
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(PERSON_INFO_USER, userName);
        contentValue.put(PERSON_INFO_NAME, name);
        contentValue.put(PERSON_INFO_EMAIL,email);
        contentValue.put(PERSON_INFO_INSTRUCTION,instruction);
        long result = db1.insert(PERSON_TABLE_NAME,null,contentValue);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    //return person info
    public Cursor showUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor user_data = db.rawQuery("SELECT * FROM "+PERSON_TABLE_NAME, null);
        return user_data;
    }

    //update user info
    public void updateUserInfo(int index, String userName, String nickName, String email, String instruction){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "
                        + PERSON_TABLE_NAME + " SET " + PERSON_INFO_USER + " = ?, "
                        + PERSON_INFO_NAME + " = ?, " + PERSON_INFO_EMAIL + " = ?, "
                        + PERSON_INFO_INSTRUCTION + " = ? " + " WHERE "
                        + PERSON_INFO_ID + " = ?",
                new String[]{userName, nickName, email, instruction, String.valueOf(index)});
    }


}

package com.example.todoapp.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todoapp.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="TODO_DB";
    private static final String TABLE_NAME="TODO_TABLE";
    private static final String COL_1="ID";
    private static final String COl_2="TASK";
    private static final String COl_3="STATUS";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT,STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addTask(TodoModel model){
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COl_2,model.getTask());
        contentValues.put(COl_3,0);
        db.insert(TABLE_NAME,null,contentValues);
    }
    public void updateTask(int id,String task){
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COl_2,task);
        db.update(TABLE_NAME,contentValues,"ID+?",new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id,int status){
        db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COl_3,status);
        db.update(TABLE_NAME,contentValues,"ID+?",new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID+?",new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")

    public List<TodoModel> getAllTasks(){
        db=this.getWritableDatabase();
        Cursor cursor=null;
        List<TodoModel> todoModelList=new ArrayList<>();
        db.beginTransaction();
        try{
            cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do {
                        TodoModel task=new TodoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COl_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COl_3)));
                        todoModelList.add(task);
                    }while(cursor.moveToNext());
                }
            }

        }
        finally {
            db.endTransaction();
            cursor.close();
        }
        return todoModelList;
    }



}

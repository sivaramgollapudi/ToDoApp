package com.sivaram.todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import com.sivaram.todoapp.utils.Constants;

/**
 * Created by User on 07/10/2017.
 */

// Class Extends From SQLiteOpenHelper Class
public class AppDatabase extends SQLiteOpenHelper {


    // Create Context and Query String To Create Database n Table
    Context context;
    String sqlQuery  = "CREATE TABLE IF NOT EXISTS " + Constants.TODO_LIST + " ( " +
                        Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TITLE + " TEXT, " +
                        Constants.DESCRIPTION + " TEXT , " +
                        Constants.ACTIONDATE + " TEXT , " +
                        Constants.STATUS + " INT ) " ;

    // Default Constructor to Create Database And Table
    public AppDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    // Override Method executes first time app launch
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlQuery);
    }

    // Updated Based on Version Number from Constants Class
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        context.deleteDatabase(Constants.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}

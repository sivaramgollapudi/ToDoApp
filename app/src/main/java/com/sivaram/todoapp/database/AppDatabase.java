package com.sivaram.todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import com.sivaram.todoapp.utils.Constants;

/**
 * Created by User on 07/10/2017.
 */

public class AppDatabase extends SQLiteOpenHelper {

    Context context;
    String sqlQuery  = "CREATE TABLE IF NOT EXISTS " + Constants.TODO_LIST + " ( " +
                        Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Constants.TITLE + " TEXT, " +
                        Constants.DESCRIPTION + " TEXT , " +
                        Constants.ACTIONDATE + " TEXT , " +
                        Constants.STATUS + " INT ) " ;

    public AppDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        context.deleteDatabase(Constants.DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }
}

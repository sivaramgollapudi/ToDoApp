package com.sivaram.todoapp.utils;

import android.content.Context;
import android.widget.Toast;

import com.sivaram.todoapp.database.DBHelper;

/**
 * Created by User on 07/10/2017.
 */

public class CommonUtilities {

    public static DBHelper getDBObject(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        if (dbHelper != null)
            Toast.makeText(context, "CommonUtilies.getDBObject...", Toast.LENGTH_SHORT).show();
        return dbHelper;
    }
}

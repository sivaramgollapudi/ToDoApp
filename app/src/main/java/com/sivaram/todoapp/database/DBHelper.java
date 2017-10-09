package com.sivaram.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.sivaram.todoapp.utils.Constants;

/**
 * Created by User on 07/10/2017.
 */

public class DBHelper {

    private SQLiteDatabase db;
    private final Context context;
    private final AppDatabase dbHelper;
    private static DBHelper db_helper;

    public DBHelper(Context context) {
        this.context = context;
        // this will create a new DB for you..
        dbHelper = new AppDatabase(context, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);

    }

    public static  DBHelper getInstance(Context context){
        try{
            if(db_helper == null){
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  db_helper;
    }

    public void open() throws SQLiteException {
        try{
            db = dbHelper.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
            db= dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        if(db.isOpen()){
            db.close();
        }
    }

    public long insertToDoList(String tableName, ContentValues contentValues){
        long id = 0 ;
        try{
            db.beginTransaction();
            id = db.insert(tableName, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return id;
    }

    public Cursor getToDoRecords(String tableName, String[] columns, String where, String orderby){
        Cursor c = db.query(false,tableName, columns, where, null,null,null, orderby, null);
        if (db == null) Toast.makeText(context, "DB IS NULL", Toast.LENGTH_SHORT).show();
        return c;
    }

    public int updateRecord(String tableName,ContentValues contentValues,String where,String[] whereArgs){
        int rowCount = 0;
        try{
            db.beginTransaction();
            rowCount = db.update(tableName, contentValues, where, whereArgs);
            db.setTransactionSuccessful();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return rowCount;
    }

    public void deleteRecord(String tableName, String where, String[] whereArgs){
        try{
            db.beginTransaction();
            db.delete(tableName,where,whereArgs);
            db.setTransactionSuccessful();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            db.endTransaction();
        }
    }
}

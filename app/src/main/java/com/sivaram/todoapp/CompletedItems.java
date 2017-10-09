package com.sivaram.todoapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sivaram.todoapp.database.DBHelper;
import com.sivaram.todoapp.utils.CommonUtilities;
import com.sivaram.todoapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 09/10/2017.
 */

public class CompletedItems extends AppCompatActivity{
    // Completed Items Activity.

    ListView completedToDoListView;
    ToDoListCustomAdapter completedToDoListCustomAdapter;
    DBHelper dbHelper;
    List<ToDoList> completedToDoListItems;
    String[] columnNames = new String[]{
            Constants.ID.toString(),
            Constants.TITLE.toString(),
            Constants.DESCRIPTION.toString(),
            Constants.ACTIONDATE.toString(),
            Constants.STATUS.toString()
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_items);
        setTitle("ToDo - Completed");

        dbHelper = CommonUtilities.getDBObject(this);

        getAllCompletedItems();

        completedToDoListView = (ListView) findViewById(R.id.completedListView);

        completedToDoListCustomAdapter  = new ToDoListCustomAdapter(getApplicationContext(), completedToDoListItems);
        completedToDoListView.setAdapter(completedToDoListCustomAdapter);

        completedToDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                deleteCompletedItem(position);
                return true;
            }
        });
    }

    // Delete Spceific Record.
    private void deleteCompletedItem(int position){
        dbHelper.deleteRecord(Constants.TODO_LIST, "KEY_ID=" + Integer.parseInt(completedToDoListItems.get(position).getId()),null);
        getAllCompletedItems();
        completedToDoListCustomAdapter.notifyDataSetChanged();
    }

    // Get COmpleted List Items From Database By Key Status = 1
    private void getAllCompletedItems(){
        Cursor c = dbHelper.getToDoRecords(Constants.TODO_LIST,columnNames,"KEY_STATUS=1","KEY_DATE");
        completedToDoListItems = new ArrayList();

        c.moveToFirst();
        if (c!= null && c.getCount() > 0){
            do{
                String id = String.valueOf(c.getInt(c.getColumnIndex(Constants.ID)));
                String title = c.getString(c.getColumnIndex(Constants.TITLE));
                String description = c.getString(c.getColumnIndex(Constants.DESCRIPTION));
                String actionDate = c.getString(c.getColumnIndex(Constants.ACTIONDATE));
                int status = c.getInt(c.getColumnIndex(Constants.STATUS));

                String row = title + " " + c.getString(c.getColumnIndex(Constants.DESCRIPTION)) + " " +
                        c.getString(c.getColumnIndex(Constants.ACTIONDATE)) + " " + c.getInt(c.getColumnIndex(Constants.STATUS));

                // Updated to To Do List Class andAdd to Custom Adapter.

                ToDoList todoList = new ToDoList(id, title.toString(), description.toString(), actionDate.toString(), status);

                completedToDoListItems.add(todoList);
            }while(c.moveToNext());
        }
    }
}

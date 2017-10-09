package com.sivaram.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sivaram.todoapp.database.DBHelper;
import com.sivaram.todoapp.utils.CommonUtilities;
import com.sivaram.todoapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView toDoListView;
    List<ToDoList> toDoListItems;
    ToDoListCustomAdapter toDoListCustomAdapter;
    AlertDialog dialog;
    EditText titleEditText, descriptionEditText;
    DatePicker taskDatePicker;
    DBHelper dbHelper;

    String[] columnNames = new String[]{
            Constants.ID.toString(),
            Constants.TITLE.toString(),
            Constants.DESCRIPTION.toString(),
            Constants.ACTIONDATE.toString(),
            Constants.STATUS.toString()
    };

    String selectedRowID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = CommonUtilities.getDBObject(this);

        getAllToDoListItems();

        toDoListView = (ListView) findViewById(R.id.toDoListView);

        toDoListCustomAdapter  = new ToDoListCustomAdapter(getApplicationContext(), toDoListItems);
        toDoListView.setAdapter(toDoListCustomAdapter);

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showAddItemDialog(true, position);
            }
        });

        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                updateToDoItemStatus(position);
                return true;
            }
        });
    }

    //Inflate Menu Which will Allow to add new Item.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.todolist_menu,menu);

        return true;
    }

    //  On Item Selected from Custom Adapter View. Based on the Menu Item. Status WIll be updated.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("New")){
            showAddItemDialog(false,-1);
        }
        else if (item.getTitle().toString().equals("Completed")){
            showCompletedItems();
        }

        return true;
    }


    // Show Completed Items Activity via intent.
    private void showCompletedItems(){
        Intent completedItemsIntent = new Intent(MainActivity.this, CompletedItems.class);
        startActivity(completedItemsIntent);
    }

    // Store All ToDoList Items from database to ToDoList Items Colleciton.
    private void getAllToDoListItems(){
        Cursor c = dbHelper.getToDoRecords(Constants.TODO_LIST,columnNames,null,"KEY_DATE");
        toDoListItems = new ArrayList();

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


                ToDoList todoList = new ToDoList(id, title, description, actionDate, status);

                toDoListItems.add(todoList);
            }while(c.moveToNext());
        }
    }

    // Update To dO Item status
    private void updateToDoItemStatus(int position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.STATUS,1);

        int result = dbHelper.updateRecord(Constants.TODO_LIST,contentValues,"KEY_ID=" + Integer.parseInt(toDoListItems.get(position).getId()),null);

        if (result > 0) {
            getAllToDoListItems();
        }
        toDoListCustomAdapter.notifyDataSetChanged();
    }

    //Show Alert Dialog To Add New To Do Item.
    private void showAddItemDialog(boolean isItemSelected, int position){
        final ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this,R.style.Theme_AppCompat_Dialog);
        dialog = new AlertDialog.Builder(contextThemeWrapper).setView(R.layout.add_todo_item).create();

        if (!isItemSelected) dialog.setTitle(R.string.add_to_do_item);
        else dialog.setTitle("Edit ToDo Item");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();

        titleEditText = (EditText) dialog.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) dialog.findViewById(R.id.descriptionEditText);
        taskDatePicker = (DatePicker) dialog.findViewById(R.id.actionDatePicker);

        selectedRowID = "";


        // Display Data In Alert Dialog Layout. if clicked from Custom Adapter List View Item.
        if (isItemSelected){
            selectedRowID = toDoListItems.get(position).getId();
            titleEditText.setText(toDoListItems.get(position).getTitle());
            descriptionEditText.setText(toDoListItems.get(position).getDescription());

        }

        Button toDoItemSaveButton = (Button)dialog.findViewById(R.id.saveButton);
        Button toDoItemCancelButton = (Button) dialog.findViewById(R.id.cancelButton);


        // Create Save Button Listener
        toDoItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result_key_id;
                String selectedDate = taskDatePicker.getDayOfMonth() + "/" + (taskDatePicker.getMonth() + 1) + "/" + taskDatePicker.getYear();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.TITLE, titleEditText.getText().toString());
                contentValues.put(Constants.DESCRIPTION, descriptionEditText.getText().toString());
                contentValues.put(Constants.ACTIONDATE, selectedDate);
                contentValues.put(Constants.STATUS, 0);

                // If Selected Row Id has nothing, create new ToDoList Item.
                if (selectedRowID.equals(""))
                    result_key_id = dbHelper.insertToDoList(Constants.TODO_LIST,contentValues);
                else // If not Update Existing Item by Selected ID
                    result_key_id = dbHelper.updateRecord(Constants.TODO_LIST, contentValues, "KEY_ID=" + selectedRowID, null);

                // Toast Successfully Created/Updated
                Toast.makeText(contextThemeWrapper, (selectedRowID.equals("")) ? "New ToDo Item Created Successfully... " : "ToDo Item Updated Successfully. ", Toast.LENGTH_SHORT).show();


                getAllToDoListItems();
                toDoListCustomAdapter  = new ToDoListCustomAdapter(getApplicationContext(), toDoListItems);
                toDoListView.setAdapter(toDoListCustomAdapter);

                toDoListCustomAdapter.notifyDataSetChanged(); // Notify Adapter.
                dialog.dismiss();
            }
        });
        
        toDoItemCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}

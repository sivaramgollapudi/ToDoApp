package com.sivaram.todoapp;

import android.content.ContentValues;
import android.database.Cursor;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView toDoListView;
    List<ToDoList> toDoListItems;
    ToDoListCustomAdapter toDoListCustomAdapter;
    AlertDialog dialog;
    EditText titleEditText, descriptionEditText;
    DatePicker taskDatePicker;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] columnNames = new String[]{
                Constants.TITLE.toString(),
                Constants.DESCRIPTION.toString(),
                Constants.ACTIONDATE.toString(),
                Constants.STATUS.toString()
        };

        dbHelper = CommonUtilities.getDBObject(this);

         Cursor c = dbHelper.getToDoRecords(Constants.TODO_LIST,columnNames,null,null);
        if (c != null ) Toast.makeText(this, "Cursor Object Not Null", Toast.LENGTH_SHORT).show();
        toDoListItems = new ArrayList();

         c.moveToFirst();
        if (c!= null){
            do{
                String title = c.getString(c.getColumnIndex(Constants.TITLE));
                String description = c.getString(c.getColumnIndex(Constants.DESCRIPTION));
                String actionDate = c.getString(c.getColumnIndex(Constants.ACTIONDATE));
                String row = title + " " + c.getString(c.getColumnIndex(Constants.DESCRIPTION)) + " " +
                        c.getString(c.getColumnIndex(Constants.ACTIONDATE)) + " " + c.getInt(c.getColumnIndex(Constants.STATUS));


                ToDoList todoList = new ToDoList(title.toString(), description.toString(), actionDate.toString(), 0);

                Toast.makeText(this, row, Toast.LENGTH_SHORT).show();

                toDoListItems.add(todoList);
            }while(c.moveToNext());
        }
        toDoListView = (ListView) findViewById(R.id.toDoListView);

        //toDoListItems = new ArrayList<>();

        toDoListCustomAdapter  = new ToDoListCustomAdapter(getApplicationContext(), toDoListItems);
        toDoListView.setAdapter(toDoListCustomAdapter);

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showAddItemDialog(true, position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.todolist_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().equals("New")){
            showAddItemDialog(false,-1);
        }

        return true;
    }

    private void showAddItemDialog(boolean isItemSelected, int position){
        final ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this,R.style.Theme_AppCompat_Dialog);
        dialog = new AlertDialog.Builder(contextThemeWrapper).setView(R.layout.add_todo_item).create();

        dialog.setTitle(R.string.add_to_do_item);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.show();

        titleEditText = (EditText) dialog.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) dialog.findViewById(R.id.descriptionEditText);
        taskDatePicker = (DatePicker) dialog.findViewById(R.id.actionDatePicker);


        if (isItemSelected){


            //dbHelper.getToDoRecords(Constants.TODO_LIST, columnNames, " ")
            titleEditText.setText(toDoListItems.get(position).getTitle());
            descriptionEditText.setText(toDoListItems.get(position).getDescription());
            //Toast.makeText(contextThemeWrapper, toDoListItems.get(position).getActionDate(), Toast.LENGTH_SHORT).show();
            ///Toast.makeText(contextThemeWrapper, toDoListItems.get(position).getActionDate().toString().substring(0, 1).toString(),
             //       Toast.LENGTH_SHORT).show();
            //String day = toDoListItems.get(position).getActionDate().substring(0,1);
            //String month = toDoListItems.get(position).getActionDate().substring(3,1);

            //Toast.makeText(contextThemeWrapper,  month  , Toast.LENGTH_SHORT).show();

            /*Calendar c = Calendar.getInstance();
            c.set(Integer.parseInt(toDoListItems.get(position).getActionDate().substring(5,4)),
                    Integer.parseInt(toDoListItems.get(position).getActionDate().substring(3,1)),
                    Integer.parseInt(toDoListItems.get(position).getActionDate().substring(1,2)));

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // set current date into datepicker
            taskDatePicker.init(year, month, day, null);
            */
            //Toast.makeText(contextThemeWrapper, Integer.toString(taskDatePicker.getYear()), Toast.LENGTH_SHORT).show();
        }

        Button toDoItemSaveButton = (Button)dialog.findViewById(R.id.saveButton);
        Button toDoItemCancelButton = (Button) dialog.findViewById(R.id.cancelButton);

        toDoItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedDate = taskDatePicker.getDayOfMonth() + "/" + taskDatePicker.getMonth() + "/" + taskDatePicker.getYear();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Constants.TITLE, titleEditText.getText().toString());
                contentValues.put(Constants.DESCRIPTION, descriptionEditText.getText().toString());
                contentValues.put(Constants.ACTIONDATE, selectedDate);
                contentValues.put(Constants.STATUS, 0);

                dbHelper.insertToDoList(Constants.TODO_LIST,contentValues);

                toDoListItems.add(new ToDoList(titleEditText.getText().toString(),
                                                descriptionEditText.getText().toString(),
                                                selectedDate, 1));
                toDoListCustomAdapter.notifyDataSetChanged();

                Toast.makeText(contextThemeWrapper, "Save Button Clicked : " + taskDatePicker.getYear(), Toast.LENGTH_SHORT).show();
            }
        });
        
        toDoItemCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contextThemeWrapper, "Cancel Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

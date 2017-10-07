package com.sivaram.todoapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 06/10/2017.
 */

public class ToDoListCustomAdapter extends BaseAdapter {
    private Context context;
    private List<ToDoList> toDoListItems;

    public ToDoListCustomAdapter(Context context, List<ToDoList> toDoListItems) {
        this.context = context;
        this.toDoListItems = toDoListItems;
    }

    @Override
    public int getCount() {
        return toDoListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return toDoListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View toDoListCustomView = View.inflate(context, R.layout.todo_listview,null);

        TextView titleTextView = (TextView) toDoListCustomView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = (TextView) toDoListCustomView.findViewById(R.id.descriptionTextView);
        TextView dateTextView = (TextView) toDoListCustomView.findViewById(R.id.dateTextView);
        ImageView statusImageView= (ImageView) toDoListCustomView.findViewById(R.id.statusImageView);

        titleTextView.setText(toDoListItems.get(position).getTitle());
        descriptionTextView.setText(toDoListItems.get(position).getDescription());
        dateTextView.setText(toDoListItems.get(position).getActionDate());
        statusImageView.setImageResource(R.drawable.thumbsup);

        return toDoListCustomView;
    }
}

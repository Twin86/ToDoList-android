package com.example.sebastian.todolist;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by sebastian on 27.01.17.
 */

public class MyArrayAdapter extends ArrayAdapter<Task> {

    Context context;

    public MyArrayAdapter(Context context, int resource , ArrayList<Task> zadania) {
        super(context, resource, zadania);
    }

}

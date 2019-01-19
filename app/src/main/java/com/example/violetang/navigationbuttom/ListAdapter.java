package com.example.violetang.navigationbuttom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: List Adapter for lists listview
 */
public class ListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<String> list_name;
    ArrayList<Integer> list_id;

    private DatabaseHelper myDB;

    public ListAdapter(Context c, ArrayList<Integer> id, ArrayList<String> name) {
        list_id = id;
        list_name = name;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_name.size();
    }

    @Override
    public Object getItem(int position) {
        return list_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_list, null);
        myDB = new DatabaseHelper(MyApplication.getAppContext());

        TextView nameTextView = (TextView) v.findViewById(R.id.listName_textView);

        final int index = position;

        String name = list_name.get(position);
        nameTextView.setText(name);

        return v;
    }

}

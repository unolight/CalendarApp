package com.mixerbox.mbcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 2016/8/26.
 */
public class TodoAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<Event> eventArrayList;
    public TodoAdapter(Context context, ArrayList<Event> _studentArrayList) {
        inflater = LayoutInflater.from(context);
        eventArrayList = _studentArrayList;
    }
    @Override
    public int getCount() {
        if(eventArrayList == null){
            eventArrayList = new ArrayList<>();
        }
        return eventArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return eventArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_todo, null);
        TextView tv_todoname = (TextView) view.findViewById(R.id.tv_todoname);
        TextView tv_deadline = (TextView) view.findViewById(R.id.tv_deadline);

        Event todo = (Event) getItem(position);
        tv_todoname.setText(todo.name);

        Calendar cal = Calendar.getInstance();
        cal.setTime(todo.endDate);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        tv_deadline.setText(month + "/" + date);

        return view;
    }

    public void updateTodoArrayList(ArrayList<Event> _eventArrayList) {
        eventArrayList = _eventArrayList;
        notifyDataSetChanged();
    }


}

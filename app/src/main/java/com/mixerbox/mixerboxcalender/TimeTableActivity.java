package com.mixerbox.mbcalendar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;

public class TimeTableActivity extends AppCompatActivity {

    static final String TAG = "TimeTableActivity";
    static final int POST_REQUEST = 0;

    ArrayList<Course> courselist;
    TableLayout courseTimeLayout;
    CourseTimeList timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        // get view or layout in xml
        courseTimeLayout = (TableLayout) findViewById(R.id.tablelayout);

        courselist = new ArrayList<>();
        readFromDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_course:
                // add new course activity
                Intent intent = new Intent(this, AddCourseActivity.class);
                startActivityForResult(intent, POST_REQUEST);
                // startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == POST_REQUEST) {
            if (resultCode == RESULT_OK) {
                int id = data.getIntExtra("ID", -1);
                // name, professor, location
                String[] courseinfo = data.getStringArrayExtra("COURSE");
                // day, start, end
                int[] timeinfo = data.getIntArrayExtra("TIME");
                // add course
                id = addModifyDbTable(courseinfo, timeinfo, id);

                Log.d(TAG + " acti", "course: " + courseinfo[0] + ", " + courseinfo[1] + ", " + courseinfo[2]);
                Log.d(TAG + " acti", "time: " + timeinfo[0] + ", " + timeinfo[1] + ", " + timeinfo[2]);

            } else if (resultCode == RESULT_CANCELED) {
                // User pressed back button of phone or cancel button on page
            }
        }
    }

    private int addModifyDbTable(String[] courseinfo, int[] timeinfo, int id) {
        if(id == -1){
            if(checkNoClassExist(courseinfo, timeinfo, id)){
                id = addToDb(courseinfo, timeinfo, id);
                addCourseToTable(courseinfo, timeinfo, id);
            }else{
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Duplicate course");
                alertDialogBuilder.setMessage("There is already a course at that time.");
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        }else{
            // modify
            cleanTable(id);
            addToDb(courseinfo, timeinfo, id);
            addCourseToTable(courseinfo, timeinfo, id);
        }
        return id;
    }

    private int addToDb(String[] courseinfo, int[] timeinfo, int id){
        DB dbHelper = new DB(TimeTableActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", courseinfo[0]);
        values.put("prof", courseinfo[1]);
        values.put("location", courseinfo[2]);
        values.put("day", timeinfo[0]);
        values.put("startSlot", timeinfo[1]);
        values.put("endSlot", timeinfo[2]);

        if(id == -1){
            return (int) db.insert(DB.courseTable, null, values);
        } else {
            String selection = "_id = ?";
            String[] selectionArgs = { String.valueOf(id) };
            int count = db.update(
                    DB.courseTable,
                    values,
                    selection,
                    selectionArgs
            );
            return id;
        }
    }

    private void readFromDb(){
        DB dbHelper = new DB(TimeTableActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "_id",
                "name",
                "prof",
                "location",
                "day",
                "startSlot",
                "endSlot" };
        String sortOrder = "_id" + " ASC";
        Cursor cursor = db.query(DB.courseTable, projection,
                null, null, null, null, sortOrder);
        cursor.moveToFirst();
        int numOfRows = cursor.getCount();
        for (int i = 0; i < numOfRows; i++) {
            int id = cursor.getInt(0);
            String[] courseinfo = {cursor.getString(1), cursor.getString(2), cursor.getString(3)};
            int[] timeinfo = {cursor.getInt(4), cursor.getInt(5), cursor.getInt(6)};

            courselist.add(new Course(courseinfo, timeinfo, id));
            Log.d(TAG +" readF", String.valueOf(id));
            addCourseToTable(courseinfo, timeinfo, id);

            Log.d(TAG + " rDB", "id: " + id);
            Log.d(TAG + " rDB", "course: " + courseinfo[0] + ", " + courseinfo[1] + ", " + courseinfo[2]);
            Log.d(TAG + " rDB", "time: " + timeinfo[0] + ", " + timeinfo[1] + ", " + timeinfo[2]);

            cursor.moveToNext();
        }
        cursor.close();
    }

    private void modifyCourseTimeList(CourseTimeList courseTimeList){
        courseTimeList.modifyLayout(courseTimeLayout, this);
    }

    private TextView getTextView(int weekday, int course){
        TableRow row = (TableRow) courseTimeLayout.getChildAt(course-1);
        return (TextView) row.getChildAt(weekday);
    }

    private boolean addCourseToTable(String[] courseinfo, int[] timeinfo, int _id){
        int day = timeinfo[0];
        int start = timeinfo[1];
        int end = timeinfo[2];

        Log.d(TAG +" addC", String.valueOf(_id));

        ArrayList<TextView> tvList = new ArrayList<>();

        for(int i = start; i <= end; i++){
            TextView tv = getTextView(day, i);
            if(!tv.getText().toString().equals("")){
                return false;
            }
            tvList.add(tv);
        }

        final String[] course = courseinfo;
        final int[] ints = timeinfo;
        final int id = _id;

        for(int i = 0; i < tvList.size(); i++){
            tvList.get(i).setText(courseinfo[0]);
            tvList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(TimeTableActivity.this, AddCourseActivity.class);
                    intent.putExtra("COURSE", course);
                    intent.putExtra("TIME", ints);
                    intent.putExtra("ID", id);

                    startActivityForResult(intent, POST_REQUEST);
                }
            });
        }

        return true;
    }

    private void cleanTable(int _id){
        DB dbHelper = new DB(TimeTableActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "_id",
                "day",
                "startSlot",
                "endSlot" };
        String sortOrder = "_id" + " ASC";
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(_id) };
        Cursor cursor = db.query(DB.courseTable, projection,
                selection, selectionArgs, null, null, sortOrder);
        cursor.moveToFirst();
        int numOfRows = cursor.getCount();
        for (int i = 0; i < numOfRows; i++) {
            int day = cursor.getInt(1);
            int start = cursor.getInt(2);
            int end = cursor.getInt(3);
            for(int j = start; j <= end; j++){
                TextView tv = getTextView(day, j);
                tv.setText("");
                tv.setOnClickListener(null);
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    private boolean checkNoClassExist(String[] courseinfo, int[] timeinfo, int _id){
        int day = timeinfo[0];
        int start = timeinfo[1];
        int end = timeinfo[2];

        for(int i = start; i <= end; i++){
            TextView tv = getTextView(day, i);
            if(!tv.getText().toString().equals("")){
                return false;
            }
        }
        return true;
    }

}

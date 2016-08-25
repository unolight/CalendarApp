package com.mixerbox.mbcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AddCourseActivity extends AppCompatActivity {

    Button bt_edittime, bt_addtodo;
    TextView tv_classtime;
    EditText et_name, et_prof, et_loc;
    String name, professor, location;
    static final int POST_REQUEST = 0;

    int weekday, start, end;
    int id;
    static final String[] weekdays =  { "Mon.", "Tue.", "Wed.", "Thur.", "Fri." };

    private ArrayList<Event> todolist;
    TodoAdapter todoAdapter;
    DB dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        dbHelper = new DB(AddCourseActivity.this);
        db = dbHelper.getReadableDatabase();

        // findView
        bt_edittime = (Button) findViewById(R.id.bt_edittime);
        bt_addtodo = (Button) findViewById(R.id.bt_addtodo);
        tv_classtime = (TextView) findViewById(R.id.tv_classtime);
        et_name = (EditText) findViewById(R.id.et_name);
        et_prof = (EditText) findViewById(R.id.et_professor);
        et_loc = (EditText) findViewById(R.id.et_location);

        todolist = new ArrayList<>();
        todoAdapter = new TodoAdapter(this, todolist);
        ListView listView = (ListView) findViewById(R.id.lv_todolist);
        listView.setAdapter(todoAdapter);

        // initial value
        String[] courseinfo;
        if (getIntent().hasExtra("COURSE")) {
            courseinfo = getIntent().getStringArrayExtra("COURSE");
            name = courseinfo[0];
            professor = courseinfo[1];
            location = courseinfo[2];
        } else {
            name = "";
            professor = "";
            location = "";
        }
        int[] timeinfo;
        if (getIntent().hasExtra("TIME")) {
            timeinfo = getIntent().getIntArrayExtra("TIME");
            weekday = timeinfo[0];
            start = timeinfo[1];
            end = timeinfo[2];
            tv_classtime.setText(weekdays[weekday-1] + " " + start + "~" + end );
        } else {
            weekday = start = end = 0;
            tv_classtime.setText("");
        }
        if (getIntent().hasExtra("ID")) {
            id = getIntent().getIntExtra("ID", -1);
        } else {
            id = -1;
        }

        findTodoInDB(id);

        et_name.setText(name);
        et_prof.setText(professor);
        et_loc.setText(location);

        bt_edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCourse();
            }
        });

        bt_addtodo.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                if(id == -1) return;
                Intent intent = new Intent(AddCourseActivity.this, AddEventActivity.class);
                intent.putExtra("ID", id);
                startActivityForResult(intent, POST_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == POST_REQUEST){
            if(resultCode == RESULT_OK){
                int todoid = data.getIntExtra("ID", -1);
                if(todoid != -1){
                    // TODO: add todo into todolist
                    String[] projection = {
                            "name",
                            "endDate",
                            "type" };
                    String selection = "_id = ?";
                    String selectarg[] = { String.valueOf(todoid) };

                    Cursor cursor = db.query(DB.eventTable, projection,
                            selection, selectarg, null, null, null);
                    cursor.moveToFirst();

                    int numOfRows = cursor.getCount();
                    for (int i = 0; i < numOfRows; i++) {
                        String todoname = cursor.getString(0);
                        Date endDate;
                        try {
                            endDate = DB.dateParser.parse(cursor.getString(1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }
                        String todotype = cursor.getString(2);
                        todolist.add(new Event(todoname, endDate, todotype));
                        cursor.moveToNext();
                    }
                    todoAdapter.updateTodoArrayList(todolist);
                    cursor.close();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addcourse_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                // return OK
                name = et_name.getText().toString();
                professor = et_prof.getText().toString();
                location = et_loc.getText().toString();
                if(name.length() == 0){
                    // check if class name is empty
                    Toast.makeText(getApplicationContext(), "Class name shouldn't be empty!", Toast.LENGTH_SHORT).show();
                }else if(tv_classtime.getText().toString().length() == 0){
                    // check if class time is empty
                    Toast.makeText(getApplicationContext(), "Class time shouldn't be empty!", Toast.LENGTH_SHORT).show();
                }else{
                    // TODO
                    // save data into database
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ID", id);
                    resultIntent.putExtra("COURSE", new String[] {name, professor, location});
                    resultIntent.putExtra("TIME", new int[]{weekday, start, end});
                    setResult(Activity.RESULT_OK, resultIntent);

                    finish();
                }
                return true;
            case R.id.cancel:
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dialogCourse()
    {
        final Dialog d = new Dialog(this);
        d.setTitle("Choose course time");
        d.setContentView(R.layout.layout_add_course_time);

        final NumberPicker np_day = (NumberPicker) d.findViewById(R.id.np_day);
        np_day.setMaxValue(5);
        np_day.setMinValue(1);
        np_day.setDisplayedValues(weekdays);

        final NumberPicker np_start_class = (NumberPicker) d.findViewById(R.id.np_start_class);
        np_start_class.setMaxValue(10); // max value 100
        np_start_class.setMinValue(1);   // min value 0

        final NumberPicker np_end_class = (NumberPicker) d.findViewById(R.id.np_end_class);
        np_end_class.setMaxValue(10); // max value 100
        np_end_class.setMinValue(1);   // min value 0

        Button bt_ok = (Button) d.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                weekday = np_day.getValue();
                start = np_start_class.getValue();
                end = np_end_class.getValue();
                tv_classtime.setText( weekdays[weekday-1] + " " + start + "~" + end );
                d.dismiss();
            }
        });

        d.show();
    }

    void findTodoInDB(int id){

        // dbHelper = new DB(AddCourseActivity.this);
        // SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "_id",
                "name",
                "endDate",
                "type" };
        String selection = "courseId = ?";
        String selectarg[] = { String.valueOf(id) };

        String sortOrder = "endDate" + " ASC";
        Cursor cursor = db.query(DB.eventTable, projection,
                selection, selectarg, null, null, sortOrder);
        cursor.moveToFirst();

        int numOfRows = cursor.getCount();
        for (int i = 0; i < numOfRows; i++) {
            int todoid = cursor.getInt(0);
            String todoname = cursor.getString(1);
            Date endDate;
            try {
                endDate = DB.dateParser.parse(cursor.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            String todotype = cursor.getString(3);
            todolist.add(new Event(todoname, endDate, todotype));
            cursor.moveToNext();
        }
        cursor.close();
        todoAdapter.updateTodoArrayList(todolist);
    }

}

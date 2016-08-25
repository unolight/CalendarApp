package com.mixerbox.mixerboxcalender;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/**
 * Created by user on 8/25/16.
 */
public class AddEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1, spinner2;
    String spinnerSelect;
    RadioGroup radioAlert;
    RadioButton radioAlertButton;
    EditText et_name;
   
    private Button startDateButton;
    private Button endDateButton;
    private Button startTimeButton;
    private Button endTimeButton;

    private TextView tv_start_date;
    private TextView tv_end_date;
    private TextView tv_start_time;
    private TextView tv_end_time;

    private Calendar calendar;
    private Calendar calendar2;

    private TimePickerDialog timePickerDialog;

    private int mYear, mMonth, mDay;
    private int mYear2, mMonth2, mDay2;

    private int mHour, mMinute;
    private int mHour2, mMinute2;
    private DatePickerDialog datePickerDialog;

    int course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        setTitle("Add New Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      // get intent param
        course_id = getIntent().getIntExtra("ID", -1);


    // et name
        et_name = (EditText) findViewById(R.id.et_name);

        et_name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
         //       if(et_name.requestFocus()) {
            //        et_name.setCursorVisible(true);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
          //      }

                et_name.setText("");
            }
       //     et_name.setCursorVisible(false);

        });


    // start date button
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        mHour2 = 100;
        mMinute2 = 100;

        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);


        startDateButton = (Button)findViewById(R.id.start_date);
        startDateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDialog(0);
                //datePickerDialog.show();
                datePickerDialog.updateDate(mYear, mMonth, mDay);
            }

        });

        endDateButton = (Button)findViewById(R.id.end_date);
        calendar2 = Calendar.getInstance();
        mYear2 = 100;
        mMonth2 = 100;
        mDay2 = 100;
        endDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(1);
                //datePickerDialog.show();
                datePickerDialog.updateDate(mYear2, mMonth2, mDay2);
            }

        });

    //  Time Picker
        startTimeButton = (Button) findViewById(R.id.btn_start_time);
        endTimeButton = (Button) findViewById(R.id.btn_end_time);

        startTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(2);
                //datePickerDialog.show();
                timePickerDialog.updateTime(mHour, mMinute);
            }

        });
        endTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(3);
                //datePickerDialog.show();
                timePickerDialog.updateTime(mHour2,mMinute2);
            }

        });

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                false);


        // radio button
        radioAlert = (RadioGroup) findViewById(R.id.radioAlert);


    // Spinner
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner1.setOnItemSelectedListener(this);

    // Button Done
        Button btn_done = (Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // date
                Log.v("date", Integer.toString(mDay));
                Log.v("date1", Integer.toString(mDay2));
            // time
                Log.v("time", Integer.toString(mMinute));
                Log.v("time1", Integer.toString(mMinute2));
            // radio button
                int selectedId = radioAlert.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioAlertButton = (RadioButton) findViewById(selectedId);
                Log.v("radio", radioAlertButton.getText().toString());


            // spinner
                Log.v("spinner1", spinner1.getSelectedItem().toString());
                Log.v("spinner2", spinner2.getSelectedItem().toString());

                finish();
           }
        });


    }



    // Date dialog
    @Override
    protected Dialog onCreateDialog(final int id) {
         if(id == 1 || id == 0) {
             datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month,
                                       int day) {
                     Log.v("dddd", Integer.toString(id));
                     if (id == 0) {
                         mYear = year;
                         mMonth = month;
                         mDay = day;
                         tv_start_date.setText(setDateFormat(year, month, day));
                     } else if (id == 1) {
                         mYear2 = year;
                         mMonth2 = month;
                         mDay2 = day;
                         tv_end_date.setText(setDateFormat(year, month, day));
                     }
                 }
             }, mYear, mMonth, mDay);

             return datePickerDialog;
         }

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                Log.v("ttt", Integer.toString(id));
                if (id == 2) {
                    mHour = hour;
                    mMinute = minute;
                    tv_start_time.setText(setTimeFormat(hour, minute));
                } else if (id == 3) {
                    mHour2 = hour;
                    mMinute2 = minute;
                    tv_end_time.setText(setTimeFormat(hour, minute));
                }
            }
        }, mHour,mMinute, true);

        return timePickerDialog;
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.format("%04d/%02d/%02d", year, monthOfYear, dayOfMonth);
       // return String.valueOf(year) + "/"
      //          + String.valueOf(monthOfYear + 1) + "/"
       //         + String.valueOf(dayOfMonth);
    }
    private String setTimeFormat(int hourOfDay,int minute){
        return String.format("%02d:%02d", hourOfDay, minute);
       /* return String.valueOf(hourOfDay) + ":"
                + String.valueOf(minute);*/
    }


    // spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        spinnerSelect = parent.getItemAtPosition(position).toString();

        if(spinnerSelect.equals("Event")) {
            List<String> categories = new ArrayList<>();
            categories.add("Tour");
            categories.add("Meeting");
            categories.add("Meal");
            categories.add("Sport");
            categories.add("Appointment");
            categories.add("Flight");
            categories.add("Other");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner2.setAdapter(dataAdapter);

        } else if(spinnerSelect.equals("To Do")){
            List<String> categories = new ArrayList<String>();
            categories.add("Homework");
            categories.add("Exam");
            categories.add("Other");
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner2.setAdapter(dataAdapter);

        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                // show share intent

                return true;
            case R.id.action_del_event:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
}

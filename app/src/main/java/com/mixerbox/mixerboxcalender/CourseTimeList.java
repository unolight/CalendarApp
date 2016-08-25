package com.mixerbox.mbcalendar;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by User on 2016/8/25.
 */
public class CourseTimeList {
    ArrayList<CourseTime> courseTimes;
    public CourseTimeList(){
        courseTimes = new ArrayList<>();
    }
    public CourseTimeList(int[] timeArrayList){
        courseTimes = new ArrayList<>();
        for(int i = 0; i < timeArrayList.length-1; ){
            courseTimes.add(new CourseTime(timeArrayList[i], timeArrayList[i+1]));
            i = i + 2;
        }
    }
    public int addCourseTime(int startTime, int endTime){
        courseTimes.add(new CourseTime(startTime, endTime));
        return courseTimes.size() - 1;
    }
    public void modifyCourseTime(int index, int startTime, int endTime){
        courseTimes.set(index, new CourseTime(startTime, endTime));
    }
    public void deleteCourseTime(int index){
        courseTimes.remove(index);
    }
    public void modifyLayout(TableLayout layout, Context context){
        int len = courseTimes.size();
        for(int i = 0; i < layout.getChildCount(); i++){
            if(i < len){
                // set visible & change time
                TableRow row = (TableRow) layout.getChildAt(i);
                row.setVisibility(View.VISIBLE);
                TextView tv = (TextView) row.getChildAt(0);

                int start = courseTimes.get(i).getStartTime();
                int end = courseTimes.get(i).getEndTime();
                String startstring = String.valueOf((int)(start/100)) + ":" + String.valueOf((int)(start%100));
                String endstring = String.valueOf((int)(end/100)) + ":" + String.valueOf((int)(end%100));

                tv.setText(startstring + "\n\n" + endstring);

            }else{
                // set gone
                TableRow row = (TableRow) layout.getChildAt(i);
                row.setVisibility(View.GONE);
            }
        }
    }
}

class CourseTime{
    int startTime;
    int endTime;
    public CourseTime(int _startTime, int _endTime){
        startTime = _startTime;
        endTime = _endTime;
    }
    public int getStartTime(){
        return startTime;
    }
    public int getEndTime(){
        return endTime;
    }
}

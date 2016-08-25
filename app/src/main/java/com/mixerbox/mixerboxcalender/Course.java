package com.mixerbox.mbcalendar;

import java.util.ArrayList;

/**
 * Created by User on 2016/8/25.
 */
public class Course {
    String[] courseinfo;
    int[] timeinfo;
    int id;

    ArrayList<Part> coursepart;
    public Course(String[] _courseinfo, int[] _timeinfo, int _id){
        courseinfo = _courseinfo;
        timeinfo = _timeinfo;
        id = _id;
    }

    public int addPart(int _weekday, int _start, int _end){
        coursepart.add(new Part(_weekday, _start, _end));
        return coursepart.size() - 1;
    }

    public void deletePart(int index){
        coursepart.remove(index);
    }

    public void modifyPart(int index, int _weekday, int _start, int _end){
        coursepart.set(index, new Part(_weekday, _start, _end));
    }
}

class Part{
    int weekday; // Mon = 1, Tue = 2... Sun = 0
    int start, end; // start course
    String classroom;
    String professor;
    public Part(int _weekday, int _start, int _end){
        weekday = _weekday;
        start = _start;
        end = _end;
    }
}

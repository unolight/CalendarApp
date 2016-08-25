package com.mixerbox.mbcalendar;

/**
 * Created by User on 2016/8/26.
 */
import java.util.Date;

public class Event {
    Date startDate, endDate;
    int alertTime;
    String name, type;
    boolean alert;

    public Event(String _name, Date _endDate, String _type){
        name = _name;
        endDate = _endDate;
        type = _type;
    }

}
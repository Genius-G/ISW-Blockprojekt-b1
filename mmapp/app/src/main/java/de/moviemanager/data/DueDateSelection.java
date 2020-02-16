package de.moviemanager.data;

import java.util.Calendar;
import java.util.Date;

import de.util.DateUtils;

import static de.util.DateUtils.normDate;

public class DueDateSelection {
    private Date date;
    private Calendar calendar;
    private Date currentDate;

    public DueDateSelection(Date date) {
        this.date = date;
        this.calendar = Calendar.getInstance();;
        this.currentDate = normDate(DateUtils.now());
    }


    public boolean isOverDue(Date date) {
        return date.before(currentDate);
    }
}

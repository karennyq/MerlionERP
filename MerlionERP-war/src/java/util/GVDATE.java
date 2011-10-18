/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author karennyq
 */
public class GVDATE {

    public static Date addWeek(Date myDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }

    public static Date addDay(Date myDate, int noDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, noDay);
        return c.getTime();
    }

    public static Date getFirstDateOfWeek(Date myDate) {
        switch (myDate.getDay()) {
            case 0:
                return myDate;
            case 1:
                return addDay(myDate, -1);
            case 2:
                return addDay(myDate, -2);
            case 3:
                return addDay(myDate, -3);
            case 4:
                return addDay(myDate, -4);
            case 5:
                return addDay(myDate, -5);
            case 6:
                return addDay(myDate, -6);

        }
        return myDate;
    }

    public static Date getLastDateOfWeek(Date myDate) {
        switch (myDate.getDay()) {
            case 0:
                return addDay(myDate, 6);
            case 1:
                return addDay(myDate, 5);
            case 2:
                return addDay(myDate, 4);
            case 3:
                return addDay(myDate, 3);
            case 4:
                return addDay(myDate, 2);
            case 5:
                return addDay(myDate, 1);
            case 6:
                return addDay(myDate, 0);

        }
        return myDate;
    }

    public static int calNoOfWeeks(Date fromDate, Date toDate) {
        Date startDate = getFirstDateOfWeek(fromDate);
        Date endDate = getLastDateOfWeek(toDate);

        Date date = (Date) startDate.clone();
        int daysBetween = 0;
        while (date.before(endDate)) {
            date = addDay(date, 1);
            daysBetween++;
        }

        int noWeeks = (daysBetween + 1) / 7;
        return noWeeks;
    }
}

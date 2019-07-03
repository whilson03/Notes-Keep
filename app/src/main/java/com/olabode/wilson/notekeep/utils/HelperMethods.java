package com.olabode.wilson.notekeep.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by OLABODE WILSON on 2019-06-28.
 */
public class HelperMethods {

    /**
     * get current date
     *
     * @return string value of date
     */
    public static String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }


}

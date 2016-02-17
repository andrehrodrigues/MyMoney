package br.com.saturno.android.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andre on 16/11/2015.
 */
public class DateUtil {

    /**
     * Validate date format with regular expression
     * @param date date address for validation
     * @return true valid date format, false invalid date format
     * source: http://www.mkyong.com/regular-expressions/how-to-validate-date-with-regular-expression/
     */
    public static boolean validate(final String date){

        Pattern pattern;
        Matcher matcher;

        final String DATE_PATTERN =
                "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])";
        String pat1 = ".*(0?[1-9]).*";
        String pat2 = ".*(1[012]).*";

        Pattern pattern1 = Pattern.compile(pat1);
        Pattern pattern2 = Pattern.compile(pat2);
        Matcher matcher1 = pattern1.matcher(date);
        Matcher matcher2 = pattern2.matcher(date);



        pattern = Pattern.compile(DATE_PATTERN);

        matcher = pattern.matcher(date);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()){

                String day = matcher.group(1);
                String month = matcher.group(2);

                if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    public static String getCurrentDate(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append("/").append(month+1).append("/")
                .append(year).append(" ").toString();

    }

}

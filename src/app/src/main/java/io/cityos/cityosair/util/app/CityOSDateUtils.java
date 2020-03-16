package io.cityos.cityosair.util.app;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CityOSDateUtils {
  //2017-01-27 16:42:00
  public static String getRelativeDateTime(String dateTime) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      Date timeCreatedDate = dateFormat.parse(dateTime);
      return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(timeCreatedDate);
    } catch (ParseException e) {
      return "";
    }
  }

  public static String getTimeAgo(Date date) {

    if (date == null) {
      return null;
    }

    long time = date.getTime();

    Date curDate = currentDate();
    long now = curDate.getTime();
    if (time > now) {
      return "";
    }

    int dim = getTimeDistanceInMinutes(time);

    String timeAgo = null;

    if (dim == 0) {
      timeAgo = "s";
    } else if (dim == 1) {
      timeAgo = "1m";
    } else if (dim >= 2 && dim <= 44) {
      timeAgo = dim + "m";
    } else if (dim >= 45 && dim <= 89) {
      timeAgo = "1h";
    } else if (dim >= 90 && dim <= 1439) {
      timeAgo = (Math.round(dim / 60)) + "h";
    } else if (dim >= 1440 && dim <= 2519) {
      timeAgo = "1 day";
    } else if (dim >= 2520 && dim <= 43199) {
      timeAgo = (Math.round(dim / 1440)) + " days";
    }

    return timeAgo + " ago";
  }

  private static int getTimeDistanceInMinutes(long time) {
    long timeDistance = currentDate().getTime() - time;
    return Math.round((Math.abs(timeDistance) / 1000) / 60);
  }

  private static Date currentDate() {
    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    return calendar.getTime();
  }

  public static long getLastWeekTimestampInSeconds() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, -7);
    return cal.getTimeInMillis() / 1000L;
  }

  public static long getLastThreeWeeksTimestampInSeconds() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.WEEK_OF_YEAR, -3);
    return cal.getTimeInMillis() / 1000L;
  }

  public static long getLastMonthTimestampInSeconds() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.WEEK_OF_YEAR, -12);
    return cal.getTimeInMillis() / 1000L;
  }

  public static long getPreviousDayTimestampInSeconds() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_WEEK, -1);
    return cal.getTimeInMillis() / 1000L;
  }

  public static long getPreviousFiveHoursTimestampInSeconds() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR_OF_DAY, -5);
    return cal.getTimeInMillis() / 1000L;
  }

  public static String getLastUpdatedDateString(long timestamp) {
    Date date = new Date(timestamp * 1000L);
    return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(date);
  }

  public static String getRelativeDateTimeForTimeFrame(String dateTime) {

    String inputFormat = "yyy-MM-dd'T'HH:mm:ss";
    String outputFormat = "MMM d HH:mm";

    DateFormat dateFormat = new SimpleDateFormat(inputFormat);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    try {
      Date timeCreatedDate = dateFormat.parse(dateTime);
      return new SimpleDateFormat(outputFormat).format(timeCreatedDate);
    } catch (ParseException e) {
      return "";
    }
  }

  public static String getRelativeDateTimeForTimestamp(long timestamp) {

    String inputFormat = "yyy-MM-dd'T'HH:mm:ss";
    String outputFormat = "MMM d HH:mm";

    DateFormat dateFormat = new SimpleDateFormat(inputFormat);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

    Date date = new Date(timestamp * 1000L);
    String strDate = new SimpleDateFormat(outputFormat).format(date);
    return strDate;
  }

  public static String getDateLocale(String dateTime) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    try {
      return DateFormat.getDateInstance().format(dateFormat.parse(dateTime));
    } catch (ParseException e) {
      return "";
    }
  }

  public static String getMonth(int month) {
    return new DateFormatSymbols(Locale.US).getMonths()[month - 1];
  }
}

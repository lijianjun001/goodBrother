package com.cylty.zmkj.calender.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by joybar on 2/24/16.
 */
public class CalendarUtils {

    /**
     * 获取上一月最后一天的日期数
     *
     * @param calendar
     * @return
     */
    public static int getDayOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int result = calendar.get(Calendar.DATE);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }

    /**
     * 获取上一月月份数
     *
     * @param calendar
     * @return
     */
    public static int getMonthOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int result = (calendar.get(Calendar.MONTH) + 1);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }

    /**
     * 获取上一月年份数
     *
     * @param calendar
     * @return
     */
    public static int getYearOfLastMonth(Calendar calendar) {

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        int result = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.DATE, +1);//重新加一天，恢复
        return result;
    }

    /**
     * 获取某年某月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getdataCount(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int count = cal.getActualMaximum(Calendar.DATE);
        return count;
    }

    public static int getWeekOfDay(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(year + "-" + month + "-" + 1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.set(Calendar.DAY_OF_MONTH, day);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }

    /**
     * 根据年月生成当月的日历中的日期
     *
     * @param year
     * @param month
     * @return
     * @throws ParseException
     */
    public static List<CalendarSimpleDate> getEverydayOfMonth(int year, int month) throws ParseException {
        List<CalendarSimpleDate> list = new ArrayList<>();
        int count = getdataCount(year, month); //获取当月的天数
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(year + "-" + month + "-" + 1));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int begin = cal.get(Calendar.DAY_OF_WEEK) - 1;//获取每月1号星期几
        //重置
        cal.setTime(sdf.parse(year + "-" + month + "-" + 1));
        // int first = cal.get(Calendar.DAY_OF_WEEK);
        int dayOfLastMonth = getDayOfLastMonth(cal);
        int monthOfLastMonth = getMonthOfLastMonth(cal);
        int yearOfLastMonth = getYearOfLastMonth(cal);
        //填补上月的数据
        for (int i = 0; i < begin; i++) {
            //每月第一天为星期日则不添加
            if (begin != 0) {
                CalendarSimpleDate calendarDate = new CalendarSimpleDate(yearOfLastMonth, monthOfLastMonth, dayOfLastMonth - begin + i + 2);
                list.add(calendarDate);
            }

        }
        //填补本月的数据
        for (int i = 1; i <= count; i++) {
            CalendarSimpleDate calendarDate = new CalendarSimpleDate(year, month, i);
            list.add(calendarDate);
            cal.setTime(sdf.parse(year + "-" + month + "-" + i));

        }
        int nextMonthNum = 7 - (begin + count) % 7;
        //填补下月的数据
        for (int i = 1; i <= nextMonthNum; i++) {
            if (nextMonthNum != 0 && nextMonthNum != 7) {
                int nextYear = year;
//                int nextMonth = (month + 1) % 12;
//                if (nextMonth == 1) {
//                    nextYear = nextYear + 1;
//                }
                int nextMonth = month + 1;
                if (nextMonth == 13) {
                    nextMonth = 1;
                }
                CalendarSimpleDate calendarDate = new CalendarSimpleDate(nextYear, nextMonth, i);
                list.add(calendarDate);
            }
        }
        return list;
    }

    public static class CalendarSimpleDate {
        private int year;
        private int month;
        private int day;
        private int week;

        public CalendarSimpleDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.week = getWeekOfDay(year, month, day);
        }

        public int getWeek() {
            return week;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }

}

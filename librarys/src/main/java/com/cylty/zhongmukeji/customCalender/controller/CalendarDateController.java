package com.cylty.zhongmukeji.customCalender.controller;


import com.cylty.zhongmukeji.customCalender.data.CalendarDate;
import com.cylty.zhongmukeji.customCalender.data.Solar;
import com.cylty.zhongmukeji.customCalender.utils.CalendarUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarDateController {

    public static List<CalendarDate> getCalendarDate(int year, int month) {
        List<CalendarDate> mListDate = new ArrayList<>();
        List<CalendarUtils.CalendarSimpleDate> list = null;
        try {
            list = CalendarUtils.getEverydayOfMonth(year, month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = list.size();

        for (int i = 0; i < count; i++) {
            Solar solar = new Solar();
            solar.solarYear = list.get(i).getYear();
            solar.solarMonth = list.get(i).getMonth();
            solar.solarDay = list.get(i).getDay();
            solar.solarWeek = list.get(i).getWeek();
//            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
            mListDate.add(new CalendarDate(month == list.get(i).getMonth(), false, solar, null));
        }

        return mListDate;
    }


}

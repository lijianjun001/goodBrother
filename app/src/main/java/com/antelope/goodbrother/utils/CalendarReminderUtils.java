package com.antelope.goodbrother.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.cylty.zmkj.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class CalendarReminderUtils {
    private static final String ACCOUNT_NAME = "众牧宝";

    /**
     * 使用以下Uri时，Android版本>=14; 注意引用包路径：android.provider.CalendarContract下的；
     **/
    private static Uri calendarsUri = CalendarContract.Calendars.CONTENT_URI;
    private static Uri eventsUri = CalendarContract.Events.CONTENT_URI;
    private static Uri remindersUri = CalendarContract.Reminders.CONTENT_URI;


    /**
     * Events table columns
     */
    public static final String[] EVENTS_COLUMNS = new String[]{CalendarContract.Events._ID,
            CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.EVENT_LOCATION, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND,
            CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Events.HAS_ALARM, CalendarContract.Events.STATUS,
            CalendarContract.Events.HAS_ATTENDEE_DATA, CalendarContract.Events.RRULE};

    /**
     * 插入事件
     */
    public static void insertEvent(Activity activity, EventModel model) {
        String calId = queryCalenderId(activity);
        if (TextUtils.isEmpty(calId)) {
            addAccount(activity);
            insertEvent(activity, model);
            return;
        }
        // 插入事件
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMNS[1], calId);
        contentValues.put(EVENTS_COLUMNS[2], model.getTitle());//标题
        contentValues.put(EVENTS_COLUMNS[3], model.getContent());//备注
        contentValues.put(EVENTS_COLUMNS[4], "");//地点需要可以添加
        contentValues.put(EVENTS_COLUMNS[5], model.getStartTime());//开始时间
        contentValues.put(EVENTS_COLUMNS[6], model.getEndTime());//结束时间
        contentValues.put(EVENTS_COLUMNS[7], TimeZone.getDefault().getID());//时区
        contentValues.put(EVENTS_COLUMNS[8], 1);//是否提醒
        contentValues.put(EVENTS_COLUMNS[9], CalendarContract.Events.STATUS_CONFIRMED);//状态确定
        contentValues.put(EVENTS_COLUMNS[10], 1);//是否有出席者
        contentValues.put(EVENTS_COLUMNS[11], "FREQ=DAILY;INTERVAL=1");//提醒方式每天提醒

        Uri eventUri = activity.getContentResolver().insert(eventsUri, contentValues);
        // 事件提醒的设定
        String id = eventUri != null ? eventUri.getLastPathSegment() : "1";
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, id);
        values.put(CalendarContract.Reminders.MINUTES, 10);//提前提醒时间 min
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);//提醒方式
        Uri uri = activity.getContentResolver().insert(remindersUri, values);

        if (uri != null && BuildConfig.DEBUG) {
            Log.d("insertEvent", uri.toString());
        }

    }

    /**
     * 根据账户查询账户日历活动
     *
     * @return List
     */
    public static List<EventModel> queryEvents(Activity activity) {
        List<EventModel> calendars = new ArrayList<>();
        Cursor cursor;
        cursor = activity.getContentResolver().query(eventsUri, EVENTS_COLUMNS, CalendarContract.Calendars.ACCOUNT_NAME + " = ? ", new String[]{ACCOUNT_NAME}, null);
        while (cursor != null && cursor.moveToNext()) {
            EventModel eventModel = new EventModel();
            eventModel.setId(cursor.getString(0));
            eventModel.setTitle(cursor.getString(2));
            eventModel.setContent(cursor.getString(3));
            eventModel.setStartTime(cursor.getLong(5));
            eventModel.setEndTime(cursor.getLong(6));
            calendars.add(eventModel);
        }
        return calendars;
    }

    /**
     * 根据活动title查询账户日历活动
     *
     * @return List
     */
    public static List<EventModel> queryEvent(Activity activity, String title) {
        List<EventModel> calendars = new ArrayList<>();
        Cursor cursor;
        cursor = activity.getContentResolver().query(eventsUri, EVENTS_COLUMNS, CalendarContract.Calendars.ACCOUNT_NAME + " = ? and " + CalendarContract.Events.TITLE + "=?", new String[]{ACCOUNT_NAME, title}, null);
        while (cursor != null && cursor.moveToNext()) {
            EventModel eventModel = new EventModel();
            eventModel.setId(cursor.getString(0));
            eventModel.setTitle(cursor.getString(2));
            eventModel.setContent(cursor.getString(3));
            eventModel.setStartTime(cursor.getLong(5));
            eventModel.setEndTime(cursor.getLong(6));
            calendars.add(eventModel);
        }
        return calendars;
    }

    /**
     * 更新某条Event
     *
     * @param model model
     */
    public static void updateEvent(Activity activity, EventModel model) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.DTSTART, model.getStartTime());
        contentValues.put(CalendarContract.Events.DESCRIPTION, model.getContent());
        activity.getContentResolver().update(eventsUri, contentValues
                , CalendarContract.Events._ID + " =? ", new String[]{model.getId()});
    }

    /**
     * 删除某条Event
     *
     * @param id eventId
     * @return The number of rows deleted.
     */
    public static int deleteEvent(Activity activity, String id) {
        return activity.getContentResolver()
                .delete(eventsUri, CalendarContract.Events._ID + " =? ", new String[]{id});
    }

    /**
     * 删除某条Event
     *
     * @param title eventTitle
     * @return The number of rows deleted.
     */
    public static int deleteEventByTitle(Activity activity, String title) {
        return activity.getContentResolver()
                .delete(eventsUri, CalendarContract.Events.TITLE + " =? ", new String[]{title});
    }

    /**
     * 删除所有Event
     *
     * @return The number of rows deleted.
     */
    public static int deleteAllEvent(Activity activity) {
        return activity.getContentResolver()
                .delete(eventsUri, CalendarContract.Events.CALENDAR_ID + " =? ", new String[]{queryCalenderId(activity)});
    }

    /**
     * 查询 calendar_id
     *
     * @return calId
     */
    private static String queryCalenderId(Activity activity) {
        Cursor userCursor = null;
        try {
            userCursor = activity.getContentResolver().query(calendarsUri, null,
                    "name=?", new String[]{ACCOUNT_NAME}, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userCursor == null) {
            return null;
        }
        if (userCursor.getCount() > 0) {
            userCursor.moveToLast(); //是向符合条件的最后一个账户添加
            return userCursor.getString(userCursor.getColumnIndex("_id"));
        }
        return null;
    }

    /**
     * 添加账户
     */
    private static void addAccount(Context context) {
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        value.put(CalendarContract.Calendars._SYNC_ID, "1");
        value.put(CalendarContract.Calendars.DIRTY, "1");
        value.put(CalendarContract.Calendars.NAME, ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
        value.put(CalendarContract.Calendars.VISIBLE, "1");
        value.put(CalendarContract.Calendars.SYNC_EVENTS, "1");
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, "1");
        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "1").build();
        context.getContentResolver().insert(calendarUri, value);
    }


    public static class EventModel {
        private String title;
        private String id;
        private long startTime;//startMillis
        private long endTime;//endMillis
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}

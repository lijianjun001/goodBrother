package com.antelope.goodbrother.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import androidx.core.app.ActivityCompat;

public class CalendarReminderUtils {
    private static final String ACCOUNT_NAME = "zmb";

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
            CalendarContract.Events.EVENT_TIMEZONE, CalendarContract.Events.HAS_ALARM, CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.AVAILABILITY, CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.STATUS,};

    /**
     * 插入事件
     */
    public static void insertEvent(Activity activity, EventModel model, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
            String calId = queryCalId(activity, reqPermissionCode);
            if (TextUtils.isEmpty(calId)) {
                addAccount(activity);
                insertEvent(activity, model, reqPermissionCode);
                return;
            }
            // 插入事件
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.Events.TITLE, model.getTitle());//标题
            contentValues.put(CalendarContract.Events.DESCRIPTION, model.getContent());//备注
            contentValues.put(CalendarContract.Events.EVENT_LOCATION, model.getLocation());//地点用需要可以添加)
            contentValues.put(CalendarContract.Events.CALENDAR_ID, calId);
            contentValues.put(CalendarContract.Events.DTSTART, model.getStartTime());//开始时间
            contentValues.put(CalendarContract.Events.DTEND, model.getEndTime());//结束时间
            contentValues.put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED);
            contentValues.put(CalendarContract.Events.HAS_ATTENDEE_DATA, 1);
            contentValues.put(CalendarContract.Events.HAS_ALARM, 1);//是否生效?
            contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());//时区，必须有
            Uri newEvent = activity.getContentResolver().insert(eventsUri, contentValues);
            // 事件提醒的设定
            long id = Long.parseLong(newEvent != null ? newEvent.getLastPathSegment() : "1");
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, id);
            values.put(CalendarContract.Reminders.MINUTES, "10");//提前提醒时间 min
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);//提醒方式
            activity.getContentResolver().insert(remindersUri, values);
        } else {
            requestCalendarPermission(activity, reqPermissionCode);
        }


    }

    /**
     * 根据账户查询账户日历
     *
     * @return List
     */
    public static List<EventModel> queryEvents(Activity activity, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
            List<EventModel> calendars = new ArrayList<>();
            Cursor cursor;
            // 本地帐户查询：ACCOUNT_TYPE_LOCAL是一个特殊的日历账号类型，它不跟设备账号关联。这种类型的日历不同步到服务器
            // 如果是谷歌的账户是可以同步到服务器的
            cursor = activity.getContentResolver().query(eventsUri, EVENTS_COLUMNS,
                    CalendarContract.Calendars.ACCOUNT_NAME + " = ? ", new String[]{ACCOUNT_NAME}, null);
            while (cursor != null && cursor.moveToNext()) {
                EventModel eventModel = new EventModel();
                eventModel.setId(cursor.getString(0));
                eventModel.setStartTime(cursor.getLong(5));
                eventModel.setContent(cursor.getString(3));
                calendars.add(eventModel);
            }
            return calendars;
        } else {

            requestCalendarPermission(activity, reqPermissionCode);
        }
        return null;
    }

    /**
     * 更新某条Event
     *
     * @param model model
     */
    public static void updateEvent(Activity activity, EventModel model, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.Events.DTSTART, model.getStartTime());
            contentValues.put(CalendarContract.Events.DESCRIPTION, model.getContent());
            activity.getContentResolver().update(eventsUri, contentValues
                    , CalendarContract.Events._ID + " =? ", new String[]{model.getId()});
        } else {
            requestCalendarPermission(activity, reqPermissionCode);
        }
    }

    /**
     * 删除某条Event
     *
     * @param id id
     * @return The number of rows deleted.
     */
    public static int deleteEvent(Activity activity, String id, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
            return activity.getContentResolver()
                    .delete(eventsUri, CalendarContract.Events._ID + " =? ", new String[]{id});
        } else {
            requestCalendarPermission(activity, reqPermissionCode);
        }
        return 0;
    }

    /**
     * 删除所有Event
     *
     * @return The number of rows deleted.
     */
    public static int deleteAllEvent(Activity activity, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
            return activity.getContentResolver()
                    .delete(eventsUri, CalendarContract.Events.CALENDAR_ID + " =? ", new String[]{queryCalId(activity, reqPermissionCode)});
        } else {
            requestCalendarPermission(activity, reqPermissionCode);
        }
        return 0;
    }

    /**
     * 查询 calendar_id
     *
     * @return calId
     */
    private static String queryCalId(Activity activity, int reqPermissionCode) {
        if (checkCalendarPermission(activity)) {
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
        } else {
            requestCalendarPermission(activity, reqPermissionCode);
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


    private static boolean checkCalendarPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestCalendarPermission(Activity activity, int reqPermissionCode) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, reqPermissionCode);

    }

    public static class EventModel {
        private String title;
        private String id;
        private long startTime;
        private long endTime;
        private String content;
        private String location;

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

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}

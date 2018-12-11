package com.cylty.zhongmukeji.customCalender.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cylty.zhongmukeji.customCalender.data.CalendarDate;
import com.cylty.zhongmukeji.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private List<CalendarDate> mListData = new ArrayList<>();

    private Context context;

    public CalendarGridViewAdapter(List<CalendarDate> mListData, Context context) {
        this.mListData = mListData;
        this.context = context;
    }

    public List<CalendarDate> getListData() {
        return mListData;
    }


    public int getCount() {
        return mListData.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        CalendarDate calendarDate = mListData.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_day.setText((calendarDate.getSolar().solarDay) + "");

        if (calendarDate.getSolar().solarWeek == 0 || calendarDate.getSolar().solarWeek == 6) {
            viewHolder.tv_day.setTextColor(context.getResources().getColor(R.color.color_week_liu_ri));
        } else {
            viewHolder.tv_day.setTextColor(context.getResources().getColor(R.color.color_week_12345));
        }

        if (!mListData.get(position).isInThisMonth()) {
            viewHolder.tv_day.setVisibility(View.INVISIBLE);
        }

        if (mListData.size() - position <= 7) {
            viewHolder.buttonLine.setVisibility(View.GONE);
        }
        return convertView;
    }


    public static class ViewHolder {
        private TextView tv_day;
        private TextView tv_lunar_day;
        private View buttonLine;

        public ViewHolder(View itemView) {
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_lunar_day = (TextView) itemView.findViewById(R.id.tv_lunar_day);
            buttonLine = itemView.findViewById(R.id.buttom_line);
        }
    }
}


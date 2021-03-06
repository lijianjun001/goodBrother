package com.cylty.zmkj.calender.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cylty.zmkj.R;
import com.cylty.zmkj.calender.adapter.CalendarViewPagerAdapter;
import com.cylty.zmkj.calender.utils.DateUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


/**
 * Created by lijianjun on 4/28/16.
 */
public class CalendarViewPagerFragment extends Fragment {

    private static final String CHOICE_MODE_SINGLE = "choice_mode_single";
    private boolean isChoiceModelSingle;
    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;
    private int currentPosition;
    private ImageView leftIv, rightIv;
    private TextView dateTv;

    public CalendarViewPagerFragment() {
    }

    public static CalendarViewPagerFragment newInstance() {//boolean isChoiceModelSingle
        CalendarViewPagerFragment fragment = new CalendarViewPagerFragment();
        Bundle args = new Bundle();
        args.putBoolean(CHOICE_MODE_SINGLE, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onPageChangeListener = (OnPageChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnDateClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isChoiceModelSingle = getArguments().getBoolean(CHOICE_MODE_SINGLE, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_viewpager, container, false);
        initViewPager(view);
        return view;
    }

    private void initViewPager(View view) {
        leftIv = (ImageView) view.findViewById(R.id.left_iv);
        leftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition--;
                viewPager.setCurrentItem(currentPosition);
            }
        });
        rightIv = (ImageView) view.findViewById(R.id.right_iv);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition++;
                viewPager.setCurrentItem(currentPosition);
            }
        });
        dateTv = (TextView) view.findViewById(R.id.date_tv);
        dateTv.setText(DateUtils.getYear() + "年" + DateUtils.getMonth() + "月");
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        final CalendarViewPagerAdapter myAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(), isChoiceModelSingle);
        viewPager.setAdapter(myAdapter);
        currentPosition = CalendarViewPagerAdapter.NUM_ITEMS_CURRENT;
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                int year = myAdapter.getYearByPosition(position);
                int month = myAdapter.getMonthByPosition(position);
                dateTv.setText(year + "年" + month + "月");
                onPageChangeListener.onPageChange(year, month);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public interface OnPageChangeListener {
        void onPageChange(int year, int month);
    }
}

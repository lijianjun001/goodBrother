package com.cylty.zmkj.widget;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class VerticalPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;


    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public VerticalPagerAdapter(FragmentManager fm, List<Fragment> ListFragments) {
        super(fm);
        this.fragments = ListFragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}

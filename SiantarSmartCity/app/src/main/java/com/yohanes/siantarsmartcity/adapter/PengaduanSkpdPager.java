package com.yohanes.siantarsmartcity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yohanes.siantarsmartcity.fragment.SkpdCompletedFragment;
import com.yohanes.siantarsmartcity.fragment.SkpdOnProgressFragment;
import com.yohanes.siantarsmartcity.fragment.SkpdWaitingFragment;

/**
 * Created by Yohanes_marthin on 25/05/2016.
 */
public class PengaduanSkpdPager extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PengaduanSkpdPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SkpdWaitingFragment tab1 = new SkpdWaitingFragment();
                return tab1;
            case 1:
                SkpdOnProgressFragment tab2 = new SkpdOnProgressFragment();
                return tab2;
            case 2:
                SkpdCompletedFragment tab3 = new SkpdCompletedFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

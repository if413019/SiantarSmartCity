package com.yohanes.siantarsmartcity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yohanes.siantarsmartcity.fragment.CompletedFragment;
import com.yohanes.siantarsmartcity.fragment.OnProgressFragment;
import com.yohanes.siantarsmartcity.fragment.WatingFragment;

/**
 * Created by Yohanes_marthin on 24/05/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                WatingFragment tab1 = new WatingFragment();
                return tab1;
            case 1:
                OnProgressFragment tab2 = new OnProgressFragment();
                return tab2;
            case 2:
                CompletedFragment tab3 = new CompletedFragment();
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

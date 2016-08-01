package com.yohanes.siantarsmartcity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yohanes.siantarsmartcity.fragment.UserCompletedFragment;
import com.yohanes.siantarsmartcity.fragment.UserOnProgressFragment;
import com.yohanes.siantarsmartcity.fragment.UserWaitingFrament;

/**
 * Created by Yohanes_marthin on 25/05/2016.
 */
public class PengaduanSayaPager extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PengaduanSayaPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserWaitingFrament tab1 = new UserWaitingFrament();
                return tab1;
            case 1:
                UserOnProgressFragment tab2 = new UserOnProgressFragment();
                return tab2;
            case 2:
                UserCompletedFragment tab3 = new UserCompletedFragment();
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

package com.gpslab.kaun.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gpslab.kaun.fragment.FragmentOne;
import com.gpslab.kaun.fragment.ImportantFragment;
import com.gpslab.kaun.fragment.OtherFragment;
import com.gpslab.kaun.fragment.SpamFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                FragmentOne home = new FragmentOne();
                return home;
            case 1:
                ImportantFragment about = new ImportantFragment();
                return about;
            case 2:
                OtherFragment contact = new OtherFragment();
                return contact;

            case 3:
                SpamFragment contacts = new SpamFragment();
                return contacts;
            default:
                return null;
        }
    }
}
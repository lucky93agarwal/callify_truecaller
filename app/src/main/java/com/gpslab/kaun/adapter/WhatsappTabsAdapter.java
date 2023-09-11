package com.gpslab.kaun.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gpslab.kaun.fragment.CallFragment;
import com.gpslab.kaun.fragment.CallsFragment;
import com.gpslab.kaun.fragment.CameraFragment;
import com.gpslab.kaun.fragment.ChatsFragment;
import com.gpslab.kaun.fragment.FragmentOne;
import com.gpslab.kaun.fragment.ImportantFragment;
import com.gpslab.kaun.fragment.OtherFragment;
import com.gpslab.kaun.fragment.SpamFragment;
import com.gpslab.kaun.fragment.StatusFragment;
import com.gpslab.kaun.fragment.StatusNewFragment;

public class WhatsappTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public WhatsappTabsAdapter(FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//
//                CameraFragment camera = new CameraFragment();
//                return camera;
            case 0:
                ChatsFragment chat = new ChatsFragment();
                return chat;
//            case 2:
//                StatusFragment status = new StatusFragment();
//                return status;
//
//            case 3:
//                CallsFragment call = new CallsFragment();
//                return call;
            default:
                return null;
        }
    }
}

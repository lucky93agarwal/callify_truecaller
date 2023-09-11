package com.gpslab.kaun.Home;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.TabsAdapter;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.top_one_two,
            R.drawable.top_two_one,
            R.drawable.bagnew,
            R.drawable.sheldone
    };

    private String[] titles = {
            "Call Log",
            "Important",
            "Others",
            "Spam"
    };

    TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.home_activity_page, container, false);

        viewPager = view.findViewById(R.id.viewPager);


        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);



        tabLayout.addTab(tabLayout.newTab().setText("Call Log"));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
//        tabLayout.getTabAt(0).setIcon(R.drawable.top_one_two);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


//        tabLayout.getTabAt(0).getOrCreateBadge().setNumber(10);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tab.setText(titles[tab.getPosition()]);
                Log.d("WhatisNuameLcky", "postion  = = " + String.valueOf(position));
                if (position == 0) {
                    tabLayout.getTabAt(0).setIcon(R.drawable.top_one_two);
                    tabLayout.getTabAt(1).setIcon(R.drawable.top_two_one);
                    tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);
                    tabLayout.getTabAt(3).setIcon(R.drawable.sheldone);
                    int tabIconColor = ContextCompat.getColor(getActivity(), R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.tabSelectedIconColor));


                } else if (position == 1) {
                    tabLayout.getTabAt(1).setIcon(R.drawable.startnewicon);
                    tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
                    tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);
                    tabLayout.getTabAt(3).setIcon(R.drawable.sheldone);
                    int tabIconColor = ContextCompat.getColor(getActivity(), R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.tabSelectedIconColor));


                } else if (position == 2) {
                    tabLayout.getTabAt(2).setIcon(R.drawable.bagnewnew);
                    tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);

                    tabLayout.getTabAt(1).setIcon(R.drawable.top_two_one);
                    tabLayout.getTabAt(3).setIcon(R.drawable.sheldone);
                    int tabIconColor = ContextCompat.getColor(getActivity(), R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007AFF"));
                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.tabSelectedIconColor));


                } else if (position == 3) {

                    tabLayout.getTabAt(1).setIcon(R.drawable.top_two_one);
                    tabLayout.getTabAt(0).setIcon(R.drawable.top_one_one);
                    tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);

                    tabLayout.getTabAt(3).setIcon(R.drawable.sheldtwo);

                    int tabIconColor = ContextCompat.getColor(getActivity(), R.color.red);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ec0b0b"));

                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.red));


                } else {
                    tabLayout.getTabAt(3).setIcon(R.drawable.sheldone);
                    tabLayout.getTabAt(2).setIcon(R.drawable.bagnew);
                    int tabIconColor = ContextCompat.getColor(getActivity(), R.color.tabSelectedIconColor);
                    tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#3B8AFD"));
                    tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.tabSelectedIconColor));
                }



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getActivity(), R.color.tabUnselectedIconColor);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                tab.setText("");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("WhatisNuameLcky", "postion  = = 3" );
            }
        });
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setupTabIcons();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabsAdapter tabsAdapter = new TabsAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);

    }

}
package com.gpslab.kaun.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.ViewPagerAdapter;
import com.gpslab.kaun.fragment.FragmentOne;
import com.gpslab.kaun.fragment.FragmentTwo;

public class HomeActivity extends AppCompatActivity {




    /// it for RecyclerView
//    public List<CallLogInfo> productList = new ArrayList<>();
//    public CallLogInfo getbcdata;
//    public CallLogAdapter adapter;
//    public RecyclerView mRecyclerView;
//    public RecyclerView.LayoutManager mLayoutManager;
//    public String LIST_STATE_KEY = "list_state";
//    Cursor cursor;
//    String name, phonenumber;




    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_page);



        viewPager = findViewById(R.id.viewPager);

        addTabs(viewPager);
        ((TabLayout) findViewById(R.id.tabLayout)).setupWithViewPager( viewPager );

    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentOne(), "ONE");
        adapter.addFrag(new FragmentTwo(), "TWO");
        adapter.addFrag(new FragmentOne(), "THREE");
        viewPager.setAdapter(adapter);
    }



}
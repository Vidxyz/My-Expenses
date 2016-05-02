package com.vidhyasagar.myexpenses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vidhyasagar on 4/22/2016.
 */
public class DashboardFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int no_of_tabs = 2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate layout_tab and setup Views.
         */
        View x =  inflater.inflate(R.layout.layout_tab,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.fragment_dashboard);
        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : Log.i("applog", "switch0") ; return new DailyViewFragment();
                case 1 : Log.i("applog", "switch1") ;return new MonthlyViewFragment();
            }
            return null;
        }
        @Override
        public int getCount() {

            return no_of_tabs;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Daily View";
                case 1 :
                    return "Monthly View";
            }
            return null;
        }
    }

}


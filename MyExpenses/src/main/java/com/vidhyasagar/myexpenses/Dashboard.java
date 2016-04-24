package com.vidhyasagar.myexpenses;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {


    private static final int NUM_PAGES = 2;

    private int pageNumber;
    Boolean menuPositionLeft = true;
    Boolean menuPositionRight = false;

    private ViewPager mPager;
    private TabAdapter mPagerAdapter;

    public class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if(i == 0) {
                fragment = new OverviewFragment();
                Bundle args = new Bundle();
            }
            else if(i == 1) {
                Log.i("MyApp", "Swiped");
                fragment = new OverviewFragment();
                Bundle args = new Bundle();
                // Our object is just an integer :-P
//            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
//            fragment.setArguments(args);
            }
            return fragment;
        }



        //Implement On back pressed listener

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }



    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_red_light)));
            Log.i("MyApp", "Actionbar found");
        } else {
            Log.i("MyApp", "Actionbar not found");
        }




        //VIEWPAGER
        // Instantiate a ViewPager and a PagerAdapter.
        pageNumber = 0;

        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new TabAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("MyApp page scrolled", Integer.toString(position));
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    menuPositionLeft = true;
                    menuPositionRight = false;
                }
                else if(position == 1) {
                    menuPositionRight = true;
                    menuPositionLeft = false;
                }
                Log.i("MyApp page selected", Integer.toString(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.i("MyApp scroll state", Integer.toString(state));
                if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if(menuPositionLeft) {
                        Toast.makeText(Dashboard.this, "Menu will be shown on left now", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Dashboard.this, "Menu will be shown on right now", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

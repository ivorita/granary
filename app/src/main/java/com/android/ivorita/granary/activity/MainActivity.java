package com.android.ivorita.granary.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.ivorita.granary.R;
import com.android.ivorita.granary.adapter.viewPagerAdapter;
import com.android.ivorita.granary.fragment.dataFragment;
import com.android.ivorita.granary.fragment.historyFragment;
import com.android.ivorita.granary.util.NetWorkUtils;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private ViewPager mViewPager;
    private MenuItem mMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.data:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.historical_data:
                    mViewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mMenuItem != null) {
                mMenuItem.setChecked(false);
            } else {
                navView.getMenu().getItem(0).setChecked(false);
            }
            mMenuItem = navView.getMenu().getItem(position);
            mMenuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断网络状态
        boolean netWorkAvailable = NetWorkUtils.isNetWorkAvailable(this);
        if (!netWorkAvailable) {
            Toast.makeText(MainActivity.this, "联网：" + netWorkAvailable, Toast.LENGTH_SHORT).show();
        }

        navView = findViewById(R.id.nav_view);

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setUpViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(dataFragment.newInstance());
        viewPagerAdapter.addFragment(historyFragment.newInstance());
        viewPager.setAdapter(viewPagerAdapter);
    }

}

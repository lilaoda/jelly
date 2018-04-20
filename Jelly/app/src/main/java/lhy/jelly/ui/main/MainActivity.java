package lhy.jelly.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import lhy.jelly.R;
import lhy.jelly.bean.TabBean;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;
import lhy.lhylibrary.base.LhyActivity;
import lhy.lhylibrary.base.LhyFragment;
import lhy.lhylibrary.view.tablayout.CommonTabLayout;
import lhy.lhylibrary.view.tablayout.listener.CustomTabEntity;
import lhy.lhylibrary.view.tablayout.listener.OnTabSelectListener;

/**
 * Created by Lilaoda on 2018/3/28.
 * Email:749948218@qq.com
 */

public class MainActivity extends LhyActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawlayout)
    DrawerLayout drawlayout;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;

    @Inject
    User user;

    private List<LhyFragment> mFragment;
    private ArrayList<CustomTabEntity> mTabEntitys;
    private String[] mTitles = {"音乐", "视频", "社区", "工具"};
    private int[] mIconUnselectIds = {
            R.mipmap.home_normal, R.drawable.ic_music_note_black_24dp,
            R.mipmap.home_normal, R.mipmap.home_normal,};
    private int[] mIconSelectIds = {
            R.mipmap.home_pressed, R.drawable.ic_music_note_black_24dp,
            R.mipmap.home_pressed, R.mipmap.home_pressed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
        Logger.d(user);
    }

    private void initView() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawlayout, R.string.draw_open, R.string.draw_close);
        drawlayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        drawlayout.openDrawer(Gravity.LEFT);
                        break;
                    case R.id.item2:
                        drawlayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        mFragment = new ArrayList<>();
        mFragment.add(MusicFragment.newInstance());
        mFragment.add(VideoFragment.newInstance());
        mFragment.add(ChatFragment.newInstance());
        mFragment.add(MineFragment.newInstance());
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), mFragment));

        mTabEntitys = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntitys.add(new TabBean(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntitys);
    }

    private void initListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    static class MainAdapter extends FragmentPagerAdapter {

        private List mFragment;

        public MainAdapter(FragmentManager fm, List mFragment) {
            super(fm);
            this.mFragment = mFragment;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }
    }

}


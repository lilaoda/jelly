package lhy.jelly.ui;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.ui.fragment.ChatFragment;
import lhy.jelly.ui.fragment.MeFragment;
import lhy.jelly.ui.fragment.MusicFragment;
import lhy.jelly.ui.fragment.VideoFragment;
import lhy.lhylibrary.base.BaseActivity;
import lhy.lhylibrary.base.BaseFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawlayout)
    DrawerLayout drawlayout;
    private List<BaseFragment> mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
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
        mFragment.add(MeFragment.newInstance());
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(),mFragment));
    }


    static class MainAdapter extends FragmentPagerAdapter{

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

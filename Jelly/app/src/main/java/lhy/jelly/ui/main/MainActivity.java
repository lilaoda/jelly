package lhy.jelly.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.base.AbstractDiActivity;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;

/**
 * Created by Lilaoda on 2018/3/28.
 * Email:749948218@qq.com
 */

public class MainActivity extends AbstractDiActivity {

    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawlayout)
    DrawerLayout drawlayout;
    @BindView(R.id.bottom_view)
    BottomNavigationView bottomNavigationView;

    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        initView();
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(MusicFragment.newInstance());
        mFragments.add(VideoFragment.newInstance());
        mFragments.add(ChatFragment.newInstance());
        mFragments.add(MineFragment.newInstance());
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
                        drawlayout.openDrawer(Gravity.START);
                        break;
                    case R.id.item2:
                        drawlayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.music:
                        navigationFragment(0);
                        break;
                    case R.id.video:
                        navigationFragment(1);
                        break;
                    case R.id.chat:
                        navigationFragment(2);
                        break;
                    case R.id.setting:
                        navigationFragment(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, mFragments.get(0)).commit();

//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//        new QBadgeView(bottomNavigationView.getContext()).bindTarget(menuView.getChildAt(2)).setBadgeNumber(5).setGravityOffset(10, 0, true);
    }

    private int mCurrentItemPostion = 0;

    private void navigationFragment(int position) {
        if (mCurrentItemPostion != position) {
            Fragment fragment = mFragments.get(position);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mFragments.get(mCurrentItemPostion));
            if (fragment.isAdded()) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.add(R.id.fl_content, fragment);
            }
            fragmentTransaction.commit();
            mCurrentItemPostion = position;
        }
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}


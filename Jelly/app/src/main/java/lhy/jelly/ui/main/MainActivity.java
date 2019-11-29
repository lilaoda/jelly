package lhy.jelly.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.base.BaseActivity;
import lhy.jelly.base.Constant;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;

/**
 * Created by Lilaoda on 2018/3/28.
 * Email:749948218@qq.com
 */
@Route(path = Constant.ROUTE_PATH_JELLY_MAIN_ACTIVITY)
public class MainActivity extends BaseActivity {

    public static final String[] TABS = {"music", "video", "chat", "mine"};

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
        initFragments(savedInstanceState);
        initView();
    }

    private void initFragments(Bundle savedInstanceState) {
        mFragments = new ArrayList<>();
        MusicFragment musicFragment = null;
        VideoFragment videoFragment = null;
        ChatFragment chatFragment = null;
        MineFragment mineFragment = null;
        if (savedInstanceState != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            musicFragment = (MusicFragment) supportFragmentManager.findFragmentByTag(TABS[0]);
            videoFragment = (VideoFragment) supportFragmentManager.findFragmentByTag(TABS[1]);
            chatFragment = (ChatFragment) supportFragmentManager.findFragmentByTag(TABS[2]);
            mineFragment = (MineFragment) supportFragmentManager.findFragmentByTag(TABS[3]);
        }
        if (musicFragment == null) {
            musicFragment = MusicFragment.newInstance();
        }
        if (videoFragment == null) {
            videoFragment = VideoFragment.newInstance();
        }
        if (chatFragment == null) {
            chatFragment = ChatFragment.newInstance();
        }
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
        }
        mFragments.add(musicFragment);
        mFragments.add(videoFragment);
        mFragments.add(chatFragment);
        mFragments.add(mineFragment);
    }

    private void initView() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawlayout, R.string.draw_open, R.string.draw_close);
        drawlayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        drawlayout.openDrawer(GravityCompat.START);
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
                    case R.id.mine:
                        navigationFragment(3);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        if (getSupportFragmentManager().getFragments().size() == 0) {
            navigationFragment(0);
        }
  }

    private void navigationFragment(int position) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
            if (i == position) {
                if (fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.fl_content, fragment, TABS[position]);
                }
            }
        }
        fragmentTransaction.commit();
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                item.setShiftingMode(false);
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}


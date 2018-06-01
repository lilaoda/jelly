package lhy.jelly.ui.main;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.base.BaseActivity;
import lhy.jelly.bean.TabBean;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.ui.chat.ChatFragment;
import lhy.jelly.ui.mine.MineFragment;
import lhy.jelly.ui.music.MusicFragment;
import lhy.jelly.ui.video.VideoFragment;
import lhy.lhylibrary.base.LhyFragment;
import lhy.lhylibrary.view.tablayout.CommonTabLayout;
import lhy.lhylibrary.view.tablayout.listener.CustomTabEntity;
import lhy.lhylibrary.view.tablayout.listener.OnTabSelectListener;

/**
 * Created by Lilaoda on 2018/3/28.
 * Email:749948218@qq.com
 */

public class MainActivity extends BaseActivity  {

    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawlayout)
    DrawerLayout drawlayout;
    @BindView(R.id.tabLayout)
    CommonTabLayout tabLayout;

    @Inject
    User user;

    private ArrayList<Fragment> mFragment;
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
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            setupWindowAnimations();
        }
        initView();
        initListener();
        Logger.d(user);
    }

    @TargetApi(value = 21)
    //上一页面启动时以动画形式启动才有效果
    private void setupWindowAnimations() {
//        (1)setExitTransition() - 当A start B时，使A中的View退出场景的transition
//                (2)setEnterTransition() - 当A start B时，使B中的View进入场景的transition
//                (3)setReturnTransition() - 当B 返回 A时，使B中的View退出场景的transition
////                (4)setReenterTransition() - 当B 返回 A时，使A中的View进入场景的transition
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        slide.setDuration(1000);
//        getWindow().setEnterTransition(slide);
//        getWindow().setReturnTransition(slide);

        Explode explode = new Explode();
        explode.setDuration(1000);
//        explode.setMode(Explode.MODE_OUT);//系统默认进去时IN 出去时OUT.其它slide fade也是一样的
//        getWindow().setEnterTransition(explode);
//        getWindow().setReturnTransition(explode);

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(fade);

        TransitionSet transitionSet = new TransitionSet();
//        transitionSet.addTransition(slide);
//        transitionSet.addTransition(explode);
//        getWindow().setEnterTransition(transitionSet);
//        getWindow().setReturnTransition(transitionSet);
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
                }
                return true;
            }
        });

        mFragment = new ArrayList<>();
        mFragment.add(MusicFragment.newInstance());
        mFragment.add(VideoFragment.newInstance());
        mFragment.add(ChatFragment.newInstance());
        mFragment.add(MineFragment.newInstance());

        mTabEntitys = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntitys.add(new TabBean(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntitys, this, R.id.fl_content, mFragment);
    }

    private void initListener() {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void navigateToFragment(LhyFragment fragment) {
//        getFragmentManager().beginTransaction().replace()
    }

}


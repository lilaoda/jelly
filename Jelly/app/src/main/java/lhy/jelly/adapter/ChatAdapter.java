package lhy.jelly.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2018/8/8 10:48
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 *
 * 聊天页面中 会话、c */
public class ChatAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public ChatAdapter(FragmentManager fm, List<Fragment> list, List<String> titleList) {
        super(fm);
        fragments = list;
        titles = titleList;
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (titles == null) {
            titles = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

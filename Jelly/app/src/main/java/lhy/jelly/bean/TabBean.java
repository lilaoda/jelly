package lhy.jelly.bean;

import lhy.lhylibrary.view.tablayout.listener.CustomTabEntity;

/**
 * Created by Liheyu on 2017/9/6.
 * Email:liheyu999@163.com
 * 主页底部标签
 */

public class TabBean implements CustomTabEntity {

    private String tabTitle;
    private int tabSelectedIcon;
    private int tabUnselectedIcon;

    public TabBean(String tabTitle, int tabSelectedIcon, int tabUnselectedIcon) {
        this.tabTitle = tabTitle;
        this.tabSelectedIcon = tabSelectedIcon;
        this.tabUnselectedIcon = tabUnselectedIcon;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return tabSelectedIcon;
    }

    public void setTabSelectedIcon(int tabSelectedIcon) {
        this.tabSelectedIcon = tabSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return tabUnselectedIcon;
    }

    public void setTabUnselectedIcon(int tabUnselectedIcon) {
        this.tabUnselectedIcon = tabUnselectedIcon;
    }
}

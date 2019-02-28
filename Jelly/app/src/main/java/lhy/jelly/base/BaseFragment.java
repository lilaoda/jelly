package lhy.jelly.base;

import com.umeng.analytics.MobclickAgent;

import lhy.lhylibrary.base.LhyFragment;

/**
 * Created  on 2019/1/18 10:35
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class BaseFragment extends LhyFragment {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}

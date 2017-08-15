package lhy.lhylibrary.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;


/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public class ViewPagerIndicate extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    public static final String TAG = "ViewPagerIndicate";

    private ViewPager mViewPager;
    private LinearLayout mRootLinearlayout;
    private PagerAdapter mPagerAdapter;
    private List<String> mTitles;

    public ViewPagerIndicate(Context context) {
        this(context, null);
    }

    public ViewPagerIndicate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootLinearlayout = new LinearLayout(getContext());
        mRootLinearlayout.setOrientation(LinearLayout.HORIZONTAL);
        mRootLinearlayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mRootLinearlayout);
    }

    public void setViewPager(ViewPager viewPager, List<String> titles) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            Log.e(TAG, "ViewPagerIndicate must set ViewPager ");
            return;
        }
        this.mViewPager = viewPager;
        this.mPagerAdapter = viewPager.getAdapter();
        this.mTitles = titles;
        mViewPager.addOnPageChangeListener(this);
        notifyDataChanged();
    }

    private void addTab(final int position, String title, View tabView) {
        mTitles.add(title);
        mRootLinearlayout.addView(tabView);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    private void notifyDataChanged() {
        //    if(mPagerAdapter.getCount())
    }

    private void setSelectedTab(int position) {
        int childCount = mRootLinearlayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRootLinearlayout.getChildAt(position);
            if (i == position) {
                view.requestFocus();
                view.setFocusableInTouchMode(true);
                view.setFocusable(true);
            } else {
                view.clearFocus();
                view.setFocusableInTouchMode(false);
                view.setFocusable(false);
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

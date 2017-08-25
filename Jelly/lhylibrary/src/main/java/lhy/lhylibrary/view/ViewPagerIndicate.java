package lhy.lhylibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.List;

import lhy.lhylibrary.R;


/**
 * Created by Liheyu on 2017/8/15.
 * Email:liheyu999@163.com
 */

public class ViewPagerIndicate extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    public static final String TAG = "ViewPagerIndicate";

    private ViewPager mViewPager;
    private LinearLayout mRootLinearlayout;
    private PagerAdapter mPagerAdapter;
    private Paint mPaint;

    private List<String> mTitles;

    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mTextSize;
    private int mUnderlineColor;
    private int mUnderlineHeight;

    public ViewPagerIndicate(Context context) {
        this(context, null);
    }

    public ViewPagerIndicate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicate);
        mTextColorNormal = typedArray.getColor(R.styleable.ViewPagerIndicate_normal_textcolor, getResources().getColor(R.color.colorPrimary));
        mTextColorSelected = typedArray.getColor(R.styleable.ViewPagerIndicate_selected_textcolor, Color.BLACK);
        mUnderlineColor = typedArray.getColor(R.styleable.ViewPagerIndicate_underline_color, getResources().getColor(R.color.colorPrimary));
        mUnderlineHeight = typedArray.getColor(R.styleable.ViewPagerIndicate_underline_height, dp2px(2));
        mTextSize = typedArray.getColor(R.styleable.ViewPagerIndicate_textsize, dp2px(15));
    }

    private void initView() {
        mRootLinearlayout = new LinearLayout(getContext());
        mRootLinearlayout.setOrientation(LinearLayout.HORIZONTAL);
        mRootLinearlayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mRootLinearlayout);
        mPaint = new Paint();
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
        for (int i = 0; i < mTitles.size(); i++) {

        }
          //  if(mPagerAdapter.getCount())
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

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}

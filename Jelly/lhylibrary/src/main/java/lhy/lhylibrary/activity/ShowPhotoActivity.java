package lhy.lhylibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import lhy.lhylibrary.R;
import lhy.lhylibrary.base.LhyActivity;
import lhy.lhylibrary.utils.StatusBarUtil;


/**
 * Created by Liheyu on 2017/8/23.
 * Email:liheyu999@163.com
 * 纯用于展示图片
 */

public class ShowPhotoActivity extends LhyActivity {

    public static final String PHOTO_LIST = "photo_list";
    public static final String PHOTO_CURRENT_POSITION = "photo_current_position";

    private ViewPager mViewPager;
    private TextView mTextPosition;
    private ArrayList<String> mList;
    private int mCurrentPosition;
    private int mRawWidth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        StatusBarUtil.setColor(this, Color.BLACK);
        initData();
        initView();
    }

    private void initData() {
        mList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.hasExtra(PHOTO_LIST)) {
            mList = intent.getStringArrayListExtra(PHOTO_LIST);
        }
        if (intent.hasExtra(PHOTO_CURRENT_POSITION)) {
            int position = intent.getIntExtra(PHOTO_CURRENT_POSITION, 0);
            if (position < mList.size()) {
                mCurrentPosition = position;
            }
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTextPosition = (TextView) findViewById(R.id.text_position);
        mViewPager.setAdapter(new PhotoAdapter(this, mList));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTextPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mCurrentPosition);
        setTextPosition(mCurrentPosition);
    }


    private void setTextPosition(int position) {
        mTextPosition.setText(position + 1 + "/" + mList.size());
    }

     private class PhotoAdapter extends PagerAdapter {

        private List<String> mData;
        private Activity activity;

        public PhotoAdapter(Activity activity, List<String> mdata) {
            this.activity = activity;
            this.mData = mdata;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.show_photo_img, null, false);
            Glide.with(activity).load(mData.get(position)).into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   activity.finish();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}

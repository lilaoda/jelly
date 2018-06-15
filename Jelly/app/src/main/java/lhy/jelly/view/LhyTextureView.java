package lhy.jelly.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import lhy.ijkplayer.media.MeasureHelper;

/**
 * Created by Lihy on 2018/6/12 15:33
 * E-Mail ：liheyu999@163.com
 */
public class LhyTextureView extends TextureView{

    private MeasureHelper mMeasureHelper;

    public LhyTextureView(Context context) {
        this(context,null);
    }

    public LhyTextureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LhyTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        mMeasureHelper = new MeasureHelper(this);
    }

    public boolean shouldWaitForResize() {
        return false;
    }
    //--------------------
    // Layout & Measure
    //--------------------
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    public void setAspectRatio(int aspectRatio) {
        mMeasureHelper.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mMeasureHelper.getMeasuredWidth(), mMeasureHelper.getMeasuredHeight());
    }

}

package lhy.jelly.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Formatter;
import java.util.Locale;

import lhy.ijkplayer.media.IRenderView;
import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Lihy on 2018/6/1 14:02
 * E-Mail ：liheyu999@163.com
 */
public class VideoView extends FrameLayout implements View.OnClickListener {

    //播放指示器默认显示时间
    private static final int mDefaultTimeout = 3000;

    private final Context mContext;
    private final StringBuilder mFormatBuilder;
    private final Formatter mFormatter;
    private final WindowManager mWindowManager;

    private ImageView ivThumb;
    private TextView textCurrentTime;
    private SeekBar seekBar;
    private TextView textTotalTime;
    private ImageView fullScreenButton;
    private ImageView pauseButton;
    private LinearLayout llIndicator;
    private RelativeLayout uiController;
    private IjkVideoView ijkVideo;

    //seekBar正在拖动
    private boolean mDragging;
    //播放指示器是否正在显示
    private boolean mShowing;
    private ProgressBar loadingProgress;

    public VideoView(@NonNull Context context) {
        this(context, null);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        initControllerView();
    }

    private void initControllerView() {
        setBackgroundColor(Color.BLACK);
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.view_video, this);
        ijkVideo = view.findViewById(R.id.ijk_view);
        ivThumb = view.findViewById(R.id.iv_thumb);
        textCurrentTime = view.findViewById(R.id.text_current_time);
        seekBar = view.findViewById(R.id.sb_progress);
        textTotalTime = view.findViewById(R.id.text_total_time);
        fullScreenButton = view.findViewById(R.id.ib_full_screen);
        llIndicator = view.findViewById(R.id.ll_indicator);
        uiController = view.findViewById(R.id.play_ui);
        pauseButton = view.findViewById(R.id.ib_pause);
        loadingProgress = view.findViewById(R.id.loading);

        seekBar.setOnSeekBarChangeListener(mSeekListener);
        seekBar.setMax(1000);
        pauseButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        ijkVideo.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                loadingProgress.setVisibility(GONE);
            }
        });
        ijkVideo.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }
        });
        ijkVideo.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }
        });
        ijkVideo.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

            }
        });
    }


    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    //设置seekbar的进度，并更新当前时间与总时间
    private int setProgress() {
        if (ijkVideo == null || mDragging) {
            return 0;
        }
        int position = ijkVideo.getCurrentPosition();
        int duration = ijkVideo.getDuration();

        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            seekBar.setProgress((int) pos);

            int percent = ijkVideo.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);
        }

        textTotalTime.setText(stringForTime(duration));
        textCurrentTime.setText(stringForTime(position));

        return position;
    }

    public void show(int timeout) {
        if (!mShowing) {
            uiController.setVisibility(VISIBLE);
            setProgress();
            mShowing = true;
        }
        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        //更新进度条
        post(mShowProgress);

        if (timeout != 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    //设置是否可用，暂时只禁用进度条
    @Override
    public void setEnabled(boolean enabled) {
        if (seekBar != null) {
            seekBar.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    public void show() {
        show(mDefaultTimeout);
    }

    //隐藏指示器
    public void hide() {
        if (mShowing) {
            uiController.setVisibility(GONE);
            removeCallbacks(mShowProgress);
            mShowing = false;
        }
    }

    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    //更新进度条事件
    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mShowing && ijkVideo.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                break;
            case MotionEvent.ACTION_UP:
                show(); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);
            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = ijkVideo.getDuration();
            long newposition = (duration * progress) / 1000L;
            ijkVideo.seekTo((int) newposition);
            textCurrentTime.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            show(mDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            post(mShowProgress);
        }
    };


    // This is called whenever mAnchor's layout bound changes
    private final OnLayoutChangeListener mLayoutChangeListener =
            new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight,
                                           int oldBottom) {
                    updateLayout();
                    if (mShowing) {
                        requestLayout();
                    }
                }
            };

    //当视频大小改变时，更改控制器位置
    private void updateLayout() {
//        int[] anchorPos = new int[2];
//        mAnchor.getLocationOnScreen(anchorPos);
//
//        // we need to know the size of the controller so we can properly position it
//        // within its space
//        measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), MeasureSpec.AT_MOST),
//                MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), MeasureSpec.AT_MOST));
//
//        WindowManager.LayoutParams p = mDecorLayoutParams;
//        p.width = mAnchor.getWidth();
//        p.x = anchorPos[0] + (mAnchor.getWidth() - p.width) / 2;
//        p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
    }

    //暂停或播放
    private void doPauseResume() {
        if (ijkVideo.isPlaying()) {
            ijkVideo.pause();
        } else {
            loadingProgress.setVisibility(VISIBLE);
            ijkVideo.start();
        }
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (ijkVideo.isPlaying()) {
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            pauseButton.setContentDescription("stop");
        } else {
            pauseButton.setImageResource(android.R.drawable.ic_media_play);
            pauseButton.setContentDescription("play");
        }
    }

    public void setVideoPath(String path) {
        ijkVideo.setVideoPath(path);
    }


    public void setFullScreen() {
        int orientation = getResources().getConfiguration().orientation;
        Logger.d(orientation);
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            if (mActivity != null) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                hideActionBar();
                Logger.d("SCREEN_ORIENTATION_LANDSCAPE");
            }
        } else {
            if (mActivity != null) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Logger.d("SCREEN_ORIENTATION_PORTRAIT");
            }
        }
    }

    private void hideActionBar() {
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().hide();
            mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void showActionBar() {
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            mActivity.getSupportActionBar().show();
        }
    }


    AppCompatActivity mActivity;

    public void setActivity(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Logger.d("w:" + w + "h:" + h);
    }

    public void setConfiguration(Configuration newConfig) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        DisplayMetrics outMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(outMetrics);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            layoutParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.width = outMetrics.widthPixels;
            layoutParams.height = outMetrics.heightPixels;
//            layoutParams.width = LayoutParams.MATCH_PARENT;
//            layoutParams.height = LayoutParams.MATCH_PARENT;
        }
        setLayoutParams(layoutParams);
        requestLayout();
        Logger.d(getLayoutParams().width + "__" + getLayoutParams().height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setScreenRate(int rate) {
        DisplayMetrics outMetrics = getResources().getDisplayMetrics();
        int widthPixels = outMetrics.widthPixels;
        int heightPixels = outMetrics.heightPixels;
        int width = 0;
        int height = 0;
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {// 竖屏
            if (rate == IRenderView.AR_ASPECT_FIT_PARENT) {
                width = ijkVideo.getVideoWidth();
                height = ijkVideo.getVideoHeight();
            } else if (rate == IRenderView.AR_4_3_FIT_PARENT) {
                width = widthPixels;
                height = widthPixels * 3 / 4;
            } else if (rate == IRenderView.AR_16_9_FIT_PARENT) {
                width = widthPixels;
                height = widthPixels * 9 / 16;
            }

        } else {
            if (rate == IRenderView.AR_ASPECT_FIT_PARENT) {
                width = widthPixels;
                height = heightPixels;
            } else if (rate == IRenderView.AR_4_3_FIT_PARENT) {
                width = widthPixels;
                height = heightPixels;
            } else if (rate == IRenderView.AR_16_9_FIT_PARENT) {
                width = widthPixels;
                height = heightPixels;
            }
        }
        if (width > 0 && height > 0) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ijkVideo.getRenderView().getView().getLayoutParams();
            lp.width = width;
            lp.height = height;
            Log.d("videoView", "getView: " + width + "__" + height);
            ijkVideo.getRenderView().getView().setLayoutParams(lp);
            //    mRenderView.setVideoSize(width,height);
        }
    }

    public void toggleAspectRatio() {
        ijkVideo.toggleAspectRatio();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_pause:
                doPauseResume();
                show(mDefaultTimeout);
                break;
            case R.id.ib_full_screen:
                show(mDefaultTimeout);
                setFullScreen();
                break;

        }
    }
}

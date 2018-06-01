package lhy.jelly.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import lhy.ijkplayer.media.IMediaController;
import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;

/**
 * Created by Lihy on 2018/6/1 14:02
 * E-Mail ：liheyu999@163.com
 */
public class VideoView extends FrameLayout implements IMediaController {

    //播放指示器默认显示时间
    private static final int mDefaultTimeout = 3000;

    private final Context mContext;
    private final StringBuilder mFormatBuilder;
    private final Formatter mFormatter;
    private final WindowManager mWindowManager;

    private IjkVideoView ijkView;
    private ImageView ivThumb;
    private TextView textCurrentTime;
    private SeekBar seekBar;
    private TextView textTotalTime;
    private ImageView fullScreenButton;
    private ImageView pauseButton;
    private LinearLayout llIndicator;
    private RelativeLayout uiController;
    private MediaController.MediaPlayerControl mPlayer;

    //seekBar正在拖动
    private boolean mDragging;
    //播放指示器是否正在显示
    private boolean mShowing;
    private View mAnchor;
    private View mRoot;


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
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.view_video, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View view) {
        ijkView = view.findViewById(R.id.ijk_view);
        ivThumb = view.findViewById(R.id.iv_thumb);
        textCurrentTime = view.findViewById(R.id.text_current_time);
        seekBar = view.findViewById(R.id.sb_progress);
        textTotalTime = view.findViewById(R.id.text_total_time);
        fullScreenButton = view.findViewById(R.id.iv_full_screen);
        llIndicator = view.findViewById(R.id.ll_indicator);
        uiController = view.findViewById(R.id.play_ui);
        pauseButton = view.findViewById(R.id.iv_play);
        seekBar.setOnSeekBarChangeListener(mSeekListener);
        seekBar.setMax(1000);
        pauseButton.setOnClickListener(mPauseListener);
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
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();

        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            seekBar.setProgress((int) pos);

            int percent = mPlayer.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);
        }

        textTotalTime.setText(stringForTime(duration));
        textCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
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

    @Override
    public void show() {
        show(mDefaultTimeout);
    }

    private ArrayList<View> mShowOnceArray = new ArrayList<View>();

    @Override
    public void showOnce(View view) {
        mShowOnceArray.add(view);
        view.setVisibility(View.VISIBLE);
        show();
    }

    //隐藏指示器
    @Override
    public void hide() {
        if (mShowing) {
            uiController.setVisibility(GONE);
            removeCallbacks(mShowProgress);
            mShowing = false;
        }
        for (View view : mShowOnceArray)
            view.setVisibility(View.GONE);
        mShowOnceArray.clear();
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }


    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     * When VideoView calls this method, it will use the VideoView's parent
     * as the anchor.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    //被使用者调用
    @Override
    public void setAnchorView(View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    @Override
    public void setMediaPlayer(MediaController.MediaPlayerControl player) {
        mPlayer = player;
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
            if (!mDragging && mShowing && mPlayer.isPlaying()) {
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
                show(mDefaultTimeout); // start timeout
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

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
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

    private final View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show(mDefaultTimeout);
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
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void updatePausePlay() {
        if (mPlayer.isPlaying()) {
            pauseButton.setImageResource(android.R.drawable.ic_media_pause);
            pauseButton.setContentDescription("stop");
        } else {
            pauseButton.setImageResource(android.R.drawable.ic_media_play);
            pauseButton.setContentDescription("play");
        }
    }

    public void setVideoPath(String path) {
        ijkView.setVideoPath(path);
    }
}

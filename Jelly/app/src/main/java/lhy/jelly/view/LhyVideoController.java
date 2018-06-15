package lhy.jelly.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import lhy.jelly.R;

/**
 * Created by Lihy on 2018/6/4 11:02
 * E-Mail ：liheyu999@163.com
 */
public class LhyVideoController extends FrameLayout implements  View.OnClickListener {

    private LhyVideoView mPlayer;
    private final Context mAppContext;
    private WindowManager mWindowManager;
    private boolean mShowing;
    private boolean mDragging;
    private static final int mDefaultTimeout = 3000;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    private ImageView ivThumb;
    private ImageView ivPlay;
    private TextView textCurrentTime;
    private SeekBar seekBar;
    private TextView textTotalTime;
    private ImageView fullScreenButton;
    private ImageView pauseButton;
    private LinearLayout llIndicator;
    private LinearLayout errorController;
    private RelativeLayout playController,wifiController;
    private ProgressBar loadingProgress;

    public LhyVideoController(@NonNull Context context) {
        this(context,null,0);

    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAppContext = context.getApplicationContext();
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        initControllerView();
    }

    private void initControllerView() {
        LayoutInflater inflate = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.view_video_controller, this);
        ivThumb = view.findViewById(R.id.iv_thumb);
        ivPlay = view.findViewById(R.id.iv_play);

        textCurrentTime = view.findViewById(R.id.text_current_time);
        seekBar = view.findViewById(R.id.sb_progress);
        textTotalTime = view.findViewById(R.id.text_total_time);
        fullScreenButton = view.findViewById(R.id.ib_full_screen);
        llIndicator = view.findViewById(R.id.ll_indicator);
        playController = view.findViewById(R.id.rl_play);
        wifiController = view.findViewById(R.id.rl_wifi);
        pauseButton = view.findViewById(R.id.ib_pause);
        loadingProgress = view.findViewById(R.id.loading);
        errorController = view.findViewById(R.id.ll_error);

        seekBar.setOnSeekBarChangeListener(mSeekListener);
        seekBar.setMax(1000);
        pauseButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
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

    public void show() {
        show(mDefaultTimeout);
    }

    public void hide() {
        updateController(STATUS_HIDE);
        removeCallbacks(mShowProgress);
        if (mShowing) {
            playController.setVisibility(INVISIBLE);
            removeCallbacks(mShowProgress);
            mShowing = false;
        }
    }


    public boolean isShowing() {
        return mShowing;
    }


    public void setMediaPlayer(LhyVideoView player) {
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
            if (!mDragging  && mPlayer != null && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

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

            seekBar.setSecondaryProgress(mPlayer.getBufferPercentage() * 10);
        }

        textTotalTime.setText(stringForTime(duration));
        textCurrentTime.setText(stringForTime(position));

        return position;

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

    public void show(int timeout) {
        if (!mShowing) {
            playController.setVisibility(VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_pause:
                doPauseResume();
                show(mDefaultTimeout);
                break;
            case R.id.iv_play:
                mPlayer.play();
                break;
            case R.id.ib_full_screen:
                show(mDefaultTimeout);
                if (mOnFullScreenClickListener != null) {
                    mOnFullScreenClickListener.onFullScreenClick();
                }
                break;
        }
    }

    public static final int STATUS_IDEL=0;
    public static final int STATUS_LOADING=1;
    public static final int STATUS_PLAYING=2;
    public static final int STATUS_PAUSE=3;
    public static final int STATUS_COMPLETED=4;
    public static final int STATUS_ERROR=5;
    public static final int STATUS_NO_WIFI=6;
    public static final int STATUS_HIDE=7;

    public void updateController(int state){
        if(state==STATUS_IDEL||state==STATUS_COMPLETED){
            ivThumb.setVisibility(VISIBLE);
            ivPlay.setVisibility(VISIBLE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        }else if(state ==STATUS_LOADING){
            ivThumb.setVisibility(VISIBLE);
            loadingProgress.setVisibility(VISIBLE);
            ivPlay.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        } else if(state ==STATUS_PLAYING||state ==STATUS_PAUSE){
            playController.setVisibility(VISIBLE);
            ivThumb.setVisibility(GONE);
            ivPlay.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        } else if(state ==STATUS_ERROR){
            errorController.setVisibility(VISIBLE);
            ivThumb.setVisibility(VISIBLE);
            ivPlay.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        }else if(state ==STATUS_NO_WIFI){
            wifiController.setVisibility(VISIBLE);
            ivThumb.setVisibility(VISIBLE);
            ivPlay.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
        }else if(state ==STATUS_HIDE){
            wifiController.setVisibility(GONE);
            ivThumb.setVisibility(GONE);
            ivPlay.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
        }
    }

    public interface OnFullScreenClickListener{
        void onFullScreenClick();
    }
    private OnFullScreenClickListener mOnFullScreenClickListener;

    public void setOnFullScreenClickListener(OnFullScreenClickListener listener){
        mOnFullScreenClickListener = listener;
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

    @Override
    public void setEnabled(boolean enabled) {
        seekBar.setEnabled(enabled);
    }

    public ImageView getIvThumb() {
        return ivThumb;
    }
}

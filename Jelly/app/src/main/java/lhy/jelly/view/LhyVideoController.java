package lhy.jelly.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

import lhy.jelly.R;

/**
 * Created by Lihy on 2018/6/4 11:02
 * E-Mail ：liheyu999@163.com
 */
public class LhyVideoController extends FrameLayout implements View.OnClickListener {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_PREPARING = 1;
    /**
     * 准备完成
     */
    //    public static final int STATUS_PREPARED = 2;
    public static final int STATUS_PLAY_PAUSE = 3;
    public static final int STATUS_COMPLETED = 5;
    public static final int STATUS_ERROR = 6;
    public static final int STATUS_NO_WIFI = 7;
    //    public static final int STATUS_PAUSE = 4;
//    public static final int STATUS_HIDE = 8;
    private int mCurrentStatus = STATUS_NORMAL;


    private LhyVideoView mPlayer;
    private final Context mAppContext;
    private WindowManager mWindowManager;
    private boolean mDragging;
    private static final int mDefaultTimeout = 3000;
    private StringBuilder mFormatBuilder;
    private  Formatter mFormatter;

    private ImageView ivThumb;
    private TextView textCurrentTime;
    private TextView textTotalTime;
    private SeekBar seekBar;
    private ImageView fullScreenButton;
    private ImageView ivPlay, ivStart;
    private LinearLayout llIndicator;
    private LinearLayout errorController;
    private RelativeLayout playController, wifiController;
    private ProgressBar loadingProgress;
    private TextView textTitle;

    private boolean mShowing;

    public LhyVideoController(@NonNull Context context) {
        this(context, null, 0);

    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAppContext = context.getApplicationContext();
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
//        setClickable(true);
        requestFocus();
        initControllerView();
    }

    private void initControllerView() {
        LayoutInflater inflate = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.view_video_controller, this);
        ivThumb = view.findViewById(R.id.iv_thumb);

        textTitle = view.findViewById(R.id.controller_title);
        textCurrentTime = view.findViewById(R.id.text_current_time);
        seekBar = view.findViewById(R.id.sb_progress);
        textTotalTime = view.findViewById(R.id.text_total_time);
        fullScreenButton = view.findViewById(R.id.ib_full_screen);
        llIndicator = view.findViewById(R.id.ll_indicator);
        playController = view.findViewById(R.id.rl_play);
        wifiController = view.findViewById(R.id.rl_wifi);
        ivPlay = view.findViewById(R.id.iv_play);
        ivStart = view.findViewById(R.id.iv_start);
        loadingProgress = view.findViewById(R.id.loading);
        errorController = view.findViewById(R.id.ll_error);

        seekBar.setOnSeekBarChangeListener(mSeekListener);
        seekBar.setMax(1000);
        ivPlay.setOnClickListener(this);
        ivStart.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
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
        }
    };

    public void show() {
        show(mDefaultTimeout);
    }

    public void show(int timeout) {
        if (!mShowing) {
            setProgress();
            playController.setVisibility(VISIBLE);
            mShowing = true;
        }

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        //循环更新进度条
        post(mShowProgress);

        if (timeout != 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    //当正在播放或暂停，触摸后隐藏，当资源被释放后也可能调用，故加播放或暂停的判断
    public void hide() {
        if (mShowing) {
            playController.setVisibility(INVISIBLE);
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

    /**
     * 更新进度条事件
     */
    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mPlayer != null && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    /**
     * 设置seekbar的进度，并更新当前时间与总时间
     * @return 当前进度
     */
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

    public void setMediaPlayer(LhyVideoView player) {
        mPlayer = player;
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

    public void updateController(int state) {
        mCurrentStatus = state;
        Logger.d("mCurrentStatus:" + hashCode() + "___" + state);
        if (state == STATUS_NORMAL || state == STATUS_COMPLETED) {
            ivThumb.setVisibility(VISIBLE);
            ivStart.setVisibility(VISIBLE);
            textTitle.setVisibility(VISIBLE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        } else if (state == STATUS_PREPARING) {
            ivThumb.setVisibility(VISIBLE);
            ivStart.setVisibility(GONE);
            loadingProgress.setVisibility(VISIBLE);
            textTitle.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        } else if (state == STATUS_PLAY_PAUSE) {
            playController.setVisibility(VISIBLE);
            textTitle.setVisibility(VISIBLE);
            ivStart.setVisibility(GONE);
            ivThumb.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            errorController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
        } else if (state == STATUS_ERROR) {
            errorController.setVisibility(VISIBLE);
            ivThumb.setVisibility(VISIBLE);
            ivStart.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            wifiController.setVisibility(GONE);
            textTitle.setVisibility(GONE);
        } else if (state == STATUS_NO_WIFI) {
            wifiController.setVisibility(VISIBLE);
            ivThumb.setVisibility(VISIBLE);
            ivStart.setVisibility(GONE);
            loadingProgress.setVisibility(GONE);
            playController.setVisibility(GONE);
            errorController.setVisibility(GONE);
            textTitle.setVisibility(GONE);
        }
    }

    public ImageView getIvThumb() {
        return ivThumb;
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
            ivPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            ivPlay.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Logger.d("onTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                && mCurrentStatus == STATUS_PLAY_PAUSE) {
            toggleMediaControlsVisiblity();
        }
        return super.onTouchEvent(ev);
    }

    private void toggleMediaControlsVisiblity() {
        if (mShowing) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start:
                if (mCurrentStatus == STATUS_COMPLETED) {
                    mPlayer.rePlay();
                } else {
                    mPlayer.beginPlay();
                }
                break;
            case R.id.iv_play:
                doPauseResume();
                show(mDefaultTimeout);
                break;
            case R.id.ib_full_screen:
                show(mDefaultTimeout);
                if (mPlayer.isFullScreen()) {
                    mPlayer.exitFullScreen();
                } else {
                    mPlayer.enterFullScreen();
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        int keyCode = event.getKeyCode();
//        final boolean uniqueDown = event.getRepeatCount() == 0
//                && event.getAction() == KeyEvent.ACTION_DOWN;
//        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
//                || keyCode == KeyEvent.KEYCODE_SPACE) {
//            if (uniqueDown) {
//                doPauseResume();
//                show(mDefaultTimeout);
//                if (ivPlay != null) {
//                    ivPlay.requestFocus();
//                }
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
//            if (uniqueDown && !mPlayer.isPlaying()) {
//                mPlayer.start();
//                show(mDefaultTimeout);
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
//                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
//            if (uniqueDown && mPlayer.isPlaying()) {
//                mPlayer.pause();
//                show(mDefaultTimeout);
//            }
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
//                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
//                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
//                || keyCode == KeyEvent.KEYCODE_CAMERA) {
//            // don't show the controls for volume adjustment
//            return super.dispatchKeyEvent(event);
//        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
//            if (uniqueDown) {
//                hide();
//            }
//            return true;
//        }
//
//        show(mDefaultTimeout);
//        return super.dispatchKeyEvent(event);
//    }


}

package com.lhy.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Formatter;
import java.util.Locale;

/**
 * author: liheyu
 * date: 2019-11-20
 * email: liheyu999@163.com
 *
 * 只做播放控制 暂停播放 全屏 进度拖动
 */
public class LhyVideoController extends FrameLayout implements IMediaController {

    public static final String TAG = "LhyVideoController";

    private Formatter mFormatter;
    private TextView textCurrentTime;
    private TextView textTotalTime;
    private ImageView playButton;
    private ImageButton btnFullScreen;
    private SeekBar mProgress;

    private final Context mAppContext;
    private StringBuilder mFormatBuilder;
    private static final int sDefaultTimeout = 3000;

    private IMediaPlayerControl mPlayer;
    private boolean mDragging;
    private boolean mShowing = true;
    private ProgressBar controllerLoading;
    private LinearLayout controllerError;
    private RelativeLayout controllerNormal;
    private LinearLayout controllerCompleted;

    public LhyVideoController(@NonNull Context context) {
        this(context, null);
    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LhyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAppContext = context.getApplicationContext();
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        initView();
    }

    private void initView() {
        LayoutInflater inflate = (LayoutInflater) mAppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.view_lhy_controller, this);
        playButton = view.findViewById(R.id.iv_play);
        textCurrentTime = view.findViewById(R.id.text_current_time);
        textTotalTime = view.findViewById(R.id.text_total_time);
        btnFullScreen = view.findViewById(R.id.ib_full_screen);
        mProgress = view.findViewById(R.id.sb_progress);
        mProgress.setOnSeekBarChangeListener(mSeekListener);
        mProgress.setMax(1000);
        playButton.setOnClickListener(v -> {
            doPauseResume();
            show(sDefaultTimeout);
        });
        btnFullScreen.setOnClickListener(v -> {
            mPlayer.fullScreen();
        });

        controllerLoading = view.findViewById(R.id.controller_loading);
        controllerError = view.findViewById(R.id.controller_error);
        controllerNormal = view.findViewById(R.id.controller_normal);
        controllerCompleted = view.findViewById(R.id.controller_completed);
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
            if (textCurrentTime != null)
                textCurrentTime.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            post(mShowProgress);
        }
    };

    private final Runnable mShowProgress = this::updateProgress;

    private void updateProgress() {
        int pos = setProgress();
        if (!mDragging && mShowing && mPlayer.isPlaying()) {
            postDelayed(mShowProgress, 1000 - (pos % 1000));
        }
    }

    private final Runnable mFadeOut = this::hide;

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

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (textTotalTime != null)
            textTotalTime.setText(stringForTime(duration));
        if (textCurrentTime != null)
            textCurrentTime.setText(stringForTime(position));

        return position;
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            playButton.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mPlayer.start();
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        }
//        postDelayed(updatePlayButton, 500);
        //播放和暂停异步有延迟，不能准确显示，故点击时的切换移到上面或延迟500毫秒再显示
//        updatePausePlay();
    }

    private Runnable updatePlayButton = this::updatePausePlay;

    private void updatePausePlay() {
        if (mPlayer.isPlaying()) {
            playButton.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void hide() {
        if (mShowing) {
            setVisibility(INVISIBLE);
            removeCallbacks(mShowProgress);
            mShowing = false;
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public void setMediaPlayer(IMediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }


    @Override
    public void show(int timeout) {
        if (!mShowing) {
            setProgress();
            setVisibility(VISIBLE);
            mShowing = true;
        }

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        //循环更新进度条
        post(mShowProgress);

//        updatePausePlay();

        if (timeout != 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    @Override
    public void showOnce(View view) {

    }

    @Override
    public void setAnchorView(View view) {

    }

    @Override
    public View getView() {
        return this;
    }


    private final OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mShowing) {
                    hide();
                }
            }
            return false;
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: LhyVideController");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN");
//                show(0);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: ACTION_UP");
//                show(sDefaultTimeout);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE");
//                show(sDefaultTimeout);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: ACTION_CANCEL");
//                hide();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (playButton != null) {
                    playButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void showErrorView(){
        controllerError.setVisibility(VISIBLE);
    }

}

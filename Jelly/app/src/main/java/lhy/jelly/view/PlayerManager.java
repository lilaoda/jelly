package lhy.jelly.view;

import android.view.Surface;

import com.orhanobut.logger.Logger;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Lihy on 2018/6/11 15:48
 * E-Mail ：liheyu999@163.com
 */
public class PlayerManager {

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    private static IMediaPlayer sMediaPlayer;
    private LhyVideoView mVideoView;
    private static Surface mSurface;

    private PlayerManager() {
    }

    private static PlayerManager instance;

    public static synchronized PlayerManager instance() {
        if (instance == null) {
            instance =  new PlayerManager();
        }
        return instance;
    }

    public int getPlayer() {
        return 0;
    }

    //是否硬解码
    public boolean getUsingMediaCodec() {
        return false;
    }

    // 是否后台播放
    public static boolean getEnableBackgroundPlay() {
        return false;
    }

    public static void setMediaPlayer(IMediaPlayer mp) {
        if (sMediaPlayer != null && sMediaPlayer != mp) {
            if (sMediaPlayer.isPlaying())
                sMediaPlayer.stop();
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
        sMediaPlayer = mp;
    }

    public static IMediaPlayer getMediaPlayer() {
        IjkMediaPlayer mediaPlayer = new IjkMediaPlayer();
        mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        return mediaPlayer;
    }

    public void setCurrentVideoView(LhyVideoView videoView) {
        Logger.d("setCurrentVideoView");
        if (mVideoView != null && mVideoView != videoView) {
            mVideoView.release(true);
            mVideoView = null;
        }
        mVideoView = videoView;
    }

    public LhyVideoView getCurrenVideoView() {
        return mVideoView;
    }

    public void releaseVideoView() {
        if (mVideoView != null) {
            mVideoView.release(true);
            mVideoView = null;
        }
    }

    public void pauseVideoView() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }
}

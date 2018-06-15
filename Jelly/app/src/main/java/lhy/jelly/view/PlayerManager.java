package lhy.jelly.view;

import android.util.SparseArray;

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
    private static int mCurrentPos = -9;
    private LhyVideoView mVideoView;
    private SparseArray<LhyVideoView> mVideos = new SparseArray<>();

    private PlayerManager() {
    }

    private static PlayerManager instance;

    public static synchronized PlayerManager instance() {
        if (instance == null) {
            instance = new PlayerManager();
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
        if (mVideos.size() > 0) {
            LhyVideoView lhyVideoView = mVideos.get(0);
            if (lhyVideoView != videoView) {
                lhyVideoView.release(true);
            }
        }
        if (videoView == null) {
            mVideos.clear();
            return;
        }
        mVideos.put(0, videoView);
    }

    public LhyVideoView getCurrenVideoView() {
        return mVideos.size() > 0 ? mVideos.get(0) : null;
    }

    public void releaseVideoView() {
        if (mVideos.size() > 0)
            mVideos.get(0).release(true);
    }

    public static void setCurrentPosition(int pos) {
        mCurrentPos = pos;
    }

    public static int getCurrentPos() {
        return mCurrentPos;
    }
}

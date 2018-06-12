package lhy.jelly.view;

import android.content.Context;

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
    private static VideoView3 mVideoView3;

    public PlayerManager(Context mAppContext) {
    }

    public int getPlayer() {
        return 0;
    }

    //是否硬解码
    public boolean getUsingMediaCodec() {
        return false;
    }

    // 是否后台播放
    public boolean getEnableBackgroundPlay() {
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

    public static void setCurrentVideoView(VideoView3 videoView) {
        mVideoView3 = videoView;
    }

    public static void releaseOldView() {
        if (mVideoView3 != null) {
            mVideoView3.release(true);
        }
    }
}

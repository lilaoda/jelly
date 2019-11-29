package com.lhy.player;

import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * author: liheyu
 * date: 2019-11-20
 * email: liheyu999@163.com
 */
public class PlayManager {

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public static final boolean ENABLE_MediaCodec = false;
    public static final boolean ENABLE_MEDIACODEC_AUTO_ROTATE = false;
    public static final boolean ENABLE_MMEDIACODEC_HANDLE_RESOLUTION_CHANGE = false;
    public static final boolean ENABLE_OPENSLES = false;
    public static final boolean ENABLE_BACKGROUD_PLAY = false;

//    private static IMediaPlayer mMediaPlayer;

    private static volatile PlayManager instance;

    public static PlayManager instance() {
        if (instance == null) {
            synchronized (PlayManager.class) {
                if (instance == null) {
                    instance = new PlayManager();
                }
            }
        }
        return instance;
    }

//    public static void setMediaPlayer(IMediaPlayer mp) {
//        if (mMediaPlayer != null && mMediaPlayer != mp) {
//            if (mMediaPlayer.isPlaying())
//                mMediaPlayer.stop();
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//        mMediaPlayer = mp;
//    }

    public static IMediaPlayer getMediaPlayer() {
        return createPlayer(PV_PLAYER__AndroidMediaPlayer);
    }

    public static IMediaPlayer createPlayer(int playerType) {
        IMediaPlayer mediaPlayer = null;
        switch (playerType) {
//            case Settings.PV_PLAYER__IjkExoMediaPlayer: {
//                IjkExoMediaPlayer IjkExoMediaPlayer = new IjkExoMediaPlayer(mAppContext);
//                mediaPlayer = IjkExoMediaPlayer;
//            }
//            break;
            case PV_PLAYER__AndroidMediaPlayer: {
                mediaPlayer = new AndroidMediaPlayer();
            }
            break;
            case PV_PLAYER__IjkMediaPlayer:
            default:
                mediaPlayer = getIjkMediaPlayer();
                break;
        }
        return mediaPlayer;
    }

    private static IjkMediaPlayer getIjkMediaPlayer() {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        //准备好后是否立即播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        if (ENABLE_MediaCodec) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            if (ENABLE_MEDIACODEC_AUTO_ROTATE) {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
            } else {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
            }
            if (ENABLE_MMEDIACODEC_HANDLE_RESOLUTION_CHANGE) {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            } else {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
            }
        } else {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        }

        if (ENABLE_OPENSLES) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        } else {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        }
        return ijkMediaPlayer;
    }

    private LhyVideoView mVideoView;

    public void setCurrentView(LhyVideoView view) {
        if (mVideoView != null) {
            mVideoView.release();
            mVideoView = null;
        }
        mVideoView = view;
//        if (mVideoView.getTag() != null && mVideoView.getTag() == view.getTag()) {
//
//        } else {
//            if (mVideoView != null) {
//                mVideoView.release();
//                mVideoView = null;
//            }
//            mVideoView = view;
//        }
    }
}

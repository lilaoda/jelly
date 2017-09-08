package lhy.jelly.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.io.File;

import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;
import lhy.lhylibrary.base.BaseFragment;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends BaseFragment {

    private IjkVideoView mVideoView;

    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("VideoFragment:" + "oncreateView");
        View rootView = inflater.inflate(R.layout.fragment_video, null);

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mVideoView = (IjkVideoView) rootView.findViewById(R.id.ijkView);
//        Uri pathUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"Kalimba.mp3"));
//        Uri pathUri = Uri.parse("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Wildlife.wmv";
//        mVideoView.setVideoPath(videoPath);
        mVideoView.setVideoURI(Uri.parse("https://zv.3gv.ifeng.com/live/zhongwen800k.m3u8"));
        mVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
        Logger.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d("onstop");
        if (!mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }
}

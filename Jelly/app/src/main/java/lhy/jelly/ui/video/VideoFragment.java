package lhy.jelly.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;
import lhy.lhylibrary.base.LhyFragment;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends LhyFragment {

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
        View rootView = inflater.inflate(R.layout.fragment_video, null);

//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkffmpeg.so");

        mVideoView = (IjkVideoView) rootView.findViewById(R.id.ijkView);
//        Uri pathUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"Kalimba.mp3"));
//        Uri pathUri = Uri.parse("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

//        String videoPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"FlickAnimation.avi";
//        Logger.d(videoPath);
//        mVideoView.setVideoPath(videoPath);
////        mVideoView.setVideoURI(Uri.parse("https://zv.3gv.ifeng.com/live/zhongwen800k.m3u8"));
//        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                return false;
//            }
//        });
//        mVideoView.setRender(IjkVideoView.RENDER_TEXTURE_VIEW);
//        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IMediaPlayer iMediaPlayer) {
//              mVideoView.start();
//            }
//        });
//        mVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mVideoView.pause();
//        Logger.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Logger.d("onstop");
//        if (!mVideoView.isBackgroundPlayEnabled()) {
//            mVideoView.stopPlayback();
//            mVideoView.release(true);
//            mVideoView.stopBackgroundPlay();
//        } else {
//            mVideoView.enterBackground();
//        }
//        IjkMediaPlayer.native_profileEnd();
    }
}

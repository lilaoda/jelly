package lhy.jelly.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lhy.jelly.R;
import lhy.jelly.adapter.VideoAdapter;
import lhy.jelly.bean.VideoBean;
import lhy.lhylibrary.base.LhyFragment;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends LhyFragment {

    @BindView(R.id.rlv_video)
    RecyclerView rlvVideo;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private VideoAdapter mVideoAdapter;

    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        Logger.d("onCreateView");

//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkffmpeg.so");
//        Uri pathUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"Kalimba.mp3"));
//        Uri pathUri = Uri.parse("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");

        unbinder = ButterKnife.bind(this, rootView);
        initView();
        toolbar.setTitle("video");
        return rootView;
    }

    private void initView() {
        List<VideoBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            VideoBean videoBean = new VideoBean();
            videoBean.setTitle("title " + i);
            videoBean.setVideoPath("");
            list.add(videoBean);
        }
        rlvVideo.setLayoutManager(new LinearLayoutManager(getContext()));
        mVideoAdapter = new VideoAdapter(list);
        rlvVideo.setAdapter(mVideoAdapter);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

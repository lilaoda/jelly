package lhy.jelly.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.adapter.VideoAdapter;
import lhy.jelly.base.JellyApplicaiton;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.util.VideoUtils;
import lhy.jelly.view.VideoView3;
import lhy.lhylibrary.base.LhyFragment;
import lhy.lhylibrary.http.RxObserver;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

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
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        toolbar.setTitle("video");
        initPlayer();
        return rootView;
    }


    //{"duration":34268,"path":"/storage/emulated/0/b8273312cb47a1bc311094e634bf20e6.mp4"}
    private void initPlayer() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkffmpeg.so");
    }

    private void initView() {
        final LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rlvVideo.setLayoutManager(layout);
        mVideoAdapter = new VideoAdapter(null);
        rlvVideo.setAdapter(mVideoAdapter);
        mVideoAdapter.setOnItemClickListener(new VideoAdapter.ItemClickListener() {
            @Override
            public void onClick(VideoView3 videoView, VideoBean videoBean, int pos) {
                //videoView.setVideoPath(videoBean.getPath());
            }
        });

        Observable.just(VideoUtils.getList(JellyApplicaiton.getContext()))
                .subscribeOn(Schedulers.io())
                .compose(this.<List<VideoBean>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<List<VideoBean>>() {
                    @Override
                    public void onSuccess(List<VideoBean> value) {
                        mVideoAdapter.setNewData(value);
                        Logger.d(new Gson().toJson(value));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mVideoView.pause();
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

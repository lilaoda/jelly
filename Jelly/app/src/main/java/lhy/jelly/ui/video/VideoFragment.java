package lhy.jelly.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;
import lhy.jelly.adapter.VideoAdapter;
import lhy.jelly.view.VideoView;
import lhy.lhylibrary.base.LhyFragment;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends LhyFragment {

    //    @BindView(R.id.rlv_video)
//    RecyclerView rlvVideo;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ijk_video)
    IjkVideoView ijkVideo;
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
        initView2();
        toolbar.setTitle("video");
        initPlayer();
        return rootView;
    }

    private void initView2() {
        ijkVideo.setMediaController(new VideoView(getContext()));
    }

    //{"duration":34268,"path":"/storage/emulated/0/b8273312cb47a1bc311094e634bf20e6.mp4"}
    private void initPlayer() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkffmpeg.so");
    }

    private void initView() {
//        rlvVideo.setLayoutManager(new LinearLayoutManager(getContext()));
//        mVideoAdapter = new VideoAdapter(null);
//        rlvVideo.setAdapter(mVideoAdapter);
//        mVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                VideoBean item = mVideoAdapter.getItem(position);
//            }
//        });
//        Observable.just(VideoUtils.getList(JellyApplicaiton.getContext()))
//                .subscribeOn(Schedulers.io())
//                .compose(this.<List<VideoBean>>bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new RxObserver<List<VideoBean>>() {
//                    @Override
//                    public void onSuccess(List<VideoBean> value) {
//                        mVideoAdapter.setNewData(value);
//                        Logger.d(new Gson().toJson(value));
//                    }
//                });
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

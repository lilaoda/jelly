package lhy.jelly.ui.video;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.adapter.VideoAdapter;
import lhy.jelly.base.AbstractDiFragment;
import lhy.jelly.base.JellyApplicaiton;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.util.VideoUtils;
import lhy.lhylibrary.http.RxObserver;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends AbstractDiFragment {

    @BindView(R.id.rlv_video)
    RecyclerView rlvVideo;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private VideoAdapter mVideoAdapter;
    private View mView;

    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_video, container, false);
            unbinder = ButterKnife.bind(this, mView);
            toolbar.setTitle("video");
            initView();
        }
        return mView;
    }

    private void initView() {
        final LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rlvVideo.setLayoutManager(layout);
        mVideoAdapter = new VideoAdapter(null);
        rlvVideo.setAdapter(mVideoAdapter);

        rlvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int firstVisibleItemPosition = layout.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layout.findLastVisibleItemPosition();
//                LhyVideoView currenVideoView = PlayerManager.instance().getCurrenVideoView();
//                if(currenVideoView !=null){
//                    int tag = PlayerManager.getCurrentPos();
//                    if(tag<firstVisibleItemPosition||tag>lastVisibleItemPosition){
//                        PlayerManager.instance().releaseVideoView();
//                    }
//                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean){
                            scanVideo();
                        }else {
                            ToastUtils.showString("无读写SD卡权限");
                        }
                    }
                });


        //1*0.05
    }

    private void scanVideo() {
        Observable.just(VideoUtils.getList(JellyApplicaiton.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<VideoBean>>bindToLifecycle())
                .subscribe(new RxObserver<List<VideoBean>>() {
                    @Override
                    public void onSuccess(List<VideoBean> value) {
                        mVideoAdapter.setNewData(value);
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
}

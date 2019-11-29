package lhy.jelly.ui.video;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.adapter.VideoAdapter;
import lhy.jelly.base.BaseFragment;
import lhy.jelly.base.Constant;
import lhy.jelly.base.JellyApplication;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.util.VideoUtils;
import lhy.lhylibrary.http.RxObserver;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class VideoFragment extends BaseFragment {

    @BindView(R.id.rlv_video)
    RecyclerView rlvVideo;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private VideoAdapter mVideoAdapter;
    private View mEmptyView;

    public static VideoFragment newInstance() {

        Bundle args = new Bundle();

        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("video");
        mEmptyView = inflater.inflate(R.layout.view_empty, null, false);
        initView();
        return view;
    }

    private void initView() {
        final LinearLayoutManager layout = new LinearLayoutManager(getContext());
        rlvVideo.setLayoutManager(layout);
        mVideoAdapter = new VideoAdapter(null);
        rlvVideo.setAdapter(mVideoAdapter);
        mVideoAdapter.setEmptyView(mEmptyView);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                checkPermission();
            }
        });
        mVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ARouter.getInstance().build(Constant.ROUTE_PATH_PLAY_VIDEO_ACTIVITY)
                        .withParcelable("video",mVideoAdapter.getItem(position))
                        .navigation();
            }
        });

        rlvVideo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkPermission();
    }

    private void checkPermission() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        Disposable sdDisposable = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(b -> {
                    if (b) {
                        scanVideo();
                    } else {
                        ToastUtils.showString("无读写SD卡权限");
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    private void scanVideo() {
        Observable.just(VideoUtils.getList(JellyApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<VideoBean>>bindToLifecycle())
                .subscribe(new RxObserver<List<VideoBean>>() {
                    @Override
                    public void onSuccess(List<VideoBean> value) {
                       List<VideoBean> list = new ArrayList<>();
                        for (VideoBean videoBean : value) {
                            list.add(videoBean);
                            list.add(videoBean);
                            list.add(videoBean);
                        }
                        mVideoAdapter.setNewData(list);
                        refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void onFailure(String msg) {
                        refreshLayout.finishRefresh(false);
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
    }
}

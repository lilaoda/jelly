package lhy.jelly.ui.music;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import lhy.jelly.R;
import lhy.jelly.adapter.MusicAdapter;
import lhy.jelly.base.BaseFragment;
import lhy.jelly.bean.MusicBean;
import lhy.jelly.data.remote.ApiService;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Unbinder unbinder;
    private MusicAdapter mMusicAdapter;
    private MusicModel musicModel;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;

    @Inject
    ApiService apiService;


    public static MusicFragment newInstance() {
        Bundle args = new Bundle();
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("music");
        mLoadingView = inflater.inflate(R.layout.view_loading, (ViewGroup) recyclerView.getParent(), false);
        mEmptyView = inflater.inflate(R.layout.view_empty, (ViewGroup) recyclerView.getParent(), false);
        mErrorView = inflater.inflate(R.layout.view_error, (ViewGroup) recyclerView.getParent(), false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        musicModel = ViewModelProviders.of(this, viewModelFactory).get(MusicModel.class);
        musicModel.getMusicResource().observe(this, resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    mMusicAdapter.setEmptyView(mLoadingView);
                    break;
                case ERROR:
                    mMusicAdapter.setEmptyView(mErrorView);
                    refreshLayout.finishRefresh(false);
                    break;
                case SUCCESS:
                    List<MusicBean> value = resource.getData();
                    if (value == null || value.size() == 0) {
                        mMusicAdapter.setEmptyView(mEmptyView);
                    } else {
                        mMusicAdapter.setNewData(value);
                    }
                    refreshLayout.finishRefresh(true);
                    break;
            }
        });
    }

    private void doRefresh() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        Disposable disposable = rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(b -> {
                    if (b) {
                        scanMusic();
                    } else {
                        ToastUtils.showString("无读写SD卡权限");
                    }
                });
    }

    private void scanMusic() {
        musicModel.doRefresh();
//        musicModel.getLocalMusic()
//                .compose(bindToLifecycle())
//                .doOnSubscribe((disposable) -> mMusicAdapter.setEmptyView(mLoadingView))
//                .subscribe(new RxObserver<List<MusicBean>>() {
//                    @Override
//                    public void onSuccess(List<MusicBean> value) {
//                        if (value == null || value.size() == 0) {
//                            mMusicAdapter.setEmptyView(mEmptyView);
//                        } else {
//                            mMusicAdapter.setNewData(value);
//                        }
//                        refreshLayout.finishRefresh();
//                    }
//
//                    @Override
//                    public void onFailure(String msg) {
//                        mMusicAdapter.setEmptyView(mErrorView);
//                        refreshLayout.finishRefresh();
//                    }
//                });
    }


    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new MusicAdapter(null);
        recyclerView.setAdapter(mMusicAdapter);
        mMusicAdapter.enableSwipeItem();
        mMusicAdapter.setOnItemClickListener((adapter, view, position) -> {
            View shareView = view.findViewById(R.id.img_music);
            gotoPlayMusic(shareView);
        });

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableOverScrollDrag(false);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener((v) -> doRefresh());
    }

    @TargetApi(value = 21)
    private void gotoPlayMusic(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "name");
        startActivity(new Intent(getActivity(), MusicPlayActivity.class), activityOptions.toBundle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package lhy.jelly.ui.music;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lhy.jelly.R;
import lhy.jelly.adapter.MusicAdapter;
import lhy.jelly.base.AbstractDiFragment;
import lhy.jelly.bean.MusicBean;
import lhy.lhylibrary.http.RxObserver;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends AbstractDiFragment  {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

//    private CompositeDisposable mDisposables = new CompositeDisposable();

    private Unbinder unbinder;
    private MusicAdapter mMusicAdapter;
    private MusicModel musicModel;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;


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
        mLoadingView = getLayoutInflater().inflate(R.layout.view_loading, (ViewGroup) recyclerView.getParent(), false);
        mEmptyView = getLayoutInflater().inflate(R.layout.view_empty, (ViewGroup) recyclerView.getParent(), false);
        mErrorView = getLayoutInflater().inflate(R.layout.view_error, (ViewGroup) recyclerView.getParent(), false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        musicModel = ViewModelProviders.of(this, viewModelFactory).get(MusicModel.class);
    }

    private void doRefresh() {
        mMusicAdapter.setEmptyView(mLoadingView);
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                       if(aBoolean){
                           scanMusic();
                       }else {
                           ToastUtils.showString("无读写SD卡权限");
                       }
                    }
                });
    }

    private void scanMusic() {
        Observable.just(musicModel.getMusicList(getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<MusicBean>>bindToLifecycle())
                .subscribe(new RxObserver<List<MusicBean>>() {
                    @Override
                    public void onSuccess(List<MusicBean> value) {
                        if (value == null || value.size() == 0) {
                            mMusicAdapter.setEmptyView(mEmptyView);
                        } else {
                            mMusicAdapter.setNewData(value);
                        }
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onFailure(String msg) {
                        mMusicAdapter.setEmptyView(mErrorView);
                        refreshLayout.finishRefresh();
                    }
                });
    }


    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new MusicAdapter(null);
        recyclerView.setAdapter(mMusicAdapter);
        mMusicAdapter.enableSwipeItem();
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                View shareView = view.findViewById(R.id.img_music);
                gotoPlayMusic(shareView);
            }
        });

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableOverScrollDrag(false);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doRefresh();
            }
        });
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

package lhy.jelly.ui.music;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lhy.jelly.R;
import lhy.jelly.adapter.MusicAdapter;
import lhy.jelly.bean.MusicBean;
import lhy.jelly.data.local.entity.User;
import lhy.jelly.di.Injectable;
import lhy.jelly.util.MusicUtils;
import lhy.lhylibrary.base.LhyFragment;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends LhyFragment implements Injectable {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private MusicAdapter mMusicAdapter;
    private MediaPlayer mMediaPlayer;

    @Inject
    User user;

    public static MusicFragment newInstance() {
        Bundle args = new Bundle();
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, null);
        unbinder = ButterKnife.bind(this, view);
        toolbar.setTitle("music");
        initView();
        return view;
    }

    private void doRefresh() {
        Disposable subscribe = Flowable.timer(2, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                .compose(MusicFragment.this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.d("doRefresh  doRefresh");
                        List<MusicBean> mList = MusicUtils.getMp3Infos(requireContext());
                        mMusicAdapter.setNewData(mList);
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void doLoadMore() {
        Disposable subscribe = Flowable.timer(2, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                .compose(MusicFragment.this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.d("doLoadMore  doRefresh");
//                                refreshLayout.setLoadmoreFinished(false);
//                        refreshLayout.finishLoadMore();
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                });
    }

    private void initView() {
        List<MusicBean> mList = MusicUtils.getMp3Infos(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new MusicAdapter(mList);
        recyclerView.setAdapter(mMusicAdapter);
        mMusicAdapter.enableSwipeItem();
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                View shareView = view.findViewById(R.id.img_music);
                Logger.d(mMusicAdapter.getData().get(position));
                gotoPlayMusic(shareView);
            }
        });

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableOverScrollDrag(false);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                doLoadMore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                doRefresh();
            }
        });
    }

    @TargetApi(value = 21)
    private void gotoPlayMusic(View view) {
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "name");
        startActivity(new Intent(getActivity(),MusicPlayActivity.class),activityOptions.toBundle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

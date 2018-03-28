package lhy.jelly.ui.music;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import lhy.jelly.R;
import lhy.jelly.adapter.MusicAdapter;
import lhy.jelly.bean.MusicBean;
import lhy.jelly.util.MusicUtils;
import lhy.lhylibrary.base.LhyFragment;
import lhy.lhylibrary.utils.ToastUtils;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends LhyFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private List<MusicBean> mList;
    private MusicAdapter mMusicAdapter;
    private MediaPlayer mMediaPlayer;

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
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        mMediaPlayer = new MediaPlayer();
    }

    private void initListener() {
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showInt(position);
                // playMusic(position);
            }
        });
    }

    private void playMusic(int position) {
        MusicBean musicBean = mMusicAdapter.getData().get(position);
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.setDataSource(musicBean.getUrl());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mList = MusicUtils.getMp3Infos(getContext());
        ArrayList<MusicBean> objects = new ArrayList<>(mList.size());
        objects.addAll(mList);
        mList.addAll(objects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new MusicAdapter(R.layout.item_music, mList);
        recyclerView.setAdapter(mMusicAdapter);
        //  mMusicAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        final View headView = View.inflate(getContext(), R.layout.head_view, null);
        final TextView textHead = (TextView) headView.findViewById(R.id.text_head);

//        refreshLayout.setRefreshHeader(new RefreshHeaderWrapper(headView) {
//            @Override
//            public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
//                super.onPullingDown(percent, offset, headHeight, extendHeight);
//                if (percent > 0.5f) {
//                    textHead.setText("释放刷新");
//                } else {
//                    textHead.setText("下拉刷新");
//                }
//            }
//
//            @Override
//            public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
//                super.onReleasing(percent, offset, headHeight, extendHeight);
//                textHead.setText("正在刷新");
//            }
//
//            @Override
//            public int onFinish(RefreshLayout layout, boolean success) {
//                if (success) {
//                    textHead.setText("刷新完成");
//                } else {
//                    textHead.setText("刷新失败");
//                }
//                return super.onFinish(layout, success);
//            }
//
//            @Override
//            public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
//                super.onStartAnimator(layout, headHeight, extendHeight);
//            }
//
//            @Override
//            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//                super.onStateChanged(refreshLayout, oldState, newState);
//            }
//        });
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout freshlayout) {
                Logger.d("onLoadmore");
                Flowable.timer(2, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                        .compose(MusicFragment.this.<Long>bindToLifecycle())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                Logger.d("onLoadmore  accept");
//                                refreshLayout.setLoadmoreFinished(false);
                                refreshLayout.finishLoadmore(true);

                            }
                        });
            }

            @Override
            public void onRefresh(RefreshLayout freshlayout) {
                Logger.d("onRefresh");
                Flowable.timer(2000, TimeUnit.MILLISECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                        .compose(MusicFragment.this.<Long>bindToLifecycle())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                refreshLayout.finishRefresh(true);
                            }
                        });
            }
        });
        refreshLayout.autoRefresh();
//        mMusicAdapter.setEnableLoadMore(true);
//        mMusicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                if (mList.size() > 40) {
//                    mMusicAdapter.loadMoreEnd();
//                } else {
//                    Observable.timer(2000, TimeUnit.MILLISECONDS)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<Long>() {
//                                @Override
//                                public void accept(@NonNull Long aLong) throws Exception {
//                                    mMusicAdapter.notifyDataSetChanged();
//                                    mMusicAdapter.loadMoreComplete();
//                                }
//                            });
//                }
//            }
//        }, recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

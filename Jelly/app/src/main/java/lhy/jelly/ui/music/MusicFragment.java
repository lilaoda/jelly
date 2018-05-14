package lhy.jelly.ui.music;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lhy.jelly.Injectable;
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

public class MusicFragment extends LhyFragment implements Injectable {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
        toolbar.setTitle("music");
        initListener();
        return view;
    }

    private void initData() {
        mMediaPlayer = new MediaPlayer();
    }

    private void initListener() {

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

    private void doRefresh() {
        Disposable subscribe = Flowable.timer(2, TimeUnit.SECONDS).take(1).observeOn(AndroidSchedulers.mainThread())
                .compose(MusicFragment.this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        Logger.d("doRefresh  doRefresh");
//                                refreshLayout.setLoadmoreFinished(false);
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
                        refreshLayout.finishLoadMore();
//                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                });
    }

    private void initView() {
        mList = MusicUtils.getMp3Infos(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMusicAdapter = new MusicAdapter( mList);
        recyclerView.setAdapter(mMusicAdapter);

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


        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mMusicAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // 开启拖拽
        mMusicAdapter.enableDragItem(itemTouchHelper, R.id.text_music_name, true);
        // 开启滑动删除
        mMusicAdapter.enableSwipeItem();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setEnableOverScrollDrag(false);
        mMusicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.d(mMusicAdapter.getData().get(position));
                ToastUtils.showString(position + "");
                // playMusic(position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package lhy.jelly.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.impl.RefreshHeaderWrapper;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import lhy.jelly.R;
import lhy.jelly.adapter.MusicAdapter;
import lhy.jelly.bean.MusicInfo;
import lhy.lhylibrary.base.BaseFragment;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private Unbinder unbinder;
    private List<MusicInfo> mList;

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
        initView();
        return view;
    }

    private void initView() {
        mList = new ArrayList<>();
        getDataFromServer();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final MusicAdapter adapter = new MusicAdapter(R.layout.item_music, mList);
        recyclerView.setAdapter(adapter);
        final View headView = View.inflate(getContext(), R.layout.head_view, null);
        final View headView2 = View.inflate(getContext(), R.layout.head_view2, null);
        final TextView textHead = (TextView) headView2.findViewById(R.id.textView);
     //   headView.setVisibility(View.VISIBLE);
   //     adapter.addHeaderView(headView);

        refreshLayout.setRefreshHeader(new RefreshHeaderWrapper(headView2){
            @Override
            public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
                super.onPullingDown(percent, offset, headHeight, extendHeight);
                if(percent>0.5f){
                    textHead.setText("释放刷新");
                }else {
                    textHead.setText("下拉刷新");
                }
            }

            @Override
            public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
                super.onReleasing(percent, offset, headHeight, extendHeight);
                textHead.setText("正在刷新");
            }

            @Override
            public int onFinish(RefreshLayout layout, boolean success) {
                if(success){
                    textHead.setText("刷新完成");
                }else {
                    textHead.setText("刷新失败");
                }
                return super.onFinish(layout, success);
            }

            @Override
            public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
                super.onStartAnimator(layout, headHeight, extendHeight);
                ObjectAnimator.ofFloat(textHead,"scaleX",0f,0.5f,1f,2f).start();
            }

            @Override
            public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
                super.onStateChanged(refreshLayout, oldState, newState);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                Flowable.timer(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(@NonNull Long aLong) throws Exception {
                                    Logger.d("我刷新了");
                                    refreshlayout.finishRefresh(false);
                                }
                            });
                }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                if (mList.size() > 40) {
                    refreshlayout.setLoadmoreFinished(true);
                    adapter.loadMoreEnd();
                } else {
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(@NonNull Long aLong) throws Exception {
                                    getDataFromServer();
                                    adapter.notifyDataSetChanged();
                                    refreshlayout.finishLoadmore(true);
                                }
                            });
                    Logger.d("上拉加载更多");
                }

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
//
//        adapter.setUpFetchEnable(true);
//        adapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
//            @Override
//            public void onUpFetch() {
//                if (!adapter.isUpFetching()) {
//                    adapter.setUpFetching(true);
//                    Logger.d("开始刷新");
//                    Flowable.timer(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<Long>() {
//                                @Override
//                                public void accept(@NonNull Long aLong) throws Exception {
//                                    Logger.d("我刷新了");
//                                    adapter.setUpFetching(false);
//                                    headView.setVisibility(View.GONE);
//                                }
//                            });
//                }
//            }
//        });
//
//
//        adapter.setEnableLoadMore(true);
//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                if (mList.size() > 40) {
//                    adapter.loadMoreEnd();
//                } else {
//                    Observable.timer(2000, TimeUnit.MILLISECONDS)
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<Long>() {
//                                @Override
//                                public void accept(@NonNull Long aLong) throws Exception {
//                                    adapter.loadMoreComplete();
//                                    getDataFromServer();
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                    Logger.d("上拉加载更多");
//                }
//            }
//        }, recyclerView);
    }

    private void getDataFromServer() {
        MusicInfo musicInfo;
        for (int i = 0; i < 20; i++) {
            musicInfo = new MusicInfo();
            musicInfo.setMusicName(String.valueOf(i + "____"));
            mList.add(musicInfo);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package lhy.jelly.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lhy.jelly.R;
import lhy.jelly.base.AbstractDiFragment;
import lhy.jelly.view.LhyVideoView;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MineFragment extends AbstractDiFragment {
    @BindView(R.id.video_view)
    LhyVideoView videoView;
    private Unbinder unbinder;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_me, null);
        unbinder = ButterKnife.bind(this, inflate);
        initView();
        return inflate;
    }

    private void initView() {
        videoView.setVideoPath("/storage/emulated/0/tencent/MobileQQ/shortvideo/DCEFF3216419D2584AA76E21DF625909/1120758336390325855367927240.mp4");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

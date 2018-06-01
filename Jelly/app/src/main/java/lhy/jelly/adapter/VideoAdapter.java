package lhy.jelly.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.view.VideoView;

/**
 * Created by Lihy on 2018/4/20 14:58
 * E-Mail ：liheyu999@163.com
 */
public class VideoAdapter extends BaseQuickAdapter<VideoBean, VideoAdapter.VideoHolder> {

    public VideoAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.item_video, data);
    }

    //"duration":5667,"path":"/storage/emulated/0/Wildlife.mp4"
    @Override
    protected void convert(final VideoHolder helper, VideoBean item) {
        helper.videoView.setVideoPath(item.getPath());
    }

    public static class VideoHolder extends BaseViewHolder {

        @BindView(R.id.video_view)
        VideoView videoView;

        public VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

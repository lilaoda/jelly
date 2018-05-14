package lhy.jelly.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lhy.ijkplayer.media.IjkVideoView;
import lhy.jelly.R;
import lhy.jelly.bean.VideoBean;

/**
 * Created by Lihy on 2018/4/20 14:58
 * E-Mail ：liheyu999@163.com
 */
public class VideoAdapter extends BaseQuickAdapter<VideoBean, VideoAdapter.VideoHolder> {

    public VideoAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.item_video, data);
    }

    @Override
    protected void convert(VideoHolder helper, VideoBean item) {
        if(!TextUtils.isEmpty(item.getVideoPath()))
        helper.videoView.setVideoPath(item.getVideoPath());
        helper.textTitle.setText(item.getTitle());
    }

    public static class VideoHolder extends BaseViewHolder {

        @BindView(R.id.video_view)
        IjkVideoView videoView;
        @BindView(R.id.text_title)
        TextView textTitle;


        public VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        @OnClick({R.id.btn_start, R.id.btn_pause})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.btn_start:
                    if(!videoView.isPlaying())videoView.start();
                    break;
                case R.id.btn_pause:
                    if(videoView.isPlaying())videoView.pause();
                    break;
            }
        }

    }
}

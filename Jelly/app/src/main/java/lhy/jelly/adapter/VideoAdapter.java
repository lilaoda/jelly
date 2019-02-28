package lhy.jelly.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lhy.jelly.R;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.view.LhyVideoView;

/**
 * Created by Lihy on 2018/4/20 14:58
 * E-Mail ：liheyu999@163.com
 */
public class VideoAdapter extends BaseQuickAdapter<VideoBean,BaseViewHolder> {

    public VideoAdapter(@Nullable List<VideoBean> data) {
        super(R.layout.item_video,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        LhyVideoView videoView = helper.getView(R.id.video_view);
        videoView.setVideoPath(item.getPath());
        Glide.with(mContext).load(item.getThumbPath()).into(videoView.getIvThumb());
    }
}

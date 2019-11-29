package lhy.jelly.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lhy.player.LhyVideoView;

import java.util.List;

import lhy.jelly.R;
import lhy.jelly.bean.VideoBean;

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
        LhyVideoView videoView = helper.getView(R.id.video);
        videoView.setTitle(item.getTitle());
        videoView.setDuration(item.getDuration());
        videoView.setVideoPath(item.getPath());
//       videoView.setTag(helper.getLayoutPosition());
//        videoView.release();
//        helper.setText(R.id.text_title,item.getTitle());
//        helper.setText(R.id.text_duration,item.getDuration()+"");
//        ImageView view = helper.getView(R.id.img_thumb);
//        Glide.with(mContext).load(item.getThumbPath()).into(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LhyVideoView view = holder.getView(R.id.video_view);
        view.release();
    }

}

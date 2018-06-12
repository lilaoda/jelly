package lhy.jelly.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.bean.VideoBean;
import lhy.jelly.view.VideoView3;

/**
 * Created by Lihy on 2018/4/20 14:58
 * E-Mail ：liheyu999@163.com
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private List<VideoBean> mData;
    private Context context;
    private ItemClickListener mItemClickListener;
    private List<VideoView3> playViews = new ArrayList<>();

    public VideoAdapter(@Nullable List<VideoBean> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        return new VideoHolder(LayoutInflater.from(context).inflate(R.layout.item_video, null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        final int pos = holder.getLayoutPosition();
        final VideoView3 videoView = holder.videoView;
        final VideoBean videoBean = mData.get(pos);
        Glide.with(context).load(videoBean.getThumbPath()).into(videoView.getIvThumb());
        releaseView();
        videoView.setVideoPath(videoBean.getPath());
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logger.d(videoBean);
//                releaseView();
//                playViews.add(videoView);
//                videoView.setVideoPath(videoBean.getPath());
//                if (mItemClickListener != null) {
//                    mItemClickListener.onClick(videoView, videoBean, pos);
//
//                }
//            }
//        });
    }

    private void releaseView() {
        for (VideoView3 playView : playViews) {
            playView.release(true);
        }
        playViews.clear();
    }

    public void setNewData(List<VideoBean> value) {
        mData = value == null ? new ArrayList<VideoBean>() : value;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onClick(VideoView3 videoView, VideoBean videoBean, int pos);
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_view)
        VideoView3 videoView;

        public VideoHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

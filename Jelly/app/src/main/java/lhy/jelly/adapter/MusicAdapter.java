package lhy.jelly.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lhy.jelly.R;
import lhy.jelly.bean.MusicBean;
import lhy.lhylibrary.view.roundImageView.RoundedImageView;

/**
 * Created by Liheyu on 2017/8/30.
 * Email:liheyu999@163.com
 */

public class MusicAdapter extends BaseItemDraggableAdapter<MusicBean, MusicAdapter.MusicHolder> {

    public static final int[] mImgArray = {R.mipmap.test1, R.mipmap.test2, R.mipmap.test3, R.mipmap.test4, R.mipmap.test5};

    public MusicAdapter(@Nullable List<MusicBean> data) {
        super(R.layout.item_music, data);
    }

    @Override
    protected void convert(MusicHolder helper, MusicBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.textMusicArtist.setText(item.getArtist());
        helper.textMusicName.setText(item.getTitle());
        Glide.with(helper.imgMusic).load(mImgArray[layoutPosition % 5]).into(helper.imgMusic);
//        Glide.with(helper.imgMusic).load(item.getUrl()).into(helper.imgMusic);
    }

    @Override
    public void addData(@NonNull MusicBean data) {
        super.addData(data);
    }

    public static class MusicHolder extends BaseViewHolder {

        @BindView(R.id.img_music)
        RoundedImageView imgMusic;
        @BindView(R.id.text_music_name)
        TextView textMusicName;
        @BindView(R.id.text_music_artist)
        TextView textMusicArtist;
        @BindView(R.id.ibt_music_info)
        ImageButton ibtMusicInfo;

        public MusicHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

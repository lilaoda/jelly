package lhy.jelly.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lhy.jelly.R;
import lhy.jelly.bean.MusicBean;

/**
 * Created by Liheyu on 2017/8/30.
 * Email:liheyu999@163.com
 */

public class MusicAdapter extends BaseItemDraggableAdapter<MusicBean, BaseViewHolder> {

    public static final int[] mImgArray = {R.mipmap.test1, R.mipmap.test2, R.mipmap.test3, R.mipmap.test4, R.mipmap.test5};

    public MusicAdapter(@Nullable List<MusicBean> data) {
        super(R.layout.item_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.text_music_artist,item.getArtist());
        helper.setText(R.id.text_music_name,item.getTitle());
        ImageView view = helper.getView(R.id.img_music);
        Glide.with(view).load(mImgArray[layoutPosition % 5]).into(view);
    }

}

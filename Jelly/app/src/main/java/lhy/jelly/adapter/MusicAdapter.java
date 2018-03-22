package lhy.jelly.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lhy.jelly.R;
import lhy.jelly.bean.MusicBean;

/**
 * Created by Liheyu on 2017/8/30.
 * Email:liheyu999@163.com
 */

public class MusicAdapter extends BaseQuickAdapter<MusicBean,BaseViewHolder> {


    public MusicAdapter(@LayoutRes int layoutResId, @Nullable List<MusicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean item) {
        helper.setText(R.id.text_music_name,item.getTitle());
    }
}

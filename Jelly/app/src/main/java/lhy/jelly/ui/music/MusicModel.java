package lhy.jelly.ui.music;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import lhy.jelly.bean.MusicBean;
import lhy.jelly.util.MusicUtils;

/**
 * Created  on 2018/7/25 15:24
 * E-Mail ：liheyu999@163.com
 *
 * @author lihy
 */
public class MusicModel extends ViewModel{

    @Inject
    public MusicModel() {
    }

    public List<MusicBean> getMusicList(Context context){
       return MusicUtils.getMp3Infos(context);
    }
}

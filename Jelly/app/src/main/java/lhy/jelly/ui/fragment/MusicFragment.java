package lhy.jelly.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lhy.jelly.R;
import lhy.lhylibrary.base.BaseFragment;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class MusicFragment extends BaseFragment {


    public static MusicFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music,null);
    }
}

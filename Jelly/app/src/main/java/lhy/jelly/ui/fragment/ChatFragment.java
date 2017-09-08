package lhy.jelly.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import lhy.jelly.R;
import lhy.jelly.ui.activity.MapActivity;
import lhy.lhylibrary.activity.ShowPhotoActivity;
import lhy.lhylibrary.base.BaseFragment;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Liheyu on 2017/8/21.
 * Email:liheyu999@163.com
 */

public class ChatFragment extends BaseFragment {

    private ArrayList<String> mPhotoList;

    public static ChatFragment newInstance() {

        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, null);
        Button button = (Button) view.findViewById(R.id.btn_photo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openAlbum();
                startActivity(new Intent(getActivity(), MapActivity.class));
            }
        });

        return view;
    }

    private void openAlbum() {
        MultiImageSelector.create()
                .showCamera(true)
                .count(6)
                .multi()
                .origin(mPhotoList) // original select data set, used width #.multi()
                .start(this, 999);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999&&resultCode== Activity.RESULT_OK){
            ArrayList<String> list = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            Intent intent = new Intent(getActivity(), ShowPhotoActivity.class).putStringArrayListExtra(ShowPhotoActivity.PHOTO_LIST, list);
            intent.putExtra(ShowPhotoActivity.PHOTO_CURRENT_POSITION,3);
            startActivity(intent);
        }
    }
}

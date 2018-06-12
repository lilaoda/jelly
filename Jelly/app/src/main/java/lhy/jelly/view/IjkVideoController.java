package lhy.jelly.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import java.util.ArrayList;

import lhy.ijkplayer.media.IMediaController;

/**
 * Created by Lihy on 2018/6/4 11:37
 * E-Mail ：liheyu999@163.com
 */
public class IjkVideoController extends VideoController implements IMediaController {

    private ActionBar mActionBar;

    public IjkVideoController(Context context) {
        super(context);
    }

    public IjkVideoController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public void setSupportActionBar(@Nullable ActionBar actionBar) {
        mActionBar = actionBar;
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override
    public void show() {
        super.show();
        if (mActionBar != null)
            mActionBar.show();
    }

    @Override
    public void hide() {
        super.hide();
        if (mActionBar != null)
            mActionBar.hide();
        for (View view : mShowOnceArray)
            view.setVisibility(View.GONE);
        mShowOnceArray.clear();
    }

    //----------
    // Extends
    //----------
    private ArrayList<View> mShowOnceArray = new ArrayList<View>();

    public void showOnce(@NonNull View view) {
        mShowOnceArray.add(view);
        view.setVisibility(View.VISIBLE);
        show();
    }
}

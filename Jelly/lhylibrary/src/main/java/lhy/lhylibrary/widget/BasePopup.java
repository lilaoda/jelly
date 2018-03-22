package lhy.lhylibrary.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 下拉菜单
 * Created by 妍仔仔 on 2016/10/4.
 */
public abstract class BasePopup extends PopupWindow {

    public BasePopup() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        this.setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
    }

    protected abstract View initContentView();

    public interface PopupItemListener<D> {
        void OnItemClickListen(D value, int position);
    }

    protected PopupItemListener mListener;

    public void setOnItemListener(PopupItemListener listener) {
        this.mListener = listener;
    }
}

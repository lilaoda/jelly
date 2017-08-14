package lhy.lhylibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import lhy.lhylibrary.R;


public class CircleDialog extends Dialog {

    public CircleDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * @param view   内容VIEW
     * @param width  与屏幕宽的比率
     * @param height 与屏幕高的比率
     */
    public void setContentView(View view, int gravity, float width, float height) {
        setContentView(view);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(gravity);
        mDialogWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_circle_corner));
        if (width > 0 && height > 0) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            int defaultWidth = mDialogWindow.getWindowManager().getDefaultDisplay().getWidth();
            int defaultHeight = mDialogWindow.getWindowManager().getDefaultDisplay().getHeight();
            params.width = (int) (defaultWidth * width);
            params.height = (int) (defaultHeight * height);
            mDialogWindow.setAttributes(params);
        }
    }
    public void setContentView(View view, int gravity, float width) {
        setContentView(view);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(gravity);
        mDialogWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_circle_corner));
        if (width != 0) {
            WindowManager.LayoutParams params = mDialogWindow.getAttributes();
            int defaultWidth = mDialogWindow.getWindowManager().getDefaultDisplay().getWidth();
            params.width = (int) (defaultWidth * width);
            mDialogWindow.setAttributes(params);
        }
    }
}

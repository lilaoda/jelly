package lhy.lhylibrary.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import lhy.lhylibrary.R;


public class BottomDialog extends Dialog {

    public BottomDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * @param view   内容VIEW
     * @param height 与屏幕高的比率 0时包裹内容
     */
    public void setContentView(View view, float height) {
        setContentView(view);
        Window mDialogWindow = getWindow();
        mDialogWindow.setGravity(Gravity.BOTTOM);
        mDialogWindow.setWindowAnimations(R.style.Bottom_inout_anim);
        mDialogWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        WindowManager.LayoutParams params = mDialogWindow.getAttributes();
        Display defaultDisplay = mDialogWindow.getWindowManager().getDefaultDisplay();
        params.width = defaultDisplay.getWidth();
        if (height > 0) {
            int defaultHeight = defaultDisplay.getHeight();
            params.height = (int) (defaultHeight * height);
        }
        mDialogWindow.setAttributes(params);
    }
}

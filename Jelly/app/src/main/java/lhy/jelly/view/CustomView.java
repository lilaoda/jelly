package lhy.jelly.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lihy on 2018/5/29 11:05
 * E-Mail ：liheyu999@163.com
 */
public class CustomView extends View{

    private Paint mPaint;

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0, 0, 200, 300);
        canvas.drawRect(rect,mPaint);
        canvas.drawRoundRect(rect,100,100,mPaint);
      //  canvas.drawBitmap();
//        canvas.drawPosText();
    }
}

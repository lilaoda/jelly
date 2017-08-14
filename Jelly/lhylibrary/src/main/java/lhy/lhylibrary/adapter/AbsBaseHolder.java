package lhy.lhylibrary.adapter;

import android.content.Context;
import android.view.View;

public abstract class AbsBaseHolder<T> {
    protected Context context;

    private View view;

    public AbsBaseHolder(Context context) {
        this.context = context;
        view = initView();
        view.setTag(this);
    }

    public abstract View initView();

    public abstract  void setDatas(T data);

    public final View getView() {
        return view;
    }
}

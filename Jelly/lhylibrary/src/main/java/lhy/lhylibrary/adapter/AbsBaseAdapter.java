package lhy.lhylibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class AbsBaseAdapter<T> extends BaseAdapter {
    public Context context;
    protected List<T> datas;

    public AbsBaseAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsBaseHolder holder = null;
        if (convertView == null) {
            holder = getHolder();
            convertView = holder.getView();
        } else {
            holder = (AbsBaseHolder) convertView.getTag();
        }
        holder.setDatas(getItem(position));
        return convertView;
    }

    public abstract AbsBaseHolder getHolder();

    public final void addList(List<T> data) {
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public final void updateList(List<T> data) {
        this.datas = data;
        notifyDataSetChanged();
    }

    public final List<T> getList() {
        return datas;
    }
}

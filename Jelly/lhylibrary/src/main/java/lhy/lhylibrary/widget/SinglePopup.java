package lhy.lhylibrary.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import lhy.lhylibrary.R;

/**
 * 下拉菜单
 * Created by 妍仔仔 on 2016/10/4.
 * 简单的下拉选择 POPUP
 */
public abstract class SinglePopup<T> extends BasePopup {

    private Activity mActivity;
    private List<T> mData;
    private ListView listView;
    private ArrayAdapter<T> mAdapter;


    public SinglePopup(Activity activity, List<T> data) {
        super();
        this.mActivity = activity;
        this.mData = data;
        setContentView(initContentView());
    }

    protected View initContentView() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.view_listview, null);
        listView = (ListView) rootView.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<T>(mActivity, R.layout.view_textview, mData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mActivity).inflate(R.layout.view_textview, parent, false);
                    holder = new TextHolder();
                    holder.textView = (TextView) convertView.findViewById(R.id.pop_text);
                    convertView.setTag(holder);
                } else {
                    holder = (TextHolder) convertView.getTag();
                }
                setText(holder.textView, getItem(position));
                return convertView;
            }
        };
        listView.setAdapter(mAdapter);
        initListener();
        return rootView;
    }

    protected abstract void setText(TextView textView, T item);

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);
                mAdapter.notifyDataSetChanged();
                T t = mData.get(position);
                if (mListener != null) {
                    mListener.OnItemClickListen(t, position);
                }
                dismiss();
            }
        });
    }

    private static class TextHolder {

        TextView textView;

        private TextHolder() {

        }
    }
}

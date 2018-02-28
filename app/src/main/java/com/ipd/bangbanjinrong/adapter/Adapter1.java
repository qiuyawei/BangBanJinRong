package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.Entity1;

import java.util.List;


public class Adapter1 extends BaseAdapter {
    private List<Entity1> data;
    private List<String> contents;
    private Context context;

    public Adapter1(Context mContext, List<Entity1> data, List<String> contents) {
        this.data = data;
        context = mContext;
        this.contents = contents;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_adapter1, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText(data.get(position).title);
        if (contents.size() > 0) {
            if (TextUtils.isEmpty(contents.get(position)))
                viewHolder.tv_content.setText("暂无");
            else
                viewHolder.tv_content.setText(contents.get(position));

            if (contents.get(position).contains("预约下户") && (contents.get(contents.size() - 1).equals("6") || contents.get(contents.size() - 1).equals("7") || contents.get(contents.size() - 1).equals("11"))) {
                viewHolder.tv_content.setTextColor(context.getResources().getColor(R.color.base_color));
            } else {
                viewHolder.tv_content.setTextColor(context.getResources().getColor(R.color.black));
            }
        }


        return convertView;
    }


    public class ViewHolder {
        public TextView tv_title, tv_content;
    }


}

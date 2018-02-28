package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.DaiKuanSortEnity;

import java.util.List;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/9
 * @Email 448739075@qq.com
 */

public class DaiKuanSortAdapter extends BaseAdapter {
    private Context context;
    private List<DaiKuanSortEnity> data;

    public DaiKuanSortAdapter(Context context,List<DaiKuanSortEnity> data){
        this.context=context;
        this.data=data;
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
         ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_daikuansort,null);
            viewHolder=new ViewHolder();
            viewHolder.checkBox= (CheckBox) convertView.findViewById(R.id.item_checkbox);
            viewHolder.sortPercent= (TextView) convertView.findViewById(R.id.item_percent);
            viewHolder.sortDesc= (TextView) convertView.findViewById(R.id.item_desc);

            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.checkBox.setChecked(data.get(position).isChecked);
        viewHolder.sortPercent.setText(data.get(position).REBATE+"%");
        viewHolder.sortDesc.setText(data.get(position).AMOUNT_MIN+"-"+data.get(position).AMOUNT_MAX);
        viewHolder.checkBox.setText(data.get(position).NAME);



        return convertView;
    }


    public class ViewHolder{
        TextView sortPercent;
        TextView sortDesc;
        CheckBox checkBox;

    }
}

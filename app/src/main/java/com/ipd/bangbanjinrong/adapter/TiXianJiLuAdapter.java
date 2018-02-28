package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.TiXianJiLuEnity;

import java.util.List;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/29
 * @Email 448739075@qq.com
 */

public class TiXianJiLuAdapter extends BaseAdapter {
    private Context context;
    private List<TiXianJiLuEnity> data;

    public TiXianJiLuAdapter(Context context,List<TiXianJiLuEnity> data){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(context, R.layout.item_ti_xian_ji_lu,null);
            viewHolder=new ViewHolder();
            viewHolder.tv_money= (TextView) convertView.findViewById(R.id.tv_money);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_state= (TextView) convertView.findViewById(R.id.tv_state);

            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_money.setText(data.get(position).MONEY);
        viewHolder.tv_time.setText(data.get(position).CREATETIME);
        /*0:待审核  1:已审核 2:已拒绝*/
        switch (data.get(position).VERIFY){
            case "0":
                viewHolder.tv_state.setText("待审核");
                viewHolder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case "1":
                viewHolder.tv_state.setText("已审核");
                viewHolder.tv_state.setTextColor(context.getResources().getColor(R.color.lightgreen));
                break;
            case "2":
                viewHolder.tv_state.setText("已拒绝");
                viewHolder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }



        return convertView;
    }


    class ViewHolder{
        TextView tv_money,tv_time,tv_state;
    }

}

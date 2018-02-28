package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.Entity2;

import java.util.List;


public class Adapter2 extends BaseAdapter {
	private List<Entity2> data;
	private Context context;
	public Adapter2(Context mContext, List<Entity2> data) {
		this.data = data;
		context = mContext; 
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
			convertView=View.inflate(context, R.layout.item_adapter2,null);
			viewHolder=new ViewHolder();
			viewHolder.tv_date= (TextView) convertView.findViewById(R.id.tv_date);
			viewHolder.tv_bj= (TextView) convertView.findViewById(R.id.tv_bj);
			viewHolder.tv_fwf= (TextView) convertView.findViewById(R.id.tv_fuw);
            viewHolder.button= (TextView) convertView.findViewById(R.id.bt_text);

			convertView.setTag(viewHolder);
		}else {
			viewHolder= (ViewHolder) convertView.getTag();
		}

		viewHolder.tv_date.setText(data.get(position).DATE);
		viewHolder.tv_bj.setText(data.get(position).MONEY);
		viewHolder.tv_fwf.setText(data.get(position).MONEYED);
//0:未还   1:已还  2:逾期
        switch (data.get(position).STATUS){
            case 0:
            	viewHolder.button.setVisibility(View.GONE);
                viewHolder.button.setBackgroundResource(R.drawable.shape_normal_button_green);
                viewHolder.button.setText("未还");
                break;
            case 1:
				viewHolder.button.setVisibility(View.VISIBLE);
				viewHolder.button.setBackgroundResource(R.drawable.shape_normal_button_green);
                viewHolder.button.setText("已还");
                break;
            case 2:
				viewHolder.button.setVisibility(View.VISIBLE);
				viewHolder.button.setBackgroundResource(R.drawable.shape_normal_button);
                viewHolder.button.setText("逾期");

                break;
        }

//		viewHolder.tv_content.setText(data.get(position).title);

		return convertView;
	}


	public class ViewHolder{
        TextView tv_date,tv_bj,tv_fwf;
		TextView button;
	}


}

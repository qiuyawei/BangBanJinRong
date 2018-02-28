package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.RecordEntity;

import java.util.List;


public class Adapter3 extends BaseAdapter {
	private List<RecordEntity> data;
	private Context context;
	public Adapter3(Context mContext, List<RecordEntity> data) {
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
			convertView=View.inflate(context, R.layout.item_adapter1,null);
			viewHolder=new ViewHolder();
			viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);

			convertView.setTag(viewHolder);
		}else {
			viewHolder= (ViewHolder) convertView.getTag();
		}

		switch (data.get(position).TYPE){
			case 0:
				viewHolder.tv_title.setText("支付下户");
				viewHolder.tv_content.setTextColor(context.getResources().getColor(R.color.red));
				viewHolder.tv_content.setText("-"+data.get(position).BALANCE);

				break;
			case 1:
				viewHolder.tv_title.setText("奖励");
				viewHolder.tv_content.setTextColor(context.getResources().getColor(R.color.green));
				viewHolder.tv_content.setText("+"+data.get(position).BALANCE);

				break;
		}

		return convertView;
	}


	public class ViewHolder{
		public TextView tv_title,tv_content;
	}


}

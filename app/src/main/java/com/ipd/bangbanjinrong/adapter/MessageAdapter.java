package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.MessageEntity;

import java.util.List;


/**
 * 消息适配器
 */
public class MessageAdapter extends BaseAdapter {
	private List<MessageEntity> data;
	private Context context;
	public MessageAdapter(Context mContext, List<MessageEntity> data) {
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
			convertView=View.inflate(context, R.layout.item_message,null);
			viewHolder=new ViewHolder();
			viewHolder.time= (TextView) convertView.findViewById(R.id.tv_time);
			viewHolder.title= (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.iv_dot= (ImageView) convertView.findViewById(R.id.iv_dot);

			convertView.setTag(viewHolder);
		}else {
			viewHolder= (ViewHolder) convertView.getTag();
		}

		viewHolder.time.setText(data.get(position).CREATETIME);
		viewHolder.title.setText(data.get(position).CONTENT);
		switch (data.get(position).STATUS){
			case "1":
				//未读消息显示小红点
				viewHolder.iv_dot.setVisibility(View.VISIBLE);
				break;
			case "2":
				viewHolder.iv_dot.setVisibility(View.GONE);
				break;
		}

		return convertView;
	}


	public class ViewHolder{
		public TextView time,title;
		public ImageView iv_dot;
	}


}

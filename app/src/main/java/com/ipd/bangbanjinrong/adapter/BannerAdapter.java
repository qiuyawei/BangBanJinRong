package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ipd.bangbanjinrong.activity.AdvDetail;
import com.ipd.bangbanjinrong.entity.AdvEntity;
import com.ipd.bangbanjinrong.global.Constant;

import java.util.List;


/**
 * 广告适配器
 */
public class BannerAdapter extends PagerAdapter {
	private List<AdvEntity> adList;
	private Context context;
	public BannerAdapter(Context mContext, List<AdvEntity> adList) {
		this.adList = adList;
		context = mContext; 
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object instantiateItem(final ViewGroup container, int position) {
		final int pos = position % adList.size();
		ImageView imageView = new ImageView(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//		保持比例
//		imageView.setAdjustViewBounds(true);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//图片点击
				Intent intent=new Intent(context, AdvDetail.class);
				intent.putExtra("entity",adList.get(pos));
				context.startActivity(intent);
			}
		});
//		imageLoader.displayImage(adList.get(pos), imageView, options);
//		CommonUtil.MyGlide(context,adList.get(pos),imageView);
		Glide.with(context).load(Constant.IMAGE_API+adList.get(pos).URL).into(imageView);
//		imageView.setImageResource(R.mipmap.test_banner);
		container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		return imageView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}


}

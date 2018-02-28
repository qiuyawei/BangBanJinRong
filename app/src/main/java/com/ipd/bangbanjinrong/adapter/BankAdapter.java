package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;
import com.ipd.bangbanjinrong.entity.BankEntity;

import java.util.List;


/**
 * 银行卡适配器
 */
public class BankAdapter extends BaseAdapter {
    private List<BankEntity> data;
    private Context context;

    public BankAdapter(Context mContext, List<BankEntity> data) {
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_bank, null);
            viewHolder = new ViewHolder();
            viewHolder.cardNumber = (TextView) convertView.findViewById(R.id.tv_cardMumber);
            viewHolder.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cardNumber.setText("**** **** **** " + data.get(position).CARDID.substring(data.get(position).CARDID.length() - 4, data.get(position).CARDID.length()));
        switch (data.get(position).BANK_CODE) {
            case "0102":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.gong_shang_bank);//
                break;
            case "0103":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.nong_hang);//
                break;
            case "0104":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.zhong_guo_yin_hang);//
                break;
            case "0105":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.jian_hang_bank);//
                break;
            case "0301":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.jiao_hang);//
                break;
            case "0308":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.zhao_shang);//
                break;
            case "0309":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.xing_ye);//
                break;
            case "0305":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.min_sheng);//
                break;
            case "0306":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.guang_fa_bank);//
                break;
            case "0307":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.ping_an);//
                break;
            case "0310":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.pu_fa);//
                break;
            case "0304":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.hua_xia_bank);//
                break;
            case "0313":

                viewHolder.rl_bg.setBackgroundResource(R.mipmap.city_shang_ye_yh);//
                break;
            case "0314":

                viewHolder.rl_bg.setBackgroundResource(R.mipmap.country_shang_ye_yh);//
                break;
            case "0315":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.heng_feng_bank);//
                break;

            case "0403":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.you_zheng);//
                break;
            case "0303":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.guang_da_bank);//
                break;
            case "0302":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.zhong_xin);//
                break;
            case "0316":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.zhe_shang);//
                break;

            case "0318":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.bo_hai_bank);//
                break;
            case "0319":
                viewHolder.rl_bg.setBackgroundResource(R.mipmap.wei_shang_bank);//
                break;
            case "0320":

                viewHolder.rl_bg.setBackgroundResource(R.mipmap.cz_yh);//
                break;
            case "0322":

                viewHolder.rl_bg.setBackgroundResource(R.mipmap.sh_ns_yh);//
                break;
            case "0402":

                viewHolder.rl_bg.setBackgroundResource(R.mipmap.nc_xys);
                break;
        }

        return convertView;
    }

    public class ViewHolder {
        TextView cardNumber;
        RelativeLayout rl_bg;
    }





}

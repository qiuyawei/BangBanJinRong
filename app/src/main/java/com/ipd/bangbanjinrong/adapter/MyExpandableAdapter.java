package com.ipd.bangbanjinrong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipd.bangbanjinrong.R;

import java.util.List;

/**
 * @author qiu_ya_wei
 * @Date 2017/8/15
 * @Email 448739075@qq.com
 * 订单详情适配器
 */

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private List<String> groupArray;
    private String[][] childArray=
            {{"订单编号","提交订单时间","借款人","借款人身份证号","订单状态","签约机构"},
                    {"初评申请金额","房产证号","抵押顺位"},
                    {"初评结果","最高可贷金额"},
                    {"调查预约结果","调查员姓名","调查事件","联系电话","支付情况"},
                    {"调查状态","补充资料","奖励情况"},
                    {"审批结果","产品类型","审批金额","最高期限","基准借贷利率","基准服务利率"},
                    {"产品类型","合同借款金额","合同借款期限","总点位","合同借款利率","月利息","合同服务费率","合同服务费","平台费率","返佣平台费","返佣服务费","返佣金额"},
                    {"面签地址","面签资料"},
                    {"进抵预约结果","权证员姓名","预约时间","联系电话"},
                    {"放款时间","放款户名","放款账户","放款金额"}
            };;
    private Context mContext;

    public MyExpandableAdapter(Context context, List<String> groupArray){
        mContext = context;
        this.groupArray = groupArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        GroupHolder holder = null;
        if(view == null){
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.expandlist_group, null);
            holder.groupName = (TextView)view.findViewById(R.id.tv_group_name);
            holder.arrow = (ImageView)view.findViewById(R.id.iv_arrow);
            view.setTag(holder);
        }else{
            holder = (GroupHolder)view.getTag();
        }

        //判断是否已经打开列表
        if(isExpanded){
            holder.arrow.setBackgroundResource(R.mipmap.up_arrow);
        }else{
            holder.arrow.setBackgroundResource(R.mipmap.down_arrow);
        }

        holder.groupName.setText(groupArray.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if(view == null){
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.expandlist_item, null);
            holder.childName = (TextView)view.findViewById(R.id.tv_child_name);
            holder.tv_content = (TextView)view.findViewById(R.id.tv_content);

            holder.divider = (View) view.findViewById(R.id.line);
            view.setTag(holder);
        }else{
            holder = (ChildHolder)view.getTag();
        }

        if(childPosition == 0){
            holder.divider.setVisibility(View.GONE);
        }

        holder.childName.setText(childArray[groupPosition][childPosition]);
        Log.i("TAG","posc="+childPosition);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder{
        public TextView groupName;
        public ImageView arrow;
    }

    class ChildHolder{
        public TextView childName,tv_content;
        public View divider;
    }
}

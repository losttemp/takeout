package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class DeliveryTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<ArriveTimeBean.MeituanBean.DataBean.TimelistBean> mData;
    private int mDatePos = -1;
    private int mTimepos = -1;
    private int mPreDatePos = -1;

    public DeliveryTimeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ArriveTimeBean.MeituanBean.DataBean.TimelistBean> data,
                        int datepos) {
        mData = data;
        mDatePos = datepos;
        notifyDataSetChanged();
    }

    public void setCurrentItem(int currentTimeItem, int currentDateItem) {
        this.mTimepos = currentTimeItem;
        this.mPreDatePos = currentDateItem;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {

        return mData == null ? 0 : mData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return mData == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_time_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_time = convertView.findViewById(R.id.tv_delivery_time);
            viewHolder.tv_cost = convertView.findViewById(R.id.tv_delivery_cost);
            viewHolder.img_select = convertView.findViewById(R.id.img_delivery_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mTimepos == position) {
            viewHolder.tv_time.setTextColor(mContext.getResources().getColor(R.color.select_date_color));
            viewHolder.tv_cost.setTextColor(mContext.getResources().getColor(R.color.select_date_color));
            viewHolder.img_select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_time.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.tv_cost.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.img_select.setVisibility(View.INVISIBLE);
        }


        String time = mData.get(position).getView_time();
        String shipping_fee = mData.get(position).getView_shipping_fee();

        int unixtime = mData.get(position).getUnixtime();
        if (unixtime == 0) {
            viewHolder.tv_time.setText(mContext.getString(R.string.delivery_immediately));
        } else {
            viewHolder.tv_time.setText(time);
        }
        viewHolder.tv_cost.setText(shipping_fee);
        viewHolder.img_select.setImageResource(R.drawable.choose_64);

        if (mTimepos == position && mDatePos == mPreDatePos) {
            viewHolder.img_select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.img_select.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView tv_time;
        public TextView tv_cost;
        public ImageView img_select;
    }
}

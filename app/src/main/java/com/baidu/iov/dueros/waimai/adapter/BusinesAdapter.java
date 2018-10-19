package com.baidu.iov.dueros.waimai.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.bumptech.glide.Glide;

import java.util.List;
public class BusinesAdapter extends BaseAdapter {

    private Context mContext;
    
    private List<BusinessBean.Business.OpenPoiBaseInfo> mData;

    public BusinesAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<BusinessBean.Business.OpenPoiBaseInfo> data) {
        mData = data;
        notifyDataSetChanged();
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
            convertView = View.inflate(mContext, R.layout.item_business, null);
            viewHolder = new ViewHolder();
            viewHolder.ivBusiness= convertView.findViewById(R.id.iv_business);
            viewHolder.tvBusinessName= convertView.findViewById(R.id.tv_business_name); 
            viewHolder.tvBusinessScore= convertView.findViewById(R.id.tv_business_score);
            viewHolder.tvMonthSaleNume= convertView.findViewById(R.id.tv_month_sale_nume);
            viewHolder.tvMinPrice= convertView.findViewById(R.id.tv_min_price);
            viewHolder.tvShippingFee= convertView.findViewById(R.id.tv_shipping_fee);
            viewHolder.tvDistance= convertView.findViewById(R.id.tv_distance);
            viewHolder.tvAvgDeliveryTime= convertView.findViewById(R.id.tv_avg_delivery_time);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BusinessBean.Business.OpenPoiBaseInfo mOpenPoiBaseInfo = mData.get(position);
        viewHolder.tvBusinessName.setText(mOpenPoiBaseInfo.getName());
        viewHolder.tvBusinessScore.setText(""+mOpenPoiBaseInfo.getWmPoiScore());
        viewHolder.tvMonthSaleNume.setText(mOpenPoiBaseInfo.getMonthSaleNum());
        viewHolder.tvMinPrice.setText(""+mOpenPoiBaseInfo.getMinPrice()); 
        viewHolder.tvShippingFee.setText(""+mOpenPoiBaseInfo.getShippingFee());
        viewHolder.tvDistance.setText(mOpenPoiBaseInfo.getDistance());
        viewHolder.tvAvgDeliveryTime.setText(mOpenPoiBaseInfo.getAvgDeliveryTime());
        Glide.with(mContext).load(mOpenPoiBaseInfo.getPicUrl()).into(viewHolder.ivBusiness);
        
        return convertView;
    }



    public class ViewHolder {
        private ImageView ivBusiness;
        private TextView tvBusinessName;
        private TextView tvBusinessScore;
        private TextView tvMonthSaleNume;
        private TextView tvMinPrice;
        private TextView tvShippingFee;
        private TextView tvDistance;
        private TextView tvAvgDeliveryTime;
       
    }
}

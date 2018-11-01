package com.baidu.iov.dueros.waimai.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.view.RatingBar;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.List;
public class BusinesAdapter extends BaseAdapter {

    private Context mContext;
    
    private List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo> mData;

    public BusinesAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo> data) {
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
            viewHolder.tvAveragePrice= convertView.findViewById(R.id.tv_average_price);
            viewHolder.ratingBar = convertView.findViewById(R.id.ratingBar);
            viewHolder.tvStatusDesc = convertView.findViewById(R.id.tv_status_desc);
            viewHolder.rl= convertView.findViewById(R.id.rl);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BusinessBean.MeituanBean.Business.OpenPoiBaseInfo mOpenPoiBaseInfo = mData.get(position);
        viewHolder.tvBusinessName.setText(mOpenPoiBaseInfo.getName());
        viewHolder.tvBusinessScore.setText(""+mOpenPoiBaseInfo.getWmPoiScore());
        viewHolder.tvMonthSaleNume.setText(String.format(mContext.getResources().getString(R.string
                .month_sale_num), mOpenPoiBaseInfo.getMonthSaleNum()));
        viewHolder.tvMinPrice.setText(String.format(mContext.getResources().getString(R.string
                .min_price),mOpenPoiBaseInfo.getMinPrice())); 
        viewHolder.tvShippingFee.setText(String.format(mContext.getResources().getString(R.string
                .shipping_fee),mOpenPoiBaseInfo.getShippingFee()));
        viewHolder.tvDistance.setText(mOpenPoiBaseInfo.getDistance());
        viewHolder.tvAvgDeliveryTime.setText(String.format(mContext.getResources().getString(R.string
                .delivery_time),mOpenPoiBaseInfo.getAvgDeliveryTime()));
        viewHolder.tvAveragePrice.setText(""+mOpenPoiBaseInfo.getAveragePriceTip());
        viewHolder.ratingBar.setClickable(false);
        viewHolder.ratingBar.setStar((float) mOpenPoiBaseInfo.getWmPoiScore());
        if (mOpenPoiBaseInfo.getStatus()==Constant.STROE_STATUS_NORMAL){
            viewHolder.tvStatusDesc.setVisibility(View.GONE);
        }else{
            viewHolder.tvStatusDesc.setVisibility(View.VISIBLE);
            viewHolder.tvStatusDesc.setText(mOpenPoiBaseInfo.getStatusDesc());
        }
        if (mOpenPoiBaseInfo.getStatus()==Constant.STROE_STATUS_BREAK){
            viewHolder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }else{
            viewHolder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
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
        private TextView tvAveragePrice;
        private RatingBar ratingBar;
        private TextView tvStatusDesc;
        private RelativeLayout rl;
    }
}

package com.baidu.iov.dueros.waimai.adapter;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.BusinessBean;
import com.baidu.iov.dueros.waimai.ui.BusinessActivity;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.NoClikRecyclerView;
import com.baidu.iov.dueros.waimai.view.RatingBar;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;

import java.util.List;
public class BusinesAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = BusinesAdapter.class.getSimpleName();
    private Context mContext;
    
    private List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo> mData;

    private BusinesAdapter.OnBusinessItemClickListener mOnItemClickListener;

    private static  final int FREE_DISTRIBUTION=0;

    private static  final int DEFALUT_SHOW=2;


    public BusinesAdapter(Context context) {
        mContext = context;
    }

    
    public void setData(List<BusinessBean.MeituanBean.Business.OpenPoiBaseInfo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnBusinessItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            viewHolder.tvBusinessIndex= convertView.findViewById(R.id.tv_business_index);
            viewHolder.rvDiscount= convertView.findViewById(R.id.rv_discount);
            viewHolder.rlDiscount=convertView.findViewById(R.id.rl_discount);
            viewHolder.ivDiscount=convertView.findViewById(R.id.iv_discount);
            //横向列表布局
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            viewHolder.rvDiscount.setLayoutManager(manager);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        BusinessBean.MeituanBean.Business.OpenPoiBaseInfo mOpenPoiBaseInfo = mData.get(position);
        viewHolder.tvBusinessIndex.setText(""+(position+1));
        viewHolder.tvBusinessName.setText(mOpenPoiBaseInfo.getName());
        viewHolder.tvBusinessScore.setText(""+mOpenPoiBaseInfo.getWmPoiScore());
        viewHolder.tvMonthSaleNume.setText(String.format(mContext.getResources().getString(R.string
                .month_sale_num), mOpenPoiBaseInfo.getMonthSaleNum()));
        viewHolder.tvMinPrice.setText(String.format(mContext.getResources().getString(R.string
                .min_price),mOpenPoiBaseInfo.getMinPrice())); 
        if (mOpenPoiBaseInfo.getShippingFee()!=FREE_DISTRIBUTION) {
            viewHolder.tvShippingFee.setText(String.format(mContext.getResources().getString(R.string
                    .shipping_fee), mOpenPoiBaseInfo.getShippingFee()));
        }else{
            viewHolder.tvShippingFee.setText(mContext.getString(R.string.free_distribution));
        }
        viewHolder.tvDistance.setText(mOpenPoiBaseInfo.getDistance());
        viewHolder.tvAvgDeliveryTime.setText(String.format(mContext.getResources().getString(R.string
                .delivery_time),mOpenPoiBaseInfo.getAvgDeliveryTime()));
        viewHolder.tvAveragePrice.setText(""+mOpenPoiBaseInfo.getAveragePriceTip());
        viewHolder.ratingBar.setClickable(false);
        viewHolder.ratingBar.setStar((float) mOpenPoiBaseInfo.getWmPoiScore());
        final BusDiscountAdpater mBusDiscountAdpater=new BusDiscountAdpater(mContext);
        if (mOpenPoiBaseInfo.getDiscounts().isEmpty()||mOpenPoiBaseInfo.getDiscounts().size()<=DEFALUT_SHOW){
            viewHolder.ivDiscount.setVisibility(View.GONE);
        }
        if (mOpenPoiBaseInfo.isOpenDiscount()){
            mBusDiscountAdpater.setData(mOpenPoiBaseInfo.getDiscounts());
            viewHolder.ivDiscount.setImageResource(R.mipmap.arrow_up);
        }else{
            mBusDiscountAdpater.setData(mOpenPoiBaseInfo.getDiscounts().subList(0,DEFALUT_SHOW));
            viewHolder.ivDiscount.setImageResource(R.mipmap.arrow_down);
        }
            
      
        viewHolder.rvDiscount.setAdapter(mBusDiscountAdpater);
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
        viewHolder.rlDiscount.setTag(position);
        viewHolder.rlDiscount.setOnClickListener(this);
        viewHolder.rl.setOnClickListener(this);
        
        return convertView;
    }
 
    

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl:
                ViewHolder viewHolder =(ViewHolder)v.getTag();
                int position =Integer.parseInt(viewHolder.tvBusinessIndex.getText().toString())-1;
                Lg.getInstance().d(TAG,"position:"+position);
                if (mOnItemClickListener!=null) {
                    mOnItemClickListener.onItemClick(position);
                }
                break;
            case R.id.rl_discount:
                Lg.getInstance().d(TAG," v:"+ v.getTag());
                int index =(int)v.getTag();
                if (!mData.get(index).getDiscounts().isEmpty()||mData.get(index).getDiscounts().size()>DEFALUT_SHOW) {
                    if (mData.get(index).isOpenDiscount()) {
                        mData.get(index).setOpenDiscount(false);
                    } else {
                        mData.get(index).setOpenDiscount(true);
                    }
                    notifyDataSetChanged();
                }
                break;
                       

        }
       
    }


    public interface OnBusinessItemClickListener {
        void onItemClick(int itemPosition);
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
        private TextView tvBusinessIndex;
        private NoClikRecyclerView rvDiscount;
        private RelativeLayout rlDiscount;
        private ImageView ivDiscount;
    }
}

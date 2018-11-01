package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.view.RatingBar;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderListAdaper extends RecyclerView.Adapter<OrderListAdaper.ViewHolder> {

    private List<OrderListResponse.IovBean.DataBean> mOrderList;
    private Context mContext;

    public OrderListAdaper(List<OrderListResponse.IovBean.DataBean> orderList,
                           Context context) {
        mOrderList = orderList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .layout_order_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        OrderListResponse.IovBean.DataBean order = mOrderList.get
                (position);

        viewHolder.tvIndex.setText(((position + 1) + ""));
        viewHolder.tvStoreName.setText(order.getOrder_name());
        viewHolder.tvFood.setText(order.getOut_trade_status());
        viewHolder.tvPrice.setText(order.getOrder_amount());

        viewHolder.tvFoodNum.setText(String.format(mContext.getResources().getString(R.string
                .order_amount), order.getOrder_amount()));
        viewHolder.tvOrderStatus.setText(order.getOrder_status());
        viewHolder.tvOrderTime.setText(order.getOrder_time());
        viewHolder.tvOneMore.setText(mContext.getResources().getString(R.string.one_more_order));
        //Glide.with(mContext).load(order.getPic_url()).into(viewHolder.ivStore);

        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvIndex;
        private ImageView ivStore;
        private LinearLayout llOrderInfo;
        private AppCompatTextView tvStoreName;
        private AppCompatTextView tvFood;
        private AppCompatTextView tvPrice;
        private AppCompatTextView tvFoodNum;
        private AppCompatTextView tvOrderTime;
        private AppCompatTextView tvOrderStatus;
        private AppCompatTextView tvOneMore;

        private ViewHolder(View view) {
            super(view);

            tvIndex = (AppCompatTextView) view.findViewById(R.id.index);
            ivStore = (ImageView) view.findViewById(R.id.iv_store);
            llOrderInfo = (LinearLayout) view.findViewById(R.id.ll_order_info);
            tvStoreName = (AppCompatTextView) view.findViewById(R.id.tv_store_name);
            tvFood = (AppCompatTextView) view.findViewById(R.id.tv_food);
            tvPrice = (AppCompatTextView) view.findViewById(R.id.tv_price);
            tvFoodNum = (AppCompatTextView) view.findViewById(R.id.tv_food_num);
            tvOrderTime = (AppCompatTextView) view.findViewById(R.id.tv_order_time);
            tvOrderStatus = (AppCompatTextView) view.findViewById(R.id.tv_order_status);
            tvOneMore = (AppCompatTextView) view.findViewById(R.id.one_more_order);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }
}
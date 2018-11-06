package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.iov.dueros.waimai.net.entity.response.OrderListResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderListExtraBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.faceos.client.GsonUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class OrderListAdaper extends RecyclerView.Adapter<OrderListAdaper.ViewHolder> implements View.OnClickListener {

    private List<OrderListResponse.IovBean.DataBean> mOrderList;
    private Context mContext;
    String user_phone;
    String pay_status;
    String food_name;
    String wm_pic_url;
    int total_price;
    int food_num;
    double food_price;
    OrderListExtraBean extraBean;
    private OrderListExtraBean.OrderInfos.Food_list mOrderInfosfood_list;

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

        pay_status = order.getOrder_status();
        switch (pay_status) {
            case "1":
                pay_status = mContext.getResources().getString(R.string.paid);
                break;
            case "2":
                pay_status = mContext.getResources().getString(R.string.to_be_paid);
                break;
            case "3":
                pay_status = mContext.getResources().getString(R.string.completed);
            default:
                break;
        }
        viewHolder.tvIndex.setText(((position + 1) + ""));
        viewHolder.tvStoreName.setText(order.getOrder_name());
        viewHolder.tvOrderStatus.setText(pay_status);
        viewHolder.tvOrderTime.setText(order.getOrder_time());
        viewHolder.tvCancelOrder.setText(mContext.getResources().getString(R.string.order_cancel));
        viewHolder.tvOneMore.setText(mContext.getResources().getString(R.string.one_more_order));
        String extra = order.getExtra();
        extraBean = GsonUtil.fromJson(extra, OrderListExtraBean.class);
        user_phone = extraBean.getPayload().getUser_phone();
        int total_count = 0;
        for (int i = 0; i < extraBean.getOrderInfos().getFood_list().size(); i++) {
            total_count = total_count + extraBean.getOrderInfos().getFood_list().get(i).getCount();
        }
        mOrderInfosfood_list = extraBean.getOrderInfos().getFood_list().get(0);
        food_name = mOrderInfosfood_list.getName();
        food_num = mOrderInfosfood_list.getCount();
        total_price = extraBean.getOrderInfos().getGoods_total_price();
        wm_pic_url = extraBean.getOrderInfos().getWm_pic_url();

        viewHolder.tvFood.setText(food_name);
        viewHolder.tvFoodNum.setText("x" + String.valueOf(food_num));
        viewHolder.tvPrice.setText(String.valueOf(total_price));
        viewHolder.tvTotalCount.setText(String.format(mContext.getResources().getString(R.string
                .goods_total_count), total_count));
        Glide.with(mContext).load(wm_pic_url).into(viewHolder.ivStore);

        viewHolder.itemView.setTag(position);
        viewHolder.tvStoreName.setTag(position);
        viewHolder.ivClick.setTag(position);
        viewHolder.tvOneMore.setTag(position);
        viewHolder.tvCancelOrder.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvIndex;
        private ImageView ivStore;
        private ImageView ivClick;
        private LinearLayout llOrderInfo;
        private AppCompatTextView tvStoreName;
        private AppCompatTextView tvFood;
        private AppCompatTextView tvPrice;
        private AppCompatTextView tvFoodNum;
        private AppCompatTextView tvTotalCount;
        private AppCompatTextView tvOrderTime;
        private AppCompatTextView tvOrderStatus;
        private AppCompatTextView tvOneMore;
        private AppCompatTextView tvCancelOrder;

        private ViewHolder(View view) {
            super(view);

            tvIndex = (AppCompatTextView) view.findViewById(R.id.index);
            ivStore = (ImageView) view.findViewById(R.id.iv_store);
            llOrderInfo = (LinearLayout) view.findViewById(R.id.ll_order_info);
            tvStoreName = (AppCompatTextView) view.findViewById(R.id.tv_store_name);
            ivClick = (ImageView) view.findViewById(R.id.iv_click);
            tvFood = (AppCompatTextView) view.findViewById(R.id.tv_food);
            tvPrice = (AppCompatTextView) view.findViewById(R.id.tv_price);
            tvFoodNum = (AppCompatTextView) view.findViewById(R.id.tv_food_num);
            tvTotalCount = (AppCompatTextView) view.findViewById(R.id.tv_total_count);
            tvOrderTime = (AppCompatTextView) view.findViewById(R.id.tv_order_time);
            tvCancelOrder = (AppCompatTextView) view.findViewById(R.id.cancel_order);
            tvOrderStatus = (AppCompatTextView) view.findViewById(R.id.tv_order_status);
            tvOneMore = (AppCompatTextView) view.findViewById(R.id.one_more_order);
            view.setOnClickListener(OrderListAdaper.this);
            tvStoreName.setOnClickListener(OrderListAdaper.this);
            tvOneMore.setOnClickListener(OrderListAdaper.this);
            ivClick.setOnClickListener(OrderListAdaper.this);
            tvCancelOrder.setOnClickListener(OrderListAdaper.this);
        }
    }


    public enum ViewName {
        ITEM,
        STORENAME,
        ONEMORE,
        ORDERCANCLE
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewName viewname, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.tv_store_name:
                case R.id.iv_click:
                    mOnItemClickListener.onItemClick(v, ViewName.STORENAME, position);
                    break;
                case R.id.one_more_order:
                    mOnItemClickListener.onItemClick(v, ViewName.ONEMORE, position);
                    break;
                case R.id.cancel_order:
                    mOnItemClickListener.onItemClick(v, ViewName.ORDERCANCLE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }
}
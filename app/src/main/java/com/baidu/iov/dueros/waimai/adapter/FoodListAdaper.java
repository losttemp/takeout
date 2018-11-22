package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class FoodListAdaper extends RecyclerView.Adapter<FoodListAdaper.ViewHolder> {
    private List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> mData;
    private Context mContext;

    public FoodListAdaper(Context context) {
        mContext = context;
    }

    public void setData(List<OrderDetailsResponse.MeituanBean.DataBean.FoodListBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public FoodListAdaper.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = View.inflate(mContext, R.layout.item_order_details, null);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvfoodname.setText(mData.get(position).getName());

        NumberFormat numberFormat = new DecimalFormat("#.#");
        double price = mData.get(position).getPrice();
        holder.tvfoodprice.setText(String.format(mContext.getResources().getString(R.string.cost_text), numberFormat.format(price)));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvfoodname;
        private TextView tvfoodprice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvfoodname = itemView.findViewById(R.id.tv_food_name);
            tvfoodprice = itemView.findViewById(R.id.tv_food_price);
        }
    }

}

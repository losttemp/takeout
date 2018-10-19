package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.view.RatingBar;
import com.bumptech.glide.Glide;

import java.util.List;

public class StoreAdaper extends RecyclerView.Adapter<StoreAdaper.ViewHolder> {

	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList;
	private Context mContext;

	public StoreAdaper(List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> storeList,
					   Context context) {
		mStoreList = storeList;
		mContext = context;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.layout_store_item, viewGroup, false);
		final ViewHolder holder = new ViewHolder(view);

		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean store = mStoreList.get
				(position);

		viewHolder.tvStoreName.setText(store.getName());
		viewHolder.tvScore.setText(store.getWm_poi_score() + "");
		viewHolder.tvDistance.setText(store.getDistance());
		viewHolder.tvAveragePrice.setText(store.getAverage_price_tip());
		viewHolder.tvStatusDesc.setText(store.getStatus_desc());
		viewHolder.tvSales.setText(String.format(mContext.getResources().getString(R.string
				.month_sale_num), store.getMonth_sale_num()));
		viewHolder.tvTime.setText(String.format(mContext.getResources().getString(R.string
				.delivery_time), store.getAvg_delivery_time()));
		viewHolder.tvMinPrice.setText(String.format(mContext.getResources().getString(R.string
				.min_price), store.getMin_price()));
		viewHolder.tvExpressPrice.setText(String.format(mContext.getResources().getString(R.string
				.shipping_fee), store.getShipping_fee()));

		viewHolder.ratingBar.setClickable(false);
		viewHolder.ratingBar.setStar((float) store.getWm_poi_score());

		Glide.with(mContext).load(store.getPic_url()).into(viewHolder.ivStore);

	}

	@Override
	public int getItemCount() {
		return mStoreList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private AppCompatImageView ivStore;
		private LinearLayout llStoreInfo;
		private AppCompatTextView tvStoreName;
		private AppCompatTextView tvStatusDesc;
		private RatingBar ratingBar;
		private AppCompatTextView tvScore;
		private AppCompatTextView tvSales;
		private AppCompatTextView tvTime;
		private AppCompatTextView tvDistance;
		private AppCompatTextView tvMinPrice;
		private AppCompatTextView tvExpressPrice;
		private AppCompatTextView tvAveragePrice;

		private ViewHolder(View view) {
			super(view);

			ivStore = (AppCompatImageView) view.findViewById(R.id.iv_store);
			llStoreInfo = (LinearLayout) view.findViewById(R.id.ll_store_info);
			tvStoreName = (AppCompatTextView) view.findViewById(R.id.tv_store_name);
			tvStatusDesc = (AppCompatTextView) view.findViewById(R.id.tv_status_desc);
			ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
			tvScore = (AppCompatTextView) view.findViewById(R.id.tv_score);
			tvSales = (AppCompatTextView) view.findViewById(R.id.tv_sales);
			tvTime = (AppCompatTextView) view.findViewById(R.id.tv_time);
			tvDistance = (AppCompatTextView) view.findViewById(R.id.tv_distance);
			tvMinPrice = (AppCompatTextView) view.findViewById(R.id.tv_min_price);
			tvExpressPrice = (AppCompatTextView) view.findViewById(R.id.tv_express_price);
			tvAveragePrice = (AppCompatTextView) view.findViewById(R.id.tv_average_price);
		}
	}
}
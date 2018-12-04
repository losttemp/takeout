package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdaper extends RecyclerView.Adapter<ProductAdaper.ViewHolder> {

	private Context mContext;

	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean.ProductListBean> mData;

	public ProductAdaper(Context context,List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean.ProductListBean> list) {
		mData = list;
		mContext=context;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.item_store_product, viewGroup, false);
		final ViewHolder holder = new ViewHolder(view);

		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.tvProductName.setText(mData.get(position).getName());
		viewHolder.tvProductPrice.setText("¥"+mData.get(position).getPrice());
		Glide.with(mContext).load(mData.get(position).getPicture()).into(viewHolder.ivProduct);
	}

	@Override
	public int getItemCount() {
		return mData==null?0:mData.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView ivProduct;
		private TextView tvProductName;
		private TextView tvProductPrice;

		private ViewHolder(View view) {
			super(view);
			tvProductName =  view.findViewById(R.id.tv_product_name);
			tvProductPrice =  view.findViewById(R.id.tv_product_price);
			ivProduct =  view.findViewById(R.id.iv_product);
		}
	}

}
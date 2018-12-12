package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class DiscountAdaper extends RecyclerView.Adapter<DiscountAdaper.ViewHolder> {

	private List<String> mData;
	private Context mContext;

	public DiscountAdaper(Context context,List<String> list) {
		mContext=context;
		mData = list;
	}



	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.item_store_discount, viewGroup, false);
		final ViewHolder holder = new ViewHolder(view);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick();
				}
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.tvText.setText(mData.get(position));
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tvText;

		private ViewHolder(View view) {
			super(view);

			tvText = (TextView) view.findViewById(R.id.tv_text);
		}
	}
	public interface OnItemClickListener {
		void onItemClick();
	}

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}
	private OnItemClickListener mItemClickListener;
}
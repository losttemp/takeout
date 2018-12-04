package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean.DiscountsBean;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.view.FlowLayoutManager;
import com.baidu.iov.dueros.waimai.view.RatingBar;
import com.bumptech.glide.Glide;
import com.domain.multipltextview.MultiplTextView;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public class StoreAdaper extends RecyclerView.Adapter<StoreAdaper.ViewHolder> {

	private  int DISCOUNT_LINE_HEIGHT =22;

	private int space =5;

	private int mFromPageType;

	private List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> mStoreList;
	private Context mContext;
	private OnItemClickListener mItemClickListener;

	public StoreAdaper(List<StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean> storeList,
					   Context context,int mFromPageType) {
		mStoreList = storeList;
		mContext = context;
		DISCOUNT_LINE_HEIGHT =mContext.getResources().getDimensionPixelSize(R.dimen.px60dp);
		space=mContext.getResources().getDimensionPixelSize(R.dimen.px6dp);
		this.mFromPageType=mFromPageType;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
				.layout_store_item, viewGroup, false);
		final ViewHolder holder = new ViewHolder(view);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick((Integer) v.getTag());
				}
			}
		});

		return holder;
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
		final StoreResponse.MeituanBean.DataBean.OpenPoiBaseInfoListBean store = mStoreList.get
				(position);

		viewHolder.tvStoreName.setText(store.getName());
		viewHolder.tvScore.setText(String.valueOf(store.getWm_poi_score()));
		viewHolder.tvDistance.setText(store.getDistance());
		viewHolder.tvAveragePrice.setText(store.getAverage_price_tip());
		viewHolder.tvSales.setText(String.format(mContext.getResources().getString(R.string
				.month_sale_num), store.getMonth_sale_num()));
		viewHolder.tvTime.setText(String.format(mContext.getResources().getString(R.string
				.delivery_time), store.getAvg_delivery_time()));
		viewHolder.tvMinPrice.setText(String.format(mContext.getResources().getString(R.string
				.min_price_s), NumberFormat.getInstance().format(store.getMin_price())));
		viewHolder.tvExpressPrice.setText(String.format(mContext.getResources().getString(R.string
				.shipping_fee_s), NumberFormat.getInstance().format(store.getShipping_fee())));
		viewHolder.tvStoreIndex.setText(String.valueOf(position + 1));
		Glide.with(mContext).load(store.getPic_url()).into(viewHolder.ivStore);

		viewHolder.ratingBar.setClickable(false);
		viewHolder.ratingBar.setStar((float) store.getWm_poi_score());
		
		
		String averagePrice = store.getAverage_price_tip();
		if (!TextUtils.isEmpty(averagePrice)) {
			viewHolder.tvAveragePrice.setText(String.format(mContext.getResources().
					getString(R.string.average_price), averagePrice));
		} else {
			viewHolder.tvAveragePrice.setText("");
		}

		//status
		final int status = store.getStatus();
		if (status == Constant.STROE_STATUS_BREAK) {
			viewHolder.tvStatusDesc.setText(mContext.getResources().getString(R.string
					.store_status_break));
			viewHolder.tvStatusDesc.setVisibility(View.VISIBLE);
			viewHolder.rl.setBackgroundColor(mContext.getResources().getColor(R.color.gray_bg));
		} else if (status == Constant.STROE_STATUS_BUSY) {
			viewHolder.tvStatusDesc.setText(mContext.getResources().getString(R.string
					.store_status_busy));
			viewHolder.tvStatusDesc.setVisibility(View.VISIBLE);
			viewHolder.rl.setBackground(null);
		} else {
			viewHolder.tvStatusDesc.setVisibility(View.GONE);
			viewHolder.rl.setBackground(null);
		}
		
        if (mFromPageType==Constant.STORE_FRAGMENT_FROM_SEARCH){
			LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
			mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
			viewHolder.rvStoreProduct.setLayoutManager(mLinearLayoutManager);
			final  ProductAdaper mProductAdaper = new ProductAdaper(mContext,store.getProduct_list());
			viewHolder.rvStoreProduct.setAdapter(mProductAdaper);
			viewHolder.rvStoreProduct.setVisibility(View.VISIBLE);
		}else{
			viewHolder.rvStoreProduct.setVisibility(View.GONE);
		}
			
        
	

		//Discounts
		List<String> discounts = getDiscountList(store.getDiscounts());
		final FlowLayoutManager mFlowLayoutManager = new FlowLayoutManager();
		if (discounts == null || discounts.size() == 0) {
			viewHolder.rlDiscount.setVisibility(View.GONE);
		} else {
			if (viewHolder.rvStoreDiscount.getItemDecorationCount() == 0) {
				viewHolder.rvStoreDiscount.addItemDecoration(new SpaceItemDecoration(space));
			}
			viewHolder.rvStoreDiscount.setLayoutManager(mFlowLayoutManager);
			DiscountAdaper discountAdaper = new DiscountAdaper(discounts);
			viewHolder.rvStoreDiscount.setAdapter(discountAdaper);
			viewHolder.rlDiscount.setVisibility(View.VISIBLE);

			viewHolder.itemView.getViewTreeObserver().addOnGlobalLayoutListener(new
																						ViewTreeObserver
					.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					int lines = mFlowLayoutManager.getLineRows();
					if (lines > 1) {
						viewHolder.ivStoreDiscount.setVisibility(View.VISIBLE);
					} else {
						viewHolder.ivStoreDiscount.setVisibility(View.GONE);
					}

					if (store.isDiscountsDown()) {
						viewHolder.ivStoreDiscount.setImageResource(R.drawable.arrow_up);
						setRecyclerViewHight(viewHolder.rvStoreDiscount, DISCOUNT_LINE_HEIGHT *
								lines);
					} else {
						viewHolder.ivStoreDiscount.setImageResource(R.drawable.arrow_down);
						setRecyclerViewHight(viewHolder.rvStoreDiscount, DISCOUNT_LINE_HEIGHT);
					}

					viewHolder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				}
			});

			viewHolder.ivStoreDiscount.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (store.isDiscountsDown()) {
						viewHolder.ivStoreDiscount.setImageResource(R.drawable.arrow_down);
						setRecyclerViewHight(viewHolder.rvStoreDiscount, DISCOUNT_LINE_HEIGHT);
						store.setDiscountsDown(false);
					} else {
						int lines = mFlowLayoutManager.getLineRows();
						if (lines > 1) {
							viewHolder.ivStoreDiscount.setImageResource(R.drawable.arrow_up);
							store.setDiscountsDown(true);
							setRecyclerViewHight(viewHolder.rvStoreDiscount, DISCOUNT_LINE_HEIGHT
									* lines);
						} else {
							setRecyclerViewHight(viewHolder.rvStoreDiscount, DISCOUNT_LINE_HEIGHT);
						}
					}
				}
			});

		}

		viewHolder.itemView.setTag(position);

	}

	@Override
	public int getItemCount() {
		return mStoreList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		private AppCompatImageView ivStore;
		private MultiplTextView tvStoreName;
		private TextView tvStatusDesc;
		private RatingBar ratingBar;
		private MultiplTextView tvScore;
		private MultiplTextView tvSales;
		private MultiplTextView tvTime;
		private MultiplTextView tvDistance;
		private MultiplTextView tvMinPrice;
		private MultiplTextView tvExpressPrice;
		private MultiplTextView tvAveragePrice;
		private TextView tvStoreIndex;
		private RelativeLayout rlDiscount;
		private RecyclerView rvStoreDiscount;
		private AppCompatImageView ivStoreDiscount;
		private RelativeLayout rl;
		private RecyclerView rvStoreProduct;

		private ViewHolder(View view) {
			super(view);

			ivStore =  view.findViewById(R.id.iv_store);
			tvStoreName =  view.findViewById(R.id.tv_store_name);
			tvStatusDesc =view.findViewById(R.id.tv_status_desc);
			ratingBar = view.findViewById(R.id.ratingBar);
			tvScore =  view.findViewById(R.id.tv_score);
			tvSales =  view.findViewById(R.id.tv_sales);
			tvTime =  view.findViewById(R.id.tv_time);
			tvDistance =  view.findViewById(R.id.tv_distance);
			tvMinPrice = view.findViewById(R.id.tv_min_price);
			tvExpressPrice =  view.findViewById(R.id.tv_express_price);
			tvAveragePrice =  view.findViewById(R.id.tv_average_price);
			tvStoreIndex =  view.findViewById(R.id.tv_store_index);
			rlDiscount =  view.findViewById(R.id.rl_discount);
			rvStoreDiscount = view.findViewById(R.id.rv_store_discount);
			ivStoreDiscount =  view.findViewById(R.id.iv_store_discount);
			rl=  view.findViewById(R.id.rl);
			rvStoreProduct=  view.findViewById(R.id.rv_store_product);
		}
	}

	public interface OnItemClickListener {
		void onItemClick(int position);
	}

	public void setItemClickListener(OnItemClickListener itemClickListener) {
		mItemClickListener = itemClickListener;
	}

	class SpaceItemDecoration extends RecyclerView.ItemDecoration {
		private int space;

		public SpaceItemDecoration(int space) {
			this.space = space;
		}

		@Override
		public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull
				RecyclerView parent, @NonNull RecyclerView.State state) {
			outRect.top = 0;
			outRect.left = 0;
			outRect.right = space;
			outRect.bottom = space;
		}
	}

	private void setRecyclerViewHight(RecyclerView rvStoreDiscount, int height) {
		ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) rvStoreDiscount.getLayoutParams();
		params.height = height;
		rvStoreDiscount.setLayoutParams(params);

	}

	private List<String> getDiscountList(List<DiscountsBean> discounts) {
		List<String> list = new ArrayList<>();
		for (DiscountsBean bean : discounts) {
			String[] name = bean.getInfo().split(";");
			list.addAll(Arrays.asList(name));
		}

		return list;
	}
}
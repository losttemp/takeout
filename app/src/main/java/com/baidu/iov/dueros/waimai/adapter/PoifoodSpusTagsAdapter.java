package com.baidu.iov.dueros.waimai.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.PoifoodSpusTagsBean;

import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class PoifoodSpusTagsAdapter extends RecyclerView.Adapter<PoifoodSpusTagsAdapter.ViewHolder> {

    private final Context context;
    private List<PoifoodSpusTagsBean> poifoodSpusTagsBeans;
    private OnItemClickListener mItemClickListener;
    private int selectedPosition;

    public PoifoodSpusTagsAdapter(Context context, List<PoifoodSpusTagsBean> poifoodSpusTagsBeans) {
        this.context = context;
        this.poifoodSpusTagsBeans = poifoodSpusTagsBeans;
    }

    @NonNull
    @Override
    public PoifoodSpusTagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorize_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PoifoodSpusTagsAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.foodSpuTagsBeanName.setText(poifoodSpusTagsBeans.get(position).getFoodSpuTagsBeanName());
        if (poifoodSpusTagsBeans.get(position).getNumber() != 0) {
            viewHolder.number.setVisibility(View.VISIBLE);
            viewHolder.number.setText("" + poifoodSpusTagsBeans.get(position).getNumber());
        } else {
            viewHolder.number.setVisibility(View.GONE);
        }
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(view, position);
                }
            }
        });

        viewHolder.view.setContentDescription(String.format(context.getString(R.string.to_eat_something), poifoodSpusTagsBeans.get(position).getFoodSpuTagsBeanName()));
        viewHolder.view.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(host, position);
                }
                return true;
            }
        });

        if (position == selectedPosition) {
            viewHolder.foodSpuTagsBeanName.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.view.setBackgroundColor(context.getResources().getColor(R.color.refresh_layout));
        } else {
            viewHolder.foodSpuTagsBeanName.setTextColor(context.getResources().getColor(R.color.white_60));
            viewHolder.view.setBackgroundColor(context.getResources().getColor(R.color.zuobianlan_bg));
        }
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return poifoodSpusTagsBeans == null ? 0 : poifoodSpusTagsBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView number;
        TextView foodSpuTagsBeanName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            foodSpuTagsBeanName = (TextView) itemView.findViewById(R.id.mainitem_txt);
            number = (TextView) itemView.findViewById(R.id.tv_num);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

}

package com.baidu.iov.dueros.waimai.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;

import java.util.List;

/**
 * Created by ubuntu on 18-11-19.
 */

public class SpecificationAdapter extends RecyclerView.Adapter<SpecificationAdapter.SpecificationAdapterViewHolder> {

    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> valuesBeans;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs;
    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus;
    private OnItemClickListener mItemClickListerner;

    public SpecificationAdapter(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs,
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus,
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> valuesBeans,
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans) {
        this.valuesBeans = valuesBeans;
        this.skusBeans = skusBeans;
        this.choiceAttrs = choiceAttrs;
        this.choiceSkus = choiceSkus;
    }

    @NonNull
    @Override
    public SpecificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .item_specification, viewGroup, false);
        SpecificationAdapterViewHolder holder = new SpecificationAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificationAdapterViewHolder holder, final int position) {
        if (valuesBeans != null) {
            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean valuesBean = valuesBeans.get(position);
            holder.text.setText(valuesBean.getValue().trim());
        } else {
            String spec = skusBeans.get(position).getSpec();
            holder.text.setText(spec.trim());
        }
        if (choiceAttrs != null && choiceAttrs.size() > 0) {
            String value = choiceAttrs.get(0).getValue();
            if (value.equals(holder.text.getText())){
                holder.item.setBackgroundResource(R.drawable.shape_filter_selected_bg);
            }
        }
        if (choiceSkus!=null&& choiceSkus.size() > 0){
            String value = choiceSkus.get(0).getSpec();
            if (value.equals(holder.text.getText())){
                holder.item.setBackgroundResource(R.drawable.shape_filter_selected_bg);
            }
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListerner != null) {
                    mItemClickListerner.OnItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (valuesBeans != null) {
            return valuesBeans.size();
        } else {
            return skusBeans.size();
        }
    }

    public class SpecificationAdapterViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView text;

        public SpecificationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView;
            text = (TextView) itemView.findViewById(R.id.textItem);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int porison);
    }

    public void setOnItemClickListerner(OnItemClickListener mItemClickListerner) {
        this.mItemClickListerner = mItemClickListerner;
    }
}

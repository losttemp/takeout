package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter {

    private IShoppingCartToDetailListener shopToDetailListener;

    public void setShopToDetailListener(IShoppingCartToDetailListener callBackListener) {
        this.shopToDetailListener = callBackListener;
    }

    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans;
    private LayoutInflater mInflater;

    public ShoppingCartAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans) {
        this.spusBeans = spusBeans;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return spusBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return spusBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.trade_widget, null);
            viewHolder = new ViewHolder();
            viewHolder.commodityName = (TextView) convertView.findViewById(R.id.commodityName);
            viewHolder.commodityPrise = (TextView) convertView.findViewById(R.id.commodityPrise);
            viewHolder.commodityNum = (TextView) convertView.findViewById(R.id.commodityNum);
            viewHolder.increase = (TextView) convertView.findViewById(R.id.increase);
            viewHolder.reduce = (TextView) convertView.findViewById(R.id.reduce);
            viewHolder.shoppingNum = (TextView) convertView.findViewById(R.id.shoppingNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.commodityName.setText(spusBeans.get(position).getName());
        viewHolder.commodityPrise.setText("" + spusBeans.get(position).getMin_price());
        viewHolder.commodityNum.setText(1 + "");
        viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");

        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = spusBeans.get(position).getNumber();
                int min_order_count = 1;
                if (spusBeans.get(position).getSkus().size() > 1) {
                    min_order_count = spusBeans.get(position).getChoiceSkus().get(0).getMin_order_count();
                } else {
                    min_order_count = spusBeans.get(position).getSkus().get(0).getMin_order_count();
                }
                num += min_order_count;
                spusBeans.get(position).setNumber(num);
                viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                if (shopToDetailListener != null) {
                    shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag());
                } else {
                }
            }
        });

        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = spusBeans.get(position).getNumber();
                if (num > 0) {
                    num--;
                    if (num == 0) {
                        spusBeans.get(position).setNumber(num);
                        shopToDetailListener.onRemovePriduct(spusBeans.get(position));
                    } else {
                        spusBeans.get(position).setNumber(num);
                        viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                        if (shopToDetailListener != null) {
                            shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag());
                        } else {
                        }
                    }

                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView commodityName;
        public TextView commodityPrise;
        public TextView commodityNum;
        public TextView increase;
        public TextView reduce;
        public TextView shoppingNum;
    }
}

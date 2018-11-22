package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.R;
import com.domain.multipltextview.MultiplTextView;

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
            viewHolder.commodityName = (MultiplTextView) convertView.findViewById(R.id.commodityName);
            viewHolder.commodityPrise = (MultiplTextView) convertView.findViewById(R.id.commodityPrise);
            viewHolder.increase = (ImageView) convertView.findViewById(R.id.increase);
            viewHolder.reduce = (ImageView) convertView.findViewById(R.id.reduce);
            viewHolder.shoppingNum = (MultiplTextView) convertView.findViewById(R.id.shoppingNum);
            viewHolder.shopSpecifications = (MultiplTextView) convertView.findViewById(R.id.tv_shop_specifications);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.commodityName.setText(spusBeans.get(position).getName());
        viewHolder.commodityPrise.setText("" + spusBeans.get(position).getMin_price());
        viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < spusBeans.get(position).getAttrs().size(); i++) {
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs =
                    spusBeans.get(position).getAttrs().get(i).getChoiceAttrs();
            String value = choiceAttrs.get(0).getValue();
            if (i == spusBeans.get(position).getAttrs().size() - 1) {
                stringBuffer.append(value);
            } else {
                stringBuffer.append(value + "+");
            }
        }
        if (spusBeans.get(position).getSkus().size() <= 1) {
        } else {
            if (spusBeans.get(position).getChoiceSkus() != null) {
                String spec = spusBeans.get(position).getChoiceSkus().get(0).getSpec();
                stringBuffer.append("+" + spec);
            }
        }
        if (stringBuffer.length() > 0) {
            viewHolder.shopSpecifications.setVisibility(View.VISIBLE);
            viewHolder.shopSpecifications.setText(stringBuffer);
        } else {
            viewHolder.shopSpecifications.setVisibility(View.GONE);
        }

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
                    shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                            spusBeans.get(position).getSection(), true);
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
                    spusBeans.get(position).setNumber(num);
                    viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                    if (shopToDetailListener != null) {
                        shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                                spusBeans.get(position).getSection(), false);
                    } else {
                    }
                    if (num == 0) {
                        shopToDetailListener.onRemovePriduct(spusBeans.get(position));
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        public MultiplTextView commodityName;
        public MultiplTextView commodityPrise;
        public ImageView increase;
        public ImageView reduce;
        public MultiplTextView shoppingNum;
        public MultiplTextView shopSpecifications;
    }
}

package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.domain.multipltextview.MultiplTextView;

import java.text.NumberFormat;
import java.util.List;

public class ShoppingCartAdapter extends BaseAdapter {

    private final Context context;
    private IShoppingCartToDetailListener shopToDetailListener;

    public void setShopToDetailListener(IShoppingCartToDetailListener callBackListener) {
        this.shopToDetailListener = callBackListener;
    }

    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans;
    private LayoutInflater mInflater;

    public ShoppingCartAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans) {
        this.spusBeans = spusBeans;
        this.context = context;
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
            viewHolder.increase = (ImageView) convertView.findViewById(R.id.increase);
            viewHolder.reduce = (ImageView) convertView.findViewById(R.id.reduce);
            viewHolder.shoppingNum = (MultiplTextView) convertView.findViewById(R.id.shoppingNum);
            viewHolder.shopSpecifications = (TextView) convertView.findViewById(R.id.tv_shop_specifications);
            viewHolder.shopDiscountPrice = (TextView) convertView.findViewById(R.id.tv_discount_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setContentDescription(String.format(context.getString(R.string.cancel_position),String.valueOf(position+1)));
        convertView.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                StandardCmdClient.getInstance().playTTS(context,context.getString(R.string.already_cancel)+spusBeans.get(position).getName());
                spusBeans.get(position).setNumber(0);
                if (shopToDetailListener != null && spusBeans.size() > 0) {
                    shopToDetailListener.onRemovePriduct(spusBeans.get(position),spusBeans.get(position).getTag(),
                            spusBeans.get(position).getSection(), false);
                }
                return true;
            }
        });
        viewHolder.commodityName.setText(spusBeans.get(position).getName());
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBeans.get(position).getSkus();
        if (skus != null) {
            if (skus.size() == 0) {
                viewHolder.commodityPrise.setText(NumberFormat.getInstance().format(spusBeans.get(position).getMin_price()));
                viewHolder.shopDiscountPrice.setVisibility(View.GONE);
            } else if (skus.size() == 1) {
                viewHolder.commodityPrise.setText(NumberFormat.getInstance().format(spusBeans.get(position).getSkus().get(0).getPrice()));
                double origin_price = spusBeans.get(position).getSkus().get(0).getOrigin_price();
                double price = spusBeans.get(position).getSkus().get(0).getPrice();
                if (origin_price > price) {
                    viewHolder.shopDiscountPrice.setVisibility(View.VISIBLE);
                    viewHolder.shopDiscountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getSkus().get(0).getOrigin_price())));
                    viewHolder.shopDiscountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.shopDiscountPrice.getPaint().setAntiAlias(true); //去掉锯齿
                } else {
                    viewHolder.shopDiscountPrice.setVisibility(View.GONE);
                }
            } else if (skus.size() > 1) {
                viewHolder.commodityPrise.setText(NumberFormat.getInstance().format(spusBeans.get(position).getChoiceSkus().get(0).getPrice()));
                double origin_price = spusBeans.get(position).getSkus().get(0).getOrigin_price();
                double price = spusBeans.get(position).getSkus().get(0).getPrice();
                if (origin_price > price) {
                    viewHolder.shopDiscountPrice.setVisibility(View.VISIBLE);
                    viewHolder.shopDiscountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getChoiceSkus().get(0).getOrigin_price())));
                    viewHolder.shopDiscountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.shopDiscountPrice.getPaint().setAntiAlias(true); //去掉锯齿
                } else {
                    viewHolder.shopDiscountPrice.setVisibility(View.GONE);
                }
            }
        } else {
            viewHolder.commodityPrise.setText(NumberFormat.getInstance().format(spusBeans.get(position).getMin_price()));
        }

        viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");

        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < spusBeans.get(position).getAttrs().size(); i++) {
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs =
                    spusBeans.get(position).getAttrs().get(i).getChoiceAttrs();
            String value = "";
            if (choiceAttrs != null) {
                value = choiceAttrs.get(0).getValue();
            }
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
                num ++;
                spusBeans.get(position).setNumber(num);
                viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                if (shopToDetailListener != null) {
                    shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                            spusBeans.get(position).getSection(), true, false);
                } else {
                }
            }
        });

        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = spusBeans.get(position).getNumber();
                int minOrderCount = getMinOrderCount(spusBeans.get(position));
                boolean isMinOrderCount = false;
                if (num > 0) {
                    if (minOrderCount == num) {
                        num -= minOrderCount;
                        isMinOrderCount = true;
                    } else {
                        num--;
                        isMinOrderCount =false;
                    }
                    spusBeans.get(position).setNumber(num);
                    viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                    if (shopToDetailListener != null) {
                        shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                                spusBeans.get(position).getSection(), false, isMinOrderCount);
                    } else {
                    }
                    if (num == 0) {
                        shopToDetailListener.onRemovePriduct(spusBeans.get(position),spusBeans.get(position).getTag(),
                                spusBeans.get(position).getSection(), false);
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView commodityName;
        public TextView commodityPrise;
        public ImageView increase;
        public ImageView reduce;
        public MultiplTextView shoppingNum;
        public TextView shopSpecifications;
        public TextView shopDiscountPrice;
    }

    private int getMinOrderCount(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
        int min_order_count = 1;
        if (spusBean.getSkus() != null) {
            if (spusBean.getSkus().size() > 1 && spusBean.getChoiceSkus() != null) {
                min_order_count = spusBean.getChoiceSkus().get(0).getMin_order_count();
            } else {
                min_order_count = spusBean.getSkus().get(0).getMin_order_count();
            }
        }
        return min_order_count;
    }
}

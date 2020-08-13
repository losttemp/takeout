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
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DoubleUtil;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
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

        convertView.setContentDescription(String.format(context.getString(R.string.cancel_position), String.valueOf(position + 1)));
        convertView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public boolean performAccessibilityAction(View host, int action, Bundle args) {
                spusBeans.get(position).setNumber(0);
                if (shopToDetailListener != null && spusBeans.size() > 0) {
                    shopToDetailListener.onRemovePriduct(spusBeans.get(position), spusBeans.get(position).getTag(),
                            spusBeans.get(position).getSection(), false);
                }
                return true;
            }
        });
        viewHolder.commodityName.setText(spusBeans.get(position).getName());
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBeans.get(position).getSkus();
        if (skus != null) {
            if (skus.size() == 0) {
                viewHolder.commodityPrise.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getMin_price() * spusBeans.get(position).getNumber())));
                viewHolder.shopDiscountPrice.setVisibility(View.GONE);
            } else {
                viewHolder.commodityPrise.setText(String.format("¥%1$s", NumberFormat.getInstance().format(getPrise(spusBeans.get(position)))));
                double origin_price = spusBeans.get(position).getSkus().get(0).getOrigin_price();
                double price = spusBeans.get(position).getSkus().get(0).getPrice();
                if (origin_price > price) {
                    viewHolder.shopDiscountPrice.setVisibility(View.VISIBLE);
                    viewHolder.shopDiscountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getSkus().get(0).getOrigin_price() * spusBeans.get(position).getNumber())));
                    viewHolder.shopDiscountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.shopDiscountPrice.getPaint().setAntiAlias(true); //去掉锯齿
                } else {
                    viewHolder.shopDiscountPrice.setVisibility(View.GONE);
                }
            }
        } else {
            viewHolder.commodityPrise.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getMin_price() * spusBeans.get(position).getNumber())));
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
                //判断库存
                if (foodIsMaximum(spusBeans.get(position))) {
                    ToastUtils.show(context, String.format(context.getString(R.string.hint_food_maximum), spusBeans.get(position).getName()), Toast.LENGTH_SHORT);
                } else if (num >= 99) {
                    ToastUtils.show(context, context.getString(R.string.can_not_buy_more), Toast.LENGTH_SHORT);
                } else {
                    num++;
                    spusBeans.get(position).setNumber(num);
                    viewHolder.commodityPrise.setText(String.format("¥%1$s", NumberFormat.getInstance().format(getPrise(spusBeans.get(position)))));
                    if (viewHolder.shopDiscountPrice.getVisibility() == View.VISIBLE) {
                        viewHolder.shopDiscountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getSkus().get(0).getOrigin_price() * spusBeans.get(position).getNumber())));
                    }
                    viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                    if (shopToDetailListener != null) {
                        shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                                spusBeans.get(position).getSection(), true, false);
                    }
                }
            }
        });

        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            int num = 0;
            int minOrderCount = 0;

            @Override
            public void onClick(View v) {
                if (spusBeans.size() > 0 && spusBeans.size() > position) {
                    num = spusBeans.get(position).getNumber();
                    minOrderCount = getMinOrderCount(spusBeans.get(position));
                }
                boolean isMinOrderCount = false;
                if (num > 0) {
                    if (minOrderCount == num) {
                        num -= minOrderCount;
                        isMinOrderCount = true;
                        Constant.MIN_COUNT = true;
                    } else {
                        num--;
                        isMinOrderCount = false;
                        Constant.MIN_COUNT = false;
                    }
                    spusBeans.get(position).setNumber(num);
                    viewHolder.commodityPrise.setText(String.format("¥%1$s", NumberFormat.getInstance().format(getPrise(spusBeans.get(position)))));
                    if (viewHolder.shopDiscountPrice.getVisibility() == View.VISIBLE) {
                        viewHolder.shopDiscountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBeans.get(position).getSkus().get(0).getOrigin_price() * spusBeans.get(position).getNumber())));
                    }
                    viewHolder.shoppingNum.setText(spusBeans.get(position).getNumber() + "");
                    if (shopToDetailListener != null) {
                        shopToDetailListener.onUpdateDetailList(spusBeans.get(position), spusBeans.get(position).getTag(),
                                spusBeans.get(position).getSection(), false, isMinOrderCount);
                    }
                    if (num == 0) {
                        shopToDetailListener.onRemovePriduct(spusBeans.get(position), spusBeans.get(position).getTag(),
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

    private boolean foodIsMaximum(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
        //判断库存
        if (spusBean.getChoiceSkus() != null &&
                spusBean.getChoiceSkus().size() > 0 && spusBean.getSkus() != null && spusBean.getSkus().size() > 0) {
            for (int i = 0; i < spusBean.getChoiceSkus().size(); i++) {
                int selectSkusId = spusBean.getChoiceSkus().get(i).getId();
                for (int j = 0; j < spusBean.getSkus().size(); j++) {
                    int skusId = spusBean.getSkus().get(j).getId();
                    if (selectSkusId != 0 && skusId != 0 && selectSkusId == skusId
                            && spusBean.getSkus().get(0).getStock() != -1//-1为无限库存
                            && spusBean.getNumber() >= spusBean.getSkus().get(j).getStock()) {
                        return true;
                    }
                }
            }
        } else if (spusBean.getSkus() != null && spusBean.getSkus().size() > 0
                && spusBean.getSkus().get(0).getStock() != -1) {//-1为无限库存
            return spusBean.getNumber() >= spusBean.getSkus().get(0).getStock();
        }
        return false;
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

    private double getPrise(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean pro) {
        double sum = 0;
        int restrict = 0;
        double priceRestrict = 0;
        boolean exceedingTheLimit = false;
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = pro.getSkus();
        double price = 0;
        if (skus != null) {
            if (skus.size() == 0) {
                price = pro.getMin_price();
            } else if (skus.size() == 1) {
                if (exceedingTheLimit) {
                    price = pro.getSkus().get(0).getOrigin_price();
                } else {
                    price = pro.getSkus().get(0).getPrice();
                }
                if (pro.getSkus().get(0).getPrice() < pro.getSkus().get(0).getOrigin_price()) {
                    restrict = pro.getSkus().get(0).getRestrict();
                    if (restrict > 0 && restrict < pro.getNumber() && !exceedingTheLimit) {
                        exceedingTheLimit = true;
                    } else {
                        exceedingTheLimit = false;
                    }
                    for (int i = 0; i < pro.getNumber(); i++) {
                        if (restrict > 0) {
                            if (i < restrict) {
                                priceRestrict = pro.getSkus().get(0).getPrice();
                            } else {
                                price = pro.getSkus().get(0).getOrigin_price();
                            }
                        }
                    }
                }
            } else if (skus.size() > 1) {
                price = pro.getChoiceSkus().get(0).getPrice();
                if (pro.getChoiceSkus().get(0).getPrice() < pro.getChoiceSkus().get(0).getOrigin_price()) {
                }
            }
        } else {
            price = pro.getMin_price();
        }

        if (exceedingTheLimit) {
            double sumRestrict = DoubleUtil.sum(sum, DoubleUtil.mul((double) restrict, Double.parseDouble("" + priceRestrict)));
            sum = DoubleUtil.sum(sumRestrict, DoubleUtil.mul((double) (pro.getNumber() - restrict), Double.parseDouble("" + price)));
        } else {
            sum = DoubleUtil.sum(sum, DoubleUtil.mul((double) pro.getNumber(), Double.parseDouble("" + price)));
        }
        return sum;
    }
}

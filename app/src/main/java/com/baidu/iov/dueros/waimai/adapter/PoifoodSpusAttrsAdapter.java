package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class PoifoodSpusAttrsAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater mInflater;
    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeans;
    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans;
    private final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs;
    private SetPriceListener setPriceListener;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
    private boolean[] choiseAttrs;

    public PoifoodSpusAttrsAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeans,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans,
                                   PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                                   boolean[] choiseAttrs) {
        this.context = context;
        this.attrsBeans = attrsBeans;
        this.skusBeans = skusBeans;
        this.spusBean = spusBean;
        this.choiceSkus = choiceSkus;
        this.productList = productList;
        this.choiseAttrs = choiseAttrs;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (attrsBeans.size() > 0) {
            if (skusBeans.size() > 1) {
                return attrsBeans.size() + 1;
            } else {
                return attrsBeans.size();
            }
        } else {
            if (skusBeans.size() > 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spus_attrs, null);
            viewHolder = new ViewHolder();
            viewHolder.attrsName = (TextView) convertView.findViewById(R.id.tv_attrs_name);
            viewHolder.recyclerview = (RecyclerView) convertView.findViewById(R.id.rl_recyclerview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GridLayoutManager layoutManage = new GridLayoutManager(context, 3);
        viewHolder.recyclerview.setLayoutManager(layoutManage);
        if (attrsBeans.size() > 0 && position != attrsBeans.size()) {
            viewHolder.attrsName.setText(attrsBeans.get(position).getName());
            final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> values = attrsBeans.get(position).getValues();
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> lastChoiceAttrs = attrsBeans.get(position).getChoiceAttrs();
            final SpecificationAdapter specificationAdapter = new SpecificationAdapter(context, lastChoiceAttrs, null, values, null);
            viewHolder.recyclerview.setAdapter(specificationAdapter);
            specificationAdapter.setOnItemClickListerner(new SpecificationAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int p) {
                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> valuesBeans = attrsBeans.get(position).getValues();
                    for (int i = 0; i < viewHolder.recyclerview.getChildCount(); i++) {
                        View rootView = viewHolder.recyclerview.getChildAt(i);
                        rootView.setBackgroundResource(R.drawable.shape_filter_selected_bg);
                        TextView textItem = ((TextView) rootView.findViewById(R.id.textItem));

                        if (i == p) {
                            rootView.setBackgroundResource(R.drawable.shape_filter_selected_bg);
                            textItem.setTextColor(context.getResources().getColor(R.color.specifiction_blue));
                            choiceAttrs = new ArrayList<>();
                            choiceAttrs.add(valuesBeans.get(i));
                            spusBean.getAttrs().get(position).setChoiceAttrs(choiceAttrs);
                            choiseAttrs[position] = true;
                        } else {
                            rootView.setBackgroundResource(R.drawable.shape_filter_unselected_bg);
                            textItem.setTextColor(context.getResources().getColor(R.color.white));

                        }
                    }
                    if (spusBean.getSkus().size() >= 2) {
                        if (choiceSkus.size() == 0) {
                            return;
                        }
                    }
                    if (spusBean.getAttrs().size() > 0) {
                        for (int i = 0; i < spusBean.getAttrs().size(); i++) {
                            if (choiseAttrs[i] == false) {
                                return;
                            }
                        }
                    }
                    inProductList();
                }
            });

        } else {
            if (skusBeans.size() > 1) {
                viewHolder.attrsName.setText(context.getString(R.string.specification) + ":");
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> lastChoiceSkus = spusBean.getChoiceSkus();

                final SpecificationAdapter specificationAdapter = new SpecificationAdapter(context, null, lastChoiceSkus, null, skusBeans);
                viewHolder.recyclerview.setAdapter(specificationAdapter);
                specificationAdapter.setOnItemClickListerner(new SpecificationAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int p) {
                        for (int i = 0; i < viewHolder.recyclerview.getChildCount(); i++) {
                            View rootView = viewHolder.recyclerview.getChildAt(i);
                            rootView.setBackgroundResource(R.drawable.shape_filter_selected_bg);
                            TextView textItem = ((TextView) rootView.findViewById(R.id.textItem));

                            if (i == p) {
                                rootView.setBackgroundResource(R.drawable.shape_filter_selected_bg);
                                textItem.setTextColor(context.getResources().getColor(R.color.specifiction_blue));

                                choiceSkus.clear();
                                choiceSkus.add(skusBeans.get(i));
                                spusBean.setChoiceSkus(choiceSkus);
                                double price = skusBeans.get(i).getPrice();
                                if (setPriceListener != null) {
                                    setPriceListener.setPrice("" + price);
                                }
                            } else {
                                rootView.setBackgroundResource(R.drawable.shape_filter_unselected_bg);
                                textItem.setTextColor(context.getResources().getColor(R.color.white));
                            }
                        }
                        if (spusBean.getSkus().size() >= 2) {
                            if (choiceSkus.size() == 0) {
                                return;
                            }
                        }
                        if (spusBean.getAttrs().size() > 0) {
                            for (int i = 0; i < spusBean.getAttrs().size(); i++) {
                                if (choiseAttrs[i] == false) {
                                    return;
                                }
                            }
                        }
                        inProductList();
                    }
                });
            }
        }
        return convertView;
    }

    private void inProductList() {
        boolean inList = false;
        if (productList.contains(spusBean)) {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                if (spusBean.equals(shopProduct)) {
                    boolean isFood = true;
                    for (int a = 0; a < spusBean.getAttrs().size(); a++) {
                        if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean shopBean = shopProduct.getAttrs().get(a);
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean = spusBean.getAttrs().get(a);
                            //判断是否为同一规格
                            if (shopBean.getChoiceAttrs() != null && shopBean.getChoiceAttrs().size() > 0 &&
                                    attrsBean.getChoiceAttrs() != null && attrsBean.getChoiceAttrs().size() > 0 &&
                                    shopBean.getChoiceAttrs().get(0).getId() != attrsBean.getChoiceAttrs().get(0).getId()) {
                                isFood = false;
                                break;
                            }
                        }
                    }
                    //规格
                    for (int b = 0; b < spusBean.getChoiceSkus().size(); b++) {
                        if (shopProduct.getChoiceSkus() != null && shopProduct.getChoiceSkus().size() > 0) {
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean shopBean = shopProduct.getChoiceSkus().get(b);
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean skusBean = spusBean.getChoiceSkus().get(b);
                            if (shopBean.getId() != skusBean.getId()) {
                                isFood = false;
                                break;
                            }
                        }
                    }
                    if (isFood) {
                        setPriceListener.setNumber(shopProduct.getNumber());
                        inList = true;
                        break;
                    }
                }
            }
            if (!inList) {
                if (setPriceListener != null) {
                    inList = false;
                    setPriceListener.setNumber(0);
                }
            }
        } else {
            if (FoodListActivity.mOneMoreOrder) {
                for (int i = 0; i < productList.size(); i++) {
                    PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct = productList.get(i);
                    if (shopProduct.getId() == spusBean.getId()) {
                        boolean isFood = true;
                        for (int a = 0; a < spusBean.getAttrs().size(); a++) {
                            if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean shopBean = shopProduct.getAttrs().get(a);
                                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean = spusBean.getAttrs().get(a);
                                //判断是否为同一规格
                                if (shopBean.getChoiceAttrs() != null && shopBean.getChoiceAttrs().size() > 0 &&
                                        attrsBean.getChoiceAttrs() != null && attrsBean.getChoiceAttrs().size() > 0 &&
                                        shopBean.getChoiceAttrs().get(0).getId() != attrsBean.getChoiceAttrs().get(0).getId()) {
                                    isFood = false;
                                    break;
                                }
                            }
                        }
                        //规格
                        for (int b = 0; b < spusBean.getChoiceSkus().size(); b++) {
                            if (shopProduct.getChoiceSkus() != null && shopProduct.getChoiceSkus().size() > 0) {
                                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean shopBean = shopProduct.getChoiceSkus().get(b);
                                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean skusBean = spusBean.getChoiceSkus().get(b);
                                if (shopBean.getId() != skusBean.getId()) {
                                    isFood = false;
                                    break;
                                }
                            }
                        }
                        if (isFood) {
                            setPriceListener.setNumber(shopProduct.getNumber());
                            inList = true;
                            break;
                        }
                    }
                }
                if (!inList) {
                    if (setPriceListener != null) {
                        inList = false;
                        setPriceListener.setNumber(0);
                    }
                }
            } else {
                if (setPriceListener != null) {
                    inList = false;
                    setPriceListener.setNumber(0);
                }
            }
        }
    }

    class ViewHolder {
        public TextView attrsName;
        public RecyclerView recyclerview;
    }

    public void setPriceListener(SetPriceListener setPriceListener) {
        this.setPriceListener = setPriceListener;
    }

    public interface SetPriceListener {
        void setPrice(String price);

        void setNumber(int number);
    }
}

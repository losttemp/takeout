package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 18-10-19.
 */

public class PoifoodSpusListAdapter extends PoifoodSpusListSectionedBaseAdapter {
    private static final String TAG = PoifoodSpusListAdapter.class.getSimpleName();
    private FoodListActivity activity;
    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans;
    private HolderClickListener mHolderClickListener;
    private Context context;
    private LayoutInflater mInflater;
    private onCallBackListener callBackListener;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public PoifoodSpusListAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                                  List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans, FoodListActivity activity) {
        this.context = context;
        this.foodSpuTagsBeans = foodSpuTagsBeans;
        this.productList = productList;
        mInflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    @Override
    public Object getItem(int section, int position) {
        return foodSpuTagsBeans.get(section).getSpus().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return foodSpuTagsBeans.size();
    }

    @Override
    public int getCountForSection(int section) {
        return foodSpuTagsBeans.get(section).getSpus().size();
    }

    @Override
    public View getItemView(final int section, final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.food_spu_tags_bean_item, null);
            viewHolder = new ViewHolder();
            viewHolder.head = (ImageView) convertView.findViewById(R.id.head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.prise = (TextView) convertView.findViewById(R.id.prise);
            viewHolder.increase = (TextView) convertView.findViewById(R.id.increase);
            viewHolder.reduce = (TextView) convertView.findViewById(R.id.reduce);
            viewHolder.shoppingNum = (TextView) convertView.findViewById(R.id.shoppingNum);
            viewHolder.add = (Button) convertView.findViewById(R.id.btn_add);
            viewHolder.specifications = (Button) convertView.findViewById(R.id.btn_specifications);
            viewHolder.action = (LinearLayout) convertView.findViewById(R.id.action);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = foodSpuTagsBeans.get(section).getSpus().get(position);
        Lg.getInstance().d(TAG, "spusBean.getName() = " + spusBean.getName());
        viewHolder.name.setText(spusBean.getName());
        final String pictureUrl = spusBean.getPicture();
        GlideApp.with(context)
                .load(pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(viewHolder.head);
        viewHolder.prise.setText("" + spusBean.getMin_price());
        viewHolder.shoppingNum.setText("" + spusBean.getNumber());
        if ((spusBean.getAttrs() != null && spusBean.getAttrs().size() > 0) ||
                (spusBean.getSkus() != null && spusBean.getSkus().size() > 1)) {
            viewHolder.specifications.setVisibility(View.VISIBLE);
            viewHolder.add.setVisibility(View.GONE);
            viewHolder.specifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBeanNew = null;
                    try {
                        spusBeanNew = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) spusBean.deepClone();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    View popView = activity.getPopView(R.layout.dialog_spus_specifications);
                    final Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart_specification);
                    ListView specificationsList = (ListView) popView.findViewById(R.id.gv_specifications);
                    final TextView specificationsPrice = (TextView) popView.findViewById(R.id.tv_specifications_price);
                    TextView increase = (TextView) popView.findViewById(R.id.increase);
                    TextView reduce = (TextView) popView.findViewById(R.id.reduce);
                    final TextView shoppingNum = (TextView) popView.findViewById(R.id.shoppingNum);
                    final LinearLayout action = (LinearLayout) popView.findViewById(R.id.action);
                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                    final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = new ArrayList<>();
                    final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus = new ArrayList<>();

                    PoifoodSpusAttrsAdapter poifoodSpusAttrsAdapter = new PoifoodSpusAttrsAdapter(context, attrs, skus, spusBeanNew, choiceAttrs, choiceSkus);
                    specificationsList.setAdapter(poifoodSpusAttrsAdapter);
                    poifoodSpusAttrsAdapter.setPriceListener(new PoifoodSpusAttrsAdapter.SetPriceListener() {
                        @Override
                        public void setPrice(String price) {
                            specificationsPrice.setText(String.format("￥%1$s", price));
                        }
                    });

                    final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean finalSpusBeanNew = spusBeanNew;
                    addToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalSpusBeanNew.getSkus().size() >= 2) {
                                if (choiceSkus.size() == 0) {
                                    Toast.makeText(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if (finalSpusBeanNew.getAttrs().size() > 0) {
                                for (int i = 0; i < finalSpusBeanNew.getAttrs().size(); i++) {
                                    if (choiceAttrs.size() == 0) {
                                        Toast.makeText(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            if (productList.contains(finalSpusBeanNew)) {
                                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                                    Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                                    if (finalSpusBeanNew.getId() == shopProduct.getId()) {
                                        if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                                            for (int i = 0; i < shopProduct.getAttrs().size(); i++) {
                                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = shopProduct.getAttrs().get(i).getChoiceAttrs();
                                                long id = choiceAttrs.get(0).getId();
                                                if (id == finalSpusBeanNew.getAttrs().get(i).getChoiceAttrs().get(0).getId()) {
                                                    finalSpusBeanNew.setNumber(shopProduct.getNumber());
                                                }
                                            }
                                        } else {
                                            if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                                for (int i = 0; i < shopProduct.getSkus().size(); i++) {
                                                    int id = shopProduct.getChoiceSkus().get(0).getId();
                                                    if (id == finalSpusBeanNew.getSkus().get(i).getId()) {
                                                        finalSpusBeanNew.setNumber(shopProduct.getNumber());
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            int num = finalSpusBeanNew.getNumber();
                            int min_order_count = getMinOrderCount(finalSpusBeanNew);
                            num += min_order_count;
                            finalSpusBeanNew.setNumber(num);
                            if (callBackListener != null) {
                                callBackListener.updateProduct(finalSpusBeanNew, finalSpusBeanNew.getTag());
                            }
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.VISIBLE);
                            shoppingNum.setText(finalSpusBeanNew.getNumber() + "");
                        }
                    });
                    increase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            increaseOnClick(finalSpusBeanNew, viewHolder);
                            shoppingNum.setText(finalSpusBeanNew.getNumber() + "");
                        }
                    });
                    reduce.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reduceOnClick(finalSpusBeanNew, viewHolder);
                            shoppingNum.setText(finalSpusBeanNew.getNumber() + "");
                        }
                    });
                    activity.showFoodListActivityDialog(view, popView);
                }
            });
        } else {
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.specifications.setVisibility(View.GONE);
            viewHolder.action.setVisibility(View.GONE);
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.add.setVisibility(View.GONE);
                    viewHolder.action.setVisibility(View.VISIBLE);
                    increaseOnClick(spusBean, viewHolder);
                }
            });
        }
        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseOnClick(spusBean, viewHolder);
            }
        });
        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceOnClick(spusBean, viewHolder);
            }
        });

        viewHolder.shoppingNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    int shoppingNum = Integer.parseInt(viewHolder.shoppingNum.getText().toString());
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popView = activity.getPopView(R.layout.dialog_spus_details);
                Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart);
                TextView spusName = (TextView) popView.findViewById(R.id.tv_spus_name);
                ImageView spusPicture = (ImageView) popView.findViewById(R.id.iv_spus);
                TextView spusPrice = (TextView) popView.findViewById(R.id.tv_spus_price);
                spusPrice.setText(String.format("￥%1$s", "" + spusBean.getMin_price()));
                spusName.setText(spusBean.getName());
                GlideApp.with(context)
                        .load(pictureUrl)
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()
                        .into(spusPicture);
                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num = spusBean.getNumber();
                        int minOrderCount = getMinOrderCount(spusBean);
                        num += minOrderCount;
                        spusBean.setNumber(num);
                        viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                        if (callBackListener != null) {
                            Lg.getInstance().d("FoodListActivity", "spusBean.getNumber() = " + spusBean.getNumber());
                            callBackListener.updateProduct(spusBean, spusBean.getTag());
                        }
                    }
                });
                activity.showFoodListActivityDialog(view, popView);
            }
        });


        return convertView;
    }

    private void reduceOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, ViewHolder viewHolder) {
        int num = spusBean.getNumber();
        if (num > 0) {
            int min_order_count = getMinOrderCount(spusBean);
            num -= min_order_count;
            spusBean.setNumber(num);
            viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
            if (callBackListener != null) {
                callBackListener.updateProduct(spusBean, spusBean.getTag());
            } else {
            }
        }
    }

    private void increaseOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, ViewHolder viewHolder) {
        int min_order_count = getMinOrderCount(spusBean);
        if (min_order_count > 1) {
//            Toast.makeText(context, "至少购" + min_order_count + "份", Toast.LENGTH_SHORT).show();
        }
        int num = spusBean.getNumber();

        num += min_order_count;
        spusBean.setNumber(num);
        viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
        if (callBackListener != null) {
            callBackListener.updateProduct(spusBean, spusBean.getTag());
        } else {
        }
        if (mHolderClickListener != null) {
            int[] start_location = new int[2];
            viewHolder.shoppingNum.getLocationInWindow(start_location);
            Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);
            mHolderClickListener.onHolderClick(drawable, start_location);
        }
    }

    private int getMinOrderCount(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
        int min_order_count = 1;
        if (spusBean.getSkus().size() > 1) {
            min_order_count = spusBean.getChoiceSkus().get(0).getMin_order_count();
        } else {
            min_order_count = spusBean.getSkus().get(0).getMin_order_count();
        }
        return min_order_count;
    }

    class ViewHolder {
        public ImageView head;
        public TextView name;
        public TextView prise;
        public TextView increase;
        public TextView shoppingNum;
        public TextView reduce;
        public Button add;
        public Button specifications;
        public LinearLayout action;
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }


    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText("" + foodSpuTagsBeans.get(section).getName());
        return layout;
    }

    public interface onCallBackListener {
        void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag);
    }
}

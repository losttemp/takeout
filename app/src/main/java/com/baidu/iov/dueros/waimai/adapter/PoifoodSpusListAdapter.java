package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConstraintHeightListView;
import com.baidu.xiaoduos.syncclient.Entry;
import com.baidu.xiaoduos.syncclient.EventType;
import com.domain.multipltextview.MultiplTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ubuntu on 18-10-19.
 */

public class PoifoodSpusListAdapter extends RecyclerView.Adapter<PoifoodSpusListAdapter.MyViewHolder> {
    private static final String TAG = PoifoodSpusListAdapter.class.getSimpleName();
    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans;
    private HolderClickListener mHolderClickListener;
    private Context context;
    private LayoutInflater mInflater;
    private onCallBackListener callBackListener;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
    private View mVeiw;
    private boolean mInList;
    private Window mWindow;
    private PopupWindow mDetailDialog;
    private PopupWindow specificationDialog;

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public PoifoodSpusListAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                                  List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans, Window window) {
        this.context = context;
        this.foodSpuTagsBeans = foodSpuTagsBeans;
        this.productList = productList;
        this.mWindow = window;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_food_list_right_recyclerview, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int position) {
        viewHolder.textUI.setText(foodSpuTagsBeans.get(position).getName());
        viewHolder.my_gridview.setAdapter(viewHolder.new GridViewAdapter(
                foodSpuTagsBeans.get(position).getSpus(), position, context, productList,
                callBackListener, mHolderClickListener, foodSpuTagsBeans));
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(context);
        staggeredGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewHolder.my_gridview.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public int getItemCount() {
        return foodSpuTagsBeans.size();
    }

    public void SetOnSetHolderClickListener(HolderClickListener holderClickListener) {
        this.mHolderClickListener = holderClickListener;
    }

    public interface HolderClickListener {
        public void onHolderClick(Drawable drawable, int[] start_location);
    }

    public interface onCallBackListener {
        void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag, boolean increase, boolean refreshList);

        void removeProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag, int selection, boolean increase, boolean spec);
    }

    public void showFoodListActivityDialog(View view, View contentView, final PopupWindow window) {
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAtLocation(view, Gravity.TOP, 0, 0);
        backgroundAlpha(0.5f);
        ImageView dismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    public View getPopView(int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null, false);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mWindow.setAttributes(lp);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView my_gridview;
        TextView textUI;

        public RecyclerView getRecyclerView() {
            return this.my_gridview;
        }


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            my_gridview = (RecyclerView) itemView.findViewById(R.id.my_gridview);
            textUI = (TextView) itemView.findViewById(R.id.textItem);
        }

        public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
            private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
            private Context context;
            private boolean mInList;
            private HolderClickListener mHolderClickListener;
            private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans;
            private int section;
            private View mVeiw;
            private onCallBackListener callBackListener;
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans;

            public GridViewAdapter(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeans,
                                   int section, Context context,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                                   onCallBackListener callBackListener, HolderClickListener mHolderClickListener,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans) {
                this.spusBeans = spusBeans;
                this.section = section;
                this.context = context;
                this.productList = productList;
                this.callBackListener = callBackListener;
                this.mHolderClickListener = mHolderClickListener;
                this.foodSpuTagsBeans = foodSpuTagsBeans;
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_spu_tags_bean_item, viewGroup, false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean1 = spusBeans.get(position);
                if (spusBean1 != null && FoodListActivity.selectFoods.containsKey(spusBean1.getId())) {
                    spusBeans.remove(spusBean1);
                    spusBeans.add(position, FoodListActivity.selectFoods.get(spusBean1.getId()));
                    spusBean1 = spusBeans.get(position);
                }
                final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = spusBean1;
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans = spusBean.getSkus();
                Lg.getInstance().d(TAG, "spusBean.getName() = " + spusBean.getName());
                viewHolder.name.setText(spusBean.getName());
                if (section == 0) {
                    viewHolder.storeIndex.setText(String.valueOf(position + 1));
                } else {
                    int index = 0;
                    for (int i = 1; i <= section; i++) {
                        index += foodSpuTagsBeans.get(i - 1).getSpus().size();
                    }
                    viewHolder.storeIndex.setText(String.valueOf(index + position + 1));
                }
                viewHolder.describe.setText(spusBean.getDescription());
                if ((spusBean.getAttrs() != null && spusBean.getAttrs().size() > 0) ||
                        (spusBean.getSkus() != null && spusBean.getSkus().size() > 1)) {
                    viewHolder.specifications.setVisibility(View.VISIBLE);
                    viewHolder.add.setVisibility(View.GONE);
                    viewHolder.action.setVisibility(View.GONE);

                    int count = 0;
                    for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                        if (spusBean.getId() == shopProduct.getId()) {
                            count += shopProduct.getNumber();
                        }

                    }
                    if (count > 0) {
                        viewHolder.specificationsNumber.setText(String.valueOf(count));
                        viewHolder.specificationsNumber.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.specificationsNumber.setVisibility(View.GONE);
                    }

                } else if (spusBean.getNumber() == 0) {
                    viewHolder.specifications.setVisibility(View.GONE);
                    viewHolder.add.setVisibility(View.VISIBLE);
                    viewHolder.action.setVisibility(View.GONE);
                    viewHolder.discountPrice.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.specifications.setVisibility(View.GONE);
                    viewHolder.add.setVisibility(View.GONE);
                    viewHolder.action.setVisibility(View.VISIBLE);
                    viewHolder.discountPrice.setVisibility(View.GONE);
                    viewHolder.shoppingNum.setText("" + spusBean.getNumber());
                }
                if (spusBean.getStatus() == 0) {
                    viewHolder.soldOut.setVisibility(View.GONE);
                    viewHolder.view.setEnabled(true);
                    viewHolder.view.setAlpha(1.0f);
                    viewHolder.addNumber.setVisibility(View.VISIBLE);
                    int minOrderCount = getMinOrderCount(spusBean);
                    if (minOrderCount > 1) {
                        viewHolder.soldOut.setVisibility(View.VISIBLE);
                        viewHolder.soldOut.setText(String.format(context.getString(R.string.min_buy), NumberFormat.getInstance().format(minOrderCount)));
                    } else {
                        viewHolder.soldOut.setVisibility(View.GONE);
                    }
                } else if (spusBean.getStatus() == 1) {
                    viewHolder.addNumber.setVisibility(View.GONE);
                    viewHolder.soldOut.setVisibility(View.VISIBLE);
                    viewHolder.view.setEnabled(false);
                    viewHolder.view.setAlpha(0.6f);
                } else if (spusBean.getStatus() == 2) {
                    viewHolder.addNumber.setVisibility(View.GONE);
                    viewHolder.soldOut.setVisibility(View.VISIBLE);
                    viewHolder.soldOut.setText(context.getString(R.string.looting));
                    viewHolder.view.setEnabled(false);
                    viewHolder.view.setAlpha(0.6f);
                } else if (spusBean.getStatus() == 3) {
                    viewHolder.addNumber.setVisibility(View.GONE);
                    viewHolder.canNotBuy.setVisibility(View.VISIBLE);
                    viewHolder.view.setEnabled(false);
                    viewHolder.view.setAlpha(0.6f);
                }

                final String pictureUrl = spusBean.getPicture();
                GlideApp.with(context)
                        .load(pictureUrl)
                        .placeholder(R.drawable.default_goods_icon)
                        .skipMemoryCache(false)
                        .into(viewHolder.head);

                if (skusBeans.size() == 1) {
                    double price = skusBeans.get(0).getPrice();
                    double originPrice = skusBeans.get(0).getOrigin_price();
                    if (price == originPrice) {
                        viewHolder.discountPrice.setVisibility(View.GONE);
                        viewHolder.limitBuy.setVisibility(View.GONE);
                    } else {
                        viewHolder.discountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(originPrice)));
                        viewHolder.discountPrice.setVisibility(View.VISIBLE);
                        viewHolder.discountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        viewHolder.discountPrice.getPaint().setAntiAlias(true); //去掉锯齿
                        if (skusBeans.get(0).getRestrict() != 0 && skusBeans.get(0).getRestrict() > 0) {
                            viewHolder.limitBuy.setVisibility(View.VISIBLE);
                            viewHolder.limitBuy.setText(String.format(context.getString(R.string.limit_buy), NumberFormat.getInstance().format(skusBeans.get(0).getRestrict())));
                        }
                    }
                    viewHolder.prise.setText(NumberFormat.getInstance().format(price));
                } else {
                    double minOriginPrice = 0;
                    for (int i = 0; i < skusBeans.size(); i++) {
                        double originPrice = skusBeans.get(i).getOrigin_price();
                        Double dMinOriginPrice = new Double(String.valueOf(minOriginPrice));
                        Double dOriginPrice = new Double(String.valueOf(originPrice));
                        int retval = dMinOriginPrice.compareTo(dOriginPrice);
                        if (i == 0) {
                            minOriginPrice = originPrice;
                        } else {
                            if (retval == 0) {
                                minOriginPrice = originPrice;
                            } else if (retval > 0) {
                                minOriginPrice = originPrice;
                            } else {
                                minOriginPrice = minOriginPrice;
                            }
                        }
                    }
                    if (spusBean.getMin_price() == minOriginPrice) {
                        viewHolder.discountPrice.setVisibility(View.GONE);
                    } else {
                        viewHolder.discountPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(minOriginPrice)));
                        viewHolder.discountPrice.setVisibility(View.VISIBLE);
                        viewHolder.discountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        viewHolder.discountPrice.getPaint().setAntiAlias(true); //去掉锯齿
                    }
                    viewHolder.prise.setText(NumberFormat.getInstance().format(spusBean.getMin_price()));
                }
                viewHolder.shoppingNum.setText("" + spusBean.getNumber());
                viewHolder.add.setContentDescription(String.format(context.getString(R.string.to_eat_position), viewHolder.storeIndex.getText().toString()));
                viewHolder.increase.setContentDescription(String.format(context.getString(R.string.to_eat_position), viewHolder.storeIndex.getText().toString()));
                viewHolder.add.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                    @Override
                    public boolean performAccessibilityAction(View host, int action, Bundle args) {
                        int index = getIndex(viewHolder);
                        viewHolder.autoClick(index);
                        return true;
                    }
                });
                viewHolder.increase.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                    @Override
                    public boolean performAccessibilityAction(View host, int action, Bundle args) {
                        int index = getIndex(viewHolder);
                        viewHolder.autoClick(index);
                        return true;
                    }
                });
                viewHolder.reduce.setContentDescription(String.format(context.getString(R.string.cancel_position), viewHolder.storeIndex.getText().toString()));
                viewHolder.reduce.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                    @Override
                    public boolean performAccessibilityAction(View host, int action, Bundle args) {
                        int index = getIndex(viewHolder);
                        StandardCmdClient.getInstance().playTTS(context, context.getString(R.string.already_cancel) + spusBeans.get(index).getName());
                        if (callBackListener != null) {
                            callBackListener.removeProduct(spusBeans.get(index), spusBeans.get(index).getTag(), section, false, false);
                        }
                        return true;
                    }
                });
//                viewHolder.specifications.setContentDescription(String.format(context.getString(R.string.cancel_position), viewHolder.storeIndex.getText().toString()));
//                viewHolder.specifications.setAccessibilityDelegate(new View.AccessibilityDelegate() {
//                    @Override
//                    public boolean performAccessibilityAction(View host, int action, Bundle args) {
//                        int index = getIndex(viewHolder);
//                        StandardCmdClient.getInstance().playTTS(context, context.getString(R.string.already_cancel) + spusBeans.get(index).getName());
//                        if (callBackListener != null) {
//                            callBackListener.removeProduct(spusBeans.get(index), spusBeans.get(index).getTag(), section, false, true);
//                        }
//                        return true;
//                    }
//                });

                viewHolder.specifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        spcificationsOnclick(view, spusBean, viewHolder, section);
                    }
                });
                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //判断库存
                        if (foodIsMaximum(spusBean)) {
                            ToastUtils.show(context, String.format(context.getString(R.string.hint_food_maximum), spusBean.getName()), Toast.LENGTH_SHORT);
                        } else {
                            viewHolder.add.setVisibility(View.GONE);
                            viewHolder.action.setVisibility(View.VISIBLE);
                            increaseOnClick(spusBean, viewHolder, section, false, null);
                        }
                    }
                });
                viewHolder.increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constant.ANIMATION_END) {
                            increaseOnClick(spusBean, viewHolder, section, true, null);
                        }
                    }
                });
                viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reduceOnClick(spusBean, viewHolder, section);
                        if (spusBean.getNumber() == 0) {
                            viewHolder.add.setVisibility(View.VISIBLE);
                            viewHolder.action.setVisibility(View.GONE);
                        }
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

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mVeiw = view;
                        View popView = PoifoodSpusListAdapter.this.getPopView(R.layout.dialog_spus_details);
                        final Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart);
                        Button detailSpecifications = (Button) popView.findViewById(R.id.btn_spus_details_specifications);
                        TextView spusName = (TextView) popView.findViewById(R.id.tv_spus_name);
                        ImageView spusPicture = (ImageView) popView.findViewById(R.id.iv_spus);
                        MultiplTextView spusPrice = (MultiplTextView) popView.findViewById(R.id.tv_spus_price);
                        MultiplTextView spusOriginPrice = (MultiplTextView) popView.findViewById(R.id.tv_spus_origin_price);
                        TextView spusDescription = (TextView) popView.findViewById(R.id.tv_spus_description);
                        ImageView increase = (ImageView) popView.findViewById(R.id.increase);
                        ImageView reduce = (ImageView) popView.findViewById(R.id.reduce);
                        final MultiplTextView shoppingNum = (MultiplTextView) popView.findViewById(R.id.shoppingNum);
                        final RelativeLayout action = (RelativeLayout) popView.findViewById(R.id.action);
                        if (spusBean.getStatus() != 0) {
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.GONE);
                            detailSpecifications.setVisibility(View.GONE);
                        }
                        mDetailDialog = new PopupWindow(popView,
                                WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT, true);
                        showFoodListActivityDialog(view, popView, mDetailDialog);
                        if (viewHolder.specifications.getVisibility() == View.VISIBLE) {
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.GONE);
                            detailSpecifications.setVisibility(View.VISIBLE);
                        }
                        detailSpecifications.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDetailDialog.dismiss();
                                spcificationsOnclick(mVeiw, spusBean, viewHolder, section);
                            }
                        });

                        spusDescription.setText(spusBean.getDescription());
                        double originPrice = spusBean.getSkus().get(0).getOrigin_price();
                        double price = spusBean.getSkus().get(0).getPrice();
                        spusPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(price)));
                        if (price < originPrice) {
                            spusOriginPrice.setVisibility(View.VISIBLE);
                            spusOriginPrice.setText(String.format("¥%1$s", NumberFormat.getInstance().format(spusBean.getSkus().get(0).getOrigin_price())));
                            spusOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                            spusOriginPrice.getPaint().setAntiAlias(true); //去掉锯齿
                        } else {
                            spusOriginPrice.setVisibility(View.GONE);
                        }
                        spusName.setText(spusBean.getName());
                        GlideApp.with(context)
                                .load(pictureUrl)
                                .skipMemoryCache(true)
                                .into(spusPicture);
                        addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Entry.getInstance().onEvent(Constant.POIFOODLIST_CLICK_ON_INCREASE_OR_DECREASE, EventType.TOUCH_TYPE);
                                int num = spusBean.getNumber();
                                int minOrderCount = getMinOrderCount(spusBean);
                                if (minOrderCount > 1) {
                                    num += minOrderCount;
                                } else {
                                    num++;
                                }
                                spusBean.setNumber(num);
                                if (num > 0) {
                                    viewHolder.discountPrice.setVisibility(View.GONE);
                                }
                                viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                                if (callBackListener != null) {
                                    Lg.getInstance().d("FoodListActivity", "spusBean.getNumber() = " + spusBean.getNumber());
                                    callBackListener.updateProduct(spusBean, spusBean.getTag(), true, false);
                                }
                                addToCart.setVisibility(View.GONE);
                                action.setVisibility(View.VISIBLE);
                                shoppingNum.setText(spusBean.getNumber() + "");
                                viewHolder.add.setVisibility(View.GONE);
                                viewHolder.action.setVisibility(View.VISIBLE);
                                viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                            }
                        });
                        increase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //判断库存
                                if (foodIsMaximum(spusBean)) {
                                    ToastUtils.show(context, String.format(context.getString(R.string.hint_food_maximum), spusBean.getName()), Toast.LENGTH_SHORT);
                                } else if (spusBean.getNumber() >= 99) {
                                    ToastUtils.show(context, context.getString(R.string.can_not_buy_more), Toast.LENGTH_SHORT);
                                } else {
                                    increaseOnClick(spusBean, viewHolder, section, true, shoppingNum);
                                    shoppingNum.setText(spusBean.getNumber() + "");
                                }
                            }
                        });
                        reduce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                reduceOnClick(spusBean, viewHolder, section);
                                shoppingNum.setText(spusBean.getNumber() + "");
                                if (spusBean.getNumber() == 0) {
                                    addToCart.setVisibility(View.VISIBLE);
                                    action.setVisibility(View.GONE);
                                }
                            }
                        });
                        if (detailSpecifications.getVisibility() == View.GONE && spusBean.getNumber() > 0) {
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.VISIBLE);
                            shoppingNum.setText(spusBean.getNumber() + "");
                        }
                    }
                });
                if (viewHolder.action.getVisibility() == View.VISIBLE) {
                    viewHolder.discountPrice.setVisibility(View.GONE);
                }
            }

            private int getIndex(@NonNull ViewHolder viewHolder) {
                int index = 0;
                for (int i = 0; i < spusBeans.size(); i++) {
                    if (viewHolder.name.getText().equals(spusBeans.get(i).getName())) {
                        index = i;
                        break;
                    }
                }
                return index;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemCount() {
                return spusBeans == null ? 0 : spusBeans.size();
            }

            public class ViewHolder extends RecyclerView.ViewHolder {
                View view;
                ImageView head;
                TextView name;
                MultiplTextView prise;
                ImageView increase;
                ImageView reduce;
                TextView storeIndex;
                MultiplTextView shoppingNum;
                ImageView add;
                TextView specifications;
                Button specificationsNumber;
                RelativeLayout action;
                RelativeLayout addNumber;
                MultiplTextView discountPrice;
                MultiplTextView limitBuy;
                TextView describe;
                TextView soldOut;
                LinearLayout canNotBuy;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    view = itemView;
                    head = (ImageView) itemView.findViewById(R.id.head);
                    name = (TextView) itemView.findViewById(R.id.name);
                    prise = (MultiplTextView) itemView.findViewById(R.id.prise);
                    increase = (ImageView) itemView.findViewById(R.id.increase);
                    reduce = (ImageView) itemView.findViewById(R.id.reduce);
                    storeIndex = (TextView) itemView.findViewById(R.id.tv_store_index);
                    shoppingNum = (MultiplTextView) itemView.findViewById(R.id.shoppingNum);
                    add = (ImageView) itemView.findViewById(R.id.btn_add);
                    specifications = (TextView) itemView.findViewById(R.id.btn_specifications);
                    specificationsNumber = (Button) itemView.findViewById(R.id.tv_num);
                    action = (RelativeLayout) itemView.findViewById(R.id.action);
                    addNumber = (RelativeLayout) itemView.findViewById(R.id.rl_add_number);
                    discountPrice = (MultiplTextView) itemView.findViewById(R.id.tv_discount_price);
                    limitBuy = (MultiplTextView) itemView.findViewById(R.id.mt_limit_buy);
                    describe = (TextView) itemView.findViewById(R.id.tv_describe);
                    soldOut = (TextView) itemView.findViewById(R.id.tv_sold_out);
                    canNotBuy = (LinearLayout) itemView.findViewById(R.id.ll_not_buy_time);
                }

                public void autoClick(int position) {
                    if (mDetailDialog != null && mDetailDialog.isShowing()) {
                        mDetailDialog.dismiss();
                    }
                    if (specificationDialog != null && specificationDialog.isShowing()) {
                        specificationDialog.dismiss();
                    }
                    if (increase.getVisibility() == View.VISIBLE && add.getVisibility() == View.GONE
                            && specifications.getVisibility() == View.GONE) {
                        increase.performClick();
                        int count = getMinOrderCount(spusBeans.get(position));
                        String n = spusBeans.get(position).getName();
                        StandardCmdClient.getInstance().playTTS(context, String.format(context.getString(R.string.add_commodity), count, n));
                    }

                    if (add.getVisibility() == View.VISIBLE
                            && specifications.getVisibility() == View.GONE) {
                        add.performClick();
                        int count = getMinOrderCount(spusBeans.get(position));
                        String n = spusBeans.get(position).getName();
                        StandardCmdClient.getInstance().playTTS(context, String.format(context.getString(R.string.add_commodity), count, n));
                    }

                    if (specifications.getVisibility() == View.VISIBLE) {
                        specifications.performClick();
                        String n = spusBeans.get(position).getName();
                        StandardCmdClient.getInstance().playTTS(context, String.format(context.getString(R.string.choice_specifications), n));
                    }
                }
            }


            private void spcificationsOnclick(View view, PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                              final MyViewHolder.GridViewAdapter.ViewHolder viewHolder, final int section) {
                Entry.getInstance().onEvent(Constant.POIFOODLIST_SPECIFICATION_CLICK, EventType.TOUCH_TYPE);
                View popView = getPopView(R.layout.dialog_spus_specifications);
                final Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart_specification);
                ConstraintHeightListView specificationsList = (ConstraintHeightListView) popView.findViewById(R.id.gv_specifications);
                final MultiplTextView specificationsPrice = (MultiplTextView) popView.findViewById(R.id.tv_specifications_price);
                ImageView increase = (ImageView) popView.findViewById(R.id.increase);
                ImageView reduce = (ImageView) popView.findViewById(R.id.reduce);
                final MultiplTextView shoppingNum = (MultiplTextView) popView.findViewById(R.id.shoppingNum);
                TextView spusName = (TextView) popView.findViewById(R.id.tv_spus_name);
                final RelativeLayout action = (RelativeLayout) popView.findViewById(R.id.action);
                specificationsList.setmMaxHeight(296);
                spusName.setText(spusBean.getName());
                specificationsPrice.setText("¥" + NumberFormat.getInstance().format(spusBean.getMin_price()));
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus = new ArrayList<>();
                final boolean[] choiseAttrs = new boolean[attrs.size()];
                for (int i = 0; i < attrs.size(); i++) {
                    choiseAttrs[i] = false;
                }
                if (spusBean.getStatus() != 0) {
                    addToCart.setVisibility(View.GONE);
                    action.setVisibility(View.GONE);
                } else {

                }

                int specificationsNumber = spusBean.getSpecificationsNumber();
                if (specificationsNumber == 0 && spusBean.getChoiceSkus() != null && spusBean.getChoiceSkus().size() > 0) {
                    specificationsNumber = spusBean.getNumber();
                }
                List<Integer> list = new ArrayList<>();
                if (specificationsNumber != 0) {
                    for (int i = 0; i < productList.size(); i++) {
                        if (spusBean.getId() == productList.get(i).getId()) {
                            list.add(i);
                        }
                    }
                    if (list.size() > 0) {
                        Collections.sort(list);
                        Integer lastChoiceSpusIndex = list.get(list.size() - 1);
                        PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean lastChoiceSpus = productList.get(lastChoiceSpusIndex);
                        try {
                            spusBean = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) lastChoiceSpus.deepClone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        attrs = spusBean.getAttrs();
                        skus = spusBean.getSkus();
                        choiceSkus = spusBean.getChoiceSkus();
                        if (choiceSkus == null) {
                            choiceSkus = new ArrayList<>();
                        }
                        for (int i = 0; i < spusBean.getAttrs().size(); i++) {
                            choiseAttrs[i] = true;
                        }
                        int num = spusBean.getNumber();
                        spusBean.setNumber(num);
                        addToCart.setVisibility(View.GONE);
                        action.setVisibility(View.VISIBLE);
                        mInList = true;
                        shoppingNum.setText(spusBean.getNumber() + "");
                        if (skus != null) {
                            if (skus.size() == 0) {
                                specificationsPrice.setText("¥" + NumberFormat.getInstance().format(spusBean.getMin_price()));
                            } else if (skus.size() == 1) {
                                specificationsPrice.setText("¥" + NumberFormat.getInstance().format(spusBean.getSkus().get(0).getPrice()));
                            } else if (skus.size() > 1) {
                                specificationsPrice.setText("¥" + NumberFormat.getInstance().format(spusBean.getChoiceSkus().get(0).getPrice()));
                            }
                        } else {
                            specificationsPrice.setText("¥" + NumberFormat.getInstance().format(spusBean.getMin_price()));
                        }
                    }
                } else {
                    if (attrs != null) {
                        for (int i = 0; i < attrs.size(); i++) {
                            if (attrs.get(i).getChoiceAttrs() != null) {
                                attrs.get(i).getChoiceAttrs().clear();
                            }
                        }
                    }
                    if (spusBean.getChoiceSkus() != null) {
                        spusBean.getChoiceSkus().clear();
                    }
                }

                // 由于RecyclerView加载问题,需要将初始化商品放在此处
                // 初始化未选择
                if (choiseAttrs != null) {
                    try {
                        for (int k = 0; k < spusBean.getAttrs().size(); k++) {
                            if (!choiseAttrs[k]) {
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = new ArrayList<>();
                                choiceAttrs.add(attrs.get(k).getValues().get(0));
                                spusBean.getAttrs().get(k).setChoiceAttrs(choiceAttrs);
                                choiseAttrs[k] = true;
                            }
                        }
                    } catch (Exception e) {
                        // 防止数组越界
                        e.printStackTrace();
                    }
                }

                if (choiceSkus != null && choiceSkus.size() == 0) {
                    choiceSkus.clear();
                    choiceSkus.add(skus.get(0));
                    spusBean.setChoiceSkus(choiceSkus);
                    String price = String.valueOf(skus.get(0).getPrice());
                    if (specificationsPrice != null) {
                        specificationsPrice.setText(String.format("¥%1$s", price));
                    }
                }

                PoifoodSpusAttrsAdapter poifoodSpusAttrsAdapter = new PoifoodSpusAttrsAdapter(context, attrs, skus, spusBean,
                        choiceSkus, productList, choiseAttrs);
                specificationsList.setAdapter(poifoodSpusAttrsAdapter);

                poifoodSpusAttrsAdapter.setPriceListener(new PoifoodSpusAttrsAdapter.SetPriceListener() {
                    @Override
                    public void setPrice(String price) {
                        specificationsPrice.setText(String.format("¥%1$s", price));
                    }

                    @Override
                    public void setNumber(int number) {
                        if (number != 0) {
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.VISIBLE);
                            shoppingNum.setText(number + "");
                        } else {
                            addToCart.setVisibility(View.VISIBLE);
                            action.setVisibility(View.GONE);
                        }
                    }
                });

                final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> finalChoiceSkus = choiceSkus;
                final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean finalSpusBean = spusBean;
                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Entry.getInstance().onEvent(Constant.POIFOODLIST_CLICK_ON_INCREASE_OR_DECREASE, EventType.TOUCH_TYPE);
                        if (finalSpusBean.getSkus().size() >= 2) {
                            if (finalChoiceSkus.size() == 0) {
                                ToastUtils.show(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT);
                                return;
                            }
                        }
                        if (finalSpusBean.getAttrs().size() > 0) {
                            for (int i = 0; i < finalSpusBean.getAttrs().size(); i++) {
                                if (choiseAttrs[i] == false) {
                                    ToastUtils.show(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT);
                                    return;
                                }
                            }
                        }

                        mInList = inProductList(finalSpusBean);
                        int num = finalSpusBean.getNumber();
                        int min_order_count = getMinOrderCount(finalSpusBean);
                        if (min_order_count > 1) {
                            ToastUtils.show(context, context.getString(R.string.must_buy) +
                                    min_order_count + context.getString(R.string.share_buy), Toast.LENGTH_SHORT);
                            num += min_order_count;
                        } else {
                            num++;
                        }
                        finalSpusBean.setNumber(num);
                        if (callBackListener != null) {
                            callBackListener.updateProduct(finalSpusBean, finalSpusBean.getTag(), true, true);
                        }
                        addToCart.setVisibility(View.GONE);
                        action.setVisibility(View.VISIBLE);
                        mInList = true;
                        shoppingNum.setText(finalSpusBean.getNumber() + "");

                        viewHolder.specificationsNumber.setVisibility(View.VISIBLE);
                        setSpecificationNumber(finalSpusBean, viewHolder);
                        if (foodSpuTagsBeans != null && foodSpuTagsBeans.size() > 0 && context.getString(R.string.heat_text).equals(foodSpuTagsBeans.get(0).getName())) {
                            FoodListActivity.selectFoods.put(finalSpusBean.getId(), finalSpusBean);
                        }
                    }
                });
                increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //判断库存
                        if (foodIsMaximum(finalSpusBean)) {
                            ToastUtils.show(context, String.format(context.getString(R.string.hint_food_maximum), finalSpusBean.getName()), Toast.LENGTH_SHORT);
                        } else if (finalSpusBean.getNumber() >= 99) {
                            ToastUtils.show(context, context.getString(R.string.can_not_buy_more), Toast.LENGTH_SHORT);
                        } else {
                            inProductList(finalSpusBean);
                            increaseOnClick(finalSpusBean, viewHolder, section, true, shoppingNum);
                            shoppingNum.setText(finalSpusBean.getNumber() + "");
                        }
                        setSpecificationNumber(finalSpusBean, viewHolder);
                    }
                });
                reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inProductList(finalSpusBean);
                        reduceOnClick(finalSpusBean, viewHolder, section);
                        shoppingNum.setText(finalSpusBean.getNumber() + "");
                        if (finalSpusBean.getNumber() == 0) {
                            addToCart.setVisibility(View.VISIBLE);
                            action.setVisibility(View.GONE);
                        }
                        setSpecificationNumber(finalSpusBean, viewHolder);
                    }
                });
                specificationDialog = new PopupWindow(popView,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                showFoodListActivityDialog(view, popView, specificationDialog);
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

            private void setSpecificationNumber(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, ViewHolder viewHolder) {
                int specificationsNumber = 0;
                int specificationsSpus = -1;
                for (int i = 0; i < productList.size(); i++) {
                    if (spusBean.getId() == productList.get(i).getId()) {
                        specificationsSpus = i;
                        specificationsNumber += productList.get(specificationsSpus).getNumber();
                    }
                }
                if (specificationsNumber == 0) {
                    viewHolder.specificationsNumber.setVisibility(View.GONE);
                } else {
                    viewHolder.specificationsNumber.setText(specificationsNumber + "");
                }
                spusBean.setSpecificationsNumber(specificationsNumber);
                spusBean.setSpecificationsNumber(specificationsNumber);
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList = foodSpuTagsBeans.get(section).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (spusBean.getId() == shopProduct.getId()) {
                        shopProduct.setSpecificationsNumber(specificationsNumber);
                        break;
                    }
                }
            }

            private boolean inProductList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
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
                                spusBean.setNumber(shopProduct.getNumber());
                                inList = true;
                                break;
                            }
                        }
                    }
                    if (!inList) {
                        inList = false;
                        spusBean.setNumber(0);
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
                                    spusBean.setNumber(shopProduct.getNumber());
                                    spusBean.setSpecificationsNumber(shopProduct.getNumber());
                                    inList = true;
                                    break;
                                }
                            }
                        }
                        if (!inList) {
                            inList = false;
                            spusBean.setNumber(0);
                        }
                    } else {
                        inList = false;
                        spusBean.setNumber(0);
                    }
                }
                return inList;
            }

            private void reduceOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, MyViewHolder.GridViewAdapter.ViewHolder viewHolder, int section) {
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CLICK_ON_INCREASE_OR_DECREASE, EventType.TOUCH_TYPE);
                int num = spusBean.getNumber();
                int minOrderCount = getMinOrderCount(spusBean);
                if (num > 0) {
                    if (minOrderCount == spusBean.getNumber()) {
                        num -= minOrderCount;
                        Constant.MIN_COUNT = true;
                    } else {
                        num--;
                        Constant.MIN_COUNT = false;
                    }
                    spusBean.setNumber(num);
                    if (num == 0) {
                        viewHolder.discountPrice.setVisibility(View.VISIBLE);
                    }
                    viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                    if (spusBean.getNumber() == 0) {
                        viewHolder.action.setVisibility(View.GONE);
                        if (viewHolder.specifications.getVisibility() == View.GONE) {
                            viewHolder.add.setVisibility(View.VISIBLE);
                        }
                    }
                    if (callBackListener != null) {
                        callBackListener.updateProduct(spusBean, spusBean.getTag(), false, true);
                    } else {
//                        callBackListener.updateProduct(spusBean, spusBean.getTag(), section, false);
                    }
                    if (foodSpuTagsBeans != null && foodSpuTagsBeans.size() > 0 && context.getString(R.string.heat_text).equals(foodSpuTagsBeans.get(0).getName())) {
                        FoodListActivity.selectFoods.put(spusBean.getId(), spusBean);
                    }
                }
            }

            private void increaseOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                         ViewHolder viewHolder, int section, boolean alreadyToast, MultiplTextView detailShopNum) {
                Entry.getInstance().onEvent(Constant.POIFOODLIST_CLICK_ON_INCREASE_OR_DECREASE, EventType.TOUCH_TYPE);
                int min_order_count = getMinOrderCount(spusBean);
                if (min_order_count > 1 && !alreadyToast) {
                    ToastUtils.show(context, context.getString(R.string.must_buy) +
                            min_order_count + context.getString(R.string.share_buy), Toast.LENGTH_SHORT);
                }
                int num = spusBean.getNumber();
                //判断库存
                if (foodIsMaximum(spusBean)) {
                    ToastUtils.show(context, String.format(context.getString(R.string.hint_food_maximum), spusBean.getName()), Toast.LENGTH_SHORT);
                } else if (num >= 99) {
                    ToastUtils.show(context, context.getString(R.string.can_not_buy_more), Toast.LENGTH_SHORT);
                } else {
                    if (alreadyToast) {
                        num++;
                    } else {
                        if (min_order_count > 1) {
                            num += min_order_count;
                        } else {
                            num++;
                        }
                    }
                    spusBean.setNumber(num);
                    viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                    if (num > 0) {
                        viewHolder.discountPrice.setVisibility(View.GONE);
                    }
                    if (callBackListener != null) {
                        callBackListener.updateProduct(spusBean, spusBean.getTag(), true, true);
                    } else {
                    }
                    if (mHolderClickListener != null) {
                        int[] start_location = new int[2];
                        if (detailShopNum != null) {
                            detailShopNum.getLocationInWindow(start_location);
                        } else {
                            viewHolder.shoppingNum.getLocationInWindow(start_location);
                        }
                        Drawable drawable = context.getResources().getDrawable(R.drawable.adddetail);
                        mHolderClickListener.onHolderClick(drawable, start_location);
                    }
                    if (foodSpuTagsBeans != null && foodSpuTagsBeans.size() > 0 && context.getString(R.string.heat_text).equals(foodSpuTagsBeans.get(0).getName())) {
                        FoodListActivity.selectFoods.put(spusBean.getId(), spusBean);
                    }
                }
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
    }
}

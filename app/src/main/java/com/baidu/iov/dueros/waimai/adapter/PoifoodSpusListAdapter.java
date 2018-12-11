package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.domain.multipltextview.MultiplTextView;

import java.util.ArrayList;
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
        void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag, int selection, boolean increase);
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
                final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = spusBeans.get(position);
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
                } else if (spusBean.getNumber() == 0) {
                    viewHolder.specifications.setVisibility(View.GONE);
                    viewHolder.add.setVisibility(View.VISIBLE);
                    viewHolder.action.setVisibility(View.GONE);
                } else {
                    viewHolder.specifications.setVisibility(View.GONE);
                    viewHolder.add.setVisibility(View.GONE);
                    viewHolder.action.setVisibility(View.VISIBLE);
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
                        viewHolder.soldOut.setText(String.format(context.getString(R.string.min_buy), "" + minOrderCount));
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
                        .placeholder(R.mipmap.ic_launcher)
                        .centerCrop()
                        .into(viewHolder.head);

                if (skusBeans.size() == 1) {
                    double price = skusBeans.get(0).getPrice();
                    double originPrice = skusBeans.get(0).getOrigin_price();
                    if (price == originPrice) {
                        viewHolder.discountPrice.setVisibility(View.GONE);
                        viewHolder.limitBuy.setVisibility(View.GONE);
                    } else {
                        viewHolder.discountPrice.setText(String.format("¥%1$s", "" + originPrice));
                        viewHolder.discountPrice.setVisibility(View.VISIBLE);
                        viewHolder.discountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        if (skusBeans.get(0).getRestrict() != 0 && skusBeans.get(0).getRestrict() > 0) {
                            viewHolder.limitBuy.setVisibility(View.VISIBLE);
                            viewHolder.limitBuy.setText(String.format(context.getString(R.string.limit_buy), "" + skusBeans.get(0).getRestrict()));
                        }
                    }
                    viewHolder.prise.setText("" + price);
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
                        viewHolder.discountPrice.setText(String.format("¥%1$s", "" + minOriginPrice));
                        viewHolder.discountPrice.setVisibility(View.VISIBLE);
                        viewHolder.discountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    viewHolder.prise.setText("" + spusBean.getMin_price());
                }
                viewHolder.shoppingNum.setText("" + spusBean.getNumber());

                viewHolder.specifications.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        spcificationsOnclick(view, spusBean, viewHolder, section);
                    }
                });
                viewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.add.setVisibility(View.GONE);
                        viewHolder.action.setVisibility(View.VISIBLE);
                        increaseOnClick(spusBean, viewHolder, section, false);
                    }
                });
                viewHolder.increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseOnClick(spusBean, viewHolder, section, true);
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
                        MultiplTextView spusName = (MultiplTextView) popView.findViewById(R.id.tv_spus_name);
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
                        final PopupWindow window = new PopupWindow(popView,
                                WindowManager.LayoutParams.MATCH_PARENT,
                                WindowManager.LayoutParams.WRAP_CONTENT, true);
                        showFoodListActivityDialog(view, popView, window);
                        if (viewHolder.specifications.getVisibility() == View.VISIBLE) {
                            addToCart.setVisibility(View.GONE);
                            action.setVisibility(View.GONE);
                            detailSpecifications.setVisibility(View.VISIBLE);
                        }
                        detailSpecifications.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                window.dismiss();
                                spcificationsOnclick(mVeiw, spusBean, viewHolder, section);
                            }
                        });

                        spusDescription.setText(spusBean.getDescription());
                        double originPrice = spusBean.getSkus().get(0).getOrigin_price();
                        double price = spusBean.getSkus().get(0).getPrice();
                        spusPrice.setText(String.format("¥%1$s", "" + price));
                        if (price < originPrice) {
                            spusOriginPrice.setVisibility(View.VISIBLE);
                            spusOriginPrice.setText(String.format("¥%1$s", "" + spusBean.getSkus().get(0).getOrigin_price()));
                            spusOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        } else {
                            spusOriginPrice.setVisibility(View.GONE);
                        }
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
                                if (minOrderCount > 1) {
                                    num += minOrderCount;
                                } else {
                                    num++;
                                }
                                spusBean.setNumber(num);
                                viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                                if (callBackListener != null) {
                                    Lg.getInstance().d("FoodListActivity", "spusBean.getNumber() = " + spusBean.getNumber());
                                    callBackListener.updateProduct(spusBean, spusBean.getTag(), section, true);
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
                                increaseOnClick(spusBean, viewHolder, section, true);
                                shoppingNum.setText(spusBean.getNumber() + "");
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
                MultiplTextView storeIndex;
                MultiplTextView shoppingNum;
                ImageView add;
                Button specifications;
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
                    storeIndex = (MultiplTextView) itemView.findViewById(R.id.tv_store_index);
                    shoppingNum = (MultiplTextView) itemView.findViewById(R.id.shoppingNum);
                    add = (ImageView) itemView.findViewById(R.id.btn_add);
                    specifications = (Button) itemView.findViewById(R.id.btn_specifications);
                    action = (RelativeLayout) itemView.findViewById(R.id.action);
                    addNumber = (RelativeLayout) itemView.findViewById(R.id.rl_add_number);
                    discountPrice = (MultiplTextView) itemView.findViewById(R.id.tv_discount_price);
                    limitBuy = (MultiplTextView) itemView.findViewById(R.id.mt_limit_buy);
                    describe = (TextView) itemView.findViewById(R.id.tv_describe);
                    soldOut = (TextView) itemView.findViewById(R.id.tv_sold_out);
                    canNotBuy = (LinearLayout) itemView.findViewById(R.id.ll_not_buy_time);
                }

                public void autoClick(int position) {
                    if (increase.getVisibility() == View.VISIBLE && add.getVisibility() == View.GONE
                            && specifications.getVisibility() == View.GONE) {
                        increase.performClick();
                        int count = getMinOrderCount(spusBeans.get(position));
                        String n = spusBeans.get(position).getName();
                        VoiceManager.getInstance().playTTS(context, String.format(context.getString(R.string.add_commodity), count, n));
                    }

                    if (add.getVisibility() == View.VISIBLE
                            && specifications.getVisibility() == View.GONE) {
                        add.performClick();
                        int count = getMinOrderCount(spusBeans.get(position));
                        String n = spusBeans.get(position).getName();
                        VoiceManager.getInstance().playTTS(context, String.format(context.getString(R.string.add_commodity), count, n));
                    }

                    if (specifications.getVisibility() == View.VISIBLE) {
                        specifications.performClick();
                        String n = spusBeans.get(position).getName();
                        VoiceManager.getInstance().playTTS(context, String.format(context.getString(R.string.choice_specifications), n));
                    }
                }
            }


            private void spcificationsOnclick(View view, final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                              final MyViewHolder.GridViewAdapter.ViewHolder viewHolder, final int section) {
                View popView = getPopView(R.layout.dialog_spus_specifications);
                final Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart_specification);
                ListView specificationsList = (ListView) popView.findViewById(R.id.gv_specifications);
                final MultiplTextView specificationsPrice = (MultiplTextView) popView.findViewById(R.id.tv_specifications_price);
                ImageView increase = (ImageView) popView.findViewById(R.id.increase);
                ImageView reduce = (ImageView) popView.findViewById(R.id.reduce);
                final MultiplTextView shoppingNum = (MultiplTextView) popView.findViewById(R.id.shoppingNum);
                MultiplTextView spusName = (MultiplTextView) popView.findViewById(R.id.tv_spus_name);
                final RelativeLayout action = (RelativeLayout) popView.findViewById(R.id.action);
                spusName.setText(spusBean.getName());
                specificationsPrice.setText("¥" + spusBean.getMin_price());
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs = spusBean.getAttrs();
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus = spusBean.getSkus();
                final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus = new ArrayList<>();
                final boolean[] choiseAttrs = new boolean[attrs.size()];
                for (int i = 0; i < attrs.size(); i++) {
                    choiseAttrs[i] = false;
                }
                if (spusBean.getStatus() != 0) {
                    addToCart.setVisibility(View.GONE);
                    action.setVisibility(View.GONE);
                } else {

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

                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (spusBean.getSkus().size() >= 2) {
                            if (choiceSkus.size() == 0) {
                                ToastUtils.show(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT);
                                return;
                            }
                        }
                        if (spusBean.getAttrs().size() > 0) {
                            for (int i = 0; i < spusBean.getAttrs().size(); i++) {
                                if (choiseAttrs[i] == false) {
                                    ToastUtils.show(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT);
                                    return;
                                }
                            }
                        }
                        mInList = inProductList(spusBean);
                        int num = spusBean.getNumber();
                        int min_order_count = getMinOrderCount(spusBean);
                        num += min_order_count;
                        spusBean.setNumber(num);
                        if (callBackListener != null) {
                            callBackListener.updateProduct(spusBean, spusBean.getTag(), section, true);
                        }
                        addToCart.setVisibility(View.GONE);
                        action.setVisibility(View.VISIBLE);
                        mInList = true;
                        shoppingNum.setText(spusBean.getNumber() + "");
                    }
                });
                increase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inProductList(spusBean);
                        increaseOnClick(spusBean, viewHolder, section, true);
                        shoppingNum.setText(spusBean.getNumber() + "");
                    }
                });
                reduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inProductList(spusBean);
                        reduceOnClick(spusBean, viewHolder, section);
                        shoppingNum.setText(spusBean.getNumber() + "");
                        if (spusBean.getNumber() == 0) {
                            addToCart.setVisibility(View.VISIBLE);
                            action.setVisibility(View.GONE);
                        }
                    }
                });
                final PopupWindow window = new PopupWindow(popView,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                showFoodListActivityDialog(view, popView, window);
            }

            private boolean inProductList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
                boolean inList = false;
                if (productList.contains(spusBean)) {
                    for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                        Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                        if (spusBean.getId() == shopProduct.getId()) {
                            if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                                for (int i = 0; i < shopProduct.getAttrs().size(); i++) {
                                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = shopProduct.getAttrs().get(i).getChoiceAttrs();
                                    long id = choiceAttrs.get(0).getId();
                                    if (id == spusBean.getAttrs().get(i).getChoiceAttrs().get(0).getId()) {
                                        if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                            int skusId = shopProduct.getChoiceSkus().get(0).getId();
                                            if (skusId == spusBean.getChoiceSkus().get(0).getId()) {
                                                spusBean.setNumber(shopProduct.getNumber());
                                                inList = true;
                                                break;
                                            }
                                        } else {
                                            spusBean.setNumber(shopProduct.getNumber());
                                            inList = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (inList) {
                                    break;
                                }
                            } else {
                                for (int i = 0; i < shopProduct.getSkus().size(); i++) {
                                    int id = shopProduct.getChoiceSkus().get(0).getId();
                                    if (id == spusBean.getChoiceSkus().get(0).getId()) {
                                        spusBean.setNumber(shopProduct.getNumber());
                                        inList = true;
                                        break;
                                    }
                                }
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
                return inList;
            }


            private void reduceOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, MyViewHolder.GridViewAdapter.ViewHolder viewHolder, int section) {
                int num = spusBean.getNumber();
                int minOrderCount = getMinOrderCount(spusBean);
                if (num > 0) {
                    if (minOrderCount == spusBean.getNumber()) {
                        num -= minOrderCount;
                    } else {
                        num--;
                    }
                    spusBean.setNumber(num);
                    viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                    if (spusBean.getNumber() == 0) {
                        viewHolder.action.setVisibility(View.GONE);
                        viewHolder.add.setVisibility(View.VISIBLE);
                    }
                    if (callBackListener != null) {
                        callBackListener.updateProduct(spusBean, spusBean.getTag(), section, false);
                    } else {
                    }
                }
            }

            private void increaseOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                         ViewHolder viewHolder, int section, boolean alreadyToast) {
                int min_order_count = getMinOrderCount(spusBean);
                if (min_order_count > 1 && !alreadyToast) {
                    ToastUtils.show(context, context.getString(R.string.must_buy) +
                            min_order_count + context.getString(R.string.share_buy), Toast.LENGTH_SHORT);
                }
                int num = spusBean.getNumber();
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
                if (callBackListener != null) {
                    callBackListener.updateProduct(spusBean, spusBean.getTag(), section, true);
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

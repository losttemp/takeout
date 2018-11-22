package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.domain.multipltextview.MultiplTextView;

import java.text.Format;
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
    private View mVeiw;
    private boolean mInList;

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
            viewHolder.name = (MultiplTextView) convertView.findViewById(R.id.name);
            viewHolder.prise = (MultiplTextView) convertView.findViewById(R.id.prise);
            viewHolder.increase = (ImageView) convertView.findViewById(R.id.increase);
            viewHolder.reduce = (ImageView) convertView.findViewById(R.id.reduce);
            viewHolder.storeIndex = (MultiplTextView) convertView.findViewById(R.id.tv_store_index);
            viewHolder.shoppingNum = (MultiplTextView) convertView.findViewById(R.id.shoppingNum);
            viewHolder.add = (ImageView) convertView.findViewById(R.id.btn_add);
            viewHolder.specifications = (Button) convertView.findViewById(R.id.btn_specifications);
            viewHolder.action = (RelativeLayout) convertView.findViewById(R.id.action);
            viewHolder.addNumber = (RelativeLayout) convertView.findViewById(R.id.rl_add_number);
            viewHolder.discountPrice = (MultiplTextView) convertView.findViewById(R.id.tv_discount_price);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_describe);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = foodSpuTagsBeans.get(section).getSpus().get(position);
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans = spusBean.getSkus();
        Lg.getInstance().d(TAG, "spusBean.getName() = " + spusBean.getName());
        viewHolder.name.setText(spusBean.getName());
        viewHolder.storeIndex.setText(String.valueOf(position + 1));
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
        }
        if (spusBean.getStatus() != 0) {
            viewHolder.addNumber.setVisibility(View.GONE);
        } else {
            viewHolder.addNumber.setVisibility(View.VISIBLE);
        }
        final String pictureUrl = spusBean.getPicture();
        GlideApp.with(context)
                .load(pictureUrl)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(viewHolder.head);
        viewHolder.prise.setText("" + spusBean.getMin_price());

        if (skusBeans.size() == 1) {
            double price = skusBeans.get(0).getPrice();
            double originPrice = skusBeans.get(0).getOrigin_price();
            if (price == originPrice) {
                viewHolder.discountPrice.setVisibility(View.GONE);
            } else {
                viewHolder.discountPrice.setText(String.format("¥%1$s", "" + originPrice));
                viewHolder.discountPrice.setVisibility(View.VISIBLE);
                viewHolder.discountPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
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
                increaseOnClick(spusBean, viewHolder, section);
            }
        });
        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseOnClick(spusBean, viewHolder, section);
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVeiw = view;
                View popView = activity.getPopView(R.layout.dialog_spus_details);
                final Button addToCart = (Button) popView.findViewById(R.id.btn_add_to_cart);
                Button detailSpecifications = (Button) popView.findViewById(R.id.btn_spus_details_specifications);
                MultiplTextView spusName = (MultiplTextView) popView.findViewById(R.id.tv_spus_name);
                ImageView spusPicture = (ImageView) popView.findViewById(R.id.iv_spus);
                MultiplTextView spusPrice = (MultiplTextView) popView.findViewById(R.id.tv_spus_price);
                MultiplTextView spusEvaluate = (MultiplTextView) popView.findViewById(R.id.tv_spus_evaluate);
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
                activity.showFoodListActivityDialog(view, popView, window);
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
                spusEvaluate.setText(String.format(context.getString(R.string.evaluate), "" + spusBean.getMonth_saled()));
                spusPrice.setText(String.format("¥%1$s", "" + spusBean.getMin_price()));
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
                        increaseOnClick(spusBean, viewHolder, section);
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
            }
        });
        return convertView;
    }

    private void spcificationsOnclick(View view, final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, final ViewHolder viewHolder, final int section) {
        View popView = activity.getPopView(R.layout.dialog_spus_specifications);
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

        if (spusBean.getStatus() != 0) {
            addToCart.setVisibility(View.GONE);
            action.setVisibility(View.GONE);
        } else {

        }
        PoifoodSpusAttrsAdapter poifoodSpusAttrsAdapter = new PoifoodSpusAttrsAdapter(context, attrs, skus, spusBean,
                choiceSkus, productList);
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
                        Toast.makeText(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (spusBean.getAttrs().size() > 0) {
                    for (int i = 0; i < spusBean.getAttrs().size(); i++) {
                        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = spusBean.getAttrs().get(i).getChoiceAttrs();
                        if (choiceAttrs.size() == 0) {
                            Toast.makeText(context, context.getString(R.string.please_select_the_specifications_first), Toast.LENGTH_SHORT).show();
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
                increaseOnClick(spusBean, viewHolder, section);
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
        activity.showFoodListActivityDialog(view, popView, window);
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
                                    for (int j = 0; j < shopProduct.getSkus().size(); j++) {
                                        int skusId = shopProduct.getChoiceSkus().get(0).getId();
                                        if (skusId == spusBean.getChoiceSkus().get(i).getId()) {
                                            spusBean.setNumber(shopProduct.getNumber());
                                            inList = true;
                                            break;
                                        }
                                    }
                                    if (inList) {
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
                            if (id == spusBean.getChoiceSkus().get(i).getId()) {
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


    private void reduceOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, ViewHolder viewHolder, int section) {
        int num = spusBean.getNumber();
        if (num > 0) {
            int min_order_count = getMinOrderCount(spusBean);
            num -= min_order_count;
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

    private void increaseOnClick(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, ViewHolder viewHolder, int section) {
        int min_order_count = getMinOrderCount(spusBean);
        if (min_order_count > 1) {
//            Toast.makeText(context, "至少购" + min_order_count + "份", Toast.LENGTH_SHORT).show();
        }
        int num = spusBean.getNumber();

        num += min_order_count;
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

    class ViewHolder {
        public ImageView head;
        public MultiplTextView name;
        public MultiplTextView prise;
        public ImageView increase;
        public MultiplTextView shoppingNum;
        public ImageView reduce;
        public MultiplTextView storeIndex;
        public MultiplTextView discountPrice;
        public TextView describe;
        public ImageView add;
        public Button specifications;
        public RelativeLayout action;
        public RelativeLayout addNumber;
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
        void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String tag, int selection, boolean increase);
    }
}

package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;
import com.baidu.iov.dueros.waimai.ui.R;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;

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

    public void setCallBackListener(onCallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public PoifoodSpusListAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans, FoodListActivity activity) {
        this.context = context;
        this.foodSpuTagsBeans = foodSpuTagsBeans;
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
    public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
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
        viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = spusBean.getNumber();
                num++;
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
        });
        viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = spusBean.getNumber();
                if (num > 0) {
                    num--;
                    spusBean.setNumber(num);
                    viewHolder.shoppingNum.setText(spusBean.getNumber() + "");
                    if (callBackListener != null) {
                        callBackListener.updateProduct(spusBean, spusBean.getTag());
                    } else {
                    }
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
                        num++;
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

    class ViewHolder {
        public ImageView head;
        public TextView name;
        public TextView prise;
        public TextView increase;
        public TextView shoppingNum;
        public TextView reduce;
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

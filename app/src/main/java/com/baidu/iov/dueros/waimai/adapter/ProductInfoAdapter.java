package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.R;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ProductInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> mProductList = null;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfoBean = null;

    public ProductInfoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                        PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean) {
        mProductList = productList;
        mPoiInfoBean = poiInfoBean;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mProductList == null ? 0 : mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList == null ? 0 : mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductList == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.product_info_item, null);
            viewHolder = new ViewHolder();
            viewHolder.im_photo = convertView.findViewById(R.id.product_photo);
            viewHolder.tv_name = convertView.findViewById(R.id.product_name);
            viewHolder.tv_attrs = convertView.findViewById(R.id.product_attrs);
            viewHolder.tv_count = convertView.findViewById(R.id.product_count);
            viewHolder.tv_price = convertView.findViewById(R.id.product_price);
            viewHolder.tv_origin_price = convertView.findViewById(R.id.origin_price);
            viewHolder.tv_discounts = convertView.findViewById(R.id.product_discount);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String pictureUrl = mProductList.get(position).getPicture();
        String name = mProductList.get(position).getName();

        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeanList;
        attrsBeanList = mProductList.get(position).getAttrs();
        StringBuilder attrs = new StringBuilder();

        if (attrsBeanList.size() > 0) {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : attrsBeanList){
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean valuesBean : attrsBean.getChoiceAttrs())
                    attrs.append(valuesBean.getValue() + " ");
            }
            viewHolder.tv_attrs.setText(attrs.toString());
            viewHolder.tv_attrs.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tv_attrs.setVisibility(View.INVISIBLE);
        }


        NumberFormat nf = new DecimalFormat("#.#");
        int count = mProductList.get(position).getNumber();
        double price = mProductList.get(position).getSkus().get(0).getPrice();
        double origin_price = mProductList.get(position).getSkus().get(0).getOrigin_price();

        if (price > origin_price) {
            viewHolder.tv_origin_price.setText(String.format(mContext.getResources().getString(R.string.cost_text), nf.format(origin_price)));
            viewHolder.tv_origin_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.tv_origin_price.setVisibility(View.VISIBLE);
            viewHolder.tv_discounts.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_origin_price.setVisibility(View.INVISIBLE);
            viewHolder.tv_discounts.setVisibility(View.INVISIBLE);
        }
        Glide.with(mContext).load(pictureUrl).into(viewHolder.im_photo);
        viewHolder.tv_name.setText(name);
        viewHolder.tv_count.setText(String.format(mContext.getResources().getString(R.string.count_char), count));
        viewHolder.tv_price.setText(String.format(mContext.getResources().getString(R.string.cost_text), nf.format(price)));

        return convertView;

    }

    public class ViewHolder {

        ImageView im_photo;
        TextView tv_name;
        TextView tv_attrs;
        TextView tv_discounts;
        TextView tv_count;
        TextView tv_price;
        TextView tv_origin_price;
    }
}

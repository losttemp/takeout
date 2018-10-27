package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.ui.R;

public class ProductInfoAdapter extends BaseAdapter {

    private Context mContext;

    public ProductInfoAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.product_info_item, null);
            viewHolder = new ViewHolder();
            viewHolder.im_photo = convertView.findViewById(R.id.product_photo);
            viewHolder.tv_name = convertView.findViewById(R.id.product_name);
            viewHolder.tv_count = convertView.findViewById(R.id.product_count);
            viewHolder.tv_price = convertView.findViewById(R.id.product_price);
            viewHolder.tv_discounts = convertView.findViewById(R.id.discount_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

    public class ViewHolder {
        public ImageView im_photo;
        public TextView tv_name;
        public TextView tv_count;
        public TextView tv_price;
        public TextView tv_discounts;
    }
}

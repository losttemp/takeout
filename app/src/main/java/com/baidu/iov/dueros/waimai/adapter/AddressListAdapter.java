package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.R;

import java.util.List;

public class AddressListAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressListBean.IovBean.DataBean> mData;

    public AddressListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<AddressListBean.IovBean.DataBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_address_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_select = convertView.findViewById(R.id.img_select);
            viewHolder.tv_address = convertView.findViewById(R.id.tv_address);
            viewHolder.tv_address_type = convertView.findViewById(R.id.tv_address_type);
            viewHolder.tv_phone = convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.img_add = convertView.findViewById(R.id.img_add);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String address = mData.get(position).getAddress();
        viewHolder.tv_address.setText(address);

        //String address_type = mData.get(position).getAddressRangeTip();
        //viewHolder.tv_address_type.setText(address_type);

        String name = mData.get(position).getUser_name();
        viewHolder.tv_name.setText(name);

        String phone = mData.get(position).getUser_phone();
        viewHolder.tv_phone.setText(phone);

        return convertView;
    }


    public class ViewHolder {

        ImageView img_select;
        TextView tv_address;
        TextView tv_address_type;
        TextView tv_name;
        TextView tv_phone;
        CheckBox img_add;

    }
}

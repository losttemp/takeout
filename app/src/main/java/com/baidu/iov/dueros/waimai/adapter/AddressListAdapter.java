package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<AddressListBean.IovBean.DataBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public AddressListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<AddressListBean.IovBean.DataBean> data) {
        mData = data;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_serial;
        TextView tv_address;
        TextView tv_address_type;
        TextView tv_name;
        TextView tv_phone;
        ImageView img_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_serial = itemView.findViewById(R.id.tv_num);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_address_type = itemView.findViewById(R.id.tv_address_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            img_edit = itemView.findViewById(R.id.img_select);

            itemView.setOnClickListener(AddressListAdapter.this);
            img_edit.setOnClickListener(AddressListAdapter.this);
        }
    }

    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_address_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(AddressListAdapter.ViewHolder holder, int position) {

        try {
            holder.tv_serial.setText(String.valueOf((position + 1)));

            String address = Encryption.desEncrypt(mData.get(position).getAddress());
            holder.tv_address.setText(address);

            String address_type = mData.get(position).getType();
            if (address_type != null) {

                holder.tv_address_type.setText(address_type);
            } else {
                holder.tv_address_type.setText(mContext.getString(R.string.address_tag_other));
            }

            String name = Encryption.desEncrypt(mData.get(position).getUser_name());
            holder.tv_name.setText(name);

            String phone = Encryption.desEncrypt(mData.get(position).getUser_phone());
            holder.tv_phone.setText(phone);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
        holder.img_edit.setTag(position);

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {

        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()) {
                case R.id.img_select:
                    mOnItemClickListener.onItemClick(v, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, position);
                    break;
            }
        }
    }

}


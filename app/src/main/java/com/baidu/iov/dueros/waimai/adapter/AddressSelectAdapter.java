package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.AddressViewHolder> {

    private final Context mContext;
    private List<AddressListBean.IovBean.DataBean> mAddressList;
    private OnItemClickListener mItemClickListerner;

    public void setAddressList(List<AddressListBean.IovBean.DataBean> mAddressList) {
        this.mAddressList = mAddressList;
    }

    public void setOnItemClickListerner(OnItemClickListener mItemClickListerner) {
        this.mItemClickListerner = mItemClickListerner;
    }

    public AddressSelectAdapter(List<AddressListBean.IovBean.DataBean> addressList, Context context) {
        mAddressList = addressList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .activity_address_select_item, viewGroup, false);
        AddressViewHolder holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder viewHolder, int position) {
        AddressListBean.IovBean.DataBean dataBean = mAddressList.get(position);
        viewHolder.bindData(position, dataBean);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }


    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView num;
        private TextView details;
        private TextView name;
        private TextView phone;
        private ImageView ivType;
        private ImageView edit;
        private AddressListBean.IovBean.DataBean mDataBean;

        private AddressViewHolder(View view) {
            super(view);
            num = view.findViewById(R.id.address_select_num);
            ivType = view.findViewById(R.id.address_type);
            details = view.findViewById(R.id.address_select_des_details);
            name = view.findViewById(R.id.address_select_name);
            phone = view.findViewById(R.id.address_select_phone);
            edit = view.findViewById(R.id.address_select_edit);
            edit.setOnClickListener(this);
            view.findViewById(R.id.address_select_details_container).setOnClickListener(this);
        }

        public void bindData(int position, AddressListBean.IovBean.DataBean dataBean) {
            this.mDataBean = dataBean;
            String type = dataBean.getType();
            if (TextUtils.isEmpty(type)) {
                ivType.setImageResource(R.drawable.address_location);
            } else if (type.equals(mContext.getString(R.string.address_company))) {
                ivType.setImageResource(R.drawable.address_company);
            } else if (type.equals(mContext.getString(R.string.address_home))) {
                ivType.setImageResource(R.drawable.address_home);
            } else {
                ivType.setImageResource(R.drawable.address_location);
            }
            num.setText((position + 1) + "");
            try {
                details.setText(Encryption.desEncrypt(dataBean.getAddress()));
                name.setText(Encryption.desEncrypt(dataBean.getUser_name()));
                phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListerner != null) {
                mItemClickListerner.OnItemClick(v, mDataBean);
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean);
    }
}
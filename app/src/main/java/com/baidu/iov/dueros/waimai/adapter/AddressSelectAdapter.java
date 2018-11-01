package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

import com.baidu.iov.dueros.waimai.ui.R;

import java.util.List;

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.AddressViewHolder> {

    private final Context mContext;
    private List<AddressListBean.MeituanBean.DataBean> mAddressList;
    private OnItemClickListener mItemClickListerner;

    public void setAddressList(List<AddressListBean.MeituanBean.DataBean> mAddressList) {
        this.mAddressList = mAddressList;
    }

    public void setmItemClickListerner(OnItemClickListener mItemClickListerner) {
        this.mItemClickListerner = mItemClickListerner;
    }

    public AddressSelectAdapter(List<AddressListBean.MeituanBean.DataBean> addressList, Context context) {
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
        AddressListBean.MeituanBean.DataBean dataBean = mAddressList.get(position);
        viewHolder.bindData(dataBean);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }


    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView details;
        private TextView name;
        private TextView phone;
        private TextView des;
        private ImageView edit;
        private AddressListBean.MeituanBean.DataBean mDataBean;

        private AddressViewHolder(View view) {
            super(view);
            des = view.findViewById(R.id.address_select_des);
            details = view.findViewById(R.id.address_select_des_details);
            name = view.findViewById(R.id.address_select_name);
            phone = view.findViewById(R.id.address_select_phone);
            edit = view.findViewById(R.id.address_select_edit);
            details.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        public void bindData(AddressListBean.MeituanBean.DataBean dataBean) {
            this.mDataBean = dataBean;
            des.setText(dataBean.getName());
            details.setText(dataBean.getAddress());
            name.setText(dataBean.getName());
            phone.setText(dataBean.getPhone());
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListerner != null) {
                mItemClickListerner.OnItemClick(v,mDataBean);
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, AddressListBean.MeituanBean.DataBean dataBean);
    }
}
package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;

import java.util.List;

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.BaseViewHolderHelper> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;
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
    public BaseViewHolderHelper onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        if( position == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                    .item_footer_view, viewGroup, false);
            return new FooterViewHolder(view);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .activity_address_select_item, viewGroup, false);
        BaseViewHolderHelper holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolderHelper viewHolder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL){
            AddressViewHolder addressViewHolder = (AddressViewHolder) viewHolder;
            AddressListBean.IovBean.DataBean dataBean = mAddressList.get(position);
            addressViewHolder.bindData(position, dataBean);
        } else if (getItemViewType(position) == TYPE_FOOTER){
            FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
            footerViewHolder.addressBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAddress();
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == mAddressList.size()) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mAddressList.size() + 1;
    }

    public void addAddress(){

    }


    class AddressViewHolder extends BaseViewHolderHelper implements View.OnClickListener {
        private TextView num;
        private TextView details;
        private TextView name;
        private TextView phone;
        private TextView tvType;
        private ImageView ivType;
        private ImageView edit;
        private AddressListBean.IovBean.DataBean mDataBean;

        private AddressViewHolder(View view) {
            super(view);
            num = view.findViewById(R.id.address_select_num);
//            ivType = view.findViewById(R.id.address_type);
            tvType = view.findViewById(R.id.tv_address_type);
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
//            if (TextUtils.isEmpty(type)) {
//                ivType.setImageResource(R.drawable.address_other);
//            }else if (type.equals(mContext.getString(R.string.address_destination))) {
//                ivType.setImageResource(R.drawable.address_location);
//            } else if (type.equals(mContext.getString(R.string.address_company))) {
//                ivType.setImageResource(R.drawable.address_company);
//            } else if (type.equals(mContext.getString(R.string.address_home))) {
//                ivType.setImageResource(R.drawable.address_home);
//            } else {
//                ivType.setImageResource(R.drawable.address_other);
//            }
           if (TextUtils.isEmpty(type)){
               tvType.setText(mContext.getString(R.string.address_tag_other));
               tvType.setBackgroundResource(R.drawable.tag_bg);
           }else if (mContext.getString(R.string.address_home).equals(type)) {
                tvType.setBackgroundResource(R.drawable.tag_bg_green);
                tvType.setText(type);

           } else if (mContext.getString(R.string.address_company).equals(type)) {
                tvType.setBackgroundResource(R.drawable.tag_bg_blue);
                tvType.setText(type);

           } else if (mContext.getString(R.string.address_tag_other).equals(type)) {
                tvType.setBackgroundResource(R.drawable.tag_bg);
                tvType.setText(type);

           } else if (mContext.getString(R.string.address_destination).equals(type)){
                tvType.setText(mContext.getString(R.string.address_tag_other));
                tvType.setBackgroundResource(R.drawable.tag_bg);
           }
            num.setText((position + 1) + "");
            name.setText("");
            phone.setText("");
            try {
                details.setText(Encryption.desEncrypt(dataBean.getAddress()));
                if (!TextUtils.isEmpty(dataBean.getUser_name())) {
                    name.setText(Encryption.desEncrypt(dataBean.getUser_name()));
                }
                if (!TextUtils.isEmpty(dataBean.getUser_phone())) {
                    phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
                }
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

    class BaseViewHolderHelper  extends RecyclerView.ViewHolder{
        public BaseViewHolderHelper (@NonNull View itemView) {
            super(itemView);
        }
    }
    class FooterViewHolder extends BaseViewHolderHelper{
        Button addressBtn;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            addressBtn = (Button)itemView.findViewById(R.id.item_address_select_add);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean);
    }
}
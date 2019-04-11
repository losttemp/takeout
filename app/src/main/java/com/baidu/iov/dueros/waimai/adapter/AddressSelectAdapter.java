package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.VoiceTouchUtils;

import java.util.List;

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.BaseViewHolderHelper> {

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout
                .activity_address_select_item, viewGroup, false);
        BaseViewHolderHelper holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolderHelper viewHolder, int position) {
        AddressViewHolder addressViewHolder = (AddressViewHolder) viewHolder;
        AddressListBean.IovBean.DataBean dataBean = mAddressList.get(position);
        addressViewHolder.bindData(position, dataBean);
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public void addAddress() {

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
        private View item_content;

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
            item_content = view.findViewById(R.id.address_select_details_container);
            item_content.setOnClickListener(this);
        }

        public void bindData(int position, AddressListBean.IovBean.DataBean dataBean) {
            this.mDataBean = dataBean;
            num.setText((position + 1) + "");
            name.setText("");
            phone.setText("");
            String detail = "";
            try {
                detail = Encryption.desEncrypt(dataBean.getAddress());
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) details.getLayoutParams();
                if (detail.length() > 16) {
                    lp.width = 586;
                } else {
                    lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                details.setLayoutParams(lp);
                details.setText(detail);
                if (!TextUtils.isEmpty(dataBean.getUser_name())) {
                    String nameText = Encryption.desEncrypt(dataBean.getUser_name());
                    LinearLayout.LayoutParams namelp = (LinearLayout.LayoutParams) name.getLayoutParams();
                    if (nameText.length() > 10) {
                        namelp.width = 300;
                    } else {
                        namelp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    }
                    name.setLayoutParams(namelp);
                    name.setText(nameText);
                }
                if (!TextUtils.isEmpty(dataBean.getUser_phone())) {
                    phone.setText(Encryption.desEncrypt(dataBean.getUser_phone()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            edit.setVisibility(View.VISIBLE);
            String type = dataBean.getType();
            if (TextUtils.isEmpty(type)) {
                tvType.setBackgroundResource(R.drawable.tag_bg);
                VoiceTouchUtils.setVoicesTouchSupport(item_content, String.format(mContext.getString(R.string.choose_an_address), position + 1));
                VoiceTouchUtils.setVoiceTouchTTSSupport(item_content, String.format(mContext.getString(R.string.harvest_address), detail));
            } else if (mContext.getString(R.string.address_home).equals(type)) {
                VoiceTouchUtils.setVoicesTouchSupport(item_content, String.format(mContext.getString(R.string.send_to_home), position + 1));
                VoiceTouchUtils.setVoiceTouchTTSSupport(item_content, String.format(mContext.getString(R.string.harvest_address), detail));
                tvType.setBackgroundResource(R.drawable.tag_bg_green);
            } else if (mContext.getString(R.string.address_company).equals(type)) {
                VoiceTouchUtils.setVoicesTouchSupport(item_content, String.format(mContext.getString(R.string.send_to_company), position + 1));
                VoiceTouchUtils.setVoiceTouchTTSSupport(item_content, String.format(mContext.getString(R.string.harvest_address), detail));
                tvType.setBackgroundResource(R.drawable.tag_bg_blue);
            } else if (mContext.getString(R.string.address_tag_other).equals(type)) {
                tvType.setBackgroundResource(R.drawable.tag_bg);
                VoiceTouchUtils.setVoicesTouchSupport(item_content, String.format(mContext.getString(R.string.choose_an_address), position + 1));
                VoiceTouchUtils.setVoiceTouchTTSSupport(item_content, String.format(mContext.getString(R.string.harvest_address), detail));
            } else if (mContext.getString(R.string.address_destination).equals(type)) {
                VoiceTouchUtils.setVoicesTouchSupport(item_content, String.format(mContext.getString(R.string.send_to_destination), position + 1));
                VoiceTouchUtils.setVoiceTouchTTSSupport(item_content, String.format(mContext.getString(R.string.harvest_address), detail));
                tvType.setBackgroundResource(R.drawable.tag_bg_mudidi);
                edit.setVisibility(View.GONE);
            }
            tvType.setText(!TextUtils.isEmpty(type) ? type : mContext.getString(R.string.address_tag_other));
            VoiceTouchUtils.setItemVoicesTouchSupport(edit, position, position == 0 ? R.array.address_edit_other : R.array.address_edit);
            VoiceTouchUtils.setVoiceTouchTTSSupport(edit, mContext.getString(R.string.start_edit_address_success_text));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListerner != null) {
                mItemClickListerner.OnItemClick(v, mDataBean);
            }
        }
    }

    class BaseViewHolderHelper extends RecyclerView.ViewHolder {
        public BaseViewHolderHelper(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean);
    }
}
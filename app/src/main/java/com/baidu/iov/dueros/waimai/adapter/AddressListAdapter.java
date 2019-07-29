package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.VoiceTouchUtils;

import java.util.List;

import static com.baidu.iov.dueros.waimai.utils.ResUtils.getResources;

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AddressListBean.IovBean.DataBean> mData;
    private OnItemClickListener mOnItemClickListener;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public AddressListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<AddressListBean.IovBean.DataBean> data) {
        mData = data;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(1);
//        notifyDataSetChanged();
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_serial;
        TextView tv_address;
        TextView tv_address_type;
        TextView tv_name;
        TextView tv_phone;
        ImageView img_edit;
        RelativeLayout address_item;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_serial = itemView.findViewById(R.id.tv_num);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_address_type = itemView.findViewById(R.id.tv_address_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            img_edit = itemView.findViewById(R.id.img_select);
            address_item = itemView.findViewById(R.id.address_item);

            itemView.setOnClickListener(this);
            img_edit.setOnClickListener(this);
            address_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition() - 1;
            if (mOnItemClickListener != null && position >= 0) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_address_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) return;
        final int realPosition = getRealPosition(holder);
        if (holder instanceof ViewHolder) {
            try {
                ((ViewHolder) holder).tv_serial.setText(String.valueOf(realPosition + 1));
                ((ViewHolder) holder).img_edit.setVisibility(View.VISIBLE);
                String address = Encryption.desEncrypt(mData.get(realPosition).getAddress());
                ((ViewHolder) holder).tv_address.setText(address);
                String address_type = mData.get(realPosition).getType();
                if (mContext.getString(R.string.address_destination).equals(address_type)) {
                    ((ViewHolder) holder).tv_address_type.setBackgroundResource(R.drawable.tag_bg_mudidi);
                    ((ViewHolder) holder).tv_address_type.setText(address_type);
                    ((ViewHolder) holder).img_edit.setVisibility(View.GONE);
                    if (MyApplicationAddressBean.USER_NAMES.get(0) != null && MyApplicationAddressBean.USER_PHONES.get(0) != null) {
                        ((ViewHolder) holder).tv_name.setText(MyApplicationAddressBean.USER_NAMES.get(0));
                        ((ViewHolder) holder).tv_phone.setText(MyApplicationAddressBean.USER_PHONES.get(0));
                    }
                } else if (mContext.getString(R.string.address_home).equals(address_type)) {
                    ((ViewHolder) holder).tv_address_type.setBackgroundResource(R.drawable.tag_bg_green);
                    ((ViewHolder) holder).tv_address_type.setText(address_type);

                } else if (mContext.getString(R.string.address_company).equals(address_type)) {
                    ((ViewHolder) holder).tv_address_type.setBackgroundResource(R.drawable.tag_bg_blue);
                    ((ViewHolder) holder).tv_address_type.setText(address_type);

                } else if (mContext.getString(R.string.address_tag_other).equals(address_type)) {
                    ((ViewHolder) holder).tv_address_type.setBackgroundResource(R.drawable.tag_bg);
                    ((ViewHolder) holder).tv_address_type.setText(address_type);

                } else {
                    ((ViewHolder) holder).tv_address_type.setText(mContext.getString(R.string.address_tag_other));
                    ((ViewHolder) holder).tv_address_type.setBackgroundResource(R.drawable.tag_bg);
                }

                ((ViewHolder) holder).tv_name.setText("");
                ((ViewHolder) holder).tv_phone.setText("");
                if (!TextUtils.isEmpty(mData.get(realPosition).getUser_name())) {
                    String name = Encryption.desEncrypt(mData.get(realPosition).getUser_name());
                    ((ViewHolder) holder).tv_name.setText(name);
                }
                if (!TextUtils.isEmpty(mData.get(realPosition).getUser_phone())) {
                    String phone = Encryption.desEncrypt(mData.get(realPosition).getUser_phone());
                    ((ViewHolder) holder).tv_phone.setText(phone);
                }

                holder.itemView.setTag(realPosition);
                String ttsAddress = Encryption.desEncrypt(mData.get(realPosition).getAddress());
                String str = String.format(mContext.getString(R.string.delivery_an_address), ttsAddress);
                VoiceTouchUtils.setItemVoicesTouchSupport(((ViewHolder) holder).itemView, realPosition, mContext.getString(R.string.choose_an_address));
                VoiceTouchUtils.setVoiceTouchTTSSupport(((ViewHolder) holder).itemView, str);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mData.get(realPosition).getCanShipping() == 1) {
                ((ViewHolder) holder).tv_address.setTextColor(mContext.getResources().getColor(R.color.white));
                ((ViewHolder) holder).tv_address_type.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.itemView.setBackground(null);
            } else {
                ((ViewHolder) holder).tv_address.setTextColor(0x99ffffff);
                ((ViewHolder) holder).tv_address_type.setTextColor(0x99ffffff);
                ((ViewHolder) holder).tv_name.setTextColor(0x99ffffff);
            }

            ((ViewHolder) holder).img_edit.setTag(realPosition);

            VoiceTouchUtils.setItemVoicesTouchSupport(((ViewHolder) holder).img_edit, realPosition, R.array.address_edit);
            VoiceTouchUtils.setVoiceTouchTTSSupport(((ViewHolder) holder).img_edit, mContext.getString(R.string.start_edit_address_success_text));
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {

        if (mData != null) {
            return mHeaderView == null ? mData.size() : mData.size() + 1;

        } else {

            return 0;
        }

    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

}


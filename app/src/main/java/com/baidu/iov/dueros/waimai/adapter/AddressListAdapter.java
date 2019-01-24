package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.Encryption;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

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
        notifyDataSetChanged();
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_serial;
        TextView tv_address;
        TextView tv_address_type;
        TextView tv_name;
        ImageView img_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            tv_serial = itemView.findViewById(R.id.tv_num);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_address_type = itemView.findViewById(R.id.tv_address_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_edit = itemView.findViewById(R.id.img_select);

            itemView.setOnClickListener(AddressListAdapter.this);
            img_edit.setOnClickListener(AddressListAdapter.this);
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

                String address = Encryption.desEncrypt(mData.get(realPosition).getAddress());
                ((ViewHolder) holder).tv_address.setText(address);
                if (address.length() > 17) {
                    ((ViewHolder) holder).tv_address.setWidth((int) mContext.getResources().getDimension(R.dimen.px550dp));
                }
                String address_type = mData.get(realPosition).getType();

                if (mContext.getString(R.string.address_home).equals(address_type)) {
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
                String name = Encryption.desEncrypt(mData.get(realPosition).getUser_name());
                String phone = Encryption.desEncrypt(mData.get(realPosition).getUser_phone());
                ((ViewHolder) holder).tv_name.setText(name + " " + phone);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mData.get(realPosition).getCanShipping() == 1) {
                holder.itemView.setBackground(null);
            } else {
                ((ViewHolder) holder).tv_address.setTextColor(0x99ffffff);
                ((ViewHolder) holder).tv_address_type.setTextColor(0x99ffffff);
                ((ViewHolder) holder).tv_name.setTextColor(0x99ffffff);
            }

            ((ViewHolder) holder).img_edit.setTag(realPosition);

            holder.itemView.setTag(realPosition);

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


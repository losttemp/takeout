package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListBean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class CYBChangeCityGridViewAdapter extends BaseAdapter {

    private List<CityListBean.HotBean> list;
    private Context context;
    private LayoutInflater inflater;

    public CYBChangeCityGridViewAdapter(Context context, List<CityListBean.HotBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_cyb_change_city, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.city_hot.setText(list.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        private TextView city_hot;

        public ViewHolder(View view) {
            city_hot = view.findViewById(R.id.city_hot);
        }
    }
}

package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.PoifoodSpusTagsBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.view.GoodsViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by ubuntu on 18-11-3.
 */

public class PoifoodSpusTagsAdapter extends BaseAdapter {

    private final Context context;
    private List<PoifoodSpusTagsBean> poifoodSpusTagsBeans;

    public PoifoodSpusTagsAdapter(Context context, List<PoifoodSpusTagsBean> poifoodSpusTagsBeans) {
        this.context = context;
        this.poifoodSpusTagsBeans = poifoodSpusTagsBeans;
    }

    @Override
    public int getCount() {
        return poifoodSpusTagsBeans == null ? 0 : poifoodSpusTagsBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.categorize_item, null);
            viewHolder.foodSpuTagsBeanName = (TextView) convertView.findViewById(R.id.mainitem_txt);
            viewHolder.number = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.foodSpuTagsBeanName.setText(poifoodSpusTagsBeans.get(position).getFoodSpuTagsBeanName());
        if (poifoodSpusTagsBeans.get(position).getNumber() != 0) {
            viewHolder.number.setText("" + poifoodSpusTagsBeans.get(position).getNumber());
        }
        return convertView;
    }

    private final class ViewHolder {
        TextView foodSpuTagsBeanName;
        TextView number;
    }
}

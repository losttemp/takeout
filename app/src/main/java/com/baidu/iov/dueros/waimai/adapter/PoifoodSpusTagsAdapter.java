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
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.view.GoodsViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by ubuntu on 18-11-3.
 */

public class PoifoodSpusTagsAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> foodSpuTagsBeanNames;
    private final List<Map<Integer, Integer>> num;

    public PoifoodSpusTagsAdapter(Context context, List<String> foodSpuTagsBeanNames, List<Map<Integer, Integer>> num) {
        this.context = context;
        this.foodSpuTagsBeanNames = foodSpuTagsBeanNames;
        this.num = num;
    }

    @Override
    public int getCount() {
        return foodSpuTagsBeanNames == null ? 0 : foodSpuTagsBeanNames.size();
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
        viewHolder.foodSpuTagsBeanName.setText(foodSpuTagsBeanNames.get(position));
       /* if (num.get(position).containsKey(position)) { TODO
            viewHolder.number.setText(String.valueOf(num.get(position).get(position)));
        }*/
        return convertView;
    }

    private final class ViewHolder {
        TextView foodSpuTagsBeanName;
        TextView number;
    }
}

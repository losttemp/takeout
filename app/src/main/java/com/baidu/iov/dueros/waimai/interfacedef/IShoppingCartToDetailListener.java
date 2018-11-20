package com.baidu.iov.dueros.waimai.interfacedef;

import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;

/**
 * Created by ubuntu on 18-10-22.
 */

public interface IShoppingCartToDetailListener {
    void onUpdateDetailList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product, String type, int section, boolean increase);

    void onRemovePriduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product);
}

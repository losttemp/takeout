package com.baidu.iov.dueros.waimai.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusListAdapter;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusTagsAdapter;
import com.baidu.iov.dueros.waimai.bean.PoifoodSpusTagsBean;
import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.model.IPoifoodListModel;
import com.baidu.iov.dueros.waimai.model.PoifoodListModel;
import com.baidu.iov.dueros.waimai.net.entity.request.ArriveTimeReqBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderOwnerReq;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewJsonBean;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderPreviewReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.ArriveTimeBean;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderPreviewBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.ui.AddressSelectActivity;
import com.baidu.iov.dueros.waimai.ui.FoodListActivity;
import com.baidu.iov.dueros.waimai.ui.HomeActivity;
import com.baidu.iov.dueros.waimai.ui.TakeawayLoginActivity;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.faceos.client.GsonUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_NO;
import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_SELECT;
import static com.baidu.iov.dueros.waimai.utils.StandardCmdClient.CMD_YES;

/**
 * Created by ubuntu on 18-10-18.
 */

public class PoifoodListPresenter extends Presenter<PoifoodListPresenter.PoifoodListUi> {
    private static final String TAG = PoifoodListPresenter.class.getSimpleName();
    private IPoifoodListModel mPoifoodListModel;

    @Override
    public void onCommandCallback(String cmd, String extra) {
        if (getUi() == null) {
            return;
        }

        switch (cmd) {
            case CMD_YES:
                getUi().sureOrder();
                break;
            case CMD_SELECT:
                if (Integer.parseInt(extra) >= 1) {
                    getUi().selectListItem(Integer.parseInt(extra) - 1);
                }
                break;
            case StandardCmdClient.CMD_NEXT:
                getUi().nextPage(true);
                break;
            case StandardCmdClient.CMD_PRE:
                getUi().nextPage(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void registerCmd(Context context) {
        if (null != mStandardCmdClient) {
            ArrayList<String> cmdList = new ArrayList<String>();
            cmdList.add(CMD_NO);
            cmdList.add(CMD_SELECT);
            cmdList.add(CMD_YES);
            cmdList.add(StandardCmdClient.CMD_NEXT);
            cmdList.add(StandardCmdClient.CMD_PRE);
            mStandardCmdClient.registerCmd(context, cmdList, mVoiceCallback);
        }
    }

    @Override
    public void unregisterCmd(Context context) {
        if (null != mStandardCmdClient) {
            mStandardCmdClient.unregisterCmd(context, mVoiceCallback);
        }
    }

    public PoifoodListPresenter() {
        mPoifoodListModel = new PoifoodListModel();
    }

    @Override
    public void onUiReady(PoifoodListUi ui) {
        super.onUiReady(ui);
        mPoifoodListModel.onReady();
    }

    @Override
    public void onUiUnready(PoifoodListUi ui) {
        super.onUiUnready(ui);
        mPoifoodListModel.onDestroy();
    }

    public void requestPoifoodList(ArrayMap<String, String> map) {
        mPoifoodListModel.requestPoifoodList(map, new RequestCallback<PoifoodListBean>() {

            @Override
            public void onSuccess(PoifoodListBean data) {
                if (getUi() != null) {

                    getUi().onPoifoodListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {

                    getUi().onPoifoodListError(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestPoifoodList getLogid: " + id);
            }
        });
    }

    public void requestPoidetailinfo(ArrayMap<String, String> map) {
        mPoifoodListModel.requestPoidetailinfo(map, new RequestCallback<PoidetailinfoBean>() {

            @Override
            public void onSuccess(PoidetailinfoBean data) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onPoidetailinfoError(msg);
                }

            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestPoidetailinfo getLogid: " + id);
            }
        });
    }

    public void requestOrderPreview(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList,
                                    PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean, int delivery_time) {
        String payload = onCreatePayLoadJson(spusBeanList, poiInfoBean, delivery_time);
        OrderPreviewReqBean orderPreviewReqBean = new OrderPreviewReqBean();
        orderPreviewReqBean.setPayload(Encryption.encrypt(payload));
        mPoifoodListModel.requestOrderPreview(orderPreviewReqBean, new RequestCallback<OrderPreviewBean>() {
            @Override
            public void onSuccess(OrderPreviewBean data) {
                if (getUi() != null) {
                    getUi().onOrderPreviewSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (getUi() != null) {
                    getUi().onOrderPreviewFailure(msg);
                }
            }

            @Override
            public void getLogid(String id) {

            }
        });
    }

    private String onCreatePayLoadJson(List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList,
                                       PoifoodListBean.MeituanBean.DataBean.PoiInfoBean poiInfoBean, int delivery_time) {
        OrderPreviewJsonBean orderPreviewJsonBean = new OrderPreviewJsonBean();
        OrderPreviewJsonBean.WmOrderingListBean wmOrderingListBean = new OrderPreviewJsonBean.WmOrderingListBean();
        wmOrderingListBean.setWm_poi_id(poiInfoBean.getWm_poi_id());
        wmOrderingListBean.setDelivery_time(delivery_time);
        wmOrderingListBean.setPay_type(2);

        List<OrderPreviewJsonBean.WmOrderingListBean.FoodListBean> foodListBeans = new ArrayList<>();
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean : spusBeanList) {
            OrderPreviewJsonBean.WmOrderingListBean.FoodListBean foodListBean = new OrderPreviewJsonBean.WmOrderingListBean.FoodListBean();

            List<Long> food_spu_attr_ids = new ArrayList<>();
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : spusBean.getAttrs()) {
                if (attrsBean.getChoiceAttrs() != null) {
                    long id = attrsBean.getChoiceAttrs().get(0).getId();
                    food_spu_attr_ids.add(id);
                }
            }
            foodListBean.setFood_spu_attr_ids(food_spu_attr_ids);
            foodListBean.setCount(spusBean.getNumber());
            if (spusBean.getChoiceSkus() != null) {
                foodListBean.setWm_food_sku_id(spusBean.getChoiceSkus().get(0).getId());
            } else {
                foodListBean.setWm_food_sku_id(spusBean.getSkus().get(0).getId());
            }
            foodListBeans.add(foodListBean);
        }
        wmOrderingListBean.setFood_list(foodListBeans);
        orderPreviewJsonBean.setWm_ordering_list(wmOrderingListBean);

        OrderPreviewJsonBean.WmOrderingUserBean wmOrderingUserBean = new OrderPreviewJsonBean.WmOrderingUserBean();
        orderPreviewJsonBean.setWm_ordering_user(wmOrderingUserBean);
        return GsonUtil.toJson(orderPreviewJsonBean);
    }

    public void requestCheckOrderOwner(long order_id) {
        OrderOwnerReq req = new OrderOwnerReq();
        req.setOrder_id(order_id);
        mPoifoodListModel.requestCheckOrderOwner(req, new RequestCallback<OrderOwnerBean>() {

            @Override
            public void onSuccess(OrderOwnerBean data) {
                if (null != getUi()) {
                    getUi().onCheckOrderOwnerSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {

                if (null != getUi()) {
                    getUi().onCheckOrderOwnerError(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requestArriveTimeData getLogid: " + id);
            }
        });

    }

    public void requesLogout() {

        mPoifoodListModel.requesLogout(new RequestCallback<LogoutBean>() {
            @Override
            public void onSuccess(LogoutBean data) {
                if (getUi() != null) {
                    getUi().onLogoutSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUi() != null) {
                    getUi().onLogoutError(msg);
                }
            }

            @Override
            public void getLogid(String id) {
                Lg.getInstance().d(TAG, "requesLogout getLogid: " + id);
            }
        });
    }


    public interface PoifoodListUi extends Ui {
        void onLogoutSuccess(LogoutBean data);

        void onLogoutError(String msg);

        void onPoifoodListSuccess(PoifoodListBean data);

        void onPoidetailinfoSuccess(PoidetailinfoBean data);

        void onOrderPreviewSuccess(OrderPreviewBean data);

        void onArriveTimeSuccess(ArriveTimeBean data);

        void onOrderPreviewFailure(String msg);

        void onPoifoodListError(String error);

        void onPoidetailinfoError(String error);

        void onArriveTimeError(String error);

        void sureOrder();

        void selectListItem(int i);

        void nextPage(boolean isNextPage);

        void onCheckOrderOwnerSuccess(OrderOwnerBean orderOwnerBean);

        void onCheckOrderOwnerError(String error);
    }

    /**
     * 再来一单时 填充商品的规格
     */
    public void SetAttrsAndSkus(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrs,
                                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skus,
                                List<String> attrIds, List<String> attrValues, String spec) {
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusList;
        for (int i = 0; i < attrIds.size(); i++) {
            String attrId = attrIds.get(i);
            //科学计数法转成数字
            if (attrId.contains("E")) {
                BigDecimal bd1 = new BigDecimal(attrId);
                attrId = bd1.toPlainString();
            }
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean : attrs) {
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean valuesBean : attrsBean.getValues()) {
                    if (attrId.equals(String.valueOf(valuesBean.getId()))) {
                        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrsList = new ArrayList<>();
                        PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean attrValuesBean = new PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean();
                        attrValuesBean.setId(valuesBean.getId());
                        attrValuesBean.setValue(valuesBean.getValue());
                        choiceAttrsList.add(attrValuesBean);
                        attrsBean.setChoiceAttrs(choiceAttrsList);
                    }
                }
            }
        }
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean skusBean : skus) {
            if (!TextUtils.isEmpty(spec) && spec.equals(skusBean.getSpec())) {
                List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkusList = spusBean.getChoiceSkus();
                if (choiceSkusList == null) {
                    skusList = new ArrayList<>();
                    skusList.add(skusBean);
                    spusBean.setChoiceSkus(skusList);
                } else {
                    choiceSkusList.clear();
                    choiceSkusList.add(skusBean);
                }
            }
        }
    }

    /**
     * 设置一级分类数量
     */
    public void refreshSpusTagNum(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                  List<PoifoodSpusTagsBean> poifoodSpusTagsBeans,
                                  List<String> tags,
                                  PoifoodSpusTagsAdapter adapter,
                                  boolean increase, boolean firstAdd, boolean remove) {
        for (int i = 0; i < poifoodSpusTagsBeans.size(); i++) {
            if (Integer.parseInt(spusBean.getTag()) == poifoodSpusTagsBeans.get(i).getTag()
                    && !tags.contains(spusBean.getTag())) {
                Integer number = poifoodSpusTagsBeans.get(i).getNumber();
                int minOrderCount = getMinOrderCount(spusBean);
                if (increase) {
                    if (firstAdd && minOrderCount > 1) {
                        number += minOrderCount;
                    } else {
                        number++;
                    }
                } else {
                    if (remove) {
                        number -= spusBean.getNumber();
                    } else {
                        if (minOrderCount > 1 && Constant.MIN_COUNT) {
                            number -= minOrderCount;
                        } else {
                            number--;
                        }
                    }
                    if (number < 0) number = 0;
                    if (number == 0) {
                        tags.add(spusBean.getTag());
                    }
                }
                poifoodSpusTagsBeans.get(i).setNumber(number);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取最小数量
     */
    public int getMinOrderCount(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean) {
        int min_order_count = 1;
        if (spusBean.getSkus() != null) {
            if (spusBean.getSkus().size() > 1 && spusBean.getChoiceSkus() != null) {
                min_order_count = spusBean.getChoiceSkus().get(0).getMin_order_count();
            } else {
                min_order_count = spusBean.getSkus().get(0).getMin_order_count();
            }
        }
        return min_order_count;
    }

    /**
     * 优惠信息
     */
    public List<String> getDiscountList(Context context, List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts, boolean allShow) {
        List<String> list = new ArrayList<>();
        List<String> discountList = new ArrayList<>();
        for (PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean bean : discounts) {
            String[] name = bean.getInfo().split(";");
            for (int i = 0; i < name.length; i++) {
                if (!name[i].startsWith(context.getString(R.string.ten)) && !name[i].contains(context.getString(R.string.cash_coupon))) {
                    list.add(name[i]);
                }
            }
        }
        if (list.size() > 3 && !allShow) {
            discountList.add(list.get(0));
            discountList.add(list.get(1));
            discountList.add(list.get(2));
            return discountList;
        } else {
            return list;
        }
    }

    /**
     * 购物车有同一件商品时移除掉
     */
    public void judgeTheSameThing(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                  List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList, boolean mOneMoreOrder) {
        if (mOneMoreOrder) {
            for (int i = 0; i < productList.size(); i++) {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct = productList.get(i);
                if (shopProduct.getId() == spusBean.getId()) {
                    boolean isFood = true;
                    for (int a = 0; a < spusBean.getAttrs().size(); a++) {
                        if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean shopBean = shopProduct.getAttrs().get(a);
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean attrsBean = spusBean.getAttrs().get(a);
                            //判断是否为同一规格
                            if (shopBean.getChoiceAttrs() != null && shopBean.getChoiceAttrs().size() > 0 &&
                                    attrsBean.getChoiceAttrs() != null && attrsBean.getChoiceAttrs().size() > 0 &&
                                    shopBean.getChoiceAttrs().get(0).getId() != attrsBean.getChoiceAttrs().get(0).getId()) {
                                isFood = false;
                                break;
                            }
                        }
                    }
                    //规格
                    for (int b = 0; b < spusBean.getChoiceSkus().size(); b++) {
                        if (shopProduct.getChoiceSkus() != null && shopProduct.getChoiceSkus().size() > 0) {
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean shopBean = shopProduct.getChoiceSkus().get(b);
                            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean skusBean = spusBean.getChoiceSkus().get(b);
                            if (shopBean.getId() != skusBean.getId()) {
                                isFood = false;
                                break;
                            }
                        }
                    }
                    if (isFood) {
                        productList.remove(shopProduct);
                        i--;
                    }
                }
            }
        }
    }

    /**
     * 设置购物车数量
     */
    public void setProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                           List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList, boolean mOneMoreOrder, boolean increase) {
        if (spusBean.getNumber() == 0) {
            productList.remove(spusBean);
        } else {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                if (spusBean.equals(shopProduct)) {
                    if (mOneMoreOrder) {
                        shopProduct.setNumber(spusBean.getNumber());
                        Lg.getInstance().e(TAG, spusBean.getNumber() + "Number");
                    } else {
                        if (increase) {
                            shopProduct.setNumber(shopProduct.getNumber() + 1);
                        } else {
                            if (shopProduct.getNumber() == getMinOrderCount(spusBean)) {
                                shopProduct.setNumber(0);
                            } else {
                                shopProduct.setNumber(shopProduct.getNumber() - 1);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 判断购物车内的商品
     */
    public void setOneMoreOrderProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                       List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList,
                                       Map<Integer, String> cache, String groupTag, boolean mOneMoreOrder, boolean increase) {
        if (spusBean.getNumber() == 0) {
            productList.remove(spusBean);
        } else {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                if (spusBean.equals(shopProduct)) {
                    if (mOneMoreOrder) {
                        if (spusBean.getSkus() != null && spusBean.getSkus().size() > 0
                                && spusBean.getSkus().get(0).getRestrict() > 0) {
                            //确保商品在同一分类下只设置一次数量
                            if (cache.get(spusBean.getId()).equals(groupTag)) {
                                shopProduct.setNumber(shopProduct.getNumber() + 1);
                                spusBean.setNumber(shopProduct.getNumber());
                            } else {
                                spusBean.setNumber(shopProduct.getNumber());
                            }
                        } else {
                            shopProduct.setNumber(spusBean.getNumber());
                        }
                        Lg.getInstance().e(TAG, spusBean.getNumber() + "Number");
                    } else {
                        if (increase) {
                            shopProduct.setNumber(shopProduct.getNumber() + 1);
                        } else {
                            if (shopProduct.getNumber() == getMinOrderCount(spusBean)) {
                                shopProduct.setNumber(0);
                            } else {
                                shopProduct.setNumber(shopProduct.getNumber() - 1);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * 语音选择
     *
     * @param mContext         mContext
     * @param i                position
     * @param foodSpuTagsBeans foodSpuTagsBeans
     * @param mSpusList        mSpusList
     */
    public void selectDuerOSItem(Context mContext, int i, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans
            , RecyclerView mSpusList) {
        int flag = i + 1;
        int section = 0;
        int position = 0;
        boolean ok = true;
        while (ok) {
            int oldFlag = flag;
            flag -= foodSpuTagsBeans.get(section).getSpus().size();
            if (flag <= 0 && section == 0) {
                position = i;
                ok = false;
                continue;
            }
            if (flag <= 0) {
                position = oldFlag - 1;
                ok = false;
                continue;
            }
            section++;
        }

        LinearLayoutManager manager = (LinearLayoutManager) mSpusList.getLayoutManager();
        if (null != manager) {
            int firstItemPosition = manager.findFirstVisibleItemPosition();
            int lastItemPosition = manager.findLastVisibleItemPosition();

            if (firstItemPosition <= section && lastItemPosition >= section) {
                View view = mSpusList.getChildAt(section - firstItemPosition);
                if (null != mSpusList.getChildViewHolder(view)) {
                    PoifoodSpusListAdapter.MyViewHolder viewHolder = (PoifoodSpusListAdapter.MyViewHolder) mSpusList.getChildViewHolder(view);
                    RecyclerView recyclerView = viewHolder.getRecyclerView();
                    LinearLayoutManager m = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (null != m) {
                        int f = m.findFirstVisibleItemPosition();
                        int l = m.findLastVisibleItemPosition();
                        if (f <= position && l >= position) {
                            View v = recyclerView.getChildAt(position - f);
                            if (null != recyclerView.getChildViewHolder(v)) {
                                PoifoodSpusListAdapter.MyViewHolder.GridViewAdapter.ViewHolder holder = (PoifoodSpusListAdapter.MyViewHolder.GridViewAdapter.ViewHolder) recyclerView.getChildViewHolder(v);
                                View canNotBuy = holder.itemView.findViewById(R.id.ll_not_buy_time);
                                TextView tv_sold_out = holder.itemView.findViewById(R.id.tv_sold_out);
                                //判断是否卖完
                                if (tv_sold_out.getVisibility() == View.VISIBLE
                                        && (mContext.getString(R.string.sold_out).equals(tv_sold_out.getText().toString()) ||
                                        mContext.getString(R.string.looting).equals(tv_sold_out.getText().toString()))) {
                                    ToastUtils.show(mContext, mContext.getString(R.string.looting), Toast.LENGTH_SHORT);
                                    return;
                                }
                                //非可售时间
                                if (canNotBuy.getVisibility() == View.VISIBLE) {
                                    ToastUtils.show(mContext, mContext.getString(R.string.not_buy_time), Toast.LENGTH_SHORT);
                                    return;
                                }
                                holder.autoClick(position);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 提示用户再来一单的账号不同
     *
     * @param mContext
     */
    public void startCheckOrderOwnerDialog(final Context mContext) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.permission_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        Button btn_sure = layout.findViewById(R.id.to_setting);
        Button btn_cancel = layout.findViewById(R.id.i_know);
        TextView textView = layout.findViewById(R.id.persion_hint);
        TextView textViewTwo = layout.findViewById(R.id.persion_hint_two);
        textView.setText("下单订单账号与当前登录的美团账号不");
        textViewTwo.setText("一致，是否需切换登录账号");
        btn_sure.setText(mContext.getString(R.string.ok));
        btn_cancel.setText(mContext.getString(R.string.close));
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setWindowAnimations(R.style.Dialog);
        dialog.show();
        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setLayout((int) mContext.getResources().getDimension(R.dimen.px912dp), (int) mContext.getResources().getDimension(R.dimen.px516dp));
            window.setBackgroundDrawableResource(R.drawable.permission_dialog_bg);
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setGravity(Gravity.TOP);
            lp.y = (int) mContext.getResources().getDimension(R.dimen.px480dp);
            window.setAttributes(lp);
        }
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requesLogout();
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AtyContainer.getInstance().finishAllActivity();
                Intent intent;
                if (CacheUtils.getAddrTime() == 0) {
                    if (TextUtils.isEmpty(CacheUtils.getAddress())) {
                        intent = new Intent(mContext, TakeawayLoginActivity.class);
                    } else {
                        intent = new Intent(mContext, AddressSelectActivity.class);
                    }
                } else {
                    intent = new Intent(mContext, HomeActivity.class);
                }
                mContext.startActivity(intent);
            }
        });
    }

    public FrameLayout createAnimLayout(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    public AddressListBean.IovBean.DataBean getLocation(Context mContext) {
        AddressListBean.IovBean.DataBean mAddressData = null;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("_cache", Context.MODE_PRIVATE);
        String addressDataJson = sharedPreferences.getString(Constant.ADDRESS_DATA, null);
        if (addressDataJson != null) {
            mAddressData = GsonUtil.fromJson(addressDataJson, AddressListBean.IovBean.DataBean.class);
        }
        return mAddressData;
    }
}

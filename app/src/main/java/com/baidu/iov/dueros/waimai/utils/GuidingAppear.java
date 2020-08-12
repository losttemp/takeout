package com.baidu.iov.dueros.waimai.utils;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.baidu.iov.dueros.waimai.net.entity.response.HintsBean;
import com.baidu.iov.dueros.waimai.ui.WaiMaiApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum GuidingAppear {
    INSTANCE;
    private Handler mHandler;
    private Runnable mRunnable;
    private int times;

    private static final String TAG = GuidingAppear.class.getSimpleName();
    private static final long GUIDE_INTERVAL = 5000;
    private static final String UUID = "qa_test_123123";
    ArrayList<String> newList=new ArrayList<>();

    public void init(Context context,HintsBean tipsStr){
        if(tipsStr==null){
            return;
        }
        if (!UUID.equals(Constant.UUID)) {
        }
    }

    public void init(@NonNull final Context context, @NonNull final List<String> strings) {
        if(strings==null){
            return;
        }
        disappear();
        if (strings.size() == 0)
            return;
        if (strings.size() == 1) {
            Lg.getInstance().d(TAG, strings.get(0));
            if (!UUID.equals(Constant.UUID)) {
            }
            return;
        }
        newList.addAll(strings);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                showString(context, strings);
            }
        };
        mHandler.post(mRunnable);
    }

    private void showString(@NonNull Context context, @NonNull List<String> strings) {
        Random random=new Random();
        int length=newList.size();
        for (int i = 0; i <length ; i++) {
            Lg.getInstance().e(TAG,"showString=====:"+newList.get(i));
        }
        int randomNum=random.nextInt(length);

        if (!UUID.equals(Constant.UUID)) {
//            StatusBarsManager.conversationByApp(context, Config.PACKGE_NAME, strings.get(times % strings.size()));
            Lg.getInstance().e(TAG,"randomnum="+newList.get(randomNum)+"-----------------");
            newList.remove(randomNum);
            if(newList.size()==0){
                newList.addAll(strings);
            }
        }
        Lg.getInstance().d(TAG, strings.get(times % strings.size()));
        times++;
        mHandler.postDelayed(mRunnable, GUIDE_INTERVAL);
    }

    public void disappear() {
        if (mHandler != null && mRunnable != null)
            mHandler.removeCallbacks(mRunnable);
        times = 0;
        if (mHandler != null)
            mHandler = null;
        if (mRunnable != null)
            mRunnable = null;
    }

    public void exit(Context context) {
        disappear();
        if (!UUID.equals(Constant.UUID)) {
        }
        Lg.getInstance().d(TAG, "exit");
    }

    public  void showtTips(Context context, List<HintsBean> allTipsList, String key) {

        if (allTipsList == null||allTipsList.size()==0) {
            return;
        }else if(allTipsList.size()==1){
            GuidingAppear.INSTANCE.init(context,allTipsList.get(0));
            return;
        }
        ArrayList<Integer> showedIdList=new ArrayList<>();
        if(WaiMaiApplication.getInstance().getTipsMap().get(key)!=null){
            showedIdList.addAll(WaiMaiApplication.getInstance().getTipsMap().get(key));
        }

        ArrayList<HintsBean> newList=new ArrayList<>();
        newList.addAll(allTipsList);
        int lastId=-1;

        if(showedIdList.size()==allTipsList.size()){
            lastId=showedIdList.get(showedIdList.size()-1);
            showedIdList.clear();
            showedIdList.add(lastId);
        }
        for (int i = 0; i < showedIdList.size(); i++) {
            Lg.getInstance().e(TAG,showedIdList.get(i)+"----------------");
        }

        for (int i = 0; i < showedIdList.size(); i++) {
            int a=showedIdList.get(i);
            int newListLength=newList.size();
            for (int j = 0; j <newListLength ; j++) {
                if(allTipsList.get(showedIdList.get(i)).equals(newList.get(j))){
                    newList.remove(j);
                    break;
                }
            }
        }

        Random random = new Random();
        if(newList.size()>0){
            int length=newList.size();
            int randomNum= random.nextInt(length);
            for (int i = 0; i <allTipsList.size() ; i++) {
                if(TextUtils.equals(newList.get(randomNum).getHint(),allTipsList.get(i).getHint())){
                    showedIdList.add(i);
                    WaiMaiApplication.getInstance().getTipsMap().put(key,showedIdList);
                    break;
                }
            }
            GuidingAppear.INSTANCE.init(context,newList.get(randomNum));
        }

    }
}
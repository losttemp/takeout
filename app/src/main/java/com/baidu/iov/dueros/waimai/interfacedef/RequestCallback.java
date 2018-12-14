package com.baidu.iov.dueros.waimai.interfacedef;

public interface RequestCallback<T> {
    void onSuccess(T data);
    void onFailure(String msg);
    void getLogid(String id);
}

package com.baidu.iov.dueros.waimai.net.entity.response;

public  class HintsBean {
    /**
     * hint : 新增地址
     * is_online_support : 1
     * is_offline_support : 0
     * bot_desc : 我的收货地址为空
     * extend_json : null
     * extend_json_decoded : null
     */

    private String hint;
    private String is_online_support;
    private String is_offline_support;
    private String bot_desc;
    private String extend_json;
    private String extend_json_decoded;

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getIs_online_support() {
        return is_online_support;
    }

    public void setIs_online_support(String is_online_support) {
        this.is_online_support = is_online_support;
    }

    public String getIs_offline_support() {
        return is_offline_support;
    }

    public void setIs_offline_support(String is_offline_support) {
        this.is_offline_support = is_offline_support;
    }

    public String getBot_desc() {
        return bot_desc;
    }

    public void setBot_desc(String bot_desc) {
        this.bot_desc = bot_desc;
    }

    public Object getExtend_json() {
        return extend_json;
    }

    public void setExtend_json(String extend_json) {
        this.extend_json = extend_json;
    }

    public String getExtend_json_decoded() {
        return extend_json_decoded;
    }

    public void setExtend_json_decoded(String extend_json_decoded) {
        this.extend_json_decoded = extend_json_decoded;
    }
}

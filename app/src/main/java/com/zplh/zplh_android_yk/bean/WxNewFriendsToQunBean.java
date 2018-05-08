package com.zplh.zplh_android_yk.bean;

import java.io.Serializable;

/**
 * Created by lichun on 2018/1/30.
 * Description:
 */

public class WxNewFriendsToQunBean implements Serializable {

    /**
     * ret : 200
     * msg : success
     * data : {"id":"11","qr_code_address":"/Uploads/qrcode/1517196292.png","group_count":"0","status":"0","created_at":"2018-01-29 04:49:30","updated_at":null,"wx_group_name":"客户可"}
     */

    private String ret;
    private String msg;
    private DataBean data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 11
         * qr_code_address : /Uploads/qrcode/1517196292.png
         * group_count : 0
         * status : 0
         * created_at : 2018-01-29 04:49:30
         * updated_at : null
         * wx_group_name : 客户可
         */

        private String id;
        private String qr_code_address;
        private String group_count;
        private String status;
        private String created_at;
        private Object updated_at;
        private String wx_group_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQr_code_address() {
            return qr_code_address;
        }

        public void setQr_code_address(String qr_code_address) {
            this.qr_code_address = qr_code_address;
        }

        public String getGroup_count() {
            return group_count;
        }

        public void setGroup_count(String group_count) {
            this.group_count = group_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Object getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(Object updated_at) {
            this.updated_at = updated_at;
        }

        public String getWx_group_name() {
            return wx_group_name;
        }

        public void setWx_group_name(String wx_group_name) {
            this.wx_group_name = wx_group_name;
        }
    }
}

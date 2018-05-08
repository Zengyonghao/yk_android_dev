package com.zplh.zplh_android_yk.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangshuai on 2017/11/22.
 * Description: 支付宝转账时，将手机号和 实名认识的好友的名 传到服务器
 */

public class AlipayTransferMessageBean implements Serializable {



    private List<ResBean> res;


    public List<ResBean> getRes() {
        return res;
    }

    public void setRes(List<ResBean> res) {
        this.res = res;
    }


    public  static  class ResBean {
        /**
         * batch : test_u7110
         * isRealname : 1
         * uid : 7110
         * zfbAcount : zhenzhexuan9466@163.com
         * zfbPhoneName : 伟俊
         * zfbPhoneNum : 13602797499
         * zfbRealname : 伟俊
         */

        private String batch;
        private int isRealname;
        private String uid;
        private String account;
        private String nickname;
        private String phonenumber;
        private String zfbRealname;

        public String getBatch() {
            return batch;
        }

        public void setBatch(String batch) {
            this.batch = batch;
        }

        public int getIsRealname() {
            return isRealname;
        }

        public void setIsRealname(int isRealname) {
            this.isRealname = isRealname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getZfbRealname() {
            return zfbRealname;
        }

        public void setZfbRealname(String zfbRealname) {
            this.zfbRealname = zfbRealname;
        }
        public ResBean(String phonenumber, String nickname , int isRealname , String zfbRealname , String uid , String account,String  batch){
            this.phonenumber=phonenumber;
            this.nickname=nickname;
            this.isRealname=isRealname;
            this.zfbRealname=zfbRealname;
            this.uid = uid;
            this.account=account;
            this.batch =batch;
        }
    }
    public AlipayTransferMessageBean (List<ResBean> res){
        this.res =res;

    }
}

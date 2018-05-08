package com.zplh.zplh_android_yk.bean;

import java.util.List;

/**
 * Created by lichun on 2017/8/25.
 * Description:
 */

public class AlipayBillStatisticsBean {

    /**
     * ali_account : fjksdljg@163.com
     * flock : [{"flock_name":"十里桃花A209101","flock_num":"1"},{"flock_name":"天空A209191","flock_num":"2"},{"flock_name":"优惠A209177","flock_num":"2"}]
     * uid : 2091
     */

    private String ali_account;
    private String uid;
    private List<FlockBean> flock;

    public String getAli_account() {
        return ali_account;
    }

    public void setAli_account(String ali_account) {
        this.ali_account = ali_account;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<FlockBean> getFlock() {
        return flock;
    }

    public void setFlock(List<FlockBean> flock) {
        this.flock = flock;
    }

    public static class FlockBean {
        /**
         * flock_name : 十里桃花A209101
         * flock_num : 1
         */

        private String flock_name;
        private String flock_num;

        public FlockBean(String flock_name,String flock_num){
            this.flock_name=flock_name;
            this.flock_num=flock_num;
        }

        public String getFlock_name() {
            return flock_name;
        }

        public void setFlock_name(String flock_name) {
            this.flock_name = flock_name;
        }

        public String getFlock_num() {
            return flock_num;
        }

        public void setFlock_num(String flock_num) {
            this.flock_num = flock_num;
        }
    }
}

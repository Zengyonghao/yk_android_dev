package com.zplh.zplh_android_yk.bean;

import java.util.List;

/**
 * Created by lichun on 2017/7/8.
 * Description:
 */

public class AlipayAccountFlockNumBean {

    /**
     * account : [{"alipay_account":"mujinhua@163.com","flock":[{"flock_name":"天气","flock_num":"3"},{"flock_name":"kf","flock_num":"3"}]},{"alipay_account":"hfjkk@163.com","flock":[{"flock_name":"天气","flock_num":"3"},{"flock_name":"kf","flock_num":"3"},{"flock_name":"fahuo","flock_num":"4"}]}]
     * uid : 0446
     */

    private String uid;
    private List<AccountBean> account;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<AccountBean> getAccount() {
        return account;
    }

    public void setAccount(List<AccountBean> account) {
        this.account = account;
    }

    public static class AccountBean {
        /**
         * alipay_account : mujinhua@163.com
         * flock : [{"flock_name":"天气","flock_num":"3"},{"flock_name":"kf","flock_num":"3"}]
         */

        private String alipay_account;
        private List<FlockBean> flock;

        public String getAlipay_account() {
            return alipay_account;
        }

        public void setAlipay_account(String alipay_account) {
            this.alipay_account = alipay_account;
        }

        public List<FlockBean> getFlock() {
            return flock;
        }

        public void setFlock(List<FlockBean> flock) {
            this.flock = flock;
        }

        public static class FlockBean {
            /**
             * flock_name : 天气
             * flock_num : 3
             */

            private String flock_name;
            private String flock_num;

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
}

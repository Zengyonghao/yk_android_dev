package com.zplh.zplh_android_yk.bean;

import java.util.List;

/**
 * Created by lichun on 2017/7/11.
 * Description:
 */

public class AlipayAccountFlockClickBean {

    /**
     * account : [{"alipay_account":"meili.jiying@163.com","flock":[{"click_num":"16","gender":"男","flock_name":"亮亮A209103"}]},{"alipay_account":"mujinhua727@163.com","flock":[{"click_num":"16","gender":"女","flock_name":"王者A209101"}]}]
     * uid : 2091
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
         * alipay_account : meili.jiying@163.com
         * flock : [{"click_num":"16","gender":"男","flock_name":"亮亮A209103"}]
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
             * click_num : 16
             * flock_name : 亮亮A209103
             */

            private String click_num;
            private String flock_name;
            private String come_num;

            public String getCome_num() {
                return come_num;
            }

            public void setCome_num(String come_num) {
                this.come_num = come_num;
            }

            public String getClick_num() {
                return click_num;
            }

            public void setClick_num(String click_num) {
                this.click_num = click_num;
            }

            public String getFlock_name() {
                return flock_name;
            }

            public void setFlock_name(String flock_name) {
                this.flock_name = flock_name;
            }
        }
    }
}

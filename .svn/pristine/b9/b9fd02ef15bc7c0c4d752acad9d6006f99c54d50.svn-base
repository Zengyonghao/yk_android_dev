package com.zplh.zplh_android_yk.bean;

/**
 * Created by lichun on 2017/5/27.
 * Description:极光自定义消息
 */

public class MessageBean {


    /**
     * content : {"data":{"imei":"87975431324687132417123,869848020772184","range":"执行范围:所有的手机全部执行","task_type":"任务类型:自动拉群任务"}}
     * txt : 自动拉群任务
     * type : http
     */

    private ContentBean content;
    private String txt;
    private String type;

    @Override
    public String toString() {
        return "MessageBean{" +
                "content=" + content +
                ", txt='" + txt + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class ContentBean {
        /**
         * data : {"imei":"87975431324687132417123,869848020772184","range":"执行范围:所有的手机全部执行","task_type":"任务类型:自动拉群任务"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * imei : 87975431324687132417123,869848020772184
             * range : 执行范围:所有的手机全部执行
             * task_type : 任务类型:自动拉群任务
             */

            private String imei;
            private String range;
            private String task_type;

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getRange() {
                return range;
            }

            public void setRange(String range) {
                this.range = range;
            }

            public String getTask_type() {
                return task_type;
            }

            public void setTask_type(String task_type) {
                this.task_type = task_type;
            }
        }
    }
}

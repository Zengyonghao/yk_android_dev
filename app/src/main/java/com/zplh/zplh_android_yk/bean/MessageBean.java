package com.zplh.zplh_android_yk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lichun on 2017/5/27.
 * Description:极光自定义消息
 */

public class MessageBean implements Serializable{


    /**
     * content : {"data":{"param":{"dz_num":"5"},"range":["1376","1359","1361","1378","1379","1377","1377","1360"],"task_id":1,"todo_time":""}}
     */

    private ContentBean content;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean implements Serializable{
        /**
         * data : {"param":{"dz_num":"5"},"range":["1376","1359","1361","1378","1379","1377","1377","1360"],"task_id":1,"todo_time":""}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            /**
             * param : {"dz_num":"5"}
             * range : ["1376","1359","1361","1378","1379","1377","1377","1360"]
             * task_id : 1
             * todo_time :
             */
            private ParamBean param;
            private int task_id;
            private String todo_time;
            private List<String> range;

            public ParamBean getParam() {
                return param;
            }

            public void setParam(ParamBean param) {
                this.param = param;
            }

            public int getTask_id() {
                return task_id;
            }

            public void setTask_id(int task_id) {
                this.task_id = task_id;
            }

            public String getTodo_time() {
                return todo_time;
            }

            public void setTodo_time(String todo_time) {
                this.todo_time = todo_time;
            }

            public List<String> getRange() {
                return range;
            }

            public void setRange(List<String> range) {
                this.range = range;
            }

            public static class ParamBean implements Serializable{
                /**
                 * dz_num : 5
                 */
                private int phone_add_num;//一次任务每部手机最多请求加好友次数

                private int wx_add_num;//一次任务每个微信最多请求加好友次数

                private String dz_num;//朋友圈点赞

                private	 String	materia_id;//图片素材ID//也可以成为视频素材ID

                private String	is_remind;	// 设置朋友圈发布查看权限

                private String	is_protect;//是否设置为朋友圈视频私密

                private	 String	one_add_num;	/*13*///一个微信号每次任务最多请求加好友次数(通讯录加好友)

                private String	day_add_num;	/*10*///一个微信号每天最多请求加好友次数:(通讯录加好友)

                private	String	is_verify;	/*1*///添加认证信息(通讯录加好友)

                private String	sniffing_type;	/*1*///嗅探加好友 好友来源方式

                private String materia_pic;//微信群发图片

                private String materia_vedio;//微信群发视频

                private String is_mass;//好友发消息



                public String getMateria_pic() {
                    return materia_pic;
                }

                public void setMateria_pic(String materia_pic) {
                    this.materia_pic = materia_pic;
                }

                public String getMateria_vedio() {
                    return materia_vedio;
                }

                public void setMateria_vedio(String materia_vedio) {
                    this.materia_vedio = materia_vedio;
                }

                public String getIs_mass() {
                    return is_mass;
                }

                public void setIs_mass(String is_mass) {
                    this.is_mass = is_mass;
                }

                public String getMateria_id() {
                    return materia_id;
                }

                public void setMateria_id(String materia_id) {
                    this.materia_id = materia_id;
                }

                public String getIs_remind() {
                    return is_remind;
                }

                public void setIs_remind(String is_remind) {
                    this.is_remind = is_remind;
                }

                public String getIs_protect() {
                    return is_protect;
                }

                public void setIs_protect(String is_protect) {
                    this.is_protect = is_protect;
                }

                public String getOne_add_num() {
                    return one_add_num;
                }

                public void setOne_add_num(String one_add_num) {
                    this.one_add_num = one_add_num;
                }

                public String getDay_add_num() {
                    return day_add_num;
                }

                public void setDay_add_num(String day_add_num) {
                    this.day_add_num = day_add_num;
                }

                public String getIs_verify() {
                    return is_verify;
                }

                public void setIs_verify(String is_verify) {
                    this.is_verify = is_verify;
                }

                public String getSniffing_type() {
                    return sniffing_type;
                }

                public void setSniffing_type(String sniffing_type) {
                    this.sniffing_type = sniffing_type;
                }

                public String getDz_num() {
                    return dz_num;
                }

                public void setDz_num(String dz_num) {
                    this.dz_num = dz_num;
                }

                public int getPhone_add_num() {
                    return phone_add_num;
                }

                public void setPhone_add_num(int phone_add_num) {
                    this.phone_add_num = phone_add_num;
                }

                public int getWx_add_num() {
                    return wx_add_num;
                }

                public void setWx_add_num(int wx_add_num) {
                    this.wx_add_num = wx_add_num;
                }
            }
        }
    }
}

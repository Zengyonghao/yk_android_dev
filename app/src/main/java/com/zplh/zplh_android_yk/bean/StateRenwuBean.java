package com.zplh.zplh_android_yk.bean;

/**
 * Created by Administrator on 2017/6/30.
 */

public class StateRenwuBean {
    private int id_task;
    private int login_id;
    private String result;
    private String times;

    public StateRenwuBean(int id_task, int login_id, String result,String times) {
        this.id_task=id_task;
        this.login_id=login_id;
        this.result=result;
        this.times=times;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public StateRenwuBean() {
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "StateRenwuBean{" +
                "id_task=" + id_task +
                ", login_id='" + login_id + '\'' +
                ", result='" + result + '\'' +
                ", times='" + times + '\'' +
                '}';
    }
}

package com.zplh.zplh_android_yk.bean;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ZhiXIngBean {
    private String stuteRenu;
    private String login;

    public String getStuteRenu() {
        return stuteRenu;
    }

    public void setStuteRenu(String stuteRenu) {
        this.stuteRenu = stuteRenu;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ZhiXIngBean(String stuteRenu, String login) {
        this.stuteRenu = stuteRenu;
        this.login = login;
    }

    @Override
    public String toString() {
        return "ZhiXIngBean{" +
                "stuteRenu='" + stuteRenu + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}

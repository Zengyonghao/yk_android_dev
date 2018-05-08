package com.zplh.zplh_android_yk.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by lichun on 2017/9/15.
 * Description:公众号浏览日期
 */

public class PublicMarkBean extends DataSupport {
    private String publicMark;
    private String publicDay;

    public String getPublicMark() {
        return publicMark;
    }

    public void setPublicMark(String publicMark) {
        this.publicMark = publicMark;
    }

    public String getPublicDay() {
        return publicDay;
    }

    public void setPublicDay(String publicDay) {
        this.publicDay = publicDay;
    }
}

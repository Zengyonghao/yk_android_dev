package com.zplh.zplh_android_yk.bean;

import java.util.List;

/**
 * Created by lichun on 2017/12/19.
 * Description:
 */

public class ZfbPhoneNumberBean {

    /**
     * status : 1
     * info : success
     * data : ["18588239910","18961816797","13140968550","13430752607","18040540573","13523041373","13823636746","13451510619","13229576991","15876563516"]
     * code : 2017-12-19 12:25:15_u3045_yawen773zailian@163.com_8470
     */

    private int status;
    private String info;
    private String code;
    private List<String> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}

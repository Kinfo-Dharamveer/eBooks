package com.kinfoitsolutions.ebooks.ui.responsemodel.AllSearchDataSuccess;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class AllSearchDataSuccess {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<AllSearchDataPayload> data = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AllSearchDataPayload> getData() {
        return data;
    }

    public void setData(List<AllSearchDataPayload> data) {
        this.data = data;
    }


}

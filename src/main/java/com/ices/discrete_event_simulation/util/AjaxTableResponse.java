package com.ices.discrete_event_simulation.util;

import java.util.List;

public class AjaxTableResponse {
    private Integer code;
    private String msg;//成功一般不需要msg，失败才需要告诉前端
    private Integer count;//表格中数据的个数
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AjaxTableResponse(Integer code, String msg, Integer count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    //常用的，用静态方法
    public static AjaxTableResponse AjaxData(Integer code, String msg, Integer count, Object data) {
        return new AjaxTableResponse(code,  msg, count, data);
    }
}

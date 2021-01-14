package com.ices.discrete_event_simulation.util;

public class AjaxResponse {
    private Integer status;
    private String msg;//成功一般不需要msg，失败才需要告诉前端
    private Object obj;//数据的载体

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    //这种写法的好处，只要下面静态方法，调用就可以了
    private AjaxResponse(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    //常用的，用静态方法
    public static AjaxResponse AjaxData(Integer status, String msg, Object obj) {
        return new AjaxResponse(status, msg, obj);
    }
}

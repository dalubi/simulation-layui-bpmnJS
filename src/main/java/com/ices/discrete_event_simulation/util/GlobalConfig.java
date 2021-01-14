package com.ices.discrete_event_simulation.util;

//try error的时候的返回值，定义
public class GlobalConfig {
    /**
     * 测试场景
     */
    public static final Boolean Test = true;//表示开发阶段

    //windows路径
    //public static final String BPMN_PathMapping = "file:D:\\WangJianIDEA_Test\\activiti-imooc\\src\\main\\resources\\resources\\bpmn\\";

    //Liunx路径
    public static final String BPMN_PathMapping = "file:/Users/america/Java_study/activiti7_webflow/src/main/resources/resources/bpmn/";

    public enum ResponseCode {
        SUCCESS(0, "成功"),
        ERROR(1, "错误");

        private final int code;
        private final String desc;

        ResponseCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}

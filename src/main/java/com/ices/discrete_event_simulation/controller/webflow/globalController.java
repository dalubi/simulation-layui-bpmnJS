package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.Federate;
import com.ices.discrete_event_simulation.mapper.FederateMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class globalController {
    private static String federation;
    private static String federate;
    private static int federateId;

    public static String getFederation() {
        return federation;
    }

    public static void setFederation(String federation) {
        globalController.federation = federation;
    }

    public static String getFederate() {
        return federate;
    }

    public static void setFederate(String federate) {
        globalController.federate = federate;
    }

    public static int getFederateId() {
        return federateId;
    }

    public static void setFederateId(int federateId) {
        globalController.federateId = federateId;
    }

    //选中federation
    @RequestMapping(value = "/setTheChosenFederation" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setFederationInfo(@RequestBody JSONObject json){
        String federationName = json.get("bpmnFile").toString();
        setFederation(federationName);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"已确认选中联邦:"+federationName);
    }

    @Autowired
    FederateMapper federateMapper;

    //选中federate
    @RequestMapping(value = "/federateInfo/setTheChosenFederate" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setFederateInfo(@RequestBody JSONObject json){
        String federateName = json.get("federate").toString();
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federateName",federateName);
        List<Federate> federateList = federateMapper.selectByMap(columnMap);
        federateId = federateList.get(0).getFederateId();
        federate=federateName;
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"已确认选中联邦成员:"+federateName+",请填写初始化表单");
    }
}

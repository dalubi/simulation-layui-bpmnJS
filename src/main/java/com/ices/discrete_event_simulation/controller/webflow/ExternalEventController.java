package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.ExternaltaskActionattributeMapper;
import com.ices.discrete_event_simulation.mapper.ExternaltaskMapper;
import com.ices.discrete_event_simulation.mapper.ExternaltaskScopeMapper;
import com.ices.discrete_event_simulation.mapper.FederateVariableMapper;
import com.ices.discrete_event_simulation.pojo.actionAttributeNeedInfo;
import com.ices.discrete_event_simulation.pojo.scopeNeedInfo;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ExternalEventController {

    @Autowired
    ExternaltaskMapper externaltaskMapper;

    @Autowired
    FederateVariableMapper federateVariableMapper;

    //添加外部事件信息
    @RequestMapping(value = "/externalEvent/addExternalEventInfo",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addExternalEventInfo(@RequestParam("externalEventName")String externalEventName,
                                         @RequestParam("externalEventId")String externalEventId,
                                         @RequestParam("action_tpye")String action_tpye,
                                             @RequestParam("probability")String probability
                                            , @RequestParam("tofederate")String tofederate){
        Externaltask externaltask = new Externaltask();
        externaltask.setProbability(probability);
        externaltask.setInteractionName(externalEventName);
        externaltask.setActionTpye(Integer.parseInt(action_tpye));
        externaltask.setExternaleventid(Integer.parseInt(externalEventId));
        externaltask.setTofederate(tofederate);
        externaltaskMapper.insert(externaltask);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"添加成功，继续添加作用变量信息！");

    }

    //没有外部事件ID的时候，查询外部事件影响的属性
    @RequestMapping(value = "/externalEvent/getExternalEventInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getExternalEventInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<Externaltask> externaltaskList = externaltaskMapper.selectList(null);
        for(Externaltask et:externaltaskList){
            HashMap<String,Object> element = new HashMap<>();
            if (et.getActionTpye()==1){
                element.put("actionType","联邦成员");
            }else {
                element.put("actionType","成员实例");
            }
            element.put("externalEventId",et.getExternaleventid());
            element.put("interactionName",et.getInteractionName());
            element.put("probability",et.getProbability()+"%");
            element.put("tofederate",et.getTofederate());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    public static String getExternalEventId() {
        return externalEventId;
    }

    public static void setExternalEventId(String externalEventId) {
        ExternalEventController.externalEventId = externalEventId;
    }


    //ActionAttribute模块----------------------------------------------------------------

    //设置ActionAttribute之前需要的信息
    @RequestMapping(value = "/externalEvent/setActionAttributePre" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setActionAttributePre(@RequestBody JSONObject json){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        String actionType = json.get("actionType").toString();//联邦成员、成员实例
        String externalEventId = json.get("externalEventId").toString();

        actionAttributeNeedInfo.setActionType(actionType);
        actionAttributeNeedInfo.setExternalEventId(externalEventId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                "");
    }

    private static com.ices.discrete_event_simulation.pojo.actionAttributeNeedInfo actionAttributeNeedInfo = new actionAttributeNeedInfo();

    //ajax获取AddAttribute对应的Federate对应属性信息
    @RequestMapping(value = "/externalEvent/getFederateVariables" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getFederateVariables(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        //externalEventId -- federate（name）-- federatevaraible表
        String externalEventId = actionAttributeNeedInfo.getExternalEventId();

        //externalEventId -- federate（name）
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("externaleventid",externalEventId);
        String tofederate = externaltaskMapper.selectByMap(columnMap).get(0).getTofederate();

        //federate（name）-- federatevaraible表
        Map<String,Object> columnMap2 = new HashMap<>();
        columnMap2.put("federate",tofederate);
        List<FederateVariable> federateVariables = federateVariableMapper.selectByMap(columnMap2);

        for(FederateVariable fv:federateVariables){
            HashMap<String,Object> element = new HashMap<>();
            element.put("id",fv.getVariableId());
            element.put("variableName",fv.getVariableName());
            listMap.add(element);
        }
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap);
    }

    //没有外部事件ID的时候，查询外部事件影响的属性
    @RequestMapping(value = "/externalEvent/getActionAttributeByWithoutId" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getActionAttributeByWithoutId(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> element = new HashMap<>();
        element.put("name","无");
        element.put("change","无");
        element.put("durationTime","无");
        element.put("interactionName","无");
        listMap.add(element);
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    private static String externalEventId ;

    @Autowired
    ExternaltaskActionattributeMapper externaltaskActionattributeMapper;

    //有外部事件ID的时候，查询外部事件影响的属性
    @RequestMapping(value = "/externalEvent/getAllActionAttribute" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getActionAttributeById(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<ExternaltaskActionattribute> AAList = externaltaskActionattributeMapper.selectList(null);

        for(ExternaltaskActionattribute aa:AAList){
            HashMap<String,Object> element = new HashMap<>();
            element.put("attributeName",aa.getAttributeName());
            element.put("changes",aa.getChanges());
            element.put("durationTime",aa.getDurationTime());

            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("externaleventid",aa.getExternaleventid());
            String interactionName = externaltaskMapper.selectByMap(columnMap).get(0).getInteractionName();
            element.put("interactionName",interactionName);
            element.put("actionAttributeId",aa.getId());

            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //添加外部事件影响变量的信息
    @RequestMapping(value = "/externalEvent/addActionAttributeInfo",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addActionAttributeInfo(@RequestParam("attribute_name")String attribute_name,
                                             @RequestParam("changes")String changes,
                                             @RequestParam("durationTime")String durationTime){
        ExternaltaskActionattribute externaltaskActionattribute = new ExternaltaskActionattribute();
        externaltaskActionattribute.setAttributeName(attribute_name);
        externaltaskActionattribute.setChanges(changes);
        externaltaskActionattribute.setDurationTime(durationTime);
        externaltaskActionattribute.setExternaleventid(Integer.parseInt(actionAttributeNeedInfo.getExternalEventId()));
        externaltaskActionattributeMapper.insert(externaltaskActionattribute);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"作用变量信息添加成功！");
    }

    //添加Scope信息----------------------------------------------------------------

    @Autowired
    ExternaltaskScopeMapper externaltaskScopeMapper;

    //获取Scope信息
    @RequestMapping(value = "/externalEvent/getAllScopes" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllScopes(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<ExternaltaskScope> externaltaskScopes = externaltaskScopeMapper.selectList(null);

        for(ExternaltaskScope es:externaltaskScopes){

            HashMap<String,Object> element = new HashMap<>();
            if(es.getActionAttributeId().equals("null")){
                element.put("QualifiedAttribute","无依赖属性");
                element.put("Type","无范围变化");
                element.put("ListName","无");
                element.put("ObejctName","无");
                element.put("Scope","无");
                element.put("id","无");
            }else {
                element.put("QualifiedAttribute",es.getQualifiedAttribute());
                element.put("Type",es.getType());
                element.put("ListName",es.getListName());
                element.put("ObejctName",es.getObejctName());
                element.put("Scope",es.getScope());
                element.put("id",es.getId());
            }

            Integer actionAttributeId = es.getActionAttributeId();
            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("id",actionAttributeId);
            String attributeName = externaltaskActionattributeMapper.selectByMap(columnMap).get(0).getAttributeName();
            element.put("attributeName",attributeName);
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    private static scopeNeedInfo scopeNeedInfo = new scopeNeedInfo();

    public static com.ices.discrete_event_simulation.pojo.scopeNeedInfo getScopeNeedInfo() {
        return scopeNeedInfo;
    }

    public static void setScopeNeedInfo(com.ices.discrete_event_simulation.pojo.scopeNeedInfo scopeNeedInfo) {
        ExternalEventController.scopeNeedInfo = scopeNeedInfo;
    }

    @RequestMapping(value = "/externalEvent/setActionAttributeNoScope",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setActionAttributeNoScope(@RequestBody JSONObject json){
        String actionAttributeId = json.get("actionAttributeId").toString();

        //更新值
        ExternaltaskActionattribute ea = new ExternaltaskActionattribute();
        ea.setId(Integer.parseInt(actionAttributeId));
        ea.setScopeids("null");
        externaltaskActionattributeMapper.updateById(ea);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"更新成功！");
    }

    //设置Scope之前需要的信息
    @RequestMapping(value = "/externalEvent/setScopePre" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setScopePre(@RequestBody JSONObject json){
        String actionAttributeId = json.get("actionAttributeId").toString();
        scopeNeedInfo.setActionAttributeId(actionAttributeId);

        //通过actionAttributeId--externaleventId--action_type
        //actionAttributeId--externaleventId
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("id",actionAttributeId);
        Integer externaleventid = externaltaskActionattributeMapper.selectByMap(columnMap).get(0).getExternaleventid();

        //externaleventId--action_type
        Map<String,Object> columnMap1 = new HashMap<>();
        columnMap1.put("externaleventid",externaleventid);
//        Integer actionTpye = externaltaskMapper.selectByMap(columnMap1).get(0).getActionTpye();
        Externaltask et = externaltaskMapper.selectByMap(columnMap1).get(0);
        Integer actionTpye = et.getActionTpye();
        scopeNeedInfo.setFederateName(et.getTofederate());

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                actionTpye);
    }

    //添加FederateScope信息
    @RequestMapping(value = "/externalEvent/addFederateScope",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addFederateScope(@RequestParam("qualified_attribute")String qualified_attribute,
                                               @RequestParam("type")String type,
                                               @RequestParam("scope")String scope){
        ExternaltaskScope ets = new ExternaltaskScope();

        ets.setActionAttributeId(Integer.parseInt(scopeNeedInfo.getActionAttributeId()));
        ets.setQualifiedAttribute(qualified_attribute);
        ets.setScope(scope);
        ets.setType(type);

        externaltaskScopeMapper.insert(ets);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"");
    }

    //添加FederateInstanceScope信息，此时才需要查询scope信息，federateInstanceScope页面
    @RequestMapping(value = "/externalEvent/addFederateInstanceScope",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addFederateInstanceScope(@RequestParam("qualified_attribute")String qualified_attribute,
                                         @RequestParam("type")String type,
                                         @RequestParam("scope")String scope){
        ExternaltaskScope ets = new ExternaltaskScope();
        ets.setActionAttributeId(Integer.parseInt(scopeNeedInfo.getActionAttributeId()));
        ets.setType(type);
        ets.setScope(scope);
        externaltaskScopeMapper.insert(ets);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"");
    }

    //根据Federate，查询它有啥变量，拿出来
    @RequestMapping(value = "/externalEvent/getFederateVariables",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addFederateInstanceScope(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        //获得当前应该查询哪个Federate，获得他的变量信息
        String federate = scopeNeedInfo.getFederateName();

        //查询
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federate",federate);
        List<FederateVariable> variableList = federateVariableMapper.selectByMap(columnMap);

        for(FederateVariable fv:variableList){
            HashMap<String,Object> element = new HashMap<>();
            element.put("variableName",fv.getVariableName());
            element.put("variableId",fv.getVariableId());
            listMap.add(element);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,listMap);
    }

}

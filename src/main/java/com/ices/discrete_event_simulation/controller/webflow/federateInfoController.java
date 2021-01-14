package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import com.ices.discrete_event_simulation.util.generateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class federateInfoController {

    private static String federate;
    private static int federateId;
    private static String federation;

    @Autowired
    FederateMapper federateMapper;

    @Autowired
    FederationBpmnNicknameMapper federationBpmnNicknameMapper;

    @Autowired
    StartInformationMapper startInformationMapper;

    // 获得federate的信息
    @RequestMapping(value = "/federateInfo/getAllFederateInfos" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateInfos(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<Federate> federateList = federateMapper.selectByMap(null);

        for(int i=0;i<federateList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("federateName",federateList.get(i).getFederateName());
//            element.put("federation",federateList.get(i).getFederation());
//            Map<String,Object> columnMap = new HashMap<>();
//            columnMap.put("federationName",federateList.get(i).getFederation());

            //查看初始化信息是否填完了
//            Map<String,Object> col = new HashMap<>();
//            col.put("federateId",federateList.get(i).getFederateId());
//            List<StartInformation> startInformationList = startInformationMapper.selectByMap(col);
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    //添加联邦成员初始化信息
    @RequestMapping(value = "/federateInfo/addFederateInitialize" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse AddParameterByPiece(@RequestBody JSONObject json){
        String isFirst = json.get("isFirst").toString();
        String firstTask = json.get("firstTask").toString();

        StartInformation start=new StartInformation();

        start.setFederateId(globalController.getFederateId());
        start.setFirstTask(Integer.parseInt(firstTask));
        start.setIsFirst(Integer.parseInt(isFirst));

        startInformationMapper.insert2(start);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"成功添加一条参数信息");
    }

    @Autowired
    FederateObjectMapper federateObjectMapper;

    @Autowired
    ParameterMapper parameterMapper;

    //查询所有FederateObject信息
    @RequestMapping(value = "/federateInfo/getAllFederateObjectsInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateObjectsInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<FederateObject> federateObjectList = federateObjectMapper.selectList(null);

        for(int i=0;i<federateObjectList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("objectName",federateObjectList.get(i).getObjectName());

            //把参数名和参数类型对应起来，拼接成句子返回
            String parameterNames = federateObjectList.get(i).getParameterNames();
            String parameterTypes = federateObjectList.get(i).getParameterTypes();
            String[] parameterNameArray = parameterNames.split(",");
            String[] parameterTypeArray = parameterTypes.split(",");
            StringBuilder sb = new StringBuilder();
            for(int j=0;j<parameterNameArray.length;j++){
                sb.append("第"+(j+1)+"个参数，参数名称是:"+parameterNameArray[j]+"，参数类型是:"+parameterTypeArray[j]+";");
            }
            element.put("objectDescription",sb.toString());
            element.put("InitialId",federateObjectList.get(i).getInitialId());
            element.put("objectId",federateObjectList.get(i).getObjectId());
            element.put("federate",federateObjectList.get(i).getFederate());
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    //添加一条FederateObject信息
    @RequestMapping(value = "/federateInfo/federateobject",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse postFederateObjct(@RequestParam("objectName")String objectName,
                                    @RequestParam("InitialId")String InitialId,
                                    @RequestParam("parameterName")String parameterNames,
                                    @RequestParam("parameterType")String parameterTypes,
                                    @RequestParam("federate")String federate){

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federateName",federate);
        List<Federate> federateList = federateMapper.selectByMap(columnMap);
        if(federateList.size()==0){
            //异常
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"并未找到对应Federate");
        }else{
            //正常操作
            int curFederateId = federateList.get(0).getFederateId();
            String s = startInformationMapper.queryFederateObjectsById(curFederateId);
            List<Integer> curlistId=new ArrayList<>();
            if(s!=null){
                curlistId.addAll(generateUtils.extract(s));
            }
            FederateObject federateobject = new FederateObject();
            federateobject.setObjectName(objectName);
            federateobject.setInitialId(Integer.parseInt(InitialId));
            federateobject.setParameterNames(parameterNames);
            federateobject.setParameterTypes(parameterTypes);
            federateobject.setFederate(federate);
            federateObjectMapper.insert(federateobject);
            int maxId = federateObjectMapper.maxId();
            curlistId.add(maxId);
            StringBuilder sb=new StringBuilder();
            for(Integer id:curlistId){
                sb.append(id);
                sb.append(",");
            }
            startInformationMapper.updateFederateObjectByFederateId(curFederateId,sb.toString());
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"成功添加一条仿真参数信息");
        }

    }

    //删除一条FederateObject信息
    @RequestMapping(value = "/federateInfo/deleteFederateObject" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteFederateObjectById(@RequestBody JSONObject json){
        String objectId = json.get("objectId").toString();

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("objectId",objectId);
        federateObjectMapper.deleteByMap(columnMap);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除编号为:"+objectId+"的对象");
    }


    @Autowired
    FederateVariableMapper federateVariableMapper;

    //查询所有FederateVarible信息
    @RequestMapping(value = "/federateInfo/getAllFederateVariable" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateVariableInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();


        //这里写startinformation
        
        List<FederateVariable> federateVariableList = federateVariableMapper.selectList(null);

        for(int i=0;i<federateVariableList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("variableId",federateVariableList.get(i).getVariableId());
            element.put("variableName",federateVariableList.get(i).getVariableName());
            element.put("variableType",federateVariableList.get(i).getVariableType());
            if(federateVariableList.get(i).getIsStatic()==1){
                element.put("isStatic","是");
            }else{
                element.put("isStatic","否");
            }
            element.put("InitialValue",federateVariableList.get(i).getInitialValue());
            element.put("federate",federateVariableList.get(i).getFederate());
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //添加联邦全局变量
    @RequestMapping(value = "/federateInfo/addFederateVariable",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse postFederateVariable(@RequestParam("variableType")String variableType,
                                       @RequestParam("variableName")String variableName,
                                       @RequestParam("isStatic")String isStatic,
                                       @RequestParam("InitialValue")String InitialValue,
                                       @RequestParam("federate")String federate){

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federateName",federate);
        List<Federate> federateList = federateMapper.selectByMap(columnMap);
        if(federateList.size()==0){
            //异常
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"并未找到对应Federate");
        }else{
            //正常操作
            int curFederateId = federateList.get(0).getFederateId();
            List<Integer> curlistId=new ArrayList<>();
            String s = startInformationMapper.queryFederateVariableById(curFederateId);
           if(s!=null){
                curlistId.addAll(generateUtils.extract(s));
            }
            FederateVariable federateVariable=new FederateVariable();
            federateVariable.setVariableType(variableType);
            federateVariable.setVariableName(variableName);
            federateVariable.setIsStatic(Integer.parseInt(isStatic));
            federateVariable.setInitialValue(InitialValue);
            federateVariable.setFederate(federate);
            federateVariableMapper.insert(federateVariable);
            int maxId = federateVariableMapper.maxId();
            curlistId.add(maxId);
            StringBuilder sb=new StringBuilder();
            for(Integer id:curlistId){
                sb.append(id);
                sb.append(",");
            }
            startInformationMapper.updateFederateVariableByFederateId(curFederateId,sb.toString());
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"成功添加一条仿真全局变量信息");
        }
    }

    //删除联邦全局变量
    @RequestMapping(value = "/federateInfo/deleteFederateVariable" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteFederateVariableById(@RequestBody JSONObject json){
        String variableId = json.get("variableId").toString();

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("variableId",variableId);
        federateVariableMapper.deleteByMap(columnMap);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除编号为:"+variableId+"的全局变量");
    }



    @Autowired
    FederateListMapper federateListMapper;

    //获取所有的仿真列表信息
    @RequestMapping(value = "/federateInfo/getAllFederateListInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateListInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<FederateList> federateListList = federateListMapper.selectList(null);
        for(int i=0;i<federateListList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("listId",federateListList.get(i).getListId());
            element.put("objectType",federateListList.get(i).getObjectType());
            element.put("listName",federateListList.get(i).getListName());
            element.put("federate",federateListList.get(i).getFederate());

            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    //添加仿真列表信息
    @RequestMapping(value = "/federateInfo/addFederateList",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse postFederateList(@RequestParam("listName")String listName,
                                             @RequestParam("objectName")String objectName,
                                             @RequestParam("federate")String federate){
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federateName",federate);
        List<Federate> federateListList = federateMapper.selectByMap(columnMap);

        if(federateListList.size()==0){
            //异常
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"并未找到对应Federate");
        }else{
            //正常操作
            int curFederateId = federateListList.get(0).getFederateId();
            List<Integer> curlistId=new ArrayList<>();
            String s = startInformationMapper.queryFederateListIdsById(curFederateId);
            if(s!=null){
                curlistId.addAll(generateUtils.extract(s));
            }
            FederateList federateList = new FederateList();
            federateList.setListName(listName);
            federateList.setObjectType(objectName);
            federateList.setFederate(federate);
            federateListMapper.insert(federateList);
            int maxId = federateListMapper.maxId();
            curlistId.add(maxId);
            StringBuilder sb=new StringBuilder();
            for(Integer id:curlistId){
                sb.append(id);
                sb.append(",");
            }
            startInformationMapper.updateFederateListByFederateId(curFederateId,sb.toString());
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"成功添加一条仿真列表信息");
        }
    }

    //删除仿真列表信息（还没写）
    @RequestMapping(value = "/federateInfo/deleteFederateList" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteFederateListById(@RequestBody JSONObject json){
        String listId = json.get("listId").toString();

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("listId",listId);
        federateListMapper.deleteByMap(columnMap);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除编号为:"+listId+"的列表");
    }

    //获取所有的仿真实例初始化信息
    @RequestMapping(value = "/federateInfo/getAllInitialInstanceInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllInitialInstanceInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<StartInformation> startInformationList = startInformationMapper.selectList(null);

        for(int i=0;i<startInformationList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            StartInformation si = startInformationList.get(i);
            String federateId = si.getFederateId().toString();

            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("federateId",federateId);
            List<Federate> federateList = federateMapper.selectByMap(columnMap);
            String federateName = federateList.get(0).getFederateName();

            //联邦类型
            String fedearteType = "";
            String isFirst = si.getIsFirst().toString();
            switch (isFirst){
                case "1":
                    fedearteType = "普通联邦";
                    break;
                case "0":
                    fedearteType = "起始联邦";
                    break;
            }

            //初步任务
            String firstTaskId = isNull(isNull2(si.getFirstTask()));

            //other
            String variableIds = isNull(isNull2(si.getVariableIds()));
            String objectIds = isNull(isNull2(si.getObjects()));

            //自发任务
            //是否拥有自发任务
            String hasOwnEvent = isNull(isNull2(si.getHasOwnEvent()));

            //自发任务ID
            String ownEventTask = isNull(isNull2(si.getOwnEventTask()));

            //初始实例任务ID
            String initialInstanceTaskId = isNull(isNull2(si.getInitialInstanceTask()));

            //更新实例任务ID
            String updateInstanceTaskId = isNull(isNull2(si.getUpdateInstanceTask()));

            element.put("federateName",federateName);
            element.put("fedearteType",fedearteType);
            element.put("firstTaskId",firstTaskId);
            element.put("variableIds",variableIds);
            element.put("objectIds",objectIds);
            element.put("hasOwnEvent", hasOwnEvent );
            element.put("ownEventTask", ownEventTask);
            element.put("initialInstanceTaskId",initialInstanceTaskId);
            element.put("updateInstanceTaskId", updateInstanceTaskId);

            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //获取所有的仿真实例初始化信息
    @RequestMapping(value = "/federateInfo/getAllUpdateInstanceInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllUpdateInstanceInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<StartInformation> startInformationList = startInformationMapper.selectList(null);

        for(int i=0;i<startInformationList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            StartInformation si = startInformationList.get(i);
            String federateId = si.getFederateId().toString();

            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("federateId",federateId);
            List<Federate> federateList = federateMapper.selectByMap(columnMap);
            String federateName = federateList.get(0).getFederateName();


            //初步任务
            String firstTaskId = isNull(isNull2(si.getFirstTask()));

            //other
            String variableIds = isNull(isNull2(si.getVariableIds()));
            String objectIds = isNull(isNull2(si.getObjects()));

            //初始实例任务ID
            String initialInstanceTaskId = isNull(isNull2(si.getInitialInstanceTask()));

            //更新实例任务ID
            String updateInstanceTaskId = isNull(isNull2(si.getUpdateInstanceTask()));

            element.put("federateName",federateName);

            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    private String isNull(String s){
        if(s.equals("")||s==""){
            return "null";
        }
        return s;
    }

    private String isNull2(Object o){
        if(o==null){
            return "null";
        }else{
            return o.toString();
        }
    }
}

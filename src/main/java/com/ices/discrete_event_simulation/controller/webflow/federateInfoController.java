package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.pojo.propertyPOJO;
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

    //获得federate的信息
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

    //添加一条FederateObjectInstance信息
    @RequestMapping(value = "/federateInfo/federateobjectinstance",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse postFederateObjctInstance(@RequestParam("objectName")String objectName,
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


    //initialInstanceId部分
    @Autowired
    InitialinstancetaskMapper initialinstancetaskMapper;

    //添加初始化实例
    @RequestMapping(value = "/federateInfo/addFederateInitialInstance" , method = RequestMethod.POST)
    @ResponseBody
    public  AjaxResponse addFederateInstance(@RequestParam("InstanceNumber")String InstanceNumber,
                                             @RequestParam("listName")String listName,
                                             @RequestParam("instanceType")String instanceType){
        Initialinstancetask ii = new Initialinstancetask();
        ii.setInstancenum(Integer.parseInt(InstanceNumber));
        ii.setInstancetype(instanceType);
        ii.setListname(listName);

        initialinstancetaskMapper.insert(ii);
        return  AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                "成功");
    }

    //获取初始化实例信息
    @RequestMapping(value = "/federateInfo/getAllFederateInitialInstance" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateInitialInstance(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<Initialinstancetask> initialinstancetasks = initialinstancetaskMapper.selectList(null);

        for(int i=0;i<initialinstancetasks.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            Initialinstancetask ii = initialinstancetasks.get(i);
            element.put("listName",ii.getListname());
            element.put("instanceType",ii.getInstancetype());
            element.put("instanceNum",ii.getInstancenum());
            element.put("id",ii.getId());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    private static String InitialInstanceTaskId;
    //获取某个federateObject对应Id下所有属性和对应的类型
    //获取所有的仿真列表信息
    @RequestMapping(value = "/federateInfo/setInitialInstancetaskById" , method = RequestMethod.POST)
    @ResponseBody
    public  AjaxResponse getFederateObjectAttributeInfo(@RequestBody JSONObject json){
        InitialInstanceTaskId = json.get("id").toString();
        return  AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                "成功");
    }

    private List<propertyPOJO> propertyPOJOList = new ArrayList<>();

    @RequestMapping(value = "/federateInfo/getPropertyInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getPropertyInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        //根据initialInstanceTaskId获得federateObject的类型
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("id",InitialInstanceTaskId );
        Initialinstancetask ii = initialinstancetaskMapper.selectByMap(columnMap).get(0);

        Map<String,Object> columnMap2 = new HashMap<>();
        columnMap2.put("objectName",ii.getInstancetype() );
        FederateObject fo = federateObjectMapper.selectByMap(columnMap2).get(0);

        String[] names = fo.getParameterNames().split(",");
        String[] types = fo.getParameterTypes().split(",");
        Integer initialId = fo.getInitialId();

        if (initialId==null){

        }else{
            HashMap<String,Object> element = new HashMap<>();
            element.put("name","startTime");
            element.put("type","int");
            HashMap<String,Object> element2 = new HashMap<>();
            element2.put("name","endTime");
            element2.put("type","int");
            listMap.add(element);
            listMap.add(element2);
            propertyPOJOList.add(new propertyPOJO("startTime","int"));
            propertyPOJOList.add(new propertyPOJO("endTime","int"));
        }
        for(int i=0;i<names.length;i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("name",names[i]);
            element.put("type",types[i]);
            listMap.add(element);
            propertyPOJOList.add(new propertyPOJO(names[i],types[i]));
        }
        return  AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    private static int propertyCount=0;
    //设置某个属性的初值
    @RequestMapping(value = "/federateInfo/setInitialInstancePropertyValue" , method = RequestMethod.POST)
    @ResponseBody
    public  AjaxResponse setInitialInstancePropertyValue(@RequestBody JSONObject json){
        String name = json.get("name").toString();
        String value = json.get("value").toString();
        //遍历propertyPOJOList，看名字对上了，就将value堆上去
        for(propertyPOJO pp:propertyPOJOList){
            if(pp.getName().equals(name)){
                pp.setValue(value);
                propertyCount++;
            }
        }
        if(propertyCount==propertyPOJOList.size()){
            StringBuilder propertyNames = new StringBuilder();
            StringBuilder propertyTypes = new StringBuilder();
            StringBuilder propertyValues = new StringBuilder();
            for(propertyPOJO pp:propertyPOJOList){
                propertyNames.append(pp.getName()+",");
                propertyTypes.append(pp.getType()+",");
                propertyValues.append(pp.getValue()+",");
            }
            Initialinstancetask ii = new Initialinstancetask();
            ii.setId(Integer.parseInt(InitialInstanceTaskId));
            ii.setPropertynames(propertyNames.toString());
            ii.setPropertytypes(propertyTypes.toString());
            ii.setPropertyvalues(propertyValues.toString());
            initialinstancetaskMapper.updateById(ii);
            propertyCount=0;
            propertyPOJOList.clear();
        }
        return  AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                "成功");
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

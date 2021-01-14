package com.ices.discrete_event_simulation.controller.post;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.dto.instanceParameterDTO;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.pojo.Result;
import com.ices.discrete_event_simulation.dto.federateObjectDTO;
import com.ices.discrete_event_simulation.util.generateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InstanceController {
    @Autowired
    FederateMapper federateMapper;

    @Autowired
    StartInformationMapper startInformationMapper;

    @Autowired
    FederateListMapper federateListMapper;

    @Autowired
    FederateVariableMapper federateVariableMapper;

    @Autowired
    FederateObjectMapper federateObjectMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionUpdatetimeperiodMapper instructionUpdateTimePeriodMapper;

    @Autowired
    InstructionFornumberMapper instructionFornumberMapper;

    @Autowired
    InstructionRandomintMapper instructionRandomIntMapper;

    @Autowired
    InstructionCreateMapper instructionCreateMapper;

    @Autowired
    InstructionListaddMapper instructionListAddMapper;

    @Autowired
    InstructionExpressionMapper instructionExpressionMapper;

    @Autowired
    InstructionLoopendMapper instructionLoopEndMapper;

    //动态option
    @RequestMapping(value = "/post/instanceSelect", method = RequestMethod.POST)
    @ResponseBody
    public Result listSelect(){
        Result result=new Result();
        List<federateObjectDTO> federateODTOS= federateObjectMapper.queryAll();
        if(federateODTOS!=null){
            result.setMsg("Query all succeed");
            result.setStateCode(200);
            result.setData(federateODTOS);
        } else{
            result.setMsg("Query failes,no tickets");
            result.setStateCode(404);
        }
        return result;
    }

    @RequestMapping(value = "/post/listSelect1", method = RequestMethod.POST)
    @ResponseBody
    public Result InstanceSelect(){
        Result result=new Result();
        List<String> allListNames= federateListMapper.findAllListName();
        if(allListNames!=null){
            result.setMsg("Query all succeed");
            result.setStateCode(200);
            result.setData(allListNames);
        } else{
            result.setMsg("Query failes,no tickets");
            result.setStateCode(404);
        }
        return result;
    }

    @RequestMapping(value = "/post/showChoosenInstance", method = RequestMethod.POST)
    @ResponseBody
    public Result postInstanceSelect(@RequestBody JSONObject jsonObject){
        int  instanceId= Integer.parseInt(jsonObject.getString("federateObjectId"));
        System.out.println(jsonObject.getString("federateObjectId"));
        Result result=new Result();
        FederateObject federateObject = federateObjectMapper.queryInstanceById(instanceId);
        String parameterNames = federateObject.getParameterNames();
        if(parameterNames!=null){
            List<String> instanceParameterNames = generateUtils.extractString(parameterNames);
            List<String> instanceParameterTypes = generateUtils.extractString(federateObject.getParameterTypes());
            List<instanceParameterDTO> dtos=new ArrayList<>();
            for(int i=0;i<instanceParameterNames.size();++i){
                instanceParameterDTO  dto=new instanceParameterDTO();
                dto.setInstanceName(instanceParameterNames.get(i));
                dto.setInstanceType(instanceParameterTypes.get(i));
                dtos.add(dto);
            }
            result.setMsg("Query all succeed");
            result.setStateCode(200);
            result.setData(dtos);
        }else {
            result.setMsg("Query failes,no tickets");
            result.setStateCode(404);
        }
        return result;
    }

    @RequestMapping(value = "/post/initialInstance",method = RequestMethod.POST)
    @ResponseBody
    public String postInitialInstance(@RequestParam("InstanceNumber")String InstanceNumber,
                                      @RequestParam("listName")String listName,
                                      @RequestParam("instanceId")String instanceId,
                                      @RequestParam("isRandom")String isRandom,
                                      @RequestParam("fromInt")String fromInt,
                                      @RequestParam("toInt")String toInt,
                                      @RequestParam("initialValue")String initialValue,
                                        @RequestParam("federate")String federate
    ){
        String[] isRandoms = isRandom.split(",");
        String[] fromInts = fromInt.split(",");
        String[] toInts = toInt.split(",");
        String[] initialValues = initialValue.split(",");

        int currentFederateId = federateMapper.findFederateIdByName(federate);
        initialInstanceProcess(InstanceNumber,listName,instanceId,isRandoms,fromInts,toInts,initialValues,currentFederateId);
        return "本联邦所需对象的设计信息已提交";
    }

    public void initialInstanceProcess(String InstanceNumber,String listName,String instanceId,String[] isRandoms,
                                       String[] fromInts,String[] toInts,String[] initialValues,int curFederateId ){
        List<String> instructionArray=new ArrayList<>();
        List<Integer> instructionIdArray=new ArrayList<>();
        //fornumber指令
        InstructionFornumber instructionFornumber=new InstructionFornumber();
        instructionFornumber.setLoopVariableName("i");
        instructionFornumber.setNumber(InstanceNumber);
        instructionFornumberMapper.insert(instructionFornumber);
        int forNumberID = instructionFornumberMapper.maxId();
        instructionIdArray.add(forNumberID);
        instructionArray.add("fornumber");

        //具体task
        FederateObject federateObject = federateObjectMapper.queryInstanceById(Integer.parseInt(instanceId));
        String[] ParameterNamesArray = federateObject.getParameterNames().split(",");
        String[] ParameterTypesArray = federateObject.getParameterTypes().split(",");
        int count=isRandoms.length;

        //create指令
        InstructionCreate create=new InstructionCreate();
        create.setObjectId(federateObject.getObjectId()+"");
        create.setOutName("createObject");
        StringBuilder createType=new StringBuilder();
        StringBuilder createName=new StringBuilder();
        for(int i=0;i<count;++i){
            if(Integer.parseInt(isRandoms[i])==0){
                //随机整数
                InstructionRandomint randomInt=new InstructionRandomint();
                randomInt.setFromInt(fromInts[i]);
                randomInt.setToInt(toInts[i]);
                randomInt.setOutName(ParameterNamesArray[i]);
                createName.append(ParameterNamesArray[i]+",");
                instructionRandomIntMapper.insert(randomInt);
                int randomId = instructionRandomIntMapper.maxId();
                instructionIdArray.add(randomId);
                instructionArray.add("randomint");
            }else if(Integer.parseInt(isRandoms[i])==1){
                //赋值
                InstructionExpression expression=new InstructionExpression();
                expression.setExpressionInformation("?");
                expression.setOutName(ParameterNamesArray[i]);
                expression.setOutType(ParameterTypesArray[i]);
                expression.setFillInformation(initialValues[i]);
                createName.append(ParameterNamesArray[i]+",");
                instructionExpressionMapper.insert(expression);
                int expId= instructionExpressionMapper.maxId();
                instructionIdArray.add(expId);
                instructionArray.add("expression");
            }else {
                //参数是一个对象
                createName.append(ParameterNamesArray[i]+",");
                InstructionCreate nullCreate=new InstructionCreate();
                nullCreate.setOutName(ParameterNamesArray[i]);
                int objectId=federateObjectMapper.queryObjectByName(ParameterTypesArray[i]);
                nullCreate.setObjectId(objectId+"");
                instructionCreateMapper.insertNullObject(nullCreate);
                instructionArray.add("create");
                int nullCreateId=instructionCreateMapper.maxId();
                instructionIdArray.add(nullCreateId);
            }
            createType.append("2,");
        }
        create.setTypeInformation(createType.toString());
        create.setActualInformation(createName.toString());
        instructionCreateMapper.insert(create);
        int createId=instructionCreateMapper.maxId();
        instructionArray.add("create");
        instructionIdArray.add(createId);
        //listAdd
        InstructionListadd listAdd=new InstructionListadd();
        listAdd.setListName(listName);
        listAdd.setObjectName("createObject");
        instructionListAddMapper.insert(listAdd);
        int listAddId=instructionListAddMapper.maxId();
        instructionArray.add("listAdd");
        instructionIdArray.add(listAddId);

        //loopend
        InstructionLoopend loopEnd=new InstructionLoopend();
        instructionLoopEndMapper.insert(loopEnd);
        int loopEndId=instructionLoopEndMapper.maxId();
        instructionArray.add("loopend");
        instructionIdArray.add(loopEndId);

        Task task2=new Task();
        int count2=instructionIdArray.size();
        StringBuilder sequence=new StringBuilder();
        StringBuilder sequenceId=new StringBuilder();
        for(int i=0;i<count2;++i){
            sequence.append(instructionArray.get(i)+",");
            sequenceId.append(instructionIdArray.get(i)+",");
        }
        task2.setInstructionSequence(sequence.toString());
        task2.setInstructionIds(sequenceId.toString());
        taskMapper.insertTask(task2);
        int taskMaxId=taskMapper.maxId();
        startInformationMapper.updateInitialInstanceTaskByFederateId(curFederateId,taskMaxId);
    }


    @RequestMapping(value = "/post/instanceSelect1", method = RequestMethod.POST)
    @ResponseBody
    public Result postInstanceSelect1(){
        Result result=new Result();
        List<federateObjectDTO> federateODTOS= federateObjectMapper.queryAll();
        if(federateODTOS!=null){
            result.setMsg("Query all succeed");
            result.setStateCode(200);
            result.setData(federateODTOS);
        } else{
            result.setMsg("Query failes,no tickets");
            result.setStateCode(404);
        }
        return result;
    }


    @RequestMapping(value = "/post/updateInstance",method = RequestMethod.POST)
    @ResponseBody
    public String postUpdateInstance(@RequestParam("instanceName")String instanceName,
                                     @RequestParam("activeListName")String activeListName,
                                     @RequestParam("dormantListName")String dormantListName){

        //thisPageService.updateInstanceProcess(instanceName,activeListName,dormantListName,this.curFederateId);
        InstructionUpdatetimeperiod timePeriod=new InstructionUpdatetimeperiod();
        timePeriod.setInstanceName(instanceName);
        timePeriod.setActiveListName(activeListName);
        timePeriod.setDormantListName(dormantListName);
        instructionUpdateTimePeriodMapper.insert(timePeriod);
        int instructionMaxId = instructionUpdateTimePeriodMapper.maxId();
        Task task=new Task();
        task.setInstructionIds(instructionMaxId+",");
        task.setInstructionSequence("TimePeriod");
        taskMapper.insertTask(task);
        int taskMaxId=taskMapper.maxId();
        //插入federateId
        //startInformationMapper.updateUpdateInstanceByFederateId(curFederateId,taskMaxId);
        return "本联邦所需对象的设计信息已提交";
    }
}

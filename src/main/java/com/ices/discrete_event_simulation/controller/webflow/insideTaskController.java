package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.HelpVariableDescription;
import com.ices.discrete_event_simulation.entity.InsideSelect;
import com.ices.discrete_event_simulation.entity.InsideTask;
import com.ices.discrete_event_simulation.entity.InstructionSelect;
import com.ices.discrete_event_simulation.mapper.*;
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
public class insideTaskController {
    //内部任务
    @Autowired
    InsideTaskMapper insidetaskMapper;

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    HelpVariableDescriptionMapper helpVariableDescriptionMapper;

    @Autowired
    FederateMapper federateMapper;

    @RequestMapping(value = "/insideTask/queryInsideTask", method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getallInsidetask(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<InsideTask> allInsidetask=insidetaskMapper.queryAll();
        for (InsideTask it : allInsidetask){
            HashMap<String,Object> element = new HashMap<>();
            element.put("InsidetaskId",it.getInsidetaskId());
            element.put("Information",it.getInformation());
            element.put("Iscomplete",it.getIscomplete());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //不是内部任务时候访问到的内容
    @RequestMapping(value = "/insideTask/notClickButton", method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse notClickButton(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        HashMap<String,Object> element = new HashMap<>();
        element.put("parameterName","暂无");
        element.put("parameterType","暂无");
        element.put("federateName","暂无");
        element.put("variableDescription","非内部任务无数据");
        listMap.add(element);

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //不是内部任务时候访问到的内容
    @RequestMapping(value = "/insideTask/clickButton", method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse clickButton(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<HelpVariableDescription> allHelpVariable=helpVariableDescriptionMapper.queryAll();
        for(HelpVariableDescription hvd:allHelpVariable){
            HashMap<String,Object> element = new HashMap<>();
            element.put("parameterName",hvd.getParameterName());
            element.put("parameterType",hvd.getParameterType());
            String federateNameById = federateMapper.findFederateNameById(hvd.getFederateId());
            element.put("federateName",federateNameById);
            element.put("variableDescription",hvd.getVariableDescription());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    @RequestMapping(value = "/insideTask/setInsideTaskID" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse postInsideTask(@RequestBody JSONObject json){
        String insidetaskId = json.get("InsidetaskId").toString();

        //设置当前的任务ID为内部任务的ID
        ActivitiEditLifeCycle.setTaskID(insidetaskId);

        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("taskId",insidetaskId );
        List<com.ices.discrete_event_simulation.entity.Task> taskList = taskMapper.selectByMap(columnMap);
        if(taskList.size()==0){
            //如果表中不存在taskID，任务ID插入任务表中
            taskMapper.insertTaskId(insidetaskId);
        }else{
            //当前流程中没有任务了
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"内部任务已存在！");
        }
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"已选择成功选择内部任务！");
    }

    @RequestMapping(value = "/insideTask/addInsideTask" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addInsideTask(@RequestParam("insidetaskId") String insidetaskId,
                                      @RequestParam("information") String information){

        InsideTask insidetask=new InsideTask();
        insidetask.setInsidetaskId(Integer.parseInt(insidetaskId));
        insidetask.setInformation(information);
        insidetask.setIscomplete("未完成");
        insidetaskMapper.insert(insidetask);
        taskMapper.insertTaskId(insidetaskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"已添加内部任务："+information);
    }

    @Autowired
    InsideSelectMapper insideSelectMapper;

    @RequestMapping(value = "/insideTask/addSelect", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateSelect(@RequestParam("selectId") String selectId,
                               @RequestParam("branch1Id") String branch1Id,
                               @RequestParam("branch2Id") String branch2Id){

        //现在内部任务中，将select任务标记为完成状态
        InsideSelect insideSelect=new InsideSelect();
        insideSelect.setId(Integer.parseInt(selectId));
        insideSelect.setIscomplete("已完成");
        insideSelectMapper.updateInsideSelect(insideSelect);

        InstructionSelect instructionselect=new InstructionSelect();
        instructionselect.setBranch1Id(branch1Id);
        instructionselect.setBranch2Id(branch2Id);
        //这里问一下
        instructionselect.setInformationName(selectId);
        insideSelectMapper.updateInstructionSelect(instructionselect);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"");
    }


}

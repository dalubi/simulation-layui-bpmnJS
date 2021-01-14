package com.ices.discrete_event_simulation.controller.post;

import com.ices.discrete_event_simulation.controller.webflow.ActivitiEditLifeCycle;
import com.ices.discrete_event_simulation.entity.InstructionCreate;
import com.ices.discrete_event_simulation.entity.InstructionObjectget;
import com.ices.discrete_event_simulation.entity.InstructionObjectset;
import com.ices.discrete_event_simulation.mapper.InstructionCreateMapper;
import com.ices.discrete_event_simulation.mapper.InstructionObjectgetMapper;
import com.ices.discrete_event_simulation.mapper.InstructionObjectsetMapper;
import com.ices.discrete_event_simulation.mapper.TaskMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.java2d.SurfaceDataProxy;


@RestController
public class ObjectController {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionCreateMapper instructionCreateMapper;

    @Autowired
    InstructionObjectgetMapper instructionObjectgetMapper;

    @Autowired
    InstructionObjectsetMapper instructionObjectsetMapper;

    //create
    @RequestMapping(value = "/inst/create",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_create(
            @RequestParam("objectId")String objectId,
            @RequestParam("typeInformation")String typeInformation,
            @RequestParam("actualInformation")String actualInformation,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionCreate create= new InstructionCreate();
        create.setActualInformation(actualInformation);
        create.setObjectId(objectId);
        create.setOutName(outName);
        instructionCreateMapper.insert(create);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionCreateMapper.maxId();
        sequence = sequence+"create,";
        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //objectget
    @RequestMapping(value = "/inst/objectget",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_objectget(
            @RequestParam("objectName")String objectName,
            @RequestParam("parameterName")String parameterName,
            @RequestParam("outType")String outType,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionObjectget objectget= new InstructionObjectget();
        objectget.setObjectName(objectName);
        objectget.setOutName(outName);
        objectget.setOutType(outType);
        objectget.setParameterName(parameterName);
        instructionObjectgetMapper.insert(objectget);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionObjectgetMapper.maxId();
        sequence = sequence+"objectget,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");

    }

    //objectget
    @RequestMapping(value = "/inst/objectset",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_objectset(
            @RequestParam("objectName")String objectName,
            @RequestParam("parameterName")String parameterName,
            @RequestParam("setName")String setName
    ){
        //插入指令 修改1
        InstructionObjectset objectset= new InstructionObjectset();
        objectset.setParameterName(parameterName);
        objectset.setSetName(setName);
        objectset.setObjectName(objectName);
        instructionObjectsetMapper.insert(objectset);

        //获得taskID
        String taskId = ActivitiEditLifeCycle.getTaskID();

        //根据taskID获得task对应的内容
        String sequence = taskMapper.findInstructionSequenceById(taskId);
        String interactionIds = taskMapper.findInstructionIdsById(taskId);

        if (sequence==null){
            sequence="";
        }
        if (interactionIds==null){
            interactionIds="";
        }

        //修改2
        int instructionId = instructionObjectsetMapper.maxId();
        sequence = sequence+"objectset,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }
}

package com.ices.discrete_event_simulation.controller.post;

import com.baomidou.mybatisplus.extension.api.R;
import com.ices.discrete_event_simulation.controller.webflow.ActivitiEditLifeCycle;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OtherController {

    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionExpressionMapper instructionExpressionMapper;

    @Autowired
    InstructionAssignMapper instructionAssignMapper;

    @Autowired
    InstructionLogoutputMapper instructionLogoutputMapper;

    @Autowired
    InstructionSendMapper instructionSendMapper;

    @Autowired
    InstructionRandomintMapper instructionrandomIntMapper;

    @Autowired
    InstructionRandomdoubleMapper instructionrandomDoubleMapper;

    @Autowired
    InstructionRandomordernameMapper instructionrandomOrderNameMapper;

    @Autowired
    InstructionRandomlocationnameMapper instructionrandomLocationNameMapper;

    @Autowired
    InstructionSimulationtimeMapper instructionSimulationTimeMapper;

    @Autowired
    InstructionRealtimeMapper instructionRealTimeMapper;

    @Autowired
    InstructionDelayMapper instructionDelayMapper;

    //expression
    @RequestMapping(value = "/inst/expression",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_expression(
            @RequestParam("expressionInformation")String expressionInformation,
            @RequestParam("fillInformation")String fillInformation,
            @RequestParam("outType")String outType,
            @RequestParam("outName")String outName
    ){
        //插入指令
        InstructionExpression instructionExpression= new InstructionExpression();
        instructionExpression.setExpressionInformation(expressionInformation);
        instructionExpression.setFillInformation(fillInformation);
        instructionExpression.setOutName(outName);
        instructionExpression.setOutType(outType);
        instructionExpressionMapper.insert(instructionExpression);

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

        int instructionId = instructionExpressionMapper.maxId();
        sequence = sequence+"expression,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //assign
    @RequestMapping(value = "/inst/assign",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_assign(
            @RequestParam("parameterType")String parameterType,
            @RequestParam("fillInformation")String fillInformation,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionAssign Assign= new InstructionAssign();
        Assign.setFillInformation(fillInformation);
        Assign.setOutName(outName);
        Assign.setParameterType(parameterType);
        instructionAssignMapper.insert(Assign);

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
        int instructionId = instructionAssignMapper.maxId();
        sequence = sequence+"assign,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //logOutput
    @RequestMapping(value = "/inst/logOutput",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_logOutput(
            @RequestParam("formatInformation")String formatInformation,
            @RequestParam("fillInformation")String fillInformation
    ){
        //插入指令 修改1
        InstructionLogoutput Logoutput = new InstructionLogoutput();
        Logoutput.setFillInformation(fillInformation);
        Logoutput.setFormatInformation(formatInformation);
        instructionLogoutputMapper.insert(Logoutput);

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
        int instructionId = instructionLogoutputMapper.maxId();
        sequence = sequence+"logoutput,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //send
    @RequestMapping(value = "/inst/send",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_send(
            @RequestParam("sendInteractionName")String sendInteractionName,
            @RequestParam("sendInformation")String sendInformation
    ){
        //插入指令 修改1
        InstructionSend Send= new InstructionSend();
        Send.setSendInformation(sendInformation);
        Send.setSendInteractionName(sendInteractionName);
        instructionSendMapper.insert(Send);

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
        int instructionId = instructionSendMapper.maxId();
        sequence = sequence+"send,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //randomInt
    @RequestMapping(value = "/inst/randomInt",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_randomInt(
            @RequestParam("fromInt")String fromInt,
            @RequestParam("toInt")String toInt,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionRandomint Randomint= new InstructionRandomint();
        Randomint.setFromInt(fromInt);
        Randomint.setOutName(outName);
        Randomint.setToInt(toInt);
        instructionrandomIntMapper.insert(Randomint);

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
        int instructionId = instructionrandomIntMapper.maxId();
        sequence = sequence+"randomint,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //randomDouble
    @RequestMapping(value = "/inst/randomDouble",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_randomDouble(
            @RequestParam("origin")String origin,
            @RequestParam("bound")String bound,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionRandomdouble randomDouble = new InstructionRandomdouble();
        randomDouble.setBound(bound);
        randomDouble.setOrigin(origin);
        randomDouble.setOutName(outName);
        instructionrandomDoubleMapper.insert(randomDouble);

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
        int instructionId = instructionrandomDoubleMapper.maxId();
        sequence = sequence+"randomdouble,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //randomOrderName
    @RequestMapping(value = "/inst/randomOrderName",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_randomOrderName(
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionRandomordername randomOrderName = new InstructionRandomordername();
        randomOrderName.setOutName(outName);
        instructionrandomOrderNameMapper.insert(randomOrderName);

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
        int instructionId = instructionrandomOrderNameMapper.maxId();
        sequence = sequence+"randomordername,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //randomLocationName
    @RequestMapping(value = "/inst/randomLocationName",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_randomLocationName(
            @RequestParam("outName")String outName
    ){
//插入指令 修改1
        InstructionRandomlocationname randomLocationName= new InstructionRandomlocationname();
        randomLocationName.setOutName(outName);
        instructionrandomLocationNameMapper.insert(randomLocationName);

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
        int instructionId = instructionrandomLocationNameMapper.maxId();
        sequence = sequence+"randomlocationname,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //SimulationTime
    @RequestMapping(value = "/inst/SimulationTime",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_SimulationTime(
            @RequestParam("outName")String outName
    ){
//插入指令 修改1
        InstructionSimulationtime SimulationTime= new InstructionSimulationtime();
        SimulationTime.setOutName(outName);
        instructionSimulationTimeMapper.insert(SimulationTime);

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
        int instructionId = instructionSimulationTimeMapper.maxId();
        sequence = sequence+"simulationtime,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //RealTime
    @RequestMapping(value = "/inst/RealTime",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_RealTime(
            @RequestParam("SimulationTime")String SimulationTime,
            @RequestParam("outName")String outName
    ){
//插入指令 修改1
        InstructionRealtime RealTime= new InstructionRealtime();
        RealTime.setOutName(outName);
        RealTime.setSimulationTime(SimulationTime);
        instructionRealTimeMapper.insert(RealTime);

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
        int instructionId = instructionRealTimeMapper.maxId();
        sequence = sequence+"realtime,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //delay
    @RequestMapping(value = "/inst/delay",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_delay(
            @RequestParam("delayTime")String delayTime
    ){
//插入指令 修改1
        InstructionDelay delay= new InstructionDelay();
        delay.setDelayTime(delayTime);
        instructionDelayMapper.insert(delay);

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
        int instructionId = instructionDelayMapper.maxId();
        sequence = sequence+"delay,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

}

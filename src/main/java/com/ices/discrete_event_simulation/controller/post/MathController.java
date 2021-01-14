package com.ices.discrete_event_simulation.controller.post;

import com.ices.discrete_event_simulation.controller.webflow.ActivitiEditLifeCycle;
import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.mapper.*;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MathController {

    //MathAbs
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    InstructionMathabsMapper instructionMathabsMapper;

    @Autowired
    InstructionMathexpMapper instructionMathexpMapper;

    @Autowired
    InstructionMathlogMapper instructionMathlogMapper;

    @Autowired
    InstructionMathpowMapper instructionMathpowMapper;

    @Autowired
    InstructionMathceilMapper instructionMathceilMapper;

    @Autowired
    InstructionMathfloorMapper instructionMathfloorMapper;

    @Autowired
    InstructionMathroundMapper instructionMathroundMapper;

    @Autowired
    InstructionGaussiandistributionMapper instructionGaussiandistributionMapper;

    @Autowired
    InstructionExpdistributionMapper instructionExpdistributionMapper;

    @Autowired
    InstructionPossiondistributionMapper instructionPossiondistributionMapper;

    //Mathabs
    @RequestMapping(value = "/inst/MathAbs",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MathAbs(
            @RequestParam("fillInformation")String fillInformation,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathabs Mathabs= new InstructionMathabs();
        Mathabs.setFillInformation(fillInformation);
        Mathabs.setOutName(outName);
        instructionMathabsMapper.insert(Mathabs);

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
        int instructionId = instructionMathabsMapper.maxId();
        sequence = sequence+"mathabs,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //MathExp
    @RequestMapping(value = "/inst/MathExp",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MathExp(
            @RequestParam("parameter")String parameter,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathexp Mathexp= new InstructionMathexp();
        Mathexp.setOutName(outName);
        Mathexp.setParameter(parameter);
        instructionMathexpMapper.insert(Mathexp);

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
        int instructionId = instructionMathexpMapper.maxId();
        sequence = sequence+"mathexp,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //MathLog
    @RequestMapping(value = "/inst/MathLog",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MathLog(
            @RequestParam("parameter")String parameter,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathlog Mathlog= new InstructionMathlog();
        Mathlog.setOutName(outName);
        Mathlog.setParameter(parameter);
        instructionMathlogMapper.insert(Mathlog);

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
        int instructionId = instructionMathlogMapper.maxId();
        sequence = sequence+"mathlog,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //MathPow
    @RequestMapping(value = "/inst/MathPow",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_MathPow(
            @RequestParam("base")String base,
            @RequestParam("index")String index,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathpow Mathpow= new InstructionMathpow();
        Mathpow.setBase(base);
        Mathpow.setIndex(index);
        Mathpow.setOutName(outName);
        instructionMathpowMapper.insert(Mathpow);

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
        int instructionId = instructionMathpowMapper.maxId();
        sequence = sequence+"mathpow,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //Mathceil
    @RequestMapping(value = "/inst/ceil",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_ceil(
            @RequestParam("parameter")String parameter,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathceil Mathceil= new InstructionMathceil();
        Mathceil.setOutName(outName);
        Mathceil.setParameter(parameter);
        instructionMathceilMapper.insert(Mathceil);

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
        int instructionId =instructionMathceilMapper.maxId();
        sequence = sequence+"mathceil,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //floor
    @RequestMapping(value = "/inst/floor",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_floor(
            @RequestParam("parameter")String parameter,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathfloor Mathfloor= new InstructionMathfloor();
        Mathfloor.setOutName(outName);
        Mathfloor.setParameter(parameter);
        instructionMathfloorMapper.insert(Mathfloor);

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
        int instructionId = instructionMathfloorMapper.maxId();
        sequence = sequence+"mathfloor,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //round
    @RequestMapping(value = "/inst/round",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_round(
            @RequestParam("parameter")String parameter,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionMathround Mathround= new InstructionMathround();
        Mathround.setOutName(outName);
        Mathround.setParameter(parameter);
        instructionMathroundMapper.insert(Mathround);

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
        int instructionId = instructionMathroundMapper.maxId();
        sequence = sequence+"mathround,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //GaussianDistribution
    @RequestMapping(value = "/inst/GaussianDistribution",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_GaussianDistribution(
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionGaussiandistribution Gaussiandistribution= new InstructionGaussiandistribution();
        Gaussiandistribution.setOutName(outName);
        instructionGaussiandistributionMapper.insert(Gaussiandistribution);

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
        int instructionId = instructionGaussiandistributionMapper.maxId();
        sequence = sequence+"gaussiandistribution,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //ExpDistribution
    @RequestMapping(value = "/inst/ExpDistribution",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_ExpDistribution(

            @RequestParam("lambda")String lambda,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionExpdistribution Expdistribution= new InstructionExpdistribution();
        Expdistribution.setLambda(lambda);
        Expdistribution.setOutName(outName);
        instructionExpdistributionMapper.insert(Expdistribution);

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
        int instructionId = instructionExpdistributionMapper.maxId();
        sequence = sequence+"expdistribution,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

    //PossionDistribution
    @RequestMapping(value = "/inst/PossionDistribution",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse Process_PossionDistribution(
            @RequestParam("lambda")String lambda,
            @RequestParam("outName")String outName
    ){
        //插入指令 修改1
        InstructionPossiondistribution Possiondistribution= new InstructionPossiondistribution();
        Possiondistribution.setLambda(lambda);
        Possiondistribution.setOutName(outName);
        instructionPossiondistributionMapper.insert(Possiondistribution);

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
        int instructionId = instructionPossiondistributionMapper.maxId();
        sequence = sequence+"Possiondistribution,";

        interactionIds=interactionIds+instructionId+",";
        taskMapper.updateSequenceAndinstructionIdsById(sequence,interactionIds,taskId);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),GlobalConfig.ResponseCode.SUCCESS.getDesc(),"ok");
    }

}

package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.InsideTask;
import com.ices.discrete_event_simulation.mapper.InsideTaskMapper;
import com.ices.discrete_event_simulation.mapper.TaskMapper;
import com.ices.discrete_event_simulation.pojo.instructionByType;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActivitiEditLifeCycle {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    InsideTaskMapper insidetaskMapper;

    @Autowired
    TaskMapper taskMapper;

    //当前任务定义一个变量保存下来，taskId
    private static String taskID;

    public static String getTaskID() {
        return taskID;
    }

    public static void setTaskID(String taskID) {
        ActivitiEditLifeCycle.taskID = taskID;
    }

    //获得当前任务的信息，可能都要修改
    @RequestMapping(value = "/activiti/getCurrentTaskInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllBPMNInstanceInfos(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        if(getTaskID().length()>10){
            //activiti的任务
            //查询activiti任务相关信息，放入map中
            Task ActivitiTask=taskService.createTaskQuery() // 创建任务查询
                    .taskId(getTaskID()) // 根据任务id查询
                    .singleResult();
            HashMap<String,Object> element = new HashMap<>();
            element.put("currentTaskId",ActivitiTask.getId());
            element.put("currentTask",ActivitiTask.getName());
            String[] description = ActivitiTask.getDescription().split(",");
            element.put("recvInteraction",description[0]);
            element.put("pubInteraction",description[1]);
            List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().list();
            for(ProcessInstance pi:instanceList){
                if(pi.getProcessInstanceId().equals(ActivitiDeployOrInstanceController.getCurrentProcessInstanceID())){
                    element.put("federate",pi.getProcessDefinitionKey());
                }
            }
            listMap.add(element);
        }else{
            //内部任务
            //查询内部任务相关信息，放入map中
            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("insidetaskId",getTaskID());
            InsideTask insideTask = insidetaskMapper.selectByMap(columnMap).get(0);

            HashMap<String,Object> element = new HashMap<>();
            element.put("currentTaskId",insideTask.getInsidetaskId());
            element.put("currentTask",insideTask.getInformation());
            element.put("recvInteraction","null");
            element.put("pubInteraction","null");
            element.put("federate","null");
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //多级选择
    @RequestMapping(value = "/activiti/getInstructionsByType",method = RequestMethod.POST )
    @ResponseBody
    public List<instructionByType> getInstructionsByType(@RequestBody JSONObject json){
        String instructionType = json.get("instructionType").toString();
        List<instructionByType> instructionByTypes = TypeInstructionMap.get(instructionType);
        if(flag==false){
            AllInstruction();
        }
        return TypeInstructionMap.get(instructionType);
    }

    //用一个HashMap包裹着 指令的汉语 指令的英文 指令对应的url
    static HashMap<String,List<instructionByType>> TypeInstructionMap = new HashMap<>();
    static boolean flag = false;
    private static void AllInstruction(){
        //逻辑控制类指令
        List<instructionByType> list1 = new ArrayList<>();
        list1.add(new instructionByType("if","if","暂无"));
        list1.add(new instructionByType("else if","else if","暂无"));
        list1.add(new instructionByType("else","else","暂无"));
        list1.add(new instructionByType("while","while","暂无"));
        list1.add(new instructionByType("forNumber","按次数遍历",""));
        list1.add(new instructionByType("forEach","列表遍历",""));
        list1.add(new instructionByType("select","select",""));
        list1.add(new instructionByType("break","break",""));
        list1.add(new instructionByType("continue","continue",""));
        list1.add(new instructionByType("loopend","loopend",""));
        list1.add(new instructionByType("executeTask","子任务标识",""));
        TypeInstructionMap.put("logic",list1);
        //对象类指令
        List<instructionByType> list2 = new ArrayList<>();
        list2.add(new instructionByType("create","创建对象","/post/create"));
        list2.add(new instructionByType("objectset","设置对象属性","/post/objectset"));
        list2.add(new instructionByType("objectget","获取对象属性","/post/objectget"));
        TypeInstructionMap.put("object",list2);
        //工具类指令
        List<instructionByType> list3 = new ArrayList<>();
        list3.add(new instructionByType("listSize","获得列表大小","/post/listsize"));
        list3.add(new instructionByType("listGet","在列表中根据属性值流式获取对象","/post/listget"));
        list3.add(new instructionByType("listGetRandom","随机从列表中获取一个对象",""));
        list3.add(new instructionByType("listremove","从列表中移除一个对象","/post/listremove"));
        list3.add(new instructionByType("listclear","列表清空","/post/listclear"));
        list3.add(new instructionByType("listAdd","向列表加入一个对象","/post/listadd"));
        list3.add(new instructionByType("MapGet","哈希表获取键值对",""));
        list3.add(new instructionByType("MapPut","哈希表放入键值对",""));
        list3.add(new instructionByType("containsKey","哈希表中是否存在该key",""));
        list3.add(new instructionByType("containsValue","哈希表中是否存在该Value",""));
        list3.add(new instructionByType("createStringBuilder","可变字符串",""));
        list3.add(new instructionByType("append","字符串追加内容",""));
        list3.add(new instructionByType("toString","对象转化为字符串",""));
        list3.add(new instructionByType("IntegerToString","数字转字符串",""));
        list3.add(new instructionByType("StringToInteger","字符串转数字",""));
        list3.add(new instructionByType("typeConversion","强制转换","/post/typeconversion"));
        TypeInstructionMap.put("tool",list3);
        //数学类指令
        List<instructionByType> list4 = new ArrayList<>();
        list4.add(new instructionByType("MathAbs","取绝对值","/post/mathabs"));
        list4.add(new instructionByType("MathExp","指数",""));
        list4.add(new instructionByType("MathLog","对数",""));
        list4.add(new instructionByType("MathPow","幂",""));
        list4.add(new instructionByType("ceil","向上取整",""));
        list4.add(new instructionByType("floor","向下取整",""));
        list4.add(new instructionByType("round","四舍五入取整",""));
        list4.add(new instructionByType("GaussianDistribution","高斯分布",""));
        list4.add(new instructionByType("ExpDistribution","指数分布",""));
        list4.add(new instructionByType("PossionDistribution","泊松分布",""));
        TypeInstructionMap.put("math",list4);
        //其他指令
        List<instructionByType> list5 = new ArrayList<>();
        list5.add(new instructionByType("expression","表达式（例?+?;?>?）","/post/expression"));
        list5.add(new instructionByType("assign","赋值",""));
        list5.add(new instructionByType("logOutput","日志输出",""));
        list5.add(new instructionByType("send","发送交互类","/post/send"));
        list5.add(new instructionByType("randomInt","随机整数","/post/randomint"));
        list5.add(new instructionByType("randomDouble","随机浮点数",""));
        list5.add(new instructionByType("randomOrderName","随机菜名","/post/randomordername"));
        list5.add(new instructionByType("randomLocationName","随机地点",""));
        list5.add(new instructionByType("SimulationTime","获取仿真时间 double",""));
        list5.add(new instructionByType("RealTime","获取仿真时间转换实际时间",""));
        list5.add(new instructionByType("delay","延时","/post/delay"));
        TypeInstructionMap.put("other",list5);
        //复合指令
        List<instructionByType> list6 = new ArrayList<>();
        list6.add(new instructionByType("UpdateTimePeriod","更新生命周期","/post/updatetimeperiod"));
        list6.add(new instructionByType("initialInstance","生成一批实例",""));
        TypeInstructionMap.put("complex",list6);
    }


    //获得当前任务的指令序列
    @RequestMapping(value = "/activiti/getCurrentTaskInstructionsInfo" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getCurrentTaskInstructionsInfo(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        String taskID = ActivitiEditLifeCycle.getTaskID();
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("taskId",taskID);
        List<com.ices.discrete_event_simulation.entity.Task> taskList = taskMapper.selectByMap(columnMap);

        if (taskList.size()==0){
            return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    listMap.size(),listMap);
        }

        String instructionIds = taskList.get(0).getInstructionIds();
        String instructionSequence = taskList.get(0).getInstructionSequence();

        //如果指令序列为空
        if (instructionSequence==null){
            instructionSequence="";
        }
        if (instructionIds==null){
            instructionIds="";
        }

        //如果指令序列不为空
        String[] ids = instructionIds.split(",");
        String[] insts = instructionSequence.split(",");

        for(int i=0;i<ids.length;i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("seqId",i);
            element.put("instruction",insts[i]);
            element.put("instId",ids[i]);
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }

    //completeCurrentTask
    //完成当前任务的编辑
    //选中federation
    @RequestMapping(value = "/activiti/completeCurrentTask" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setFederationInfo(@RequestBody JSONObject json){
        String taskID = ActivitiEditLifeCycle.getTaskID();
        if(taskID.length()>10){
            //完成Activiti任务
            taskService.complete(taskID);
            //获取新的任务
            String currentProcessInstanceID = ActivitiDeployOrInstanceController.getCurrentProcessInstanceID();
            List<Task> TaskList = taskService.createTaskQuery().processInstanceId(currentProcessInstanceID).list();
            if (TaskList.size()==0){
                //当前实例没有新的任务了
                return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                        GlobalConfig.ResponseCode.SUCCESS.getDesc()
                        ,"当前流程实例没有任务了，请完成其他任务！");
            }else{
                //如果有任务
                ActivitiEditLifeCycle.setTaskID(TaskList.get(0).getId());
                Map<String,Object> columnMap = new HashMap<>();
                columnMap.put("taskId",taskID);
                List<com.ices.discrete_event_simulation.entity.Task> taskList = taskMapper.selectByMap(columnMap);
                if(taskList.size()==0){
                    //如果表中不存在taskID，任务ID插入任务表中
                    taskMapper.insertTaskId(taskID);
                }
                return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                        GlobalConfig.ResponseCode.SUCCESS.getDesc()
                        ,"Activiti任务完成，请点击更新");
            }
        }else{
            InsideTask insideTask = new InsideTask();
            insideTask.setInsidetaskId(Integer.parseInt(taskID));
            insideTask.setIscomplete("完成");
            insidetaskMapper.updateById(insideTask);
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"内部任务完成，重新选择内部任务");
        }
    }
}

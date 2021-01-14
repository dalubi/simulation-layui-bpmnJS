package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.ices.discrete_event_simulation.entity.Federate;
import com.ices.discrete_event_simulation.mapper.FederateMapper;
import com.ices.discrete_event_simulation.mapper.TaskMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ActivitiDeployOrInstanceController {

    @Autowired
    FederateMapper federateMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    TaskMapper taskMapper;

    //实例要用runtimeService
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    //获取流程bpmn的部署信息
    @RequestMapping(value = "/activiti/getAllBPMNDeploy" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllBPMNInfos(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();

        List<Federate> federateList = federateMapper.selectList(null);

        for(int i=0;i<federateList.size();i++){
            HashMap<String,Object> element = new HashMap<>();
            element.put("federateName",federateList.get(i).getFederateName());
            //看看是否有deployment
            String hasInstance = "没有";
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for(Deployment d:list){
                String name = d.getName();
                if(name.equals(federateList.get(i).getFederateName())){
                    hasInstance="有";
                }
            }
            element.put("hasInstance",hasInstance);
            listMap.add(element);
        }

        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    //部署bpmn，文件部署，
    @RequestMapping(value = "/activiti/deployBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse uploadBPMNFile(@RequestParam("file") MultipartFile file)throws IOException {
        String fileName = file.getOriginalFilename();
        String[] split = fileName.split("\\.");
        String deploymentName = split[0];
        try{
            InputStream inputStream = file.getInputStream();
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(fileName,inputStream)
                    .name(deploymentName)
                    .deploy();
            runtimeService.startProcessInstanceByKey(deploymentName);
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"BPMN:"+deploymentName+"部署成功");
        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"BPMN:"+deploymentName+"部署失败");
        }
    }

    @Value("${file.processespath}")
    private String processespath;

    //现在我们用processes中的文件部署
    @RequestMapping(value = "/activiti/deployAndCreateInstance" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse uploadBPMNFile(@RequestBody JSONObject json) throws FileNotFoundException {
        String federate = json.get("federate").toString();
        String fileName = processespath+federate+".bpmn";
        File BpmnFile = new File(fileName);

        //读取文件内容
        FileInputStream fileInputStream = new FileInputStream(BpmnFile);

        try{
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(federate,fileInputStream)
                    .name(federate)
                    .deploy();
            runtimeService.startProcessInstanceByKey(federate);

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), deployment.getId()+";"+deployment.getId());
        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"String方式部署流程失败",e.toString());
        }
    }


    //获取流程bpmn的部署信息
    @RequestMapping(value = "/activiti/getAllBPMNInstance" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllBPMNInstanceInfos(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery().list();
        List<Task> taskList = taskService.createTaskQuery().list();
        for(ProcessInstance pi:instanceList){
            HashMap<String,Object> element = new HashMap<>();
            element.put("DefinitionKey",pi.getProcessDefinitionKey());
            element.put("ProcessInstanceId",pi.getProcessInstanceId());
            String currentTask = "";
            for(Task t:taskList){
                if(t.getProcessInstanceId().equals(pi.getProcessInstanceId())){
                    currentTask=t.getName();
                }
            }
            if(currentTask.equals("")){
                //说明没有找到对应任务
                currentTask="已完成";
            }
            element.put("currentTask",currentTask);
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    @Autowired
    private HistoryService historyService;

    //查询已完成的流程实例信息
    @RequestMapping(value = "/activiti/getCompletedInstance" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getCompletedInstance(){
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();

        for(HistoricProcessInstance hpi:list){
            HashMap<String,Object> element = new HashMap<>();
            element.put("DefinitionKey",hpi.getProcessDefinitionKey());
            element.put("ProcessInstanceId",hpi.getSuperProcessInstanceId());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                listMap.size(),listMap);
    }


    //删除仿真流程实例
    @RequestMapping(value = "/activiti/deleteFederateInstance" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteFederateListById(@RequestBody JSONObject json){
        String ProcessInstanceId = json.get("ProcessInstanceId").toString();
        runtimeService.deleteProcessInstance(ProcessInstanceId,"删除流程实例");
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除流程实例:"+ProcessInstanceId);
    }

    private static String currentProcessInstanceID;

    public static String getCurrentProcessInstanceID() {
        return currentProcessInstanceID;
    }

    public static void setCurrentProcessInstanceID(String currentProcessInstanceID) {
        ActivitiDeployOrInstanceController.currentProcessInstanceID = currentProcessInstanceID;
    }

    //设定选定的流程实例，从选中流程实例中获取当期的任务
    @RequestMapping(value = "/activiti/chooseFederateInstance" ,method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse chooseProcessInstance(@RequestBody JSONObject json){
        String ProcessInstanceId = json.get("ProcessInstanceId").toString();
        currentProcessInstanceID = ProcessInstanceId;
        //获得流程实例当前的任务
        List<Task> list = taskService.createTaskQuery().processInstanceId(currentProcessInstanceID).list();
        if(list.size()>0){
            //从流程实例的任务列表中，只获取一个任务的ID
            String taskID = list.get(0).getId();
            ActivitiEditLifeCycle.setTaskID(taskID);
            //把选中的任务的ID插入task表中
            Map<String,Object> columnMap = new HashMap<>();
            columnMap.put("taskId",taskID);
            List<com.ices.discrete_event_simulation.entity.Task> taskList = taskMapper.selectByMap(columnMap);
            if(taskList.size()==0){
                //如果表中不存在taskID，任务ID插入任务表中
                taskMapper.insertTaskId(taskID);
            }
        }else{
            //当前流程中没有任务了
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"选定的流程实例已无可编辑任务！");
        }
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"选定流程实例:"+ProcessInstanceId);
    }
}

package com.ices.discrete_event_simulation.service.generateXML;

import com.ices.discrete_event_simulation.entity.*;
import com.ices.discrete_event_simulation.util.generateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


@Controller
public class GenerateFederateXML {
    private Element simulationElement;
    private Element subtasksElement;
    private Element triggerTasksElement;

    public static GenerateFederateXML generateFederateXMLByCYF;

    @Autowired
    com.ices.discrete_event_simulation.mapper.generateDaoMapper generateDaoMapper;

    @PostConstruct
    public void init(){
        generateFederateXMLByCYF =this;
        generateFederateXMLByCYF.generateDaoMapper=this.generateDaoMapper;
    }

    public  void createXML(){
        // 1、创建document对象
        Document document = DocumentHelper.createDocument();
        // 2、创建根节点simulation
        simulationElement = document.addElement("Simulation");
        this.triggerTasksElement = simulationElement.addElement("TriggerTasks");
        this.subtasksElement = simulationElement.addElement("Subtasks");
        //加上子节点RunTimeControl
        RunTimeControlProcess();

        //加上子节点ExternalInformation
        ExternalInformationProcess();

        //加上Parameters节点
        ParametersProcess();

        //加上子节点Interactions
        InteractionsProcess();

//        //加上子节点Subtasks
//        SubtasksProcess();
//
//        //加上子节点TriggerTasks
//        TriggerTasksProcess();

        //加上子节点Federates
        FederatesProcess();




        try {
            //生成文件的一些处理
            GenerateFileAndOtherProcess(document);
        } catch (IOException e) {
            System.out.println("生成XML文件出错");
            e.printStackTrace();
        }

    }

    private  void FederatesProcess() {
        Element federatesElement = simulationElement.addElement("Federates");
        List<Federate> allFederate = generateDaoMapper.findAllFederate();

        for(Federate federate:allFederate){
            StartInformation sim = generateDaoMapper.findStartInformationById(federate.getFederateId());
            Element federateElement =federatesElement.addElement("Federate");
            if(sim.getIsFirst()!=-1){
                CommonFederateProcess(federateElement,sim);
            }else {
                ExternalFederateProcess(federateElement,sim);
            }

        }
    }

    private void ExternalFederateProcess(Element federateElement, StartInformation sim) {
        Federate federate = generateDaoMapper.findFederateById(sim.getFederateId());
        federateElement.attributeValue("name",federate.getFederateName());
        federateElement.attributeValue("isFirst","0");
        if(sim.getOwnEventTask()!=-1 && sim.getOwnEventTask()!=null){
            federateElement.attributeValue("hasOwnEvent","1");
        }else {
            federateElement.attributeValue("hasOwnEvent","0");
        }
        federateElement.attributeValue("isExternal","1");

    }

    //一步步填充每一个federate
    private void CommonFederateProcess(Element federateElement,StartInformation sim) {
        Federate federate = generateDaoMapper.findFederateById(sim.getFederateId());
        //先填充属性
        federateElement.attributeValue("name",federate.getFederateName());
        if(sim.getIsFirst()==1){
            federateElement.attributeValue("isFirst","1");
            FirstTaskGenerate(federateElement.addElement("FirstTask"),sim);
        }else {
            federateElement.attributeValue("isFirst","0");
        }
        if(sim.getOwnEventTask()!=-1 && sim.getOwnEventTask()!=null){
            federateElement.attributeValue("hasOwnEvent","1");
            OwnEventTaskGenerate(federateElement.addElement("OwnEventTask"),sim);
        }else {
            federateElement.attributeValue("hasOwnEvent","0");
        }
        federateElement.attributeValue("isExternal","0");

        //填充子节点
        InitialInstanceTaskGenerate(federateElement.addElement("InitialInstanceTask"),sim);
        UpdateInstanceTaskGenerate(federateElement.addElement("UpdateInstanceTask"),sim);
        PublishInteractionsGenerate(federateElement.addElement("PublishInteractions"),federate);
        SubscribeInteractionsGenerate(federateElement.addElement("SubscribeInteractions"),federate);
        FederateUtilsGenerate(federateElement.addElement("FederateUtils"),sim);

    }

    private void FederateUtilsGenerate(Element federateUtils, StartInformation sim) {
        //Lists
        Element ListsElement = federateUtils.addElement("Lists");
        generateLists(ListsElement,sim.getListIds());

        //Variables
        Element variablesElement = federateUtils.addElement("Variables");
        generateVariables(variablesElement,sim.getVariableIds());
        //Objects
        Element objectsElement = federateUtils.addElement("Objects");
        generateObjects(objectsElement,sim.getObjects());
        //Maps   保留 未完成
        //Element Maps = federateUtils.addElement("Maps");
    }

    private void generateObjects(Element objectsElement, String objects) {
        List<Integer> ids = generateUtils.extract(objects);
        for(Integer id:ids){
            FederateObject object = generateDaoMapper.findFederateObjectById(id);
            Element objectElement= objectsElement.addElement("object");
            objectElement.attributeValue("name",object.getObjectName());
            objectElement.attributeValue("parameterTypes",object.getParameterTypes());
            objectElement.attributeValue("parameterNames",object.getParameterNames());
//            objectElement.attributeValue("initialId",object.getInitialId());

        }
    }

    private void generateVariables(Element variablesElement, String variableIds) {
        List<Integer> ids = generateUtils.extract(variableIds);
        for(Integer id:ids){
            FederateVariable variable = generateDaoMapper.findFederateVariableById(id);
            Element variableElement= variablesElement.addElement("variable");
            variableElement.attributeValue("name",variable.getVariableName());
            variableElement.attributeValue("variableType",variable.getVariableType());
            variableElement.attributeValue("initialValue",variable.getInitialValue());
//            variableElement.attributeValue("isStatic",variable.getIsStatic());
//            variableElement.attributeValue("isFinal",variable.getIsFinal());

        }
    }

    private void generateLists(Element listsElement, String listIds) {
        List<Integer> ids = generateUtils.extract(listIds);
        for(Integer id:ids){
            FederateList list = generateDaoMapper.findFederateListById(id);
            Element listElement= listsElement.addElement("list");
            listElement.attributeValue("name",list.getListName());
//            listElement.attributeValue("type",list.getObjectName());

        }
    }

    private void SubscribeInteractionsGenerate(Element subscribeInteractions, Federate federate) {
        List<Integer> subscribeIds = generateUtils.extract(federate.getSubscribeInteractionIds());
        for(Integer interactionId:subscribeIds){
            Element interactionElement = subscribeInteractions.addElement("Interaction");
            interactionElement.attributeValue("interaction_id",String.valueOf(interactionId));
            EditInformation editInformation=generateDaoMapper.findEditInformationByInteractionId(federate.getFederateId(),interactionId);
//            Integer taskId=editInformation.getTaskId();
//            interactionElement.attributeValue("triggerTask_id",String.valueOf(taskId));
//            Task task=generateDaoMapper.findTaskByTaskId(taskId);
//            //加入triggertask
//            Element triggerTaskElement = triggerTasksElement.addElement("TriggerTask");
//            triggerTaskElement.attributeValue("sequence",task.getInstructionSequence());
//            triggerTaskElement.attributeValue("id",String.valueOf(taskId));
//            TaskProcess(triggerTaskElement,task);

        }
    }

    private void PublishInteractionsGenerate(Element publishInteractions, Federate federate) {
        List<Integer> publishIds = generateUtils.extract(federate.getPublishInteractionIds());
        for(Integer interactionId:publishIds){
            Element interactionElement = publishInteractions.addElement("Interaction");
            interactionElement.attributeValue("interaction_id",String.valueOf(interactionId));
        }
    }

    private void UpdateInstanceTaskGenerate(Element updateInstanceTask, StartInformation sim) {
        Task task = generateDaoMapper.findTaskByTaskId(sim.getOwnEventTask());
        String instructionSequence = task.getInstructionSequence();
        updateInstanceTask.attributeValue("sequence",instructionSequence);
        TaskProcess(updateInstanceTask,task);
    }

    private void InitialInstanceTaskGenerate(Element initialInstanceTask, StartInformation sim) {
        Task task = generateDaoMapper.findTaskByTaskId(sim.getOwnEventTask());
        String instructionSequence = task.getInstructionSequence();
        initialInstanceTask.attributeValue("sequence",instructionSequence);
        TaskProcess(initialInstanceTask,task);
    }

    private void OwnEventTaskGenerate(Element ownEventTask, StartInformation sim) {
        Task task = generateDaoMapper.findTaskByTaskId(sim.getOwnEventTask());
        String instructionSequence = task.getInstructionSequence();
        ownEventTask.attributeValue("sequence",instructionSequence);
        TaskProcess(ownEventTask,task);

    }

    private void FirstTaskGenerate(Element firstTask, StartInformation sim) {
            Task task = generateDaoMapper.findTaskByTaskId(sim.getFirstTask());
            String instructionSequence = task.getInstructionSequence();
            firstTask.attributeValue("sequence",instructionSequence);
            TaskProcess(firstTask,task);


    }
    //核心  处理task
    private void TaskProcess(Element element, Task task) {
        if(task==null || task.getInstructionSequence()==null)return;
        List<Integer> instructionIds = generateUtils.extract(task.getInstructionIds());
        List<String> instructionNames = generateUtils.extractString(task.getInstructionSequence());
        for(int i=0;i<instructionIds.size();++i){
            Integer id=instructionIds.get(i);
            String instructionName=instructionNames.get(i);
            Element instructionElement = element.addElement("Instruction");
            instructionElement.attributeValue("name",instructionName);
            //判断是否为子任务标志
            if(instructionName.toUpperCase().equals("EXECUTETASK")){
                Element subtaskElement=subtasksElement.addElement("Subtask");
                //获取指令对象  注入subtasks
                String tableName="Instruction_"+instructionName;
//                Object subtask=getInstructionByIdAndName(instructionName,id);
                Object subtask=generateDaoMapper.findInstructionByTableAndId(tableName,id);
                Object subtaskId = getFieldValueByName("taskId",subtask);
                instructionElement.attributeValue("id",String.valueOf(subtaskId));
                subtaskElement.attributeValue("id",String.valueOf(subtaskId));
                TaskProcess(subtaskElement,generateDaoMapper.findTaskByTaskId(id));
            }else {
                //获取指令对象  注入所有属性
                String tableName="Instruction_"+instructionName;
//                Object curInstruction=getInstructionByIdAndName(instructionName,id);
                Object curInstruction=generateDaoMapper.findInstructionByTableAndId(tableName,id);
                Object subtask=generateDaoMapper.findInstructionByTableAndId(tableName,id);
                String[] fieldNames = getFiledName(curInstruction);
                for(int j=0;j<fieldNames.length;++j){
                    Element  propertyElement= instructionElement.addElement("property");
                    String fieldName = fieldNames[j];
                    Object fieldValue = getFieldValueByName(fieldName,curInstruction);
                    propertyElement.attributeValue("name",fieldName);
                    propertyElement.attributeValue("value",String.valueOf(fieldValue));

                }
            }

        }


    }


    private  void InteractionsProcess() {
        Element interactionsElement = simulationElement.addElement("Interactions");
        List<Interaction> allInteraction = generateDaoMapper.findAllInteraction();
        for(Interaction interaction:allInteraction){
            Element element = interactionsElement.addElement("Interaction");
            element.attributeValue("name",interaction.getInteractionName());
            element.attributeValue("parameter_ids",interaction.getContainParameterIds());
        }
    }

    private  void ParametersProcess() {
        Element parametersElement = simulationElement.addElement("Parameters");
        List<Parameter> allParameter = generateDaoMapper.findAllParameter();
        for(Parameter parameter:allParameter){
            Element element = parametersElement.addElement("parameter");
            element.attributeValue("id",parameter.getParameterId()+"");
            element.attributeValue("name",parameter.getParameterName());
            element.attributeValue("type",parameter.getParameterType());
        }
    }
    /*
    外部事件未完待续
     */
    private  void ExternalInformationProcess() {
        Element externalInformationElement = simulationElement.addElement("ExternalInformation");

    }

    private  void RunTimeControlProcess() {
        Element runTimeControlElement = simulationElement.addElement("RunTimeControl");
        Element runTime = runTimeControlElement.addElement("RunTime");
//        FederationRunTimeControl runTimeControl = generateDaoMapper.findRunTimeControlById(generateDaoMapper.runtimeMaxId());
//        runTime.attributeValue("value",runTimeControl.getRuntime());
//        Element advanceStep = runTimeControlElement.addElement("AdvanceStep");
//        advanceStep.attributeValue("AdvanceStep",runTimeControl.getAdvanceStep());

    }

    private  void GenerateFileAndOtherProcess(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置编码格式
        format.setEncoding("UTF-8");

        // 6、生成xml文件
        File file = new File("Simulation.xml");
        XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);

        // 设置是否转义，默认使用转义字符
        writer.setEscapeText(false);
        writer.write(document);
        writer.close();
    }
//    /*
//      获取指令对象
//     */
//    public Object getInstructionByIdAndName(String instructionName,int instructionId){
//        Map<String,Object> columnMap = new HashMap<>();
//        columnMap.put("id",instructionId);
//        String tableName="Instruction_"+instructionName;
//
//        return federateMapper.selectByMap(columnMap).get(0);
//
//
//    }

    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /* 根据属性名获取属性值
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object value = method.invoke(o);
            return value;
        } catch (Exception e) {

            return null;
        }
    }
}

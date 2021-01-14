package com.ices.discrete_event_simulation.controller.activiti;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinationController {
    @Autowired
    private RepositoryService repositoryService;

    //添加流程定义（上传bpmn）
    @PostMapping(value = "/uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(
            @RequestParam("processFile") MultipartFile multipartFile,
            @RequestParam("deploymentName")String deploymentName) {
        // 获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();

        try {
            // 得到输入流（字节流）对象
            InputStream fileInputStream = multipartFile.getInputStream();

            // 文件的扩展名
            String extension = FilenameUtils.getExtension(fileName);

            Deployment deployment = null;
            if (extension.equals("zip")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()//初始化流程
                        .addZipInputStream(zip)
                        .name(deploymentName)
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()//初始化流程
                        .addInputStream(fileName, fileInputStream)
                        .name(deploymentName)
                        .deploy();
            }

            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), deployment.getId()+";"+fileName);

        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "部署流程失败", e.toString());
        }
    }
    //在线提交bpmn的XML
    @PostMapping(value = "/addDeploymentByString")
    public AjaxResponse addDeploymentByString(@RequestParam("stringBPMN")String stringBPMN) {

       try{
           Deployment deployment = repositoryService.createDeployment()
                   .addString("createWithBPMNJS.bpmn",stringBPMN)
                   .name("deploymentName")
                   .deploy();
           return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                   GlobalConfig.ResponseCode.SUCCESS.getDesc(), deployment.getId()+";"+deployment.getId());

       }catch (Exception e){
           return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),"String方式部署流程失败",e.toString());
       }
    }

    //获取流程定义列表
    @GetMapping("/getDefinations")
    public AjaxResponse getDefinations(){
        try{
            //如何作出像样的k-v对的数据
            List<HashMap<String,Object>> listMap = new ArrayList<HashMap<String,Object>>();

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            for(ProcessDefinition pd:list){
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",pd.getName());
                hashMap.put("key",pd.getKey());
                hashMap.put("resourceName",pd.getResourceName());
                hashMap.put("deploymentId",pd.getDeploymentId());
                hashMap.put("version",pd.getVersion());
                listMap.add(hashMap);
            }
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    listMap
            );
        }catch (Exception e){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程定义失败",
                    e.toString());
        }
    }

    //获取流程定义XML
    @GetMapping(value = "/getDefinitionXML")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName
    ) {
        try {
            //获取bpmn的文件xml
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId,resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            //获得输出流
            OutputStream outputStream = response.getOutputStream();
            //把数据流放到输出流中
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            e.toString();
        }
    }

    //获取流程部署列表
    @GetMapping(value = "/getDeployments")
    public AjaxResponse getDeployments() {
        try {
            List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
                listMap.add(hashMap);
            }
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(), listMap);
        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "查询失败", e.toString());
        }
    }

    //删除流程定义
    //删除流程定义
    @GetMapping(value = "/delDefinition")
    public AjaxResponse delDefinition(@RequestParam("pdID") String pdID) {
        try {
            repositoryService.deleteDeployment(pdID, true);
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    "删除成功", null);


        } catch (Exception e) {
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    "删除失败", e.toString());
        }
    }

    //文件的上传后台
    @PostMapping(value = "/upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam("processFile") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            System.out.println("文件为空");
        }
        String fileName = multipartFile.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = GlobalConfig.BPMN_PathMapping; // 上传后的路径

        //本地路径格式转上传路径格式
        //filePath = filePath.replace("\\", "/");
        filePath = filePath.replace("file:", "");

        //String filePath = "/Users/america/Java_study/activiti7_webflow/src/main/resources/resources/bpmn/";

        // String filePath = request.getSession().getServletContext().getRealPath("/") + "bpmn/";
        //上传同一个文件多次，避免名字不对
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File file = new File(filePath + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(file);//上传文件的代码
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(), fileName);
    }
}

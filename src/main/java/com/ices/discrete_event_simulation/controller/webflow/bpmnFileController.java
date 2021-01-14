package com.ices.discrete_event_simulation.controller.webflow;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ices.discrete_event_simulation.entity.FederateInfo;
import com.ices.discrete_event_simulation.entity.FederationBpmnNickname;
import com.ices.discrete_event_simulation.mapper.FederateInfoMapper;
import com.ices.discrete_event_simulation.mapper.FederationBpmnNicknameMapper;
import com.ices.discrete_event_simulation.service.parseXML.XMLDealService;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.AjaxTableResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;

import org.dom4j.DocumentException;
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
public class bpmnFileController {

    @Value("${file.federationBPMNPath}")
    private String federationBPMNPath;

    @Value("${file.filepath}")
    private String filepath;

    @Autowired
    XMLDealService xmlservice;

    @Autowired
    FederationBpmnNicknameMapper federationBpmnNicknameMapper;

    //bpmn文件的上传
    @RequestMapping(value = "/uploadBpmnFile" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse uploadBPMNFile(@RequestParam("file") MultipartFile file)throws IOException {
        //上传文件路径保存路径
        String path = federationBPMNPath;
        //上传文件名
        String bpmnFileName = file.getOriginalFilename();//文件的原本名称
        File tempFile = new File(path + File.separator + bpmnFileName);
        file.transferTo(tempFile);//文件写入

        //文件存放在本地之后，写入nickName的表
        FederationBpmnNickname nickname = new FederationBpmnNickname();
        nickname.setFederationName(bpmnFileName);
        nickname.setFederationNickName("");
        federationBpmnNicknameMapper.insert(nickname);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"Federation的BPMN图成功上传");
    }

    //bpmn文件建立别名
    @RequestMapping(value = "/setBPMNNickName" , method = RequestMethod.POST)
    @ResponseBody
    //注意这里的JSONObject是fastjson
    public AjaxResponse setBPMNnickName(@RequestBody JSONObject json){
        FederationBpmnNickname nickname = new FederationBpmnNickname();
        nickname.setFederationName(json.get("federationName").toString());
        nickname.setFederationNickName(json.get("nickName").toString());

        //查询
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federationName",nickname.getFederationName());
        List<FederationBpmnNickname> federationNickNameList = federationBpmnNicknameMapper.selectByMap(columnMap);
        if(federationNickNameList.size()==0){
            //插入
            federationBpmnNicknameMapper.insert(nickname);
        }else{
            //更新
            nickname.setFederationId(federationNickNameList.get(0).getFederationId());
            federationBpmnNicknameMapper.updateById(nickname);
        }

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"更新"+nickname.getFederationName()+"的别名为"+nickname.getFederationNickName());
    }

    //获取Federation的详细信息
    @RequestMapping(value = "/getAllFederations" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederationFileContent(){
        List<FederationBpmnNickname> federationBpmnNicknameList = federationBpmnNicknameMapper.selectList(null);
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<federationBpmnNicknameList.size();i++) {
            HashMap<String, Object> element = new HashMap<>();
            element.put("federationName", federationBpmnNicknameList.get(i).getFederationName());
            element.put("nickName", federationBpmnNicknameList.get(i).getFederationNickName());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                federationBpmnNicknameList.size(),listMap);
    }

    //解析BPMN
    @RequestMapping(value = "/parseFederationBpmn" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse parseFederation(@RequestBody JSONObject json) throws IOException, DocumentException {
        //解析文件夹中的文件，从json中获取文件名
        String bpmnFile = json.get("bpmnFile").toString();

        String path = filepath+bpmnFile;

        //用parser解析文件
        xmlservice.parse(path);

        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"解析bpmn文件:"+bpmnFile);
    }

    //删除Federation的bpmn
    @RequestMapping(value = "/deleteFederationBpmn" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteFederation(@RequestBody JSONObject json){
        String bpmnFile = json.get("bpmnFile").toString();
        //别名数据库删除
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("federationName",bpmnFile);
        federationBpmnNicknameMapper.deleteByMap(columnMap);
        //文件夹下删除
        String path = federationBPMNPath;
        File toBeDelete = new File(path+File.separator+bpmnFile);
        toBeDelete.delete();
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除bpmn文件:"+bpmnFile);
    }

    @Autowired
    FederateInfoMapper federateInfoMapper;

    @Value("${file.processesfilefolder}")
    private String processesfilefolder;

    //把federate的信息更新
    @RequestMapping(value = "/getAllFederates" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxTableResponse getAllFederateContent(){
        //首先检查processes文件，每个文件名保证在federateInfo表中存在数据
        String path = processesfilefolder;
        File[] files = new File(path).listFiles();
        //这里可以改成文件夹的形式
        for(int i=0;i<files.length;i++){
            //根据文件名，如果没有找到就更新
            String name = files[i].getName();
            if (name.endsWith(".bpmn")){
                FederateInfo federateInfo1 = new FederateInfo();
                federateInfo1.setReupload("");
                federateInfo1.setFederate(name.split(".bpmn")[0]);
                federateInfo1.setFederation("");

                Map<String,Object> columnMap = new HashMap<>();
                columnMap.put("federate",federateInfo1.getFederate());
                if (federateInfoMapper.selectByMap(columnMap).size()==0){
                    federateInfoMapper.insert(federateInfo1);
                }
            }
        }

        List<FederateInfo> federateInfoList = federateInfoMapper.selectList(null);
        List<HashMap<String, Object>> listMap= new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<federateInfoList.size();i++) {
            HashMap<String, Object> element = new HashMap<>();
            element.put("federate", federateInfoList.get(i).getFederate());
            element.put("federation", federateInfoList.get(i).getFederation());
            element.put("reupload",federateInfoList.get(i).getReupload());
            listMap.add(element);
        }
        return AjaxTableResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                federateInfoList.size(),listMap);
    }

    //更新Federate的信息
    @RequestMapping(value = "/setFederateInfo" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse setFederateInfo(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        String reupload = json.get("reupload").toString();
        //mbp 修改值
        FederateInfo federateInfo = new FederateInfo();
        federateInfo.setReupload(reupload);
        UpdateWrapper<FederateInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("federate",federate);
        int update = federateInfoMapper.update(federateInfo, wrapper);
        if(update==1){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"更新标记");
        }else{
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    GlobalConfig.ResponseCode.ERROR.getDesc()
                    ,"更新标记错误");
        }
    }

    //下载BPMN
    @RequestMapping(value = "/downloadBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse downloadBPMN(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        String reupload = json.get("reupload").toString();
        //mbp 修改值
        FederateInfo federateInfo = new FederateInfo();
        federateInfo.setReupload(reupload);
        UpdateWrapper<FederateInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("federate",federate);
        int update = federateInfoMapper.update(federateInfo, wrapper);
        if(update==1){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"更新标记");
        }else{
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    GlobalConfig.ResponseCode.ERROR.getDesc()
                    ,"更新标记错误");
        }
    }

    //下载BPMN
    @RequestMapping(value = "/bpmnFederate/downloadBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse downloadFederateBPMN(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        String reupload = json.get("reupload").toString();
        //mbp 修改值
        FederateInfo federateInfo = new FederateInfo();
        federateInfo.setReupload(reupload);
        UpdateWrapper<FederateInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("federate",federate);
        int update = federateInfoMapper.update(federateInfo, wrapper);
        if(update==1){
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc()
                    ,"更新标记");
        }else{
            return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                    GlobalConfig.ResponseCode.ERROR.getDesc()
                    ,"更新标记错误");
        }
    }

    //重传BPMN
    @RequestMapping(value = "/bpmnFederate/reuploadBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse reuploadBPMN(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        //重新上传bpmn文件，覆盖原来的文件，然后更新federate-info表
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                GlobalConfig.ResponseCode.ERROR.getDesc()
                ,"更新标记错误");

    }

    //查看BPMN
    @RequestMapping(value = "/bpmnFederate/viewBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse viewBPMN(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        //拼接出文件名  processesfilefolder
        String path = processesfilefolder+"/"+federate+".bpmn";
        //将文件读取成String字符串
        String bpmnStr = readToString(path);
        //把字符串发送给解析的网页，展示
        //把字节交给解析的后台代码
        currentBPMNStr=bpmnStr;
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.ERROR.getCode(),
                GlobalConfig.ResponseCode.ERROR.getDesc()
                ,"更新标记错误");

    }

    //giveBPMNStr
    @RequestMapping(value = "/bpmnFederate/getBPMNStr" , method = RequestMethod.POST)
    @ResponseBody
    public String getBPMNStr(){
        return currentBPMNStr;
    }



    //将文件读取成String字符串
    private static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    private static String currentBPMNStr = null;

    public static String getCurrentBPMNStr() {
        return currentBPMNStr;
    }

    public static void setCurrentBPMNStr(String currentBPMNStr) {
        bpmnFileController.currentBPMNStr = currentBPMNStr;
    }

    //删除BPMN
    @RequestMapping(value = "/bpmnFederate/deleteBPMN" , method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteBPMN(@RequestBody JSONObject json){
        String federate = json.get("federate").toString();
        //删除process下面的文件
        //删除federate—info表下面的数据
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"删除Federate的BPMN文件");
    }


    //新的仿真项目运行前的数据清理操作，数据库部分还没有写
    @RequestMapping(value = "/cleanData" , method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse cleanData(){
        //数据清理的数据库操作
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc()
                ,"数据清理完成!");
    }

}

package com.ices.discrete_event_simulation.ZMPGenerator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//mybatis Plus 用来生成 代码
public class GenerateFiles {
    public static void main(String[] args) {
        //创建Generator对象
        AutoGenerator autoGenerator = new AutoGenerator();
        //数据源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/des_simu?characterEncoding=UTF-8");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("haohao123");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");

        autoGenerator.setDataSource(dataSourceConfig);

        //全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        //生成代码放入的地方
        globalConfig.setOutputDir(System.getProperty("user.dir")+"/src/main/java");
        globalConfig.setOpen(false);//创建好不打开
        globalConfig.setAuthor("zth");
        autoGenerator.setGlobalConfig(globalConfig);

        //包信息
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.ices.discrete_event_simulation");
        packageConfig.setController("ZMPGenerator.controller");
        packageConfig.setService("ZMPGenerator.service");
        packageConfig.setServiceImpl("ZMPGenerator.service.impl");
        packageConfig.setMapper("ZMPGenerator.mapper");
        packageConfig.setEntity("ZMPGenerator.entity");
        autoGenerator.setPackageInfo(packageConfig);

        //配置策略
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setEntityLombokModel(true);//自动增加lombok注解
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);//数据库下划线转驼峰
        autoGenerator.setStrategy(strategyConfig);

        autoGenerator.execute();

        //删除Activiti的表
        List<String> fileFolders = new ArrayList<>();
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/service/base/impl");
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/mapper");
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/entity");
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/controller/base");
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/service/base");
        fileFolders.add("src/main/java/com/ices/discrete_event_simulation/ZMPGenerator/mapper/xml");
        for(int i=0;i<fileFolders.size();i++){
            String fileFolderName = fileFolders.get(i);
            File fileFolder = new File(fileFolderName);
            File[] files = fileFolder.listFiles();

            for(int j=0;j<files.length;j++){
                String name = files[j].getName();
                boolean matches1 = Pattern.matches("Act[\\w]+.java", name);
                boolean matches2 = Pattern.matches("IAct[\\w]+.java",name);
                boolean matches3 = Pattern.matches("Act[\\w]+.xml",name);

                boolean matches = matches1||matches2||matches3;
                if(matches && files[j].isFile() && files[j].exists() ){
                    files[j].delete();
                }
            }

        }
    }
}

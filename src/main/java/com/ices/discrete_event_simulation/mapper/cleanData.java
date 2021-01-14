package com.ices.discrete_event_simulation.mapper;

//import org.apache.commons.dbcp.BasicDataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class cleanData {

    public static String DBDRIVER = "com.mysql.jdbc.Driver";

    public static String DB_URL = "jdbc:mysql://localhost:3306/des_simu?user=root&password=haohao123&useSSL=false&serverTimezone=GMT";

    public static BasicDataSource bds = null;

    public static void cleanSimulationData(){
        Statement stmt = null;
        Connection conn = null;

        try{
            Class.forName(DBDRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            stmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
            stmt.addBatch("DROP TABLE ACT_EVT_LOG");
            stmt.addBatch("DROP TABLE ACT_GE_BYTEARRAY");
            stmt.addBatch("DROP TABLE ACT_GE_PROPERTY");
            stmt.addBatch("DROP TABLE ACT_HI_ACTINST");
            stmt.addBatch("DROP TABLE ACT_HI_ATTACHMENT");
            stmt.addBatch("DROP TABLE ACT_HI_COMMENT");
            stmt.addBatch("DROP TABLE ACT_HI_DETAIL");
            stmt.addBatch("DROP TABLE ACT_HI_IDENTITYLINK");
            stmt.addBatch("DROP TABLE ACT_HI_PROCINST");
            stmt.addBatch("DROP TABLE ACT_HI_TASKINST");
            stmt.addBatch("DROP TABLE ACT_HI_VARINST");
            stmt.addBatch("DROP TABLE ACT_PROCDEF_INFO");
            stmt.addBatch("DROP TABLE ACT_RE_DEPLOYMENT");
            stmt.addBatch("DROP TABLE ACT_RE_MODEL");
            stmt.addBatch("DROP TABLE ACT_RE_PROCDEF");
            stmt.addBatch("DROP TABLE ACT_RU_DEADLETTER_JOB");
            stmt.addBatch("DROP TABLE ACT_RU_EVENT_SUBSCR");
            stmt.addBatch("DROP TABLE ACT_RU_EXECUTION");
            stmt.addBatch("DROP TABLE ACT_RU_IDENTITYLINK");
            stmt.addBatch("DROP TABLE ACT_RU_INTEGRATION");
            stmt.addBatch("DROP TABLE ACT_RU_JOB");
            stmt.addBatch("DROP TABLE ACT_RU_SUSPENDED_JOB");
            stmt.addBatch("DROP TABLE ACT_RU_TASK");
            stmt.addBatch("DROP TABLE ACT_RU_TIMER_JOB");
            stmt.addBatch("DROP TABLE ACT_RU_VARIABLE");
            stmt.addBatch("SET FOREIGN_KEY_CHECKS=1");

            stmt.addBatch("TRUNCATE TABLE edit_information");
            stmt.addBatch("TRUNCATE TABLE external_information");
            stmt.addBatch("TRUNCATE TABLE federate");
            stmt.addBatch("TRUNCATE TABLE federate_info");
            stmt.addBatch("TRUNCATE TABLE federate_list");
            stmt.addBatch("TRUNCATE TABLE federate_map");
            stmt.addBatch("TRUNCATE TABLE federate_object");
            stmt.addBatch("TRUNCATE TABLE federate_variable");
            stmt.addBatch("TRUNCATE TABLE help_variable_description");
            stmt.addBatch("TRUNCATE TABLE inside_select");
            stmt.addBatch("TRUNCATE TABLE inside_task");
            stmt.addBatch("TRUNCATE TABLE instruction_append");
            stmt.addBatch("TRUNCATE TABLE instruction_assign");
            stmt.addBatch("TRUNCATE TABLE instruction_break");
            stmt.addBatch("TRUNCATE TABLE instruction_containskey");
            stmt.addBatch("TRUNCATE TABLE instruction_containsvalue");
            stmt.addBatch("TRUNCATE TABLE instruction_continue");
            stmt.addBatch("TRUNCATE TABLE instruction_create");
            stmt.addBatch("TRUNCATE TABLE instruction_createstringbuilder");
            stmt.addBatch("TRUNCATE TABLE instruction_delay");
            stmt.addBatch("TRUNCATE TABLE instruction_else");
            stmt.addBatch("TRUNCATE TABLE instruction_elseif");
            stmt.addBatch("TRUNCATE TABLE instruction_executetask");
            stmt.addBatch("TRUNCATE TABLE instruction_expdistribution");
            stmt.addBatch("TRUNCATE TABLE instruction_expression");
            stmt.addBatch("TRUNCATE TABLE instruction_foreach");
            stmt.addBatch("TRUNCATE TABLE instruction_fornumber");
            stmt.addBatch("TRUNCATE TABLE instruction_gaussiandistribution");
            stmt.addBatch("TRUNCATE TABLE instruction_if");
            stmt.addBatch("TRUNCATE TABLE instruction_listadd");
            stmt.addBatch("TRUNCATE TABLE instruction_listclear");
            stmt.addBatch("TRUNCATE TABLE instruction_listget");
            stmt.addBatch("TRUNCATE TABLE instruction_listgetrandom");
            stmt.addBatch("TRUNCATE TABLE instruction_listremove");
            stmt.addBatch("TRUNCATE TABLE instruction_listsize");
            stmt.addBatch("TRUNCATE TABLE instruction_logoutput");
            stmt.addBatch("TRUNCATE TABLE instruction_loopend");
            stmt.addBatch("TRUNCATE TABLE instruction_mapget");
            stmt.addBatch("TRUNCATE TABLE instruction_mapput");
            stmt.addBatch("TRUNCATE TABLE instruction_mathabs");
            stmt.addBatch("TRUNCATE TABLE instruction_mathacos");
            stmt.addBatch("TRUNCATE TABLE instruction_mathasin");
            stmt.addBatch("TRUNCATE TABLE instruction_mathatan");
            stmt.addBatch("TRUNCATE TABLE instruction_mathceil");
            stmt.addBatch("TRUNCATE TABLE instruction_mathcos");
            stmt.addBatch("TRUNCATE TABLE instruction_mathexp");
            stmt.addBatch("TRUNCATE TABLE instruction_mathfloor");
            stmt.addBatch("TRUNCATE TABLE instruction_mathlog");
            stmt.addBatch("TRUNCATE TABLE instruction_mathpow");
            stmt.addBatch("TRUNCATE TABLE instruction_mathround");
            stmt.addBatch("TRUNCATE TABLE instruction_mathsin");
            stmt.addBatch("TRUNCATE TABLE instruction_mathtan");
            stmt.addBatch("TRUNCATE TABLE instruction_objectget");
            stmt.addBatch("TRUNCATE TABLE instruction_objectset");
            stmt.addBatch("TRUNCATE TABLE instruction_possiondistribution");
            stmt.addBatch("TRUNCATE TABLE instruction_randomdouble");
            stmt.addBatch("TRUNCATE TABLE instruction_randomint");
            stmt.addBatch("TRUNCATE TABLE instruction_randomlocationname");
            stmt.addBatch("TRUNCATE TABLE instruction_randomordername");
            stmt.addBatch("TRUNCATE TABLE instruction_realtime");
            stmt.addBatch("TRUNCATE TABLE instruction_select");
            stmt.addBatch("TRUNCATE TABLE instruction_send");
            stmt.addBatch("TRUNCATE TABLE instruction_simulationtime");
            stmt.addBatch("TRUNCATE TABLE instruction_stringtointeger");
            stmt.addBatch("TRUNCATE TABLE instruction_tostring");
            stmt.addBatch("TRUNCATE TABLE instruction_typeconversion");
            stmt.addBatch("TRUNCATE TABLE instruction_updatetimeperiod");
            stmt.addBatch("TRUNCATE TABLE instruction_while");
            stmt.addBatch("TRUNCATE TABLE interaction");
            stmt.addBatch("TRUNCATE TABLE parameter");
            stmt.addBatch("TRUNCATE TABLE runtime_control");
            stmt.addBatch("TRUNCATE TABLE start_information");
            stmt.addBatch("TRUNCATE TABLE task");

            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("数据清理完成！");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

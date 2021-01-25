package com.ices.discrete_event_simulation.mapper;


import com.ices.discrete_event_simulation.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface generateDaoMapper {
    @Select("select * from federate")
    List<Federate> findAllFederate();

    @Select("select * from federate where federateId=#{federateId}")
    Federate findFederateById(Integer federateId);

    @Select("select * from interaction")
    List<Interaction> findAllInteraction();

    @Select("select * from interaction where interactionId=#{interactionId}")
    Interaction findInteractionById(Integer interactionId);

    @Select("select * from parameter")
    List<Parameter> findAllParameter();

    @Select("select * from parameter where parameterId=#{parameterId}")
    Parameter findParameterById(Integer parameterId);

    @Select("select * from startInformation where federateId=#{federateId}")
    StartInformation findStartInformationById(Integer federateId);



    @Select("select * from editInformation where fromFederateId=#{federateId}  and fromInteractionId=#{interactionId} ")
    EditInformation findEditInformationByInteractionId(@Param("federateId") Integer federateId, @Param("interactionId") Integer interactionId);

    @Select("select * from Task where taskId=#{taskId}")
    Task findTaskByTaskId(Integer taskId);

    @Select("select * from federateObject where objectId=#{objectId}")
    FederateObject findFederateObjectById(Integer objectId);

    @Select("select * from federateList where listId=#{listId}")
    FederateList findFederateListById(Integer listId);

    @Select("select * from federateVariable where variableId=#{variableId}")
    FederateVariable findFederateVariableById(Integer variableId);

    @Select("select * from externalInformation where Id=#{Id} ")
    ExternalInformation findExternalInformation(Integer Id);

    @Select("select * from federateObject")
    List<FederateObject> findFederateObject();

    @Select("select * from instructionselect where selectId=#{instructionId}")
    InstructionSelect findInstructionSelectById(Integer instructionId);

    @Select("select * from instructionCreate where createId=#{instructionId}")
    InstructionCreate findInstructionCreateById(Integer instructionId);

    @Select("select * from instructionDelay where delayId=#{instructionId}")
    InstructionDelay findInstructionDelayById(Integer instructionId);

    @Select("select * from instructionListAdd where addId=#{instructionId}")
    InstructionListadd findInstructionListAddById(Integer instructionId);

    @Select("select * from instructionListRemove where removeId=#{instructionId}")
    InstructionListremove findInstructionListRemoveById(Integer instructionId);

    @Select("select * from instructionObjectGet where objectGetId=#{instructionId}")
    InstructionObjectget findInstructionObjectGetById(Integer instructionId);

    @Select("select * from instructionObjectSet where objectSetId=#{instructionId}")
    InstructionObjectset findInstructionObjectSetById(Integer instructionId);

    @Select("select * from instructionSend where sendId=#{instructionId}")
    InstructionSend findInstructionSendById(Integer instructionId);

    @Select("select * from instructionListSize where sizeId=#{instructionId}")
    InstructionListsize findInstructionListSizeById(Integer instructionId);

    @Select("select * from instructionRandomInt where randomId=#{instructionId}")
    InstructionRandomint findInstructionRandomIntById(Integer instructionId);

    @Select("select * from instructionRandomOrderName where randomId=#{instructionId}")
    InstructionRandomordername findInstructionRandomOrderNameById(Integer instructionId);

    @Select("select * from InstructionExpression where expressionId=#{instructionId}")
    InstructionExpression findInstructionExpressionById(Integer instructionId);

    @Select("select * from InstructionMathAbs where id=#{instructionId}")
    InstructionMathabs findInstructionMathAbsById(Integer instructionId);

    @Select("select * from InstructionForNumber where id=#{instructionId}")
    InstructionFornumber findInstructionForNumberById(Integer instructionId);

    @Select("select * from InstructionForeach where id=#{instructionId}")
    InstructionForeach findInstructionForeachById(Integer instructionId);

    @Select("select * from InstructionListGet where id=#{instructionId}")
    InstructionListget findInstructionListGetById(Integer instructionId);

    @Select("select * from InstructionListGetRandom where id=#{instructionId}")
    InstructionListgetrandom findInstructionListGetRandomById(Integer instructionId);

    @Select("select * from InstructionUpdateTimePeriod where id=#{instructionId}")
    InstructionUpdatetimeperiod findInstructionUpdateTimePeriodById(Integer instructionId);

    @Select("select * from InstructionTypeConversion where id=#{instructionId}")
    InstructionTypeconversion findInstructionTypeConversionById(Integer instructionId);

    @Select("select * from InstructionListClear where id=#{instructionId}")
    InstructionListclear findInstructionListClearById(Integer instructionId);

    @Select("select * from InstructionSimulationTime where id=#{instructionId}")
    InstructionSimulationtime findInstructionSimulationTimeById(Integer instructionId);

    @Select("select * from InstructionRealTime where id=#{instructionId}")
    InstructionRealtime findInstructionRealTimeById(Integer instructionId);

    @Select("select * from InstructionLogOutput where id=#{instructionId}")
    InstructionLogoutput findInstructionLogOutputById(Integer instructionId);

    @Select("select * from InstructionCreateStringBuilder where id=#{instructionId}")
    InstructionCreatestringbuilder findInstructionCreateStringBuilderById(Integer instructionId);

    @Select("select * from InstructionAppend where id=#{instructionId}")
    InstructionAppend findInstructionAppendById(Integer instructionId);

    @Select("select * from InstructionToString where id=#{instructionId}")
    InstructionTostring findInstructionToStringById(Integer instructionId);

    @Select("select * from InstructionPossionDistribution where id=#{instructionId}")
    InstructionPossiondistribution findInstructionPossionById(Integer instructionId);

    @Select("select * from InstructionRandomDouble where id=#{instructionId}")
    InstructionRandomdouble findInstructionRandomDoubleById(Integer instructionId);

    @Select("select * from InstructionExpDistribution where id=#{instructionId}")
    InstructionExpdistribution findInstructionExpById(Integer instructionId);

    @Select("select * from InstructionMathExp where id=#{instructionId}")
    InstructionMathexp findInstructionMathExpById(Integer instructionId);

    @Select("select * from InstructionMathLog where id=#{instructionId}")
    InstructionMathlog findInstructionMathLogById(Integer instructionId);

    @Select("select * from InstructionMathPow where id=#{instructionId}")
    InstructionMathpow findInstructionMathPowById(Integer instructionId);

    @Select("select * from InstructionMathCeil where id=#{instructionId}")
    InstructionMathceil findInstructionMathCeilById(Integer instructionId);

    @Select("select * from InstructionMathFloor where id=#{instructionId}")
    InstructionMathfloor findInstructionMathFloorById(Integer instructionId);

    @Select("select * from InstructionMathRound where id=#{instructionId}")
    InstructionMathround findInstructionMathRoundById(Integer instructionId);

    @Select("select * from InstructionElse where id=#{instructionId}")
    InstructionElse findInstructionElseById(Integer instructionId);

    @Select("select * from InstructionIf where id=#{instructionId}")
    InstructionIf findInstructionIfById(Integer instructionId);

    @Select("select * from InstructionElseIf where id=#{instructionId}")
    InstructionElseif findInstructionElseIfById(Integer instructionId);

    @Select("select * from InstructionExecuteTask where id=#{instructionId}")
    InstructionExecutetask findInstructionExecuteTaskById(Integer instructionId);

    @Select("select * from InstructionAssign where id=#{instructionId}")
    InstructionAssign findInstructionAssignById(Integer instructionId);

    @Select("select * from InstructionBreak where id=#{instructionId}")
    InstructionBreak findInstructionBreakById(Integer instructionId);

    @Select("select * from InstructionContinue where id=#{instructionId}")
    InstructionContinue findInstructionContinueById(Integer instructionId);

    @Select("select * from InstructionMapPut where id=#{instructionId}")
    InstructionMapput findInstructionMapPutById(Integer instructionId);

    @Select("select * from InstructionMapGet where id=#{instructionId}")
    InstructionMapget findInstructionMapGetById(Integer instructionId);

    @Select("select * from InstructionContainsKey where id=#{instructionId}")
    InstructionContainskey findInstructionContainsKeyById(Integer instructionId);

    @Select("select * from InstructionContainsValue where id=#{instructionId}")
    InstructionContainsvalue findInstructionContainsValueById(Integer instructionId);

    @Select("select * from InstructionStringToInteger where id=#{instructionId}")
    InstructionStringtointeger findInstructionStringToIntegerById(Integer instructionId);

//    @Select("select * from InstructionIntegerToString where id=#{instructionId}")
//    InstructionIntegertostring findInstructionIntegerToStringById(Integer instructionId);

    @Select("select * from InstructionGaussianDistribution where id=#{instructionId}")
    InstructionGaussiandistribution findInstructionGaussianDistributionById(Integer instructionId);

    @Select("select * from InstructionWhile where id=#{instructionId}")
    InstructionWhile findInstructionWhileById(Integer instructionId);

    @Select("select * from InstructionRandomLocationName where id=#{instructionId}")
    InstructionRandomlocationname findInstructionRandomLocationNameById(Integer instructionId);

    @Select("select * from InstructionMathSin where id=#{instructionId}")
    InstructionMathsin findInstructionMathSinById(Integer instructionId);

    @Select("select * from InstructionMathCos where id=#{instructionId}")
    InstructionMathcos findInstructionMathCosById(Integer instructionId);

    @Select("select * from InstructionMathTan where id=#{instructionId}")
    InstructionMathtan findInstructionMathTanById(Integer instructionId);

    @Select("select * from InstructionMathAsin where id=#{instructionId}")
    InstructionMathasin findInstructionMathAsinById(Integer instructionId);

    @Select("select * from InstructionMathAcos where id=#{instructionId}")
    InstructionMathacos findInstructionMathAcosById(Integer instructionId);

    @Select("select * from InstructionMathRound where id=#{instructionId}")
    InstructionMathatan findInstructionMathAtanById(Integer instructionId);




    @Insert("insert into elema.federate(federateName) " +
            "values(#{federateName})")
    void insertExternal(String federateName);

    @Select("select max(Id) from externalinformation")
    int ExternalInformationMaxId();

    @Select("select max(Id) from runtimecrtl")
    int runtimeMaxId();

    @Select("select runtime from runtimecrtl where id=#{id}")
    int queryRuntime(int id);

    @Insert("insert into elema.runtimecrtl(runtime) values(#{runtime})")
    void insertRuntime(int runtime);

//    @Select("select * from RunTimeControl where id=#{instructionId}")
//    FederationRuntimecontrol findRunTimeControlById(Integer instructionId);

    @Select("select * from ${tableName} where id=#{instructionId}")
    Object findInstructionByTableAndId(@Param("tableName") String tableName,@Param("instructionId") Integer id);

}

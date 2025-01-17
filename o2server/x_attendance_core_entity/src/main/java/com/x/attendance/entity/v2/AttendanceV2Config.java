package com.x.attendance.entity.v2;

import com.x.attendance.entity.PersistenceProperties;
import com.x.base.core.entity.JpaObject;
import com.x.base.core.entity.SliceJpaObject;
import com.x.base.core.entity.annotation.ContainerEntity;
import com.x.base.core.project.annotation.FieldDescribe;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;

/**
 * 考勤配置
 * Created by fancyLou on 2023/2/28.
 * Copyright © 2023 O2. All rights reserved.
 */

@Schema(name = "AttendanceV2Config", description = "考勤配置信息.")
@ContainerEntity(dumpSize = 1000, type = ContainerEntity.Type.content, reference = ContainerEntity.Reference.strong)
@Entity
@Table(name = PersistenceProperties.AttendanceV2Config.table, uniqueConstraints = @UniqueConstraint(name = PersistenceProperties.AttendanceV2Config.table
        + JpaObject.IndexNameMiddle + JpaObject.DefaultUniqueConstraintSuffix, columnNames = {JpaObject.IDCOLUMN,
        JpaObject.CREATETIMECOLUMN, JpaObject.UPDATETIMECOLUMN, JpaObject.SEQUENCECOLUMN}))
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AttendanceV2Config extends SliceJpaObject {

    private static final String TABLE = PersistenceProperties.AttendanceV2Config.table;
    private static final long serialVersionUID = 3024779660233909771L;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @FieldDescribe("数据库主键,自动生成.")
    @Id
    @Column(length = length_id, name = ColumnNamePrefix + id_FIELDNAME)
    private String id = createId();

    public void onPersist() throws Exception {
    }
    /*
     * =============================================================================
     * ===== 以上为 JpaObject 默认字段
     * =============================================================================
     * =====
     */

    /*
     * =============================================================================
     * ===== 以下为具体不同的业务及数据表字段要求
     * =============================================================================
     * =====
     */


//    public static final String holidayList_FIELDNAME = "holidayList";
//    @FieldDescribe("节假日")
//    @PersistentCollection(fetch = FetchType.EAGER)
//    @OrderColumn(name = ORDERCOLUMNCOLUMN)
//    @ContainerTable(name = TABLE + ContainerTableNameMiddle
//            + holidayList_FIELDNAME, joinIndex = @org.apache.openjpa.persistence.jdbc.Index(name = TABLE + holidayList_FIELDNAME + JoinIndexNameSuffix))
//    @ElementColumn(length = JpaObject.length_64B, name = ColumnNamePrefix + holidayList_FIELDNAME)
//    @ElementIndex(name = TABLE + holidayList_FIELDNAME + ElementIndexNameSuffix)
//    private List<String> holidayList;
//
//
//
//    public static final String workDayList_FIELDNAME = "workDayList";
//    @FieldDescribe("工作日")
//    @PersistentCollection(fetch = FetchType.EAGER)
//    @OrderColumn(name = ORDERCOLUMNCOLUMN)
//    @ContainerTable(name = TABLE + ContainerTableNameMiddle
//            + workDayList_FIELDNAME, joinIndex = @org.apache.openjpa.persistence.jdbc.Index(name = TABLE + workDayList_FIELDNAME + JoinIndexNameSuffix))
//    @ElementColumn(length = JpaObject.length_64B, name = ColumnNamePrefix + workDayList_FIELDNAME)
//    @ElementIndex(name = TABLE + workDayList_FIELDNAME + ElementIndexNameSuffix)
//    private List<String> workDayList;


    public static final String appealEnable_FIELDNAME = "appealEnable";
    @FieldDescribe("是否开启补卡申诉功能")
    @Column(name = ColumnNamePrefix + appealEnable_FIELDNAME)
    private Boolean appealEnable = false;


    public static final String appealMaxTimes_FIELDNAME = "appealMaxTimes";
    @FieldDescribe("每个月最多申诉次数，0不限制")
    @Column(  name = ColumnNamePrefix + appealMaxTimes_FIELDNAME)
    private Integer appealMaxTimes = 0;

//    public static final String appealProcessType_Inner = "inner";
//    public static final String appealProcessType_Process = "process";

//    public static final String appealProcessType_FIELDNAME = "appealProcessType";
//    @FieldDescribe("补卡流程类型： inner-内置固定流程 ，process-自定义流程")
//    @Column(length = length_32B, name = ColumnNamePrefix + appealProcessType_FIELDNAME)
//    private String appealProcessType = appealProcessType_Inner;
//
//    public static final String innerProcessAuditType_person = "1";
//    public static final String innerProcessAuditType_superior = "2";
//    public static final String innerProcessAuditType_duty = "3";
//
//    public static final String innerProcessAuditType_FIELDNAME = "innerProcessAuditType";
//    @FieldDescribe("内置固定流程的审核人确定方式： 1-指定人 ，2-汇报对象，3-所属部门职务")
//    @Column(length = length_32B, name = ColumnNamePrefix + innerProcessAuditType_FIELDNAME)
//    private String innerProcessAuditType = innerProcessAuditType_person; // 1，3 需要在下面的innerProcessAuditContent字段填入对应的内容，2直接从个人信息中获取
//
//    public static final String innerProcessAuditContent_FIELDNAME = "innerProcessAuditContent";
//    @FieldDescribe("内置固定流程的审核人确定内容")
//    @Column(length = length_255B, name = ColumnNamePrefix + innerProcessAuditContent_FIELDNAME)
//    private String innerProcessAuditContent;


    public static final String processId_FIELDNAME = "processId";
    @FieldDescribe("自定义流程id")
    @Column(length = length_255B, name = ColumnNamePrefix + processId_FIELDNAME)
    private String processId;

    public static final String processName_FIELDNAME = "processName";
    @FieldDescribe("自定义流程名称")
    @Column(length = length_255B, name = ColumnNamePrefix + processName_FIELDNAME)
    private String processName;

    public static final String onDutyFastCheckInEnable_FIELDNAME = "onDutyFastCheckInEnable";
    @FieldDescribe("上班极速打卡，app端有效")
    @Column(name = ColumnNamePrefix + onDutyFastCheckInEnable_FIELDNAME)
    private Boolean onDutyFastCheckInEnable = false;

    public static final String offDutyFastCheckInEnable_FIELDNAME = "offDutyFastCheckInEnable";
    @FieldDescribe("下班极速打卡，app端有效")
    @Column(name = ColumnNamePrefix + offDutyFastCheckInEnable_FIELDNAME)
    private Boolean offDutyFastCheckInEnable = false;

    public static final String checkInAlertEnable_FIELDNAME = "checkInAlertEnable";
    @FieldDescribe("打卡提醒，上班前，下班后会收到提醒，不要忘记打卡.")
    @Column(name = ColumnNamePrefix + checkInAlertEnable_FIELDNAME)
    private Boolean checkInAlertEnable = false;

    public static final String exceptionAlertEnable_FIELDNAME = "exceptionAlertEnable";
    @FieldDescribe("异常打卡提醒，次日将收到异常打卡提醒.")
    @Column(name = ColumnNamePrefix + exceptionAlertEnable_FIELDNAME)
    private Boolean exceptionAlertEnable = false;

    public static final String exceptionAlertTime_FIELDNAME = "exceptionAlertTime";
    @FieldDescribe("异常打卡提醒时间：HH:mm")
    @Column(length = length_8B, name = ColumnNamePrefix + exceptionAlertTime_FIELDNAME)
    private String exceptionAlertTime = "09:30";

    // 记录任务执行的日期，用于判断当天是否已经执行过提醒任务了
    public static final String exceptionAlertDate_FIELDNAME = "exceptionAlertDate";
    @FieldDescribe("异常打卡定时执行日期：yyyy-MM-dd .")
    @Column(length = length_32B, name = ColumnNamePrefix + exceptionAlertDate_FIELDNAME)
    private String exceptionAlertDate;


    public String getExceptionAlertDate() {
        return exceptionAlertDate;
    }

    public void setExceptionAlertDate(String exceptionAlertDate) {
        this.exceptionAlertDate = exceptionAlertDate;
    }

    public String getExceptionAlertTime() {
        return exceptionAlertTime;
    }

    public void setExceptionAlertTime(String exceptionAlertTime) {
        this.exceptionAlertTime = exceptionAlertTime;
    }

    public Integer getAppealMaxTimes() {
        return appealMaxTimes;
    }

    public void setAppealMaxTimes(Integer appealMaxTimes) {
        this.appealMaxTimes = appealMaxTimes;
    }

    public Boolean getOnDutyFastCheckInEnable() {
        return onDutyFastCheckInEnable;
    }

    public void setOnDutyFastCheckInEnable(Boolean onDutyFastCheckInEnable) {
        this.onDutyFastCheckInEnable = onDutyFastCheckInEnable;
    }

    public Boolean getOffDutyFastCheckInEnable() {
        return offDutyFastCheckInEnable;
    }

    public void setOffDutyFastCheckInEnable(Boolean offDutyFastCheckInEnable) {
        this.offDutyFastCheckInEnable = offDutyFastCheckInEnable;
    }

    public Boolean getCheckInAlertEnable() {
        return checkInAlertEnable;
    }

    public void setCheckInAlertEnable(Boolean checkInAlertEnable) {
        this.checkInAlertEnable = checkInAlertEnable;
    }

    public Boolean getExceptionAlertEnable() {
        return exceptionAlertEnable;
    }

    public void setExceptionAlertEnable(Boolean exceptionAlertEnable) {
        this.exceptionAlertEnable = exceptionAlertEnable;
    }

    public Boolean getAppealEnable() {
        return appealEnable;
    }

    public void setAppealEnable(Boolean appealEnable) {
        this.appealEnable = appealEnable;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

}

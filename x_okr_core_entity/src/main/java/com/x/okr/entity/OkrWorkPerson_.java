/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.okr.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Boolean;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.okr.entity.OkrWorkPerson.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Sat May 06 19:35:29 CST 2017")
public class OkrWorkPerson_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<OkrWorkPerson,String> authorizeRecordId;
    public static volatile SingularAttribute<OkrWorkPerson,String> centerId;
    public static volatile SingularAttribute<OkrWorkPerson,String> centerTitle;
    public static volatile SingularAttribute<OkrWorkPerson,String> companyName;
    public static volatile SingularAttribute<OkrWorkPerson,Date> completeDateLimit;
    public static volatile SingularAttribute<OkrWorkPerson,String> completeDateLimitStr;
    public static volatile SingularAttribute<OkrWorkPerson,Date> createTime;
    public static volatile SingularAttribute<OkrWorkPerson,String> deployDateStr;
    public static volatile SingularAttribute<OkrWorkPerson,String> deployMonth;
    public static volatile SingularAttribute<OkrWorkPerson,String> deployYear;
    public static volatile SingularAttribute<OkrWorkPerson,String> discription;
    public static volatile SingularAttribute<OkrWorkPerson,String> employeeIdentity;
    public static volatile SingularAttribute<OkrWorkPerson,String> employeeName;
    public static volatile SingularAttribute<OkrWorkPerson,String> id;
    public static volatile SingularAttribute<OkrWorkPerson,Boolean> isCompleted;
    public static volatile SingularAttribute<OkrWorkPerson,Boolean> isDelegateTarget;
    public static volatile SingularAttribute<OkrWorkPerson,Boolean> isOverTime;
    public static volatile SingularAttribute<OkrWorkPerson,String> organizationName;
    public static volatile SingularAttribute<OkrWorkPerson,String> parentWorkId;
    public static volatile SingularAttribute<OkrWorkPerson,String> processIdentity;
    public static volatile SingularAttribute<OkrWorkPerson,String> recordType;
    public static volatile SingularAttribute<OkrWorkPerson,String> sequence;
    public static volatile SingularAttribute<OkrWorkPerson,String> status;
    public static volatile SingularAttribute<OkrWorkPerson,Date> updateTime;
    public static volatile SingularAttribute<OkrWorkPerson,String> viewTime;
    public static volatile SingularAttribute<OkrWorkPerson,String> workCreateDateStr;
    public static volatile SingularAttribute<OkrWorkPerson,String> workDateTimeType;
    public static volatile SingularAttribute<OkrWorkPerson,String> workId;
    public static volatile SingularAttribute<OkrWorkPerson,String> workLevel;
    public static volatile SingularAttribute<OkrWorkPerson,String> workProcessStatus;
    public static volatile SingularAttribute<OkrWorkPerson,String> workTitle;
    public static volatile SingularAttribute<OkrWorkPerson,String> workType;
}

/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.attendance.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Double;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.attendance.entity.StatisticDepartmentForMonth.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Sat May 06 19:33:54 CST 2017")
public class StatisticDepartmentForMonth_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> abNormalDutyCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Double> absenceDayCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> companyName;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Date> createTime;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Double> employeeCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> id;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> lackOfTimeCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> lateCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> leaveEarlyCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> offDutyCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Long> onDutyCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Double> onDutyEmployeeCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Double> onSelfHolidayCount;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> organizationName;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> sequence;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> statisticMonth;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,String> statisticYear;
    public static volatile SingularAttribute<StatisticDepartmentForMonth,Date> updateTime;
}

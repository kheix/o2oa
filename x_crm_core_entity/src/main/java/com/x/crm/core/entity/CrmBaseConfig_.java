/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.crm.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.crm.core.entity.CrmBaseConfig.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Tue May 09 17:17:31 CST 2017")
public class CrmBaseConfig_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<CrmBaseConfig,String> baseconfigtype;
    public static volatile SingularAttribute<CrmBaseConfig,Integer> configlevel;
    public static volatile SingularAttribute<CrmBaseConfig,String> configname;
    public static volatile SingularAttribute<CrmBaseConfig,String> configvalue;
    public static volatile SingularAttribute<CrmBaseConfig,Date> createTime;
    public static volatile SingularAttribute<CrmBaseConfig,String> description;
    public static volatile SingularAttribute<CrmBaseConfig,String> id;
    public static volatile SingularAttribute<CrmBaseConfig,Integer> ordernumber;
    public static volatile SingularAttribute<CrmBaseConfig,String> parentconfigid;
    public static volatile SingularAttribute<CrmBaseConfig,String> sequence;
    public static volatile SingularAttribute<CrmBaseConfig,Date> updateTime;
    public static volatile SingularAttribute<CrmBaseConfig,String> valuetype;
}

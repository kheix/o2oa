/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.organization.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.organization.core.entity.Role.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Sat May 06 19:35:40 CST 2017")
public class Role_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<Role,Date> createTime;
    public static volatile SingularAttribute<Role,String> display;
    public static volatile ListAttribute<Role,String> groupList;
    public static volatile SingularAttribute<Role,String> id;
    public static volatile SingularAttribute<Role,String> name;
    public static volatile SingularAttribute<Role,Integer> orderNumber;
    public static volatile ListAttribute<Role,String> personList;
    public static volatile SingularAttribute<Role,String> pinyin;
    public static volatile SingularAttribute<Role,String> pinyinInitial;
    public static volatile SingularAttribute<Role,String> sequence;
    public static volatile SingularAttribute<Role,String> unique;
    public static volatile SingularAttribute<Role,Date> updateTime;
}

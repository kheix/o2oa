/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.bbs.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.bbs.entity.BBSUserInfo.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Sat May 06 19:34:23 CST 2017")
public class BBSUserInfo_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<BBSUserInfo,String> cardId;
    public static volatile SingularAttribute<BBSUserInfo,Long> creamCount;
    public static volatile SingularAttribute<BBSUserInfo,Date> createTime;
    public static volatile SingularAttribute<BBSUserInfo,Long> credit;
    public static volatile SingularAttribute<BBSUserInfo,Long> fansCount;
    public static volatile SingularAttribute<BBSUserInfo,String> id;
    public static volatile SingularAttribute<BBSUserInfo,Date> lastOperationTime;
    public static volatile SingularAttribute<BBSUserInfo,Date> lastVisitTime;
    public static volatile SingularAttribute<BBSUserInfo,String> mobile;
    public static volatile SingularAttribute<BBSUserInfo,String> nickName;
    public static volatile SingularAttribute<BBSUserInfo,Boolean> online;
    public static volatile SingularAttribute<BBSUserInfo,Integer> orderNumber;
    public static volatile SingularAttribute<BBSUserInfo,Long> originalCount;
    public static volatile SingularAttribute<BBSUserInfo,String> permissionContent;
    public static volatile SingularAttribute<BBSUserInfo,Long> popularity;
    public static volatile SingularAttribute<BBSUserInfo,Long> replyCount;
    public static volatile SingularAttribute<BBSUserInfo,Long> replyCountToday;
    public static volatile SingularAttribute<BBSUserInfo,String> sequence;
    public static volatile SingularAttribute<BBSUserInfo,Integer> sex;
    public static volatile SingularAttribute<BBSUserInfo,String> signContent;
    public static volatile SingularAttribute<BBSUserInfo,Long> subjectCount;
    public static volatile SingularAttribute<BBSUserInfo,Long> subjectCountToday;
    public static volatile SingularAttribute<BBSUserInfo,Date> updateTime;
    public static volatile SingularAttribute<BBSUserInfo,String> userName;
    public static volatile SingularAttribute<BBSUserInfo,String> userStatus;
}

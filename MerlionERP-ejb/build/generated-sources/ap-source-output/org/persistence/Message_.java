package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(Message.class)
public class Message_ { 

    public static volatile SingularAttribute<Message, String> content;
    public static volatile SingularAttribute<Message, String> sender;
    public static volatile SingularAttribute<Message, String> receiver;
    public static volatile SingularAttribute<Message, String> subject;
    public static volatile SingularAttribute<Message, Long> message_id;
    public static volatile SingularAttribute<Message, Date> msg_date_time;

}
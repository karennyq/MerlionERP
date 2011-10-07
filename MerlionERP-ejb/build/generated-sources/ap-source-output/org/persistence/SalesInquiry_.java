package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.LineItem;
import org.persistence.SalesInquiry.InquirySource;
import org.persistence.SalesInquiry.InquiryStatus;
import org.persistence.SalesInquiry.Priority;
import org.persistence.SalesInquiry.Status;
import org.persistence.SalesLead;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(SalesInquiry.class)
public class SalesInquiry_ { 

    public static volatile SingularAttribute<SalesInquiry, InquiryStatus> inquiryStatus;
    public static volatile SingularAttribute<SalesInquiry, SalesLead> inquirer;
    public static volatile SingularAttribute<SalesInquiry, Status> status;
    public static volatile SingularAttribute<SalesInquiry, Date> request_date;
    public static volatile SingularAttribute<SalesInquiry, Priority> priority;
    public static volatile SingularAttribute<SalesInquiry, String> remarks;
    public static volatile SingularAttribute<SalesInquiry, InquirySource> inquiry_source;
    public static volatile CollectionAttribute<SalesInquiry, LineItem> lineItems;
    public static volatile SingularAttribute<SalesInquiry, Long> pre_sale_doc_id;

}
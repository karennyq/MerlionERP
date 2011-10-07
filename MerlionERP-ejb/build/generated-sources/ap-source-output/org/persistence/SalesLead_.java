package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead.ConvertStatus;
import org.persistence.SalesLead.SalesLeadStatus;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(SalesLead.class)
public class SalesLead_ { 

    public static volatile SingularAttribute<SalesLead, String> contact_no;
    public static volatile SingularAttribute<SalesLead, String> contact_person;
    public static volatile SingularAttribute<SalesLead, SalesLeadStatus> sales_lead_status;
    public static volatile SingularAttribute<SalesLead, String> remarks;
    public static volatile SingularAttribute<SalesLead, String> company_add;
    public static volatile CollectionAttribute<SalesLead, SalesInquiry> preSaleDocuments;
    public static volatile SingularAttribute<SalesLead, String> fax_no;
    public static volatile SingularAttribute<SalesLead, String> country;
    public static volatile SingularAttribute<SalesLead, String> city;
    public static volatile SingularAttribute<SalesLead, Date> create_date_time;
    public static volatile SingularAttribute<SalesLead, Long> inquirer_id;
    public static volatile SingularAttribute<SalesLead, String> company_name;
    public static volatile SingularAttribute<SalesLead, String> email;
    public static volatile SingularAttribute<SalesLead, ConvertStatus> convert_status;

}
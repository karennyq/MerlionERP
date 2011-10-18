/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.persistence.LineItem;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;

/**
 *
 * @author karennyq
 */
@Local
public interface SalesInquiryFacadeLocal {

    void create(SalesInquiry salesInquiry);

    void edit(SalesInquiry salesInquiry);

    void remove(SalesInquiry salesInquiry);

    SalesInquiry find(Object id);

    List<SalesInquiry> findAll();

    List<SalesInquiry> findRange(int[] range);

    int count();

    public void create(ArrayList<LineItem> lineItemList, SalesLead inquirer, String inquiry_source, String priority, String remarks);

    public Collection findFilteredSalesInquiry(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2);

    public int countFilteredSalesInquiry(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2);

    public java.util.Collection findSalesInquiryByInquirer(long inqID);

    void updateSalesInquiry(SalesInquiry si, ArrayList<LineItem> lineItemList);
    
    public int countFilteredSalesInquiryALL(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2);
    
    public Collection findFilteredSalesInquiryALL(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2);
}

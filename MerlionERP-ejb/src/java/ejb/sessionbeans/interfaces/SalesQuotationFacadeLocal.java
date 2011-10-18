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
import org.persistence.SalesLead;
import org.persistence.SalesQuotation;

/**
 *
 * @author karennyq
 */
@Local
public interface SalesQuotationFacadeLocal {

    void create(SalesQuotation salesQuotation);

    void edit(SalesQuotation salesQuotation);

    void remove(SalesQuotation salesQuotation);

    SalesQuotation find(Object id);

    List<SalesQuotation> findAll();

    List<SalesQuotation> findRange(int[] range);

    int count();

    public void create(java.util.ArrayList<org.persistence.LineItem> lineItemList, org.persistence.SalesLead inquirer, java.lang.String inquiry_source, java.lang.String priority, java.lang.String remarks, double add_disc);

    public int countFilteredSalesQuotation(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2);

    public Collection findFilteredSalesQuotation(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2);

    public Collection findAllQuotation();

    public int countAllQuotation();

    public SalesLead findQuotation(Long id);

    void createQuotationFromInquiry(ArrayList<LineItem> lineItemList, SalesQuotation sq);

    void updateSalesQuotation(SalesQuotation sq, ArrayList<LineItem> lineItemList);

    public int countFilteredSalesQuotationALL(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2);

    public Collection findFilteredSalesQuotationALL(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2);
}

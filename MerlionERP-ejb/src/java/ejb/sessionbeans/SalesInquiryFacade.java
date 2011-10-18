/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.LineItemFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.persistence.LineItem;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;

/**
 *
 * @author karennyq
 */
@Stateless
public class SalesInquiryFacade extends AbstractFacade<SalesInquiry> implements SalesInquiryFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    @EJB
    LineItemFacadeLocal lineItemFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SalesInquiryFacade() {
        super(SalesInquiry.class);
    }

    @Override
    public void create(ArrayList<LineItem> lineItemList, SalesLead inquirer, String inquiry_source, String priority, String remarks) {
        SalesInquiry si = new SalesInquiry();
        si.setInquirer(inquirer);
        si.setInquiry_source(SalesInquiry.InquirySource.valueOf(inquiry_source));
        si.setPriority(SalesInquiry.Priority.valueOf(priority));
        si.setRemarks(remarks);
        si.setRequest_date(new Date());
        si.setInquiryStatus(SalesInquiry.InquiryStatus.Not_Converted);
        si.setStatus(SalesInquiry.Status.Active);

        si.setLineItems(new ArrayList());
        create(si);

        for (LineItem li : lineItemList) {
            lineItemFacade.create(li);
            si.getLineItems().add(li);
        }

        edit(si);
    }

    @Override
    public int countFilteredSalesInquiry(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2) {
        String queryStr = "SELECT s FROM SalesInquiry s WHERE TYPE(s) <> SalesQuotation "
                + "AND s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.priority LIKE ?3 "
                + "AND s.inquirer.company_name LIKE ?4 "
                + "AND s.request_date BETWEEN ?5 and ?6 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + inquiry_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + inquiry_priority.trim() + "%");
        query.setParameter(4, "%" + company_name + "%");
        query.setParameter(5, req_date_1, TemporalType.TIMESTAMP);
        query.setParameter(6, req_date_2, TemporalType.TIMESTAMP);
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSalesInquiry(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2) {

        String queryStr = "SELECT s FROM SalesInquiry s WHERE TYPE(s) <> SalesQuotation "
                + "AND s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.priority LIKE ?3 "
                + "AND s.inquirer.company_name LIKE ?4 "
                + "AND s.request_date BETWEEN ?5 and ?6 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + inquiry_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + inquiry_priority.trim() + "%");
        query.setParameter(4, "%" + company_name + "%");
        query.setParameter(5, req_date_1, TemporalType.TIMESTAMP);
        query.setParameter(6, req_date_2, TemporalType.TIMESTAMP);

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesInquiry>) query.getResultList();
    }
    
    @Override
    public Collection findSalesInquiryByInquirer(long inqID) {
        String queryStr = "SELECT s FROM SalesInquiry s WHERE s.inquirer.inquirer_id=?1";
        Query query = em.createQuery(queryStr);
        query.setParameter(1, inqID);
        return (Collection<SalesInquiry>) query.getResultList();
    }

    @Override
    public void updateSalesInquiry(SalesInquiry si, ArrayList<LineItem> lineItemList) {
        Collection<LineItem> liList = si.getLineItems();
        si.setLineItems(new ArrayList());
        edit(si);
        
        for (LineItem li: liList) {
            lineItemFacade.remove(li);
        }
        
        for (LineItem li: lineItemList) {
            li.setLine_item_id(null);
            lineItemFacade.create(li);
            si.getLineItems().add(li);
        }
        
        edit(si);
        
    }
    @Override
    public int countFilteredSalesInquiryALL(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2) {
        String queryStr = "SELECT s FROM SalesInquiry s WHERE TYPE(s) <> SalesQuotation "
                + "AND s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.priority LIKE ?3 "
                + "AND s.inquirer.company_name LIKE ?4 "
                + "AND s.request_date BETWEEN ?5 and ?6 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + inquiry_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + inquiry_priority.trim() + "%");
        query.setParameter(4, "%" + company_name + "%");
        query.setParameter(5, req_date_1, TemporalType.TIMESTAMP);
        query.setParameter(6, req_date_2, TemporalType.TIMESTAMP);
        return query.getResultList().size();
    }
    
    @Override
    public Collection findFilteredSalesInquiryALL(int page, int rows, String sort, String order, String inquiry_id, String inquirer_id, String inquiry_priority, String company_name, Date req_date_1, Date req_date_2) {

        String queryStr = "SELECT s FROM SalesInquiry s WHERE TYPE(s) <> SalesQuotation "
                + "AND s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.priority LIKE ?3 "
                + "AND s.inquirer.company_name LIKE ?4 "
                + "AND s.request_date BETWEEN ?5 and ?6 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + inquiry_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + inquiry_priority.trim() + "%");
        query.setParameter(4, "%" + company_name + "%");
        query.setParameter(5, req_date_1, TemporalType.TIMESTAMP);
        query.setParameter(6, req_date_2, TemporalType.TIMESTAMP);

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesInquiry>) query.getResultList();
    }

    
    
}

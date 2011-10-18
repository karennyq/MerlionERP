/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.LineItemFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import ejb.sessionbeans.interfaces.SalesQuotationFacadeLocal;
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
import org.persistence.SalesQuotation;
import util.services.DateUtil;

/**
 *
 * @author karennyq
 */
@Stateless
public class SalesQuotationFacade extends AbstractFacade<SalesQuotation> implements SalesQuotationFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    @EJB
    LineItemFacadeLocal lineItemFacade;
    @EJB
    SalesInquiryFacadeLocal siFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SalesQuotationFacade() {
        super(SalesQuotation.class);
    }

    @Override
    public void create(ArrayList<LineItem> lineItemList, SalesLead inquirer, String inquiry_source, String priority, String remarks, double add_disc) {
        SalesQuotation sq = new SalesQuotation();

        sq.setInquirer(inquirer);
        sq.setInquiry_source(SalesQuotation.InquirySource.valueOf(inquiry_source));
        sq.setPriority(SalesQuotation.Priority.valueOf(priority));
        sq.setRemarks(remarks);
        sq.setRequest_date(new Date());
        sq.setStatus(SalesQuotation.Status.Active);
        sq.setLineItems(new ArrayList());
        sq.setDiscount(add_disc);
        DateUtil du = new DateUtil();
        sq.setExpiry_date(du.addWeek(sq.getRequest_date()));
        sq.setCustGrpDiscount(1.0); // ======================================= need to define... this is hardcode...

        create(sq);
        Integer totalLeadTime = 0;

        for (LineItem li : lineItemList) {
            lineItemFacade.create(li);
            sq.getLineItems().add(li);

            if (li.getIndicative_lead_time() >= totalLeadTime) {
                totalLeadTime = li.getIndicative_lead_time();
            }
        }

        sq.setIndicative_lead_time(totalLeadTime);
        sq.setDiscounted_total();
        sq.setTotal_price();


        edit(sq);
    }

    @Override
    public int countFilteredSalesQuotation(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2) {

        String queryStr = "SELECT s FROM SalesQuotation s WHERE s.inquiryStatus IS NULL AND "
                + "s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.inquirer.company_name LIKE ?3 "
                + "AND s.expiry_date BETWEEN ?4 and ?5 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + quotation_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + company_name + "%");
        query.setParameter(4, exp_date_1, TemporalType.TIMESTAMP);
        query.setParameter(5, exp_date_2, TemporalType.TIMESTAMP);
        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSalesQuotation(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2) {

        String queryStr = "SELECT s FROM SalesQuotation s WHERE s.inquiryStatus IS NULL AND "
                + "s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.inquirer.company_name LIKE ?3 "
                + "AND s.expiry_date BETWEEN ?4 and ?5 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + quotation_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + company_name + "%");
        query.setParameter(4, exp_date_1, TemporalType.TIMESTAMP);
        query.setParameter(5, exp_date_2, TemporalType.TIMESTAMP);

        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesQuotation>) query.getResultList();
    }

    @Override
    public Collection findAllQuotation() {
        Query query = em.createQuery("SELECT s FROM SalesQuotation s where s.inquiryStatus IS NULL");
        return (Collection<SalesQuotation>) query.getResultList();
    }

    @Override
    public int countAllQuotation() {
        Query query = em.createQuery("SELECT s FROM SalesQuotation s where s.inquiryStatus IS NULL");
        return query.getResultList().size();
    }

    @Override
    public SalesLead findQuotation(Long id) {
        Query query = em.createQuery("SELECT s FROM SalesQuotation s WHERE s.inquiryStatus IS NULL AND s.pre_sale_doc_id=?1");
        query.setParameter(1, id);
        SalesLead sl = (SalesLead) query.getSingleResult();
        return sl;
    }
    
    @Override
    public void updateSalesQuotation(SalesQuotation sq, ArrayList<LineItem> lineItemList) {
        Collection<LineItem> liList = sq.getLineItems();
        sq.setLineItems(new ArrayList());
        DateUtil du = new DateUtil();
        sq.setExpiry_date(du.addWeek(sq.getRequest_date()));

        edit(sq);
        
        for (LineItem li: liList) {
            lineItemFacade.remove(li);
        }
        
        Integer totalLeadTime = 0;

        for (LineItem li: lineItemList) {
            li.setLine_item_id(null);
            lineItemFacade.create(li);
            sq.getLineItems().add(li);
            
            if (li.getIndicative_lead_time() >= totalLeadTime) {
                totalLeadTime = li.getIndicative_lead_time();
            }
        }

        sq.setIndicative_lead_time(totalLeadTime);
        sq.setTotal_price();

        edit(sq);
    }

    @Override
    public void createQuotationFromInquiry(ArrayList<LineItem> lineItemList, SalesQuotation sq) {
        //create sales quotation
        sq.setRequest_date(new Date());
        sq.setStatus(SalesQuotation.Status.Active);
        sq.setLineItems(new ArrayList());
        sq.setExpiry_date(DateUtil.addWeek(sq.getRequest_date()));

        create(sq);
        
        //create line items and set it into quotation
        Integer totalLeadTime = 0;

        for (LineItem li : lineItemList) {
            li.setLine_item_id(null);
            lineItemFacade.create(li);
            sq.getLineItems().add(li);

            if (li.getIndicative_lead_time() >= totalLeadTime) {
                totalLeadTime = li.getIndicative_lead_time();
            }
        }

        sq.setIndicative_lead_time(totalLeadTime);

        edit(sq);
    }
     @Override
    public int countFilteredSalesQuotationALL(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2) {

        String queryStr = "SELECT s FROM SalesQuotation s WHERE s.inquiryStatus IS NULL AND "
                + "s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.inquirer.company_name LIKE ?3 "
                + "AND s.expiry_date BETWEEN ?4 and ?5 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + quotation_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + company_name + "%");
        query.setParameter(4, exp_date_1, TemporalType.TIMESTAMP);
        query.setParameter(5, exp_date_2, TemporalType.TIMESTAMP);
        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSalesQuotationALL(int page, int rows, String sort, String order, String quotation_id, String inquirer_id, String company_name, Date exp_date_1, Date exp_date_2) {

        String queryStr = "SELECT s FROM SalesQuotation s WHERE s.inquiryStatus IS NULL AND "
                + "s.pre_sale_doc_id LIKE ?1 "
                + "AND s.inquirer.inquirer_id LIKE ?2 "
                + "AND s.inquirer.company_name LIKE ?3 "
                + "AND s.expiry_date BETWEEN ?4 and ?5 "
                + "ORDER BY s." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + quotation_id + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + company_name + "%");
        query.setParameter(4, exp_date_1, TemporalType.TIMESTAMP);
        query.setParameter(5, exp_date_2, TemporalType.TIMESTAMP);

        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesQuotation>) query.getResultList();
    }
    
}

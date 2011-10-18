/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.LineItemFacadeLocal;
import ejb.sessionbeans.interfaces.PurchaseOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Customer;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;
import org.persistence.SalesQuotation;

/**
 *
 * @author karennyq
 */
@Stateless
public class PurchaseOrderFacade extends AbstractFacade<PurchaseOrder> implements PurchaseOrderFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    LineItemFacadeLocal lineItemFacade;
    @EJB
    SalesOrderFacadeLocal salesOrderFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PurchaseOrderFacade() {
        super(PurchaseOrder.class);
    }

    @Override
    public PurchaseOrder create(SalesQuotation sq, ArrayList<LineItem> lineItemList, double add_disc, String po_ref_no, Date sent_date, String shipping_address, String remarks, String status) {
        PurchaseOrder po = new PurchaseOrder();
        po.setApproved_status(PurchaseOrder.ApprovedStatus.values()[Integer.parseInt(status)-1]);
        po.setCustomer((Customer)sq.getInquirer());
        po.setDiscount(add_disc);
        po.setLineItems(new ArrayList());
        po.setPo_reference_no(po_ref_no);
        po.setReceived_date(new Date());
        po.setRemarks(remarks);
        po.setSalesQuotation(sq);
        po.setSent_date(sent_date);
        po.setShipping_Address(shipping_address);
        po.setStatus(PurchaseOrder.Status.Active);
        po.setDiscounted_total();
        create(po);

        Integer totalLeadTime = 0;

        for (LineItem li : lineItemList) {
            LineItem item = new LineItem();
            item.setProduct(li.getProduct());
            item.setBase_price(li.getBase_price());
            item.setBulk_discount(li.getBulk_discount());
            item.setIndicative_lead_time(li.getIndicative_lead_time());
            item.setQuantity(li.getQuantity());
            item.setActual_price();
            lineItemFacade.create(item);
            po.getLineItems().add(item);

            if (item.getIndicative_lead_time() >= totalLeadTime) {
                totalLeadTime = item.getIndicative_lead_time();
            }
        }
        po.setActual_total();
        po.setIndicative_lead_time(totalLeadTime);
        edit(po);
        
//        SalesOrder so = salesOrderFacade.create(po);
//        po.setSalesOrder(so);
//        edit(po);

        return po;
    }
    
    @Override
    public int countFilteredPurchaseOrder(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status) {

        String queryStr = "SELECT po FROM PurchaseOrder po "
                + "WHERE "
                + "po.po_reference_no LIKE ?1 "
                + "AND po.customer.inquirer_id LIKE ?2 "
                + "AND po.approved_status LIKE ?3 "
                + "ORDER BY po." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_ref_no + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + status + "%");
        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredPurchaseOrder(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status) {

        String queryStr = "SELECT po FROM PurchaseOrder po "
                + "WHERE "
                + "po.po_reference_no LIKE ?1 "
                + "AND po.customer.inquirer_id LIKE ?2 "
                + "AND po.approved_status LIKE ?3 "
                + "ORDER BY po." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_ref_no + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + status + "%");
        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<PurchaseOrder>) query.getResultList();
    }
    
    @Override
    public void updatePurchaseOrder(PurchaseOrder po, ArrayList<LineItem> lineItemList) {
        Collection<LineItem> liList = po.getLineItems();
        po.setLineItems(new ArrayList());
        edit(po);
        
        for (LineItem li: liList) {
            lineItemFacade.remove(li);
        }
        
        for (LineItem li: lineItemList) {
            li.setLine_item_id(null);
            lineItemFacade.create(li);
            po.getLineItems().add(li);
        }
 
        edit(po);
       
        
//        Integer totalLeadTime = 0;

//        for (LineItem li: lineItemList) {
//            li.setLine_item_id(null);
//            lineItemFacade.create(li);
//            po.getLineItems().add(li);
//            
//            if (li.getIndicative_lead_time() >= totalLeadTime) {
//                totalLeadTime = li.getIndicative_lead_time();
//            }
//        }
        
    }
    
    @Override
    public Collection findAllSoId(String inquirerId){
        
        Query query = em.createQuery("SELECT s FROM PurchaseOrder p, SalesOrder s WHERE p.salesOrder.so_id = s.so_id AND p.customer.inquirer_id = ?1 AND s.deposit_requested != 0 AND s.creditCheck =?2");
       
        query.setParameter(1, Long.valueOf(inquirerId));
        //for enum
        query.setParameter(2, SalesOrder.CreditCheck.valueOf("Pending"));
        return (Collection<SalesOrder>) query.getResultList();
    }
    @Override
    public int countFilteredPurchaseOrderALL(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status) {

        String queryStr = "SELECT po FROM PurchaseOrder po "
                + "WHERE "
                + "po.po_reference_no LIKE ?1 "
                + "AND po.customer.inquirer_id LIKE ?2 "
                + "AND po.approved_status LIKE ?3 "
                + "ORDER BY po." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_ref_no + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + status + "%");
        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredPurchaseOrderALL(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status) {

        String queryStr = "SELECT po FROM PurchaseOrder po "
                + "WHERE "
                + "po.po_reference_no LIKE ?1 "
                + "AND po.customer.inquirer_id LIKE ?2 "
                + "AND po.approved_status LIKE ?3 "
                + "ORDER BY po." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_ref_no + "%");
        query.setParameter(2, "%" + inquirer_id + "%");
        query.setParameter(3, "%" + status + "%");
        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<PurchaseOrder>) query.getResultList();
    }

}

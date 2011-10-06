/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DeliveryOrderFacadeLocal;
import ejb.sessionbeans.interfaces.LineItemFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.DeliveryOrder;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;

/**
 *
 * @author karennyq
 */
@Stateless
public class SalesOrderFacade extends AbstractFacade<SalesOrder> implements SalesOrderFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    LineItemFacadeLocal lineItemFacade;
    @EJB
    DeliveryOrderFacadeLocal deliveryOrderFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SalesOrderFacade() {
        super(SalesOrder.class);
    }

    @Override
    public int getBaseAmt(Long product_id, Date min, Date max) {
        Query query = em.createQuery("SELECT so FROM SalesOrder so WHERE so.date_confirmed BETWEEN ?1 AND ?2");
        query.setParameter(1, min);
        query.setParameter(2, max);

        int baseAmt = 0;
        for (Object o : query.getResultList()) {
            SalesOrder so = (SalesOrder) o;
            for (LineItem li : so.getLineItems()) {
                if (li.getProduct().getProduct_id() == product_id) {
                    baseAmt = baseAmt + li.getQuantity();
                }
            }
        }

        return baseAmt;
    }

     @Override
    public SalesOrder create(PurchaseOrder po) {
        SalesOrder so = new SalesOrder();
        so.setCompleted_date(null);
        so.setDate_confirmed(null);
        so.setDate_creation(new Date());
        so.setDeposit_requested(0.00);
        so.setDiscount(po.getDiscount());
        so.setLead_time(po.getIndicative_lead_time());
        so.setPurchaseOrder(po);
        so.setStatus(SalesOrder.Status.Pending);
        so.setAtpCheck(SalesOrder.ATPCheck.Pending);
        so.setCreditCheck(SalesOrder.CreditCheck.Pending);
        so.setLineItems(new ArrayList());
        so.setDiscounted_total();
        so.setDeliveryOrders(new ArrayList());
        create(so);

        Integer totalLeadTime = 0;

        for (LineItem li : po.getLineItems()) {
            LineItem item = new LineItem();
            item.setProduct(li.getProduct());
            item.setBase_price(li.getBase_price());
            item.setBulk_discount(li.getBulk_discount());
            item.setIndicative_lead_time(li.getIndicative_lead_time());
            item.setQuantity(li.getQuantity());
            item.setActual_price();
            lineItemFacade.create(item);
            so.getLineItems().add(item);

            if (item.getIndicative_lead_time() >= totalLeadTime) {
                totalLeadTime = item.getIndicative_lead_time();
            }
        }
        so.setActual_total();
        so.setLead_time(totalLeadTime);
        edit(so);

        DeliveryOrder dlo = deliveryOrderFacade.create(po, so);
        so.getDeliveryOrders().add(dlo);
        edit(so);

        return so;
    }


    @Override
    public int countFilteredSalesOrder(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status) {

        String queryStr = "SELECT so FROM SalesOrder so, PurchaseOrder po "
                + "WHERE po = so.purchaseOrder "
                + "AND po.po_reference_no LIKE ?1 "
                + "AND so.so_id LIKE ?2 "
                + "AND so.atpCheck LIKE ?3 "
                + "AND so.creditCheck LIKE ?4 "
                + "AND so.status LIKE ?5 "
                + "ORDER BY so." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + atpCheck + "%");
        query.setParameter(4, "%" + creditCheck + "%");
        query.setParameter(5, "%" + status + "%");

        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSalesOrder(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status) {

          String queryStr = "SELECT so FROM SalesOrder so, PurchaseOrder po "
                + "WHERE po = so.purchaseOrder "
                + "AND po.po_reference_no LIKE ?1 "
                + "AND so.so_id LIKE ?2 "
                + "AND so.atpCheck LIKE ?3 "
                + "AND so.creditCheck LIKE ?4 "
                + "AND so.status LIKE ?5 "
                + "ORDER BY so." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + atpCheck + "%");
        query.setParameter(4, "%" + creditCheck + "%");
        query.setParameter(5, "%" + status + "%");
        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<PurchaseOrder>) query.getResultList();
    }
     @Override
    public int countFilteredSalesOrderALL(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status) {

        String queryStr = "SELECT so FROM SalesOrder so, PurchaseOrder po "
                + "WHERE po = so.purchaseOrder "
                + "AND po.po_reference_no LIKE ?1 "
                + "AND so.so_id LIKE ?2 "
                + "AND so.atpCheck LIKE ?3 "
                + "AND so.creditCheck LIKE ?4 "
                + "AND so.status LIKE ?5 "
                + "ORDER BY so." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + atpCheck + "%");
        query.setParameter(4, "%" + creditCheck + "%");
        query.setParameter(5, "%" + status + "%");

        System.out.println(query.toString());

        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSalesOrderALL(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status) {

        String queryStr = "SELECT so FROM SalesOrder so, PurchaseOrder po "
                + "WHERE po = so.purchaseOrder "
                + "AND po.po_reference_no LIKE ?1 "
                + "AND so.so_id LIKE ?2 "
                + "AND so.atpCheck LIKE ?3 "
                + "AND so.creditCheck LIKE ?4 "
                + "AND so.status LIKE ?5 "
                + "ORDER BY so." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + po_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + atpCheck + "%");
        query.setParameter(4, "%" + creditCheck + "%");
        query.setParameter(5, "%" + status + "%");
        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<PurchaseOrder>) query.getResultList();
    }
    
    
    @Override
    public void refresh(){
        em.flush();
    }
    
    @Override
    public void updateSalesOrder(SalesOrder so, ArrayList<LineItem> lineItemList, String destination) {
        Collection<LineItem> liList = so.getLineItems();
        Collection<DeliveryOrder> doList = so.getDeliveryOrders();
        so.setLineItems(new ArrayList());
        so.setDeliveryOrders(new ArrayList());

        edit(so);
        
        for (LineItem li: liList) {
            lineItemFacade.remove(li);
        }
        
        for (DeliveryOrder o: doList) {
            deliveryOrderFacade.remove(o);
        }

        for (LineItem li: lineItemList) {
            li.setLine_item_id(null);
            lineItemFacade.create(li);
            so.getLineItems().add(li);
        }
        
        for (DeliveryOrder o: doList) {
            o.setDelivery_order_id(null);
            o.setDestination(destination);
        }
        
        DeliveryOrder dlo = deliveryOrderFacade.create(destination, so);
        so.getDeliveryOrders().add(dlo);
        edit(so);
    }

    
}

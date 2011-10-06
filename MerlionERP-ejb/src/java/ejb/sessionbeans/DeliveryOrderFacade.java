/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DeliveryOrderDetailFacadeLocal;
import ejb.sessionbeans.interfaces.DeliveryOrderFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.DeliveryOrder;
import org.persistence.DeliveryOrderDetail;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;

/**
 *
 * @author karennyq
 */
@Stateless
public class DeliveryOrderFacade extends AbstractFacade<DeliveryOrder> implements DeliveryOrderFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    DeliveryOrderDetailFacadeLocal deliveryOrderDetailFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DeliveryOrderFacade() {
        super(DeliveryOrder.class);
    }
    
    @Override
    public DeliveryOrder create(PurchaseOrder po, SalesOrder so) {
        DeliveryOrder dlo = new DeliveryOrder();
        dlo.setDestination(po.getShipping_Address());
        dlo.setSalesOrder(so);
        dlo.setDeliveryStatus(DeliveryOrder.DeliveryOrderStatus.Pending);
        dlo.setStatus(DeliveryOrder.Status.Active);
        dlo.setDeliveryOrderDetails(new ArrayList());
        create(dlo);
        
        for(LineItem li : so.getLineItems()){
            DeliveryOrderDetail dod = deliveryOrderDetailFacade.create(dlo, li);
            dlo.getDeliveryOrderDetails().add(dod);
        }
        
        edit(dlo);
       
        return dlo;
    }
    
    @Override
    public DeliveryOrder create(String destination, SalesOrder so) {
        DeliveryOrder dlo = new DeliveryOrder();
        dlo.setStatus(DeliveryOrder.Status.Active);
        dlo.setDestination(destination);
        dlo.setSalesOrder(so);
        dlo.setDeliveryStatus(DeliveryOrder.DeliveryOrderStatus.Pending);
        dlo.setDeliveryOrderDetails(new ArrayList());
        create(dlo);
        
        for(LineItem li : so.getLineItems()){
            DeliveryOrderDetail dod = deliveryOrderDetailFacade.create(dlo, li);
            dlo.getDeliveryOrderDetails().add(dod);
        }
        
        edit(dlo);
       
        return dlo;
    }
    
    @Override
    public Collection findFilteredDeliveryOrder(int page, int rows, String sort, String order, String do_id, String so_id, String status) {

          String queryStr = "SELECT dlo FROM DeliveryOrder dlo "
                + "WHERE "
                + "dlo.delivery_order_id LIKE ?1 "
                + "AND dlo.salesOrder.so_id LIKE ?2 "
                + "AND dlo.deliveryStatus LIKE ?3 "
                + "ORDER BY dlo." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + do_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + status + "%");
        System.out.println(query.toString());

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesOrder>) query.getResultList();
    }
    
    @Override
    public int countFilteredDeliveryOrder(int page, int rows, String sort, String order, String do_id, String so_id, String status) {

          String queryStr = "SELECT dlo FROM DeliveryOrder dlo "
                + "WHERE "
                + "dlo.delivery_order_id LIKE ?1 "
                + "AND dlo.salesOrder.so_id LIKE ?2 "
                + "AND dlo.deliveryStatus LIKE ?3 "
                + "ORDER BY dlo." + sort + " " + order;

        Query query = em.createQuery(queryStr);
        query.setParameter(1, "%" + do_id + "%");
        query.setParameter(2, "%" + so_id + "%");
        query.setParameter(3, "%" + status + "%");
        
        return query.getResultList().size();
    }

    
}

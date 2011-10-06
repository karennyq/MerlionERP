/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DeliveryOrderDetailFacadeLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.persistence.DeliveryOrder;
import org.persistence.DeliveryOrderDetail;
import org.persistence.LineItem;

/**
 *
 * @author karennyq
 */
@Stateless
public class DeliveryOrderDetailFacade extends AbstractFacade<DeliveryOrderDetail> implements DeliveryOrderDetailFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DeliveryOrderDetailFacade() {
        super(DeliveryOrderDetail.class);
    }
    
    @Override
    public DeliveryOrderDetail create(DeliveryOrder dlo, LineItem li) {
        DeliveryOrderDetail dod = new DeliveryOrderDetail();
        dod.setQuantity_ordered(li.getQuantity());
        dod.setProduct(li.getProduct());
        return dod;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.DeliveryOrder;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;

/**
 *
 * @author karennyq
 */
@Local
public interface DeliveryOrderFacadeLocal {

    void create(DeliveryOrder deliveryOrder);

    void edit(DeliveryOrder deliveryOrder);

    void remove(DeliveryOrder deliveryOrder);

    DeliveryOrder find(Object id);

    List<DeliveryOrder> findAll();

    List<DeliveryOrder> findRange(int[] range);

    int count();

    public DeliveryOrder create(PurchaseOrder po, SalesOrder so);
    
    public DeliveryOrder create(String destination, SalesOrder so);

    public Collection findFilteredDeliveryOrder(int page, int rows, String sort, String order, String do_id, String so_id, String status);

    public int countFilteredDeliveryOrder(int page, int rows, String sort, String order, String do_id, String so_id, String status);
}

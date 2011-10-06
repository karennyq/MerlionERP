/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.DeliveryOrder;
import org.persistence.DeliveryOrderDetail;
import org.persistence.LineItem;

/**
 *
 * @author karennyq
 */
@Local
public interface DeliveryOrderDetailFacadeLocal {

    void create(DeliveryOrderDetail deliveryOrderDetail);

    void edit(DeliveryOrderDetail deliveryOrderDetail);

    void remove(DeliveryOrderDetail deliveryOrderDetail);

    DeliveryOrderDetail find(Object id);

    List<DeliveryOrderDetail> findAll();

    List<DeliveryOrderDetail> findRange(int[] range);

    int count();

    public DeliveryOrderDetail create(DeliveryOrder dlo, LineItem li);
    
}

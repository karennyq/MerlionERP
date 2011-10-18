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
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;

/**
 *
 * @author karennyq
 */
@Local
public interface SalesOrderFacadeLocal {

    void create(SalesOrder salesOrder);

    void edit(SalesOrder salesOrder);

    void remove(SalesOrder salesOrder);

    SalesOrder find(Object id);

    List<SalesOrder> findAll();

    List<SalesOrder> findRange(int[] range);

    int count();

    public int getBaseAmt(Long product_id, Date min, Date max);

    public SalesOrder create(PurchaseOrder po);

    public Collection findFilteredSalesOrder(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status);

    public int countFilteredSalesOrder(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status);

    public Collection findFilteredSalesOrderALL(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status);

    public int countFilteredSalesOrderALL(int page, int rows, String sort, String order, String po_id, String so_id, String atpCheck, String creditCheck, String status);

    public void refresh();
    
    void updateSalesOrder(SalesOrder so, ArrayList<LineItem> lineItemList, String destination);
}

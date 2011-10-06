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
import org.persistence.SalesQuotation;

/**
 *
 * @author karennyq
 */
@Local
public interface PurchaseOrderFacadeLocal {

    void create(PurchaseOrder purchaseOrder);

    void edit(PurchaseOrder purchaseOrder);

    void remove(PurchaseOrder purchaseOrder);

    PurchaseOrder find(Object id);

    List<PurchaseOrder> findAll();

    List<PurchaseOrder> findRange(int[] range);

    int count();

    public PurchaseOrder create(SalesQuotation sq, ArrayList<LineItem> lineItemList, double add_disc, String po_ref_no, Date sent_date, String shipping_address, String remarks, String status);

    public int countFilteredPurchaseOrder(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status);

    public Collection findFilteredPurchaseOrder(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status);

    public void updatePurchaseOrder(PurchaseOrder po, ArrayList<LineItem> lineItemList);
    
    public java.util.Collection findAllSoId(String inquirerId);
    
    public int countFilteredPurchaseOrderALL(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status);
    
    public Collection findFilteredPurchaseOrderALL(int page, int rows, String sort, String order, String po_ref_no, String inquirer_id, String status);
}

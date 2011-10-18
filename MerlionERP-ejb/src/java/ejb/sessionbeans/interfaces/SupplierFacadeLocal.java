/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.RawMaterialDetail;
import org.persistence.Supplier;

/**
 *
 * @author alyssia
 */
@Local
public interface SupplierFacadeLocal {
    void create(Supplier supplier);
    
    void create(ArrayList<RawMaterialDetail> rmDetail,String raw_material_id,String supplier_name, String supplier_add);

    void edit(Supplier supplier);

    void remove(Supplier supplier);
    
    void updateSupplier(Supplier s, ArrayList<RawMaterialDetail> rmItemList);

    Supplier find(Object id);

    List<Supplier> findAll();

    List<Supplier> findRange(int[] range);

    int count();
   
    public int countFilteredSupplier(int page, int rows, String sort, String order, String suppID, String suppName);
            
    public Collection findFilteredSupplier(int page, int rows, String sort, String order, String suppID, String suppName);
    
    public Collection retrieveSuppliers(String raw_material_id);
    
    String deleteSupplier(String supplierID, String raw_material_id);
}

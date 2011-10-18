/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.RawMaterialDetailFacadeLocal;
import ejb.sessionbeans.interfaces.RawMaterialFacadeLocal;
import ejb.sessionbeans.interfaces.SupplierFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.RawMaterial;
import org.persistence.RawMaterialDetail;
import org.persistence.Supplier;

/**
 *
 * @author alyssia
 */
@Stateless
public class SupplierFacade extends AbstractFacade<Supplier> implements SupplierFacadeLocal {
    @EJB
    RawMaterialDetailFacadeLocal rmDFacade;
    @EJB
    RawMaterialFacadeLocal rmFacade;
   
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SupplierFacade() {
        super(Supplier.class);
    }

    @Override
    public int countFilteredSupplier(int page, int rows, String sort, String order, String suppID, String suppName) {
        String queryStr = "SELECT s FROM Supplier s WHERE s.supplier_id LIKE ?1 AND s.supplier_name LIKE ?2 ORDER BY s." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + suppID + "%");
        query.setParameter(2, "%" + suppName + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredSupplier(int page, int rows, String sort, String order, String suppID, String suppName) {

        String queryStr =  "SELECT s FROM Supplier s WHERE s.supplier_id LIKE ?1 AND s.supplier_name LIKE ?2 ORDER BY s." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + suppID + "%");
        query.setParameter(2, "%" + suppName + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Supplier>) query.getResultList();
    }
    
    @Override
    public void create(ArrayList<RawMaterialDetail> rmDetail, String raw_material_id, String supplier_name, String supplier_add) {
        
        Supplier s = new Supplier();
        s.setSupplier_name(supplier_name);
        s.setSupplier_address(supplier_add);
        s.setSupplier_status(Supplier.SupplierStatus.Active);
        s.setRawMaterialDetail(new ArrayList());
        create(s);
        
        for (RawMaterialDetail rmD1 : rmDetail) {
            rmD1.setSupplier(s);
            rmDFacade.create(rmD1);
            s.getRawMaterialDetail().add(rmD1);
        }
        edit(s);
   }
    
    @Override
    public void updateSupplier(Supplier s, ArrayList<RawMaterialDetail> rmItemList) {
       //remove connection of supplier with raw material details
        Collection<RawMaterialDetail> rmList = s.getRawMaterialDetail();
        s.setRawMaterialDetail(new ArrayList());
       
       //edit supplier details
       edit(s);
        
       //totally remove record of raw material details
       for (RawMaterialDetail rmD: rmList) {
           rmDFacade.remove(rmD);
      }
       for (RawMaterialDetail rmD: rmItemList) {
           rmD.setRaw_mat_detail_id(null);
           rmD.setSupplier(s);
           rmDFacade.create(rmD);
           s.getRawMaterialDetail().add(rmD);
      } 
      edit(s);
   }
    
    @Override
    public Collection retrieveSuppliers(String raw_material_id) {
        Query query = em.createQuery("SELECT s FROM Supplier s,RawMaterialDetail rmD WHERE s.supplier_id = rmD.supplier.supplier_id AND s.supplier_status = ?1 AND rmD.rawMaterial.raw_material_id=?2");
        
        query.setParameter(1, Supplier.SupplierStatus.valueOf("Active"));
        query.setParameter(2, Long.valueOf(raw_material_id));
        return (Collection<Supplier>) query.getResultList();
    }
    
    @Override
    public String deleteSupplier(String supplierID, String raw_material_id) {
        String content = "";
        Supplier s = find(Long.valueOf(supplierID));
        RawMaterial rm = rmFacade.find(Long.valueOf(raw_material_id));
        Query query = em.createQuery("SELECT rmD FROM Supplier s,RawMaterialDetail rmD WHERE rmD.supplier.supplier_id= s.supplier_id AND s.supplier_id= ?1 AND rmD.rawMaterial.raw_material_id=?2");
        
        query.setParameter(1, Long.valueOf(supplierID));
        query.setParameter(2, Long.valueOf(raw_material_id));
        RawMaterialDetail rmDetail = (RawMaterialDetail) query.getSingleResult();
       
        if(rmDetail.getOrderPlaced().size() >0){
             content = "Deletion not allowed. An order exists under " +s.getSupplier_name() + ".";
        }
        else{
            s.setSupplier_status(Supplier.SupplierStatus.Inactive);  
            edit(s); // want to keep record of raw material details?
            content = s.getSupplier_name()+ " has been deleted successfully.";
        } 
        return content;
     }
}

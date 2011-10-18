/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.RawMaterial;

/**
 *
 * @author alyssia
 */
@Local
public interface RawMaterialFacadeLocal {
    
    void create(RawMaterial rawMaterial);

    void edit(RawMaterial rawMaterial);

    void remove(RawMaterial rawMaterial);

    RawMaterial find(Object id);

    List<RawMaterial> findAll();

    List<RawMaterial> findRange(int[] range);

    int count();
    
    boolean checkRMNameExist(String mat_name);
    
    public int countFilteredRawMaterial(int page, int rows, String sort, String order, String rmID, String rmName);
            
    public Collection findFilteredRawMaterial(int page, int rows, String sort, String order, String rmID, String rmName);
    
    public Collection findFilteredRawMaterials(String rmID);
}

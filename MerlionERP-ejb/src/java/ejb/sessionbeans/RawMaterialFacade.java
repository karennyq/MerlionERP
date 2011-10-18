/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.RawMaterialFacadeLocal;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.RawMaterial;

/**
 *
 * @author alyssia
 */
@Stateless
public class RawMaterialFacade extends AbstractFacade<RawMaterial> implements RawMaterialFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public RawMaterialFacade() {
        super(RawMaterial.class);
    }

    @Override
    public int countFilteredRawMaterial(int page, int rows, String sort, String order, String rmID, String rmName) {
        String queryStr = "SELECT rm FROM RawMaterial rm WHERE rm.raw_material_id LIKE ?1 AND rm.mat_name LIKE ?2 ORDER BY rm." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + rmID + "%");
        query.setParameter(2, "%" + rmName + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredRawMaterial(int page, int rows, String sort, String order, String rmID, String rmName) {

        String queryStr =  "SELECT rm FROM RawMaterial rm WHERE rm.raw_material_id LIKE ?1 AND rm.mat_name LIKE ?2 ORDER BY rm." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + rmID + "%");
        query.setParameter(2, "%" + rmName + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<RawMaterial>) query.getResultList();
    }

    @Override
    public boolean checkRMNameExist(String mat_name) {
        Query query = em.createQuery("SELECT rm FROM RawMaterial rm WHERE rm.mat_name=?1");
        query.setParameter(1, mat_name);

        if (!query.getResultList().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public Collection findFilteredRawMaterials(String matName) {
        
        Query query = em.createQuery("SELECT rm FROM RawMaterial rm WHERE rm.mat_name LIKE ?1");
        query.setParameter(1, "%" + matName + "%");
        
        return (Collection<RawMaterial>) query.getResultList();
    }
       
}

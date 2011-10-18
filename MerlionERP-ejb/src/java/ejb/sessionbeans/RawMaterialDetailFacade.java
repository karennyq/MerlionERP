/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.RawMaterialDetailFacadeLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.persistence.RawMaterialDetail;

/**
 *
 * @author alyssia
 */
@Stateless
public class RawMaterialDetailFacade extends AbstractFacade<RawMaterialDetail> implements RawMaterialDetailFacadeLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public RawMaterialDetailFacade() {
        super(RawMaterialDetail.class);
    }
    
}

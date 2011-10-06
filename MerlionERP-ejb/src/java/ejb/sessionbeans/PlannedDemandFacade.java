/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.PlannedDemandFacadeLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.persistence.PlannedDemand;

/**
 *
 * @author Randy
 */
@Stateless
public class PlannedDemandFacade extends AbstractFacade<PlannedDemand> implements PlannedDemandFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PlannedDemandFacade() {
        super(PlannedDemand.class);
    }
    
}

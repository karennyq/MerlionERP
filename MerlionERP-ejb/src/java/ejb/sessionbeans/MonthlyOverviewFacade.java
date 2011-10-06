/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.MonthlyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.PlannedDemandFacadeLocal;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;
import org.persistence.Product;

/**
 *
 * @author Randy
 */
@Stateless
public class MonthlyOverviewFacade extends AbstractFacade<MonthlyOverview> implements MonthlyOverviewFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    
    @EJB
    ProductFacadeLocal pFacade;
    
    @EJB
    PlannedDemandFacadeLocal pdFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public MonthlyOverviewFacade() {
        super(MonthlyOverview.class);
    }

    @Override
    public boolean checkPPDone(String month, Integer year) {
        Query query = em.createQuery("SELECT mo FROM MonthlyOverview mo WHERE mo.month=?1 AND mo.year=?2");
        query.setParameter(1, month);
        query.setParameter(2, year);
        
        if (query.getResultList().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void createMO(MonthlyOverview mo, ArrayList<PlannedDemand> pdList) {
        create(mo);
        
        for (PlannedDemand pd: pdList) {
            pdFacade.create(pd);
            mo.getPlannedDemand().add(pd);
        }
        
        edit(mo);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.MonthlyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.PlannedDemandFacadeLocal;
import ejb.sessionbeans.interfaces.ProductFacadeLocal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;

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
    public MonthlyOverview createMO(MonthlyOverview m, ArrayList<PlannedDemand> pdList) {
        //create planned demand record
        for (PlannedDemand mPD: pdList) {
            mPD.setOt_boxes_to_produce(0.00);
            mPD.setOt_hours_needed(0.00);
            pdFacade.create(mPD);
            m.getPlannedDemand().add(mPD);
        }
        
        create(m);
        return m;
    }

    @Override
    public void updateMO(MonthlyOverview m, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList) {
        NumberFormat formatter = new DecimalFormat("0.00");
        
        for (PlannedDemand delPD : delPDList) {
            for (PlannedDemand originalPD : m.getPlannedDemand()) {
                if (delPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    m.getPlannedDemand().remove(originalPD);
                    edit(m);
                    pdFacade.remove(originalPD);
                }
            }
        }
        
        for (PlannedDemand addPD : addPDList) {
            for (PlannedDemand originalPD : m.getPlannedDemand()) {
                if (addPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() + addPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand subPD : subPDList) {
            for (PlannedDemand originalPD : m.getPlannedDemand()) {
                if (subPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - subPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand newPD : newPDList) {
            PlannedDemand pd = new PlannedDemand();
            pd.setProduct(newPD.getProduct());
            pd.setBoxes_to_produce(newPD.getBoxes_to_produce());
            pd.setHours_needed(Double.parseDouble(formatter.format(pd.getBoxes_to_produce() / pd.getProduct().getProduction_capacity())));
            pd.setOt_boxes_to_produce(0.00);
            pd.setOt_hours_needed(0.00);
            pdFacade.create(pd);
            m.getPlannedDemand().add(pd);
            edit(m);
        }
        
        double usedCapacity = 0;
        
        for (PlannedDemand pd : m.getPlannedDemand()) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        m.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / m.getCapacity())));
        edit(m);
    }
}

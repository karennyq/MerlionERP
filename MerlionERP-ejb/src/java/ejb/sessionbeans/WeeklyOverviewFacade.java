/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DailyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.MonthlyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.PlannedDemandFacadeLocal;
import ejb.sessionbeans.interfaces.PublicHolidayFacadeLocal;
import ejb.sessionbeans.interfaces.WeeklyOverviewFacadeLocal;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.persistence.DailyOverview;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;
import org.persistence.PublicHoliday;
import org.persistence.WeeklyOverview;

/**
 *
 * @author Randy
 */
@Stateless
public class WeeklyOverviewFacade extends AbstractFacade<WeeklyOverview> implements WeeklyOverviewFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    
    @EJB
    PublicHolidayFacadeLocal phFacade;
    
    @EJB
    PlannedDemandFacadeLocal pdFacade;
    
    @EJB
    MonthlyOverviewFacadeLocal moFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public WeeklyOverviewFacade() {
        super(WeeklyOverview.class);
    }
    
    @Override
    public MonthlyOverview createWO(MonthlyOverview mo, ArrayList<PlannedDemand> mthlyPDList) {
        Calendar cal = Calendar.getInstance();
        cal.set(mo.getYear(), Integer.parseInt(mo.getMonth())-1, 1);
        
        for (int i=0; i<cal.getActualMaximum(Calendar.WEEK_OF_MONTH); i++) {
            WeeklyOverview wo = new WeeklyOverview();
            wo.setWeek(i+1);
            
            int workingDays = 0;
            while (cal.get(Calendar.WEEK_OF_MONTH) == (i+1)) {
                if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    PublicHoliday ph = phFacade.getPH(cal.getTime());
                    if (ph == null) {
                        workingDays = workingDays + 1;
                    }
                }
                cal.add(Calendar.DATE, 1);
            }
            
            wo.setWorking_days(workingDays);
            wo.setCapacity(mo.getCapacity() / mo.getWorking_days() * workingDays);
            
            double usedCapacity = 0;
            ArrayList<PlannedDemand> wklyPDList = new ArrayList<PlannedDemand>();
            for (PlannedDemand monthlyPD: mthlyPDList) {
                PlannedDemand weeklyPD = new PlannedDemand();
                
                weeklyPD.setProduct(monthlyPD.getProduct());
                
                int weeklyAmt = (int) Math.ceil((double)monthlyPD.getBoxes_to_produce() / (double)mo.getWorking_days() * (double)workingDays);
                weeklyPD.setBoxes_to_produce(weeklyAmt);
                
                Double weeklyHour = (double) weeklyAmt / monthlyPD.getProduct().getProduction_capacity();
                weeklyPD.setHours_needed(weeklyHour);
                usedCapacity = usedCapacity + weeklyHour;
                
                weeklyPD.setOt_boxes_to_produce(0);
                weeklyPD.setOt_hours_needed(0.00);
                
                pdFacade.create(weeklyPD);
                
                wklyPDList.add(weeklyPD);
            }
            
            wo.setPlannedDemand(wklyPDList);
            wo.setUtilization(usedCapacity * 100 / wo.getCapacity());
            
            wo.setOt_capacity(0.00);
            wo.setOt_utilization(0.00);
            wo.setDailyOverviews(new ArrayList());
            
            wo.setMonthlyOverview(mo);
            create(wo);
            
            mo.getWeeklyOverviews().add(wo);
            moFacade.edit(mo);
        }
        
        return mo;
    }
    
}

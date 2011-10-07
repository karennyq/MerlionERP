/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DailyOverviewFacadeLocal;
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
public class DailyOverviewFacade extends AbstractFacade<DailyOverview> implements DailyOverviewFacadeLocal {
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    
    @EJB
    PublicHolidayFacadeLocal phFacade;
    
    @EJB
    PlannedDemandFacadeLocal pdFacade;
    
    @EJB
    WeeklyOverviewFacadeLocal woFacade;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DailyOverviewFacade() {
        super(DailyOverview.class);
    }
    
    @Override
    public void createDO(MonthlyOverview mo, ArrayList<PlannedDemand> mthlyPDList) {
        Calendar cal = Calendar.getInstance();
        cal.set(mo.getYear(), Integer.parseInt(mo.getMonth())-1, 1);       
        
        for (WeeklyOverview wo: mo.getWeeklyOverviews()) {
            while (cal.get(Calendar.WEEK_OF_MONTH) == wo.getWeek()) {
                if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    PublicHoliday ph = phFacade.getPH(cal.getTime());
                    DailyOverview dailyView = new DailyOverview();
                    ArrayList<PlannedDemand> dailyPDList = new ArrayList<PlannedDemand>();
                    double usedCapacity = 0;
                    //if not a ph
                    if (ph == null) {
                        dailyView.setCapacity(wo.getCapacity() / wo.getWorking_days());
                        
                        for (PlannedDemand weeklyPD: wo.getPlannedDemand()) {
                            PlannedDemand dailyPD = new PlannedDemand();
                            dailyPD.setProduct(weeklyPD.getProduct());

                            int dailyAmt = (int) Math.ceil(weeklyPD.getBoxes_to_produce() / (double)wo.getWorking_days());
                            dailyPD.setBoxes_to_produce(dailyAmt);

                            Double dailyHour = (double) dailyAmt / weeklyPD.getProduct().getProduction_capacity();
                            dailyPD.setHours_needed(dailyHour);
                            usedCapacity = usedCapacity + dailyHour;

                            dailyPD.setOt_boxes_to_produce(0);
                            dailyPD.setOt_hours_needed(0.00);

                            pdFacade.create(dailyPD);
                            dailyPDList.add(dailyPD);
                        }
                        
                        dailyView.setPlannedDemand(dailyPDList);
                        dailyView.setUtilization(usedCapacity * 100 / dailyView.getCapacity());
                        
                        dailyView.setOt_capacity(0.00);
                        dailyView.setOt_utilization(0.00);
                        dailyView.setWorking_days(1);
                    } else {
                        dailyView.setPublicHoliday(ph);
                        dailyView.setWorking_days(0);
                    }
                    
                    dailyView.setWeeklyOverview(wo);
                    create(dailyView);
                    
                    wo.getDailyOverviews().add(dailyView);
                    woFacade.edit(wo);
                }
                cal.add(Calendar.DATE, 1);
            }
        }
    }
}

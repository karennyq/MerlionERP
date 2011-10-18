/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.DailyOverviewFacadeLocal;
import ejb.sessionbeans.interfaces.PlannedDemandFacadeLocal;
import ejb.sessionbeans.interfaces.PublicHolidayFacadeLocal;
import ejb.sessionbeans.interfaces.WeeklyOverviewFacadeLocal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    public void createDO(MonthlyOverview m) {
        NumberFormat formatter = new DecimalFormat("0.00");
        Calendar cal = Calendar.getInstance();
        cal.set(m.getYear(), Integer.parseInt(m.getMonth())-1, 1);       
        
        //loop for each week in month
        for (WeeklyOverview w: m.getWeeklyOverviews()) {
            //loop for number of days in week
            while (cal.get(Calendar.WEEK_OF_MONTH) == w.getWeek()) {
                //if not weekends
                if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    PublicHoliday ph = phFacade.getPH(cal.getTime());
                    DailyOverview d = new DailyOverview();
                    d.setPlannedDemand(new ArrayList());
                    d.setDay(cal.getTime());
                    
                    double usedCapacity = 0;
                    //if not public holiday
                    if (ph == null) {
                        d.setWorking_days(1);
                        d.setCapacity(w.getCapacity() / w.getWorking_days());
                        
                        for (PlannedDemand mPD: m.getPlannedDemand()) {
                            PlannedDemand dPD = new PlannedDemand();
                            
                            dPD.setProduct(mPD.getProduct());
                            dPD.setBoxes_to_produce(Math.ceil(mPD.getBoxes_to_produce() / m.getWorking_days()));
                            dPD.setHours_needed(Double.parseDouble(formatter.format(dPD.getBoxes_to_produce() / mPD.getProduct().getProduction_capacity())));
                            dPD.setOt_boxes_to_produce(0.00);
                            dPD.setOt_hours_needed(0.00);
                            
                            usedCapacity = usedCapacity + dPD.getHours_needed();
                            pdFacade.create(dPD);
                            d.getPlannedDemand().add(dPD);
                        }
                    } else {
                        d.setPublicHoliday(ph);
                        d.setWorking_days(0);
                    }
                    
                    d.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / d.getCapacity())));
                    d.setOt_capacity(w.getOt_capacity() / w.getWorking_days());
                    d.setOt_utilization(0.00);
                    d.setWeeklyOverview(w);
                    create(d);
                    
                    w.getDailyOverviews().add(d);
                    woFacade.edit(w);
                }
                cal.add(Calendar.DATE, 1);
            }
        }
    }

    @Override
    public void updateDOByMonth(DailyOverview d, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList) {
        NumberFormat formatter = new DecimalFormat("0.00");
        
        for (PlannedDemand delPD : delPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (delPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    d.getPlannedDemand().remove(originalPD);
                    edit(d);
                    pdFacade.remove(originalPD);
                }
            }
        }
        
        for (PlannedDemand addPD : addPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (addPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() + Math.ceil(addPD.getBoxes_to_produce() / d.getWeeklyOverview().getMonthlyOverview().getWorking_days()));
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand subPD : subPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (subPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - Math.ceil(subPD.getBoxes_to_produce() / d.getWeeklyOverview().getMonthlyOverview().getWorking_days()));
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand newPD : newPDList) {
            PlannedDemand pd = new PlannedDemand();
            pd.setProduct(newPD.getProduct());
            pd.setBoxes_to_produce(Math.ceil(newPD.getBoxes_to_produce() / d.getWeeklyOverview().getMonthlyOverview().getWorking_days()));
            pd.setHours_needed(Double.parseDouble(formatter.format(pd.getBoxes_to_produce() / pd.getProduct().getProduction_capacity())));
            pd.setOt_boxes_to_produce(0.00);
            pd.setOt_hours_needed(0.00);
            pdFacade.create(pd);
            d.getPlannedDemand().add(pd);
            edit(d);
        }
        
        double usedCapacity = 0;
        
        for (PlannedDemand pd : d.getPlannedDemand()) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        d.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / d.getCapacity())));
        edit(d);
    }
    
    @Override
    public void updateDOByDay(DailyOverview d, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList) {
        NumberFormat formatter = new DecimalFormat("0.00");
        
        for (PlannedDemand delPD : delPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (delPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    d.getPlannedDemand().remove(originalPD);
                    edit(d);
                    pdFacade.remove(originalPD);
                }
            }
        }
        
        for (PlannedDemand addPD : addPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (addPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() + addPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand subPD : subPDList) {
            for (PlannedDemand originalPD : d.getPlannedDemand()) {
                if (subPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - subPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }
        
        for (PlannedDemand newPD : newPDList) {
            newPD.setHours_needed(Double.parseDouble(formatter.format(newPD.getBoxes_to_produce()/newPD.getProduct().getProduction_capacity())));
            newPD.setOt_boxes_to_produce(0.00);
            newPD.setOt_hours_needed(0.00);
            pdFacade.create(newPD);
            d.getPlannedDemand().add(newPD);
            edit(d);
        }
        
        double usedCapacity = 0;
        
        for (PlannedDemand pd : d.getPlannedDemand()) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }
        
        d.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / d.getCapacity())));
        edit(d);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.MonthlyOverviewFacadeLocal;
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
import javax.persistence.Query;
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
    public MonthlyOverview createWO(MonthlyOverview m) {
        NumberFormat formatter = new DecimalFormat("0.00");
        Calendar cal = Calendar.getInstance();
        cal.set(m.getYear(), Integer.parseInt(m.getMonth())-1, 1);
        
        //loop for each week in the month
        for (int i=0; i<cal.getActualMaximum(Calendar.WEEK_OF_MONTH); i++) {
            WeeklyOverview w = new WeeklyOverview();
            w.setPlannedDemand(new ArrayList());
            w.setWeek(i+1);
            
            int workingDays = 0;
            //loop for number of days in week
            while (cal.get(Calendar.WEEK_OF_MONTH) == (i+1)) {
                //if not weekends
                if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    PublicHoliday ph = phFacade.getPH(cal.getTime());
                    //if not public holiday
                    if (ph == null) {
                        workingDays = workingDays + 1;
                    }
                }
                cal.add(Calendar.DATE, 1);
            }
            
            w.setWorking_days(workingDays);
            w.setCapacity(m.getCapacity() / m.getWorking_days() * workingDays);
            
            double usedCapacity = 0;
            //loop each item in monthly planned demand for breakdown purposes
            for (PlannedDemand mPD: m.getPlannedDemand()) {
                PlannedDemand wPD = new PlannedDemand();
                
                wPD.setProduct(mPD.getProduct());
                wPD.setBoxes_to_produce(Math.ceil(mPD.getBoxes_to_produce() / m.getWorking_days() * workingDays));
                wPD.setHours_needed(Double.parseDouble(formatter.format(wPD.getBoxes_to_produce() / mPD.getProduct().getProduction_capacity())));
                wPD.setOt_boxes_to_produce(0.00);
                wPD.setOt_hours_needed(0.00);
                
                usedCapacity = usedCapacity + wPD.getHours_needed();
                
                pdFacade.create(wPD);
                w.getPlannedDemand().add(wPD);
            }
            
            w.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / w.getCapacity())));
            w.setOt_capacity(m.getOt_capacity() / m.getWorking_days() * workingDays);
            w.setOt_utilization(0.00);
            w.setDailyOverviews(new ArrayList());
            w.setMonthlyOverview(m);
            
            create(w);
            
            m.getWeeklyOverviews().add(w);
            moFacade.edit(m);
        }
        return m;
    }

    @Override
    public ArrayList<WeeklyOverview> findWeekInMonth(Long mo_id) {
        Query query;
        query = em.createQuery("SELECT wo FROM WeeklyOverview wo WHERE wo.monthlyOverview.id=?1");
        query.setParameter(1, mo_id);
        
        ArrayList<WeeklyOverview> woList = new ArrayList<WeeklyOverview>();
        
        for (Object o : query.getResultList()) {
            WeeklyOverview wo = (WeeklyOverview) o;
            woList.add(wo);
        }
        
        return woList;
    }

    @Override
    public void updateWOByDay(WeeklyOverview w, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList) {
        NumberFormat formatter = new DecimalFormat("0.00");
        
        for (PlannedDemand delPD : delPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (delPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    w.getPlannedDemand().remove(originalPD);
                    edit(w);
                    pdFacade.remove(originalPD);
                }
            }
        }

        for (PlannedDemand addPD : addPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (addPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() + addPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }

        for (PlannedDemand subPD : subPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (subPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - subPD.getBoxes_to_produce());
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }

        for (PlannedDemand newPD : newPDList) {
            newPD.setPlanned_dmd_id(null);
            newPD.setHours_needed(Double.parseDouble(formatter.format(newPD.getBoxes_to_produce()/newPD.getProduct().getProduction_capacity())));
            newPD.setOt_boxes_to_produce(0.00);
            newPD.setOt_hours_needed(0.00);
            pdFacade.create(newPD);
            w.getPlannedDemand().add(newPD);
            edit(w);
        }

        double usedCapacity = 0;

        for (PlannedDemand pd : w.getPlannedDemand()) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }

        w.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / w.getCapacity())));
        edit(w);
    }
    
    @Override
    public void updateWOByMonth(WeeklyOverview w, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList) {
        NumberFormat formatter = new DecimalFormat("0.00");
        
        for (PlannedDemand delPD : delPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (delPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    w.getPlannedDemand().remove(originalPD);
                    edit(w);
                    pdFacade.remove(originalPD);
                }
            }
        }

        for (PlannedDemand addPD : addPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (addPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() + Math.ceil(addPD.getBoxes_to_produce() / w.getMonthlyOverview().getWorking_days() * w.getWorking_days()));
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }

        for (PlannedDemand subPD : subPDList) {
            for (PlannedDemand originalPD : w.getPlannedDemand()) {
                if (subPD.getProduct().getProduct_id() == originalPD.getProduct().getProduct_id()) {
                    originalPD.setBoxes_to_produce(originalPD.getBoxes_to_produce() - Math.ceil(subPD.getBoxes_to_produce() / w.getMonthlyOverview().getWorking_days() * w.getWorking_days()));
                    originalPD.setHours_needed(Double.parseDouble(formatter.format(originalPD.getBoxes_to_produce()/originalPD.getProduct().getProduction_capacity())));
                    pdFacade.edit(originalPD);
                    break;
                }
            }
        }

        for (PlannedDemand newPD : newPDList) {
            PlannedDemand pd = new PlannedDemand();
            pd.setProduct(newPD.getProduct());
            pd.setBoxes_to_produce(Math.ceil(newPD.getBoxes_to_produce() / w.getMonthlyOverview().getWorking_days() * w.getWorking_days()));
            pd.setHours_needed(Double.parseDouble(formatter.format(pd.getBoxes_to_produce() / pd.getProduct().getProduction_capacity())));
            pd.setOt_boxes_to_produce(0.00);
            pd.setOt_hours_needed(0.00);
            pdFacade.create(pd);
            w.getPlannedDemand().add(pd);
            edit(w);
        }

        double usedCapacity = 0;

        for (PlannedDemand pd : w.getPlannedDemand()) {
            usedCapacity = usedCapacity + pd.getHours_needed();
        }

        w.setUtilization(Double.parseDouble(formatter.format(usedCapacity * 100 / w.getCapacity())));
        edit(w);
    }
}

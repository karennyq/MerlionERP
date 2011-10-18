/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.persistence.DailyOverview;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;

/**
 *
 * @author Randy
 */
@Local
public interface DailyOverviewFacadeLocal {

    void create(DailyOverview dailyOverview);

    void edit(DailyOverview dailyOverview);

    void remove(DailyOverview dailyOverview);

    DailyOverview find(Object id);

    List<DailyOverview> findAll();

    List<DailyOverview> findRange(int[] range);

    int count();

    void createDO(MonthlyOverview mo);

    public void updateDOByMonth(DailyOverview d, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList);
    
    public void updateDOByDay(DailyOverview d, ArrayList<PlannedDemand> addPDList, ArrayList<PlannedDemand> subPDList, ArrayList<PlannedDemand> newPDList, ArrayList<PlannedDemand> delPDList);
    
}

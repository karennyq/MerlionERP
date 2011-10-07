/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import org.persistence.MonthlyOverview;
import org.persistence.PlannedDemand;
import org.persistence.WeeklyOverview;

/**
 *
 * @author Randy
 */
@Local
public interface WeeklyOverviewFacadeLocal {

    void create(WeeklyOverview weeklyOverview);

    void edit(WeeklyOverview weeklyOverview);

    void remove(WeeklyOverview weeklyOverview);

    WeeklyOverview find(Object id);

    List<WeeklyOverview> findAll();

    List<WeeklyOverview> findRange(int[] range);

    int count();

    MonthlyOverview createWO(MonthlyOverview mo, ArrayList<PlannedDemand> pdList);
    
}
